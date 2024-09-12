package ku.cs.app.controller.staff;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import ku.cs.app.models.*;
import ku.cs.app.services.Clickable;
import ku.cs.app.services.ObjectWrapper;
import ku.cs.app.services.*;
import com.github.saacsos.FXRouter;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class StaffController implements Initializable {

    @FXML private Label dateReportLabel;
    @FXML private Label detailReportLabel;
    @FXML private Label howToLabel;
    @FXML private GridPane allGrid;
    @FXML private Label staffLabel;
    @FXML private Label agencyLabel;
    @FXML private Label statusLabel;
    @FXML private Label titleLabel;
    @FXML private TextField searchBox;
    @FXML private Label staffNameLabel;
    @FXML private ChoiceBox<String> chooseStatusChoiceBox;
    @FXML private Circle accountImage;
    @FXML private ImageView searchIcon;
    @FXML private ImageView cancelIcon;
    @FXML private AnchorPane parent;
    @FXML private Label moreFirstLabel;
    @FXML private Label moreSecondLabel;
    @FXML private Label moreThirdLabel;
    @FXML private Label moreFourthLabel;

    @FXML private Label moreFirstTitleLabel;
    @FXML private Label moreSecondTitleLabel;
    @FXML private Label moreThirdTitleLabel;
    @FXML private Label moreFourthTitleLabel;

    @FXML private HBox moreFirstHBox;
    @FXML private HBox moreSecondHBox;
    @FXML private HBox moreThirdHBox;
    @FXML private HBox moreFourthHBox;
    @FXML private Label photoTitle;
    @FXML private ImageView photo;
    @FXML private HBox photoHBox1;
    @FXML private HBox photoHBox2;
    @FXML private HBox photoHBox3;
    @FXML private Label staffNameTitleLabel;


    private List<Report> reports = new ArrayList<>();
    private List<Category> categories = new ArrayList<>();
    private List<Organization> organizations = new ArrayList<>();
    private Image image;
    private Clickable clickable;

    private UserReportFileDataSource dataSource;
    private CategoryReportFileDataSource categoryDataSource;
    private ReportList reportList;
    private CategoryList categoryList;
    //    private ObjectWrapper objectWrapper;
//    private List<FXMLLoader> FXMLLoaderList;
    private UserAccount userAccount;
    private ObjectWrapper objectWrapper = new ObjectWrapper();
    private Boolean isLightMode;
    private List<Report> reportsRefresh = new ArrayList<>();
    private Category category = new Category();
    private Organization organization = new Organization();
    private OrganizationFileDataSource organizationFileDataSource = new OrganizationFileDataSource();
    private OrganizationList organizationList = new OrganizationList();
    private List<Report> filtered_category = new ArrayList<>();
    String lightModePath = new File(System.getProperty("user.dir") + File.separator + "/style/light_mode.css").toURI().toString();
    String darkModePath = new File(System.getProperty("user.dir") + File.separator + "/style/dark_mode.css").toURI().toString();

    private List<Organization> getOrganization() {
        organizationList = new OrganizationList();
        organizationFileDataSource = new OrganizationFileDataSource();
        organizations = organizationFileDataSource.readOrganizationData(organizationList);
        return organizations;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("initialize StaffController");
        objectWrapper = (ObjectWrapper) FXRouter.getData();
        userAccount = (UserAccount) objectWrapper.getObject("account");
        isLightMode = (Boolean) objectWrapper.getObject("mode");
        checkMode();

        organizations = new ArrayList<>(getOrganization());
        reportList = new ReportList();
        categoryList = new CategoryList();
        dataSource = new UserReportFileDataSource();
        reports = dataSource.readData(reportList);

        categoryDataSource = new CategoryReportFileDataSource();
        categories = categoryDataSource.readData(categoryList);
        categoryDataSource = new CategoryReportFileDataSource();
        organizations = new ArrayList<>(getOrganization());

        filtered_category = reportList.filterBy(new Filterer<Report>() {
            @Override
            public boolean filter(Report report) {
                Organization org = organizationList.find(userAccount.getOrganization());
                return report.getCategoryName().equals(org.getCategory());
            }
        });

        for (Organization org : organizations) {
            if (userAccount.getOrganization().equals(org.getOrganizationName())){
                organization = org;
            }
        }

        System.out.println(organization);
        System.out.println(filtered_category.toString());
//        for (Organization org : organizations) {
//            if (userAccount.getOrganization().equals(org.getOrganization())){
//                organization = org;
//                System.out.println(organization);
//            }
//        }
//
        for (Category cat: categories) {
            if (cat.getTitle().equals(organization.getCategory())) {
                category = cat;
            }
        }

        System.out.println(category);

//        System.out.println(category);
//
//        for (Category cat: categories) {
//            if (cat.getTitle().equals( ((Report) filtered_category.get(0)).getCategoryName()) ) {
//                category = cat;
//            }
//        }

        showReportBox(filtered_category);
        objectWrapper.addObject("category", category);

        staffNameLabel.setText(userAccount.getName());
        String url = new File(System.getProperty("user.dir") + File.separator + userAccount.getImagePath()).toURI().toString();
        ImagePattern pattern = new ImagePattern(new Image(url));
        accountImage.setFill(pattern);

        String searchIconUrl = getClass().getResource("/ku/cs/images/searchIcon.png").toExternalForm();
        searchIcon.setImage(new Image(searchIconUrl));

        String cancelIconUrl = getClass().getResource("/ku/cs/images/close.png").toExternalForm();
        cancelIcon.setImage(new Image(cancelIconUrl));
        cancelIcon.setVisible(false);

        ObservableList<String> list = FXCollections.observableArrayList("ทั้งหมด", "ยังไม่รับเรื่อง", "อยู่ระหว่างดำเนินการ"
                , "เสร็จสิ้น");
        chooseStatusChoiceBox.setItems(list);
        chooseStatusChoiceBox.setValue("ทั้งหมด");

        chooseStatusChoiceBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (chooseStatusChoiceBox.getValue().equals("ทั้งหมด")) {
                    showReportBox(filtered_category);
                } else {
                    List filtered = reportList.filterBy(new Filterer<Report>() {
                        @Override
                        public boolean filter(Report report) {
                            Organization org = organizationList.find(userAccount.getOrganization());
                            return report.getStatus().equals(chooseStatusChoiceBox.getValue())
                                    && report.getCategoryName().equals(org.getCategory());
                        }
                    }) ;

                    allGrid.getChildren().clear();
                    showReportBox(filtered);
                }
            }
        });
    }

    private void showReportBox(List<Report> reports) {
        reportsRefresh = reports;
        if (reports.size() > 0) {
            setChosenReport(reports.get(0));
            clickable = new Clickable() {
                @Override
                public void onClickList(Report report) {
                    setChosenReport(report);
                }

                @Override
                public void onClickLike(Report report)  {

                }
            };
        }
        int column = 0;
        int row = 1;

        try {
            for (int i=0; i < reports.size(); i++) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/ku/cs/reportList.fxml"));
                AnchorPane anchorPane = fxmlLoader.load();

                ReportListController reportListController = fxmlLoader.getController();
                reportListController.setData(reports.get(i), clickable, isLightMode);
                if (column == 3) {
                    column = 0;
                    row++;
                }
                allGrid.add(anchorPane, column++, row);
//                // set grid width
                allGrid.setMinWidth(Region.USE_COMPUTED_SIZE);
                allGrid.setPrefWidth(Region.USE_COMPUTED_SIZE);
                allGrid.setMaxWidth(Region.USE_COMPUTED_SIZE);

//
                // set grid height
                allGrid.setMinHeight(Region.USE_COMPUTED_SIZE);
                allGrid.setPrefHeight(Region.USE_COMPUTED_SIZE);
                allGrid.setMaxHeight(Region.USE_COMPUTED_SIZE);


                GridPane.setMargin(anchorPane, new Insets(10));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setChosenReport(Report report) {
//        categoryLabel.setText(report.getCategoryName());
//        categoryLabel.setStyle("-fx-background-color: #" + report.getCategoryColor() + ";\n" +
//                " -fx-background-radius: 100" + ";\n" + "-fx-text-fill: #FFFFFF;");

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


        if (!report.getAgencyName().equals(organization.getOrganizationName())) {
            staffNameTitleLabel.setVisible(false);
            staffLabel.setText("");
        } else {
            staffNameTitleLabel.setVisible(true);
            staffLabel.setText(report.getStaffName());
        }

        titleLabel.setText(report.getTitle());
        detailReportLabel.setText(report.getDetail());
        dateReportLabel.setText(report.getDate());
        howToLabel.setText(report.getHowTo());

        statusLabel.setText(report.getStatus());

        if (report.getAgencyName() != "") {
            agencyLabel.setText(report.getAgencyName());
        } else {
            agencyLabel.setText("-");
        }

//        selectedReport = report;
        objectWrapper.addObject("selected", report );
        objectWrapper.addObject("reportList", reportList);
        objectWrapper.addObject("account", userAccount);
//        accountWrapper.addObject("accountWrapper", accountWrapper);

    }


    @FXML public void handleSearchButton(ActionEvent actionEvent) {
        String search = searchBox.getText();
        if (! search.equals("")) {
            cancelIcon.setVisible(true);
            showReportBox(filtered_category);
        } else {
            cancelIcon.setVisible(false);
        }

        List filtered = reportList.filterBy(new Filterer<Report>() {
            @Override
            public boolean filter(Report report) {
                if (chooseStatusChoiceBox.getValue() == "ทั้งหมด") {
//                    Organization org = organizationList.find(userAccount.getOrganization());
//                    System.out.println(organization.getCategory());
                    return (report.getTitle().contains(search) || report.getStaffName().contains(search))
                            && report.getCategoryName().equals(organization.getCategory());
                } else {
//                    Organization org = organizationList.find(userAccount.getOrganization());
                    return (report.getTitle().contains(search) && report.getStatus().equals(chooseStatusChoiceBox.getValue())
                            || report.getStaffName().contains(search) && report.getStatus().equals(chooseStatusChoiceBox.getValue()))
                            && report.getCategoryName().equals(organization.getCategory());
                }
            }
        }) ;
        allGrid.getChildren().clear();
        showReportBox(filtered);
    }

    @FXML
    private void gotoAccountPage(MouseEvent mouseEvent) {

        try {
            objectWrapper.addObject("mode", isLightMode);
            com.github.saacsos.FXRouter.goTo("staffAccount", objectWrapper);
        } catch (IOException e) {
            System.err.println("ไปที่หน้า editReport ไม่ได้");
            System.err.println("ให้ตรวจสอบการกำหนด route");
            e.printStackTrace();
        }
    }
    @FXML private void clearSearch(MouseEvent mouseEvent) {
        searchBox.clear();
        cancelIcon.setVisible(false);
    }
    @FXML
    public void handleBackButton(ActionEvent actionEvent){
        try {
            objectWrapper.addObject("mode", isLightMode);
            com.github.saacsos.FXRouter.goTo("login", objectWrapper);
        } catch (IOException e) {
            System.err.println("ไปที่หน้า home ไม่ได้");
            System.err.println("ให้ตรวจสอบการกำหนด route");
        }
    }
    @FXML
    void handleEditPageButton(ActionEvent actionEvent) {
        try {
            objectWrapper.addObject("mode", isLightMode);
            com.github.saacsos.FXRouter.goTo("editReport", objectWrapper);
        } catch (IOException e) {
            System.err.println("ไปที่หน้า home ไม่ได้");
            System.err.println("ให้ตรวจสอบการกำหนด route");
            e.printStackTrace();
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
        allGrid.getChildren().clear();
        showReportBox(reportsRefresh);
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