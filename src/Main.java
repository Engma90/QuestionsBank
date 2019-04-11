import controllers.WindowLoader;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import models.DBSingletonHandler;


public class Main extends Application {

    @Override
    public void start(final Stage splashStage) {
        // Todo: Splash Screen
        new Thread(() -> Platform.runLater(() -> createSplash(splashStage))).start();


    }

    private void createSplash(final Stage splashStage) {
        final Pane root = new Pane();

        ImageView imageView = new ImageView(new Image(getClass().getResourceAsStream("/views/images/splash.png")));
        root.getChildren().addAll(imageView);
        Scene scene = new Scene(root, imageView.getImage().getWidth(), imageView.getImage().getHeight());
        splashStage.setScene(scene);
        splashStage.initStyle(StageStyle.TRANSPARENT);
        scene.setFill(Color.TRANSPARENT);
        splashStage.setAlwaysOnTop(true);
        splashStage.setOnShown(event -> {
            FadeTransition fadeIn = new FadeTransition(Duration.seconds(1), root);
            fadeIn.setOnFinished(event1 -> {
                DBSingletonHandler.getInstance();

                FadeTransition fadeOut = new FadeTransition(Duration.seconds(1), root);
                fadeOut.setOnFinished(event11 -> {
                    new WindowLoader().load(null, "/views/Home.fxml", null, null, false, false, null);
                    splashStage.close();
                });
                fadeOut.setFromValue(1.0);
                fadeOut.setToValue(0.0);
                fadeOut.play();

            });
            fadeIn.setFromValue(0.0);
            fadeIn.setToValue(1.0);
            fadeIn.play();
        });
        splashStage.show();
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        splashStage.setX((primScreenBounds.getWidth() - splashStage.getWidth()) / 2);
        splashStage.setY((primScreenBounds.getHeight() - splashStage.getHeight()) / 2);
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void stop(){
        try {
            super.stop();
            System.out.println("App stopped");
            DBSingletonHandler dbSingletonHandler = DBSingletonHandler.tryGetInstance();
            if(dbSingletonHandler != null){
                dbSingletonHandler.closeConnection();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
