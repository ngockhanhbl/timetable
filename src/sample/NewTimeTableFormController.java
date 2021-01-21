package sample;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;

//import java.awt.event.ActionEvent;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.ResourceBundle;

import javafx.scene.*;
import javafx.stage.*;

public class NewTimeTableFormController implements Initializable {
    @FXML
    private Button cancelNewTimetableFormButton;

    @FXML
    private Button saveNewTimetableFormButton;

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

    public void cancelNewTimetableForm(ActionEvent e) throws IOException {
        System.out.println("run close");
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        stage.close();
    }

    public void saveNewTimetableForm(ActionEvent e) throws IOException {
        System.out.println("run save");
        boolean isValid = checkValidInput();
        if (isValid == true){
            System.out.println("Valid");
            Connection connection = SqliteConnection.getInstance().getConnection();

            String activityData = activity.getText();
            String classroomData = classroom.getText();
            String descriptionData = description.getText();
            LocalDate dateData = date.getValue();
            int startAtHourData = Integer.parseInt(startAtHour.getText());
            int startAtMinuteData = Integer.parseInt(startAtMinute.getText());
            int endAtHourData = Integer.parseInt(endAtHour.getText());
            int endAtMinuteData = Integer.parseInt(endAtMinute.getText());


            String mutation = "INSERT INTO timetable (activity, classroom, description, date, startAtHour, startAtMinute, endAtHour, endAtMinute)" + " VALUES('"+activityData+"', '"+classroomData+"','"+descriptionData+"','"+dateData+"', '"+startAtHourData+"', '"+startAtMinuteData+"', '"+endAtHourData+"', '"+endAtMinuteData+"' )";
            try {
                Statement statement = connection.createStatement();
                int status = statement.executeUpdate(mutation);
                if (status > 0){
                    System.out.println("insert success");
                    // clear input

                    //show notification
                }
            }catch (Exception exception){
                exception.printStackTrace();
            }
        }

    }

    public boolean checkValidTime(String type, int time)  {
        if (type == "hour"){
            if (time >= 0 && time <= 24 ){
                return true;
            }else {
                return false;
            }
        }else {
            if (time >= 0 && time <= 59 ){
                return true;
            }else {
                return false;
            }
        }
    }

    public boolean checkValidInput()  {
        if(activity.getText().trim().isEmpty() == true){
            showAlertError("Empty activity", "Please enter your activity !");
            return false;
        }else if(classroom.getText().trim().isEmpty() == true){
            showAlertError("Empty classroom", "Please enter your classroom !");
            return false;
        }else if(date.getValue() == null){
            showAlertError("Empty date", "Please enter your date !");
            return false;
        }else if(startAtHour.getText().trim().isEmpty() == true){
            showAlertError("Empty Start at hour", "Please enter your start-at-hour !");
            return false;
        }else if(startAtMinute.getText().trim().isEmpty() == true){
            showAlertError("Empty Start at minute", "Please enter your start-at-minute !");
            return false;
        }else if(endAtHour.getText().trim().isEmpty() == true){
            showAlertError("Empty End at hour", "Please enter your end-at-hour !");
            return false;
        }else if(endAtMinute.getText().trim().isEmpty() == true){
            showAlertError("Empty End at minute", "Please enter your end-at-minute !");
            return false;
        }

        int startAtHourParse;
        try {
            startAtHourParse = Integer.parseInt(startAtHour.getText());
        }catch (NumberFormatException e){
            showAlertError("Number Format Exception", "Start at hour is not number !");
            System.out.println("error parse");
            return false;
        }
        boolean isValidStartAtHourParse = checkValidTime("hour", startAtHourParse);
        if (isValidStartAtHourParse == false){
            showAlertError("Hour Format Exception", "Start at hour is must be between 0 and 24 !");
            return false;
        }

        int startAtMinuteParse;
        try {
            startAtMinuteParse = Integer.parseInt(startAtMinute.getText());
        }catch (NumberFormatException e){
            showAlertError("Number Format Exception", "Start at minute is not number !");
            return false;
        }
        boolean isValidStartAtMinuteParse = checkValidTime("minute", startAtMinuteParse);
        if (isValidStartAtMinuteParse == false){
            showAlertError("Minute Format Exception", "Start at minute is must be between 0 and 59 !");
            return false;
        }

        int endAtHourParse;
        try {
            endAtHourParse = Integer.parseInt(endAtHour.getText());
        }catch (NumberFormatException e){
            showAlertError("Number Format Exception", "End at hour is not number !");
            return false;
        }
        boolean isValidEndAtHourParse = checkValidTime("hour", endAtHourParse);
        if (isValidEndAtHourParse == false){
            showAlertError("Hour Format Exception", "End at hour is must be between 0 and 24 !");
            return false;
        }

        int endAtMinuteParse;
        try {
            endAtMinuteParse = Integer.parseInt(endAtMinute.getText());
        }catch (NumberFormatException e){
            showAlertError("Number Format Exception", "End at minute is not number !");
            return false;
        }
        boolean isValidEndAtMinuteParse = checkValidTime("minute", endAtMinuteParse);
        if (isValidEndAtMinuteParse == false){
            showAlertError("Minute Format Exception", "End at minute is must be between 0 and 59 !");
            return false;
        }

        boolean isValidStartAtLessThanEndAt = isStartAtLessThanEndAt(startAtHourParse, startAtMinuteParse, endAtHourParse, endAtMinuteParse);
        if (isValidStartAtLessThanEndAt == false){
            showAlertError("Error Time", "Start Time should be greater than end time");
            return false;
        }
        return true;
    }

    private boolean isStartAtLessThanEndAt(int startAtHour, int startAtMinute, int endAtHour, int endAtMinute) {
        LocalTime start = LocalTime.of(startAtHour, startAtMinute);
        LocalTime end = LocalTime.of(endAtHour, endAtMinute);
        if(end.compareTo(start) > 0) {
            return true;
        }else {
            return false;
        }
    }

    private void showAlertError(String header, String ErrorText) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Validate Error");
        alert.setHeaderText(header);
        alert.setContentText(ErrorText);
        alert.showAndWait();
    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
