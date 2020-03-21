package sample;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class PopUp {

    private static Label label = new Label();

    public static void createPopup(String labelText) {
        Stage popup = new Stage();
        popup.setTitle("Pop-up");
        popup.initModality(Modality.APPLICATION_MODAL);
        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);
        label.setText(labelText);
        label.setTextAlignment(TextAlignment.CENTER);
        Button button = new Button("Ok");
        button.setOnAction(e -> popup.close());
        layout.getChildren().addAll(label, button);
        Scene pop = new Scene(layout, 200, 200);
        popup.setScene(pop);
        popup.showAndWait();
    }

    public static void editLabel(String newText) {
        label.setText(newText);
    }

}
