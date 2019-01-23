import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashMap;
import java.util.Map;

public class Demo1 {
    public static void main(String[] args) {
        Company company = new Company();
        CompanyBuilder builder = company.getBuilder();
        builder.setId(1);
        builder.setCode(1111);
        builder.setName("coco");
        builder.setAddr("aaaaaaaa");

        byte[] data = builder.toByteArray();

        Company company1 = new Company();
        Company cpy = company1.parseFrom(data);
        cpy.getId();
        cpy.getCode();
    }
}

class CoType
{
    public static final int CO_INT = 1;
    public static final int CO_LONG = 2;
    public static final int CO_STRING = 3;
}

class Util
{
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
class DataType
{
    protected byte[] byteVal = null;

    public void setVal(Object val)
    {

    }

    public byte[] packByteVal(int tag, int type, boolean need) {
        if(this.byteVal != null) {
            if (tag >= 15) {
                byte[] ret = new byte[this.byteVal.length + 2];
                ret[0] = (byte)((type << 4) | 15);
                ret[1] = (byte)(tag);
                for (int i = 2; i < ret.length; i++)
                {
                    ret[i] = this.byteVal[i - 2];
                }
                return ret;
            } else {
                byte[] ret = new byte[this.byteVal.length + 1];
                ret[0] = (byte)((type << 4) | tag);
                for (int i = 1; i < ret.length; i++)
                {
                    ret[i] = this.byteVal[i - 1];
                }
                return ret;
            }
        }

        return null;
    }

    public void setByteVal(Object val, int type) {
        if (type == CoType.CO_INT) {
            this.byteVal = Util.int2Byte((int) val);
        } else if (type == CoType.CO_LONG) {
            this.byteVal = Util.long2Byte((long) val);
        } else if (type == CoType.CO_STRING) {
            String str = (String) val;
            if (str == null) {
                this.byteVal = null;
            } else {
                byte[] strBytes = str.getBytes();
                this.byteVal = Util.byteMerge(Util.int2Byte(strBytes.length), strBytes);
            }
        }
    }
}

class CoInt extends DataType
{
    public int val;

    public void setVal(int val)
    {
        this.val = val;
    }

    public byte[] toByteVal(int tag, boolean need) {
        this.setByteVal(this.val, CoType.CO_INT);
        return this.packByteVal(tag, CoType.CO_INT, need);
    }

    public int getVal() {
        return val;
    }

    public CoInt parseByteVal(Parser parser, int tag) {
        this.val = (int) parser.getVal(tag);
        return this;
    }
}

class CoLong extends DataType
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

    public CoLong parseByteVal(Parser parser, int tag) {
        this.val = (long) parser.getVal(tag);
        return this;
    }
}

class CoString extends DataType {
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

    public CoString parseByteVal(Parser parser, int tag) {
        this.val = (String) parser.getVal(tag);
        return this;
    }
}

class Company {
    public int id;
    public long code;
    public String name;
    public String addr;
    protected CompanyBuilder builder;

    Company() {
        this.builder = new CompanyBuilder();
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCode(long code) {
        this.code = code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public int getId() {
        return id;
    }

    public long getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getAddr() {
        return addr;
    }

    public CompanyBuilder getBuilder() {
        return builder;
    }

    public Company parseFrom(byte[] data) {
        this.builder.parse(data, this);
        return this;
    }
}

class CompanyBuilder {
    public CoInt id;
    public CoLong code;
    public CoString name;
    public CoString addr;

    CompanyBuilder()
    {
        this.id = new CoInt();
        this.code = new CoLong();
        this.name = new CoString();
        this.addr = new CoString();
    }

    public void setId(int id)
    {
        this.id.setVal(id);
    }

    public void setCode(long code)
    {
        this.code.setVal(code);
    }

    public void setName(String name)
    {
        this.name.setVal(name);
    }

    public void setAddr(String addr)
    {
        this.addr.setVal(addr);
    }

    public byte[] toByteArray() {
        byte[] ret = this.id.toByteVal(18, true);
        ret = Util.byteMerge(ret, this.code.toByteVal(2, true));
        ret = Util.byteMerge(ret, this.name.toByteVal(3, false));
        ret = Util.byteMerge(ret, this.addr.toByteVal(4, false));
        return ret;
    }

    public void parse(byte[] data, Company company) {
        Parser parser = new Parser(data);
        this.id.parseByteVal(parser, 18);
        this.code.parseByteVal(parser, 2);
        this.name.parseByteVal(parser, 3);
        this.addr.parseByteVal(parser, 4);

        company.setId(this.id.getVal());
        company.setCode(this.code.getVal());
        company.setName(this.name.getVal());
        company.setAddr(this.addr.getVal());
    }
}

class Parser {
    public Map parserData;

    Parser(byte[] data) {
        ByteBuffer buf = ByteBuffer.allocate(data.length);
        buf.put(data);

        parserData = new HashMap();

        int offset = 0;
        while (offset < data.length) {
            int typeAndTag = (int) buf.get(offset);
            offset++;

            int type = typeAndTag >> 4;
            int tag = typeAndTag - (type << 4);

            if (tag == 15) {
                tag = (int) buf.get(offset);
                offset++;
            }

            if (type == CoType.CO_INT) {
                parserData.put(tag, buf.getInt(offset));
                offset = offset + 4;
            } else if (type == CoType.CO_LONG) {
                parserData.put(tag, buf.getLong(offset));
                offset = offset + 8;
            } else if (type == CoType.CO_STRING) {
                int len = buf.getInt(offset);
                offset = offset + 4;

                byte[] str = new byte[len];
                for (int i = 0; i < len; i++) {
                    str[i] = buf.get(offset);
                    offset++;
                }

                parserData.put(tag, new String(str));
            }
        }
    }

    public Object getVal(int tag) {
        if (parserData.containsKey(tag)) {
            return parserData.get(tag);
        } else {
            return null;
        }
    }
}