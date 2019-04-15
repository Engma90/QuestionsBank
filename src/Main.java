import controllers.Dialog;
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
import models.DBHandler;


public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(final Stage splashStage) {
        runWithSplash(splashStage);
    }

    @Override
    public void stop() {
        try {
            super.stop();
            DBHandler dbHandler = DBHandler.tryGetInstance();
            if (dbHandler != null) {
                dbHandler.disconnect();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void runWithSplash(final Stage splashStage) {
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
            fadeIn.setOnFinished(event1 -> Platform.runLater(() -> {
                while (true) {
                    if (DBHandler.getInstance().createSchema()) {
                        FadeTransition fadeOut = new FadeTransition(Duration.seconds(1), root);
                        fadeOut.setOnFinished(event11 -> {
                            new WindowLoader().load(null, "/views/Home.fxml", null, null, false, false, null);
                            splashStage.close();
                        });
                        fadeOut.setFromValue(1.0);
                        fadeOut.setToValue(0.0);
                        fadeOut.play();
                        break;
                    } else {
                        if (!Dialog.CreateDialog("Connection error", "Couldn't connect to server, retry?",
                                "Yes", "No", splashStage)) {
                            splashStage.close();
                            break;
                        }
                    }
                }
            }));
            fadeIn.setFromValue(0.0);
            fadeIn.setToValue(1.0);
            fadeIn.play();
        });
        splashStage.show();
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        splashStage.setX((primScreenBounds.getWidth() - splashStage.getWidth()) / 2);
        splashStage.setY((primScreenBounds.getHeight() - splashStage.getHeight()) / 2);
    }





}
