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
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.stage.Window;
import javafx.util.StringConverter;

import static sample.Util.checkValidInput;
import static sample.Util.showSnackbarSuccess;

public class DetailedTimetableController implements Initializable {

    @FXML
    private Button cancelDetailedTimetableButton;

    @FXML
    private Button updateDetailedTimetableButton;

    @FXML
    private Button deleteDetailedTimetableButton;

    @FXML
    private GridPane detailedTimetableRootPane;

    private int id;

    ComboBox<SubjectModel> comboBox = new ComboBox<>();

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

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("sample.fxml"));
        try {
            Parent newTimetableFormParent = fxmlLoader.load();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }


        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        stage.close();
    }
    public void updateDetailedTimetableButtonClick(ActionEvent e){
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
            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();

            String updateQuery = "UPDATE timetable SET subjectId= ? , classroom= ? , description= ? , date= ? , startAtHour= ? , startAtMinute= ? , endAtHour= ? , endAtMinute= ?  WHERE id= ?";

            Connection connection = SqliteConnection.getInstance().getConnection();
            System.out.println("comboBox.getSelectionModel().getSelectedItem().getId()");
            System.out.println(comboBox.getSelectionModel().getSelectedItem().getId());

            try {
                PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
                preparedStatement.setInt(1, comboBox.getSelectionModel().getSelectedItem().getId());
                preparedStatement.setString(2, classroom.getText());
                preparedStatement.setString(3, description.getText());
                preparedStatement.setString(4, date.getValue().toString());
                preparedStatement.setInt(5, Integer.valueOf(startAtHour.getText()));
                preparedStatement.setInt(6, Integer.valueOf(startAtMinute.getText()));
                preparedStatement.setInt(7, Integer.valueOf(endAtHour.getText()));
                preparedStatement.setInt(8, Integer.valueOf(endAtMinute.getText()));
                preparedStatement.setInt(9, getId());

                int status = preparedStatement.executeUpdate();

                System.out.println("status");
                System.out.println(status);

                if (status > 0) {
                    //close connection
                    connection.close();
                    // close modal
                    stage.close();

                }
            }catch (SQLException throwable){
                throwable.printStackTrace();
            }
        }
    }

    public void deleteDetailedTimetableButtonClick(ActionEvent e) throws IOException {
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        System.out.println(stage.getHeight());
        System.out.println(stage.getWidth());
        System.out.println(getId());

        String deleteQuery = "DELETE FROM timetable WHERE id= ?";

        Connection connection = SqliteConnection.getInstance().getConnection();


        try {
            PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery);
            preparedStatement.setInt(1, getId());

            int status = preparedStatement.executeUpdate();

            if (status > 0) {
                //close connection
                connection.close();

                // close modal
                stage.close();

                // show snackbar
//                FXMLLoader fxmlLoader = new FXMLLoader();
//                fxmlLoader.setLocation(getClass().getResource("sample.fxml"));
//                Parent rootPane = fxmlLoader.load();
//                showSnackbarSuccess((Pane) rootPane, "Deleted successfully");
            }
        }catch (SQLException throwable){
            throwable.printStackTrace();
        }
    }

    public void setTimetable(Timetable timetable, List<SubjectModel> subjects) {
        ObservableList<SubjectModel> list = FXCollections.observableArrayList();
        list.addAll(subjects);
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

        comboBox.getSelectionModel().select(timetable.getSubjectId());

        detailedTimetableRootPane.add(comboBox, 1, 0);

        classroom.setText(timetable.getClassroom());
        description.setText(timetable.getDescription());
        date.setValue(timetable.getDate());
        startAtHour.setText(String.valueOf(timetable.getStartAtHour()));
        startAtMinute.setText(String.valueOf(timetable.getStartAtMinute()));
        endAtHour.setText(String.valueOf(timetable.getEndAtHour()));
        endAtMinute.setText(String.valueOf(timetable.getEndAtMinute()));

        setId(timetable.getId());

        System.out.println("timetable.getId()");
        System.out.println(timetable.getId());

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
