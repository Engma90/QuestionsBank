package controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableListBase;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.web.HTMLEditor;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MyHtmlEditor extends HBox {
    private Button button;
    private HTMLEditor htmlEditor;
    private boolean isShown = false;

    //public static final String TOP_TOOLBAR = ".top-toolbar";
    public static final String BOTTOM_TOOLBAR = ".bottom-toolbar";
    public static final String WEB_VIEW = ".web-view";

    private final WebView mWebView;
    //private final ToolBar mTopToolBar;
    private final ToolBar mBottomToolBar;


    public MyHtmlEditor(){
        this.setSpacing(1);
        //this.setPrefWidth(1000);
        //this.getStylesheets().clear();
//        this.htmlEditor.setSt = FXCollections.observableArrayList();
//        this.htmlEditor.getStylesheets().add("@css/modena/modena.css");


        button = new Button("^");
        htmlEditor = new HTMLEditor();
        htmlEditor.setPrefHeight(50);
        htmlEditor.setPrefWidth(700);
        this.getChildren().add(htmlEditor);
        this.getChildren().add(button);
        hideToolbars(this.htmlEditor);
        button.setOnAction(e ->{
            if(isShown){
                button.setText("^");
                hideToolbars(this.htmlEditor);
                htmlEditor.setPrefHeight(50);
            }
            else {
                button.setText("<");
                htmlEditor.setPrefHeight(250);
                showToolbars(this.htmlEditor);
            }
            isShown = !isShown;
        });


        mWebView = (WebView) this.htmlEditor.lookup(WEB_VIEW);

        HBox.setHgrow(htmlEditor,Priority.ALWAYS);
        //HBox.setHgrow(mWebView,Priority.ALWAYS);
        //mTopToolBar = (ToolBar) this.htmlEditor.lookup(TOP_TOOLBAR);
        mBottomToolBar = (ToolBar) this.htmlEditor.lookup(BOTTOM_TOOLBAR);
//        this.mWebView.setOnDragDetected(new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent event) {
//                Dragboard db = mWebView.startDragAndDrop(TransferMode.ANY);
//                if(db.hasUrl())
//                    new Alert(Alert.AlertType.ERROR, "Imageee").show();
//                //event.consume();
//            }
//        });
//        this.mWebView.setOnDragDone(new EventHandler<DragEvent>() {
//            @Override
//            public void handle(DragEvent event) {
//                event.consume();
//            }
//        });
//        this.mWebView.setOnDragDropped(new EventHandler<DragEvent>() {
//            @Override
//            public void handle(DragEvent event) {
//                /* drag was detected, start a drag-and-drop gesture*/
//                /* allow any transfer mode */
//                Dragboard db = mWebView.startDragAndDrop(TransferMode.MOVE);
//
//                ((Node) event.getSource()).setCursor(Cursor.HAND);
//                /* Put a string on a dragboard */
//                ClipboardContent exam_content = new ClipboardContent();
//                exam_content.putString(htmlEditor.getHtmlText());
//                db.setContent(exam_content);
//
//                event.consume();
//            }
//        });

        createCustomButtons();
        this.htmlEditor.setHtmlText("<html />");
    }

    private void createCustomButtons() {
        //add embed file button
        ImageView graphic = new ImageView(new Image("/views/images/insert-image.png"));
        Button mImportFileButton = new Button("", graphic);
        mImportFileButton.setTooltip(new Tooltip("Import Image"));
        mImportFileButton.setOnAction((event) -> onImportFileButtonAction());

        //add to top toolbar
//        ObservableList<Node> buttons = FXCollections.observableArrayList();
//
//
//        buttons.addAll(mBottomToolBar.getItems());
//        //buttons.add(new Separator(Orientation.VERTICAL));
//        buttons.add(mImportFileButton);

        mBottomToolBar.getItems().addAll(mImportFileButton, new Separator(Orientation.VERTICAL));
//        mTopToolBar.getItems().add(new Separator(Orientation.VERTICAL));
//        mTopToolBar.getItems().add(mImportFileButton);


    }

    private void onImportFileButtonAction() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select a file to import");
        fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("All Files", "*.*"));
        File selectedFile = fileChooser.showOpenDialog(this.getScene().getWindow());
        if (selectedFile != null) {
            importDataFile(selectedFile);
        }
    }

    private void importDataFile(File file) {
        try {
            //check if file is too big
            if (file.length() > 1024 * 1024) {
                throw new VerifyError("File is too big.");
            }
            //get mime type of the file
            String type = java.nio.file.Files.probeContentType(file.toPath());
            //get html exam_content
            byte[] data = org.apache.commons.io.FileUtils.readFileToByteArray(file);
            String base64data = java.util.Base64.getEncoder().encodeToString(data);
//            System.out.println(base64data);
//            System.out.println(type);
            String htmlData = String.format(
                    //"<a href=""></a>",
                    "<img src=\"data:%s;base64,%s\">",
                    type, base64data);
            //System.out.println(htmlData);
            //insert html
            insertHtmlAfterCursor(htmlData);
            //htmlEditor.setHtmlText(htmlData);
        } catch (IOException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }


    public void insertHtmlAfterCursor(String html) {
        //replace invalid chars
        html = html.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\r", "\\r")
                .replace("\n", "\\n");
        //get script
        String script = String.format(
                "(function(html) {"
                        + "  var sel, range;"
                        + "  if (window.getSelection) {"
                        + "    sel = window.getSelection();"
                        + "    if (sel.getRangeAt && sel.rangeCount) {"
                        + "      range = sel.getRangeAt(0);"
                        + "      range.deleteContents();"
                        + "      var el = document.createElement(\"div\");"
                        + "      el.innerHTML = html;"
                        + "      var frag = document.createDocumentFragment(),"
                        + "        node, lastNode;"
                        + "      while ((node = el.firstChild)) {"
                        + "        lastNode = frag.appendChild(node);"
                        + "      }"
                        + "      range.insertNode(frag);"
                        + "      if (lastNode) {"
                        + "        range = range.cloneRange();"
                        + "        range.setStartAfter(lastNode);"
                        + "        range.collapse(true);"
                        + "        sel.removeAllRanges();"
                        + "        sel.addRange(range);"
                        + "      }"
                        + "    }"
                        + "  }"
                        + "  else if (document.selection && "
                        + "           document.selection.type != \"Control\") {"
                        + "    document.selection.createRange().pasteHTML(html);"
                        + "  }"
                        + "})(\"%s\");", html);
        //execute script
        mWebView.getEngine().executeScript(script);
    }

//    public String getText(){
//        return this.htmlEditor.getHtmlText();
//    }
//
//    public void setText(String text){
//        this.htmlEditor.setHtmlText(text);
//    }

    public String getHtmlText(){
        return this.htmlEditor.getHtmlText();
    }

    public void setHtmlText(String text){
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

    public void setToggleModeEnabled(boolean val){
        if(val){
            this.hideToolbars(this.htmlEditor);
            this.button.setVisible(true);
            this.button.setManaged(true);
        }else {
            this.showToolbars(this.htmlEditor);
            this.button.setVisible(false);
            this.button.setManaged(false);
        }
    }
}
