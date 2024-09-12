package ku.cs.app.controller.admin;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import ku.cs.app.models.UserAccount;
import ku.cs.app.models.UserAccountList;
import ku.cs.app.services.DataSource;
import ku.cs.app.services.DateTimeComparator;
import ku.cs.app.services.ObjectWrapper;
import ku.cs.app.services.UserAccountFileDataSource;


import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class BannedUserListController implements Initializable {

    @FXML
    private ImageView imageAdmin;

    @FXML
    private ImageView imageReport;

    @FXML
    private ImageView imageStaff;

    @FXML
    private ImageView imageUser;

    @FXML
    private TableView<UserAccount> table;
    @FXML
    private TableColumn<UserAccount, String> username;
    @FXML
    private TableColumn<UserAccount, String> date;
    @FXML
    private TableColumn<UserAccount, String> time;
    @FXML
    private TableColumn<UserAccount, String> userStatus;
    @FXML
    private TableColumn<UserAccount, String> loginAttempt;
    @FXML
    private Label usernameLabel;
    @FXML Label nameLabel;
    @FXML
    private Label dateLabel;
    @FXML
    private Label timeLabel;
    @FXML
    private TextArea appealTextArea;
    @FXML private ImageView accountImage;
    @FXML private AnchorPane parent;
    @FXML private ImageView modeImage;
    @FXML private Button banButton;
    @FXML
    private VBox reInstateVBox;


    private DataSource<UserAccountList> read = new UserAccountFileDataSource();
    private UserAccountList accountList = read.readData();
    private UserAccount userAccount;
    private UserAccountList gotReportedUserList;

    private Boolean isLightMode;
    private ObjectWrapper objectWrapper;

    String lightModePath = new File(System.getProperty("user.dir") + File.separator + "/style/light_mode.css").toURI().toString();
    String darkModePath = new File(System.getProperty("user.dir") + File.separator + "/style/dark_mode.css").toURI().toString();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        objectWrapper = (ObjectWrapper) com.github.saacsos.FXRouter.getData();
        userAccount = (UserAccount) objectWrapper.getObject("account");
        isLightMode = (Boolean) objectWrapper.getObject("mode");
        checkMode();

        banButton.setVisible(false);
        reInstateVBox.setVisible(false);

        gotReportedUserList = new UserAccountList();
        for (String account : accountList.getAllAccounts()) {
            UserAccount acc = accountList.find(account);
            if (Integer.parseInt(acc.getGotReported()) > 0 && !acc.getUserStatus().equals("banned")) {
                acc.setUserStatus("reported");
                gotReportedUserList.addAccount(acc.getUsername(), acc);
                try {
                    read.writeData(accountList);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            if(acc.getUserStatus().equals("banned")){
                gotReportedUserList.addAccount(acc.getUsername(), acc);
            }
        }
//        gotReportedUserList.sortDateTime();
        showTableView();

//        String defaultUrl = new File(System.getProperty("user.dir") + File.separator + "images/default.png").toURI().toString();

        handleSelectedTableView();
    }

    private void showTableView() {

        ArrayList list = new ArrayList<>();
        for (String rowData : gotReportedUserList.getAllAccounts()) {
            list.add(gotReportedUserList.find(rowData));
        }
        list.sort(new DateTimeComparator());
        table.setItems(FXCollections.observableArrayList(list));
        username.setCellValueFactory(new PropertyValueFactory<UserAccount, String>("username"));
        date.setCellValueFactory(new PropertyValueFactory<UserAccount, String>("date"));
        time.setCellValueFactory(new PropertyValueFactory<UserAccount, String>("time"));
        userStatus.setCellValueFactory(new PropertyValueFactory<UserAccount, String>("userStatus"));
        loginAttempt.setCellValueFactory(new PropertyValueFactory<UserAccount, String>("loginAttempt"));

//        table.setItems(FXCollections.observableArrayList(accountList.getAllAccounts()));
    }
    @FXML
    public void handleBanButton(ActionEvent actionEvent) throws IOException {
        for (String account : accountList.getAllAccounts()) {
            UserAccount acc = accountList.find(account);
            if (selectedUser.getUsername().equals(acc.getUsername())) {
//                acc.setUserStatus("banned");
                selectedUser.setUserStatus("banned");
                selectedUser.setGotReported("0");
                accountList.addAccount(selectedUser.getUsername(), selectedUser);
                gotReportedUserList.addAccount(selectedUser.getUsername(), selectedUser);
                table.refresh();
                read.writeData(accountList);
                showTableView();
            }
        }
    }

    // reinstate method set userStatus to default
    @FXML
    public void handleReinstateButton(ActionEvent actionEvent) throws IOException {
        for (String account : accountList.getAllAccounts()) {
            UserAccount acc = accountList.find(account);
            if (selectedUser.getUsername().equals(acc.getUsername())) {
                acc.setGotReported("0");
                acc.setAppealText("");
                acc.setLoginAttempt("0");
                acc.setUserStatus("");
                gotReportedUserList.removeAccount(acc.getUsername(), acc);
                read.writeData(accountList);
                System.out.println("reinstated this user (" + acc.getUsername() + ")");
            }
        }

        showTableView();
        handleSelectedTableView();
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>");
        System.out.println(gotReportedUserList);
    }

    @FXML
    public void handleBackButton(ActionEvent actionEvent) {
        try {
            com.github.saacsos.FXRouter.goTo("login", objectWrapper);
        } catch (IOException e) {
            System.err.println("ไปที่หน้า home ไม่ได้");
            System.err.println("ให้ตรวจสอบการกำหนด route");
        }
    }

    @FXML
    public void handleStaffButton(ActionEvent actionEvent) {
        try {
            com.github.saacsos.FXRouter.goTo("staffEditor", objectWrapper);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("ไปที่หน้า staffEditor ไม่ได้");
            System.err.println("ให้ตรวจสอบการกำหนด route");
        }
    }

    @FXML
    public void handleAdminButton(ActionEvent actionEvent) {
        try {
            com.github.saacsos.FXRouter.goTo("adminEditor", objectWrapper);
        } catch (IOException e) {
            System.err.println("ไปที่หน้า adminEditor ไม่ได้");
            System.err.println("ให้ตรวจสอบการกำหนด route");
            e.printStackTrace();
        }
    }

    @FXML
    public void handleReportButton(ActionEvent actionEvent) {
        try {
            com.github.saacsos.FXRouter.goTo("report", objectWrapper);
        } catch (IOException e) {
            System.out.println("ไปที่หน้า report ไม่ได้");
            System.out.println("ให้ตรวจสอบการกำหนด route");
            e.printStackTrace();
        }
    }
    @FXML
    public void handleUserButton(ActionEvent actionEvent) {
        try {
            com.github.saacsos.FXRouter.goTo("userEditor", objectWrapper);
        } catch (IOException e) {
            System.err.println("ไปที่หน้า userEditor ไม่ได้");
            System.err.println("ให้ตรวจสอบการกำหนด route");
        }
    }

    private UserAccount selectedUser;

    @FXML
    public void handleSelectedTableView() {
        table.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<UserAccount>() {
                    @Override
                    public void changed(ObservableValue<? extends UserAccount> observableValue, UserAccount userAccount, UserAccount newValue) {
                        if (newValue != null) {
                            System.out.println(newValue + " is selected");
                            //selectedUser = userAccount;

                            showSelectedUserAccount(newValue);
                        }
                    }
                }
        );
    }

    @FXML
    public void showSelectedUserAccount(UserAccount userAccount) {
        usernameLabel.setText(userAccount.getUsername());
        nameLabel.setText(userAccount.getName());
        selectedUser = userAccount;

        if (selectedUser.getUserStatus().equals("reported")) {
            banButton.setVisible(true);
            reInstateVBox.setVisible(false);
        } else if (selectedUser.getUserStatus().equals("banned")) {
            reInstateVBox.setVisible(true);
            banButton.setVisible(false);
        }

        dateLabel.setText(userAccount.getDate());
        timeLabel.setText(userAccount.getTime());
        appealTextArea.setText(userAccount.getAppealText());
        String url = new File(System.getProperty("user.dir") + File.separator + userAccount.getImagePath()).toURI().toString();
        accountImage.setImage(new Image(url));
    }

    private void checkMode() {
        if (isLightMode) {
            setLightMode();
        } else {
            setDarkMode();
        }
    }
    @FXML
    public void changeMode(ActionEvent actionEvent) {
        isLightMode = !isLightMode;
        if (isLightMode) {
            setLightMode();
        } else {
            setDarkMode();
        }
    }

    private void setLightMode() {
        parent.getStylesheets().add(lightModePath);
        parent.getStylesheets().remove(darkModePath);
        Image image = new Image(getClass().getResourceAsStream("/ku/cs/images/sun.png"));
        modeImage.setImage(image);
        objectWrapper.addObject("mode", isLightMode);
    }
    private void setDarkMode() {
        parent.getStylesheets().add(darkModePath);
        parent.getStylesheets().remove(lightModePath);
        Image image = new Image(getClass().getResourceAsStream("/ku/cs/images/moon.png"));
        modeImage.setImage(image);
        objectWrapper.addObject("mode", isLightMode);
    }
}