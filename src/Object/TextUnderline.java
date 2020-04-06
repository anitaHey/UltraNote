package Object;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class TextUnderline extends Pane {
    String color = "TRANSPARENT";
    int height = 0;
    public TextUnderline(){
        super();

        this.setMaxHeight(0);
    }

    public void setUnderline(Boolean input, String input_color){
        if(input) color = input_color;
        else color = "TRANSPARENT";

        this.setStyle("-fx-border-color: "+ color + ";" + "-fx-border-width:" + height+" 0 0 0;");
    }

    public boolean isTextUnderline(){
        return !color.equals("TRANSPARENT");
    }

    public void setUnderlineColor(String input_color){
        color = input_color;

        this.setStyle("-fx-border-color: "+ color + ";" + "-fx-border-width:" + height+" 0 0 0;");
    }

    public void setUnderlineHeight(int input) {
        height = input;

        this.setStyle("-fx-border-color: "+ color + ";" + "-fx-border-width:" + height+" 0 0 0;");
    }

    public void setUnderlineWidth(int input){
        this.setPrefWidth(input);
    }


}
