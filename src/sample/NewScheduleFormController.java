package sample;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;

//import java.awt.event.ActionEvent;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;

import java.util.List;
import java.util.ResourceBundle;

import javafx.scene.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.*;
import javafx.util.StringConverter;
import jfxtras.scene.control.agenda.Agenda;



import javafx.collections.FXCollections;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import jfxtras.scene.control.CalendarPicker;
import jfxtras.scene.control.agenda.Agenda;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.Date;
import java.util.stream.Collectors;

import static sample.Util.*;

public class NewScheduleFormController implements Initializable {
    @FXML
    private Button cancelNewTimetableFormButton;

    @FXML
    private Button saveNewTimetableFormButton;

    @FXML
    private GridPane timetableFormRootPane;

    ComboBox<SubjectModel> comboBox = new ComboBox<>();

    @FXML
    private ComboBox<SubjectModel> subjectId;

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
                comboBox.getSelectionModel().getSelectedItem().getName(),
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
            int activityData = comboBox.getSelectionModel().getSelectedItem().getId();
            String classroomData = classroom.getText();
            String descriptionData = description.getText();
            LocalDate dateData = date.getValue();
            int startAtHourData = Integer.parseInt(startAtHour.getText());
            int startAtMinuteData = Integer.parseInt(startAtMinute.getText());
            int endAtHourData = Integer.parseInt(endAtHour.getText());
            int endAtMinuteData = Integer.parseInt(endAtMinute.getText());

            String mutation = "INSERT INTO schedule (subjectId, classroom, description, date, startAtHour, startAtMinute, endAtHour, endAtMinute)" + " VALUES('"+activityData+"', '"+classroomData+"','"+descriptionData+"','"+dateData+"', '"+startAtHourData+"', '"+startAtMinuteData+"', '"+endAtHourData+"', '"+endAtMinuteData+"' )";
            System.out.println(mutation);
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
                }
            }catch (Exception exception){
                exception.printStackTrace();
            }
        }

    }

    public void clearInput()  {
        classroom.clear();
        description.clear();
        date.setValue(null);
        startAtHour.clear();
        startAtMinute.clear();
        endAtHour.clear();
        endAtMinute.clear();
    }

//            subjectId.getItems().addAll(FXCollections.observableArrayList(new SubjectModel(1, "HAHA")));

//            for (SubjectModel x : list){
//        System.out.println(x.getName());
//    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setSubjectToForm();
    }

    public void setSubjectToForm(){
        try {
            ObservableList<SubjectModel> list = FXCollections.observableArrayList();
            var data = getSubjects();
            list.addAll(data);


            StringConverter<SubjectModel> converter = new StringConverter<SubjectModel>() {
                @Override
                public String toString(SubjectModel object) {
                    return object.getName();
                }

                @Override
                public SubjectModel fromString(String string) {
                    return comboBox.getItems().stream().filter(ap ->
                            ap.getName().equals(string)).findFirst().orElse(null);
//                return subjectId.getSelectionModel().getSelectedItem();
                }
            };

            comboBox.setConverter(converter);
            comboBox.setItems(list);
            comboBox.getSelectionModel().selectFirst();

            timetableFormRootPane.add(comboBox, 1, 0);

            comboBox.valueProperty().addListener((obs, oldVal, newVal) ->
                    System.out.println("newVal " + newVal.getName() ));

        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }


}
