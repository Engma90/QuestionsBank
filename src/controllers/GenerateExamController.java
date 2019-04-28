package controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.NodeOrientation;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.StringConverter;
import models.*;
import controllers.Vars.*;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

public class GenerateExamController implements Initializable, IUpdatable, IWindow {
    public NumberField number_of_models;
    public HBox same_or_different;//, shuffle;
    public VBox exam_content;
    public CheckBox shuffle_answers;
    public RadioButton radio_same, radio_different;//, radio_shuffle_questions;
    public NumberField lbl_sum;
    private List<GenerateExamChapterRowController> generateExamChapterRowControllerList;
    private List<Chapter> chapterList;
    public Button generate;
    public ProgressBar progress_bar;


    public TextField college_text, exam_duration, department_text,
            exam_total_marks, exam_name_text;
    public DatePicker exam_date;
    public TextArea note_text;
    public ComboBox<String> exam_type, combo_format, exam_language, exam_year;

    public Course course = new Course();


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        exam_type.getSelectionModel().selectFirst();
        number_of_models.setText("1");
        number_of_models.setMin(1);
        number_of_models.setMax(10000);
        generateExamChapterRowControllerList = new ArrayList<>();
        final ToggleGroup same_or_different_group = new ToggleGroup();
        radio_same.setToggleGroup(same_or_different_group);
        radio_different.setToggleGroup(same_or_different_group);
        same_or_different.setVisible(false);
        same_or_different.setManaged(false);

        number_of_models.textProperty().addListener((observable, oldValue, newValue) -> {
            if (Integer.parseInt(newValue) > 1) {
                same_or_different.setVisible(true);
                same_or_different.setManaged(true);
                radio_same.setSelected(true);
            } else {
                radio_same.setSelected(true);
                same_or_different.setVisible(false);
                same_or_different.setManaged(false);
            }

        });

        radio_same.setSelected(true);

    }

    public void initUI() {
        generate.setDisable(true);
        progress_bar.setVisible(false);


        exam_language.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                int selection = exam_year.getSelectionModel().getSelectedIndex();
                if (newValue.equals(Languages.ENGLISH)) {
                    exam_name_text.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
                    college_text.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
                    department_text.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
                    note_text.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
                    exam_duration.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
                    exam_total_marks.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
                    //Todo: fix this on change language
                    ObservableList<String> years = FXCollections.observableArrayList();
                    years.add("Prep Year");
                    years.add("1st Year");
                    years.add("2nd Year");
                    years.add("3rd Year");
                    years.add("4th Year");
                    years.add("5th Year");
                    years.add("6th Year");
                    exam_year.setItems(years);
                    exam_year.getSelectionModel().select(selection);
                } else {
                    exam_name_text.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
                    college_text.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
                    department_text.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
                    note_text.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
                    exam_duration.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
                    exam_total_marks.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
                    //Todo: implement in a better way (bundles)
                    ObservableList<String> years = FXCollections.observableArrayList();
                    years.add("الفرقة الاعدادية");
                    years.add("الفرقة الاولى");
                    years.add("الفرقة الثانية");
                    years.add("الفرقة الثالثة");
                    years.add("الفرقة الرابعة");
                    years.add("الفرقة الخامسة");
                    years.add("الفرقة السادسة");
                    exam_year.setItems(years);
                }
                exam_year.getSelectionModel().select(selection);
            }
        });
        exam_year.getSelectionModel().select(course.year);
        //exam_language.getSelectionModel().select(DashboardController.doctor.getPreferredExamLayout());


        combo_format.getSelectionModel().selectFirst();

        chapterList = ChaptersListHandler.getInstance().getList(course);
        for (Chapter c : chapterList) {
            //System.out.println("----------------------1");
            //List<Question> questionList = QuestionsTableHandler.getInstance().getQuestionList(c.id);
            //System.out.println("----------------------2");
            //List<String> l = new ArrayList<String>();
//            for (Question q : questionList) {
//                if (!l.contains(q.getQuestion_diff())) {
//                    l.add(q.getQuestion_diff());
//                }
//            }
            if (QuestionsTableHandler.getInstance().getQuestionList(c).size() > 0)
                addRow(c);
        }
        for (GenerateExamChapterRowController gecrc : generateExamChapterRowControllerList) {
            for (GenerateExamTopicRowController getrc : gecrc.generateExamTopicRowControllerList) {
                getrc.topic_number_of_questions.setParentController(this);
                getrc.diff_max_level.setParentController(this);
            }
        }

        StringConverter<LocalDate> converter = new StringConverter<LocalDate>() {
            DateTimeFormatter dateFormatter =
                    DateTimeFormatter.ofPattern("dd/MM/yyyy");

            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }
        };
        exam_date.setConverter(converter);


        exam_name_text.setText(course.name + " - " + course.code);
        college_text.setText(DashboardController.doctor.getCollege());
        department_text.setText(DashboardController.doctor.getDepartment());
        exam_date.setValue(LocalDate.now());
        exam_date.setEditable(false);

    }

    /* Todo: - Separate import exam to db from exporting to avoid db redundancy (word and pdf)
             - add Progressbar
     */
    public void onGenerateClicked(final ActionEvent e) {

        if (validate()) {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            Node source = (Node) e.getSource();
            Window stage = source.getScene().getWindow();
            final File selectedDirectory = directoryChooser.showDialog(stage);

            if (selectedDirectory != null) {
                generate.setDisable(true);
                progress_bar.setVisible(true);
                Task<Void> task = new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        FileExporterFactory fe = new FileExporterFactory();
                        IFileExporter fileExporter = fe.createExporter(FileExporterFactory.DEFAULT);
                        if (null == fileExporter) {
                            throw new Exception(fe.getError());
                        }
                        if (generateExam(fileExporter, selectedDirectory)) {
                            return null;
                        } else {
                            throw new Exception("GenerateExam error");
                        }
                    }
                };

                task.setOnFailed(ev -> {
                    generate.setDisable(false);
                    progress_bar.setVisible(false);
                    new Alert(Alert.AlertType.ERROR, "Operation Failed, " + ev.getSource().getException().getMessage()).show();
                });
                task.setOnSucceeded(ev -> {
                    generate.setDisable(false);
                    progress_bar.setVisible(false);
                    new Alert(Alert.AlertType.INFORMATION, "Operation Completed").show();
                });
                new Thread(task).start();


            }
        }
    }


    private boolean generateExam(IFileExporter fileExporter, File selectedDirectory) {
        try {
            List<Question> exam_questions = getQuestionsList();
            Exam exam = new Exam();
            exam.setExamModelList(new ArrayList<>());
            if (exam_questions.size() > 0) {
                if (radio_same.isSelected() || Integer.parseInt(number_of_models.getText()) == 1) {
                    for (int i = 0; i < Integer.parseInt(number_of_models.getText()); i++) {
                        ExamModel examModel = new ExamModel();
                        examModel.setExamModelNumber((i + 1) + "");
                        List<Question> temp = new ArrayList<>(exam_questions);
                        Collections.shuffle(temp);
                        examModel.setQuestionsList(temp);
                        exam.getExamModelList().add(examModel);
                    }

                } else if (radio_different.isSelected() && Integer.parseInt(number_of_models.getText()) > 1) {
                    for (int i = 0; i < Integer.parseInt(number_of_models.getText()); i++) {
                        exam_questions = getQuestionsList();
                        ExamModel examModel = new ExamModel();
                        examModel.setExamModelNumber((i + 1) + "");
                        List<Question> temp = new ArrayList<>(exam_questions);
                        Collections.shuffle(temp);
                        examModel.setQuestionsList(temp);
                        exam.getExamModelList().add(examModel);
                    }

                }

                addExamToDatabase(exam);
                fileExporter.exportExam(exam, selectedDirectory.getAbsolutePath(), combo_format.getValue());
                return true;
            } else {
                return false;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    private List<Question> getQuestionsList() {
        List<Question> examQuestionList = new ArrayList<>();
        for (GenerateExamChapterRowController cRow : generateExamChapterRowControllerList) {
            if (cRow.isSelected.isSelected()) {
                for (GenerateExamTopicRowController tRow : cRow.generateExamTopicRowControllerList) {
                    if (tRow.isSelected.isSelected()) {
                        List<Question> temp_list = QuestionsTableHandler.getInstance().getQuestionList(new Topic(tRow.topic.id, ""), tRow.diff_max_level.getText());

                        Collections.shuffle(temp_list);
                        //get only desired number & convert to examQuestion
                        for (int i = 0; i < Integer.parseInt(tRow.topic_number_of_questions.getText()); i++) {

                            Question qmodel = temp_list.get(i);

                            Question examQuestion = new Question();

                            examQuestion.setQuestion_type(qmodel.getQuestion_type());
                            examQuestion.setQuestion_diff(qmodel.getQuestion_diff());
                            examQuestion.setQuestion_weight(qmodel.getQuestion_weight());
                            examQuestion.setExpected_time(qmodel.getExpected_time());

                            //Todo: Check EMQ shuffled
                            if (shuffle_answers.isSelected()) { //&& !(qmodel.getQuestion_type().equals(QuestionType.EXTENDED_MATCH))
                                List<Answer> temp = new ArrayList<>(qmodel.getAnswers());
                                Collections.shuffle(temp);
                                qmodel.setAnswers(temp);
                            }
                            examQuestion.setAnswers(qmodel.getAnswers());

                            List<QuestionContent> questionContentTempList = new ArrayList<>();
                            for (QuestionContent questionContent : qmodel.getContents()) {

                                questionContentTempList.add(questionContent);
                                i++;
                                if (i == Integer.parseInt(tRow.topic_number_of_questions.getText()) - 1)
                                    break;
                            }
                            examQuestion.setContents(questionContentTempList);
                            examQuestionList.add(examQuestion);
                        }
                    }
                }

            }
        }
        return examQuestionList;
    }


    private boolean validate() {
        if (college_text.getText().isEmpty()
                || exam_name_text.getText().isEmpty()
                || department_text.getText().isEmpty()
                || exam_duration.getText().isEmpty()
                || exam_year.getValue().isEmpty()
                || exam_total_marks.getText().isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Please fill all fields").show();
            return false;
        }
        return true;
    }

    private void addExamToDatabase(Exam exam) {
        GeneratorHandler generatorHandler = new GeneratorHandler();

        exam.setDoctor_idDoctor(DashboardController.doctor.getId());
        exam.setCollege(college_text.getText());
        exam.setDate(exam_date.getConverter().toString(exam_date.getValue()));
        exam.setDepartment(department_text.getText());
        exam.setDuration(exam_duration.getText());
        exam.setCourseCode(course.code);
        exam.setExamName(exam_name_text.getText());
        exam.setNote(note_text.getText());
        exam.setCourseName(course.name);
        exam.setCourseLevel(course.level);
        exam.setExamType(exam_type.getValue());
        exam.setTotalMarks(exam_total_marks.getText());
        exam.setYear(exam_year.getValue());
        exam.setExamLanguage(exam_language.getValue());
        generatorHandler.Add(exam);
    }


    private void addRow(Chapter chapter) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/GenerateExamChapterRow.fxml"));
            Parent p = loader.load();

            generateExamChapterRowControllerList.add(loader.getController());
            ((GenerateExamChapterRowController) loader.getController()).initUI(chapter);
            ((GenerateExamChapterRowController) loader.getController()).parent = this;
            exam_content.getChildren().add((p));


        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.toString());
            alert.show();
        }
    }

    @Override
    public void update() {
        int sum = 0;
        for (GenerateExamChapterRowController gecrc : generateExamChapterRowControllerList) {
            if (gecrc.isSelected.isSelected()) {
                for (GenerateExamTopicRowController getrc : gecrc.generateExamTopicRowControllerList) {
                    if (getrc.isSelected.isSelected()) {
                        int questions_less_than_level = 0;
                        for (Question q : getrc.topic.AllQuestionsList) {
                            if (Integer.parseInt(q.getQuestion_diff()) <= Integer.parseInt(getrc.diff_max_level.getText())) {
                                questions_less_than_level++;
                            }
                        }
                        getrc.topic_number_of_questions.setMax(
                                questions_less_than_level
                        );
                        sum += Integer.parseInt(getrc.topic_number_of_questions.getText());
                    }
                }
            }
        }
        lbl_sum.setText(sum + "");
        if (sum > 0) {
            generate.setDisable(false);
        } else {
            generate.setDisable(true);
        }
    }

    @Override
    public boolean isSaveOnCloseRequired() {
        return false;
    }

    @Override
    public boolean isSaveAndExitClicked() {
        return false;
    }

    @Override
    public Object setWindowData(Stage stage, Object initObject) {
        course = (Course) initObject;
        stage.setTitle("Generate Exam");
        stage.setMinHeight(600);
        stage.setMinWidth(1000);
        stage.setWidth(stage.getMinWidth());
        stage.setHeight(stage.getMinHeight());
        initUI();
        return this;
    }
}
