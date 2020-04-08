package Object;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class TextColorPicker extends Pane {
    /* TODO: Custom color picker. */
    public TextColorPicker() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../FXML/ColorPickerFxml.fxml"));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @FXML
    public void initialize() {

    }
}