package controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

public class AddQuestionContentRowController implements Initializable {
    public RadioButton select;
    public MyHtmlEditor content;
    public Button remove_content;
    public AddQuestionController.ContentHepler parent;
    public HBox rowContainer;
    //public List<Answer> rightAnswersList;
    FXMLLoader loader;
    private final AddQuestionContentRowController thisReference = this;
    //private QuestionContent contentModel;


//    public QuestionContent getContentModel() {
//        return contentModel;
//    }
//
//    public void setContentModel(QuestionContent contentModel) {
//        this.contentModel = contentModel;
//    }



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //rightAnswersList = new ArrayList<>();
        //contentModel = new QuestionContent();
        select.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                System.out.println(newValue);
                parent.updateContentRowAnswersUI(thisReference, newValue);
            }
        });

        content.lookup(".web-view").setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                select.setSelected(true);
            }
        });
    }

    public void onRemoveClicked(ActionEvent e){
        if (Dialog.CreateDialog("Confirmation","Are you sure?" , "Yes", "No")) {
            parent.remove(this);
        }
    }

    public void setVisible(boolean val){
            rowContainer.setVisible(val);
    }
    public void setManaged(boolean val){
        rowContainer.setManaged(val);
    }



}
