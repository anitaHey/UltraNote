package Object;

import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

public class TextObj extends Label {
    TextUnderline underline;
    FontWeight fontWeight;
    FontPosture fontPosture;
    String fontFamily;
    String fontColor = "#000000";
    int fontSize;

    public TextObj (String input){
        super(input);

        underline = null;
        fontWeight = null;
        fontPosture = null;
        fontFamily = "";
        fontSize = -1;

        this.setTextFont("Consolas", FontWeight.NORMAL, FontPosture.REGULAR, 25);
        this.getStyleClass().add("text_bg_input");
    }

    public void setTextFont(String family, FontWeight weight, FontPosture posture, int size){
        if(weight != null) fontWeight = weight;
        if(posture != null) fontPosture = posture;
        if(family != null) fontFamily = family;
        if(size != -1) {
            fontSize = size;
            if(getUnderlineObj() != null) setUnderProperty();
        }

        this.setFont(Font.font(fontFamily, fontWeight, fontPosture, fontSize));
    }

    public void setTextFamily(String family){
        fontFamily = family;
        this.setFont(Font.font(fontFamily, fontWeight, fontPosture, fontSize));
    }

    public void setFontWeight(FontWeight weight){
        fontWeight = weight;
        this.setFont(Font.font(fontFamily, fontWeight, fontPosture, fontSize));
    }

    public void setFontSize(int size){
        fontSize = size;
        this.setFont(Font.font(fontFamily, fontWeight, fontPosture, fontSize));
    }

    public void setFontPosture(FontPosture posture){
        fontPosture = posture;
        this.setFont(Font.font(fontFamily, fontWeight, fontPosture, fontSize));
    }

    public int getFontSize(){
        return fontSize;
    }

    public String getFontFamily(){
        return fontFamily;
    }

    public boolean isBold(){
        return this.getFont().getStyle().contains("Bold");
    }

    public boolean isItalic() {
        return this.getFont().getStyle().contains("Italic");
    }

    public void setUnderlineObj(TextUnderline new_underline){
        underline = new_underline;
        setUnderProperty();
    }

    public TextUnderline getUnderlineObj(){
        return underline;
    }

    public boolean isTextUnderline(){
        return getUnderlineObj().isTextUnderline();
    }

    public void setTextUnderline(Boolean input, String color){
        if(color == null) color = this.fontColor;
        getUnderlineObj().setUnderline(input, color);
    }

    public void setFontColor(String input){
        fontColor = input;
        this.setTextFill(Color.web(input));
        getUnderlineObj().setUnderlineColor(input);
    }

    public String getFontColor(){
        return fontColor;
    }

    public void setUnderProperty(){
        this.widthProperty().addListener((obs, oldVal, newVal) -> {
            getUnderlineObj().setUnderlineWidth(newVal.intValue());
        });

        this.heightProperty().addListener((obs, oldVal, newVal) -> {
            int height = ( getFontSize()<10) ? 1: (getFontSize()/10);
            getUnderlineObj().setUnderlineHeight(height);
        });
    }
}