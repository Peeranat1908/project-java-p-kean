package ku.cs.app.models;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import java.time.LocalDateTime;

public class UserAccount {
    private String username;
    private String password;
    private String name;

    private String organization;
    private String role;
    private String imagePath;
    //private String date;
    private String date;
    private String time;
    private String userStatus;
    private String loginAttempt;
    private String appealText;
    private String gotReported;



    public void changePassword(String newPassword){
        password = newPassword;

    }

//    public void reWrite(UserAccountList userAccountList, UserAccount userAccount){
//        String tempfile = "executableFiles+csv/temp.csv";
//        String filePath = directoryName + File.separƒator + fileName;
//        File oldFile = new File(filePath); //account.csv
//        File newFile = new File(tempfile); //temp
//
//        FileWriter writer = null;
//        BufferedWriter buffer = null;
//
//        try {
//            writer = new FileWriter(tempfile, true);
//            buffer = new BufferedWriter(writer);
//            String line = "";
//            for (UserAccount account : userAccountList.getAllAccounts()){
//                if(account.getUsername().equals(userAccount.getUsername())) {
//                    line = userAccount.getUsername() + ','
//                            + userAccount.getPassword() + ','
//                            + userAccount.getName() + ','
//                            + userAccount.getOrganization() + ','
//                            + userAccount.getRole() + ','
//                            + userAccount.getImagePath() + ','
//                            + userAccount.getDate() + ","
//                            + userAccount.getTime() + ','
//                            + userAccount.getUserStatus() + ','
//                            + userAccount.getAppealText() + ','
//                            + userAccount.getLoginAttempt() + ','
//                            + userAccount.getGotReported();
//                }
//                else{
//                    line = account.getUsername() + ','
//                            + account.getPassword() + ','
//                            + account.getName() + ','
//                            + account.getOrganization() + ','
//                            + account.getRole() + ','
//                            + account.getImagePath() + ','
//                            + account.getDate() + ','
//                            + account.getTime() + ','
//                            + account.getUserStatus() + ','
//                            + account.getAppealText() + ','
//                            + account.getLoginAttempt() + ','
//                            + account.getGotReported();
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
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//            File dump = new File(filePath);
//            oldFile.delete();
//            newFile.renameTo(dump);
//        }
//    }

    public LocalDateTime getSplitedDateTime() {
        String[] splitedTime = time.split(":");
        int hour = Integer.parseInt(splitedTime[0]);
        int minute = Integer.parseInt(splitedTime[1]);
        int second = Integer.parseInt(splitedTime[2]);
        String[] splitedDate = date.split("/");
        int year = Integer.parseInt(splitedDate[2]);
        int month = Integer.parseInt(splitedDate[1]);
        int day = Integer.parseInt(splitedDate[0]);
        return LocalDateTime.of(year, month, day, hour, minute, second);
    }
//    public LocalDate getSplitedDate() {
//        String[] splitedDate = date.split("/");
//        int year = Integer.parseInt(splitedDate[2]);
//        int month = Integer.parseInt(splitedDate[1]);
//        int day = Integer.parseInt(splitedDate[0]);
//        return LocalDate.of(year, month, day);
//    }
    public UserAccount(){

    }

    public UserAccount(String password, String name, String organization, String role, String imagePath, String date, String time, String userStatus, String appealText, String loginAttempt, String gotReported) {
        this.password = password;
        this.name = name;
        this.organization = organization;
        this.role = role;
        this.imagePath = imagePath;
        this.date = date;
        this.time = time;
        this.userStatus = userStatus;
        this.appealText = appealText;
        this.loginAttempt = loginAttempt;
        this.gotReported = gotReported;
    }
    public UserAccount(String username, String password, String name){
        this.username = username;
        this.password = password;
        this.name = name;
        this.organization = organization;
        this.role = role;
        this.imagePath = "images/default.png";
//        this.date = LocalDate.now().format(df);
//        this.time = LocalTime.now().format(tf);
        this.date = "";
        this.time = "";
        this.userStatus = "";
        this.appealText = "";
        this.loginAttempt = "0";
        this.gotReported = "0";
    }

    public UserAccount(String username, String password, String name, String organization, String role) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.organization = organization;
        this.role = role;
        this.imagePath = "images/default.png";
//        this.date = LocalDate.now().format(df);
//        this.time = LocalTime.now().format(tf);
        this.date = "";
        this.time = "";
        this.userStatus = "";
        this.appealText = "";
        this.loginAttempt = "0";
        this.gotReported = "0";
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrganization() {return organization;}

    public String getRole() {
        return role;
    }
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
    public String getImagePath() {
        return imagePath;
    }
    public void setOrganization(String organization) {this.organization = organization;}
    public void setRole(String role) {
        this.role = role;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

    public String getLoginAttempt() {
        return loginAttempt;
    }

    public void setLoginAttempt(String loginAttempt) {
        this.loginAttempt = loginAttempt;
    }

    public String getAppealText() {
        return appealText;
    }

    public void setAppealText(String appealText) {
        this.appealText = appealText;
    }

    public String getGotReported() {
        return gotReported;
    }

    public void setGotReported(String gotReported) {
        this.gotReported = gotReported;
    }

    @Override
    public String toString() {
        return "UserAccount{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", organization='" + organization + '\'' +
                ", role='" + role + '\'' +
                ", imagePath='" + imagePath + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", userStatus='" + userStatus + '\'' +
                ", loginAttempt='" + loginAttempt + '\'' +
                ", appealText='" + appealText + '\'' +
                '}';
    }
}
