package ku.cs.app.services;

import ku.cs.app.models.*;
import ku.cs.app.models.Organization;
import ku.cs.app.models.OrganizationList;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class OrganizationFileDataSource{

    private String directoryName = "data";
    private String filename = "organization.csv";
    private String filepath = directoryName + File.separator + filename;


    public OrganizationFileDataSource() {
        this.directoryName = "data";
        this.filename = "organization.csv";
        checkFileIsExisted();
    }

    private void checkFileIsExisted(){
//        File file = new File(directoryName);
        String filepath = directoryName + File.separator + filename;
        File file = new File(filepath);
        if( ! file.exists()) {
            file.mkdirs();
        }
        String filePath = directoryName + File.separator + filename;
        file = new File(filePath);
        if( ! file.exists()) {
            try {
                file.createNewFile();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public List readOrganizationData(OrganizationList organizationList) {
        List<Organization> organizations = new ArrayList<>();
        String filePath = directoryName + File.separator + filename;
        File file = new File(filePath);
        FileInputStream fis = null;

        try {
            fis = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
        BufferedReader buffer = new BufferedReader(isr);
        try {
            String line = "";
            while ( (line = buffer.readLine()) != null ){
                String[] data = line.split(", ");
                Organization organization = new Organization();

                organization.setOrganizationName(data[0].trim());
                organization.setCategory(data[1].trim());

                organizations.add(organization);
                organizationList.addOrganization(organization.getOrganizationName(), organization);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                buffer.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return organizations;
    }


    public void writeData(OrganizationList organizationList) throws IOException {
        String filePath = directoryName + File.separator + filename;
        File file = new File(filePath);

        FileOutputStream fos = new FileOutputStream(file);
        OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
        BufferedWriter buffer = new BufferedWriter(osw);
        try {
            for (String rowData : organizationList.getAllOrganization()) {
                String line = rowData + ", "
                        + organizationList.find(rowData).getCategory();

                buffer.append(line);
                buffer.append("\n");
            }
        }catch (IOException e){
            throw new RuntimeException(e);
        }
        finally {
            try {
                buffer.flush();
                buffer.close();
            }
            catch (IOException e){
                throw new RuntimeException(e);
            }
        }
    }

    public void createOrganization(OrganizationList organizationList, String org, String category) throws IOException {
       Organization organization = new Organization();

       organization.setOrganizationName(org);
       organization.setCategory(category);

       organizationList.addOrganization(organization.getOrganizationName() , organization);
       writeData(organizationList);
       System.out.println(organizationList.getAllOrganization());

    }

}
