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
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import jfxtras.icalendarfx.VCalendar;
import jfxtras.scene.control.CalendarPicker;
import jfxtras.scene.control.agenda.Agenda;
import jfxtras.scene.control.agenda.icalendar.ICalendarAgenda;

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
import static sample.Util.getListOfWeek;

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
                            .withSummary(item.getSubjectId().getName())
                            .withDescription(item.getDescription())
                            .withLocation(item.getClassroom())
                            .withAppointmentGroup(new Agenda.AppointmentGroupImpl().withStyleClass("group" + getRandomNumberInRange(0, 20))) // you should use a map of AppointmentGroups
            );
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        CalendarPicker calendarPicker = new CalendarPicker();
        Agenda agenda = new Agenda();
        agenda.setAllowDragging(false);
        rootPane.setCenter(agenda);
        rootPane.setRight(calendarPicker);
        List<String> weekDateList = getListOfWeek(agenda.getDisplayedLocalDateTime());
        List<Timetable> timetableList = null;
        try {
            timetableList = queryTimetableByWeekDateList(weekDateList);
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
                            .withSummary(item.getSubjectId().getName())
                            .withDescription(item.getDescription())
                            .withLocation(item.getClassroom())
                            .withAppointmentGroup(new Agenda.AppointmentGroupImpl().withStyleClass("group" + getRandomNumberInRange(0, 20)).withDescription(String.valueOf(item.getId())))
            );
        }

        agenda.setActionCallback((appointment -> {
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
                Timetable selected = null;
                try {
                    selected = parseAppointmentToTimetable(appointment);
                } catch (SQLException throwables) {
                    System.out.println("throwables");
                    throwables.printStackTrace();
                }
                System.out.println("selected");
                System.out.println(selected);

                detailedTimetableController.setTimetable(selected);

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

        calendarPicker.withValueValidationCallback((calendarRange -> {
//            Skin skin = AgendaWeekSkin();
            Date date = calendarRange.getTime();
            LocalDateTime localDateTime = LocalDateTime.ofInstant(date.toInstant(),
                    ZoneId.systemDefault());
            agenda.setDisplayedLocalDateTime(localDateTime);
            List<Timetable> _timetableList = null;

            List<String> _weekDateList = getListOfWeek(agenda.getDisplayedLocalDateTime());
            try {
                _timetableList = queryTimetableByWeekDateList(_weekDateList);
            } catch (SQLException throwable) {
                throwable.printStackTrace();
            }

            agenda.appointments().removeAll(agenda.appointments());
            for (Timetable item : _timetableList) {
                LocalDate _date = LocalDate.parse(item.getDate().toString());
                agenda.appointments().addAll(
                        new Agenda.AppointmentImplLocal()
                                .withStartLocalDateTime(_date.atTime(item.getStartAtHour(), item.getStartAtMinute()))
                                .withEndLocalDateTime(_date.atTime(item.getEndAtHour(), item.getEndAtMinute()))
                                .withSummary(item.getSubjectId().getName())
                                .withDescription(item.getDescription())
                                .withLocation(item.getClassroom())
                                .withAppointmentGroup(new Agenda.AppointmentGroupImpl().withStyleClass("group" + getRandomNumberInRange(0, 20)).withDescription(String.valueOf(item.getId())))
                );
            }

            agenda.setActionCallback((appointment -> {
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
                    Timetable selected = null;
                    try {
                        selected = parseAppointmentToTimetable(appointment);
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                    detailedTimetableController.setTimetable(selected);

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

            return true;
        }));

    }

    public List<Timetable> queryTimetableByWeekDateList( List<String> WeekDateList) throws SQLException {
        List<Timetable> timetableList = FXCollections.observableArrayList();

        PreparedStatement preparedStatement;
        ResultSet resultSet;
        var sql = String.format(
//                "SELECT * FROM timetable  WHERE date IN (%s)",

                "SELECT timetable.*, subject.*\n" +
                        "FROM timetable\n" +
                        "JOIN subject ON subject.id = timetable.subjectId \n" +
                        "where date IN (%s)",

                WeekDateList.stream()
                        .collect(Collectors.joining(", ")));

        System.out.println(sql);


        Connection connection = SqliteConnection.getInstance().getConnection();
        preparedStatement = connection.prepareStatement(sql);
        resultSet = preparedStatement.executeQuery();
        while (resultSet.next()){
            Timetable newTimeTable = new Timetable(
                    Integer.parseInt(resultSet.getString(1)),
                    new SubjectModel(
                            resultSet.getInt(10),
                            resultSet.getString(11)
                    ),
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




