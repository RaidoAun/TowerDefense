import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class Settings {

    private static int ajutineX = Main.getMap().getX();
    private static int ajutineY = Main.getMap().getY();
    private static int ajutineSpawnCount = Main.getMap().getSpawnCount();
    private static int ajutineSpawnDist = Main.getMap().getMinDisdanceBetweenSpawns();
    private static int ajutineBlockSize = Main.getBlockSize();

    public static Scene settingsScene() {

        HBox spawnpointCount = new HBox();
        spawnpointCount.setSpacing(10);
        spawnpointCount.setAlignment(Pos.CENTER);
        Label s_text = new Label("Spawnpoint count:");
        TextField s_input = new TextField(Integer.toString(Main.getMap().getSpawnCount()));
        spawnpointCount.getChildren().addAll(s_text, s_input);

        HBox distance = new HBox();
        distance.setSpacing(10);
        distance.setAlignment(Pos.CENTER);
        Label d_text = new Label("Minimum distance between spawns:");
        TextField d_input = new TextField(Integer.toString(Main.getMap().getMinDisdanceBetweenSpawns()));
        distance.getChildren().addAll(d_text, d_input);

        Slider slider = new Slider(0, 100, ajutineBlockSize);
        slider.setBlockIncrement(1);
        slider.setMajorTickUnit(10);
        slider.setMinorTickCount(9);
        slider.setShowTickMarks(true);
        slider.setShowTickLabels(true);
        slider.setSnapToTicks(true);

        VBox x = new VBox();
        x.setAlignment(Pos.CENTER);
        x.setSpacing(20);
        Label x_text = new Label("Map's x size:");
        TextField x_input = new TextField(Integer.toString(Main.getMap().getX()));
        x_input.setAlignment(Pos.CENTER);
        x_input.setDisable(true);
        x_input.setStyle("-fx-opacity: 1;");
        x.getChildren().addAll(x_text, x_input);

        VBox blockSize = new VBox();
        blockSize.setAlignment(Pos.CENTER);
        blockSize.setSpacing(20);
        Label blockSizeText = new Label("Block size:");
        TextField blockSizeInput = new TextField(Integer.toString(ajutineBlockSize));
        blockSizeInput.setAlignment(Pos.CENTER);
        blockSize.getChildren().addAll(blockSizeText, blockSizeInput);

        VBox y = new VBox();
        y.setAlignment(Pos.CENTER);
        y.setSpacing(20);
        Label y_text = new Label("Map's y size:");
        TextField y_input = new TextField(Integer.toString(Main.getMap().getY()));
        y_input.setAlignment(Pos.CENTER);
        y_input.setDisable(true);
        y_input.setStyle("-fx-opacity: 1;");
        y.getChildren().addAll(y_text, y_input);

        HBox kuldneTriio = new HBox();
        kuldneTriio.setAlignment(Pos.CENTER);
        kuldneTriio.setSpacing(10);
        kuldneTriio.getChildren().addAll(x, blockSize, y);

        HBox button_row = new HBox();
        button_row.setSpacing(40);
        button_row.setAlignment(Pos.CENTER);
        Button back = new Button("Back");
        Button apply = new Button("Apply");
        Button reset = new Button("Reset");
        button_row.getChildren().addAll(back, apply, reset);

        VBox settings_layout = new VBox();
        settings_layout.setSpacing(20);
        settings_layout.setAlignment(Pos.CENTER);
        settings_layout.getChildren().addAll(spawnpointCount, distance, slider, kuldneTriio, button_row);
        Scene settings = new Scene(settings_layout, 550, 300);

        back.setOnAction(e -> Main.switchToMenu());
        apply.setOnAction(e -> {
            Main.getMap().setX(ajutineX);
            Main.getMap().setY(ajutineY);
            Main.getMap().setSpawnCount(ajutineSpawnCount);
            Main.getMap().setMinDisdanceBetweenSpawns(ajutineSpawnDist);
            Main.setBlockSize(ajutineBlockSize);
        });
        reset.setOnAction(e -> {
            s_input.setText(Integer.toString(Main.getMap().getSpawnCount()));
            d_input.setText(Integer.toString(Main.getMap().getMinDisdanceBetweenSpawns()));
            x_input.setText(Integer.toString(Main.getMap().getX()));
            y_input.setText(Integer.toString(Main.getMap().getY()));
        });

        s_input.focusedProperty().addListener((v, oldValue, newValue) -> {
            if (!newValue) {
                try {
                    ajutineSpawnCount = Integer.parseInt(s_input.getCharacters().toString());
                } catch (NumberFormatException error) {
                    PopUp.createPopup("Error! Sisestage number!", true);
                    s_input.clear();
                    s_input.requestFocus();
                }
            }
        });

        d_input.focusedProperty().addListener((v, oldValue, newValue) -> {
            if (!newValue) {
                try {
                    ajutineSpawnDist = Integer.parseInt(d_input.getCharacters().toString());
                } catch (NumberFormatException error) {
                    PopUp.createPopup("Error! Sisestage number!", true);
                    d_input.clear();
                    d_input.requestFocus();
                }
            }
        });

        slider.valueProperty().addListener((v, oldValue, newValue) -> {
            if (!newValue.equals(oldValue)) {
                ajutineBlockSize = (int) Math.round(newValue.doubleValue());
                if (ajutineBlockSize == 0) ajutineBlockSize = 1;
                ajutineY = Main.getScreenH() / ajutineBlockSize;
                ajutineX = Main.getScreenW() / ajutineBlockSize;
                y_input.setText(Integer.toString(ajutineY));
                x_input.setText(Integer.toString(ajutineX));
                blockSizeInput.setText(Integer.toString(ajutineBlockSize));
            }
        });

        blockSizeInput.focusedProperty().addListener((v, oldValue, newValue) -> {
            if (!newValue) {
                ajutineBlockSize = Integer.parseInt(blockSizeInput.getCharacters().toString());
                if (ajutineBlockSize == 0) ajutineBlockSize = 1;
                slider.setValue(ajutineBlockSize);
            }
        });

        return settings;

    }
}