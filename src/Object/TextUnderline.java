package Object;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class TextUnderline extends Pane {
    Boolean hasLine = false;
    String color = "TRANSPARENT";
    int height = 0;

    public TextUnderline() {
        super();

        this.setMaxHeight(0);
    }

    public void setUnderline(Boolean input, String input_color) {
        hasLine = input;
        if (hasLine) {
            if (input_color != null) color = input_color;
            this.setStyle("-fx-border-color: " + color + ";" + "-fx-border-width:" + height + " 0 0 0;");
        } else
            this.setStyle("-fx-border-color: TRANSPARENT;" + "-fx-border-width:" + height + " 0 0 0;");
    }

    public boolean isTextUnderline() {
        return hasLine;
    }

    public void setUnderlineColor(String input_color) {
        color = input_color;

        if (hasLine)
            this.setStyle("-fx-border-color: " + color + ";" + "-fx-border-width:" + height + " 0 0 0;");
    }

    public void setUnderlineHeight(int input) {
        height = input;

        if (hasLine)
            this.setStyle("-fx-border-color: " + color + ";" + "-fx-border-width:" + height + " 0 0 0;");
    }

    public void setUnderlineWidth(int input) {
        this.setPrefWidth(input);
    }


}
