import co.buf.parser.CoParser;
import co.buf.type.*;
import co.buf.util.ByteUtil;
// struct Company
// {
//     0 need int id;
//     1 need long code;
//     2 need string name;
//     16 optional string addr;
// }
//

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
        byte[] ret = null;
        ret = ByteUtil.byteMerge(ret, this.id.toByteVal(0, true));
        ret = ByteUtil.byteMerge(ret, this.code.toByteVal(1, true));
        ret = ByteUtil.byteMerge(ret, this.name.toByteVal(2, false));
        ret = ByteUtil.byteMerge(ret, this.addr.toByteVal(16, false));
        return ret;
    }

    public void parse(byte[] data, Company company) {
        CoParser parser = new CoParser(data);
        this.id.parseByteVal(parser, 0);
        this.code.parseByteVal(parser, 1);
        this.name.parseByteVal(parser, 2);
        this.addr.parseByteVal(parser, 16);

        company.setId(this.id.getVal());
        company.setCode(this.code.getVal());
        company.setName(this.name.getVal());
        company.setAddr(this.addr.getVal());
    }
}

public class Demo2 {

    public static void main(String[] args) {
        Company company = new Company();
        CompanyBuilder builder = company.getBuilder();
        builder.setId(10);
        builder.setCode(1111);
        builder.setName("coco");
        builder.setAddr("aaaaaaaaaa");

        byte[] data = builder.toByteArray();

        Company company1 = new Company();
        Company cpy = company1.parseFrom(data);
        cpy.getId();
        cpy.getCode();


        // try {
        //     File file = new File("2");
        //     if(!file.exists()) {
        //         file.createNewFile();
        //     }
        //     FileOutputStream fos = new FileOutputStream(file) ;
        //     DataOutputStream fw = new DataOutputStream(fos);
        //     fw.write(data);
            
        // } catch (IOException e) {
        //     e.printStackTrace();
        // }
    }
}