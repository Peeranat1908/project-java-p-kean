package ku.cs.app.controller.admin;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import ku.cs.app.controller.staff.ReportListController;
import ku.cs.app.models.*;
import ku.cs.app.services.*;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;


import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ReportController implements Initializable {
    @FXML
    private Circle accountCircleImage;

    @FXML
    private Label adminNameLabel;

    @FXML
    private ImageView categoryIcon;
    @FXML private Label moreFirstLabel;
    @FXML private Label moreSecondLabel;
    @FXML private Label moreThirdLabel;
    @FXML private Label moreFourthLabel;

    @FXML private Label moreFirstTitleLabel;
    @FXML private Label moreSecondTitleLabel;
    @FXML private Label moreThirdTitleLabel;
    @FXML private Label moreFourthTitleLabel;

    @FXML
    private Label categoryLabel;

    @FXML
    private VBox chosenReport;

    @FXML
    private Label dateReportLabel;

    @FXML
    private Label detailReportLabel;

    @FXML
    private Label howToLabel;

    @FXML
    private ImageView imageAdmin;

    @FXML
    private ImageView imageReport;

    @FXML
    private ImageView imageStaff;

    @FXML
    private ImageView imageUser;

    @FXML
    private GridPane reportedGrid;

    @FXML
    private Label statusLabel;
    @FXML
    private Label titleLabel;
    @FXML
    private Label userLabel;

    @FXML
    private AnchorPane parent;
    @FXML
    private ImageView modeImage;
    @FXML
    private HBox moreFirstHBox;

    @FXML
    private HBox moreFourthHBox;

    @FXML
    private HBox moreSecondHBox;

    @FXML
    private HBox moreThirdHBox;


    private Clickable clickable;
    private UserReportFileDataSource dataSource;
    private List<Report> reports;
    private ReportList reportList;
    private List<Report> abuseList;
    private UserAccount userAccount;
    private DataSource<UserAccountList> read = new UserAccountFileDataSource();
    private UserAccountList accountList = read.readData();
    private Theme mode;
    private String selectedUser;

    private ObjectWrapper objectWrapper;
    private Boolean isLightMode;
    private List<Report> reportsRefresh = new ArrayList<>();
    private Category category;

    String lightModePath = new File(System.getProperty("user.dir") + File.separator + "/style/light_mode.css").toURI().toString();
    String darkModePath = new File(System.getProperty("user.dir") + File.separator + "/style/dark_mode.css").toURI().toString();
    private CategoryReportFileDataSource categoryDataSource;
    private List<Category> categories = new ArrayList<>();
    private CategoryList categoryList = new CategoryList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        objectWrapper = (ObjectWrapper) com.github.saacsos.FXRouter.getData();
        userAccount = (UserAccount) objectWrapper.getObject("account");
        isLightMode = (Boolean) objectWrapper.getObject("mode");
        checkMode();

        dataSource = new UserReportFileDataSource();
        reportList = new ReportList();
        reports = dataSource.readData(reportList);
        abuseList = new ArrayList<>();
        for (Report report : reports) {
            if (Integer.parseInt(report.getGotReported()) > 0) {
                abuseList.add(report);
            }
        }
        System.out.println(abuseList);
        try {
            showReportBox(abuseList);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void handleBanUserButton(ActionEvent actionEvent) throws IOException {
        Report report = reportList.find(selectedUser);
        for (String account : accountList.getAllAccounts()) {
            UserAccount acc = accountList.find(account);
            if (report.getUser().equals(acc.getUsername())) {
                acc.setUserStatus("banned");
                System.out.println("banned " + acc.getUsername());
                abuseList.remove(report);
                reportList.removeReport(report.getReportId(), report);
                reportedGrid.getChildren().clear();
                dataSource.writeData(reportList);
                showReportBox(abuseList);
            }
        }
    }

    public void handleIgnoreButton(ActionEvent actionEvent) throws IOException {
        Report report = reportList.find(selectedUser);
        for (String account : accountList.getAllAccounts()) {
            UserAccount acc = accountList.find(account);
            if (report.getUser().equals(acc.getUsername())) {
                report.setGotReported("0");
                abuseList.remove(report);
                reportList.addReport(report.getReportId(), report);
                dataSource.writeData(reportList);
                reportedGrid.getChildren().clear();
                showReportBox(abuseList);
            }
        }
    }
    @FXML public void handleBannedUserListButton (ActionEvent actionEvent){
        try {
            com.github.saacsos.FXRouter.goTo("bannedUserList", objectWrapper);
        } catch (IOException e) {
            System.out.println("ไปที่หน้า bannedUserList ไม่ได้");
            System.out.println("ให้ตรวจสอบการกำหนด route");
            e.printStackTrace();
        }
    }
    private void setChosenReport (Report report){
        categoryLabel.setText(report.getCategoryName());
        //String url = getClass().getResource(report.getCategoryIcon()).toExternalForm();
        //image = new Image(getClass().getResourceAsStream(report.getCategoryIcon()));
        //categoryIcon.setImage(image);
//        categoryLabel.setStyle("-fx-background-color: #" + report.getCategoryColor() + ";\n" +
//                " -fx-background-radius: 100;");
        categoryDataSource = new CategoryReportFileDataSource();
        categories = categoryDataSource.readData(categoryList);

        for (Category cat : categories) {
            if (report.getCategoryName().equals(cat.getTitle())) {
                category = cat;
            }
        }


        moreFirstHBox.getChildren().clear();
        moreSecondHBox.getChildren().clear();
        moreThirdHBox.getChildren().clear();
        moreFourthHBox.getChildren().clear();

        moreFirstHBox.setPadding(new Insets(0));
        moreSecondHBox.setPadding(new Insets(0));
        moreThirdHBox.setPadding(new Insets(0));
        moreFourthHBox.setPadding(new Insets(0));


        if (category.getMoreFirstDetailBox()) {
            if ( report.getMoreFirstDetail() != "") {
                moreFirstTitleLabel = new Label(category.getMoreFirstDetailBoxTitle() + " :");
                moreFirstLabel = new Label(report.getMoreFirstDetail());
                moreFirstHBox.getChildren().add(moreFirstTitleLabel);
                moreFirstHBox.getChildren().add(moreFirstLabel);

            } else {
                moreFirstTitleLabel = new Label(category.getMoreFirstDetailBoxTitle() + " :");
                moreFirstLabel = new Label("-");
                moreFirstHBox.getChildren().add(moreFirstTitleLabel);
                moreFirstHBox.getChildren().add(moreFirstLabel);
            }

            moreFirstLabel.setPadding(new Insets(0,0,0,15));
            moreFirstHBox.setPadding(new Insets(10, 10, 10, 0));
            moreFirstTitleLabel.setFont(new Font(15));
            moreFirstLabel.setFont(new Font(15));
        }

        if (category.getMoreSecondDetailBox()) {
            if (report.getMoreSecondDetail() != "") {
                moreSecondTitleLabel = new Label(category.getMoreSecondDetailBoxTitle() + " :");
                moreSecondLabel = new Label(report.getMoreSecondDetail());
                moreSecondHBox.getChildren().add(moreSecondTitleLabel);
                moreSecondHBox.getChildren().add(moreSecondLabel);

            } else {
                moreSecondTitleLabel = new Label(category.getMoreSecondDetailBoxTitle() + " :");
                moreSecondLabel = new Label("-");
                moreSecondHBox.getChildren().add(moreSecondTitleLabel);
                moreSecondHBox.getChildren().add(moreSecondLabel);
            }

            moreSecondLabel.setPadding(new Insets(0,0,0,15));
            moreSecondHBox.setPadding(new Insets(10, 10, 10, 0));
            moreSecondTitleLabel.setFont(new Font(15));
            moreSecondLabel.setFont(new Font(15));
        }

        if (category.getMoreThirdDetailBox()) {
            if (report.getMoreThirdDetail() != "") {
                moreThirdTitleLabel = new Label(category.getMoreThirdDetailBoxTitle() + " :");
                moreThirdLabel = new Label(report.getMoreThirdDetail());
                moreThirdHBox.getChildren().add(moreThirdTitleLabel);
                moreThirdHBox.getChildren().add(moreThirdLabel);
            } else {
                moreThirdTitleLabel = new Label(category.getMoreThirdDetailBoxTitle() + " :");
                moreThirdLabel = new Label("-");
                moreThirdHBox.getChildren().add(moreThirdTitleLabel);
                moreThirdHBox.getChildren().add(moreThirdLabel);
            }
            moreThirdLabel.setPadding(new Insets(0,0,0,15));
            moreThirdHBox.setPadding(new Insets(10, 10, 10, 0));
            moreThirdTitleLabel.setFont(new Font(15));
            moreThirdLabel.setFont(new Font(15));
        }

        if (category.getMoreFourthDetailBox()) {
            if (report.getMoreFourthDetail() != "") {
                moreFourthTitleLabel = new Label(category.getMoreFourthDetailBoxTitle() + " :");
                moreFourthLabel = new Label(report.getMoreFourthDetail());
                moreFourthHBox.getChildren().add(moreFourthTitleLabel);
                moreFourthHBox.getChildren().add(moreFourthLabel);
            } else {
                moreFourthTitleLabel = new Label(category.getMoreFourthDetailBoxTitle() + " :");
                moreFourthLabel = new Label("-");
                moreFourthHBox.getChildren().add(moreFourthTitleLabel);
                moreFourthHBox.getChildren().add(moreFourthLabel);
            }
            moreFourthLabel.setPadding(new Insets(0,0,0,15));
            moreFourthHBox.setPadding(new Insets(10, 10, 10, 0));
            moreFourthTitleLabel.setFont(new Font(15));
            moreFourthLabel.setFont(new Font(15));

        }


        titleLabel.setText(report.getTitle());
        detailReportLabel.setText(report.getDetail());
        dateReportLabel.setText(report.getDate());
        howToLabel.setText(report.getHowTo());
        userLabel.setText(report.getUser());

        statusLabel.setText(report.getStatus());

        selectedUser = report.getReportId();

//        selectedReport = report;
    }

    private void showReportBox (List < Report > reports) throws IOException {
        reportsRefresh = reports;
        if (reports.size() > 0) {
            System.out.println(reports.get(0));
            setChosenReport(reports.get(0));
            clickable = new Clickable() {
                @Override
                public void onClickList(Report report) {
                    setChosenReport(report);
                }

                @Override
                public void onClickLike(Report report) throws IOException {
                }
            };
        }

        int column = 0;
        int row = 1;
        for (int i = 0; i < reports.size(); i++) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/ku/cs/reportList.fxml"));
            AnchorPane anchorPane = fxmlLoader.load();

            ReportListController reportListController = fxmlLoader.getController();
            reportListController.setData(reports.get(i), clickable, isLightMode);  //wait add mode
            if (column == 2) {
                column = 0;
                row++;
            }


            reportedGrid.add(anchorPane, column++, row);

//                // set grid width
            reportedGrid.setMinWidth(Region.USE_COMPUTED_SIZE);
            reportedGrid.setPrefWidth(Region.USE_COMPUTED_SIZE);
            reportedGrid.setMaxWidth(Region.USE_COMPUTED_SIZE);

//
            // set grid height
            reportedGrid.setMinHeight(Region.USE_COMPUTED_SIZE);
            reportedGrid.setPrefHeight(Region.USE_COMPUTED_SIZE);
            reportedGrid.setMaxHeight(Region.USE_COMPUTED_SIZE);

            GridPane.setMargin(anchorPane, new Insets(10));
        }
    }


    @FXML
    public void handleBackButton (ActionEvent actionEvent){
        try {
            com.github.saacsos.FXRouter.goTo("login", objectWrapper);
        } catch (IOException e) {
            System.err.println("ไปที่หน้า home ไม่ได้");
            System.err.println("ให้ตรวจสอบการกำหนด route");
            e.printStackTrace();
        }
    }
    @FXML
    public void handleUserButton (ActionEvent actionEvent){
        try {
            com.github.saacsos.FXRouter.goTo("userEditor", objectWrapper);
        } catch (IOException e) {
            System.err.println("ไปที่หน้า userEditor ไม่ได้");
            System.err.println("ให้ตรวจสอบการกำหนด route");
            e.printStackTrace();
        }
    }

    @FXML public void handleStaffButton (ActionEvent actionEvent){
        try {
            com.github.saacsos.FXRouter.goTo("staffEditor", objectWrapper);
        } catch (IOException e) {
            System.err.println("ไปที่หน้า staffEditor ไม่ได้");
            System.err.println("ให้ตรวจสอบการกำหนด route");

            e.printStackTrace();
        }
    }

    @FXML public void handleReportButton (ActionEvent actionEvent){
        try {
            com.github.saacsos.FXRouter.goTo("report", objectWrapper);
        } catch (IOException e) {
            System.err.println("ไปที่หน้า report ไม่ได้");
            System.err.println("ให้ตรวจสอบการกำหนด route");

            e.printStackTrace();
        }
    }

    @FXML public void handleAdminButton (ActionEvent actionEvent){
        try {
            com.github.saacsos.FXRouter.goTo("adminEditor", objectWrapper);
        } catch (IOException e) {
            System.err.println("ไปที่หน้า adminEditor ไม่ได้");
            System.err.println("ให้ตรวจสอบการกำหนด route");
            e.printStackTrace();
        }
    }

    private void checkMode () {
        if (isLightMode) {
            setLightMode();
        } else {
            setDarkMode();
        }
    }
    @FXML
    public void changeMode (ActionEvent actionEvent){
        isLightMode = !isLightMode;
        reportedGrid.getChildren().clear();
        try {
            showReportBox(reportsRefresh);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (isLightMode) {
            setLightMode();
        } else {
            setDarkMode();
        }
    }

    private void setLightMode () {
        parent.getStylesheets().add(lightModePath);
        parent.getStylesheets().remove(darkModePath);
        Image image = new Image(getClass().getResourceAsStream("/ku/cs/images/sun.png"));
        modeImage.setImage(image);
        objectWrapper.addObject("mode", isLightMode);
    }
    private void setDarkMode () {
        parent.getStylesheets().add(darkModePath);
        parent.getStylesheets().remove(lightModePath);
        Image image = new Image(getClass().getResourceAsStream("/ku/cs/images/moon.png"));
        modeImage.setImage(image);
        objectWrapper.addObject("mode", isLightMode);
    }

}