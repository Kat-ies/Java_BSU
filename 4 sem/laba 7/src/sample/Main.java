package sample;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import javax.swing.*;
import java.util.Observable;


public class Main extends Application{

    private Observable observable;
    private LabelObserver labelObserver;
    private TextObserver textObserver;

    private AnchorPane root;
    private Scene scene;

    private int WIDTH = 800;
    private int HEIGHT = 250;

    @Override
    public void start(Stage primaryStage){
        primaryStage.setTitle("Lab7");
        root = new AnchorPane();

        observable = new Observable(){
            @Override
            public void notifyObservers(Object arg) {
                setChanged();
                super.notifyObservers(arg);
            }
        };

        labelObserver = new LabelObserver();
        textObserver = new TextObserver();

        root.getChildren().addAll(labelObserver, textObserver);
        observable.addObserver(labelObserver);
        observable.addObserver(textObserver);

        scene = new Scene(root, WIDTH, HEIGHT);
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                observable.notifyObservers(event.getCode().toString());

            }

        });

        AnchorPane.setRightAnchor(labelObserver, scene.getWidth()/2);
        AnchorPane.setBottomAnchor(labelObserver, 10.0);
        AnchorPane.setLeftAnchor(labelObserver, 30.0);
        AnchorPane.setTopAnchor(labelObserver, 10.0);

        AnchorPane.setRightAnchor(textObserver, 10.0);
        AnchorPane.setBottomAnchor(textObserver, 10.0);
        AnchorPane.setLeftAnchor(textObserver, scene.getWidth()/2);
        AnchorPane.setTopAnchor(textObserver, 10.0);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
