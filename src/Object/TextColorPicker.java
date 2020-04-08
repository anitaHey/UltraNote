package Object;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class TextColorPicker extends Pane {
    public TextColorPicker () {
        super();
        // The important part
        this.setPrefHeight(40);
        this.setPrefWidth(40);

    }
}