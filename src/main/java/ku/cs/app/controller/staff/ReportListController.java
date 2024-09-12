package ku.cs.app.controller.staff;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import ku.cs.app.models.Report;
import ku.cs.app.models.Theme;
import ku.cs.app.services.Clickable;

import java.io.File;

public class ReportListController {
    @FXML
    private ImageView categoryIcon;

    @FXML
    private Label categoryLabel;

    @FXML
    private Label dateReportLabel;

    @FXML
    private Label detailReportLabel;

    @FXML
    private void click(MouseEvent mouseEvent) {
        clickable.onClickList(report);
    }
    @FXML
    AnchorPane parentBox;

    private Report report;
    private Clickable clickable;

    String lightModePath = new File(System.getProperty("user.dir") + File.separator + "/style/light_mode.css").toURI().toString();
    String darkModePath = new File(System.getProperty("user.dir") + File.separator + "/style/dark_mode.css").toURI().toString();

    public void setData(Report report, Clickable clickable, Boolean isLightMode) {
        checkMode(isLightMode);
        this.report = report;
        this.clickable = clickable;
        categoryLabel.setText(report.getCategoryName());
//        Image image = new Image(getClass().getResourceAsStream(report.getCategoryIcon()));
//        categoryIcon.setImage(image);
        dateReportLabel.setText(report.getDate());
        detailReportLabel.setMaxWidth(130);
        detailReportLabel.setWrapText(true);
        detailReportLabel.setText(report.getTitle());
//        categoryLabel.setStyle("-fx-background-color: #" + report.getCategoryColor() + ";\n" +
//                " -fx-background-radius: 100;");


    }
    private void checkMode(Boolean isLightMode) {
        if (isLightMode) {
            setLightMode();
        } else {
            setDarkMode();
        }
    }

    private void setLightMode() {
        parentBox.getStylesheets().add(lightModePath);
        parentBox.getStylesheets().remove(darkModePath);
//        report.setCategoryColor("67b65d");

    }
    private void setDarkMode() {
        parentBox.getStylesheets().add(darkModePath);
        parentBox.getStylesheets().remove(lightModePath);
//        report.setCategoryColor("75bb65");

    }

}