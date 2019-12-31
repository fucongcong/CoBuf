package co.buf.type;

import co.buf.parser.CoParser;

public class CoList extends DataType {

    public DataType val;

    protected DataType dataType;

    public CoList(DataType type) {
        this.dataType = type;
    }

    public void setVal(DataType val) {
        this.val = val;
    }

    public DataType getVal() {
        return val;
    }

    public byte[] toByteVal(int tag, boolean need) {
        this.setByteVal(this.val, CoType.CO_LIST);
        return this.packByteVal(tag, CoType.CO_LIST, need);
    }

    public CoList parseByteVal(CoParser parser, int tag) {
        //this.val = (long) parser.getVal(tag);
        return this;
    }
}
