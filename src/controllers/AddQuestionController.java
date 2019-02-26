package controllers;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import javafx.scene.layout.VBox;
import javafx.scene.web.HTMLEditor;
import models.QuestionTableHandler;


import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AddQuestionController  implements Initializable {
    public Button add_question;
    public HTMLEditor html_editor;
    public RadioButton radio_mcq;
    public RadioButton radio_true_false;
    public RadioButton radio_answer_a;
    public RadioButton radio_answer_b;
    public RadioButton radio_answer_c;
    public RadioButton radio_answer_d;
    public RadioButton radio_answer_true;
    public RadioButton radio_answer_false;
    public VBox mcq_ui_group,true_false_ui_group,container;
    public TextField txt_answer_a, txt_answer_b, txt_answer_c, txt_answer_d;
    public ComboBox combo_q_weight,combo_q_diff;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println(combo_q_diff.getValue());
        final ToggleGroup type_group = new ToggleGroup();
        radio_mcq.setToggleGroup(type_group);
        radio_true_false.setToggleGroup(type_group);
        final ToggleGroup answer_group = new ToggleGroup();
        radio_answer_a.setToggleGroup(answer_group);
        radio_answer_b.setToggleGroup(answer_group);
        radio_answer_c.setToggleGroup(answer_group);
        radio_answer_d.setToggleGroup(answer_group);

        final ToggleGroup true_false_answer_group = new ToggleGroup();
        radio_answer_true.setToggleGroup(true_false_answer_group);
        radio_answer_false.setToggleGroup(true_false_answer_group);


        radio_mcq.selectedProperty().addListener((obs, wasPreviouslySelected, isNowSelected) -> {
            if (isNowSelected) {
                mcq_ui_group.setVisible(true);
                mcq_ui_group.setManaged(true);
                true_false_ui_group.setVisible(false);
                true_false_ui_group.setManaged(false);

            } else {
                mcq_ui_group.setVisible(false);
                mcq_ui_group.setManaged(false);
                true_false_ui_group.setVisible(true);
                true_false_ui_group.setManaged(true);
            }
        });
        radio_mcq.setSelected(true);
    }

    public void onAddClicked(ActionEvent e) {
        String Q = html_editor.getHtmlText();
        String diff = combo_q_diff.getValue().toString();
        String weight = combo_q_weight.getValue().toString();
        QuestionTableHandler questionTableHandler = new QuestionTableHandler();

        if(radio_mcq.isSelected()){
            String A = txt_answer_a.getText();
            String B = txt_answer_a.getText();
            String C = txt_answer_a.getText();
            String D = txt_answer_a.getText();
            if (validate(Q, diff, weight,A, B, C, D)){
                String[] answers = new String[]{A,B,C,D};
                String right_answer = "A";
                if(radio_answer_a.isSelected())
                    right_answer = "A";
                else if(radio_answer_b.isSelected())
                    right_answer = "B";
                else if(radio_answer_c.isSelected())
                    right_answer = "C";
                else if(radio_answer_d.isSelected())
                    right_answer = "D";
                System.out.println("valid");
                questionTableHandler.Add(Q, diff, weight, "MCQ",answers,right_answer);
            }

        }else {
            String A = "True";
            String B = "False";

            if (validate(Q, diff, weight)){
                String[] answers = new String[]{A,B};
                String right_answer = "A";
                if(radio_answer_a.isSelected())
                    right_answer = "A";
                else if(radio_answer_b.isSelected())
                    right_answer = "B";

                questionTableHandler.Add(Q, diff, weight, "True/False",answers,right_answer);
            }
        }




        try {
            Save_to_file(Q);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    private void Save_to_file(String s) throws IOException {

        FileOutputStream outputStream = new FileOutputStream("test_file.html");
        byte[] strToBytes = s.getBytes();
        outputStream.write(strToBytes);
        outputStream.close();
    }

    private boolean validate(String Q, String diff, String weight, String A, String B, String C, String D){
        if(Q.isEmpty() || A.isEmpty() || B.isEmpty() || C.isEmpty() || D.isEmpty()){
            return false;
        }
        else if(diff.equals("Difficulty") || weight.equals("Weight")){
            return false;
        }
        else if(!radio_answer_a.isSelected() && !radio_answer_b.isSelected()
                && !radio_answer_c.isSelected() && !radio_answer_d.isSelected()){
            return false;
        }
        return true;
    }
    private boolean validate(String Q, String diff, String weight){

        if(Q.isEmpty()){
            return false;
        }
        else if(diff.equals("Difficulty") || weight.equals("Weight")){
            return false;
        }
        else if(!radio_answer_true.isSelected() && !radio_answer_false.isSelected()){
            return false;
        }
        return true;
    }

}


