package sample;

import com.sun.javafx.runtime.VersionInfo;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Calendar");
        primaryStage.setWidth(1300);
        primaryStage.setHeight(1000);
        primaryStage.setScene(new Scene(root));
        primaryStage.centerOnScreen();
        primaryStage.show();

//        System.out.println("JavaFX Version: " + System.getProperty("javafx.version"));
//        System.out.println("JavaFX Runtime Version: " + System.getProperty("javafx.runtime.version"));
    }


    public static void main(String[] args) {
        launch(args);
    }
}
