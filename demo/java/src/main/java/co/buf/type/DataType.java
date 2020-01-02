package co.buf.type;

import co.buf.util.ByteUtil;

public class DataType implements Cloneable {
    protected byte[] byteVal = null;

    public void setVal(Object val) {

    }

    public byte[] getByteVal() {
        return byteVal;
    }

    public byte[] packByteVal(int tag, int type) {
        if (this.byteVal != null) {
            if (tag >= 15) {
                byte[] ret = new byte[this.byteVal.length + 2];
                ret[0] = (byte) ((type << 4) | 15);
                ret[1] = (byte) (tag);
                for (int i = 2; i < ret.length; i++) {
                    ret[i] = this.byteVal[i - 2];
                }
                return ret;
            } else {
                byte[] ret = new byte[this.byteVal.length + 1];
                ret[0] = (byte) ((type << 4) | tag);
                for (int i = 1; i < ret.length; i++) {
                    ret[i] = this.byteVal[i - 1];
                }
                return ret;
            }
        }

        return null;
    }

    public void setByteVal(Object val, int type) {
        if (type == CoType.CO_INT) {
            this.byteVal = ByteUtil.int2Byte((int) val);
        } else if (type == CoType.CO_LONG) {
            this.byteVal = ByteUtil.long2Byte((long) val);
        } else if (type == CoType.CO_STRING) {
            String str = (String) val;
            if (str == null) {
                this.byteVal = null;
            } else {
                byte[] strBytes = str.getBytes();
                this.byteVal = ByteUtil.byteMerge(ByteUtil.int2Byte(strBytes.length), strBytes);
            }
        } else if (type == CoType.CO_STRUCT || type == CoType.CO_LIST) {
            byte[] strBytes = (byte[]) val;
            this.byteVal = ByteUtil.byteMerge(ByteUtil.int2Byte(strBytes.length), strBytes);
        } else if (type == CoType.CO_BOOL) {
            byte[] ret = new byte[1];
            if ((boolean) val == true) {
                ret[0] = (byte) 1;
            } else {
                ret[0] = (byte) 0;
            }
            this.byteVal = ret;
        }
    }
}
