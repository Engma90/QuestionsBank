package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import static org.fxmisc.richtext.model.TwoDimensional.Bias.*;
import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.GenericStyledArea;
import org.fxmisc.richtext.StyledTextArea;
import org.fxmisc.richtext.TextExt;
import org.fxmisc.richtext.model.Codec;
import org.fxmisc.richtext.model.Paragraph;
import org.fxmisc.richtext.model.ReadOnlyStyledDocument;
import org.fxmisc.richtext.model.SegmentOps;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyledDocument;
import org.fxmisc.richtext.model.StyledSegment;
import org.fxmisc.richtext.model.TextOps;
import org.reactfx.SuspendableNo;
import org.reactfx.util.Either;
import org.reactfx.util.Tuple2;


import java.net.URL;
import java.util.ResourceBundle;

public class AddQuestionAnswerRowController implements Initializable {
    public Label label;
    public MyHtmlEditor txt_answer;
    public CheckBox checkbox_right_answer;
    public Button remove_answer;
    public AddQuestionController addQuestionController;
    FXMLLoader loader;
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void onRemoveClicked(ActionEvent e){
        addQuestionController.remove_row(this);
    }
}
