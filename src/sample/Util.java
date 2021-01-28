package sample;

import com.jfoenix.controls.JFXSnackbar;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import jfxtras.scene.control.agenda.Agenda;

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

    public static Timetable parseAppointmentToTimetable(Agenda.Appointment appointment) {
        Timetable timetable = new Timetable(
                1,
                appointment.getSummary(),
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

//    LocalDate date,
//    int startAtHour,
//    int startAtMinute,
//    int endAtHour,
//    int endAtMinute


}

