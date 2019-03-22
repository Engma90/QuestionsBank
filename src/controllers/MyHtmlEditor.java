package controllers;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.web.HTMLEditor;

import java.io.File;

public class MyHtmlEditor extends HBox {
    private Button button;
    private HTMLEditor htmlEditor;
    boolean isShown = false;
    public MyHtmlEditor(){
        this.setSpacing(10);
        this.setPrefWidth(1000);

        button = new Button("<");
        htmlEditor = new HTMLEditor();
        htmlEditor.setPrefHeight(50);
        htmlEditor.setPrefWidth(700);
        this.getChildren().add(htmlEditor);
        this.getChildren().add(button);
        hideToolbars(this.htmlEditor);
        button.setOnAction(e ->{
            if(isShown){
                hideToolbars(this.htmlEditor);
                htmlEditor.setPrefHeight(50);
                String imagePath = "C:\\Users\\Mohammad\\IdeaProjects\\QuestionsBank\\QRCode.png";
                File f = new File(imagePath);
                htmlEditor.setHtmlText("<img src=' " + f.toURI() + "'/>");
            }
            else {
                htmlEditor.setPrefHeight(400);
                showToolbars(this.htmlEditor);
            }
            isShown = !isShown;
        });
    }

    public String getText(){
        return this.htmlEditor.getHtmlText();
    }

    public void setText(String text){
        this.htmlEditor.setHtmlText(text);
    }


    private void showToolbars(final HTMLEditor editor)
    {
        editor.setVisible(false);
        Platform.runLater(() -> {
            Node[] nodes = editor.lookupAll(".tool-bar").toArray(new Node[0]);
            for(Node node : nodes)
            {
                node.setVisible(true);
                node.setManaged(true);
            }
            editor.setVisible(true);
        });
    }

    private void hideToolbars(final HTMLEditor editor)
    {
        editor.setVisible(false);
        Platform.runLater(() -> {
            Node[] nodes = editor.lookupAll(".tool-bar").toArray(new Node[0]);
            for(Node node : nodes)
            {
                node.setVisible(false);
                node.setManaged(false);

            }
            editor.setVisible(true);
        });
    }
}
