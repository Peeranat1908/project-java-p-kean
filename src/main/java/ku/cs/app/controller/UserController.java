package ku.cs.app.controller;

import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import ku.cs.app.models.*;
import ku.cs.app.services.*;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class UserController implements Initializable {
    @FXML
    AnchorPane FilterMenuPane;
    @FXML
    AnchorPane CreateReportPane;
    @FXML
    AnchorPane ReportViewPane;
    @FXML
    ScrollPane ReportListPane;
    @FXML
    Button FilterButton;
    @FXML
    Label categoryLabel;
    @FXML
    TextArea dateReportLabel;
    @FXML
    TextArea detailReportLabel;
    @FXML
    Label statusLabel;
    @FXML
    TextField titleLabel;
    @FXML
    VBox reportContainer;
    @FXML
    Label likeAmountLabel;
    @FXML
    Button createReportButton;
    @FXML
    TextArea createDetailReportArea;
    @FXML
    TextField createTitleReportField;
    @FXML
    ListView<Category> reportCreateCategoryListView;
    @FXML
    Button ReportInfoLikeButton;
    @FXML
    ImageView uploadExtraImageView;
    @FXML
    Button uploadImageButton;
    @FXML
    Label FirstExtraDetailTitle;
    @FXML
    TextField firstExtraDetail;
    @FXML
    Label SecondExtraDetailTitle;
    @FXML
    TextField secondExtraDetail;
    @FXML
    Label ThirdExtraDetailTitle;
    @FXML
    TextField thirdExtraDetail;
    @FXML
    Label FourthExtraDetailTitle;
    @FXML
    TextField fourthExtraDetail;
    @FXML
    Label FirstExtraViewDetailTitle;
    @FXML
    TextField firstExtraViewDetail;
    @FXML
    Label SecondExtraViewDetailTitle;
    @FXML
    TextField secondExtraViewDetail;
    @FXML
    Label ThirdExtraViewDetailTitle;
    @FXML
    TextField thirdExtraViewDetail;
    @FXML
    Label FourthExtraViewDetailTitle;
    @FXML
    TextField fourthExtraViewDetail;
    @FXML
    Label dateLabel;
    @FXML
    Label unSelectCategoryLabel;
    @FXML
    ListView categoryListViewFilter;
    @FXML
    TextField likeMinAmountFilter;
    @FXML
    TextField likeMaxAmountFilter;
    @FXML
    TextField searchTextField;
    @FXML
    Label FilterButtonText;
    @FXML
    Label CreateReportButtonText;
    @FXML
    Label UserProfileButtonText;
    @FXML
    Button userProfileButton;
    @FXML
    Button MenuCreateReportButton;
    @FXML
    Pane FilterMenuPaneLeft;
    @FXML
    Pane FilterMenuPaneRight;
    @FXML
    Button minMaxAmountSearchButton;
    @FXML
    AnchorPane parent;
    @FXML
    Button CloseCreateReportButton;
    @FXML
    Label CloseCreateReportButtonText;
    @FXML
    Button CloseReportViewButton;
    @FXML
    Label CloseReportViewButtonText;
    @FXML
    Button UserAccountReportFilterButton;
    @FXML
    AnchorPane ChooseReportUser;
    @FXML
    Button CloseReportedViewButton;
    @FXML
    Label CloseReportedViewButtonText;
    @FXML
    TextArea howToTextArea;
    @FXML
    ImageView modeImage;

    private List<Report> reports;
    private ArrayList<String> userLiked;
    private Clickable clickable;
    private ReportList reportList;
    private UserReportFileDataSource dataSource;
    private CategoryReportFileDataSource categoryDataSource;
    private List<Category> categorys;
    private CategoryList categoryList;
    private String clickedReport;
    private UserAccount userAccount;
    private DataSource<UserAccountList> read = new UserAccountFileDataSource();
    private UserAccountList accountList = read.readData();
    private ObjectWrapper objectWrapper;


    @FXML
    public void handleBackButton(ActionEvent actionEvent) {
        try {
            com.github.saacsos.FXRouter.goTo("login", objectWrapper);
        } catch (IOException e) {
            System.err.println("ไปที่หน้า home ไม่ได้");
            System.err.println("ให้ตรวจสอบการกำหนด route");
        }
    }

    public void handleUserAccountReportFilter() {
        String user = userAccount.getUsername();
        List filtered = reportList.filterBy(new Filterer<Report>() {
            @Override
            public boolean filter(Report report) {
                return report.getUser().equals(user);
            }
        }) ;
        reportContainer.getChildren().clear();
        showListView(filtered);
    }

    @FXML
    public void handleFilterButton(ActionEvent actionEvent) {
        if (FilterMenuPane.isVisible()) {
            FilterMenuPane.setVisible(false);
        } else {
            FilterMenuPane.setVisible(true);
            CreateReportPane.setVisible(false);
            ReportViewPane.setVisible(false);
            likeMinAmountFilter.setText("");
            likeMaxAmountFilter.setText("");
            showCategoryListView();
            animateFilterMenu();
        }
        onClickButton(FilterButton);
    }
    public void handleClickFilterButton(ActionEvent event) {
        Button button = (Button) event.getSource();

        if (button.getText().equals("ลบตัวกรองออก")) {
            reports = getReport();
            showListView(reports);
            searchTextField.setText("");
        } else {
            List filtered = reportList.filterBy(new Filterer<Report>() {
                @Override
                public boolean filter(Report report) {
                    return report.getStatus().equals(button.getText());
                }
            }) ;
            reportContainer.getChildren().clear();
            reports = filtered;
            showListView(filtered);
        }
        onClickButton(button);
        FilterMenuPane.setVisible(true);
    }

    public void handleDateFilter(ActionEvent event) {
        Button button = (Button) event.getSource();

        if (button.getText().equals("ล่าสุด")) {
            reports.sort(new DateComparator());
        } else {
            reports.sort(new DateComparator().reversed());
        }
        System.out.println(reports);
        showListView(reports);
        onClickButton(button);
        FilterMenuPane.setVisible(true);
    }

    public void handleLikeFilter(ActionEvent event) {
        Button button = (Button) event.getSource();

        if (button.getText().equals("มากสุด")) {
            reports.sort(new LikeComparator().reversed());
        } else {
            reports.sort(new LikeComparator());
        }
        showListView(reports);
        onClickButton(button);
        FilterMenuPane.setVisible(true);
    }
    public void handleOnType() {
        String search = searchTextField.getText();
        if (search.equalsIgnoreCase("12345")) {
            reports = getReport();
            showListView(reports);
            setLightMode();
            searchTextField.setText("");
        }
        else if (search.equalsIgnoreCase("56789")) {
            reports = getReport();
            showListView(reports);
            setDarkMode();
            searchTextField.setText("");
        }
        else if (search.equals("")) {
            reports = getReport();
            showListView(reports);
        } else {
            List filtered = reportList.filterBy(new Filterer<Report>() {
                @Override
                public boolean filter(Report report) {
                    return report.getTitle().contains(search);
                }
            }) ;
            showListView(filtered);
        }
    }

    public void handleMinMaxLikeFilter() {
        String min = likeMinAmountFilter.getText();
        String max = likeMaxAmountFilter.getText();
        if (min.equals("") && max.equals("")) {
            likeMinAmountFilter.setPromptText("กรอกเลข");
            likeMaxAmountFilter.setPromptText("ให้ครบ");
        } else if (Integer.parseInt(min) > 0 && Integer.parseInt(max) == 0) {
            List<Report> remove = new ArrayList<Report>();
            reports.sort(new LikeComparator());
            for (Report report : reports) {
                if (report.getLikeAmount() < Integer.parseInt(min)) {
                    remove.add(report);
                }
            }
            reports.removeAll(remove);
            FilterMenuPane.setVisible(false);
            showListView(reports);
        } else {
            List<Report> list = getReport();
            List<Report> remove = new ArrayList<Report>();
            reports.sort(new LikeComparator());
            for (Report report : list) {
                if (report.getLikeAmount() < Integer.parseInt(min) || report.getLikeAmount() > Integer.parseInt(max)) {
                    remove.add(report);
                }
            }
            list.removeAll(remove);
            FilterMenuPane.setVisible(false);
            showListView(list);
        }
        onClickButton(minMaxAmountSearchButton);
    }

    public void handleCategorySelectFilter() {
        String selected = String.valueOf(categoryListViewFilter.getSelectionModel().getSelectedItem());
        List filtered = reportList.filterBy(new Filterer<Report>() {
            @Override
            public boolean filter(Report report) {
                return report.getCategoryName().equals(selected);
            }
        }) ;
        reportContainer.getChildren().clear();
        reports = filtered;
        showListView(filtered);
        FilterMenuPane.setVisible(false);
    }
    @FXML
    public void handleCreateReportButton(ActionEvent actionEvent) {
        if (CreateReportPane.isVisible()) {
            CreateReportPane.setVisible(false);
        } else {
            CreateReportPane.setVisible(true);
            FilterMenuPane.setVisible(false);
            ReportViewPane.setVisible(false);
        }
        animatePaneReport(CreateReportPane);
        showCategoryListView();
    }

    @FXML
    public void handleCloseReportViewButton(ActionEvent actionEvent) {

        ReportViewPane.setVisible(false);
        showListView(reports);
    }

    public void handleCloseReportedViewButton() {
        ChooseReportUser.setVisible(false);
        onClickButton(CloseReportedViewButton);
    }
    @FXML
    public void handleCloseCreateReportButton(ActionEvent actionEvent) {
        CreateReportPane.setVisible(false);
        createTitleReportField.setText("");
        createDetailReportArea.setText("");
        FirstExtraDetailTitle.setVisible(false);
        SecondExtraDetailTitle.setVisible(false);
        ThirdExtraDetailTitle.setVisible(false);
        FourthExtraDetailTitle.setVisible(false);
        firstExtraDetail.setText("");
        firstExtraDetail.setVisible(false);
        secondExtraDetail.setText("");
        secondExtraDetail.setVisible(false);
        thirdExtraDetail.setText("");
        thirdExtraDetail.setVisible(false);
        fourthExtraDetail.setText("");
        fourthExtraDetail.setVisible(false);
        showListView(reports);
    }

    private List<Report> getReport() {
        reports = new ArrayList<>();
        reportList = new ReportList();
        dataSource = new UserReportFileDataSource();
        reports = dataSource.readData(reportList);

        return reports;
    }

    private List<Category> getCategory() {
        categorys = new ArrayList<>();
        categoryList = new CategoryList();
        categoryDataSource = new CategoryReportFileDataSource();
        categorys = categoryDataSource.readData(categoryList);

        return categorys;
    }

    private void setChosenReport(Report report) {
        if (ReportViewPane.isVisible()) {
            ReportViewPane.setVisible(false);
        } else {
            ReportViewPane.setVisible(true);
            CreateReportPane.setVisible(false);
            FilterMenuPane.setVisible(false);
        }
        firstExtraViewDetail.setVisible(false);
        FirstExtraViewDetailTitle.setVisible(false);
        secondExtraViewDetail.setVisible(false);
        SecondExtraViewDetailTitle.setVisible(false);
        thirdExtraViewDetail.setVisible(false);
        ThirdExtraViewDetailTitle.setVisible(false);
        fourthExtraViewDetail.setVisible(false);
        FourthExtraViewDetailTitle.setVisible(false);
        getCategory();
        Category category = categoryList.find(report.getCategoryName());
        categoryLabel.setText("#" +report.getCategoryName());
        categoryLabel.setStyle("-fx-background-color: #" + category.getCategoryColor());
        titleLabel.setText(report.getTitle());
        detailReportLabel.setText(report.getDetail());
        howToTextArea.setText(report.getHowTo());
        likeAmountLabel.setText(String.valueOf(report.getLikeAmount()));
        clickedReport = report.getReportId();
        statusLabel.setText("สถานะ " + report.getStatus());
        dateLabel.setText("วันที่ " + report.getDate() + "   " + report.getTime());
        animatePaneReport(ReportViewPane);
        if (categoryList.find(report.getCategoryName()).getMoreFirstDetailBox() == true) {
            firstExtraViewDetail.setVisible(true);
            FirstExtraViewDetailTitle.setVisible(true);
            firstExtraViewDetail.setText(report.getMoreFirstDetail());
            FirstExtraViewDetailTitle.setText(categoryList.find(report.getCategoryName()).getMoreFirstDetailBoxTitle());
        }
        if (categoryList.find(report.getCategoryName()).getMoreSecondDetailBox() == true) {
            secondExtraViewDetail.setText(report.getMoreSecondDetail());
            SecondExtraViewDetailTitle.setText(categoryList.find(report.getCategoryName()).getMoreSecondDetailBoxTitle());
            secondExtraViewDetail.setVisible(true);
            SecondExtraViewDetailTitle.setVisible(true);
        }
        if (categoryList.find(report.getCategoryName()).getMoreThirdDetailBox() == true) {
            thirdExtraViewDetail.setText(report.getMoreThirdDetail());
            ThirdExtraViewDetailTitle.setText(categoryList.find(report.getCategoryName()).getMoreThirdDetailBoxTitle());
            thirdExtraViewDetail.setVisible(true);
            ThirdExtraViewDetailTitle.setVisible(true);
        }
        if (categoryList.find(report.getCategoryName()).getMoreFourthDetailBox() == true) {
            fourthExtraViewDetail.setText(report.getMoreFourthDetail());
            FourthExtraViewDetailTitle.setText(categoryList.find(report.getCategoryName()).getMoreFourthDetailBoxTitle());
            fourthExtraViewDetail.setVisible(true);
            FourthExtraViewDetailTitle.setVisible(true);
        }
    }
    public void handleSelectCategory() {
        String selected = String.valueOf(reportCreateCategoryListView.getSelectionModel().getSelectedItem());
        Category category = categoryList.find(selected);
        if (unSelectCategoryLabel.isVisible()) {unSelectCategoryLabel.setVisible(false);}
        firstExtraDetail.setText("");
        secondExtraDetail.setText("");
        thirdExtraDetail.setText("");
        fourthExtraDetail.setText("");
        if(category.getMoreFirstDetailBox()) {
            firstExtraDetail.setVisible(true);
            FirstExtraDetailTitle.setText(category.getMoreFirstDetailBoxTitle());
            FirstExtraDetailTitle.setVisible(true);
        } else {
            firstExtraDetail.setVisible(false);
            FirstExtraDetailTitle.setVisible(false);
        }
        if(category.getMoreSecondDetailBox()) {
            secondExtraDetail.setVisible(true);
            SecondExtraDetailTitle.setText(category.getMoreSecondDetailBoxTitle());
            SecondExtraDetailTitle.setVisible(true);
        } else {
            secondExtraDetail.setVisible(false);
            SecondExtraDetailTitle.setVisible(false);
        }
        if(category.getMoreThirdDetailBox()) {
            thirdExtraDetail.setVisible(true);
            ThirdExtraDetailTitle.setText(category.getMoreThirdDetailBoxTitle());
            ThirdExtraDetailTitle.setVisible(true);
        } else {
            thirdExtraDetail.setVisible(false);
            ThirdExtraDetailTitle.setVisible(false);
        }
        if(category.getMoreFourthDetailBox()) {
            fourthExtraDetail.setVisible(true);
            FourthExtraDetailTitle.setText(category.getMoreFourthDetailBoxTitle());
            FourthExtraDetailTitle.setVisible(true);
        } else {
            fourthExtraDetail.setVisible(false);
            FourthExtraDetailTitle.setVisible(false);
        }
    }
    public void handleCreateButton() throws IOException {
        String title = createTitleReportField.getText();
        String detail = createDetailReportArea.getText();
        String user = userAccount.getUsername();
        String categoryName = String.valueOf(reportCreateCategoryListView.getSelectionModel().getSelectedItem());
        String agencyName = "";
        String howTo = "";
        String staffName = "";
        DateTimeFormatter date = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter tf = DateTimeFormatter.ofPattern("hh:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        //categoryDataSource.writeData(categoryList);
        if (reportCreateCategoryListView.getSelectionModel().getSelectedItem() == null) {
            unSelectCategoryLabel.setVisible(true);
        } else {
            dataSource.createPost(reportList, title, detail, date.format(now), user, categoryName, agencyName, howTo, staffName, firstExtraDetail.getText(), secondExtraDetail.getText(), thirdExtraDetail.getText(), fourthExtraDetail.getText(), tf.format(now));
            reports = getReport();
            showListView(reports);
            CreateReportPane.setVisible(false);
        }
        createTitleReportField.setText("");
        createDetailReportArea.setText("");
        FirstExtraDetailTitle.setVisible(false);
        SecondExtraDetailTitle.setVisible(false);
        ThirdExtraDetailTitle.setVisible(false);
        FourthExtraDetailTitle.setVisible(false);
        firstExtraDetail.setText("");
        firstExtraDetail.setVisible(false);
        secondExtraDetail.setText("");
        secondExtraDetail.setVisible(false);
        thirdExtraDetail.setText("");
        thirdExtraDetail.setVisible(false);
        fourthExtraDetail.setText("");
        fourthExtraDetail.setVisible(false);
    }
    public void handleInformAbuseButton(ActionEvent actionEvent) throws IOException {
        ChooseReportUser.setVisible(true);
//        Report report = reportList.find(clickedReport);
//        report.addGotReported(1);
//        dataSource.writeData(reportList);
//
//
//        for(UserAccount acc : accountList.getAllAccounts()){    //getGotReported += 1 then reWrite
//            if(report.getUser().equals(acc.getUsername())){
//                acc.setGotReported(String.valueOf(Integer.parseInt(acc.getGotReported()) + 1));
//                acc.reWrite(accountList, acc);
//            }
//        }
    }
    public void handleReportInformAbuseButton() throws IOException {
        Report report = reportList.find(clickedReport);
        report.addGotReported(1);
        dataSource.writeData(reportList);
        ChooseReportUser.setVisible(false);
    }

    public void handleUserInformAbuseButton() throws IOException {
        Report report = reportList.find(clickedReport);
        for(String account : accountList.getAllAccounts()){    //getGotReported += 1 then reWrite
            UserAccount acc = accountList.find(account);
            if(report.getUser().equals(acc.getUsername())){
                acc.setGotReported(String.valueOf(Integer.parseInt(acc.getGotReported()) + 1));
                read.writeData(accountList);
            }
        }
        ChooseReportUser.setVisible(false);
    }

    public void handleLikeButton(Report report, ReportList reportList) throws IOException {
        report.addLike(1);
        dataSource.writeData(reportList);
        showListView(reports);
    }

    public void handleLikeButtonInReportInfo() throws IOException {
        Report report = reportList.find(clickedReport);
        report.addLike(1);
        dataSource.writeData(reportList);
        likeAmountLabel.setText(String.valueOf(report.getLikeAmount()));
        onClickButton(ReportInfoLikeButton);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        objectWrapper = (ObjectWrapper) com.github.saacsos.FXRouter.getData();
        userAccount = (UserAccount) objectWrapper.getObject("account");
        isLightMode = (Boolean) objectWrapper.getObject("mode");
        reports = new ArrayList<>(getReport());
        checkMode();
        showListView(reports);
    }

    private void showListView(List<Report> reports) {

        categorys = new ArrayList<>(getCategory());
        reportContainer.getChildren().clear();
        reportContainer.setPrefHeight(Region.USE_COMPUTED_SIZE);
        if (reports.size() > 0) {
            clickable = new Clickable() {
                @Override
                public void onClickList(Report report) {
                    setChosenReport(report);
                }
                @Override
                public void onClickLike(Report report) throws IOException { handleLikeButton(report, reportList); }
            };
        }
        if (reports.size() < 5) {
            reportContainer.setPrefHeight(800);
        }
        try {
            for (Report report : reports) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/ku/cs/reportBox.fxml"));
                AnchorPane anchorPane = fxmlLoader.load();

                ReportBoxController reportListController = fxmlLoader.getController();
                reportListController.setData(report, clickable, categoryList.find(report.getCategoryName()));
                reportContainer.getChildren().add(anchorPane);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showCategoryListView() {
        categorys = new ArrayList<>(getCategory());
        ArrayList categoryNameList = new ArrayList<>();
        for (Category category : categorys) {
            categoryNameList.add(category.getTitle());
        }
        categoryListViewFilter.getItems().setAll(categoryNameList);
        reportCreateCategoryListView.getItems().setAll(categoryNameList);
    }
    public void handleUserProfileButton(){
        try {
            objectWrapper.addObject("account", userAccount);
            com.github.saacsos.FXRouter.goTo("userProfile", objectWrapper);
        } catch (IOException e) {
            System.err.println("ไปที่หน้า userProfile ไม่ได้");
            System.err.println("ให้ตรวจสอบการกำหนด route");
            e.printStackTrace();
        }
    }

    /////////////// ANIMATE //////////////////

    public void handleFilterButtonEnter() {
        ScaleTransition scale = new ScaleTransition();
        scale.setNode(FilterButton);
        scale.setByX(0.3);
        scale.setByY(0.3);
        scale.setDuration(Duration.seconds(0.2));
        scale.play();
        FilterButtonText.setVisible(true);
        TranslateTransition transition = new TranslateTransition();
        transition.setNode(FilterButtonText);
        transition.setDuration(Duration.seconds(0.2));
        transition.setByY(-5);
        transition.play();

    }
    public void handleFilterButtonExit() {
        ScaleTransition scale = new ScaleTransition();
        scale.setNode(FilterButton);
        scale.setToX(1);
        scale.setToY(1);
        scale.setDuration(Duration.seconds(0.2));
        scale.play();
        TranslateTransition transition = new TranslateTransition();
        transition.setNode(FilterButtonText);
        transition.setDuration(Duration.seconds(0.2));
        transition.setToY(1);
        transition.play();
        FilterButtonText.setVisible(false);
    }
    public void handleCreateReportButtonEnter() {
        ScaleTransition scale = new ScaleTransition();
        scale.setNode(MenuCreateReportButton);
        scale.setByX(0.3);
        scale.setByY(0.3);
        scale.setDuration(Duration.seconds(0.2));
        scale.play();
        CreateReportButtonText.setVisible(true);
        TranslateTransition transition = new TranslateTransition();
        transition.setNode(CreateReportButtonText);
        transition.setDuration(Duration.seconds(0.2));
        transition.setByY(-5);
        transition.play();

    }
    public void handleCreateReportButtonExit() {
        ScaleTransition scale = new ScaleTransition();
        scale.setNode(MenuCreateReportButton);
        scale.setToX(1);
        scale.setToY(1);
        scale.setDuration(Duration.seconds(0.2));
        scale.play();
        TranslateTransition transition = new TranslateTransition();
        transition.setNode(CreateReportButtonText);
        transition.setDuration(Duration.seconds(0.2));
        transition.setToY(1);
        transition.play();
        CreateReportButtonText.setVisible(false);
    }
    public void handleUserButtonEnter() {
        ScaleTransition scale = new ScaleTransition();
        scale.setNode(userProfileButton);
        scale.setByX(0.3);
        scale.setByY(0.3);
        scale.setDuration(Duration.seconds(0.2));
        scale.play();
        UserProfileButtonText.setVisible(true);
        TranslateTransition transition = new TranslateTransition();
        transition.setNode(UserProfileButtonText);
        transition.setDuration(Duration.seconds(0.2));
        transition.setByY(-5);
        transition.play();
    }
    public void handleUserButtonExit() {
        ScaleTransition scale = new ScaleTransition();
        scale.setNode(userProfileButton);
        scale.setToX(1);
        scale.setToY(1);
        scale.setDuration(Duration.seconds(0.2));
        scale.play();
        TranslateTransition transition = new TranslateTransition();
        transition.setNode(UserProfileButtonText);
        transition.setDuration(Duration.seconds(0.2));
        transition.setToY(1);
        transition.play();
        UserProfileButtonText.setVisible(false);
    }
    public void handleLikeButtonEnter() {
        ScaleTransition scale = new ScaleTransition();
        scale.setNode(ReportInfoLikeButton);
        scale.setByX(0.3);
        scale.setByY(0.3);
        scale.setDuration(Duration.seconds(0.2));
        scale.play();

    }
    public void handleLikeButtonExit() {
        ScaleTransition scale = new ScaleTransition();
        scale.setNode(ReportInfoLikeButton);
        scale.setToX(1);
        scale.setToY(1);
        scale.setDuration(Duration.seconds(0.2));
        scale.play();
    }
    public void animateFilterMenu() {
        ScaleTransition scaleLeft = new ScaleTransition();
        ScaleTransition scaleRight = new ScaleTransition();
        scaleLeft.setNode(FilterMenuPaneLeft);
        scaleLeft.setByX(0.05);
        scaleLeft.setByY(0.05);
        scaleLeft.setDuration(Duration.seconds(0.3));
        scaleLeft.setCycleCount(2);
        scaleLeft.setAutoReverse(true);
        scaleLeft.play();
        scaleRight.setNode(FilterMenuPaneRight);
        scaleRight.setByX(0.05);
        scaleRight.setByY(0.05);
        scaleRight.setDuration(Duration.seconds(0.3));
        scaleRight.setCycleCount(2);
        scaleRight.setAutoReverse(true);
        scaleRight.play();
    }

    public void onClickButton(Button button) {
        ScaleTransition scale = new ScaleTransition();
        scale.setNode(button);
        scale.setByX(-0.2);
        scale.setByY(-0.2);
        scale.setDuration(Duration.seconds(0.2));
        scale.setCycleCount(2);
        scale.setAutoReverse(true);
        scale.play();
    }

    public void animatePaneReport(AnchorPane pane) {
        ScaleTransition scale = new ScaleTransition();
        scale.setNode(pane);
        scale.setByX(0.05);
        scale.setByY(0.05);
        scale.setDuration(Duration.seconds(0.3));
        scale.setCycleCount(2);
        scale.setAutoReverse(true);
        scale.play();
    }

    public void handleCloseButtonEnter() {
        ScaleTransition scale = new ScaleTransition();
        scale.setNode(CloseCreateReportButton);
        scale.setByX(0.3);
        scale.setByY(0.3);
        scale.setDuration(Duration.seconds(0.2));
        scale.play();
        CloseCreateReportButtonText.setVisible(true);
        TranslateTransition transition = new TranslateTransition();
        transition.setNode(CloseCreateReportButtonText);
        transition.setDuration(Duration.seconds(0.2));
        transition.setByY(-5);
        transition.play();

    }
    public void handleCloseButtonExit() {
        ScaleTransition scale = new ScaleTransition();
        scale.setNode(CloseCreateReportButton);
        scale.setToX(1);
        scale.setToY(1);
        scale.setDuration(Duration.seconds(0.2));
        scale.play();
        TranslateTransition transition = new TranslateTransition();
        transition.setNode(CloseCreateReportButtonText);
        transition.setDuration(Duration.seconds(0.2));
        transition.setToY(1);
        transition.play();
        CloseCreateReportButtonText.setVisible(false);
    }

    public void handleCloseViewButtonEnter() {
        ScaleTransition scale = new ScaleTransition();
        scale.setNode(CloseReportViewButton);
        scale.setByX(0.3);
        scale.setByY(0.3);
        scale.setDuration(Duration.seconds(0.2));
        scale.play();
        CloseReportViewButtonText.setVisible(true);
        TranslateTransition transition = new TranslateTransition();
        transition.setNode(CloseReportViewButtonText);
        transition.setDuration(Duration.seconds(0.2));
        transition.setByY(-5);
        transition.play();

    }
    public void handleCloseViewButtonExit() {
        ScaleTransition scale = new ScaleTransition();
        scale.setNode(CloseReportViewButton);
        scale.setToX(1);
        scale.setToY(1);
        scale.setDuration(Duration.seconds(0.2));
        scale.play();
        TranslateTransition transition = new TranslateTransition();
        transition.setNode(CloseReportViewButtonText);
        transition.setDuration(Duration.seconds(0.2));
        transition.setToY(1);
        transition.play();
        CloseReportViewButtonText.setVisible(false);
    }

    public void handleCloseViewReportedButtonEnter() {
        ScaleTransition scale = new ScaleTransition();
        scale.setNode(CloseReportedViewButton);
        scale.setByX(0.3);
        scale.setByY(0.3);
        scale.setDuration(Duration.seconds(0.2));
        scale.play();
        CloseReportedViewButtonText.setVisible(true);
        TranslateTransition transition = new TranslateTransition();
        transition.setNode(CloseReportedViewButtonText);
        transition.setDuration(Duration.seconds(0.2));
        transition.setByY(-5);
        transition.play();

    }
    public void handleCloseViewReportedButtonExit() {
        ScaleTransition scale = new ScaleTransition();
        scale.setNode(CloseReportedViewButton);
        scale.setToX(1);
        scale.setToY(1);
        scale.setDuration(Duration.seconds(0.2));
        scale.play();
        TranslateTransition transition = new TranslateTransition();
        transition.setNode(CloseReportedViewButtonText);
        transition.setDuration(Duration.seconds(0.2));
        transition.setToY(1);
        transition.play();
        CloseReportedViewButtonText.setVisible(false);
    }

    //////// THEME ///////////
    private Boolean isLightMode;
    String lightModePath = new File(System.getProperty("user.dir") + File.separator + "/style/userLight.css").toURI().toString();
    String darkModePath = new File(System.getProperty("user.dir") + File.separator + "/style/userDark.css").toURI().toString();

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