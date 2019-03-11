package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Window;
import models.*;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class GenerateExamController implements Initializable {
    public NumberField number_of_models;
    public VBox same_or_different;//, shuffle;
    public VBox content;
    public CheckBox shuffle_answers;
    public RadioButton radio_same, radio_different;//, radio_shuffle_questions;
    List<GenerateExamChapterRowController> generateExamChapterRowControllerList;
    List<ChapterModel> chapterModelList;


    public TextField college_text, exam_duration, department_text, exam_date,
            exam_total_marks, exam_name_text;
    public TextArea note_text;
    public ComboBox exam_type;

    public CourseModel courseModel = new CourseModel();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        number_of_models.setText("1");
        generateExamChapterRowControllerList = new ArrayList<>();
        final ToggleGroup same_or_different_group = new ToggleGroup();
        //final ToggleGroup shuffle_group = new ToggleGroup();
        radio_same.setToggleGroup(same_or_different_group);
        radio_different.setToggleGroup(same_or_different_group);

        //radio_shuffle_questions.setToggleGroup(shuffle_group);


//        shuffle.setVisible(false);
//        shuffle.setManaged(false);
        same_or_different.setVisible(false);
        same_or_different.setManaged(false);

        number_of_models.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.isEmpty()){
                number_of_models.setText("1");
                newValue ="1";
            }
            if (Integer.parseInt(newValue) > 1) {
                same_or_different.setVisible(true);
                same_or_different.setManaged(true);
//                shuffle.setVisible(true);
//                shuffle.setManaged(true);
                radio_same.setSelected(true);
            } else {
                radio_same.setSelected(true);
       //         shuffle.setVisible(false);
      //          shuffle.setManaged(false);
                same_or_different.setVisible(false);
                same_or_different.setManaged(false);
            }

        });

        radio_same.setOnAction(e -> {
            if (radio_same.isSelected()) {
        //        shuffle.setVisible(true);
        //        shuffle.setManaged(true);

            }
        });
        radio_different.setOnAction(e -> {
            if (radio_different.isSelected()) {
      //          shuffle.setVisible(false);
      //          shuffle.setManaged(false);

            }
        });
        radio_same.setSelected(true);

    }

    public void initUI(){
        chapterModelList = ChaptersListHandler.getInstance().getChaptersList(courseModel);
        for (ChapterModel c : chapterModelList) {
            System.out.println("----------------------1");
            List<QuestionModel> questionModelList = QuestionsTableController.questionsTableHandler.getQuestionList(c.id);
            System.out.println("----------------------2");
            List<String> l = new ArrayList<String>();
            for (QuestionModel q : questionModelList) {
                if (!l.contains(q.getQuestion_diff())) {
                    l.add(q.getQuestion_diff());
                }
            }

            addRow(c.id, c.name,c.number, l);
        }
    }

    public void onGenerateClicked(ActionEvent e) {
        //if(validate()) {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            Node source = (Node) e.getSource();
            Window stage = source.getScene().getWindow();
            File selectedDirectory = directoryChooser.showDialog(stage);

            if (selectedDirectory != null) {
                System.out.println(selectedDirectory.getAbsolutePath());
                List<ExamQuestionModel> exam_questions = getExamQuestionsList();
                ExamModel examModel = new ExamModel();
                examModel.setExamModelModelList(new ArrayList<>());
                if (exam_questions.size() > 0) {
                    if (radio_same.isSelected() || Integer.parseInt(number_of_models.getText()) == 1) {
                        for (int i = 0; i < Integer.parseInt(number_of_models.getText()); i++) {
                            ExamModelModel examModelModel = new ExamModelModel();
                            examModelModel.setExamModelNumber((i+1)+"");
                            List<ExamQuestionModel> temp = new ArrayList<>(exam_questions);
                            Collections.shuffle(temp);
                            examModelModel.setExamQuestionsList(temp);
                            //Collections.shuffle(examModelModel.getExamQuestionsList());
                            System.out.println("---------------------------------------------------------------------");
                            System.out.println(examModelModel.getExamQuestionsList().get(0).getQuestionContent());
                            examModel.getExamModelModelList().add(examModelModel);
                        }

                    } else if (radio_different.isSelected() && Integer.parseInt(number_of_models.getText()) > 1) {
                        for (int i = 0; i < Integer.parseInt(number_of_models.getText()); i++) {
                            exam_questions = getExamQuestionsList();
                            ExamModelModel examModelModel = new ExamModelModel();
                            examModelModel.setExamModelNumber((i+1)+"");
                            List<ExamQuestionModel> temp = new ArrayList<>(exam_questions);
                            Collections.shuffle(temp);
                            examModelModel.setExamQuestionsList(temp);
                            examModel.getExamModelModelList().add(examModelModel);
//                            addExamToDatabase(examModel);
//                            new FileExporter().Export(examModel, selectedDirectory.getAbsolutePath());
                        }

                    }

                    addExamToDatabase(examModel);
                    new FileExporter().Export(examModel, selectedDirectory.getAbsolutePath());
                    new Alert(Alert.AlertType.INFORMATION, "Operation Compleated").show();
                }
            }
        //}
    }
    private  List<ExamQuestionModel> getExamQuestionsList(){
        //List<QuestionModel> rawQuestionsList = new ArrayList<QuestionModel>();
        List<ExamQuestionModel> examQuestionList = new ArrayList<>();
        for (GenerateExamChapterRowController cRow : generateExamChapterRowControllerList) {
            if (cRow.isSelected.isSelected()) {
                for(GenerateExamTopicRowController tRow:cRow.generateExamTopicRowControllerList){
                    if(tRow.isSelected.isSelected()){
                        QuestionsTableHandler questionsTableHandler = new QuestionsTableHandler();
                        List<QuestionModel> temp_list = questionsTableHandler.getQuestionList(new TopicModel(tRow.topic_id,""), tRow.diff_max_level.getText());
                        Collections.shuffle(temp_list);
                        //get only desired number & convert to examQuestion
                        for(int i = 0; i<Integer.parseInt(tRow.topic_number_of_questions.getText()); i++){
                            QuestionModel qmodel= temp_list.get(i);
                            ExamQuestionModel examQuestionModel = new ExamQuestionModel();
                            examQuestionModel.setQuestionContent(qmodel.getQuestion_text());
                            examQuestionModel.setQuestionType(qmodel.getQuestion_type());
                            examQuestionModel.setQuestionDifficulty(qmodel.getQuestion_diff());
                            examQuestionModel.setQuestionWeight(qmodel.getQuestion_weight());
                            examQuestionModel.setQuestionExpectedTime(qmodel.getExpected_time());

                            examQuestionModel.setAnswers(qmodel.getAnswers());
                            examQuestionModel.setCourseName(courseModel.name);
                            examQuestionModel.setCourseCode(courseModel.code);
                            examQuestionModel.setCourseCategory(courseModel.level);
                            examQuestionModel.setChapterName(cRow.chapter_name);
                            examQuestionModel.setChapterNumber(cRow.chapter_number);
                            examQuestionModel.setTopicName(tRow.topic_name);
                            if(shuffle_answers.isSelected()){
                                List<AnswerModel> temp = new ArrayList<>(qmodel.getAnswers());
                                Collections.shuffle(temp);
                                qmodel.setAnswers(temp);
                            }
                            //rawQuestionsList.add(temp_list.get(i));
                            examQuestionList.add(examQuestionModel);
                        }
                    }
                }

            }
            //Collections.shuffle(examQuestionList);
        }
        //Collections.shuffle(examQuestionList);
        return examQuestionList;
    }


    private boolean validate(){
        if(college_text.getText().isEmpty() || exam_name_text.getText().isEmpty()
        || department_text.getText().isEmpty() || note_text.getText().isEmpty()
         || exam_date.getText().isEmpty() || exam_duration.getText().isEmpty()
        || exam_total_marks.getText().isEmpty() || exam_type.getValue().toString().equals("Exam type")
        || number_of_models.getText().isEmpty()){
            new Alert(Alert.AlertType.ERROR, "Please fill all fields").show();
            return false;
        }
        for (GenerateExamChapterRowController row : generateExamChapterRowControllerList) {
            if (row.isSelected.isSelected()) {
//
//                row.easy_list = QuestionsTableController.questionsTableHandler.getQuestionList(row.chapter_id, "30");
//                Collections.shuffle(row.easy_list);
////                if (!validate_row_input(Integer.parseInt(row.Easy.getText()), row.easy_list.size())) {
////                    new Alert(Alert.AlertType.ERROR, "Wrong number of easy questions in chapter:" + row.chapter_name).show();
////                    return false;
////                }
//                row.medium_list = QuestionsTableController.questionsTableHandler.getQuestionList(row.chapter_id, "30");
//                Collections.shuffle(row.medium_list);
////                if (!validate_row_input(Integer.parseInt(row.Medium.getText()), row.medium_list.size())) {
////                    new Alert(Alert.AlertType.ERROR, "Wrong number of medium questions in chapter:" + row.chapter_name).show();
////                    return false;
////                }
//                row.hard_list = QuestionsTableController.questionsTableHandler.getQuestionList(row.chapter_id, "30");
//                Collections.shuffle(row.hard_list);
////                if (!validate_row_input(Integer.parseInt(row.Hard.getText()), row.hard_list.size())) {
////                    new Alert(Alert.AlertType.ERROR, "Wrong number of hard questions in chapter:" + row.chapter_name).show();
////                    return false;
////                }
            }
        }
        return true;
    }

    private boolean validate_row_input(int input_val, int db_return) {
        return input_val <= db_return;
    }

    private void addExamToDatabase(ExamModel examModel) {
        GeneratorHandler generatorHandler = new GeneratorHandler();

        examModel.setDoctor_idDoctor(DashboardController.current_selected_dr_id);
        examModel.setCollege(college_text.getText());
        examModel.setDate(exam_date.getText());
        examModel.setDepartment(department_text.getText());
        examModel.setDuration(exam_duration.getText());
        examModel.setExamCategory(courseModel.level);
        examModel.setCourseCode(courseModel.code);
        examModel.setExamName(exam_name_text.getText());
        examModel.setNote(note_text.getText());
        examModel.setCourseName(courseModel.name);
        examModel.setCourseCategory(courseModel.level);
        examModel.setExamType(exam_type.getValue().toString());
        examModel.setTotalMarks(exam_total_marks.getText());
        examModel.setExamLanguage("en");
        generatorHandler.Add(examModel);
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


    private void addRow(String chapter_id, String chapter_name, String chapter_number, List<String> diff) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/GenerateExamChapterRow.fxml"));
            Parent p = loader.load();

            generateExamChapterRowControllerList.add(((GenerateExamChapterRowController) loader.getController()));

//            GenerateExamChapterRowController generateExamFormRowController = new GenerateExamChapterRowController();
//
//            loader.setController(generateExamFormRowController);

            ((GenerateExamChapterRowController) loader.getController()).initUI(chapter_id, chapter_name, chapter_number, diff);
            content.getChildren().add((p));


        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.toString());
            alert.show();
        }
    }
}
