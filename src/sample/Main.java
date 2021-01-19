package sample;

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
    }


    public static void main(String[] args) {
        launch(args);
    }
}
