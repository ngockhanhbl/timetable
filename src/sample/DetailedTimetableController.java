package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;

public class DetailedTimetableController implements Initializable {

    @FXML
    private Button cancelDetailedTimetableButton;

    @FXML
    private Button updateDetailedTimetableButton;

    @FXML
    private Button deleteDetailedTimetableButton;

    @FXML
    private GridPane detailedTimetableRootPane;

    @FXML
    private TextField id;

    @FXML
    private TextField activity;

    @FXML
    private TextField classroom;

    @FXML
    private TextArea description;

    @FXML
    private DatePicker date;

    @FXML
    private TextField startAtHour;

    @FXML
    private TextField startAtMinute;

    @FXML
    private TextField endAtHour;

    @FXML
    private TextField endAtMinute;

    public void cancelDetailedTimetableClick(ActionEvent e){
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        stage.close();
    }
    public void updateDetailedTimetableButtonClick(){}
    public void deleteDetailedTimetableButtonClick(ActionEvent e){
        System.out.println(" delete ");
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        System.out.println(activity.getText());



    }

    public void setTimetable(Timetable timetable) {
        activity.setText(timetable.getActivity());
        classroom.setText(timetable.getClassroom());
        description.setText(timetable.getDescription());
        date.setValue(timetable.getDate());
        startAtHour.setText(String.valueOf(timetable.getStartAtHour()));
        startAtMinute.setText(String.valueOf(timetable.getStartAtMinute()));
        endAtHour.setText(String.valueOf(timetable.getEndAtHour()));
        endAtMinute.setText(String.valueOf(timetable.getEndAtMinute()));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
