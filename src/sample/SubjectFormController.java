package sample;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.w3c.dom.Text;

import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import static sample.Util.*;

public class SubjectFormController implements Initializable {
    @FXML
    public Button saveBtn;

    @FXML
    public Button deleteBtn;

    @FXML
    public TextField nameTextField;

    @FXML
    public Label nameLabel;

    @FXML
    public TableColumn<SubjectModel, String> nameColumn;

    @FXML
    public TableColumn<SubjectModel, Number> idColumn;

    @FXML
    public TableView<SubjectModel> table;

    private ObservableList<SubjectModel> subjectsList;

    public void addBtnClick(ActionEvent e) throws SQLException {
        String name = nameTextField.getText().trim();

        var check = checkExit(name);

        if (name.isEmpty() == true){
            showAlertError("Empty Subject", "please enter your subject");
        }else if (check == true){
            showAlertError("Subject already exists\n", "please try another subject");
        }else {
            SubjectModel newSubject = new SubjectModel();
            newSubject.setName(name);
            newSubject.setId(subjectsList.size());
            subjectsList.add(newSubject);
            addSubject(newSubject);
        }

    }

    public void saveClick(ActionEvent e) throws SQLException {
        var selectedItem = table.getSelectionModel().getSelectedItem();
        String name = nameTextField.getText().trim();

        var check = checkExit(name);

        if (name.isEmpty() == true){
            showAlertError("Empty Subject", "please enter your subject");
        }else if (check == true){
            showAlertError("Subject already exists\n", "please try another subject");
        }else {
            SubjectModel newSubject = new SubjectModel();
            newSubject.setName(name);
            newSubject.setId(selectedItem.getId());

            int size = subjectsList.size();
            System.out.println(selectedItem.getName());
            for(int i = 0; i< size;i++){
                System.out.println(subjectsList.get(i).getName());
                System.out.println("i:" + i);

                System.out.println(subjectsList.get(i).getName().equals(selectedItem.getName()));
                if (subjectsList.get(i).getName().equals(selectedItem.getName()) ){

                    System.out.println(newSubject.getName());
                    System.out.println(newSubject.getId());

                    subjectsList.remove(i);
                    subjectsList.add(i, newSubject);
                    table.getSelectionModel().select(i);


                }
            }

            editSubject(selectedItem, nameTextField.getText().trim());

        }

    }

    public boolean checkExit(String name){
        boolean check = false;
        for(var item: subjectsList ){
            if (item.getName().equals(name)){
                check = true;
                break;
            }
        }
        return check;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            List<SubjectModel> subjects = getSubjects();
            subjectsList = FXCollections.observableArrayList(subjects);
            nameColumn.setCellValueFactory(new PropertyValueFactory<SubjectModel, String>("name"));
            idColumn.setSortable(false);
            idColumn.setCellValueFactory(column-> new ReadOnlyObjectWrapper<Number>(table.getItems().indexOf(column.getValue()) + 1));
            table.setItems(subjectsList);

            table.getSelectionModel().selectedItemProperty().addListener((observable, oldItem, newItem) -> {
                nameTextField.setText(newItem.getName());
            });


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


    }


}
