package sample;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.time.LocalDate;

public class Timetable {
    private int id;
    private SubjectModel subjectId;
    private String classroom;
    private String description;
    private LocalDate date;
    private int startAtHour;
    private int startAtMinute;
    private int endAtHour;
    private int endAtMinute;

    public void setId(int id) {
        this.id = id;
    }

    public void setActivity(SubjectModel subjectId) {
        this.subjectId = subjectId;
    }

    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setStartAtHour(int startAtHour) {
        this.startAtHour = startAtHour;
    }

    public void setStartAtMinute(int startAtMinute) {
        this.startAtMinute = startAtMinute;
    }

    public void setEndAtHour(int endAtHour) {
        this.endAtHour = endAtHour;
    }

    public void setEndAtMinute(int endAtMinute) {
        this.endAtMinute = endAtMinute;
    }

    public int getId() {
        return id;
    }


    public String getClassroom() {
        return classroom;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getDate() {
        return date;
    }

    public int getStartAtHour() {
        return startAtHour;
    }

    public int getStartAtMinute() {
        return startAtMinute;
    }

    public int getEndAtHour() {
        return endAtHour;
    }

    public int getEndAtMinute() {
        return endAtMinute;
    }

    public Timetable(int id, SubjectModel subjectId, String classroom, String description, LocalDate date, int startAtHour, int startAtMinute, int endAtHour, int endAtMinute) {
        this.id = id;
        this.subjectId = subjectId;
        this.classroom = classroom;
        this.description = description;
        this.date = date;
        this.startAtHour = startAtHour;
        this.startAtMinute = startAtMinute;
        this.endAtHour = endAtHour;
        this.endAtMinute = endAtMinute;
    }

    public SubjectModel getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(SubjectModel subjectId) {
        this.subjectId = subjectId;
    }
}
