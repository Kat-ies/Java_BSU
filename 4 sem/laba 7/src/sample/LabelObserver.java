package sample;

import javafx.scene.control.Label;
import javafx.scene.text.Font;

import java.util.Observable;
import java.util.Observer;

public class LabelObserver extends Label implements Observer {
    public LabelObserver() {
        this.setFont(new Font("Verdena", 60));
    }

    @Override
    public void update(Observable o, Object arg) {
        this.setText((String)arg);
    }

}
