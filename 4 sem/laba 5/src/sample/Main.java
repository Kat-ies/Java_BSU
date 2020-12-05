package sample;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main extends Application {

    private Pattern currentPattern;
    private Map<String, Pattern> patternMap;
    private Label status;
    private TextField textField;

    private void updateStatus(String text) {
        status.setTextFill(currentPattern.matcher(text).matches()?Color.GREEN:Color.RED);
    }

    @Override
    public void start(Stage primaryStage) {

        /*___________ Task 1 ______________*/

        patternMap = new HashMap<String, Pattern>() {{
            put("Натуральные числа", Pattern.compile("[1-9]\\d*"));
            put("Integer", Pattern.compile("([+-]?[1-9]\\d*|0)"));
            put("Float", Pattern.compile("[+-]?((\\d+\\.\\d*)|(\\d*\\.?\\d+))([eE][-+]?\\d+)?"));;
            put("Дата", Pattern.compile("(((0[1-9]|[12][0-9]|3[01])\\.(0[13578]|10|12))|((0[1-9]|[12][0-9]|30)\\.(0[469]|11))|((0[1-9]|[12][0-9])\\.02))\\.\\d\\d\\d\\d"));
            put("Время", Pattern.compile("([01]?[0-9]|2[0-3])[: ][0-5][0-9]([: ][0-5][0-9])?"));
            put("Email", Pattern.compile("(\\w+\\.?+\\w+)+@(\\w+_?)+\\.\\w{2,}"));
        }};

        final ComboBox<String> patternSelect = new ComboBox<>();
        patternSelect.getItems().addAll("Натуральные числа", "Integer", "Float", "Дата", "Время", "Email");

        status = new Label("\uD83C\uDF11");
        status.setTextFill(Color.RED);

        textField = new TextField ();
        textField.textProperty().addListener((observable, oldValue, newValue) -> updateStatus(newValue));

        patternSelect.valueProperty().addListener((observable, oldValue, newValue) -> {
            currentPattern = patternMap.get(newValue);
            updateStatus(textField.getText());
        });
        patternSelect.setValue("Выберите тип");


        /*___________ Task 2 ______________*/

        ListView<String> dates = new ListView<>();
        dates.setPrefWidth(100);

        TextArea textArea = new TextArea();
        textArea.setPrefWidth(215);
        textArea.textProperty().addListener((observable, oldValue, newValue) -> {
            dates.getItems().clear();
            Matcher m = patternMap.get("Дата").matcher(newValue);
            while (m.find())
                dates.getItems().add(m.group());
        });


        /*___________ View ______________*/
        HBox task1 = new HBox();
        task1.setSpacing(15);
        task1.getChildren().addAll(patternSelect, textField, status);

        HBox task2 = new HBox();
        task2.setSpacing(5);
        task2.getChildren().addAll(textArea, dates);

        VBox root = new VBox();
        root.setSpacing(30);
        root.getChildren().addAll(task1, task2);

        Scene scene = new Scene(root, 320, 200);
        primaryStage.setTitle("Регулярные выражения");
        primaryStage.setScene(scene);
        primaryStage.show();
    }



    public static void main(String[] args) {
        launch(args);
    }
}