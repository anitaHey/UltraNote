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

public class Text_box extends TextFlow {
    private int CURRENT_LINE = 0;
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
    public void initialize() {
        main_text.setOnKeyPressed(e -> {
            HBox hbox;
            switch(wordInput(e.getCode())){
                case "word":
                    Text input = new Text(e.getText());
                    input.setFont(Font.font("Helvetica", FontWeight.BOLD, 20));
                    hbox = (HBox)text_vbox.getChildren().get(CURRENT_LINE);
                    hbox.getChildren().add(input);
                    if(hbox.getWidth()>this.getPrefWidth()-40){
                        this.setPrefWidth(hbox.getWidth()+20);
                    }
                    break;
                case "line":
                    hbox = new HBox();
                    hbox.setMinHeight(30);
                    text_vbox.getChildren().add(hbox);
                    CURRENT_LINE++;
                    this.setPrefHeight(text_vbox.getHeight()+35);
                    break;
            }
        });
        main_text.setOnMouseClicked(e -> {
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
}
