package Object;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class TextLine extends VBox {
    HBox wordHBox;
    HBox underlineHBox;

    public TextLine(){
        wordHBox = new HBox();
        underlineHBox = new HBox();

        wordHBox.setAlignment(Pos.BASELINE_LEFT);
        wordHBox.setPrefHeight(30);
        underlineHBox.setPrefHeight(10);

        TextObj first = new TextObj("");
        first.setPrefHeight(30);
        first.setPrefWidth(0);

        TextUnderline un = new TextUnderline();
        first.setUnderlineObj(un);
        addWord(first, first.getUnderlineObj(), -1);

        this.getChildren().add(wordHBox);
        this.getChildren().add(underlineHBox);
        this.setSpacing(-5);
    }

    public void addWord(TextObj text, TextUnderline underline, int num){
        if(num != -1){
            getWordHBox().getChildren().add(num, text);
            getUnderlineHBox().getChildren().add(num, underline);
        }else{
            getWordHBox().getChildren().add(text);
            getUnderlineHBox().getChildren().add(underline);
        }
    }

    public HBox getWordHBox(){
        return wordHBox;
    }

    public HBox getUnderlineHBox(){
        return underlineHBox;
    }

    public int getHBoxSize(){
        return getWordHBox().getChildren().size();
    }

    public TextObj getIndex(int index){
        return (TextObj)getWordHBox().getChildren().get(index);
    }

    public int getTextIndex(TextObj input){
        return getWordHBox().getChildren().indexOf(input);
    }

    public void removeIndex(int index){
        getWordHBox().getChildren().remove(index);
        getUnderlineHBox().getChildren().remove(index);
    }
}
