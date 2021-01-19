package sample;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;

//import java.awt.event.ActionEvent;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.scene.*;
import javafx.stage.*;

public class NewTimeTableFormController implements Initializable {
    public Button buttonAdd;

    public void btnAddClick(ActionEvent e) throws IOException {
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("new_timetable_form.fxml"));
        Parent newTimetableFormParent = fxmlLoader.load();
        Scene scene = new Scene(newTimetableFormParent);

        Stage dialog = new Stage();
        dialog.setScene(scene);
        dialog.setTitle("New Timetable Form");
        dialog.initOwner(stage);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.showAndWait();

//        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("new_timetable_form.fxml"));
//        Parent root1 = (Parent) fxmlLoader.load();
//
//        Stage stage = new Stage();
//        stage.setTitle("New Timetable Form");
//        stage.initModality(Modality.WINDOW_MODAL);
//        stage.initStyle(StageStyle.UNDECORATED);
//        stage.setScene(new Scene(root1));
//        stage.show();

    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
