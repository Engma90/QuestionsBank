package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javafx.stage.WindowEvent;
import models.*;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class QuestionILOsController implements Initializable, IWindow, IUpdatable{
    public VBox ILOsRows;
    private Question question;
    private Topic topic;
    private ObservableList<QuestionILOsRowController> ilOsRowControllers;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ilOsRowControllers = FXCollections.observableArrayList();
        Course c = new Course();
        c.setId(TopicListHandler.getInstance().getParentCourseId(topic));
        ObservableList<ILO> tempList = ILOsTableHandler.getInstance().getList(c);
        for(ILO ilo: tempList) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/QuestionILOsRow.fxml"));
                Parent root = loader.load();
                QuestionILOsRowController questionILOsRowController = loader.getController();
                questionILOsRowController.lbl_code.setText(ilo.getCode());
                questionILOsRowController.lbl_desc.setText(ilo.getDescription());
                questionILOsRowController.id = ilo.getId();
                questionILOsRowController.course_id = c.id;
//                System.out.println("ILO = " + ilo.getId());
//                System.out.println("question.getIlos() = " + question.getIlos().size());
                for(ILO ilo1:question.getIlos()){
//                    System.out.println("ILO1 = " + ilo1.getId());
                    if(ilo.getId().equals(ilo1.getId())){
                        questionILOsRowController.chk_selected.setSelected(true);
                        break;
                    }
                }
//                System.out.println("-------------------------------");
                ilOsRowControllers.add(questionILOsRowController);
                ILOsRows.getChildren().add(root);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public QuestionILOsController(Topic topic, Question question){
        this.topic = topic;
        this.question = question;
    }

    public void onSaveClicked(ActionEvent e){
        question.getIlos().clear();
        for( QuestionILOsRowController questionILOsRowController: ilOsRowControllers){
            if(questionILOsRowController.chk_selected.isSelected()){
                ILO ilo = new ILO();
                ilo.setId(questionILOsRowController.id);
//                ilo.setCourseId(questionILOsRowController.course_id);
                ilo.setCode(questionILOsRowController.lbl_code.getText());
                ilo.setDescription(questionILOsRowController.lbl_desc.getText());
                question.getIlos().add(ilo);
            }
        }
        close(e);
    }

    @Override
    public boolean isSaveOnCloseRequired() {
        return false;
    }

    @Override
    public boolean isSaveAndExitClicked() {
        return true;
    }

    @Override
    public Object setWindowData(Stage stage, Object initObject) {
        return null;
    }

    @Override
    public void update() {

    }

    private void close(ActionEvent e) {
        // get a handle to the stage
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        // do what you have to do
        stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));

    }
}
