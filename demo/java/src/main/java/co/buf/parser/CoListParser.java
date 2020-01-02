package co.buf.parser;

import co.buf.type.CoStruct;
import co.buf.type.CoType;
import co.buf.type.DataType;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class CoListParser<T> {
    public List<T> parserData = null;

    public CoListParser(byte[] data, int type) {
        ByteBuffer buf = ByteBuffer.allocate(data.length);
        buf.put(data);

        switch (type) {
            case CoType.CO_INT:
                parserIntData(buf, data.length);
            case CoType.CO_BOOL:
                parserBoolData(buf, data.length);
            case CoType.CO_LONG:
                parserLongData(buf, data.length);
            case CoType.CO_STRING:
                parserStringData(buf, data.length);
        }
    }

    public CoListParser(byte[] data, int type, DataType val) {
        ByteBuffer buf = ByteBuffer.allocate(data.length);
        buf.put(data);

        switch (type) {
            case CoType.CO_INT:
                parserIntData(buf, data.length);
            case CoType.CO_BOOL:
                parserBoolData(buf, data.length);
            case CoType.CO_LONG:
                parserLongData(buf, data.length);
            case CoType.CO_STRING:
                parserStringData(buf, data.length);
            case CoType.CO_STRUCT:
                parserStructData(buf, data.length, (CoStruct) val);
        }
    }

    private void parserStructData(ByteBuffer buf, int length, CoStruct val) {
        List<T> data = new ArrayList<>();

        int offset = 0;
        while (offset < length) {
            int len = buf.getInt(offset);
            offset = offset + 4;

            byte[] str = new byte[len];
            for (int i = 0; i < len; i++) {
                str[i] = buf.get(offset);
                offset++;
            }

            CoStruct v = val.clone();
            v.getBuilder().parse(str, v);
            data.add((T) v);
        }

        parserData = data;
    }

    private void parserStringData(ByteBuffer buf, int length) {
        List<String> data = new ArrayList<>();

        int offset = 0;
        while (offset < length) {
            int len = buf.getInt(offset);
            offset = offset + 4;

            byte[] str = new byte[len];
            for (int i = 0; i < len; i++) {
                str[i] = buf.get(offset);
                offset++;
            }

            data.add(new String(str));
        }

        parserData = (List<T>) data;
    }

    private void parserLongData(ByteBuffer buf, int length) {
        List<Long> data = new ArrayList<>();

        int offset = 0;
        while (offset < length) {
            data.add(buf.getLong(offset));
            offset = offset + 8;
        }

        parserData = (List<T>) data;
    }

    private void parserBoolData(ByteBuffer buf, int length) {
        List<Boolean> data = new ArrayList<>();

        int offset = 0;
        while (offset < length) {
            if ((int) buf.get(offset) == 1) {
                data.add(true);
            } else {
                data.add(false);
            }
            offset++;
        }

        parserData = (List<T>) data;
    }

    public List<T> getVal() {
        return parserData;
    }

    private void parserIntData(ByteBuffer buf, int length) {
        List<Integer> data = new ArrayList<>();

        int offset = 0;
        while (offset < length) {
            data.add(buf.getInt(offset));
            offset = offset + 4;
        }

        parserData = (List<T>) data;
    }
}
