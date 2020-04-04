package display;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class ScenesGroup {

    public static Scene MENU = getMenu();

    private static Scene getMenu() {
        VBox menuLayout = new VBox();
        menuLayout.setAlignment(Pos.CENTER);
        menuLayout.setSpacing(20);
        Button play = new Button("Play");
        Button settings = new Button("Settings");
        Button exit = new Button("Exit");
        menuLayout.getChildren().addAll(play, settings, exit);
        return new Scene(menuLayout, 300, 200);
    }

}
