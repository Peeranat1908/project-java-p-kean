package ku.cs;

import com.github.saacsos.FXRouter;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class ProjectApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXRouter.bind(this, stage, "JavaDeeKub", 700, 500);
        configRoute();
        com.github.saacsos.FXRouter.goTo("login");
    }

    private static void configRoute() {
        String packageStr = "ku/cs/";
        FXRouter.when("login", packageStr + "login.fxml");
        FXRouter.when("register", packageStr + "register.fxml");
        FXRouter.when("about", packageStr + "about.fxml","JavaDeeKub"
                ,400, 500);
        FXRouter.when("staff", packageStr + "staff.fxml","JavaDeeKub"
                ,1024, 750);
        FXRouter.when("userEditor", packageStr + "userEditor.fxml","JavaDeeKub"
                ,1024, 750);
        FXRouter.when("staffEditor", packageStr + "staffEditor.fxml","JavaDeeKub"
                ,1024, 750);
        FXRouter.when("addStaffAccount", packageStr + "addStaffAccount.fxml","JavaDeeKub"
                ,1024, 750);
        FXRouter.when("organizationEditor", packageStr + "organizationEditor.fxml","JavaDeeKub"
                ,1024, 750);
        FXRouter.when("adminEditor", packageStr + "adminEditor.fxml","JavaDeeKub"
                ,1024, 750);
        FXRouter.when("report", packageStr + "report.fxml","JavaDeeKub"
                ,1024, 750);
        FXRouter.when("user", packageStr+"user.fxml","JavaDeeKub"
                ,1024, 768);
        FXRouter.when("changePassword", packageStr+"changePassword.fxml","JavaDeeKub"
                ,1024, 750);
        FXRouter.when("editReport", packageStr+"editReport.fxml","JavaDeeKub"
                ,1024, 750);
        FXRouter.when("userProfile", packageStr+"userProfile.fxml","JavaDeeKub"
                ,1024, 768);
        FXRouter.when("bannedUserBox", packageStr+"bannedUserBox.fxml","JavaDeeKub"
                ,600, 400);
        FXRouter.when("staffAccount", packageStr+"staffAccount.fxml","JavaDeeKub"
                ,1024, 750);
        FXRouter.when("bannedUserList", packageStr+"bannedUserList.fxml","JavaDeeKub"
                ,1024, 750);
        FXRouter.when("setting", packageStr + "setting.fxml");

    }

    public static void main(String[] args) {
        launch();
    }
}
