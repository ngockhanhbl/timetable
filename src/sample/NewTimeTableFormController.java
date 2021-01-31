package sample;
import com.jfoenix.controls.JFXBadge;
import com.jfoenix.controls.JFXSnackbar;
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
import java.time.temporal.Temporal;
import java.util.Date;
import java.util.ResourceBundle;

import javafx.scene.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.*;
import javafx.util.Duration;
import jfxtras.scene.control.agenda.Agenda;

import static sample.Util.*;

public class NewTimeTableFormController implements Initializable {
    @FXML
    private Button cancelNewTimetableFormButton;

    @FXML
    private Button saveNewTimetableFormButton;

    @FXML
    private GridPane timetableFormRootPane;

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
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        stage.close();
    }

    public void saveNewTimetableForm(ActionEvent e) throws IOException {
        boolean isValid = checkValidInput(
                activity.getText(),
                classroom.getText(),
                date.getValue(),
                startAtHour.getText(),
                startAtMinute.getText(),
                endAtHour.getText(),
                endAtMinute.getText()
        );
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
                connection.close();
                if (status > 0){
                    System.out.println("insert success");
                    // clear input
                    clearInput();

                    //show Snackbar
                    showSnackbarSuccess(timetableFormRootPane, "Timetable successfully created");

                    // add to agenda

                    System.out.println("status");
                    System.out.println(status);


                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource("sample.fxml"));
                    Parent rootPane = fxmlLoader.load();
                    Controller controller = fxmlLoader.getController();
                    Agenda agenda = (Agenda) controller.getRootPane().getCenter();
                    System.out.println(agenda.appointments().stream().sorted());
                    agenda.appointments().addAll(
                            new Agenda.AppointmentImplLocal()
                                    .withStartLocalDateTime(LocalDate.now().atTime(4, 00))
                                    .withEndLocalDateTime(LocalDate.now().atTime(5, 30))
                                    .withSummary("TEST Ne")
                                    .withDescription("It's time")
                                    .withAppointmentGroup(new Agenda.AppointmentGroupImpl().withStyleClass("group1"))

//                            new Agenda.AppointmentImplLocal()
//                                    .withStartLocalDateTime(date.getValue().atTime(Integer.valueOf(startAtHour.getText()), Integer.valueOf(startAtMinute.getText())))
//                                    .withEndLocalDateTime(date.getValue().atTime(Integer.valueOf(endAtHour.getText()), Integer.valueOf(endAtMinute.getText())))
//                                    .withSummary(activity.getText())
//                                    .withDescription(description.getText())
//                                    .withLocation(classroom.getText())
//                                    .withAppointmentGroup(new Agenda.AppointmentGroupImpl().withStyleClass("group" + getRandomNumberInRange(0, 20)).withDescription("100"))
                    );
                }
            }catch (Exception exception){
                exception.printStackTrace();
            }
        }

    }

    public void clearInput()  {
        activity.clear();
        classroom.clear();
        description.clear();
        date.setValue(null);
        startAtHour.clear();
        startAtMinute.clear();
        endAtHour.clear();
        endAtMinute.clear();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
