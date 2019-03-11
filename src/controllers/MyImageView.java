package controllers;

import javafx.event.EventHandler;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class MyImageView extends ImageView {
    private final double[] STYLE_NORMAL = new double[2];
    private final double[] STYLE_PRESSED = new double[2];
    public MyImageView(){
        setDisable1(true);
        setOnMousePressed(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                STYLE_NORMAL[0] = getFitWidth();
                STYLE_NORMAL[1] = getFitHeight();
                STYLE_PRESSED[0] = STYLE_NORMAL[0] * 0.9;
                STYLE_PRESSED[1] = STYLE_NORMAL[1] * 0.9;
                setFitWidth(STYLE_PRESSED[0]);
                setFitHeight(STYLE_PRESSED[1]);
            }
        });

        setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                setFitWidth(STYLE_NORMAL[0]);
                setFitHeight(STYLE_NORMAL[1]);
            }
        });

        setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                ColorAdjust desaturate = new ColorAdjust();
                desaturate.setSaturation(-1);
                setEffect(desaturate);
            }
        });

        setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                ColorAdjust desaturate = new ColorAdjust();
                desaturate.setSaturation(0);
                setEffect(desaturate);
            }
        });
    }

    public void setDisable1(boolean value) {
        super.setDisable(value);
        if(value) {
            ColorAdjust desaturate = new ColorAdjust();
            desaturate.setSaturation(-1);
            setEffect(desaturate);
        }
        else {
            ColorAdjust desaturate = new ColorAdjust();
            desaturate.setSaturation(0);
            setEffect(desaturate);
        }
    }
}
