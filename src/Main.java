import controllers.FileExporter;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import models.QuestionTableHandler;

import java.io.IOException;

import static com.sun.org.apache.xalan.internal.utils.SecuritySupport.getResourceAsStream;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {

        Parent root;
        root = FXMLLoader.load(getClass().getResource("/views/Home.fxml"));
//        Image icon = new Image(getClass().getResourceAsStream("icon.png"));
//
//        primaryStage.getIcons().add(icon);
        primaryStage.setTitle("Home");
        primaryStage.setMaximized(true);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
