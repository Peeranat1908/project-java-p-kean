package ku.cs.app.controller;

import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
import ku.cs.app.models.Category;
import ku.cs.app.models.Report;
import ku.cs.app.services.Clickable;

import java.io.IOException;

public class ReportBoxController {

    @FXML
    Label titleLabel;
    @FXML
    Label categoryLabel;
    @FXML
    Label dateReportLabel;
    @FXML
    Label statusLabel;
    @FXML
    Label likeAmountLabel;
    @FXML
    Button likeButton;

    @FXML
    private void click(MouseEvent mouseEvent){
        clickable.onClickList(report);
    }

    private Report report;
    private Clickable clickable;

    private String waiting = "ยังไม่รับเรื่อง";
    private String inProgress = "อยู่ระหว่างดำเนินการ";
    private String finished = "เสร็จสิ้น";

    public void setData(Report report, Clickable clickable , Category category) {
        this.report = report;
        this.clickable = clickable;
        titleLabel.setText(report.getTitle());
        categoryLabel.setText("#" +report.getCategoryName());
        dateReportLabel.setText(report.getDate() + " " + report.getTime());
        statusLabel.setText(report.getStatus());
        System.out.println(report.getStatus());
        if (report.getStatus().equals(waiting)) {statusLabel.setStyle("-fx-background-color: #C0392B");}
        if (report.getStatus().equals(inProgress)) {statusLabel.setStyle("-fx-background-color: #E67E22");}
        if (report.getStatus().equals(finished)) {statusLabel.setStyle("-fx-background-color: #27AE60");}
        categoryLabel.setStyle("-fx-background-color: #" + category.getCategoryColor());
        likeAmountLabel.setText(String.valueOf(report.getLikeAmount()));
    }

    public void handleLikeButtonEnter() {
        ScaleTransition scale = new ScaleTransition();
        scale.setNode(likeButton);
        scale.setByX(0.3);
        scale.setByY(0.3);
        scale.setDuration(Duration.seconds(0.2));
        scale.play();

    }
    public void handleLikeButtonExit() {
        ScaleTransition scale = new ScaleTransition();
        scale.setNode(likeButton);
        scale.setToX(1);
        scale.setToY(1);
        scale.setDuration(Duration.seconds(0.2));
        scale.play();
    }

    @FXML
    public void handleLikeButton(ActionEvent actionEvent) throws IOException { clickable.onClickLike(report);}
}
