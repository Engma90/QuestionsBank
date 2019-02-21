package controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;

import javafx.scene.layout.VBox;
import javafx.scene.web.HTMLEditor;
import org.docx4j.convert.in.xhtml.XHTMLImporterImpl;
import org.docx4j.model.structure.PageSizePaper;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;


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


        radio_mcq.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> obs, Boolean wasPreviouslySelected, Boolean isNowSelected) {
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
            }
        });
        radio_mcq.setSelected(true);
    }

    public void onSave() {
        String Q = html_editor.getHtmlText();
        try {
            Save(Q);
            Save_to_file(Q);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Docx4JException e) {
            e.printStackTrace();
        }
    }

    public void Save_to_file(String s) throws IOException {

        FileOutputStream outputStream = new FileOutputStream("test_file.html");
        byte[] strToBytes = s.getBytes();
        outputStream.write(strToBytes);
        outputStream.close();
    }
    public static void Save(String s) throws Docx4JException {

        // To docx, with content controls
        WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.createPackage(PageSizePaper.A4,false);

        XHTMLImporterImpl XHTMLImporter = new XHTMLImporterImpl(wordMLPackage);

        wordMLPackage.getMainDocumentPart().getContent().addAll(
                XHTMLImporter.convert( s, null) );

        wordMLPackage.save(new java.io.File("D://sample.docx"));
    }

}


