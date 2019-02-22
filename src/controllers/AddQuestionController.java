package controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;

import javafx.scene.layout.VBox;
import javafx.scene.web.HTMLEditor;


import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AddQuestionController  implements Initializable {
    public Button saveBtn;
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
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

    public void onSave() {
        String Q = html_editor.getHtmlText();

        try {
            Save_to_file(Q);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void Save_to_file(String s) throws IOException {

        FileOutputStream outputStream = new FileOutputStream("test_file.html");
        byte[] strToBytes = s.getBytes();
        outputStream.write(strToBytes);
        outputStream.close();
    }


}


