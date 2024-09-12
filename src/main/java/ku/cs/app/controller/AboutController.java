package ku.cs.app.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import ku.cs.app.models.Theme;
import ku.cs.app.services.ObjectWrapper;

import java.io.File;
import java.io.IOException;

public class AboutController {
    @FXML
    private Circle circleImageKean;

    @FXML
    private Circle circleImagePop;

    @FXML
    private Circle circleImageTam;

    @FXML
    private Circle circleImageTat;
    @FXML private AnchorPane parent;

    private Boolean isLightMode;
    private ObjectWrapper objectWrapper;

//    private Theme mode;

    String lightModePath = new File(System.getProperty("user.dir") + File.separator + "/style/light_mode.css").toURI().toString();
    String darkModePath = new File(System.getProperty("user.dir") + File.separator + "/style/dark_mode.css").toURI().toString();
    @FXML public void initialize(){
        String TatUrl = getClass().getResource("/ku/cs/images/Tat_Pic.jpg").toExternalForm();
        String PopUrl = getClass().getResource("/ku/cs/images/Pop_Pic.jpg").toExternalForm();
        String KeanUrl = getClass().getResource("/ku/cs/images/Kean_Pic.jpg").toExternalForm();
        String TamUrl = getClass().getResource("/ku/cs/images/Tam_Pic.jpg").toExternalForm();

        objectWrapper = (ObjectWrapper) com.github.saacsos.FXRouter.getData();
        isLightMode = (Boolean) objectWrapper.getObject("mode");
        checkMode();

        ImagePattern pattern1 = new ImagePattern(new Image(TatUrl));
        ImagePattern pattern2 = new ImagePattern(new Image(PopUrl));
        ImagePattern pattern3 = new ImagePattern(new Image(KeanUrl));
        ImagePattern pattern4 = new ImagePattern(new Image(TamUrl));
        circleImagePop.setFill(pattern2);
        circleImageKean.setFill(pattern3);
        circleImageTat.setFill(pattern1);
        circleImageTam.setFill(pattern4);

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

    @FXML
    public void handleBackButton(ActionEvent actionEvent){
        try {
            com.github.saacsos.FXRouter.goTo("login", objectWrapper);
        } catch (IOException e) {
            System.err.println("ไปที่หน้า login ไม่ได้");
            System.err.println("ให้ตรวจสอบการกำหนด route");
        }
    }
}