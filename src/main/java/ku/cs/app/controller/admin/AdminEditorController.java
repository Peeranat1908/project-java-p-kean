package ku.cs.app.controller.admin;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import ku.cs.app.models.UserAccount;
import ku.cs.app.models.UserAccountList;
import ku.cs.app.services.DataSource;
import ku.cs.app.services.ObjectWrapper;
import ku.cs.app.services.UserAccountFileDataSource;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AdminEditorController implements Initializable {
    @FXML
    private Label usernameErrorLabel;
    @FXML
    private Label oldPasswordErrorLabel;
    @FXML
    private Label newPasswordErrorLabel;
    @FXML
    private Label statusLabel;
    @FXML
    private TextField usernameTextField;
    @FXML
    private TextField oldPasswordTextField;
    @FXML
    private TextField newPasswordTextField;
    @FXML private AnchorPane parent;
    @FXML private ImageView modeImage;
    String directoryName = "data";
    String filepath = directoryName + File.separator + "account.csv";
    private UserAccount userAccount;
    private DataSource<UserAccountList> read = new UserAccountFileDataSource();
    private UserAccountList accountList = read.readData();
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

        usernameErrorLabel.setText("");
        oldPasswordErrorLabel.setText("");
        newPasswordErrorLabel.setText("");
        statusLabel.setText("");
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
    public void handleUserButton(ActionEvent actionEvent) {
        try {
            com.github.saacsos.FXRouter.goTo("userEditor", objectWrapper);
        } catch (IOException e) {
            System.err.println("ไปที่หน้า userEditor ไม่ได้");
            System.err.println("ให้ตรวจสอบการกำหนด route");
        }
    }

    @FXML public void handleStaffButton(ActionEvent actionEvent) {
        try {
            com.github.saacsos.FXRouter.goTo("staffEditor", objectWrapper);
        } catch (IOException e) {
            System.err.println("ไปที่หน้า staffEditor ไม่ได้");
            System.err.println("ให้ตรวจสอบการกำหนด route");
        }
    }
    @FXML public void handleBannedUserListButton(ActionEvent actionEvent){
        try {
            com.github.saacsos.FXRouter.goTo("bannedUserList", objectWrapper);
        } catch (IOException e) {
            System.out.println("ไปที่หน้า bannedUserList ไม่ได้");
            System.out.println("ให้ตรวจสอบการกำหนด route");
            e.printStackTrace();
        }
    }


    @FXML public void handleReportButton(ActionEvent actionEvent) {
        try {
            com.github.saacsos.FXRouter.goTo("report", objectWrapper);
        } catch (IOException e) {
            System.err.println("ไปที่หน้า report ไม่ได้");
            System.err.println("ให้ตรวจสอบการกำหนด route");
            e.printStackTrace();
        }
    }

    @FXML public void handleConfirmButton(ActionEvent actionEvent) throws IOException {
        String username = usernameTextField.getText();
        String oldPassword = oldPasswordTextField.getText();
        String newPassword = newPasswordTextField.getText();

        usernameErrorLabel.setText("");
        oldPasswordErrorLabel.setText("");
        newPasswordErrorLabel.setText("");

        if(!accountList.searchUsername(username, accountList)){
            usernameErrorLabel.setText("ชื่อผู้ใช้ไม่ถูกต้อง");
            System.out.println("ชื่อผู้ใช้ไม่ถูกต้อง");
        } else if (! accountList.checkPassword(username, oldPassword, accountList)) {
            oldPasswordErrorLabel.setText("รหัสผ่านผิด");
            System.out.println("รหัสผ่านผิด");
        } else if (! accountList.searchRole("admin",accountList)) {

            System.out.println("ID ผู้ใช้ไม่ใช่ของเจ้าหน้าที่");
            usernameErrorLabel.setText("ID ผู้ใช้ไม่ใช่ของเจ้าหน้าที่");
        } else if (oldPassword.equals(newPassword)) {
            System.out.println("รหัสผ่านใหม่ตรงกับรหัสเก่า");
            newPasswordErrorLabel.setText("รหัสผ่านใหม่ตรงกับรหัสเก่า");
        }else{
            userAccount.changePassword(newPassword);
            accountList.addAccount(username, userAccount);
            read.writeData(accountList);
            System.out.println("เปลี่ยนรหัสผ่านสำเร็จ");
            statusLabel.setText("เปลี่ยนรหัสผ่านสำเร็จ");

            objectWrapper.addObject("account", userAccount);
            usernameTextField.clear();
            oldPasswordTextField.clear();
            newPasswordTextField.clear();
        }
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