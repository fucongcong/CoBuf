package co.buf.parser;

import co.buf.type.CoType;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

public class CoParser {
    public Map parserData;

    public CoParser(byte[] data) {
        ByteBuffer buf = ByteBuffer.allocate(data.length);
        buf.put(data);

        parserData = new HashMap();

        int offset = 0;
        while (offset < data.length) {
            int typeAndTag = (int) buf.get(offset);
            offset++;

            int type = typeAndTag >> 4;
            int tag = typeAndTag - (type << 4);

            if (tag == 15) {
                tag = (int) buf.get(offset);
                offset++;
            }

            if (type == CoType.CO_INT) {
                parserData.put(tag, buf.getInt(offset));
                offset = offset + 4;
            } else if (type == CoType.CO_LONG) {
                parserData.put(tag, buf.getLong(offset));
                offset = offset + 8;
            } else if (type == CoType.CO_STRING) {
                int len = buf.getInt(offset);
                offset = offset + 4;

                byte[] str = new byte[len];
                for (int i = 0; i < len; i++) {
                    str[i] = buf.get(offset);
                    offset++;
                }

                parserData.put(tag, new String(str));
            } else if (type == CoType.CO_STRUCT || type == CoType.CO_LIST) {
                int len = buf.getInt(offset);
                offset = offset + 4;

                byte[] str = new byte[len];
                for (int i = 0; i < len; i++) {
                    str[i] = buf.get(offset);
                    offset++;
                }

                parserData.put(tag, str);
            } else if (type == CoType.CO_BOOL) {
                if ((int) buf.get(offset) == 1) {
                    parserData.put(tag, true);
                } else {
                    parserData.put(tag, false);
                }
                offset++;
            }
        }
    }

    public Object getVal(int tag) {
        if (parserData.containsKey(tag)) {
            return parserData.get(tag);
        } else {
            return null;
        }
    }
}
