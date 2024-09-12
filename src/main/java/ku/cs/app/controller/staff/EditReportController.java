package ku.cs.app.controller.staff;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import ku.cs.app.models.*;
import com.github.saacsos.FXRouter;
import ku.cs.app.services.ObjectWrapper;
import ku.cs.app.services.UserReportFileDataSource;

import java.io.File;
import java.io.IOException;

public class EditReportController {

    @FXML private Label categoryLabel;
    @FXML private ChoiceBox<String> choiceBox;
    @FXML private Label dateLabel;
    @FXML private Label detailLabel;
    @FXML private TextArea howToTextArea;
    @FXML private Label staffNameLabel;
    @FXML private Circle accountCircleImage;
    @FXML private Label staffNameLabelTop;
    @FXML private Label titleLabel;
    @FXML private AnchorPane parent;
    @FXML private HBox moreFirstHBox;
    @FXML private HBox moreSecondHBox;
    @FXML private HBox moreThirdHBox;
    @FXML private HBox moreFourthHBox;
    @FXML private HBox photoHBox1;
    @FXML private HBox photoHBox2;
    @FXML private HBox photoHBox3;

    @FXML private Label moreFirstLabel;
    @FXML private Label moreSecondLabel;
    @FXML private Label moreThirdLabel;
    @FXML private Label moreFourthLabel;

    @FXML private Label moreFirstTitleLabel;
    @FXML private Label moreSecondTitleLabel;
    @FXML private Label moreThirdTitleLabel;
    @FXML private Label moreFourthTitleLabel;
    @FXML private Label photoTitle;
    @FXML private ImageView photo;

    private Report selectedReport;
    private ObjectWrapper objectWrapper;
    private UserAccount userAccount;
    //    private ObjectWrapper accountWrapper;
    private Boolean isLightMode;
    private Category category;

    String lightModePath = new File(System.getProperty("user.dir") + File.separator + "/style/light_mode.css").toURI().toString();
    String darkModePath = new File(System.getProperty("user.dir") + File.separator + "/style/dark_mode.css").toURI().toString();

    @FXML
    public void initialize() {
//        selectedReport = (Report) FXRouter.getData();
        objectWrapper = (ObjectWrapper) FXRouter.getData();
        selectedReport = (Report) objectWrapper.getObject("selected");
        userAccount = (UserAccount) objectWrapper.getObject("account");
        category = (Category) objectWrapper.getObject("category");
        isLightMode = (Boolean) objectWrapper.getObject("mode");
        checkMode();

        moreFirstHBox.getChildren().clear();
        moreSecondHBox.getChildren().clear();
        moreThirdHBox.getChildren().clear();
        moreFourthHBox.getChildren().clear();
        photoHBox1.getChildren().clear();

        moreFirstHBox.setPadding(new Insets(0));
        moreSecondHBox.setPadding(new Insets(0));
        moreThirdHBox.setPadding(new Insets(0));
        moreFourthHBox.setPadding(new Insets(0));
        photoHBox1.setPadding(new Insets(0));


        if (category.getMoreFirstDetailBox() && selectedReport.getMoreFirstDetail() != "") {
            moreFirstTitleLabel = new Label(category.getMoreFirstDetailBoxTitle() + " :");
            moreFirstLabel = new Label(selectedReport.getMoreFirstDetail());
            moreFirstHBox.getChildren().add(moreFirstTitleLabel);
            moreFirstHBox.getChildren().add(moreFirstLabel);
            moreFirstLabel.setPadding(new Insets(0,0,0,15));
            moreFirstHBox.setPadding(new Insets(10, 10, 10, 30));
            moreFirstTitleLabel.setFont(new Font(15));
            moreFirstLabel.setFont(new Font(16));
        }

        if (category.getMoreSecondDetailBox() && selectedReport.getMoreSecondDetail() != "") {
            moreSecondTitleLabel = new Label(category.getMoreSecondDetailBoxTitle() + " :");
            moreSecondLabel = new Label(selectedReport.getMoreSecondDetail());
            moreSecondHBox.getChildren().add(moreSecondTitleLabel);
            moreSecondHBox.getChildren().add(moreSecondLabel);
            moreSecondLabel.setPadding(new Insets(0,0,0,15));
            moreSecondHBox.setPadding(new Insets(10, 10, 10, 30));
            moreSecondTitleLabel.setFont(new Font(15));
            moreSecondLabel.setFont(new Font(16));
        }

        if (category.getMoreThirdDetailBox() && selectedReport.getMoreThirdDetail() != "") {
            moreThirdTitleLabel = new Label(category.getMoreThirdDetailBoxTitle() + " :");
            moreThirdLabel = new Label(selectedReport.getMoreThirdDetail());
            moreThirdHBox.getChildren().add(moreThirdTitleLabel);
            moreThirdHBox.getChildren().add(moreThirdLabel);
            moreThirdLabel.setPadding(new Insets(0,0,0,15));
            moreThirdHBox.setPadding(new Insets(10, 10, 10, 30));
            moreThirdTitleLabel.setFont(new Font(15));
            moreThirdLabel.setFont(new Font(16));
        }

        if (category.getMoreFourthDetailBox() && selectedReport.getMoreFourthDetail() != "") {
            moreFourthTitleLabel = new Label(category.getMoreFourthDetailBoxTitle() + " :");
            moreFourthLabel = new Label(selectedReport.getMoreFourthDetail());
            moreFourthHBox.getChildren().add(moreFourthTitleLabel);
            moreFourthHBox.getChildren().add(moreFourthLabel);
            moreFourthLabel.setPadding(new Insets(0,0,0,15));
            moreFourthHBox.setPadding(new Insets(10, 10, 10, 30));
            moreFourthTitleLabel.setFont(new Font(15));
            moreFourthLabel.setFont(new Font(16));
        }

        ObservableList<String> list = FXCollections.observableArrayList("ยังไม่รับเรื่อง" ,"อยู่ระหว่างดำเนินการ", "เสร็จสิ้น");
        choiceBox.setItems(list);

        staffNameLabelTop.setText(userAccount.getName());
        String url = new File(System.getProperty("user.dir") + File.separator + userAccount.getImagePath()).toURI().toString();
        ImagePattern pattern = new ImagePattern(new Image(url));
        accountCircleImage.setFill(pattern);

        titleLabel.setText(selectedReport.getTitle());
        detailLabel.setText(selectedReport.getDetail());
        dateLabel.setText(selectedReport.getDate());
        categoryLabel.setText(selectedReport.getCategoryName());
        staffNameLabel.setText(selectedReport.getStaffName());
        //categoryLabel.setStyle("-fx-background-color: #" + selectedReport.getCategoryColor() + ";\n" +
        //        " -fx-background-radius: 100;");

        if (selectedReport.getHowTo() != "" /* || selectedReport.getStaffName() != "" */ || selectedReport.getStatus() != "") {
            howToTextArea.setText(selectedReport.getHowTo());
            choiceBox.setValue(selectedReport.getStatus());
        }
    }

    @FXML
    public void handleSubmitButton(ActionEvent actionEvent) {
        if (howToTextArea.getText() != "" /* && staffTextArea.getText() != "" */ && choiceBox.getSelectionModel().getSelectedItem() != null) {

            selectedReport.setHowTo(howToTextArea.getText());
            selectedReport.setStatus(choiceBox.getSelectionModel().getSelectedItem());
            selectedReport.setStaffName(userAccount.getName());
            selectedReport.setAgencyName(userAccount.getOrganization());

            try {
                new UserReportFileDataSource().writeData((ReportList) (objectWrapper.getObject("reportList")));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

//            howToTextArea.clear();
//            staffTextArea.clear();
//            choiceBox.getSelectionModel().clearSelection();

            try {
                //objectWrapper.addObject("mode", );
                FXRouter.goTo("staff", objectWrapper);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            System.out.println("Incomplete information");
        }
    }
    @FXML
    private void gotoAccountPage(MouseEvent mouseEvent) {

        try {
            //objectWrapper.addObject("mode", mode);
            com.github.saacsos.FXRouter.goTo("staffAccount", objectWrapper);
        } catch (IOException e) {
            System.err.println("ไปที่หน้า editReport ไม่ได้");
            System.err.println("ให้ตรวจสอบการกำหนด route");
        }
    }

    @FXML
    public void gotoLoginPage(ActionEvent actionEvent) {

        try {
            //objectWrapper.addObject("mode", mode);
            com.github.saacsos.FXRouter.goTo("login", objectWrapper);
        } catch (IOException e) {
            System.err.println("ไปที่หน้า editReport ไม่ได้");
            System.err.println("ให้ตรวจสอบการกำหนด route");
        }
    }

    @FXML
    public void handleBackButton(ActionEvent actionEvent){
        try {
            //objectWrapper.addObject("mode", mode);
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