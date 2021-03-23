package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.control.cell.PropertyValueFactory;
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
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static sample.Util.*;
import static sample.Util.getListOfWeek;

public class NewTimetableFormController implements Initializable {

    private List<List<ScheduleModel>> data;
    public void setData(List<List<ScheduleModel>> val){
        data = val;
    }
    public List<List<ScheduleModel>> getData(){
        return data;
    }

    private LocalDateTime selectedLocalDate;

    public void setSelectedLocalDate(LocalDateTime val){
        selectedLocalDate = val;
    }
    public LocalDateTime getSelectedLocalDate(){
        return selectedLocalDate;
    }


    TableView<ScoreModel> tableView = createTableView("tableView");
    TableColumn<ScoreModel, Integer> numericalOrderColumn  = createTableColumn("no", "NO");
    TableColumn<ScoreModel, Double> scoreColumn = createTableColumn("score", "Score");
    TableColumn<ScoreModel, String> timetableColumn = createTableColumn("timetable", "Timetable");
    BorderPane borderPane = new BorderPane();


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

//        Agenda agenda = (Agenda) rootPane.getCenter();
        Agenda agenda = (Agenda) rootPane.lookup("#myAgenda");


        List<String> WeekDateList = getListOfWeek(getSelectedLocalDate());
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
                List<String> selectedSubject = new ArrayList<>();
                for (var item: vbox.getChildren()){
                    CheckBox checkboxItem = (CheckBox) item;
                    if (checkboxItem.isSelected()){
                        selectedSubject.add(checkboxItem.getText());
                    }
                }

                //schedule after selected subjects
                List<ScheduleModel> selectedSchedules = new ArrayList<>();
                for (var item: schedules){
                    if (selectedSubject.contains(item.getSubjectId().getName())){
                        selectedSchedules.add(item);
                    }
                }

                if (selectedSubject == null){
                    setContentText("the subject list is empty. Please select a subject to create a timetable\n ");
                    return;
                }else {
                    setContentText("\n");
                }

                List<List<ScheduleModel>> data = arrangeSchedule(selectedSchedules);
                setData(data);

            }
        };
        page2.setHeaderText("Please click on the next button to create the timetable!");

        // --- page 3
        final WizardPane page3 = new WizardPane() {
            @Override
            public void onEnteringPage(Wizard wizard) {
                List<List<ScheduleModel>> data = getData();
                List<ScoreModel> scoreModels = new ArrayList<>();
                for(var item: data){
                    var model = new ScoreModel(item);
                    scoreModels.add(model);
                }


                Collections.sort(scoreModels, new Comparator<ScoreModel>() {
                        @Override
                            public int compare(ScoreModel o1, ScoreModel o2) {
                                return Double.compare( o2.getScore(), o1.getScore());
                            }
                        }
                    );

                for(var item: scoreModels){
                   System.out.println("item.getScore(): " + item.getScore() + "  item "+ item.getSchedules());
                }
                ObservableList<ScoreModel> scoreList = FXCollections.observableArrayList(
                        scoreModels
                );




//                numericalOrderColumn.setCellValueFactory(new PropertyValueFactory<ScoreModel, Integer>("id"));
                scoreColumn.setCellValueFactory(new PropertyValueFactory<ScoreModel, Double>( "score"));
                timetableColumn.setCellValueFactory(new PropertyValueFactory<ScoreModel, String>("display"));
                tableView.getColumns().addAll( timetableColumn, scoreColumn );
                tableView.setItems(scoreList);

                Button generateBtn = new Button();
                generateBtn.setId("generateBtn");
                generateBtn.setPadding(
                        new Insets(10,10,10,10)
                );
                generateBtn.setText("Create Timetable");
                generateBtn.setStyle(
                        "-fx-background-color:#2196F3;-fx-text-fill: #FFFFFF"
                );

                borderPane.setCenter(tableView);
                borderPane.setRight(generateBtn);

                generateBtn.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        ScoreModel selectedItem = tableView.getSelectionModel().getSelectedItem();
                        System.out.println("selectedItem");
                        System.out.println(selectedItem);
                        if (selectedItem == null){
                            showAlertError("Not selected yet\n", "you must choose a schedule to create a timetable\n ");
                        }else {
                            try {
                                addTimetables(selectedItem.getSchedules());
                            } catch (SQLException throwables) {
                                throwables.printStackTrace();
                            }
                            showSnackbarSuccess(borderPane, "Timetable has been successfully created");
                        }
                    }
                });

            }
        };

        page3.setContent(borderPane);
        page3.setMinWidth(650);
        page3.setMinHeight(500);







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

    }

    private List<List<ScheduleModel>> arrangeSchedule(List<ScheduleModel> selectedSchedules){
        Map<String, List<ScheduleModel>> selectedSchedulesGroupBy = selectedSchedules.stream().collect(Collectors.groupingBy(w -> w.getSubjectId().getName()));

        int lengthOfList = selectedSchedulesGroupBy.entrySet().size();

        List<List<ScheduleModel>> mySchedulesList = new ArrayList<>();

        List<List<ScheduleModel>> selectedSchedulesList = new ArrayList<>();

        for(var item: selectedSchedulesGroupBy.values()){
            selectedSchedulesList.add(item);
        }

        int loopTotal = 1;
        for (var i: selectedSchedulesGroupBy.entrySet()){
            loopTotal *= i.getValue().size();
        }

        List<Integer> depthIndex = new ArrayList<>();
        for (var i = 0; i< lengthOfList; i++){
            depthIndex.add(0);
        }

        for (var i = 0; i< loopTotal; i++){
            List<ScheduleModel> temporaryList = new ArrayList<>();
            for(int j = 0; j < lengthOfList ; j++){
                var data = selectedSchedulesList.get(j).get(depthIndex.get(j));

                temporaryList.add(data);

                //loop toi thang cuoi cung thi check
                if (j == lengthOfList - 1){

                    int k = lengthOfList - 1;
                    while (true){
                        if (k < 0){
                            break;
                        }

                        if (depthIndex.get(k) == selectedSchedulesList.get(k).size()  ){
                            //nothing do, just minus k below
                            k--;
                        }else if (  (k == lengthOfList - 1) && depthIndex.get(k) + 1 == selectedSchedulesList.get(k).size() ) {
                            k--;
                            while (k != -1 && depthIndex.get(k) + 1 == selectedSchedulesList.get(k).size()){
                                k--;
                            }
                        }
                        else {
                            int newDepthIndex = depthIndex.get(k) + 1;
                            depthIndex.set(k, newDepthIndex);

                            boolean isSet = false;
                            while (true){
                                if (k == -1){
                                    break;
                                }
                                if (depthIndex.get(k) == selectedSchedulesList.get(k).size()){
                                    k--;
                                    isSet = true;
                                }else {
                                    break;
                                }
                            }

                            if (isSet&& k != -1){
                                depthIndex.set(k, depthIndex.get(k) + 1);
                            }

                            //reset
                            for(int m = lengthOfList - 1; m > k && k != -1; m--){
                                depthIndex.set(m, 0);
                            }

                            break;
                        }
                    }

                }
            }
            mySchedulesList.add(temporaryList);

        }

        return mySchedulesList;
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
    private TableView createTableView(String id) {
        TableView tableView = new TableView();
        tableView.setId(id);
        return tableView;
    }
    private TableColumn createTableColumn(String id, String text) {
        TableColumn tableColumn = new TableColumn();
        tableColumn.setId(id);
        tableColumn.setText(text);
        return tableColumn;
    }

    public static <T> Predicate<T> distinctByKey(
            Function<? super T, ?> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

}
