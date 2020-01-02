package co.demo.struct;


import co.buf.parser.CoBuilder;
import co.buf.parser.CoParser;
import co.buf.type.CoInt;
import co.buf.type.CoLong;
import co.buf.type.CoString;
import co.buf.type.CoStruct;
import co.buf.util.ByteUtil;

public class CompanyBuilder extends CoBuilder {
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

    public void parse(byte[] data, CoStruct struct) {
        CoParser parser = new CoParser(data);
        this.id.parseByteVal(parser, 0);
        this.code.parseByteVal(parser, 1);
        this.name.parseByteVal(parser, 2);
        this.addr.parseByteVal(parser, 16);

        Company company = (Company) struct;
        company.setId(this.id.getVal());
        company.setCode(this.code.getVal());
        company.setName(this.name.getVal());
        company.setAddr(this.addr.getVal());
    }
}