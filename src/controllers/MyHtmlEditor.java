package controllers;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableListBase;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Orientation;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.web.HTMLEditor;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import org.jsoup.Jsoup;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MyHtmlEditor extends HBox {

    private Button button;
    private HTMLEditor htmlEditor;
    private boolean isShown = false;

    public void setParentController(IUpdatable parentController) {
        this.parentController = parentController;
    }

    private IUpdatable parentController;

    public static final String TOP_TOOLBAR = ".top-toolbar";
    public static final String BOTTOM_TOOLBAR = ".bottom-toolbar";
    public static final String WEB_VIEW = ".web-view";
    public static final String RTL = ".html-editor-align-right";
    public static final String LTR = ".html-editor-align-left";

    private final WebView mWebView;
    private final ToolBar mTopToolBar;
    private final ToolBar mBottomToolBar;
    //private final Button mRTL,mLTR;
    private static final int MAX_IMAGE_WIDTH = 400;
    private static final int MAX_IMAGE_HEIGHT = 400;


    public MyHtmlEditor() {
        //Todo: handle drag and drop image
        this.setSpacing(1);
        //this.setPrefWidth(1000);
        //this.getStylesheets().clear();
//        this.htmlEditor.setSt = FXCollections.observableArrayList();
//        this.htmlEditor.getStylesheets().add("@css/modena/modena.css");


        button = new Button("^");
        htmlEditor = new HTMLEditor();

        htmlEditor.setPrefHeight(25);
        htmlEditor.setPrefWidth(700);
        this.getChildren().add(htmlEditor);


        this.getChildren().add(button);
        hideToolbars(this.htmlEditor);
        button.setOnAction(e -> {
            if (isShown) {
                button.setText("^");
                hideToolbars(this.htmlEditor);
                htmlEditor.setPrefHeight(25);
            } else {
                button.setText("<");
                htmlEditor.setPrefHeight(300);
                showToolbars(this.htmlEditor);
            }
            isShown = !isShown;
        });


        mWebView = (WebView) this.htmlEditor.lookup(WEB_VIEW);

        HBox.setHgrow(htmlEditor, Priority.ALWAYS);
        //HBox.setHgrow(mWebView,Priority.ALWAYS);
        mTopToolBar = (ToolBar) this.htmlEditor.lookup(TOP_TOOLBAR);
        mBottomToolBar = (ToolBar) this.htmlEditor.lookup(BOTTOM_TOOLBAR);
//        mRTL = (Button) htmlEditor.lookupAll(RTL).iterator().next();
//        mLTR = (Button) htmlEditor.lookupAll(LTR).iterator().next();
//        mRTL.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent event) {
//                setRTL(true);
//            }
//        });
//        mLTR.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent event) {
//                setRTL(false);
//            }
//        });

        createCustomButtons();
        //this.htmlEditor.setHtmlText("<html />");

        //TODO: change orientation when click on rtl and reverse on ltr
        //htmlEditor.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
        //mBottomToolBar.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
        //mTopToolBar.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);

        //setRTL(true);
    }

    private void setRTL(boolean val) {

        if (val) {
            htmlEditor.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
        } else {
            htmlEditor.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
        }
        mTopToolBar.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
        mBottomToolBar.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);

//        final Map map = new HashMap();
//        for (Node candidate: (mTopToolBar.lookupAll(".html-editor-align-right"))) {
//            List list = ((ToolBar) candidate).getItems();
//            for (int i = 0; i < list.size(); i++) {
//                Node b = (Node) list.get(i);
//                System.out.println(b.getStyleClass());
//                map.put(map.size() + 1, b);
//            }
//        }


//        htmlEditor.setVisible(false);
//        Platform.runLater(new Runnable() {
//
//            @Override
//            public void run() {
//                Node[] nodes = htmlEditor.lookupAll(".tool-bar").toArray(new Node[0]);
//                for (Node node : nodes) {
////                    node.setVisible(false);
////                    node.setManaged(false);
//                    for(Node n:((Pane) node).getChildren())
//                    System.out.println(node.getTypeSelector());
//                    System.out.println(node.getStyleClass());
//                    System.out.println(node.getClass());
//                    System.out.println(node.toString());
//                    System.out.println();
//                }
//                htmlEditor.setVisible(true);
//            }
//
//        });

    }

    private void createCustomButtons() {
        //add embed file button
        ImageView graphic = new ImageView(new Image("/views/images/insert-image.png"));
        Button mImportFileButton = new Button("", graphic);
        mImportFileButton.setTooltip(new Tooltip("Import Image"));
        mImportFileButton.setOnAction((event) -> {
            try {
                onImportFileButtonAction();
            } catch (IOException e) {
                new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
                //e.printStackTrace();
            }
        });

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

    private void onImportFileButtonAction() throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select a file to import");
        fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("All Files", "*.*"));
        File selectedFile = fileChooser.showOpenDialog(this.getScene().getWindow());
        if (selectedFile != null) {
            importDataFile(selectedFile);
        }
    }

    private void importDataFile(File file) throws IOException {
//        try {
        //check if file is too big
//        if (file.length() > 1024 * 1024) {
//            throw new IOException("File is too big.");
//        }
        //get mime type of the file
        String type = java.nio.file.Files.probeContentType(file.toPath());
        if (type == null || !type.toLowerCase().contains("image")) {
            throw new IOException("File not Supported");
        }

        //get html exam_content
//            byte[] data = org.apache.commons.io.FileUtils.readFileToByteArray(file);
//            String base64data = java.util.Base64.getEncoder().encodeToString(data);

        String base64data = getResizedImage(file);


//            System.out.println(base64data);
//            System.out.println(type);
        String htmlData = String.format(
                //"<a href=""></a>",
                "<img src=\"data:%s;base64,%s\"/>",
                type, base64data);
        //System.out.println(htmlData);
        //insert html
        insertHtmlAfterCursor(htmlData);
        //htmlEditor.setHtmlText(htmlData);
//        } catch (IOException ex) {
//            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
//        }
    }

    private String getResizedImage(File file) throws IOException {
        BufferedImage originalImage = ImageIO.read(file);
        int w = originalImage.getWidth();
        int h = originalImage.getHeight();
        double WIDTH_HEIGHT_RATIO = (double) w / (double)h;

        double newWidth, newHeight;
        BufferedImage finalImage;

        if (h <= MAX_IMAGE_HEIGHT && w <= MAX_IMAGE_WIDTH) {
            finalImage = originalImage;
        } else {
            if (h >= w) {
                newHeight = (double) MAX_IMAGE_HEIGHT;
                newWidth = WIDTH_HEIGHT_RATIO * newHeight;
                finalImage = scale(originalImage, (int)newWidth, (int)newHeight);
//              double scale = (newWidth / newHeight) / WIDTH_HEIGHT_RATIO
//                finalImage = scale1(originalImage,(newWidth/(double)w));
            } else {
                newWidth = (double) MAX_IMAGE_WIDTH;
                newHeight = newWidth / WIDTH_HEIGHT_RATIO;
                finalImage = scale(originalImage, (int)newWidth, (int)newHeight);
//                finalImage = scale1(originalImage, (newHeight/(double)h));
            }
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write( finalImage, (originalImage.getTransparency() == Transparency.OPAQUE) ? "jpg" : "png", baos );
        baos.flush();
        byte[] imageInByte = baos.toByteArray();
        baos.close();

        return java.util.Base64.getEncoder().encodeToString(imageInByte);
    }

    private BufferedImage scale(BufferedImage img, int targetWidth, int targetHeight) {

        int type = (img.getTransparency() == Transparency.OPAQUE) ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
        BufferedImage ret = img;
        BufferedImage scratchImage = null;
        Graphics2D g2 = null;

        int w = img.getWidth();
        int h = img.getHeight();

        int prevW = w;
        int prevH = h;

        do {
            if (w > targetWidth) {
                w /= 2;
                w = (w < targetWidth) ? targetWidth : w;
            }

            if (h > targetHeight) {
                h /= 2;
                h = (h < targetHeight) ? targetHeight : h;
            }

            if (scratchImage == null) {
                scratchImage = new BufferedImage(w, h, type);
                g2 = scratchImage.createGraphics();
            }

            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2.drawImage(ret, 0, 0, w, h, 0, 0, prevW, prevH, null);

            prevW = w;
            prevH = h;
            ret = scratchImage;
        } while (w != targetWidth || h != targetHeight);

        if (g2 != null) {
            g2.dispose();
        }

        if (targetWidth != ret.getWidth() || targetHeight != ret.getHeight()) {
            scratchImage = new BufferedImage(targetWidth, targetHeight, type);
            g2 = scratchImage.createGraphics();
            g2.drawImage(ret, 0, 0, null);
            g2.dispose();
            ret = scratchImage;
        }

        return ret;

    }


    public void insertHtmlAfterCursor(String html) {
        //replace invalid chars
        html = html.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\r", "\\r")
                .replace("\n", "\\n");
        //get script
        //Todo: add css class no-page-break to this divs
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

    public String getHtmlText() {
        return this.htmlEditor.getHtmlText();
    }

    public String getRawText() {
        return Jsoup.parse(getHtmlText()).text();
    }

    public void setHtmlText(String text) {
        this.htmlEditor.setHtmlText(text);
    }


    private void showToolbars(final HTMLEditor editor) {
        editor.setVisible(false);
        Platform.runLater(() -> {
            Node[] nodes = editor.lookupAll(".tool-bar").toArray(new Node[0]);
            for (Node node : nodes) {
                node.setVisible(true);
                node.setManaged(true);
            }
            editor.setVisible(true);
        });
    }

    private void hideToolbars(final HTMLEditor editor) {
        editor.setVisible(false);
        Platform.runLater(() -> {
            Node[] nodes = editor.lookupAll(".tool-bar").toArray(new Node[0]);
            for (Node node : nodes) {
                node.setVisible(false);
                node.setManaged(false);

            }
            editor.setVisible(true);
        });
    }

    public void setToggleModeEnabled(boolean val) {
        if (val) {
            this.hideToolbars(this.htmlEditor);
            this.button.setVisible(true);
            this.button.setManaged(true);
        } else {
            this.showToolbars(this.htmlEditor);
            this.button.setVisible(false);
            this.button.setManaged(false);
        }
    }
}
