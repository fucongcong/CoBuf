package co.demo.maplist;

import co.buf.parser.CoBuilder;
import co.buf.parser.CoParser;
import co.buf.type.*;
import co.buf.util.ByteUtil;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
// struct Members
// {
//     0 need list<string> names;
//     1 need map<string, string> sexs;
//     2 need optional bool work;
// }
// struct Company
// {
//     0 need int id;
//     1 need long code;
//     2 need string name;
//     16 optional string addr;
//     4 optional Members members;
//     5 optional list<Members> memberLists;
//     6 optional map<string, Members> memberMaps;
// }
//struct User
//{
//     0 need int id;
//     1 optional Company company;
//     2 optional bool working;
//}
//

class Members extends CoStruct {
    public List<String> names;
    public boolean work;

    Members() { this.builder = new MembersBuilder(); }

    public List<String> getNames() {
        return names;
    }

    public void setNames(List<String> names) {
        this.names = names;
    }

    public boolean isWork() {
        return work;
    }

    public void setWork(boolean work) {
        this.work = work;
    }

    public MembersBuilder getBuilder() { return (MembersBuilder) builder; }
}

class MembersBuilder extends CoBuilder {
    public CoList<String> names;
    public CoBool work;

    MembersBuilder()
    {
        this.names = new CoList(new CoString(), CoType.CO_STRING);
        this.work = new CoBool();
    }

    public void setNames(List<String> names)
    {
        this.names.setVal(names);
    }

    public void setWork(boolean work) {
        this.work.setVal(work);
    }

    public byte[] toByteArray() {
        byte[] ret = null;
        ret = ByteUtil.byteMerge(ret, this.names.toByteVal(0, true));
        ret = ByteUtil.byteMerge(ret, this.work.toByteVal(2, false));
        return ret;
    }

    public void parse(byte[] data, CoStruct struct) {
        CoParser parser = new CoParser(data);
        this.names.parseByteVal(parser, 0);
        this.work.parseByteVal(parser, 2);

        Members members = (Members) struct;
        members.setNames(this.names.getVal());
        members.setWork(this.work.getVal());
    }
}

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

class Company extends CoStruct {
    public int id;
    public long code;
    public String name;
    public String addr;
    public Members members;
    public List<Members> memberLists;
    public Map<String, Members> memberMaps;

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

    public Members getMembers() {
        return members;
    }

    public void setMembers(Members members) {
        this.members = members;
    }

    public List<Members> getMemberLists() {
        return memberLists;
    }

    public void setMemberLists(List<Members> memberLists) {
        this.memberLists = memberLists;
    }

    public Map<String, Members> getMemberMaps() {
        return memberMaps;
    }

    public void setMemberMaps(Map<String, Members> memberMaps) {
        this.memberMaps = memberMaps;
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

class CompanyBuilder extends CoBuilder {
    public CoInt id;
    public CoLong code;
    public CoString name;
    public CoString addr;
    public CoStruct members;
    public CoList<Members> memberLists;

    CompanyBuilder()
    {
        this.id = new CoInt();
        this.code = new CoLong();
        this.name = new CoString();
        this.addr = new CoString();
        this.members = new CoStruct();
        this.memberLists = new CoList(new Members(), CoType.CO_STRUCT);
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

    public void setMembers(Members members)
    {
        this.members.setVal(members);
    }

    public void setMemberLists(List<Members> memberLists) { this.memberLists.setVal(memberLists); }

    public byte[] toByteArray() {
        byte[] ret = null;
        ret = ByteUtil.byteMerge(ret, this.id.toByteVal(0, true));
        ret = ByteUtil.byteMerge(ret, this.code.toByteVal(1, true));
        ret = ByteUtil.byteMerge(ret, this.name.toByteVal(2, false));
        ret = ByteUtil.byteMerge(ret, this.addr.toByteVal(16, false));
        ret = ByteUtil.byteMerge(ret, this.members.toByteVal(4, false));
        ret = ByteUtil.byteMerge(ret, this.memberLists.toByteVal(5, false));
        return ret;
    }

    public void parse(byte[] data, CoStruct struct) {
        CoParser parser = new CoParser(data);
        this.id.parseByteVal(parser, 0);
        this.code.parseByteVal(parser, 1);
        this.name.parseByteVal(parser, 2);
        this.addr.parseByteVal(parser, 16);
        this.members.parseByteVal(parser, 4, new Members());
        this.memberLists.parseByteVal(parser, 5);

        Company company = (Company) struct;
        company.setId(this.id.getVal());
        company.setCode(this.code.getVal());
        company.setName(this.name.getVal());
        company.setAddr(this.addr.getVal());
        company.setMembers((Members) this.members.getVal());
        company.setMemberLists(this.memberLists.getVal());
    }
}

public class MapListDemo
{

    public static void main(String[] args) {
        Members members = new Members();
        MembersBuilder membersBuilder = members.getBuilder();
        List<String> names = new ArrayList<String>();
        names.add("coco");
        names.add("jersey");
        membersBuilder.setNames(names);
        membersBuilder.setWork(true);

        Members members2 = new Members();
        MembersBuilder membersBuilder2 = members2.getBuilder();
        List<String> names2 = new ArrayList<String>();
        names2.add("coco2");
        names2.add("jersey2");
        membersBuilder2.setNames(names2);
        membersBuilder2.setWork(true);

        List<Members> membersList = new ArrayList<Members>();
        membersList.add(members);
        membersList.add(members2);

        Company company = new Company();
        CompanyBuilder builder = company.getBuilder();
        builder.setId(10);
        builder.setCode(1111);
        builder.setName("coco");
        builder.setAddr("aaaaaaaaaa");
        builder.setMembers(members);
        builder.setMemberLists(membersList);

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

         try {
             File file = new File("data/map-list-demo.data");
             if(!file.exists()) {
                 file.createNewFile();
             }
             FileOutputStream fos = new FileOutputStream(file) ;
             DataOutputStream fw = new DataOutputStream(fos);
             fw.write(data);

         } catch (IOException e) {
             e.printStackTrace();
         }
    }
}

