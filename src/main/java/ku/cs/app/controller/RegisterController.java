package ku.cs.app.controller;

import com.github.saacsos.FXRouter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import ku.cs.app.models.Theme;
import ku.cs.app.models.UserAccount;
import ku.cs.app.models.UserAccountList;
import ku.cs.app.services.DataSource;
import ku.cs.app.services.ObjectWrapper;
import ku.cs.app.services.UserAccountFileDataSource;
import com.github.saacsos.FXRouter;

import java.io.File;
import java.io.IOException;

public class RegisterController {

    @FXML private TextField usernameTextField;
    @FXML private TextField passwordTextField;
    @FXML private TextField confirmPasswordTextField;
    @FXML private TextField nameTextField;
//    @FXML private Label usernameErrorLabel;
//    @FXML private Label passwordErrorLabel;
    @FXML private Label errorLabel;
    @FXML private AnchorPane parent;
    @FXML private ImageView modeImage;
    private boolean isLightMode;
    private ObjectWrapper objectWrapper;
    private DataSource<UserAccountList> read = new UserAccountFileDataSource();
    private UserAccountList accountList = read.readData();

    String lightModePath = new File(System.getProperty("user.dir") + File.separator + "/style/light_mode.css").toURI().toString();
    String darkModePath = new File(System.getProperty("user.dir") + File.separator + "/style/dark_mode.css").toURI().toString();

    @FXML
    public void initialize(){
        objectWrapper = (ObjectWrapper) FXRouter.getData();
//        mode = (Theme) objectWrapper.getObject("mode");
        isLightMode = (boolean) objectWrapper.getObject("mode");
        checkMode();
        System.out.println("initialize RegisterController");
    }

    @FXML
    public void handleRegisterButton(ActionEvent actionEvent) throws IOException {
        String username = usernameTextField.getText();
        String password = passwordTextField.getText();
        String confirmPassword = confirmPasswordTextField.getText();
        String name = nameTextField.getText();

        UserAccountList accounts = new UserAccountList();
//        usernameErrorLabel.setText("");
//        passwordErrorLabel.setText("");
        if(!password.equals(confirmPassword)){
            errorLabel.setText("รหัสผ่านไม่ตรงกัน");
            errorLabel.setTextFill(Color.web("#DE493D"));
            System.out.println("รหัสผ่านไม่ตรงกัน");
        } else if(username.equals("") || password.equals("")){
            errorLabel.setText("โปรดกรอกข้อมูลให้ครบ");
            errorLabel.setTextFill(Color.web("#DE493D"));
            System.out.println("กรอกข้อมูลไม่ครบ");
        } else if (password.contains(",")) {
            errorLabel.setText("รหัสผ่านต้องไม่มี\",\"");
            errorLabel.setTextFill(Color.web("#DE493D"));
            System.out.println("password  contains \",\"");
        } else if (accountList.searchUsername(username, accountList)) {
            errorLabel.setText("ชื่อบัญชีนี้มีคนใช้แล้ว");
            errorLabel.setTextFill(Color.web("#DE493D"));
            System.out.println("ชื่อนบัญชีมีคนใช้แล้ว");
        } else if (accountList.searchName(name, accountList)){
            errorLabel.setText("ชื่อนี้มีคนใช้แล้ว");
            errorLabel.setTextFill(Color.web("#DE493D"));
            System.out.println("ชื่อนี้มีคนใช้แล้ว");
        } else{
            UserAccount account = new UserAccount(username, password, name);
            account.setUsername(username);
            account.setPassword(password);
            account.setName(name);
            account.setOrganization("");
            account.setRole("user");
            accountList.addAccount(account.getUsername(), account);
//            accounts.addAccount(new UserAccount(username, password, name,"","user"));

            read.writeData(accountList);
            System.out.println(account);
            System.out.println("===");
            System.out.println(accounts);
            System.out.println("สมัครสมาชิกสำเร็จ");
            try {
                FXRouter.goTo("login");
            } catch (IOException e) {
                System.err.println("ไปที่หน้า login ไม่ได้");
                System.err.println("ให้ตรวจสอบการกำหนด route");
            }
        }
    }

    @FXML
    public void handleBackButton(ActionEvent actionEvent){
        try {
            com.github.saacsos.FXRouter.goTo("login", objectWrapper);
        } catch (IOException e) {
            System.err.println("ไปที่หน้า login ไม่ได้");
            System.err.println("ให้ตรวจสอบการกำหนด route");
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
    }
    private void setDarkMode() {
        parent.getStylesheets().add(darkModePath);
        parent.getStylesheets().remove(lightModePath);
        Image image = new Image(getClass().getResourceAsStream("/ku/cs/images/moon.png"));
        modeImage.setImage(image);
    }
}
