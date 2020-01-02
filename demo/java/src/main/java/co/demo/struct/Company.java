package co.demo.struct;

import co.buf.parser.CoBuilder;
import co.buf.parser.CoParser;
import co.buf.type.CoInt;
import co.buf.type.CoLong;
import co.buf.type.CoString;
import co.buf.type.CoStruct;
import co.buf.util.ByteUtil;

public class Company extends CoStruct {
    public int id;
    public long code;
    public String name;
    public String addr;

    public Company() {
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
        return (CompanyBuilder) builder;
    }

    public Company parseFrom(byte[] data) {
        this.builder.parse(data, this);
        return this;
    }
}

