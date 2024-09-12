package ku.cs.app.controller;

import com.github.saacsos.FXRouter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
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

public class BannedUserBoxController {
//    public static void display(String title, String message){
//        Stage window = new Stage();
//        window.initModality(Modality.APPLICATION_MODAL);
//        window.setMinWidth(1024);
//    }

    @FXML
    TextField usernameTextField;
    @FXML
    TextArea appealTextArea;
    @FXML
    Label errorLabel;
    @FXML
    AnchorPane parent;

    private DataSource<UserAccountList> read = new UserAccountFileDataSource();
    private UserAccountList accountList = read.readData();
    private ObjectWrapper objectWrapper;
    private UserAccount userAccount;
    private Boolean isLightMode;

    String lightModePath = new File(System.getProperty("user.dir") + File.separator + "/style/light_mode.css").toURI().toString();
    String darkModePath = new File(System.getProperty("user.dir") + File.separator + "/style/dark_mode.css").toURI().toString();

    public void initialize() {
        objectWrapper = (ObjectWrapper) FXRouter.getData();
        System.out.println("initialize BannedUserBoxController");
        userAccount = (UserAccount) objectWrapper.getObject("account");
        isLightMode = (Boolean) objectWrapper.getObject("mode");
        System.out.println(userAccount);
        checkMode();
//        Node node = (Node) actionEvent.getSource();
//        Stage stage = (Stage) node.getScene().getWindow();
//        userAccount = (UserAccount) stage.getUserData();
//        System.out.println(userAccount);
    }

    @FXML public void handleAppealButton(ActionEvent actionEvent) throws IOException {
        String username = usernameTextField.getText();
        String appealText = appealTextArea.getText();
        if(userAccount.getUsername().equals(username)){
            for(String account : accountList.getAllAccounts()){
                UserAccount acc = accountList.find(account);
                if(acc.getUsername().equals(username)){
                    acc.setAppealText(appealText);
                }
            }
            read.writeData(accountList);
            System.out.println("appeal saved");
            errorLabel.setText("ขอคืนสิทธ์สำเร็จ");
            errorLabel.setTextFill(Color.web("#49A239"));

            usernameTextField.clear();
            appealTextArea.clear();
        }else{
            errorLabel.setText("ชื่อผู้ใช้ไม่ถูกต้อง");
            errorLabel.setTextFill(Color.web("#FF0000"));
        }
    }

    @FXML public void handleBackButton(ActionEvent actionEvent){
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

    private void setLightMode() {
        parent.getStylesheets().add(lightModePath);
        parent.getStylesheets().remove(darkModePath);
    }
    private void setDarkMode() {
        parent.getStylesheets().add(darkModePath);
        parent.getStylesheets().remove(lightModePath);
    }
}
