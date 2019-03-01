package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.VBox;
import models.*;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class GenerateExamController implements Initializable {
    public ComboBox number_of_models;
    public VBox same_or_different,shuffle;
    public VBox content;
    public RadioButton radio_same, radio_different, radio_shuffle_questions;
    List<GenerateExamFormRowController> generateExamFormRowControllerList;
    List<ChapterModel> chapterModelList;
    public List<QuestionModel> exam_questions;
    public TextField college_text, exam_duration,department_text, exam_date,
            exam_total_marks, exam_name_text ;
    public TextArea note_text;
    public ComboBox exam_type;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        generateExamFormRowControllerList =new ArrayList<>();
        final ToggleGroup same_or_different_group = new ToggleGroup();
        final ToggleGroup shuffle_group = new ToggleGroup();
        radio_same.setToggleGroup(same_or_different_group);
        radio_different.setToggleGroup(same_or_different_group);

        radio_shuffle_questions.setToggleGroup(shuffle_group);


        shuffle.setVisible(false);
        shuffle.setManaged(false);
        same_or_different.setVisible(false);
        same_or_different.setManaged(false);

        number_of_models.setOnAction((e) -> {
            if(Integer.parseInt(number_of_models.getSelectionModel().getSelectedItem().toString()) > 1){
                same_or_different.setVisible(true);
                same_or_different.setManaged(true);
                shuffle.setVisible(true);
                shuffle.setManaged(true);
            }else {
                shuffle.setVisible(false);
                shuffle.setManaged(false);
                same_or_different.setVisible(false);
                same_or_different.setManaged(false);
            }

        });

        radio_same.setOnAction(e ->{
            if(radio_same.isSelected()){
                shuffle.setVisible(true);
                shuffle.setManaged(true);

            }
        });
        radio_different.setOnAction(e ->{
            if(radio_different.isSelected()) {
                shuffle.setVisible(false);
                shuffle.setManaged(false);

            }
        });
        radio_same.setSelected(true);



        chapterModelList = DashboardController.chaptersListHandler.getChaptersList();
        for (ChapterModel c:chapterModelList){
            System.out.println("----------------------1");
            List<QuestionModel> questionModelList = QuestionsController.questionTableHandler.getQuestionList(c.id);
            System.out.println("----------------------2");
            List<String> l =new ArrayList<String>();
            for (QuestionModel q:questionModelList){
                if(!l.contains(q.getQuestion_diff())){
                    l.add(q.getQuestion_diff());
                }
            }

        addRow(c.id, c.name, l);
        }


    }

    public void onGenerateClicked(ActionEvent e) {
        exam_questions = new ArrayList<QuestionModel>();
        ExamModel examModel = new ExamModel();
        for (GenerateExamFormRowController row : generateExamFormRowControllerList) {
            if (row.isSelected.isSelected()) {
                row.easy_list = QuestionsController.questionTableHandler.getQuestionList(row.chapter_id, "Easy");
                Collections.shuffle(row.easy_list);
                if (!validate_row_input(Integer.parseInt(row.Easy.getText()), row.easy_list.size())) {
                    System.out.println("");
                    //row.Easy.setBorder();
                    new Alert(Alert.AlertType.ERROR, "Wrong number of questions in chapter:" + row.chapter_name).show();

                }else {
                    for(int i=0; i<Integer.parseInt(row.Easy.getText());i++){
                        exam_questions.add(row.easy_list.get(i));
                    }
                }
                row.medium_list = QuestionsController.questionTableHandler.getQuestionList(row.chapter_id, "Medium");
                Collections.shuffle(row.medium_list);
                if (!validate_row_input(Integer.parseInt(row.Medium.getText()), row.medium_list.size())) {
                    new Alert(Alert.AlertType.ERROR, "Wrong number of questions in chapter:" + row.chapter_name).show();

                }else {
                    for(int i=0; i<Integer.parseInt(row.Medium.getText());i++){
                        exam_questions.add(row.medium_list.get(i));
                    }
                }
                row.hard_list = QuestionsController.questionTableHandler.getQuestionList(row.chapter_id, "Hard");
                Collections.shuffle(row.hard_list);
                if (!validate_row_input(Integer.parseInt(row.Hard.getText()), row.hard_list.size())) {
                    new Alert(Alert.AlertType.ERROR, "Wrong number of questions in chapter:" + row.chapter_name).show();

                }else {
                    for(int i=0; i<Integer.parseInt(row.Hard.getText());i++){
                        exam_questions.add(row.hard_list.get(i));
                    }
                }
            }
        }
        if(exam_questions.size()>0) {
            Collections.shuffle(exam_questions);
            examModel.College = college_text.getText();
            examModel.Date = exam_date.getText();
            examModel.Department = department_text.getText();
            examModel.Duration = exam_duration.getText();
            examModel.ExamCategory = DashboardController.degree_category;
            examModel.Course_idCourse = DashboardController.current_selected_course_id;
            examModel.ExamName = exam_name_text.getText();
            examModel.Note = note_text.getText();
            examModel.CourseName = DashboardController.coursesListHandler.coursesList.get
                    (DashboardController.current_selected_course_index).
                    name.toString();
            examModel.ExamModel = "A";
            examModel.ExamType = exam_type.getValue().toString();
            examModel.TotalMarks = exam_total_marks.getText();
            GeneratorHandler generatorHandler = new GeneratorHandler();
            generatorHandler.Add(examModel);
//            if(Integer.parseInt(number_of_models.getValue().toString())>1){
//                for (int i = 0; i<Integer.parseInt(number_of_models.getValue().toString()); i++){
//                    examModel.ExamModel = ((char) 65 + i) + "";
//                }

 //           }
        }
    }
    public boolean validate_row_input(int input_val, int db_return){
        return input_val <= db_return;

    }




//
//        for(GenerateExamFormRowController row:generateExamFormRowControllerList){
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



    private void addRow(String chapter_id, String chapter_name, List<String> diff){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/GenerateExamFormRow.fxml"));
            Parent p =loader.load();

            generateExamFormRowControllerList.add(((GenerateExamFormRowController)loader.getController()));

//            GenerateExamFormRowController generateExamFormRowController = new GenerateExamFormRowController();
//
//            loader.setController(generateExamFormRowController);

            ((GenerateExamFormRowController)loader.getController()).initUI(chapter_id, chapter_name,diff);
            content.getChildren().add((p));


        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.toString());
            alert.show();
        }
    }
}
