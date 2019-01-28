package co.buf.type;

import co.buf.parser.CoParser;

public class CoLong extends DataType
{
    public long val;

    public void setVal(long val)
    {
        this.val = val;
    }

    public long getVal() {
        return val;
    }

    public byte[] toByteVal(int tag, boolean need) {
        this.setByteVal(this.val, CoType.CO_LONG);
        return this.packByteVal(tag, CoType.CO_LONG, need);
    }

    public CoLong parseByteVal(CoParser parser, int tag) {
        this.val = (long) parser.getVal(tag);
        return this;
    }
}
