package co.buf.type;

import java.util.Map;

public class CoMap<T, U> extends DataType {
    public Map<T, U> val;

    public Map<T, U> getVal() {
        return val;
    }

    public void setVal(Map<T, U> val) {
        this.val = val;
    }
}
