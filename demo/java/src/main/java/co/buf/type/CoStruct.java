package co.buf.type;

import co.buf.parser.CoBuilder;
import co.buf.parser.CoParser;

public class CoStruct extends DataType {
    public CoStruct val;

    protected CoBuilder builder;

    public void setVal(CoStruct val)
    {
        this.val = val;
    }

    public CoStruct getVal() {
        return val;
    }

    public byte[] toByteVal(int tag, boolean need) {
        this.setByteVal(this.val.builder.toByteArray(), CoType.CO_STRUCT);
        return this.packByteVal(tag, CoType.CO_STRUCT, need);
    }

    public CoStruct parseByteVal(CoParser parser, int tag, CoStruct val) {
        byte[] structBytes = (byte[]) parser.getVal(tag);
        val.builder.parse(structBytes, val);
        this.val = val;
        return this;
    }
}
