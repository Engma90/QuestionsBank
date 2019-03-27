package controllers;

import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

public class NumberField extends TextField {
    private int min, max;

    public IUpdatable getParentController() {
        return parentController;
    }
    public void setParentController(IUpdatable parentController) {
        this.parentController = parentController;
    }

    private IUpdatable parentController;
    public NumberField() {
        min = Integer.MIN_VALUE;
        max = Integer.MAX_VALUE;
        setText("0");
        this.addEventFilter(KeyEvent.KEY_TYPED, new EventHandler<KeyEvent>() {
            public void handle( KeyEvent t ) {
                char ar[] = t.getCharacter().toCharArray();
                char ch = ar[t.getCharacter().toCharArray().length - 1];
                if (!(ch >= '0' && ch <= '9')) {
                    System.out.println("The char you entered is not a number");
                    t.consume();
                }
            }
        });
        textProperty().addListener((observable, oldValue, newValue) -> {
            if(getText().startsWith("0")){
                setText(Integer.parseInt(getText())+"");
            }
            if(getText().length() == 0){
//                clear();
                setText("0");
            }
            if(!getText().isEmpty()) {
                if (Integer.parseInt(getText()) < min) {
//                    clear();
                    setText(min + "");
                }
                if (Integer.parseInt(getText()) > max) {
//                    clear();
                    setText(max + "");
                }
                if(null != parentController)
                    parentController.update();
            }
        });
    }


    public void setMin(int val){
        this.min = val;
    }
    public void setMax(int val){
        this.max = val;
    }
}