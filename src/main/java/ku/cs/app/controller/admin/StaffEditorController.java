package ku.cs.app.controller.admin;

import com.github.saacsos.FXRouter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.fxml.Initializable;
import ku.cs.app.models.UserAccount;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.fxml.Initializable;
import ku.cs.app.models.UserAccount;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import ku.cs.app.models.UserAccountList;
import ku.cs.app.services.DataSource;
import ku.cs.app.services.ObjectWrapper;
import ku.cs.app.services.UserAccountFileDataSource;
import com.github.saacsos.FXRouter;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class StaffEditorController implements Initializable {

    @FXML private AnchorPane parent;
    @FXML private ImageView modeImage;

    private DataSource<UserAccountList> read = new UserAccountFileDataSource();
    private UserAccountList accountList = read.readData();
    private ObjectWrapper objectWrapper;
    private Boolean isLightMode;
    private String url;

    //    @FXML
//    public void initialize() {
//        objectWrapper = (ObjectWrapper) FXRouter.getData();
//        userAccount = (UserAccount) objectWrapper.getObject("account");
//        isLightMode = (Boolean) objectWrapper.getObject("mode");
//
//        uploadImageView.setImage(new Image(getClass().getResource("/ku/cs/images/default.jpg").toExternalForm()));
//    }

    String lightModePath = new File(System.getProperty("user.dir") + File.separator + "/style/light_mode.css").toURI().toString();
    String darkModePath = new File(System.getProperty("user.dir") + File.separator + "/style/dark_mode.css").toURI().toString();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//        userAccount = (UserAccount) com.github.saacsos.FXRouter.getData();
        objectWrapper = (ObjectWrapper) FXRouter.getData();
        isLightMode = (Boolean) objectWrapper.getObject("mode");
        checkMode();
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

    @FXML public void handleAdminButton(ActionEvent actionEvent) {
        try {
            com.github.saacsos.FXRouter.goTo("adminEditor", objectWrapper);
        } catch (IOException e) {
            System.err.println("ไปที่หน้า adminEditor ไม่ได้");
            System.err.println("ให้ตรวจสอบการกำหนด route");
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

    @FXML public void handleBannedUserListButton(ActionEvent actionEvent){
        try {
            com.github.saacsos.FXRouter.goTo("bannedUserList", objectWrapper);
        } catch (IOException e) {
            System.out.println("ไปที่หน้า bannedUserList ไม่ได้");
            System.out.println("ให้ตรวจสอบการกำหนด route");
            e.printStackTrace();
        }
    }

    @FXML public void handleAddStaffAccountButton(ActionEvent actionEvent) {
        try {
            com.github.saacsos.FXRouter.goTo("addStaffAccount", objectWrapper);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("ไปที่หน้า addStaffAccount ไม่ได้");
            System.err.println("ให้ตรวจสอบการกำหนด route");
            e.printStackTrace();
        }
    }

    @FXML public void handleOrganizationEditorButton(ActionEvent actionEvent) {
        try {
            com.github.saacsos.FXRouter.goTo("organizationEditor", objectWrapper);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("ไปที่หน้า organizationEditor ไม่ได้");
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

