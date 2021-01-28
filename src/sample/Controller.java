package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import jfxtras.icalendarfx.VCalendar;
import jfxtras.scene.control.agenda.Agenda;
import jfxtras.scene.control.agenda.icalendar.ICalendarAgenda;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static sample.Util.*;

public class Controller implements Initializable {
    public Button addButton;

    @FXML
    private BorderPane rootPane;

    public void addButtonClick(ActionEvent e) throws IOException {
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


    }
    public void loadDataButtonClick(ActionEvent e) throws IOException {
        Agenda agenda = (Agenda) rootPane.getCenter();
        List<String> WeekDateList = getListOfWeek(agenda.getDisplayedLocalDateTime());
        List<Timetable> timetableList = null;
        try {
            timetableList = queryTimetableByWeekDateList(WeekDateList);
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }

        agenda.appointments().clear();

        for (Timetable item : timetableList) {
            LocalDate date = LocalDate.parse(item.getDate().toString());
            agenda.appointments().addAll(
                    new Agenda.AppointmentImplLocal()

                            .withStartLocalDateTime(date.atTime(item.getStartAtHour(), item.getStartAtMinute()))
                            .withEndLocalDateTime(date.atTime(item.getEndAtHour(), item.getEndAtMinute()))
                            .withSummary(item.getActivity())
                            .withDescription(item.getDescription())
                            .withLocation(item.getClassroom())
                            .withAppointmentGroup(new Agenda.AppointmentGroupImpl().withStyleClass("group" + getRandomNumberInRange(0, 20))) // you should use a map of AppointmentGroups
            );
        }


    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Agenda agenda = new Agenda();
        agenda.setAllowDragging(false);
        rootPane.setCenter(agenda);

        List<String> WeekDateList = getListOfWeek(agenda.getDisplayedLocalDateTime());
        List<Timetable> timetableList = null;

        try {
            timetableList = queryTimetableByWeekDateList(WeekDateList);
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }

        //Sorting Algorithms
        MergeSort ob = new MergeSort();
        ob.sort(timetableList, 0, timetableList.size() - 1);

        for (Timetable item : timetableList) {
            LocalDate date = LocalDate.parse(item.getDate().toString());
            agenda.appointments().addAll(
                    new Agenda.AppointmentImplLocal()
                            .withStartLocalDateTime(date.atTime(item.getStartAtHour(), item.getStartAtMinute()))
                            .withEndLocalDateTime(date.atTime(item.getEndAtHour(), item.getEndAtMinute()))
                            .withSummary(item.getActivity())
                            .withDescription(item.getDescription())
                            .withLocation(item.getClassroom())
                            .withAppointmentGroup(new Agenda.AppointmentGroupImpl().withStyleClass("group" + getRandomNumberInRange(0, 20))) // you should use a map of AppointmentGroups
            );
        }

        agenda.setActionCallback((appointment -> {
            // truyền được data sang bên kia
            Stage stage = (Stage) rootPane.getScene().getWindow();

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("detailed_timetable.fxml"));
            Parent detailedTimetableParent = null;

            try {
                detailedTimetableParent = fxmlLoader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }


            if (detailedTimetableParent != null){
                //set Data to  detailed timetable view
                DetailedTimetableController detailedTimetableController = fxmlLoader.getController();
                Timetable selected = parseAppointmentToTimetable(appointment);
                detailedTimetableController.setTimetable(selected);

                System.out.println("detailedTimetableController");
                System.out.println(detailedTimetableController);

                Scene scene = new Scene(detailedTimetableParent);
                Stage dialog = new Stage();
                dialog.setScene(scene);
                dialog.setTitle("Detailed Timetable");
                dialog.initOwner(stage);
                dialog.initModality(Modality.APPLICATION_MODAL);
                dialog.showAndWait();
            }
            return null;
        }));




    }
    public List<Timetable> queryTimetableByWeekDateList( List<String> WeekDateList) throws SQLException {
        List<Timetable> timetableList = FXCollections.observableArrayList();

        PreparedStatement preparedStatement;
        ResultSet resultSet;
        var sql = String.format("SELECT * FROM timetable WHERE date IN (%s)",
                WeekDateList.stream()
                        .collect(Collectors.joining(", ")));
        Connection connection = SqliteConnection.getInstance().getConnection();
        preparedStatement = connection.prepareStatement(sql);
        resultSet = preparedStatement.executeQuery();

        while (resultSet.next()){
            Timetable newTimeTable = new Timetable(
                    Integer.parseInt(resultSet.getString(1)),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    LocalDate.parse(resultSet.getString(5)),
                    resultSet.getInt(6),
                    resultSet.getInt(7),
                    resultSet.getInt(8),
                    resultSet.getInt(9)
            );
            timetableList.add(newTimeTable);
        }

        return timetableList;
    }


    public BorderPane getRootPane() {
        return rootPane;
    }

    public void setRootPane(BorderPane rootPane) {
        this.rootPane = rootPane;
    }

}
