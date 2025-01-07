package adminController;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import serverFX.DataSingleton;

import java.io.File;


public class adminMain extends Application {
        DataSingleton singelton = DataSingleton.getInstance();
    @Override
    public void start(Stage stage) throws Exception {
        File file = new File("/client.view/src/admin.fxml");
        if (file.exists()) {
            System.out.println("problem");
        }
        Parent root = FXMLLoader.load((getClass().getClassLoader().getResource(file.getName())));
        Scene scene = new Scene(root);
        stage.setTitle("Admin");
        stage.sizeToScene();
        stage.resizableProperty().setValue(Boolean.FALSE);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
