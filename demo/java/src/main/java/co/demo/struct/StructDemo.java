package co.demo.struct;

// struct Company
// {
//     0 need int id;
//     1 need long code;
//     2 need string name;
//     16 optional string addr;
// }
//
public class StructDemo {

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