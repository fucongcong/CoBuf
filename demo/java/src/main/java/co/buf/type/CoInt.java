package co.buf.type;

import co.buf.parser.CoParser;

public class CoInt extends DataType
{
    public int val;

    public void setVal(int val)
    {
        this.val = val;
    }

    public byte[] toByteVal(int tag, boolean need) {
        this.setByteVal(this.val, CoType.CO_INT);
        return this.packByteVal(tag, CoType.CO_INT);
    }

    public int getVal() {
        return val;
    }

    public CoInt parseByteVal(CoParser parser, int tag) {
        this.val = (int) parser.getVal(tag);
        return this;
    }
}
