package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.StringConverter;
import models.*;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class GenerateExamController implements Initializable, IUpdatable, IWindow {
    public NumberField number_of_models;
    public HBox same_or_different;//, shuffle;
    public VBox exam_content;
    public CheckBox shuffle_answers;
    public RadioButton radio_same, radio_different;//, radio_shuffle_questions;
    public NumberField lbl_sum;
    private List<GenerateExamChapterRowController> generateExamChapterRowControllerList;
    private List<Chapter> chapterList;


    public TextField college_text, exam_duration, department_text,
            exam_total_marks, exam_name_text;
    public DatePicker exam_date;
    public TextArea note_text;
    public ComboBox<String> exam_type, format, exam_language, exam_year;

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
        exam_language.getSelectionModel().select("English");
        exam_year.getSelectionModel().select(course.year);
        format.getSelectionModel().selectFirst();

        chapterList = ChaptersListHandler.getInstance().getList(course);
        for (Chapter c : chapterList) {
            System.out.println("----------------------1");
            //List<Question> questionList = QuestionsTableHandler.getInstance().getQuestionList(c.id);
            System.out.println("----------------------2");
            //List<String> l = new ArrayList<String>();
//            for (Question q : questionList) {
//                if (!l.contains(q.getQuestion_diff())) {
//                    l.add(q.getQuestion_diff());
//                }
//            }
            if(QuestionsTableHandler.getInstance().getQuestionList(c).size() > 0)
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

    }
    //Todo: Separate import exam to db from exporting to avoid db redundancy (word and pdf)
    public void onGenerateClicked(ActionEvent e) {
        //if(validate()) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        Node source = (Node) e.getSource();
        Window stage = source.getScene().getWindow();
        File selectedDirectory = directoryChooser.showDialog(stage);

        if (selectedDirectory != null) {
            System.out.println(selectedDirectory.getAbsolutePath());
            List<ExamQuestion> exam_questions = getExamQuestionsList();
            Exam exam = new Exam();
            exam.setExamModelList(new ArrayList<>());
            if (exam_questions.size() > 0) {
                if (radio_same.isSelected() || Integer.parseInt(number_of_models.getText()) == 1) {
                    for (int i = 0; i < Integer.parseInt(number_of_models.getText()); i++) {
                        ExamModel examModel = new ExamModel();
                        examModel.setExamModelNumber((i + 1) + "");
                        List<ExamQuestion> temp = new ArrayList<>(exam_questions);
                        Collections.shuffle(temp);
                        examModel.setExamQuestionsList(temp);
                        //Collections.shuffle(examModel.getExamQuestionsList());
                        System.out.println("---------------------------------------------------------------------");
                        System.out.println(examModel.getExamQuestionsList().get(0).getQuestionContent());
                        exam.getExamModelList().add(examModel);
                    }

                } else if (radio_different.isSelected() && Integer.parseInt(number_of_models.getText()) > 1) {
                    for (int i = 0; i < Integer.parseInt(number_of_models.getText()); i++) {
                        exam_questions = getExamQuestionsList();
                        ExamModel examModel = new ExamModel();
                        examModel.setExamModelNumber((i + 1) + "");
                        List<ExamQuestion> temp = new ArrayList<>(exam_questions);
                        Collections.shuffle(temp);
                        examModel.setExamQuestionsList(temp);
                        exam.getExamModelList().add(examModel);
                    }

                }

                addExamToDatabase(exam);
                new FileExporter().getExporter(FileExporter.LIBRE_OFFICE)
                        .exportExam(exam, selectedDirectory.getAbsolutePath(), format.getValue());
                new Alert(Alert.AlertType.INFORMATION, "Operation Compleated").show();
            }
        }
        //}
    }

    private List<ExamQuestion> getExamQuestionsList() {
        List<ExamQuestion> examQuestionList = new ArrayList<>();
        for (GenerateExamChapterRowController cRow : generateExamChapterRowControllerList) {
            if (cRow.isSelected.isSelected()) {
                for (GenerateExamTopicRowController tRow : cRow.generateExamTopicRowControllerList) {
                    if (tRow.isSelected.isSelected()) {
                        List<Question> temp_list = QuestionsTableHandler.getInstance().getQuestionList(new Topic(tRow.topic_id, ""), tRow.diff_max_level.getText());

                        Collections.shuffle(temp_list);
                        //get only desired number & convert to examQuestion
                        for (int i = 0; i < Integer.parseInt(tRow.topic_number_of_questions.getText()); i++) {

                            Question qmodel = temp_list.get(i);

                            ExamQuestion examQuestion = new ExamQuestion();
                            examQuestion.setQuestionContent(qmodel.getQuestion_text());
                            examQuestion.setQuestionType(qmodel.getQuestion_type());
                            examQuestion.setQuestionDifficulty(qmodel.getQuestion_diff());
                            examQuestion.setQuestionWeight(qmodel.getQuestion_weight());
                            examQuestion.setQuestionExpectedTime(qmodel.getExpected_time());

                            examQuestion.setAnswers(qmodel.getAnswers());
//                            examQuestion.setCourseName(course.name);
//                            examQuestion.setCourseCode(course.code);
//                            examQuestion.setCourseLevel(course.level);
//                            examQuestion.setChapterName(cRow.chapter_name);
//                            examQuestion.setChapterNumber(cRow.chapter_number);
//                            examQuestion.setTopicName(tRow.topic_name);
                            if (shuffle_answers.isSelected()) {
                                List<Answer> temp = new ArrayList<>(qmodel.getAnswers());
                                Collections.shuffle(temp);
                                qmodel.setAnswers(temp);
                            }
                            examQuestionList.add(examQuestion);
                        }
                    }
                }

            }
        }
        return examQuestionList;
    }


    private boolean validate() {
        if (college_text.getText().isEmpty() || exam_name_text.getText().isEmpty()
                || department_text.getText().isEmpty() || note_text.getText().isEmpty()
                || exam_duration.getText().isEmpty()
                || exam_total_marks.getText().isEmpty() || exam_type.getValue().toString().equals("Exam type")
                || number_of_models.getText().isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Please fill all fields").show();
            return false;
        }
        for (GenerateExamChapterRowController row : generateExamChapterRowControllerList) {
            if (row.isSelected.isSelected()) {

            }
        }
        return true;
    }

    private void addExamToDatabase(Exam exam) {
        GeneratorHandler generatorHandler = new GeneratorHandler();

        exam.setDoctor_idDoctor(DashboardController.doctor.getId());
        exam.setCollege(college_text.getText());
        exam.setDate(exam_date.getValue().getDayOfWeek().toString().substring(0, 3) + " " + exam_date.getConverter().toString(exam_date.getValue()));
        exam.setDepartment(department_text.getText());
        exam.setDuration(exam_duration.getText());
        exam.setCourseCode(course.code);
        exam.setExamName(exam_name_text.getText());
        exam.setNote(note_text.getText());
        exam.setCourseName(course.name);
        exam.setCourseLevel(course.level);
        exam.setExamType(exam_type.getValue().toString());
        exam.setTotalMarks(exam_total_marks.getText());
        exam.setYear(exam_year.getValue().toString());
        exam.setExamLanguage(exam_language.getValue().toString());
        generatorHandler.Add(exam);
    }


//
//        for(GenerateExamChapterRowController row:generateExamChapterRowControllerList){
//            int questions_chapter = row.getNumber_of_questions();
//            int diff_chapter_len = row.getDiff().size();
//            for (int i = 0; i< diff_chapter_len; i++){
//
//
//            }
//            int diff_= questions_chapter / diff_chapter_len;
//            int mediumQuestion_chapter = ((questions_chapter - easyQuestion_chapter)/ diff_chapter_len);
//            int hardQuestion_chapter = questions_chapter - (mediumQuestion_chapter + easyQuestion_chapter);
//        }


    private void addRow(Chapter chapter) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/GenerateExamChapterRow.fxml"));
            Parent p = loader.load();

            generateExamChapterRowControllerList.add(((GenerateExamChapterRowController) loader.getController()));

//            GenerateExamChapterRowController generateExamFormRowController = new GenerateExamChapterRowController();
//
//            loader.setController(generateExamFormRowController);

            ((GenerateExamChapterRowController) loader.getController()).initUI(chapter);
            exam_content.getChildren().add((p));


        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.toString());
            alert.show();
        }
    }

    @Override
    public void update() {
        //Todo: Check for available number of question below selected level on change
        int sum = 0;
        for (GenerateExamChapterRowController gecrc : generateExamChapterRowControllerList) {
            for (GenerateExamTopicRowController getrc : gecrc.generateExamTopicRowControllerList) {
                Topic topic= new Topic();
                topic.id = getrc.topic_id;

                getrc.topic_number_of_questions.setMax(
                        QuestionsTableHandler.getInstance().getQuestionList(topic,getrc.diff_max_level.getText()).size()
                );
                sum += Integer.parseInt(getrc.topic_number_of_questions.getText());
            }
        }
        lbl_sum.setText(sum+"");
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
        stage.setMinHeight(700);
        stage.setMinWidth(1000);
        initUI();
        return this;
    }
}
