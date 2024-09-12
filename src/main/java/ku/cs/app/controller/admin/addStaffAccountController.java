package ku.cs.app.controller.admin;

import com.github.saacsos.FXRouter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import ku.cs.app.models.Organization;
import ku.cs.app.models.OrganizationList;
import ku.cs.app.models.UserAccount;
import ku.cs.app.models.UserAccountList;
import ku.cs.app.services.DataSource;
import ku.cs.app.services.ObjectWrapper;
import ku.cs.app.services.OrganizationFileDataSource;
import ku.cs.app.services.UserAccountFileDataSource;

import com.github.saacsos.FXRouter;

import javax.swing.event.AncestorEvent;
import java.io.*;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

public class addStaffAccountController {

    @FXML private TextField usernameTextField;

    @FXML private TextField  nameTextField;

    @FXML private TextField  passwordTextField;
    @FXML private TextField passwordConfirmTextField;
    @FXML private ImageView uploadImageView;
    @FXML private ComboBox<String> orgComboBox;
    @FXML private Label errorLabel;
    @FXML private Label completeLabel;
    @FXML private AnchorPane parent;
    @FXML private ImageView modeImage;

    private OrganizationFileDataSource organizationFileDataSource;
    private OrganizationList organizationList;

    private List<Organization> organizations = new ArrayList<>();

    private DataSource<UserAccountList> read = new UserAccountFileDataSource();
    private UserAccountList accountList = read.readData();
    private UserAccount userAccount;

    private String url;
    private String defaultUrl;

    private Boolean isLightMode;
    private ObjectWrapper objectWrapper;

    String lightModePath = new File(System.getProperty("user.dir") + File.separator + "/style/light_mode.css").toURI().toString();
    String darkModePath = new File(System.getProperty("user.dir") + File.separator + "/style/dark_mode.css").toURI().toString();

    @FXML
    public void initialize() {
        objectWrapper = (ObjectWrapper) FXRouter.getData();
        isLightMode = (Boolean) objectWrapper.getObject("mode");
        checkMode();

        defaultUrl = getClass().getResource("/ku/cs/images/default.jpg").toExternalForm();
        uploadImageView.setImage(new Image(defaultUrl));
        showComboBox();
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
            e.printStackTrace();
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
    @FXML public void handleAdminButton(ActionEvent actionEvent) {
        try {
            com.github.saacsos.FXRouter.goTo("adminEditor", objectWrapper);
        } catch (IOException e) {
            System.err.println("ไปที่หน้า adminEditor ไม่ได้");
            System.err.println("ให้ตรวจสอบการกำหนด route");
            e.printStackTrace();
        }
    }
    @FXML public void handleBannedUserListButton(ActionEvent actionEvent){
        try {
            com.github.saacsos.FXRouter.goTo("bannedUserList", userAccount);
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

    //register part
    @FXML
    public void handleRegisterButton(ActionEvent actionEvent) throws IOException {
        String username = usernameTextField.getText();
        String password = passwordTextField.getText();
        String confirmPassword = passwordConfirmTextField.getText();
        String name = nameTextField.getText();
//        String organization = organizationTextField.getText();
        String organization = orgComboBox.getValue();
        completeLabel.setText("");
        UserAccountList accounts = new UserAccountList();
        if (username == "" || password == "" || confirmPassword == "" || name == "" || organization == "") {
            errorLabel.setText("ข้อมูลไม่ถูกต้อง");
        } else if(!password.equals(confirmPassword)){
            errorLabel.setText("รหัสผ่านไม่ตรงกัน");
            System.out.println("รหัสผ่านไม่ตรงกัน");
        } else if (accountList.searchUsername(username, accountList)) {
            errorLabel.setText("ชื่อบัญชีนี้ถูกใช้แล้ว");
            System.out.println("ชื่อบัญชีนี้ถูกใช้แล้ว");
            errorLabel.setText("ชื่อบัญชีนี้ถูกใช้แล้ว");
        } else if(accountList.searchName(name, accountList)) {
            errorLabel.setText("ชื่อนี้ถูกใช้แล้ว");
            System.out.println("ชื่อนี้ถูกใช้แล้ว");
            errorLabel.setText("ชื่อนี้ถูกใช้แล้ว");
        } else{
            userAccount = new UserAccount(username, password, name, organization, "staff");
            // rewrite Image
            // find and acc that had been writing on the code above
            accountList.addAccount(userAccount.getUsername(), userAccount);
            DataSource<UserAccountList> write = new UserAccountFileDataSource();
            write.writeData(accountList);
            if (url != null){
                try {
                    File file = new File(url);
                    File destDir = new File("images");
                    if (!destDir.exists()){
                        destDir.mkdirs();
                    }
                    String[] fileSplit = file.getName().split("\\.");
                    String filename = username + "_" + "Pic" + "." + fileSplit[fileSplit.length - 1].toLowerCase();
                    Path target = FileSystems.getDefault().getPath(destDir.getAbsolutePath()+System.getProperty("file.separator")+filename);
                    Files.copy(file.toPath(), target, StandardCopyOption.REPLACE_EXISTING );
                    uploadImageView.setImage(new Image(target.toUri().toString()));
                    userAccount.setImagePath("images" + File.separator + filename);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            read.writeData(accountList);
            System.out.println("Welcome!");
            errorLabel.setText("");
            completeLabel.setText("สมัครสำเร็จ");

            usernameTextField.clear();
            passwordTextField.clear();
            passwordConfirmTextField.clear();
            nameTextField.clear();
            orgComboBox.getSelectionModel().clearSelection();

            uploadImageView.setImage(new Image(defaultUrl));

//            objectWrapper.addObject("account", userAccount);

//            try {
//                com.github.saacsos.FXRouter.goTo("staffEditor");
//            } catch (IOException e) {
//                System.err.println("ไปที่หน้า staffEditor ไม่ได้");
//                System.err.println("ให้ตรวจสอบการกำหนด route");
//            }
        }
    }

    @FXML
    public void handleUploadImageButton(ActionEvent actionEvent) {
        // Create a FileChooser object
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        // define which file we wanted
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("images PNG JPG", "*.png", "*.jpg", "*.jpeg"));
        Node source = (Node) actionEvent.getSource();
        File file = fileChooser.showOpenDialog(source.getScene().getWindow());
        if (file != null) {
            try {
                // RENAME FILE
                uploadImageView.setImage(new Image(new FileInputStream(file.getAbsolutePath())));
                url = file.getAbsolutePath();
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
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