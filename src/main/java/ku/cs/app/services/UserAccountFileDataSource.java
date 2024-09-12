package ku.cs.app.services;

import ku.cs.app.models.UserAccount;
import ku.cs.app.models.UserAccountList;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class UserAccountFileDataSource implements DataSource<UserAccountList> {
    private String directoryName;
    private String fileName;

    public UserAccountFileDataSource(){
        this("data/", "account.csv");
    }


    public UserAccountFileDataSource(String directoryName, String fileName) {
        this.directoryName = directoryName;
        this.fileName = fileName;
        checkFileIsExisted();
    }

    private void checkFileIsExisted(){
        File file = new File(directoryName);
        FileWriter writer = null;
        BufferedWriter buffer = null;
        if( ! file.exists()) {
            file.mkdirs();
        }
        String filePath = directoryName + File.separator + fileName;
        file = new File(filePath);
        if( ! file.exists()) {
            try {
                file.createNewFile();

            } catch (IOException e) {
//                throw new RuntimeException(e);
                e.printStackTrace();
            }finally {
                try {
                    buffer.close();
                    writer.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public UserAccountList readData(){
        UserAccountList accountList = new UserAccountList();
        String filePath = directoryName + File.separator + fileName;
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
//            reader = new FileReader(file);
//            buffer = new BufferedReader(reader);
            String line = "";
            while ( (line = buffer.readLine()) != null ){
                String[] data = line.split(",");
                UserAccount account = new UserAccount();
                account.setUsername(data[0].trim());
                account.setPassword(data[1].trim());
                account.setName(data[2].trim());
                account.setOrganization(data[3].trim());
                account.setRole(data[4].trim());
                account.setImagePath(data[5].trim());
                account.setDate((data[6].trim()));
                account.setTime(data[7].trim());
                account.setUserStatus(data[8].trim());
                account.setAppealText(data[9].trim());
                account.setLoginAttempt(data[10].trim());
                account.setGotReported(data[11].trim());
                accountList.addAccount(account.getUsername(), account);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                buffer.close();
//                reader.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return accountList;
    }

    public void writeData(UserAccountList userAccountList) throws FileNotFoundException {
        String filePath = directoryName + File.separator + fileName;
        File file = new File(filePath);

        FileOutputStream fos = new FileOutputStream(file);
        OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
        BufferedWriter buffer = new BufferedWriter(osw);
        try {
//            writer = new FileWriter(file, true);
//            buffer = new BufferedWriter(writer);

//            for (UserAccount account : userAccountList.getAllAccounts()){
//                String line = account.getUsername() + ','
//                        + account.getPassword() + ','
//                        + account.getName() + ','
//                        + account.getOrganization() + ','
//                        + account.getRole() + ','
//                        + account.getImagePath() + ','
//                        + account.getDate() + ','
//                        + account.getTime() + ','
//                        + account.getUserStatus() + ','
//                        + account.getAppealText() + ','
//                        + account.getLoginAttempt() + ','
//                        + account.getGotReported();
//                buffer.append(line);
//                buffer.newLine();
//            }
            for(String rowData : userAccountList.getAllAccounts()){
                String line = rowData + ","
                        + userAccountList.find(rowData).getPassword() + ","
                        + userAccountList.find(rowData).getName() + ","
                        + userAccountList.find(rowData).getOrganization() + ","
                        + userAccountList.find(rowData).getRole() + ","
                        + userAccountList.find(rowData).getImagePath() + ","
                        + userAccountList.find(rowData).getDate() + ","
                        + userAccountList.find(rowData).getTime() + ","
                        + userAccountList.find(rowData).getUserStatus() + ","
                        + userAccountList.find(rowData).getAppealText() + ","
                        + userAccountList.find(rowData).getLoginAttempt() + ","
                        + userAccountList.find(rowData).getGotReported();

                buffer.append(line);
                buffer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                buffer.close();
//                writer.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

//    public void writeData(UserAccountList accountList) throws IOException {
//        String filePath = directoryName + File.separator + fileName;
//        File file = new File(filePath);
//
//        FileWriter writer = new FileWriter(file);
//
//        for (String rowData : accountList.getAllStringAccounts()) {
//            String line =
//                     accountList.find(rowData).getUsername() + ","
//                    + accountList.find(rowData).getPassword() + ","
//                    + accountList.find(rowData).getName() + ","
//                    + accountList.find(rowData).getOrganization() + ","
//                    + accountList.find(rowData).getRole() + ","
//                    + accountList.find(rowData).getImagePath() + ","
//                    + accountList.find(rowData).getDate() + ","
//                    + accountList.find(rowData).getTime() + ","
//                    + accountList.find(rowData).getUserStatus() + ","
//                    + accountList.find(rowData).getAppealText() + ","
//                    + accountList.find(rowData).getLoginAttempt() + ","
//                    + accountList.find(rowData).getGotReported();
//            writer.append(line);
//            writer.append("\n");
//        }
//        writer.flush();
//        writer.close();
//    }
}
