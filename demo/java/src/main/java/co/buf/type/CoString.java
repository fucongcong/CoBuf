package co.buf.type;

import co.buf.parser.CoParser;

public class CoString extends DataType {
    public String val;

    public void setVal(String val)
    {
        this.val = val;
    }

    public String getVal() {
        return val;
    }

    public byte[] toByteVal(int tag, boolean need) {
        this.setByteVal(this.val, CoType.CO_STRING);
        return this.packByteVal(tag, CoType.CO_STRING, need);
    }

    public CoString parseByteVal(CoParser parser, int tag) {
        this.val = (String) parser.getVal(tag);
        return this;
    }
}
