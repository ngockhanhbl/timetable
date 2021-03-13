package sample;

import com.jfoenix.controls.JFXSnackbar;
import javafx.collections.FXCollections;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import jfxtras.scene.control.agenda.Agenda;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Util {
    public static int getRandomNumberInRange(int min, int max) {
        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        return (int)(Math.random() * ((max - min) + 1)) + min;

    }

    public static boolean checkValidTime(String type, int time) {
        if (type == "hour"){
            if (time >= 0 && time <= 23 ){
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

    public static boolean isStartAtLessThanEndAt(int startAtHour, int startAtMinute, int endAtHour, int endAtMinute) {
        LocalTime start = LocalTime.of(startAtHour, startAtMinute);
        LocalTime end = LocalTime.of(endAtHour, endAtMinute);
        if(end.compareTo(start) > 0) {
            return true;
        }else {
            return false;
        }
    }

    public static void showAlertError(String header, String ErrorText) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Validate Error");
        alert.setHeaderText(header);
        alert.setContentText(ErrorText);
        alert.showAndWait();
    }

    public static void showSnackbarSuccess(Pane pane, String message) {
        JFXSnackbar snackbar = new JFXSnackbar(pane);
        final JFXSnackbar.SnackbarEvent snackbarEvent = new JFXSnackbar.SnackbarEvent(new Label(message), Duration.seconds(3.33), null);
        snackbar.enqueue(snackbarEvent);
    }
    public static List<String> getListOfWeek(LocalDateTime datetime) {
        List<String> dateList = new ArrayList<>();

        LocalDate date = datetime.toLocalDate();

        //Sunday
        if (datetime.getDayOfWeek().getValue() == 7){
            for (int i = 0; i < 7; i++) {
                dateList.add( '"' + date.minusDays(i).toString() + '"');
            }
        }
        //Saturday
        if (datetime.getDayOfWeek().getValue() == 6){
            for (int i = 0; i < 6; i++) {
                dateList.add('"' +date.minusDays(i).toString() + '"');
            }
            dateList.add('"' +date.plusDays(1).toString() + '"');
        }
        //Friday
        if (datetime.getDayOfWeek().getValue() == 5){
            for (int i = 0; i < 5; i++) {
                dateList.add('"' +date.minusDays(i).toString() + '"');
            }
            for (int i = 0; i < 2; i++) {
                dateList.add('"' +date.plusDays(i + 1).toString() + '"');
            }
        }
        //Thursday
        if (datetime.getDayOfWeek().getValue() == 4){
            // 5 4 3 2
            for (int i = 0; i < 4; i++) {
                dateList.add('"' +date.minusDays(i).toString() + '"');
            }
            // 6 7 8
            for (int i = 0; i < 3; i++) {
                dateList.add('"' +date.plusDays(i + 1).toString() + '"');
            }
        }
        //Wednesday
        if (datetime.getDayOfWeek().getValue() == 3){
            // 4 3 2
            for (int i = 0; i < 3; i++) {
                dateList.add('"' +date.minusDays(i).toString() + '"');
            }
            // 5 6 7 8
            for (int i = 0; i < 4; i++) {
                dateList.add('"' +date.plusDays(i + 1).toString() + '"');
            }
        }
        // Tuesday
        if (datetime.getDayOfWeek().getValue() == 2){
            for (int i = 0; i < 6; i++) {
                dateList.add('"' +date.plusDays(i).toString() + '"');
            }
            dateList.add('"' +date.minusDays(1).toString() + '"');
        }
        //Monday
        if (datetime.getDayOfWeek().getValue() == 1){
            for (int i = 0; i < 7; i++) {
                dateList.add('"' +date.plusDays(i).toString() + '"');
            }
        }

        return dateList;
    }

    public static Timetable parseAppointmentToTimetable(Agenda.Appointment appointment) throws SQLException {
        var subjects = getSubjects();
        SubjectModel subject = null;
        for(var sub: subjects){
            if ( sub.getName().equals(appointment.getSummary())) subject = sub;
        }
        int id = -1;
        String idString = appointment.getAppointmentGroup().getDescription();
        try {
            id = Integer.valueOf(idString);
        }catch (Exception e) {
            id = -1;
        }
        System.out.println("id");


        System.out.println("subject");
        System.out.println(subject);

        Timetable timetable = new Timetable(
                id,
                subject,
                appointment.getLocation(),
                appointment.getDescription(),
                appointment.getStartLocalDateTime().toLocalDate(),
                appointment.getStartLocalDateTime().getHour(),
                appointment.getStartLocalDateTime().getMinute(),
                appointment.getEndLocalDateTime().getHour(),
                appointment.getEndLocalDateTime().getMinute()
        );


        return timetable;
    }

    public static boolean checkValidInput(String activity, String classroom, LocalDate date, String startAtHour, String startAtMinute, String endAtHour, String endAtMinute)  {
        if(activity.trim().isEmpty() == true){
            showAlertError("Empty activity", "Please enter your activity !");
            return false;
        }else if(classroom.trim().isEmpty() == true){
            showAlertError("Empty classroom", "Please enter your classroom !");
            return false;
        }else if(date == null){
            showAlertError("Empty date", "Please enter your date !");
            return false;
        }else if(startAtHour.trim().isEmpty() == true){
            showAlertError("Empty Start at hour", "Please enter your start-at-hour !");
            return false;
        }else if(startAtMinute.trim().isEmpty() == true){
            showAlertError("Empty Start at minute", "Please enter your start-at-minute !");
            return false;
        }else if(endAtHour.trim().isEmpty() == true){
            showAlertError("Empty End at hour", "Please enter your end-at-hour !");
            return false;
        }else if(endAtMinute.trim().isEmpty() == true){
            showAlertError("Empty End at minute", "Please enter your end-at-minute !");
            return false;
        }

        int startAtHourParse;
        try {
            startAtHourParse = Integer.parseInt(startAtHour);
        }catch (NumberFormatException e){
            showAlertError("Number Format Exception", "Start at hour is not number !");
            System.out.println("error parse");
            return false;
        }
        boolean isValidStartAtHourParse = checkValidTime("hour", startAtHourParse);
        if (isValidStartAtHourParse == false){
            showAlertError("Hour Format Exception", "Start at hour is must be between 0 and 23 !");
            return false;
        }

        int startAtMinuteParse;
        try {
            startAtMinuteParse = Integer.parseInt(startAtMinute);
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
            endAtHourParse = Integer.parseInt(endAtHour);
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
            endAtMinuteParse = Integer.parseInt(endAtMinute);
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


    public static List<SubjectModel> getSubjects() throws SQLException {
        List<SubjectModel> subjectModelList = FXCollections.observableArrayList();

        PreparedStatement preparedStatement;
        ResultSet resultSet;
        var sql = String.format("SELECT * FROM subject");
        Connection connection = SqliteConnection.getInstance().getConnection();
        preparedStatement = connection.prepareStatement(sql);

        resultSet = preparedStatement.executeQuery();

        while (resultSet.next()){
            SubjectModel newSubject = new SubjectModel(
                    Integer.parseInt(resultSet.getString(1)),
                    resultSet.getString(2)
            );
            subjectModelList.add(newSubject);
        }

        return subjectModelList;
    }


}

