import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.input.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.util.Timer;
import java.util.TimerTask;

public class Text_box extends TextFlow {
    private int CURRENT_LINE = 0;
    private  boolean isFocused = false;
    Timer timer;
    public Text_box(){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("TextBoxFxml.fxml"));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        }catch (Exception e){
            System.out.println(e);
        }
    }

    @FXML
    TextFlow main_text;
    @FXML
    VBox text_vbox;
    @FXML
    HBox first_hbox;
    @FXML
    public void initialize() {
        listener改成Hbox，最外層border另外設
        main_text.setOnKeyPressed(e -> {
            HBox hbox_line;
            switch(wordInput(e.getCode())){
                case "word":
                    HBox word = new HBox();
                    Text input = new Text(e.getText());
                    input.setFont(Font.font("Helvetica", FontWeight.BOLD, 20));
                    word.getChildren().add(input);

                    hbox_line = (HBox)text_vbox.getChildren().get(CURRENT_LINE);
                    hbox_line.getChildren().add(word);

                    setHboxFocus(word);
                    word.requestFocus();

                    if(hbox_line.getWidth()>this.getPrefWidth()-40){
                        this.setPrefWidth(hbox_line.getWidth()+20);
                    }
                    break;
                case "line":
                    hbox_line = new HBox();
                    hbox_line.setMinHeight(30);
                    text_vbox.getChildren().add(hbox_line);
                    CURRENT_LINE++;
                    this.setPrefHeight(text_vbox.getHeight()+35);
                    break;
            }
        });
        main_text.setOnMouseClicked(e -> {
            this.requestFocus();
        });

        main_text.setOnDragDetected(e -> {
            this.requestFocus();
        });

        main_text.focusedProperty().addListener((arg0, oldPropertyValue, newPropertyValue) -> {
            if (newPropertyValue) {
                getStyleClass().add("text_border_focus");
            } else {
                getStyleClass().clear();
            }
        });

        Draggable.Nature nature = new Draggable.Nature(main_text);
    }

    public String wordInput(KeyCode input){
        if(input == KeyCode.ENTER){
            return "line";
        }else if(input.isDigitKey() || input.isLetterKey() || input.isWhitespaceKey()){
            return "word";
        }

        return "";
    }

    public void setHboxFocus(HBox line){
        line.setOnMouseClicked(e -> {
            line.requestFocus();
        });

        line.focusedProperty().addListener((observable, wasFocused, focused) -> {
            System.out.println(line+" "+wasFocused+" "+focused);
            if (focused) {
                setTextInputAnimation(line);
            } else {
                System.out.println();
                if(timer!=null)
                    timer.cancel();
            }
        });
    }

    public void setTextInputAnimation(HBox line){
        TimerTask task = new TimerTask(){
            @Override
            public void run() {
                line.getStyleClass().add("text_border_input");
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                line.getStyleClass().remove("text_border_input");
            }
        };

        timer = new Timer();
        timer.schedule(task, 500,1000);
    }
}
