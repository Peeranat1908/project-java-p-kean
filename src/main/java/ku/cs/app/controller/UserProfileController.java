package ku.cs.app.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import ku.cs.app.models.UserAccount;
import ku.cs.app.models.UserAccountList;
import ku.cs.app.services.DataSource;
import ku.cs.app.services.ObjectWrapper;
import ku.cs.app.services.UserAccountFileDataSource;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class UserProfileController {
    @FXML ImageView userImage;
    @FXML Label nameLabel;
    @FXML
    AnchorPane parent;
    @FXML ImageView modeImage;
    private DataSource<UserAccountList> read = new UserAccountFileDataSource();
    private UserAccountList accountList = read.readData();
    private UserAccount userAccount;
    private ObjectWrapper objectWrapper;
    private Boolean isLightMode;

    String lightModePath = new File(System.getProperty("user.dir") + File.separator + "/style/light_mode.css").toURI().toString();
    String darkModePath = new File(System.getProperty("user.dir") + File.separator + "/style/dark_mode.css").toURI().toString();

    @FXML
    public void initialize(){
        objectWrapper = (ObjectWrapper) com.github.saacsos.FXRouter.getData();
        userAccount = (UserAccount) objectWrapper.getObject("account");
        isLightMode = (Boolean) objectWrapper.getObject("mode");
        checkMode();

//        userAccount = (UserAccount) com.github.saacsos.FXRouter.getData();
        nameLabel.setText(userAccount.getName());
//        String url = getClass().getResource(userAccount.getImagePath()).toExternalForm();
        String url = new File(System.getProperty("user.dir") + File.separator + userAccount.getImagePath()).toURI().toString();
        userImage.setImage(new Image(url));
    }

    @FXML
    public void handleBackButton(ActionEvent actionEvent) {
        try {
            com.github.saacsos.FXRouter.goTo("user", objectWrapper);
        } catch (IOException e) {
            System.err.println("ไปที่หน้า user ไม่ได้");
            System.err.println("ให้ตรวจสอบการกำหนด route");
            e.printStackTrace();
        }
    }

    @FXML
    public void handleChangePasswordButton(ActionEvent actionEvent){
        try {
            com.github.saacsos.FXRouter.goTo("changePassword", objectWrapper);
        } catch (IOException e) {
            System.err.println("ไปที่หน้าเปลี่ยนรหัสผ่านไม่ได้");
            System.err.println("ให้ตรวจสอบการกำหนด route");
            e.printStackTrace();
        }
    }

    @FXML
    public void handleUploadImageButton(ActionEvent actionEvent){
        // Create a FileChooser object
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        // define which file we wanted
        FileChooser.ExtensionFilter filter1 = new FileChooser.ExtensionFilter("ALL Files", "*.*");
        FileChooser.ExtensionFilter filter2 = new FileChooser.ExtensionFilter("JPEG Files", "*.jpg");
        FileChooser.ExtensionFilter filter3 = new FileChooser.ExtensionFilter("PNG Files", "*.png");
        fileChooser.getExtensionFilters().addAll(filter1, filter2, filter3);

        Node source = (Node) actionEvent.getSource();
        File file = fileChooser.showOpenDialog(source.getScene().getWindow());
        if (file != null){
            try {
                File destDir = new File("images");
                if (!destDir.exists()){
                    destDir.mkdirs();
                }
                // RENAME FILE
                String[] fileSplit = file.getName().split("\\.");
                //String filename = LocalDate.now() + "_"+System.currentTimeMillis() + "." + fileSplit[fileSplit.length - 1];
                String filename = userAccount.getUsername() + "_" + "Pic" + "." + fileSplit[fileSplit.length - 1].toLowerCase();
                Path target = FileSystems.getDefault().getPath(destDir.getAbsolutePath()+System.getProperty("file.separator")+filename);
                Files.copy(file.toPath(), target, StandardCopyOption.REPLACE_EXISTING );
                userImage.setImage(new Image(target.toUri().toString()));
                //System.out.println(target.toString());
//                    selectedMemberCard.setImagePath(destDir + "/" + filename);
//                    dataSource.writeData(cardList);
                userAccount.setImagePath("images" + File.separator + filename);
                accountList.addAccount(userAccount.getUsername(), userAccount);
                read.writeData(accountList);
            } catch (IOException e) {
                e.printStackTrace();
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
