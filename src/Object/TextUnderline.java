package Object;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class TextUnderline extends Pane {
    String color = "#000";
    public TextUnderline(){
        super();
    }

    public void setUnderline(Boolean input, String input_color){
        if(input){
            color = input_color;
            this.setStyle("-fx-background-color: "+ color + ";");
        }
        else this.setStyle("");
    }

    public boolean isTextUnderline(){
        return this.getStyle().length()>0;
    }

    public void setColor(String input_color){
        color = input_color;
        this.setStyle("-fx-background-color: "+ color + ";");
    }

    public void setHeight(int input) {
        this.setPrefHeight(input);
    }

    public void setWidth(double input){
        this.setPrefWidth(input);
    }
}
