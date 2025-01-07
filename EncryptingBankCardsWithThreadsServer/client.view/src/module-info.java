module client.view {
    requires javafx.graphics;
    requires javafx.controls;
    requires businessLogic;
    requires javafx.fxml;
    requires javafx.base;

    opens javaFxModules to javafx.fxml;
    opens adminController to javafx.fxml;
    exports adminController to javafx.graphics, javafx.fxml;
    exports javaFxModules to javafx.graphics, javafx.fxml;
    exports serverFX to javafx.graphics;

}