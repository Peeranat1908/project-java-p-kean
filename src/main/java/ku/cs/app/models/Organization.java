package ku.cs.app.models;


public class Organization {
    private String organizationName;
    private String category;


    public Organization(String organizationName, String category) {
        this.organizationName = organizationName;
        this.category = category;
    }

    public Organization() {
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public String getCategory() {
        return category;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "Organization{" +
                "organizationName='" + organizationName + '\'' +
                ", category='" + category + '\'' +
                '}';
    }

//    public void reWriteOrganization(OrganizationList organizationList, Organization organization){
//        String tempFile = "executableFiles+csv/temp.csv";
//        String filePath = directoryName + File.separator + filename;
//        File oldFile = new File(filePath); //organization.csv
//        File newFile = new File(tempFile); //temp
//
//        //File file = new File(filePath);
//        FileWriter writer = null;
//        BufferedWriter buffer = null;
//
//        try {
//            writer = new FileWriter(tempFile, true);
//            buffer = new BufferedWriter(writer);
//            String line = "";
//            for (Organization org : organizationList.getAllOrganization()){
//                if(!org.getOrganization().equals(organization.getOrganization())) {
//                    line = organization.getOrganization() + ", " + organization.getCategory();
//                }
//                else{
//                    line = org.getOrganization() + ", " + org.getCategory();
//                }
//                buffer.append(line);
//                buffer.newLine();
//            }
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        } finally {
//            try {
//                buffer.close();
//                writer.close();
//                File dump = new File(filePath);
//                oldFile.delete();
//                newFile.renameTo(dump);
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        }
//    }
}
