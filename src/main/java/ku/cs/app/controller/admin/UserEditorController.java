package ku.cs.app.controller.admin;

import javafx.beans.Observable;
import com.github.saacsos.FXRouter;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import ku.cs.app.models.*;
import ku.cs.app.services.*;
import com.github.saacsos.FXRouter;
import ku.cs.app.models.*;
import ku.cs.app.services.*;


import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class UserEditorController implements Initializable {

    @FXML
    private TableView<UserAccount> table;
    @FXML
    private TableColumn<UserAccount, String> username;
    @FXML
    private TableColumn<UserAccount, String> date;
    @FXML
    private TableColumn<UserAccount, String> time;
    @FXML
    private Label usernameLabel;
    @FXML
    private Label dateLabel;
    @FXML
    private Label timeLabel;
    @FXML
    private Label organizationLabel;

    @FXML
    private Label statusLabel;
    @FXML
    private ComboBox<String> orgComboBox;
    @FXML
    private Label staffOrgLabel;

    @FXML
    private HBox staffHBoxEdit;

    @FXML
    private HBox staffHBoxOrg;
    @FXML
    private ImageView accountImage;
    @FXML
    private Label nameTitleLabel;
    @FXML
    private Label dateTitleLabel;
    @FXML
    private Label nameLabel;
    @FXML private AnchorPane parent;
    @FXML private ImageView modeImage;

    private OrganizationFileDataSource organizationFileDataSource;
    private OrganizationList organizationList;

    private List<Organization> organizations = new ArrayList<>();

    private DataSource<UserAccountList> read = new UserAccountFileDataSource();
    private UserAccountList accountList = read.readData();

    private UserAccount userAccount;
    private UserAccountList userShowList = new UserAccountList();

//
//    private DateTimeFormatter tf = DateTimeFormatter.ofPattern("hh:mm:ss");
//    private DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private UserAccount selectedAccount;
    private boolean isLightMode;
    private ObjectWrapper objectWrapper;

    String lightModePath = new File(System.getProperty("user.dir") + File.separator + "/style/light_mode.css").toURI().toString();
    String darkModePath = new File(System.getProperty("user.dir") + File.separator + "/style/dark_mode.css").toURI().toString();
    UserReportFileDataSource dataSource;
    private List<Report> reports = new ArrayList<>();
    private ReportList reportList;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("initialize UserEditor");

        reportList = new ReportList();
        dataSource = new UserReportFileDataSource();
        reports = dataSource.readData(reportList);



        for(String account : accountList.getAllAccounts()){
            UserAccount acc = accountList.find(account);
            if (!acc.getDate().equals("") && !acc.getTime().equals("") && !acc.getRole().equals("admin")) {
                userShowList.addAccount(acc.getUsername(), acc);
            }
        }
//        userShowList.sortDateTime();
        objectWrapper = (ObjectWrapper) FXRouter.getData();
        userAccount = (UserAccount) objectWrapper.getObject("account");
        isLightMode = (boolean) objectWrapper.getObject("mode");
        checkMode();

        usernameLabel.setText("");
        dateLabel.setText("-");
        timeLabel.setText("-");
        nameLabel.setText("-");

        showTableView();
        staffOrgLabel.setVisible(false);
        staffHBoxOrg.setVisible(false);
        staffHBoxEdit.setVisible(false);
        showComboBox();

//        ImagePattern image = new ImagePattern(new Image(defaultUrl));
//        circle.setFill(image);
        handleSelectedTableView();
        objectWrapper.addObject("account", userAccount);
    }

    @FXML
    private void handleChangeOrganizationButton(ActionEvent actionEvent) {
        if (orgComboBox.getValue() != null) {
            System.out.println("ComboBox: " + orgComboBox.getValue());

            for(String account : accountList.getAllAccounts()){
                UserAccount acc = accountList.find(account);
                if (acc.getUsername().equals(selectedAccount.getUsername())) {
//                    System.out.println("-----------------------------");
                    if (!selectedAccount.getOrganization().equals(orgComboBox.getValue())) {
                        for (Report report : reports) {
                            if (report.getStaffName().equals(selectedAccount.getName())) {
                                report.setAgencyName(orgComboBox.getValue());
                                reportList.addReport(report.getReportId(), report);
                            }
                        }
                        acc.setOrganization(orgComboBox.getValue());

                        try {
                            new UserReportFileDataSource().writeData(reportList);
                            read.writeData(accountList);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
//                        System.out.println("new org " + acc.getOrganization());
//                        System.out.println("Change organization completed\n");
                        statusLabel.setText("แก้ไขสำเร็จ");
                        statusLabel.setTextFill(Color.web("#49A239"));
                        organizationLabel.setText(acc.getOrganization());

                        showComboBox();
                        break;
                    } else {
                        statusLabel.setText("โปรดเลือกหน่วยงาน");
                        statusLabel.setTextFill(Color.web("#DE493D"));
                        break;
                    }
                }
            }
        } else {
            System.out.println("orgComboBox is null");
            statusLabel.setText("โปรดเลือกหน่วยงาน");
            statusLabel.setTextFill(Color.web("#FF0000"));
        }
    }

    private List<Organization> getOrganization() {
        organizationList = new OrganizationList();
        organizationFileDataSource = new OrganizationFileDataSource();
        organizations = organizationFileDataSource.readOrganizationData(organizationList);
        return organizations;
    }

    public void showComboBox() {
        organizations = new ArrayList<>(getOrganization());
        for(String org: organizationList.getAllOrganization()){
            orgComboBox.getItems().add(organizationList.find(org).getOrganizationName());
        }
    }

    private void showTableView() {
        username.setCellValueFactory(new PropertyValueFactory<UserAccount, String>("username"));
        date.setCellValueFactory(new PropertyValueFactory<UserAccount, String>("date"));
        time.setCellValueFactory(new PropertyValueFactory<UserAccount, String>("time"));
//        table.setItems(FXCollections.observableArrayList(accountList.getAllAccounts()));
        ArrayList<UserAccount> list = new ArrayList<>();
        for (String rowData : userShowList.getAllAccounts()) {
            list.add(userShowList.find(rowData));
        }
        list.sort(new DateTimeComparator());
        table.setItems(FXCollections.observableArrayList(list));
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
    public void handleBannedUserListButton(ActionEvent actionEvent) {
        try {
            com.github.saacsos.FXRouter.goTo("bannedUserList", objectWrapper);
        } catch (IOException e) {
            System.out.println("ไปที่หน้า bannedUserList ไม่ได้");
            System.out.println("ให้ตรวจสอบการกำหนด route");
            e.printStackTrace();
        }
    }

    @FXML
    public void handleSelectedTableView() {
        table.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<UserAccount>() {
                    @Override
                    public void changed(ObservableValue<? extends UserAccount> observableValue, UserAccount userAccount, UserAccount newValue) {
                        if (newValue != null) {
                            System.out.println(newValue + " is selected");
                            selectedAccount = newValue;
                            showSelectedUserAccount(newValue);
                            statusLabel.setText("");
                        }
                    }
                }
        );
        statusLabel.setText("");
    }

    @FXML
    public void showSelectedUserAccount(UserAccount userAccount) {
        nameLabel.setText(userAccount.getName());
        usernameLabel.setText("(" + userAccount.getUsername() + ")");
        if (!userAccount.getOrganization().equals("")) {
            staffOrgLabel.setVisible(true);
            staffHBoxOrg.setVisible(true);
            staffHBoxEdit.setVisible(true);
            organizationLabel.setText(userAccount.getOrganization());
        } else {
            staffOrgLabel.setVisible(false);
            staffHBoxOrg.setVisible(false);
            staffHBoxEdit.setVisible(false);
        }

        String defaultUrl = new File(System.getProperty("user.dir") + File.separator + userAccount.getImagePath()).toURI().toString();
        accountImage.setImage(new Image(defaultUrl));
        nameTitleLabel.setText("ชื่อ-นามสกุล :");
        dateTitleLabel.setText("วันที่ / เวลา เข้าสู่ระบบล่าสุด :");
        dateLabel.setText(userAccount.getDate());
        timeLabel.setText(userAccount.getTime());
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
//        mode.setLightMode(!mode.isLightMode());
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