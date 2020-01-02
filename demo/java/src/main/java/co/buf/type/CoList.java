package co.buf.type;

import co.buf.parser.CoListParser;
import co.buf.parser.CoParser;
import co.buf.util.ByteUtil;

import java.util.List;

public class CoList<T> extends DataType {

    public List<T> val;

    protected DataType dataType;

    protected int type;

    public CoList(DataType dataType, int type) {
        this.dataType = dataType;
        this.type = type;
    }

    public void setVal(List<T> val) {
        this.val = val;
    }

    public List<T> getVal() {
        return val;
    }

    public byte[] toByteVal(int tag, boolean need) {
        byte[] bytesVal = null;

        if (type == CoType.CO_STRUCT) {
            for (T v : this.val) {
                CoStruct coStruct = (CoStruct) v;
                dataType.setByteVal(coStruct.builder.toByteArray(), this.type);
                byte[] strBytes = dataType.getByteVal();

                bytesVal = ByteUtil.byteMerge(bytesVal, strBytes);
            }
        } else {
            for (T v : this.val) {
                dataType.setVal(v);
                dataType.setByteVal(v, this.type);
                byte[] strBytes = dataType.getByteVal();

                bytesVal = ByteUtil.byteMerge(bytesVal, strBytes);
            }
        }

        this.setByteVal(bytesVal, CoType.CO_LIST);
        return this.packByteVal(tag, CoType.CO_LIST);
    }

    public CoList parseByteVal(CoParser parser, int tag) {
        byte[] listBytes = (byte[]) parser.getVal(tag);
        //不同类型解包方式不同
        if (type == CoType.CO_STRUCT) {
            CoListParser<T> coListParser = new CoListParser<T>(listBytes, type, dataType);
            this.val = coListParser.getVal();
        } else {
            CoListParser<T> coListParser = new CoListParser<>(listBytes, type);
            this.val = coListParser.getVal();
        }

        return this;
    }
}
