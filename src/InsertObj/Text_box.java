package InsertObj;

import InsertObj.Draggable;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
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
    Timer timer;
    HBox hbox_line;
    boolean pass = true;
    public Text_box(){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../FXML/TextBoxFxml.fxml"));
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

    public void setHboxFocus(HBox line){
        line.setOnMouseClicked(e -> {
            line.requestFocus();
        });

        line.focusedProperty().addListener((observable, wasFocused, focused) -> {
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
                    Thread.sleep(400);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                line.getStyleClass().add("text_border_none");
                line.getStyleClass().remove("text_border_input");
            }
        };

        timer = new Timer();
        timer.schedule(task, 400,800);
    }

    public void setInputListener(HBox word_hbox){
        word_hbox.addEventFilter(KeyEvent.ANY, e -> {
            input(e, word_hbox);
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

    public synchronized void input(KeyEvent e, HBox word_hbox){
        hbox_line = (HBox)text_vbox.getChildren().get(CURRENT_LINE);
        int num = hbox_line.getChildren().indexOf(word_hbox);
        int length = hbox_line.getChildren().size();
        if(num == -1) num = 0;
        HBox word;

        if(e.getEventType().toString().equals("KEY_PRESSED")) {
            switch(e.getCode()) {
                case ENTER:
                    pass = false;
                    HBox hbox_new_line = new HBox();
                    hbox_new_line.setMinHeight(30);

                    HBox tem = new HBox();
                    tem.setPrefWidth(3);
                    tem.getStyleClass().add("text_border_none");


                    if(num != length-1){
                        for(int a = length-1;a>num;a--){
                            HBox old_hbox = (HBox)hbox_line.getChildren().get(a);
                            hbox_new_line.getChildren().add(0,old_hbox);
                        }
                    }

                    hbox_new_line.getChildren().add(0,tem);
                    text_vbox.getChildren().add(hbox_new_line);
                    CURRENT_LINE++;

                    this.setPrefHeight(text_vbox.getHeight()+30);

                    setHboxFocus(tem);
                    setInputListener(tem);

                    hbox_new_line.getChildren().get(hbox_new_line.getChildren().size()-1).requestFocus();
                    break;
                case BACK_SPACE:
                    //還沒設長度!!!!!!
                    //每行hbox設寬度偵測
                    if(num>0){
                        int lastword = num - 1;
                        Platform.runLater(() -> hbox_line.getChildren().get(lastword).requestFocus());

                        hbox_line.getChildren().remove(num);
                    }else{
                        if(CURRENT_LINE != 0){
                            HBox hbox_last_line = (HBox)text_vbox.getChildren().get(CURRENT_LINE-1);
                            for(int a = 1; a < length;a++){
                                HBox old_hbox = (HBox)hbox_line.getChildren().get(0);
                                hbox_last_line.getChildren().add(old_hbox);
                            }
                            text_vbox.getChildren().remove(CURRENT_LINE);
                            CURRENT_LINE--;
                            hbox_last_line.getChildren().get(hbox_last_line.getChildren().size()-1).requestFocus();
                        }
                    }
                    break;
                case LEFT:
                    if(num>0)
                        hbox_line.getChildren().get(num-1).requestFocus();
                    else{
                        if(CURRENT_LINE != 0){
                            CURRENT_LINE--;
                            hbox_line = (HBox)text_vbox.getChildren().get(CURRENT_LINE);
                            hbox_line.getChildren().get(hbox_line.getChildren().size()-1).requestFocus();
                        }
                    }
                    break;
                case RIGHT:
                    if(num < length-1)
                        hbox_line.getChildren().get(num+1).requestFocus();
                    else{
                        if(CURRENT_LINE != text_vbox.getChildren().size()-1){
                            CURRENT_LINE++;
                            hbox_line = (HBox)text_vbox.getChildren().get(CURRENT_LINE);
                            hbox_line.getChildren().get(0).requestFocus();
                        }
                    }
                    break;
                case UP:
                    if(CURRENT_LINE > 0){
                        CURRENT_LINE--;
                        hbox_line = (HBox)text_vbox.getChildren().get(CURRENT_LINE);
                        if(num >= hbox_line.getChildren().size())
                            hbox_line.getChildren().get(hbox_line.getChildren().size()-1).requestFocus();
                        else
                            hbox_line.getChildren().get(num).requestFocus();
                    }
                    break;
                case DOWN:
                    if(CURRENT_LINE < text_vbox.getChildren().size()-1){
                        CURRENT_LINE++;
                        hbox_line = (HBox)text_vbox.getChildren().get(CURRENT_LINE);
                        if(num >= hbox_line.getChildren().size())
                            hbox_line.getChildren().get(hbox_line.getChildren().size()-1).requestFocus();
                        else
                            hbox_line.getChildren().get(num).requestFocus();
                    }
                    break;
            }
        }else if(e.getEventType().toString().equals("KEY_TYPED") && pass) {
            Text input = new Text(e.getCharacter());
            input.setFont(Font.font("Helvetica", FontWeight.BOLD, 20));


                word = new HBox();
                word.getChildren().add(input);
                word.getStyleClass().add("text_border_none");

                if(num != length) num += 1;
                hbox_line.getChildren().add(num, word);

                setHboxFocus(word);
                setInputListener(word);


            if(hbox_line.getWidth()>this.getPrefWidth()-40)
                this.setPrefWidth(hbox_line.getWidth()+30);

        }else{
            pass = true;
        }
    }
}
