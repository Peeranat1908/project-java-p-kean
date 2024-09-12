module cs.ku {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires java.desktop;

    opens ku.cs to javafx.fxml;
    exports ku.cs;

    exports ku.cs.app.controller;
    opens ku.cs.app.controller to javafx.fxml;
    exports ku.cs.app.controller.staff;
    opens ku.cs.app.controller.staff to javafx.fxml;

    opens ku.cs.app.models to javafx.base;
    exports ku.cs.app.models;
    exports ku.cs.app.controller.admin;
    opens ku.cs.app.controller.admin to javafx.fxml;
}