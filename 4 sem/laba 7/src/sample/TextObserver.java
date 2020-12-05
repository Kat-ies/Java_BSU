package sample;
import javafx.scene.control.TextArea;

import java.util.Observable;
import java.util.Observer;

public class TextObserver extends TextArea implements Observer {
    public TextObserver() {
        this.setEditable(false);
        this.setFocusTraversable(false);
    }

    @Override
    public void update(Observable o, Object arg) { this.appendText((String)arg + "\n"); }
}
