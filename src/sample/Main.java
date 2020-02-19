package sample;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.UnaryOperator;

public class Main extends Application {
    private static Canvas canvas = new Canvas();
    private static GraphicsContext gc = canvas.getGraphicsContext2D();
    static Map map = new Map(150,80, canvas);

    @Override
    public void start(Stage primaryStage) throws Exception{
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
        //menu.getChildren().add(menu_button_box);
        primaryStage.show();
        play_btn.setOnAction(event -> {
            GridPane game = new GridPane();
            Scene game_scene = new Scene(game);
            game.getChildren().add(canvas);
            Rectangle2D screenSizes = Screen.getPrimary().getBounds();
            double blocksize;
            if (screenSizes.getWidth()/Main.map.getX()<screenSizes.getHeight()/Main.map.getY()){
                 blocksize = screenSizes.getWidth()/Main.map.getX();
            }else{
                blocksize = screenSizes.getHeight()/Main.map.getY();
            }
            primaryStage.setScene(game_scene);
            startGame(blocksize);
            primaryStage.setMaximized(true);
        });
        settings_btn.setOnAction(event -> {
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
    void startGame(double blocksize){
        AnimationTimer animate = new AnimationTimer() {
            public void handle(long currentNanoTime) {
                long startNanoTime = System.nanoTime();
                map.drawMap(blocksize);
                drawTowerRanges();
                for (Spawnpoint spawn : map.getSpawnpoints()) {
                    spawn.moveMonsters();
                    spawn.drawMonsters();
                    //spawn.shootTowers();
                }
                double t = (currentNanoTime - startNanoTime) / 1000000000.0*1000;
                //System.out.println(t);
            }
        };

        map.initMap();
        map.genMap(2);
        map.genFlippedMap();
        //Genereerib nii palju spawnpointe, kui võimalik on.
        map.genOpenBlocks();
        map.generateSpawnpoints(5, 40);
        map.spawnSpawnpoints();
        map.drawMap(blocksize);


        System.out.println("Vali nexuse asukoht kaardil!");
        AtomicBoolean isNexus = new AtomicBoolean(false);
        map.drawMap(blocksize);
        canvas.setOnMouseClicked(e -> {

            int x = convertPixelToIndex(e.getX());
            int y = convertPixelToIndex(e.getY());
            Block eventBlock = map.getMap_matrix()[x][y];

            if (!isNexus.get()) {
                map.setNexusxy(new int[]{x, y});
                map.getSpawnpoints().get(0).genPath(500);
                if (map.getSpawnpoints().get(0).getPath().length == 0) {
                    System.out.println("Valitud nexuse asukoht ei sobi. Proovi uuesti.");
                } else {
                    map.genNexus();
                    map.genPathstoNexus(500);
                    for (Spawnpoint spawn: map.getSpawnpoints()) {
                        map.drawPath(spawn.getPath());
                    }
                    isNexus.set(true);
                    System.out.println("Nexuse asukoht valitud.");
                    animate.start();
                }
            } else {
                System.out.println(eventBlock.getId());

                //Spawnpoindid, mille teele jääb tower ette.
                List<Spawnpoint> updatableSpawns = map.pathsContain(new int[]{x, y});
                //System.out.println(updatableSpawns.size());
                boolean towerPossible = true;
                //Ajutise seina loomine.
                Block oldBlock = map.getBlock(x, y);
                map.editMap_matrix(x, y, new Block(1, 0, new Color(0, 0, 0, 1)));
                //Uuenda spawnpointide path.
                System.out.println(updatableSpawns.size());
                for (Spawnpoint spawn : updatableSpawns) {
                    int[][] oldPath = spawn.getPath();
                    spawn.genPath(500);
                    if (spawn.getPath().length == 0) {
                        towerPossible = false;
                        spawn.setPath(oldPath);
                        break;
                    } else {
                        System.out.println("Uus tee!");
                        map.deletePath(oldPath);
                        map.drawPath(spawn.getPath());
                    }
                }

                map.editMap_matrix(x, y, oldBlock);

                if (towerPossible) {
                    for (Block tower : map.getTowers()) {
                        tower.setActive(false);
                    }
                    if (eventBlock.getId() == 0 || eventBlock.getId() == 9){
                        //map.editMap_matrix(i, j, new Block(10, 0, new Color(0, 0, 0, 1)));
                        eventBlock.makeTower(10,x*map.getSize()+map.getSize()/2,y*map.getSize()+map.getSize()/2);
                        map.editMap_matrix(x, y, eventBlock);
                        //map.drawBlock(convertPixelToIndex((e.getX())),convertPixelToIndex(e.getY()));
                        map.getTowers().add(eventBlock);
                        eventBlock.setActive(true);
                    }
                    else if (eventBlock.getId()>=10){
                        eventBlock.setActive(true);
                    }
                    System.out.println(eventBlock.getId());
                    System.out.println("----------------------");
                } else {
                    System.out.println("Towerit pole võimalik maha panna. Proovi uuesti.");
                }
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static Canvas getCanvas() {
        return canvas;
    }

    static GraphicsContext getGc() {
        return gc;
    }

    private int convertPixelToIndex(double pixel_coords){
        return (int) (pixel_coords/map.getSize());
    }

    private void drawTowerRanges(){
        for (Block tower: map.getTowers()) {
            if (tower.getActive()){
                tower.drawRange();
            }
        }
    }

}

