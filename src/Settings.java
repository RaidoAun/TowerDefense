import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class Settings {

    private static int ajutineX = Main.getMap().getX();
    private static int ajutineY = Main.getMap().getY();
    private static int ajutineSpawnCount = Main.getMap().getSpawnCount();
    private static int ajutineSpawnDist = Main.getMap().getMinDisdanceBetweenSpawns();

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

        HBox mapSizeX = new HBox();
        mapSizeX.setSpacing(10);
        mapSizeX.setAlignment(Pos.CENTER);
        Label x_text = new Label("Map's x size:");
        TextField x_input = new TextField(Integer.toString(Main.getMap().getX()));
        mapSizeX.getChildren().addAll(x_text, x_input);

        HBox mapSizeY = new HBox();
        mapSizeY.setSpacing(10);
        mapSizeY.setAlignment(Pos.CENTER);
        Label y_text = new Label("Map's y size:");
        TextField y_input = new TextField(Integer.toString(Main.getMap().getY()));
        mapSizeY.getChildren().addAll(y_text, y_input);

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
        settings_layout.getChildren().addAll(spawnpointCount, distance, mapSizeX, mapSizeY, button_row);
        Scene settings = new Scene(settings_layout, 500, 250);

        back.setOnAction(e -> Main.switchToMenu());
        apply.setOnAction(e -> {
            Main.getMap().setX(getAjutineX());
            Main.getMap().setY(getAjutineY());
            Main.getMap().setSpawnCount(getAjutineS());
            Main.getMap().setMinDisdanceBetweenSpawns(getAjutineSpawnD());
        });
        reset.setOnAction(e -> {
            s_input.setText(Integer.toString(Main.getMap().getSpawnCount()));
            d_input.setText(Integer.toString(Main.getMap().getMinDisdanceBetweenSpawns()));
            x_input.setText(Integer.toString(Main.getMap().getX()));
            y_input.setText(Integer.toString(Main.getMap().getY()));
        });

        d_input.focusedProperty().addListener((v, oldValue, newValue) -> {
            if (!newValue) {
                try {
                    setAjutineSpawnD(Integer.parseInt(d_input.getCharacters().toString()));
                } catch (NumberFormatException error) {
                    PopUp.createPopup("Error! Sisestage number!", true);
                    d_input.clear();
                    d_input.requestFocus();
                }
            }
        });

        s_input.focusedProperty().addListener((v, oldValue, newValue) -> {
            if (!newValue) {
                try {
                    setAjutineS(Integer.parseInt(s_input.getCharacters().toString()));
                } catch (NumberFormatException error) {
                    PopUp.createPopup("Error! Sisestage number!", true);
                    s_input.clear();
                    s_input.requestFocus();
                }
            }
        });

        x_input.focusedProperty().addListener((v, oldValue, newValue) -> {
            if (!newValue) {
                try {
                    setAjutineX(Integer.parseInt(x_input.getCharacters().toString()));
                    setAjutineY((int) Math.ceil(ajutineX * Math.pow(Main.getAspectRatio(), -1)));
                    y_input.setText(Integer.toString(ajutineY));
                } catch (NumberFormatException error) {
                    PopUp.createPopup("Error! Sisestage number!", true);
                    x_input.clear();
                    x_input.requestFocus();
                }
            }
        });

        y_input.focusedProperty().addListener((v, oldValue, newValue) -> {
            if (!newValue) {
                try {
                    setAjutineY(Integer.parseInt(y_input.getCharacters().toString()));
                    setAjutineX((int) Math.ceil(ajutineY * Main.getAspectRatio()));
                    x_input.setText(Integer.toString(ajutineX));
                } catch (NumberFormatException error) {
                    PopUp.createPopup("Error! Sisestage number!", true);
                    y_input.clear();
                    y_input.requestFocus();
                }
            }
        });

        return settings;

    }

    private static int getAjutineX() {
        return ajutineX;
    }

    private static void setAjutineX(int x) {
        ajutineX = x;
    }

    private static int getAjutineY() {
        return ajutineY;
    }

    private static void setAjutineY(int y) {
        ajutineY = y;
    }

    private static int getAjutineS() {
        return ajutineSpawnCount;
    }

    private static void setAjutineS(int ajutineSpawnCount) {
        Settings.ajutineSpawnCount = ajutineSpawnCount;
    }

    public static int getAjutineSpawnD() {
        return ajutineSpawnDist;
    }

    public static void setAjutineSpawnD(int ajutineSpawnDist) {
        Settings.ajutineSpawnDist = ajutineSpawnDist;
    }

}