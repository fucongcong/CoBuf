package co.demo.bool;

import co.buf.parser.CoBuilder;
import co.buf.parser.CoParser;
import co.buf.type.CoBool;
import co.buf.type.CoInt;
import co.buf.type.CoStruct;
import co.buf.util.ByteUtil;
import co.demo.struct.*;
// struct Company
// {
//     0 need int id;
//     1 need long code;
//     2 need string name;
//     16 optional string addr;
// }
//struct User
//{
//     0 need int id;
//     1 optional Company company;
//     2 optional bool working;
//}
//
class User extends CoStruct {
    public int id;
    public Company company;
    public boolean working;

    User() {
        this.builder = new UserBuilder();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Company getCompany() {
        return company;
    }

    public UserBuilder getBuilder() {
        return (UserBuilder) builder;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public boolean getWorking() {
        return working;
    }

    public void setWorking(boolean working) {
        this.working = working;
    }

    public User parseFrom(byte[] data) {
        this.builder.parse(data, this);
        return this;
    }
}

class UserBuilder extends CoBuilder {
    public CoInt id;
    public CoStruct company;
    public CoBool working;

    UserBuilder()
    {
        this.id = new CoInt();
        this.company = new CoStruct();
        this.working = new CoBool();
    }

    public void setId(int id)
    {
        this.id.setVal(id);
    }

    public void setCompany(Company company)
    {
        this.company.setVal(company);
    }

    public void setWorking(boolean working) {
        this.working.setVal(working);
    }

    public byte[] toByteArray() {
        byte[] ret = null;
        ret = ByteUtil.byteMerge(ret, this.id.toByteVal(0, true));
        ret = ByteUtil.byteMerge(ret, this.company.toByteVal(1, false));
        ret = ByteUtil.byteMerge(ret, this.working.toByteVal(2, false));
        return ret;
    }

    public void parse(byte[] data, CoStruct struct) {
        CoParser parser = new CoParser(data);
        this.id.parseByteVal(parser, 0);
        this.company.parseByteVal(parser, 1, new Company());
        this.working.parseByteVal(parser, 2);

        User user = (User) struct;
        user.setId(this.id.getVal());
        user.setCompany((Company) this.company.getVal());
        user.setWorking(this.working.getVal());
    }
}

public class BoolDemo {

    public static void main(String[] args) {
        Company company = new Company();
        CompanyBuilder builder = company.getBuilder();
        builder.setId(10);
        builder.setCode(1111);
        builder.setName("coco");
        builder.setAddr("aaaaaaaaaa");

        User user = new User();
        UserBuilder userBuilder = user.getBuilder();
        userBuilder.setCompany(company);
        userBuilder.setId(1);
        userBuilder.setWorking(true);

        byte[] data = userBuilder.toByteArray();

        User user1 = new User();
        User us = user1.parseFrom(data);
        us.getId();
        us.getCompany();
        us.getWorking();

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

