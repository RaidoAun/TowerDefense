package sample;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.function.UnaryOperator;

public class Main extends Application {
    private static Canvas canvas = new Canvas();
    private static GraphicsContext gc = canvas.getGraphicsContext2D();
    static Map map = new Map(150,80, canvas);

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        //Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
       // GridPane menu = new GridPane();
        VBox menu_button_box = new VBox();
        menu_button_box.setSpacing(10);
        Scene menu_scene = new Scene(menu_button_box,300,200);
        Button play_btn = new Button("Play");
        Button settings_btn = new Button("Settings");

        menu_button_box.getChildren().addAll(play_btn,settings_btn);
        menu_button_box.setAlignment(Pos.CENTER);

        primaryStage.setTitle("Hello World");
        primaryStage.setScene(menu_scene);
        primaryStage.show();

        play_btn.setOnAction(event -> {
            primaryStage.setScene(Game.getGameScene());
            primaryStage.setMaximized(true);
            Game.generateGame();
            PopUp.createPopup("Vali nexuse asukoht kaardil!\nMäng algab pärast nexuse maha panekut!");
            canvas.setOnMouseClicked(e -> {
                if (Game.chooseNexus(e.getX(), e.getY())) Game.startRounds();
            });
        });

        settings_btn.setOnAction(e -> {
            VBox settings_Vbox = new VBox();
            VBox settings_Vbox2= new VBox();
            HBox settings_Hbox = new HBox();
            Scene settings_scene = new Scene(settings_Hbox);
            settings_Vbox.setSpacing(10);
            settings_Vbox.setAlignment(Pos.CENTER);
            UnaryOperator<TextFormatter.Change> filter = change -> {
                String text = change.getText();

                if (text.matches("[0-9]*")) {
                    return change;
                }

                return null;
            };
            TextField mapx_val = new TextField();
            TextField mapy_val = new TextField();
            mapx_val.setTextFormatter(new TextFormatter<>(filter));
            mapy_val.setTextFormatter(new TextFormatter<>(filter));
            mapx_val.setPromptText("Map horisontal size");
            mapy_val.setPromptText("Map vertical size");
            settings_Vbox.getChildren().addAll(mapx_val);
            settings_Vbox2.getChildren().addAll(mapy_val);
            settings_Hbox.getChildren().addAll(settings_Vbox,settings_Vbox2);
            primaryStage.setScene(settings_scene);
        });

    }

    public static Canvas getCanvas() {
        return canvas;
    }

    public static Map getMap() {
        return map;
    }

    static GraphicsContext getGc() {
        return gc;
    }

}

