import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.scene.input.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.util.Timer;
import java.util.TimerTask;

public class Text_box extends Pane{
    private int CURRENT_LINE = 0;
    private  boolean isFocused = false;
    Timer timer;
    HBox hbox_line;
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
    Pane main_text;
    @FXML
    VBox text_vbox;
    @FXML
    HBox first_hbox, fir;
    @FXML
    public void initialize() {
        setHboxFocus(fir);
        setInputListener(fir);
        Platform.runLater(() -> fir.requestFocus());
        main_text.setOnMouseClicked(e -> {
            focus_border(true);
            checkClickLine(e);
        });

        main_text.setOnDragDetected(e -> {
            focus_border(true);
        });

        Draggable.Nature nature = new Draggable.Nature(main_text);
    }

    public void focus_border(boolean show){
        if (show) {
            main_text.getStyleClass().add("text_border_focus");
        } else {
            main_text.getStyleClass().clear();
//           main_text.getStyleClass().remove("text_border_focus");
        }
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
//            System.out.println(line+" "+wasFocused+" "+focused);
            if (focused) {
                focus_border(true);
                setTextInputAnimation(line);
            } else {
                focus_border(false);
                if(timer!=null)
                    timer.cancel();
            }
        });

        line.requestFocus();
    }

    public void setTextInputAnimation(HBox line){
        TimerTask task = new TimerTask(){
            @Override
            public void run() {
                line.getStyleClass().remove("text_border_none");
                line.getStyleClass().add("text_border_input");
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                line.getStyleClass().add("text_border_none");
                line.getStyleClass().remove("text_border_input");
            }
        };

        timer = new Timer();
        timer.schedule(task, 500,1000);
    }

    public void setInputListener(HBox word_hbox){
        word_hbox.setOnKeyPressed(e -> {
            hbox_line = (HBox)text_vbox.getChildren().get(CURRENT_LINE);
            int num = hbox_line.getChildren().indexOf(word_hbox);
            int length = hbox_line.getChildren().size()-1;
            if(num == -1) num = 0;

            HBox word;
            switch(wordInput(e.getCode())){
                case "word":
                    Text input = new Text(e.getText());
                    input.setFont(Font.font("Helvetica", FontWeight.BOLD, 20));

                    word = new HBox();
                    word.getChildren().add(input);
                    word.getStyleClass().add("text_border_none");

                    if(num != length) num += 1;
                    hbox_line.getChildren().add(num, word);

                    setHboxFocus(word);


                    

                    if(hbox_line.getWidth()>this.getPrefWidth()-40){
                        this.setPrefWidth(hbox_line.getWidth()+30);
                    }
                    break;
                case "line":
                    hbox_line = new HBox();
                    hbox_line.setMinHeight(30);

                    HBox tem = new HBox();
                    tem.setPrefHeight(100);
                    tem.setPrefWidth(5);
                    tem.getStyleClass().add("text_border_none");

                    hbox_line.getChildren().add(tem);
                    text_vbox.getChildren().add(hbox_line);
                    CURRENT_LINE++;

                    this.setPrefHeight(text_vbox.getHeight()+35);

                    setHboxFocus(tem);
                    setInputListener(tem);
                    break;
            }
        });
    }
    public void checkClickLine(MouseEvent event){
        double input_x = event.getScreenX();
        double input_y = event.getScreenY();

        for(int a = 0 ; a < text_vbox.getChildren().size() ; a++){
            HBox tem =  (HBox)text_vbox.getChildren().get(a);
            Bounds bound_tem = tem.localToScreen(tem.getBoundsInLocal());

            if(input_y >= bound_tem.getMinY() && input_y <= bound_tem.getMaxY()){
                CURRENT_LINE = a;
                HBox last = (HBox)tem.getChildren().get(tem.getChildren().size()-1);
                Bounds bound_last = last.localToScreen(last.getBoundsInLocal());
                if(input_x >= bound_last.getMaxX())
                    last.requestFocus();
                break;
            }
        }
    }
}
