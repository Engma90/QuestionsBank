package controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

public class MyButton extends HBox {
    private ImageView imageView;
    private StringProperty imageUrl;
    private final double[] STYLE_NORMAL = new double[2];
    private final double[] STYLE_PRESSED = new double[2];

    public MyButton() {
        imageView = new ImageView();

//        imageView.setOnMousePressed(event -> {
//
//
//                });
        imageView.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                STYLE_NORMAL[0] = getPrefWidth();
                STYLE_NORMAL[1] = getPrefHeight();
                STYLE_PRESSED[0] = STYLE_NORMAL[0] * 0.9;
                STYLE_PRESSED[1] = STYLE_NORMAL[1] * 0.9;
                imageView.setFitWidth(STYLE_PRESSED[0]);
                imageView.setFitHeight(STYLE_PRESSED[1]);
            }
        });

        imageView.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                imageView.setFitWidth(STYLE_NORMAL[0]);
                imageView.setFitHeight(STYLE_NORMAL[1]);
            }
        });

        imageView.setOnMouseEntered(event -> {
            //setBackground(new Background(new BackgroundFill(new Color(0, 0, 0.5,1), CornerRadii.EMPTY,new Insets(0))));
            ColorAdjust desaturate = new ColorAdjust();
            //desaturate.setSaturation(0.3);
            desaturate.setHue(0.5);
            setEffect(desaturate);
        });

        imageView.setOnMouseExited(event -> {
            //setBackground(new Background(new BackgroundFill(new Color(0, 0, 0.5,0), CornerRadii.EMPTY,new Insets(0))));

            ColorAdjust desaturate = new ColorAdjust();
            //desaturate.setSaturation(0);
            setEffect(desaturate);
        });
        this.getChildren().add(imageView);
        this.setAlignment(Pos.CENTER);
    }

    private StringProperty imageUrlProperty() {
        if (imageUrl == null) { // might be better to write the class so that this is never true?
            imageUrl = new SimpleStringProperty();
        }
        return imageUrl;
    }

    public final void setImageUrl(String url) {
        imageUrlProperty().set(url);
        imageView.setImage(new Image(url));
        imageView.setFitWidth(getPrefWidth());
        imageView.setFitHeight(getPrefHeight());
    }

    public final String getImageUrl() {
        return imageUrlProperty().get();
    }

    public void setDisable1(boolean value) {
        super.setDisable(value);
        if (value) {
            ColorAdjust desaturate = new ColorAdjust();
            desaturate.setSaturation(-1);
            setEffect(desaturate);
        } else {
            ColorAdjust desaturate = new ColorAdjust();
            setEffect(desaturate);
        }
    }
}
