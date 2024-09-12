package ku.cs.app.controller;

import com.github.saacsos.FXRouter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
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

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class LoginController {
    @FXML private TextField usernameTextField;
    @FXML private TextField passwordTextField;
    @FXML private PasswordField passwordHidden;
    @FXML private CheckBox checkBox;
    @FXML private Label errorLabel;
    @FXML private AnchorPane parent;
    @FXML private ImageView modeImage;


    private DateTimeFormatter tf = DateTimeFormatter.ofPattern("hh:mm:ss");
    private DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private DataSource<UserAccountList> read = new UserAccountFileDataSource();
    private UserAccountList accountList = read.readData();
    UserAccount userAccount = new UserAccount();
    private ObjectWrapper objectWrapper;
    private boolean isLightMode;

    String lightModePath = new File(System.getProperty("user.dir") + File.separator + "/style/light_mode.css").toURI().toString();
    String darkModePath = new File(System.getProperty("user.dir") + File.separator + "/style/dark_mode.css").toURI().toString();

    @FXML public void initialize(){
        if (FXRouter.getData() != null) {
            objectWrapper = (ObjectWrapper) FXRouter.getData();
            isLightMode = (boolean) objectWrapper.getObject("mode");
        } else {
            objectWrapper = new ObjectWrapper();
            isLightMode = true;
        }
        checkMode();
        System.out.println("initialize LoginController");
        errorLabel.setText("");
    }

    @FXML void changeVisibility(ActionEvent event){
        if(checkBox.isSelected()){
            passwordTextField.setText(passwordHidden.getText());
            passwordTextField.setVisible(true);
            passwordHidden.setVisible(false);
            return;
        }
        passwordHidden.setText(passwordTextField.getText());
        passwordHidden.setVisible(true);
        passwordTextField.setVisible(false);
    }
    @FXML
    void handleHowToButton(ActionEvent event){
        try {
            Desktop.getDesktop().open(new File("data" + File.separator + "Introduction.pdf"));
        } catch (IOException e) {
            System.err.println("ไปที่หน้า คู่มือ ไม่ได้");
            System.err.println("ให้ตรวจสอบการกำหนด route");
            e.printStackTrace();
        }
    }
    @FXML public void handleAboutButton(ActionEvent actionEvent){
        try {
            objectWrapper.addObject("mode", isLightMode);
            com.github.saacsos.FXRouter.setAnimationType("fade", 500);
            com.github.saacsos.FXRouter.goTo("about", objectWrapper);
//            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ku/cs/about.fxml"));
//            Parent root1 =  fxmlLoader.load();
//            Stage stage = new Stage();
//            stage.setTitle("JavaDeeKub");
//            stage.setScene((new Scene(root1)));
//            stage.show();

        } catch (IOException e) {
            System.err.println("ไปที่หน้า home ไม่ได้");
            System.err.println("ให้ตรวจสอบการกำหนด route");
            e.printStackTrace();
        }
    }

    @FXML public void handleRegisterButton(ActionEvent actionEvent) {
        try {
            System.out.println(objectWrapper.toString());
            objectWrapper.addObject("mode", isLightMode);
            com.github.saacsos.FXRouter.goTo("register", objectWrapper);
        } catch (IOException e) {
            System.err.println("ไปที่หน้า register ไม่ได้");
            System.err.println("ให้ตรวจสอบการกำหนด route");
            e.printStackTrace();
        }
    }

    @FXML public void handleLoginButton(ActionEvent actionEvent) throws IOException {
        String username = usernameTextField.getText();
        String password = passwordTextField.getText();
        String hidden = passwordHidden.getText();

        if(accountList.verifyLogin(username, password, hidden, accountList)){
            for(String account : accountList.getAllAccounts()){
                UserAccount acc = accountList.find(account);
                if(acc.getUsername().equals(username)){
//                    userAccount.setUsername(acc.getUsername());
//                    userAccount.setPassword(acc.getPassword());
//                    userAccount.setName(acc.getName());
//                    userAccount.setOrganization(acc.getOrganization());
//                    userAccount.setRole(acc.getRole());
//                    userAccount.setImagePath(acc.getImagePath());
//                    userAccount.setDate(LocalDate.now().format(df));
//                    userAccount.setTime(LocalTime.now().format(tf));
//                    userAccount.setUserStatus(acc.getUserStatus());
//                    userAccount.setAppealText(acc.getAppealText());
//                    userAccount.setLoginAttempt(acc.getLoginAttempt());
//                    userAccount.setGotReported(acc.getGotReported());

//                    userAccount.reWrite(accountList, userAccount);
                    acc.setUsername(acc.getUsername());
                    acc.setPassword(acc.getPassword());
                    acc.setName(acc.getName());
                    acc.setOrganization(acc.getOrganization());
                    acc.setRole(acc.getRole());
                    acc.setImagePath(acc.getImagePath());
                    acc.setDate(LocalDate.now().format(df));
                    acc.setTime(LocalTime.now().format(tf));
                    acc.setUserStatus(acc.getUserStatus());
                    acc.setAppealText(acc.getAppealText());
                    acc.setLoginAttempt(acc.getLoginAttempt());
                    acc.setGotReported(acc.getGotReported());
                    read.writeData(accountList);

                    objectWrapper.addObject("account", acc);
                    objectWrapper.addObject("accountList", accountList);
                    objectWrapper.addObject("mode", isLightMode);
                    //read.writeData(accountList);
                    if(acc.getUserStatus().equals("banned")){
                        System.out.println("This user is banned");
                        acc.setLoginAttempt(String.valueOf(Integer.parseInt(acc.getLoginAttempt())+1));
                        read.writeData(accountList);
                        try {
//                            objectWrapper.addObject("banedAccount", userAccount);
//                            objectWrapper.addObject("mode", isLightMode);
                            com.github.saacsos.FXRouter.goTo("bannedUserBox", objectWrapper);
                        } catch (IOException e) {
                            System.err.println("ไปที่หน้า bannedUserBox ไม่ได้");
                            System.err.println("ให้ตรวจสอบการกำหนด route");
                        }
    //                        URL url = new File("src/main/resources/ku/cs/bannedUserBox.fxml").toURI().toURL();    //POP-UP Window
    //                        Parent root = FXMLLoader.load(url);       //POP-UP Window
    //                        Stage stage = new Stage();                //POP-UP Window
    //                        stage.setScene(new Scene(root));          //POP-UP Window
    //                        stage.show();                             //POP-UP Window

    //                        Node node = (Node) actionEvent.getSource();
    //                        Stage stage = (Stage) node.getScene().getWindow();
    //                        stage.close();
    //                        try {
    //                            URL url = new File("src/main/resources/ku/cs/bannedUserBox.fxml").toURI().toURL();
    //                            Parent root = FXMLLoader.load(url);
    //                            stage.setUserData(userAccount);
    //                            stage.setScene(new Scene(root));
    //                            stage.show();
    //                        } catch (IOException e){
    //                            System.err.println(String.format("Error: %s", e.getMessage()));
    //                        }

                    }else {
                        System.out.println("เข้าสู่ระบบเรียบร้อย");

                        if(acc.getRole().equals("user")){
                            try {
                                com.github.saacsos.FXRouter.goTo("user", objectWrapper);
                            } catch (IOException e) {
                                System.err.println("ไปที่หน้า user ไม่ได้");
                                System.err.println("ให้ตรวจสอบการกำหนด route");
                                e.printStackTrace();
                            }
                        } else if (acc.getRole().equals("staff")) {
                            try {
//                                objectWrapper.addObject("account", userAccount);
//                                objectWrapper.addObject("accountList", accountList);
//                                objectWrapper.addObject("mode", isLightMode);
                                com.github.saacsos.FXRouter.goTo("staff", objectWrapper);
                            } catch (IOException e) {
                                System.err.println("ไปที่หน้า staff ไม่ได้");
                                System.err.println("ให้ตรวจสอบการกำหนด route");
                                e.printStackTrace();
                            }

                        } else if (acc.getRole().equals("admin")) {
                            try {
                                com.github.saacsos.FXRouter.goTo("userEditor", objectWrapper);
                            } catch (IOException e) {
                                System.err.println("ไปที่หน้า userEditor ไม่ได้");
                                System.err.println("ให้ตรวจสอบการกำหนด route");
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }else if(! accountList.verifyLogin(username, password, hidden, accountList)){
            errorLabel.setText("ชื่อผู้ใช้หรือรหัสผ่านผิด");
            errorLabel.setTextFill(Color.web("#DE493D"));
            System.out.println("ชื่อผู้ใช้หรือรหัสผ่านผิด");
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