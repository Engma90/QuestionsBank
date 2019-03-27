import controllers.HomeController;
import controllers.IWindow;
import controllers.WindowLoader;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
//        Parent root;
//        FXMLLoader loader = new
//                FXMLLoader(getClass().getResource("/views/Home.fxml"));
//        root = loader.load();
//        primaryStage.setScene(new Scene(root));
//        ((HomeController) loader.getController()).setWindowData(primaryStage, null);
//        primaryStage.show();
        new WindowLoader().load(null,"/views/Home.fxml",null,null,false,false,null);

    }

    public static void main(String[] args) {
        launch(args);
    }




}
