package co.buf.type;

import co.buf.parser.CoParser;

public class CoBool extends DataType
{
    public boolean val;

    public void setVal(boolean val)
    {
        this.val = val;
    }

    public byte[] toByteVal(int tag, boolean need) {
        this.setByteVal(this.val, CoType.CO_BOOL);
        return this.packByteVal(tag, CoType.CO_BOOL, need);
    }

    public boolean getVal() {
        return val;
    }

    public CoBool parseByteVal(CoParser parser, int tag) {
        this.val = (boolean) parser.getVal(tag);
        return this;
    }
}
