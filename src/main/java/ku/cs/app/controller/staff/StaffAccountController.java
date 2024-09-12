package ku.cs.app.controller.staff;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import ku.cs.app.models.Category;
import ku.cs.app.models.Theme;
import ku.cs.app.models.UserAccount;
import com.github.saacsos.FXRouter;
import ku.cs.app.models.UserAccountList;
import ku.cs.app.services.DataSource;
import ku.cs.app.services.ObjectWrapper;
import ku.cs.app.services.UserAccountFileDataSource;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Calendar;
import java.util.ResourceBundle;


public class StaffAccountController implements Initializable {
    @FXML
    private ImageView accountImage;

    @FXML
    private Label agencyLabel;

    @FXML
    private Label categoryLabel;

    @FXML
    private Label nameLabel;

    @FXML
    private TextField newPasswordTextField;

    @FXML
    private TextField passwordTextField;

    @FXML
    private Label staffNameLabel;

    @FXML
    private TextField usernameTextField;
    @FXML
    private Label errorLabel;
    @FXML
    private Circle accountCircleImage;
    @FXML
    AnchorPane parent;
    private UserAccount userAccount;
    private UserAccountList accountList;
    private DataSource<UserAccountList> read = new UserAccountFileDataSource();
    private ObjectWrapper objectWrapper;
    private Category category;

    private Boolean isLightMode;
    String lightModePath = new File(System.getProperty("user.dir") + File.separator + "/style/light_mode.css").toURI().toString();
    String darkModePath = new File(System.getProperty("user.dir") + File.separator + "/style/dark_mode.css").toURI().toString();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        userAccount = (UserAccount) FXRouter.getData();
        objectWrapper = (ObjectWrapper) FXRouter.getData();
        userAccount = (UserAccount) objectWrapper.getObject("account");
        accountList = (UserAccountList) objectWrapper.getObject("accountList");
        isLightMode = (Boolean) objectWrapper.getObject("mode");
        category = (Category) objectWrapper.getObject("category");
        checkMode();
        showInformation();
    }

    private void showInformation() {
        staffNameLabel.setText(userAccount.getName());
        nameLabel.setText(userAccount.getName());
        agencyLabel.setText(userAccount.getOrganization());
        categoryLabel.setText(category.getTitle());

        String url = new File(System.getProperty("user.dir") + File.separator + userAccount.getImagePath()).toURI().toString();
        ImagePattern pattern = new ImagePattern(new Image(url));
        accountCircleImage.setFill(pattern);

        accountImage.setImage(new Image(url));

    }

    @FXML
    void changePasswordButton(ActionEvent event) throws IOException {
        String username = usernameTextField.getText();
        String oldPassword = passwordTextField.getText();
        String newPassword = newPasswordTextField.getText();

//        usernameErrorLabel.setText("");
//        oldPasswordErrorLabel.setText("");
//        newPasswordErrorLabel.setText("");


        if(!accountList.searchUsername(username, accountList) || !username.equals(userAccount.getUsername())){
            errorLabel.setText("ชื่อผู้ใช้ไม่ถูกต้อง");
            errorLabel.setTextFill(Color.web("#DE493D"));
            System.out.println("ชื่อผู้ใช้ไม่ถูกต้อง");
        } else if (! accountList.checkPassword(username, oldPassword, accountList)) {
            errorLabel.setText("รหัสผ่านไม่ถูกต้อง");
            errorLabel.setTextFill(Color.web("#DE493D"));
            System.out.println("รหัสผ่านไม่ถูกต้อง");
        }  else if (oldPassword.equals(newPassword)) {
            errorLabel.setText("รหัสผ่านใหม่ไม่ถูกต้อง");
            errorLabel.setTextFill(Color.web("#DE493D"));
            System.out.println("รหัสผ่านใหม่ไม่ถูกต้อง");
        } else{
            userAccount.changePassword(newPassword);
            read.writeData(accountList);
            errorLabel.setText("เปลี่ยนรหัสผ่านสำเร็จ");
            errorLabel.setTextFill(Color.web("#49A239"));
            System.out.println("เปลี่ยนรหัสผ่านสำเร็จ");

            usernameTextField.clear();
            passwordTextField.clear();
            newPasswordTextField.clear();
        }
    }

    @FXML
    public void gotoLoginPage(ActionEvent actionEvent) {

        try {
            objectWrapper.addObject("mode", isLightMode);
            com.github.saacsos.FXRouter.goTo("login", objectWrapper);
        } catch (IOException e) {
            System.err.println("ไปที่หน้า editReport ไม่ได้");
            System.err.println("ให้ตรวจสอบการกำหนด route");
        }
    }
    @FXML
    public void handleBackButton(ActionEvent actionEvent){
        try {
            objectWrapper.addObject("mode",isLightMode);
            com.github.saacsos.FXRouter.goTo("staff", objectWrapper);
        } catch (IOException e) {
            System.err.println("ไปที่หน้า home ไม่ได้");
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
    }
    private void setDarkMode() {
        parent.getStylesheets().add(darkModePath);
        parent.getStylesheets().remove(lightModePath);
    }
}