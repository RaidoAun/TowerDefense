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

    public static void createPopup(String labelText, boolean focus) { //focus true tähendab, et mängija ei saa muude akendega samal ajal suhelda.
        Stage popup = new Stage();
        popup.setTitle("Pop-up");
        if (focus) popup.initModality(Modality.APPLICATION_MODAL);
        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);
        label.setText(labelText);
        label.setTextAlignment(TextAlignment.CENTER);
        Button button = new Button("Ok");
        button.setOnAction(e -> popup.close());
        layout.getChildren().addAll(label, button);
        Scene pop = new Scene(layout, 300, 200);
        popup.setScene(pop);
        if (focus) {
            popup.showAndWait();
        } else {
            popup.show();
        }
    }

}
