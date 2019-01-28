package co.buf.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class ByteUtil {
    public static byte[] int2Byte(int val) {
        ByteBuffer buf = ByteBuffer.allocate(4);
        buf.order(ByteOrder.BIG_ENDIAN);
        buf.putInt(val);
        return buf.array();
    }

    public static byte[] long2Byte(long val) {
        ByteBuffer buf = ByteBuffer.allocate(8);
        buf.order(ByteOrder.BIG_ENDIAN);
        buf.putLong(val);
        return buf.array();
    }

    public static byte[] byteMerge(byte[] b1, byte[] b2) {
        if (b1 == null) {
            return b2;
        }

        if (b2 == null) {
            return b1;
        }

        byte[] b3 = new byte[b1.length + b2.length];
        System.arraycopy(b1, 0, b3, 0, b1.length);
        System.arraycopy(b2, 0, b3, b1.length, b2.length);
        return b3;
    }
}
