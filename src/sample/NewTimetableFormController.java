package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import jfxtras.scene.control.agenda.Agenda;
import org.controlsfx.dialog.Wizard;
import org.controlsfx.dialog.WizardPane;
import org.controlsfx.validation.Validator;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static sample.Util.*;
import static sample.Util.getListOfWeek;

public class NewTimetableFormController implements Initializable {
//    @FXML
//    private Button cancelNewTimetableFormButton;

//    public void initWizard(ActionEvent e) throws IOException {
//        Wizard wizard = new Wizard();
//        wizard.setTitle("Linear Wizard");
//    }

    public void initWizard() throws SQLException, IOException {
        Wizard wizard = new Wizard();
        wizard.setTitle("Generate Timetable - Wizard View");

        // --- page 1
        int row = 0;
        GridPane page1Grid = new GridPane();
        page1Grid.setVgap(10);
        page1Grid.setHgap(10);
        page1Grid.add(new Label("Subject:"), 0, row);
        System.out.println("111");

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("sample.fxml"));
        BorderPane rootPane = fxmlLoader.load();
        System.out.println("rootPane");
        System.out.println(rootPane);

        Agenda agenda = (Agenda) rootPane.getCenter();

        List<String> WeekDateList = getListOfWeek(agenda.getDisplayedLocalDateTime());
        System.out.println("222");
        System.out.println(WeekDateList);
        List<ScheduleModel> schedules = getSchedules(WeekDateList);
        System.out.println("schedules");
        System.out.println(schedules);

        VBox vbox = new VBox();
        vbox.setPadding(new Insets(20,20,20,20));

        List<ScheduleModel> schedulesFiltered = schedules.stream()
                .filter(distinctByKey(p -> p.getSubjectId().getName()))
                .collect(Collectors.toList());

        System.out.println("schedulesFiltered");
        System.out.println(schedulesFiltered);

        for(var schedule: schedulesFiltered){
            CheckBox subjectsCheckBox = createCheckBox("schedule" + schedule.getId(), schedule.getSubjectId().getName());
            vbox.getChildren().addAll(subjectsCheckBox);
        }

        if (schedulesFiltered.size() <= 0){
            System.out.println("There is no schedule for this week");
            return;
        }
        page1Grid.add(vbox, 1, row++);

        WizardPane page1 = new WizardPane();
        page1.setHeaderText("Please Select Your Subject");
        page1.setContent(page1Grid);


        // --- page 2
        final WizardPane page2 = new WizardPane() {
            @Override
            public void onEnteringPage(Wizard wizard) {
                String firstName = (String) wizard .getSettings().get("firstName");
                String lastName = (String) wizard.getSettings().get("lastName");

                System.out.println("000");
                List<String> selectedSubject = new ArrayList<>();
                for (var item: vbox.getChildren()){
                    CheckBox checkboxItem = (CheckBox) item;
                    if (checkboxItem.isSelected()){
                        selectedSubject.add(checkboxItem.getText());
                    }
                }

                //schedule after selected subjects
                List<ScheduleModel> selectedSchedules = new ArrayList<ScheduleModel>();
                for (var item: schedules){
                    System.out.println(item.getSubjectId().getName());
                    if (selectedSubject.contains(item.getSubjectId().getName())){
                        selectedSchedules.add(item);
                    }
                }

                for (var i: selectedSchedules){
                    System.out.println(i.getSubjectId());
                }

                Map<String, List<ScheduleModel>> selectedSchedulesGroupBy = selectedSchedules.stream().collect(Collectors.groupingBy(w -> w.getSubjectId().getName()));

                System.out.println(selectedSchedulesGroupBy);
                System.out.println("aksdjlkasd");
                for (var i: selectedSchedulesGroupBy.entrySet()){
                    System.out.println(i.getValue());
                }
                List<List<ScheduleModel>> listOfListChedules = new ArrayList<>();
                List<ScheduleModel> temporaryList = new ArrayList<>();
                //parent
                int currentIndexArrayOfArray = selectedSchedulesGroupBy.entrySet().size() - 1;

                int currentIndexOfArray = 0;
                int lengthOfList = selectedSchedulesGroupBy.entrySet().size();

                int loopTotal = 1;
                for (var i: selectedSchedulesGroupBy.entrySet()){
                    loopTotal *= i.getValue().size();
                }
                System.out.println("loopTotal 4:::");
                System.out.println(loopTotal);

                for (var i = 0; i< loopTotal; i++){
                    for(int j = lengthOfList; j > 1 ; j--){
                        ScheduleModel data = null;
                        if (currentIndexArrayOfArray == j-1){
//                            data = selectedSchedulesGroupBy.entrySet().toArray()[currentIndexArrayOfArray][currentIndexOfArray];
                            System.out.println("data");
                            System.out.println(selectedSchedulesGroupBy.entrySet());
                            currentIndexOfArray++;
                        }else {
                            System.out.println(selectedSchedulesGroupBy.entrySet().toArray());
                            data = (ScheduleModel) selectedSchedulesGroupBy.entrySet().toArray()[j-1];
                        }
                        temporaryList.add(data);
//                        if ()
                    }
//                    listOfListChedules.add(temporaryList);
                }


                selectedSchedulesGroupBy.entrySet().stream().forEach(r -> System.out.println(r.getValue()));

                for (var i: selectedSchedulesGroupBy.entrySet()){
                    System.out.println(i.getValue());
                }






                if (selectedSubject == null){
                    setContentText("the subject list is empty. Please select a subject to create a timetable\n ");
                    return;
                }else {
                    setContentText("Please lick on the next button to create the timetable\n");
                }




            }
        };
        page2.setHeaderText("Thanks For Your Details!");

        // --- page 3
        WizardPane page3 = new WizardPane();
        page3.setHeaderText("Goodbye!");
        page3.setContentText("Page 3, with extra 'help' button!");

        ButtonType helpDialogButton = new ButtonType("Help", ButtonBar.ButtonData.HELP_2);
        page3.getButtonTypes().add(helpDialogButton);
        Button helpButton = (Button) page3.lookupButton(helpDialogButton);
        helpButton.addEventFilter(ActionEvent.ACTION, actionEvent -> {
            actionEvent.consume(); // stop hello.dialog from closing
            System.out.println("Help clicked!");
        });

//        // --- page 3
//        WizardPane page3 = new WizardPane();
//        page3.setHeaderText("Goodbye!");
//        page3.setContentText("Page 3, with extra 'help' button!");
//
//        ButtonType helpDialogButton = new ButtonType("Help", ButtonBar.ButtonData.HELP_2);
//        page3.getButtonTypes().add(helpDialogButton);
//        Button helpButton = (Button) page3.lookupButton(helpDialogButton);
//        helpButton.addEventFilter(ActionEvent.ACTION, actionEvent -> {
//            actionEvent.consume(); // stop hello.dialog from closing
//            System.out.println("Help clicked!");
//        });


        wizard.setFlow(new Wizard.LinearFlow(page1, page2, page3));

        System.out.println("page1: " + page1);
        System.out.println("page2: " + page2);
        System.out.println("page3: " + page3);

        // show wizard and wait for response
        wizard.showAndWait().ifPresent(result -> {
            if (result == ButtonType.FINISH) {
                System.out.println("Wizard finished, settings: " + wizard.getSettings());
            }
        });


    }




    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            initWizard();
        } catch (SQLException | IOException throwables) {
            throwables.printStackTrace();
        }

    }


    private TextField createTextField(String id) {
        TextField textField = new TextField();
        textField.setId(id);
        GridPane.setHgrow(textField, Priority.ALWAYS);
        return textField;
    }

//    private ComboBox createComboBox(String id) {
//        ComboBox comboBox = new ComboBox();
//        comboBox.setId(id);
//        GridPane.setHgrow(comboBox, Priority.ALWAYS);
//        return comboBox;
//    }

    private CheckBox createCheckBox(String id, String name) {
        CheckBox checkBox = new CheckBox(name);
        checkBox.setId(id);
        checkBox.setPadding(new Insets(10,0,10,0));
        GridPane.setHgrow(checkBox, Priority.ALWAYS);
        return checkBox;
    }

    public static <T> Predicate<T> distinctByKey(
            Function<? super T, ?> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

}
