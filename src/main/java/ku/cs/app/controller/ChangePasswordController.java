package ku.cs.app.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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

public class ChangePasswordController {
    @FXML private TextField usernameTextField;
    @FXML private TextField oldPasswordTextField;
    @FXML private TextField newPasswordTextField;
    @FXML private TextField confirmNewPasswordTextField;
    @FXML private Label usernameErrorLabel;
    @FXML private Label oldPasswordErrorLabel;
    @FXML private Label newPasswordErrorLabel;
    @FXML private Label statusLabel;
    @FXML private AnchorPane parent;
    @FXML private ImageView modeImage;
    String directoryName = "data";
    String filepath = directoryName + File.separator + "account.csv";
    private UserAccount userAccount;
    private DataSource<UserAccountList> read = new UserAccountFileDataSource();
    private UserAccountList accountList = read.readData();
    private ObjectWrapper objectWrapper ;
    private Boolean isLightMode;

    String lightModePath = new File(System.getProperty("user.dir") + File.separator + "/style/light_mode.css").toURI().toString();
    String darkModePath = new File(System.getProperty("user.dir") + File.separator + "/style/dark_mode.css").toURI().toString();

    @FXML
    public void initialize(){
        objectWrapper = (ObjectWrapper) com.github.saacsos.FXRouter.getData();
        userAccount = (UserAccount) objectWrapper.getObject("account");
        isLightMode = (Boolean) objectWrapper.getObject("mode");
        checkMode();

        usernameErrorLabel.setText("");
        oldPasswordErrorLabel.setText("");
        newPasswordErrorLabel.setText("");
        statusLabel.setText("");
    }

    @FXML public void handleConfirmButton(ActionEvent actionEvent) throws IOException {
        String username = usernameTextField.getText();
        String oldPassword = oldPasswordTextField.getText();
        String newPassword = newPasswordTextField.getText();
        String confirmNewPassword =  confirmNewPasswordTextField.getText();

        usernameErrorLabel.setText("");
        oldPasswordErrorLabel.setText("");
        newPasswordErrorLabel.setText("");
        
        if(! accountList.searchUsername(username, accountList)){
            usernameErrorLabel.setText("ชื่อผู้ใช้ไม่ถูกต้อง");
            usernameErrorLabel.setTextFill(Color.web("#FF0000"));
            System.out.println("ชื่อผู้ใช้ไม่ถูกต้อง");
        } else if (! accountList.checkPassword(username, oldPassword, accountList)) {
            oldPasswordErrorLabel.setText("รหัสผ่านผิด");
            oldPasswordErrorLabel.setTextFill(Color.web("#FF0000"));
            System.out.println("รหัสผ่านผิด");
        } else if (oldPassword.equals(newPassword)) {
            newPasswordErrorLabel.setText("รหัสผ่านใหม่ไม่ถูกต้อง");
            newPasswordErrorLabel.setTextFill(Color.web("#FF0000"));
            System.out.println("รหัสผ่านใหม่ไม่ถูกต้อง");
        } else if (! newPassword.equals(confirmNewPassword)) {
            newPasswordErrorLabel.setText("รหัสผ่านไม่ตรงกัน");
            newPasswordErrorLabel.setTextFill(Color.web("#FF0000"));
            System.out.println("รหัสผ่านไม่ตรงกัน");
        }else{
            userAccount.changePassword(newPassword);
            accountList.addAccount(userAccount.getUsername(), userAccount);
            read.writeData(accountList);
            System.out.println("เปลี่ยนรหัสผ่านสำเร็จ");
            statusLabel.setText("เปลี่ยนรหัสผ่านสำเร็จ");
            statusLabel.setTextFill(Color.web("#00FF00"));

            usernameTextField.clear();
            oldPasswordTextField.clear();
            newPasswordTextField.clear();
            confirmNewPasswordTextField.clear();
            objectWrapper.addObject("account", userAccount);
        }
    }

    @FXML
    public void handleBackButton(ActionEvent actionEvent){
        try {
            com.github.saacsos.FXRouter.goTo("userProfile", objectWrapper);
        } catch (IOException e) {
            System.err.println("ไปที่หน้า userProfile ไม่ได้");
            System.err.println("ให้ตรวจสอบการกำหนด route");
            e.printStackTrace();
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
