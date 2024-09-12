package ku.cs.app.models;

import ku.cs.app.services.DataSource;
import ku.cs.app.services.UserAccountFileDataSource;

import java.util.*;

public class UserAccountList {
    private Map<String, UserAccount> accounts;
    public UserAccountList() {accounts = new TreeMap<>(); }
    public void addAccount(String username, UserAccount account){
        accounts.put(username, account);
    }
    public void removeAccount(String username, UserAccount account) {
        accounts.remove(username, account);
    }
    public UserAccount find(String userName){
        UserAccount found = accounts.get(userName);
        if (found == null) {
            throw new RuntimeException(userName + " not found");
        }
        return found;
    }
    public Set<String> getAllAccounts() {
        return accounts.keySet();
    }
    ArrayList<UserAccount> temp;
    public ArrayList<UserAccount> getAllArrayAccounts(){
        temp = new ArrayList<UserAccount>();
        for(String acc : accounts.keySet()){
            temp.add(accounts.get(acc));
        }
        return temp;
    }

    public boolean verifyLogin(String username, String password, String hidden, UserAccountList accountList){
        for (String acc : accountList.getAllAccounts()) {
            UserAccount account = accountList.find(acc);
            if (account.getUsername().equals(username)) {
                if (account.getPassword().equals(password) || account.getPassword().equals(hidden)) {
                    return true;
                }
            }
        }
//        for(UserAccount acc : accountList.getAllAccounts()){
//            if(acc.getUsername().equals(username)) {
//                if (acc.getPassword().equals(password) || acc.getPassword().equals(hidden)) {
//                    return true;
//                }
//            }
//        }
        return false;
    }
    public boolean checkPassword(String username, String password, UserAccountList accountList){
        for (String acc : accountList.getAllAccounts()) {
            UserAccount account = accountList.find(acc);
            if (account.getUsername().equals(username)) {
                if (account.getPassword().equals(password)) {
                    return true;
                }
            }
        }
//        for(UserAccount acc : accountList.getAllAccounts()){
//            if(acc.getUsername().equals(username)) {
//                if (acc.getPassword().equals(password)) {
//                    return true;
//                }
//            }
//        }
        return false;
    }

//    public ArrayList<UserAccount> sortDateTime() {
////        ArrayList<UserAccount> temp = (ArrayList<UserAccount>) accounts;
//        DataSource<UserAccountList> read = new UserAccountFileDataSource("executableFiles+csv/", "account.csv");
//        UserAccountList accountList = read.readData();
//        ArrayList<UserAccount> temp = accountList.getAllArrayAccounts();
//        temp.sort(new Comparator<UserAccount>() {
//            @Override
//            public int compare(UserAccount o1, UserAccount o2) {
//                if (o1.getSplitedDateTime().isBefore(o2.getSplitedDateTime())) return 1;
//                if (o1.getSplitedDateTime().isAfter(o2.getSplitedDateTime())) return -1;
//                return 0;
//            }
//        });
//        return temp;
//    }
    public ArrayList<UserAccount> sortDateTime() {
        DataSource<UserAccountList> read = new UserAccountFileDataSource("executableFiles+csv/", "account.csv");
        UserAccountList accountList = read.readData();
        ArrayList<UserAccount> temp = accountList.getAllArrayAccounts();
        temp.sort(new Comparator<UserAccount>() {
            @Override
            public int compare(UserAccount o1, UserAccount o2) {
                if (o1.getSplitedDateTime().isBefore(o2.getSplitedDateTime())) return 1;
                if (o1.getSplitedDateTime().isAfter(o2.getSplitedDateTime())) return -1;
                return 0;
            }
        });
        return temp;
    }

    @Override
    public String toString() {
        return "UserAccountList{" +
                "accounts=" + accounts +
                '}';
    }

    public boolean searchUsername(String searchString, UserAccountList accountList) {
        for(String account : accountList.getAllAccounts()){
            UserAccount acc = accountList.find(account);
            if (acc.getUsername().equals(searchString)){
                return true;
            }
        }
        return false;
    }
    public boolean searchName(String searchString, UserAccountList accountList) {
        for(String account : accountList.getAllAccounts()){
            UserAccount acc = accountList.find(account);
            if (acc.getName().equals(searchString)){
                return true;
            }
        }
        return false;
    }


    public boolean searchRole(String searchString, UserAccountList accountList) {
        for(String account : accountList.getAllAccounts()){
            UserAccount acc = accountList.find(account);
            if (acc.getRole().equals(searchString)){
                return true;
            }
        }
        return false;
    }


}
