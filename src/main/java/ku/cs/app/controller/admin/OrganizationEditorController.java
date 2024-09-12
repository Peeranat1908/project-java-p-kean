package ku.cs.app.controller.admin;



import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import ku.cs.app.models.*;
import ku.cs.app.services.DataSource;
import ku.cs.app.services.OrganizationFileDataSource;
import ku.cs.app.services.UserAccountFileDataSource;
import ku.cs.app.services.*;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class OrganizationEditorController implements Initializable {
    private OrganizationFileDataSource organizationFileDataSource;
    private OrganizationList organizationList;

    private List<Organization> organizations = new ArrayList<>();

    private UserAccount userAccount;
    private DataSource<UserAccountList> read = new UserAccountFileDataSource();
    private UserAccountList accountList = read.readData();
    @FXML
    private TextField newOrganization;
    @FXML
    private TextField newOrganization2;
    @FXML
    private TextField newCategory;

    @FXML
    private Label errorLabel1;
    @FXML
    private Label errorLabel2;
    @FXML
    private Label errorLabel3;

    @FXML
    private Label errorLabel4;
    @FXML
    private Label selectedOrgLabel;
    @FXML
    private ComboBox<String> orgComboBox;
    @FXML private Label finishCreateCatLabel;
    @FXML private Label finishEditCatLabel;
    @FXML private AnchorPane parent;
    @FXML private ImageView modeImage;
    @FXML private TextField moreFirstTextField;
    @FXML private TextField moreSecondTextField;
    @FXML private TextField moreThirdTextField;
    @FXML private TextField moreFourthTextField;

//    private String dropDown;

    private ObjectWrapper objectWrapper;
    private Boolean isLightMode;
    private ReportList reportList;
    private Category createCategory;
    private CategoryList categoryList = new CategoryList();
    private CategoryReportFileDataSource categoryReportFileDataSource;


    String lightModePath = new File(System.getProperty("user.dir") + File.separator + "/style/light_mode.css").toURI().toString();
    String darkModePath = new File(System.getProperty("user.dir") + File.separator + "/style/dark_mode.css").toURI().toString();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("initialize OrganizationEditor");
        reportList = new ReportList();
        categoryReportFileDataSource = new CategoryReportFileDataSource();
        categories = categoryReportFileDataSource.readData(categoryList);
        dataSource = new UserReportFileDataSource();
        reports = dataSource.readData(reportList);

        objectWrapper = (ObjectWrapper) com.github.saacsos.FXRouter.getData();
        userAccount = (UserAccount) objectWrapper.getObject("account");
        isLightMode = (Boolean) objectWrapper.getObject("mode");
        checkMode();

        organizations = new ArrayList<>(getOrganization());
//        organizations = organizationFileDataSource.readOrganizationData(organizationList);
        showComboBox();
//        handleSelectedOrganization();
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
    public void handleStaffButton(ActionEvent actionEvent) {
        try {
            com.github.saacsos.FXRouter.goTo("staffEditor", objectWrapper);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("ไปที่หน้า staffEditor ไม่ได้");
            System.err.println("ให้ตรวจสอบการกำหนด route");
        }
    }

    //    public void handleSelectedOrganization() {
//        if (orgComboBox.getValue() != null) {
////            selectedOrgLabel.setText("หน่วยงานที่ต้องการจะแก้ไข: " + orgComboBox.getValue());
////            dropDown = orgComboBox.getValue();
//        } else {
////            selectedOrgLabel.setText("");
//        }
//    }
    UserReportFileDataSource dataSource;
    private List<Report> reports = new ArrayList<>();
    private List<Category> categories = new ArrayList<>();


    @FXML
    public void handleChangeOldOrganization() throws IOException {
        String org = newOrganization2.getText();
        errorLabel4.setText("");
        if (org.equals("")|| orgComboBox.getValue() == null) {
            errorLabel4.setText("โปรดกรอกให้ครบช่อง");
            System.out.println("โปรดกรอกให้ครบช่อง");
        } else if (organizationList.searchOrganization(org,organizationList)) {
            errorLabel4.setText("ชื่อนี้หน่วยงานนี้ถูกใช้แล้ว");
            System.out.println("ชื่อนี้หน่วยงานนี้ถูกใช้แล้ว");
        } else {
            Organization changeOrganization= organizationList.find(orgComboBox.getValue());
            String category = changeOrganization.getCategory();
            organizationList.removeOrganization(orgComboBox.getValue(), changeOrganization);

            Organization newOrg = new Organization();
            newOrg.setOrganizationName(org);
            newOrg.setCategory(category);
            organizationList.addOrganization(org, newOrg);

            System.out.println(organizationList.getAllOrganization());
            organizationFileDataSource.writeData(organizationList);

            for (Report report : reports) {
                if (report.getAgencyName().equals(orgComboBox.getValue())) {
                    report.setAgencyName(newOrganization2.getText());
                    reportList.addReport(report.getReportId(), report);
                }
            }

            new UserReportFileDataSource().writeData(reportList);


            //rewrite new organization on account.csv
            for (String account : accountList.getAllAccounts()) {
                UserAccount acc = accountList.find(account);
                if (acc.getOrganization().equals(orgComboBox.getValue())) {
                    acc.setOrganization(org);
                    userAccount = acc;
                    read.writeData(accountList);
                }
            }
            orgComboBox.getItems().clear();
            showComboBox();
            System.out.println("organization name changed");
            finishEditCatLabel.setText("แก้ไขชื่อหน่วยงานสำเร็จ");
            orgComboBox.getSelectionModel().clearSelection();
            newOrganization2.clear();

            objectWrapper.addObject("account", userAccount);
//            try {
//                com.github.saacsos.FXRouter.goTo("organizationEditor");
//            } catch (IOException e) {
//                System.err.println("ไปที่หน้า organizationEditor ไม่ได้");
//                System.err.println("ให้ตรวจสอบการกำหนด route");
//            }
        }
    }

    @FXML
    public void handleCreateOrganization() throws IOException {
        String organization = newOrganization.getText();
        String category = newCategory.getText();
        createCategory = new Category();
        createCategory.setTitle(category);
        if (organization == "" || category == "") {
            errorLabel2.setText("โปรดกรอกให้ครบช่อง");
            errorLabel2.setTextFill(Color.web("#DE493D"));
            System.out.println("โปรดกรอกให้ครบช่อง");
        } else if (organizationList.searchOrganization(organization, organizationList)) {
            errorLabel2.setText("ชื่อหน่วยงานนี้ถูกใช้แล้ว");
            errorLabel2.setTextFill(Color.web("#DE493D"));

            System.out.println("ชื่อหน่วยงานนี้ถูกใช้แล้ว");
        } else if (organizationList.searchCategory(category, organizationList)) {
            errorLabel2.setText("ชื่อหมวดหมู่ร้องเรียนถูกใช้แล้ว");
            errorLabel2.setTextFill(Color.web("#DE493D"));
            System.out.println("ชื่อหมวดหมู่ร้องเรียนถูกใช้แล้ว");
        } else {
            organizationFileDataSource.createOrganization(organizationList, organization, category);
            if (moreFirstTextField.getText() != "") {
                createCategory.setMoreFirstDetailBoxTitle(moreFirstTextField.getText());
                createCategory.setMoreFirstDetailBox(true);
            } else {
                createCategory.setMoreFirstDetailBox(false);
                createCategory.setMoreFirstDetailBoxTitle("");
            }

            if (moreSecondTextField.getText() != "") {
                createCategory.setMoreSecondDetailBoxTitle(moreSecondTextField.getText());
                createCategory.setMoreSecondDetailBox(true);
            } else {
                createCategory.setMoreSecondDetailBox(false);
                createCategory.setMoreSecondDetailBoxTitle("");
            }

            if (moreThirdTextField.getText() != "") {
                createCategory.setMoreThirdDetailBoxTitle(moreThirdTextField.getText());
                createCategory.setMoreThirdDetailBox(true);
            } else {
                createCategory.setMoreThirdDetailBox(false);
                createCategory.setMoreThirdDetailBoxTitle("");
            }

            if (moreFourthTextField.getText() != "") {
                createCategory.setMoreFourthDetailBoxTitle(moreFourthTextField.getText());
                createCategory.setMoreFourthDetailBox(true);
            } else {
                createCategory.setMoreFourthDetailBox(false);
                createCategory.setMoreFourthDetailBoxTitle("");
            }

            categoryList.addCategory(category, createCategory);
            new CategoryReportFileDataSource().writeData(categoryList);
            System.out.println("สร้างหน่วยงานและหมวดหมู่ร้องเรียนสำเร็จ");
            finishCreateCatLabel.setText("สร้างสำเร็จ");

            newOrganization.clear();
            newCategory.clear();
            moreFirstTextField.clear();
            moreSecondTextField.clear();
            moreThirdTextField.clear();
            moreFourthTextField.clear();
            errorLabel2.setText("");
        }
//        orgComboBox.getItems().clear();
//        showComboBox();
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

    @FXML public void handleBannedUserListButton(ActionEvent actionEvent){
        try {
            com.github.saacsos.FXRouter.goTo("bannedUserList", objectWrapper);
        } catch (IOException e) {
            System.out.println("ไปที่หน้า bannedUserList ไม่ได้");
            System.out.println("ให้ตรวจสอบการกำหนด route");
            e.printStackTrace();
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