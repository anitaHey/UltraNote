package InsertObj;

import Controller.MainController;
import Controller.Toolbar_TextController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.input.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Text_box extends Pane {

    private int CURRENT_LINE = 0;
    HBox hbox_line, hbox_underline;
    boolean pass = true;
    Timer timer;

    public Text_box() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../FXML/TextBoxFxml.fxml"));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @FXML
    Pane main_text, fir;
    @FXML
    VBox text_vbox;

    @FXML
    public void initialize() {
        setInputFocus(fir, null);
        setInputListener(fir);
        fir.setStyle("-fx-border-insets: 3 0 -10 0;");
        Platform.runLater(() -> fir.requestFocus());
        MainController.getInstance().change_toolbar(MainController.Type.Text,false);

        main_text.setOnMouseClicked(e->{
            focus_border(true);
            checkClickLine(e);
            MainController.getInstance().change_toolbar(MainController.Type.Text,false);
        });

        main_text.setOnDragDetected(e -> {
            focus_border(true);
        });

        new Draggable.Move(main_text);
    }

    public void focus_border(boolean show) {
        if (show) {
            main_text.getStyleClass().add("text_border_focus");
        } else {
            main_text.getStyleClass().clear();
//           main_text.getStyleClass().remove("text_border_focus");
        }
    }

    public void setInputFocus(Pane word, Text text) {
        if(text != null){
            text.setOnMouseClicked(e -> {
                hbox_line = (HBox)((VBox) text_vbox.getChildren().get(CURRENT_LINE)).getChildren().get(0);

                setRequestFocus(word, hbox_line);
            });
        }

        word.setOnMouseClicked(e -> {
            hbox_line = (HBox)((VBox) text_vbox.getChildren().get(CURRENT_LINE)).getChildren().get(0);

            setRequestFocus(word, hbox_line);
        });

        word.focusedProperty().addListener((observable, wasFocused, focused) -> {
            if (focused) {
                focus_border(true);
                word.getStyleClass().add("text_bg_input");
                word.getStyleClass().remove("text_bg_none");
                setTextInputAnimation(word);
            } else {
                focus_border(false);
                word.getStyleClass().remove("text_bg_input");
                word.getStyleClass().add("text_bg_none");
                if (timer != null)
                    timer.cancel();
            }
        });

        word.requestFocus();
    }

    public void setInputListener(Pane word_pane) {
        word_pane.addEventFilter(KeyEvent.ANY, e -> {
            input(e, word_pane);
        });
    }

    public void checkClickLine(MouseEvent event) {
        double input_x = event.getScreenX();
        double input_y = event.getScreenY();

        for (int a = 0; a < text_vbox.getChildren().size(); a++) {
            VBox tem = (VBox) text_vbox.getChildren().get(a);
            Bounds bound_tem = tem.localToScreen(tem.getBoundsInLocal());

            if (input_y >= bound_tem.getMinY() && input_y <= bound_tem.getMaxY()) {
                CURRENT_LINE = a;
                HBox line = (HBox)tem.getChildren().get(0);
                Pane last = (Pane)line.getChildren().get(line.getChildren().size()-1);
                Bounds bound_last = last.localToScreen(last.getBoundsInLocal());
                if (input_x >= bound_last.getMaxX())
                    setRequestFocus(last, tem);
                break;
            }
        }
    }

    public synchronized void input(KeyEvent e, Pane word_pane) {
        hbox_line = (HBox)((VBox) text_vbox.getChildren().get(CURRENT_LINE)).getChildren().get(0);
        hbox_underline = (HBox)((VBox) text_vbox.getChildren().get(CURRENT_LINE)).getChildren().get(1);
        int num = hbox_line.getChildren().indexOf(word_pane);
        int length = hbox_line.getChildren().size();
        if (num == -1) num = 0;
        HBox word_hbox, underline_hbox;

        if (e.getEventType().toString().equals("KEY_PRESSED")) {
            switch (e.getCode()) {
                case ENTER:
                    pass = false;
                    VBox new_line = new VBox();
                    new_line.setPrefHeight(45);

                    Pane pane = new Pane();
                    pane.setPrefWidth(0);
                    pane.setPrefHeight(30);
                    pane.getStyleClass().add("text_bg_input");
                    pane.setStyle("-fx-border-insets: 3 0 -10 0;");

                    word_hbox = new HBox();
                    word_hbox.setAlignment(Pos.BASELINE_LEFT);
                    word_hbox.setPrefHeight(40);
                    word_hbox.getChildren().add(pane);

                    underline_hbox = new HBox();

                    if (num != length - 1) {
                        for (int a = length - 1; a > num; a--) {
                            Node old_node = hbox_line.getChildren().get(a);
                            word_hbox.getChildren().add(1, old_node);
                        }
                        for (int a = (length - 1)/2 -1; a > num/2 -1 ; a--) {
                            Node old_node = hbox_underline.getChildren().get(a);
                            underline_hbox.getChildren().add(0, old_node);
                        }
                    }

                    new_line.getChildren().add(word_hbox);
                    new_line.getChildren().add(underline_hbox);

                    CURRENT_LINE++;
                    text_vbox.getChildren().add(CURRENT_LINE, new_line);

                    setInputFocus(pane, null);
                    setInputListener(pane);
                    pane.requestFocus();
                    break;
                case BACK_SPACE:
                    pass = false;
                    if (num > 0) {
                        int lastword = num - 2;
                        Platform.runLater(() -> hbox_line.getChildren().get(lastword).requestFocus());

                        hbox_line.getChildren().remove(num);
                        hbox_line.getChildren().remove(num-1);

                        hbox_underline.getChildren().remove(num/2-1);
                    } else {
                        if (CURRENT_LINE != 0) {
                            HBox last_line = (HBox)((VBox) text_vbox.getChildren().get(CURRENT_LINE - 1)).getChildren().get(0);
                            HBox last_underline = (HBox)((VBox) text_vbox.getChildren().get(CURRENT_LINE - 1)).getChildren().get(1);
                            int last_length = last_line.getChildren().size();
                            for (int a = 1; a < length; a++) {
                                Node old_node = hbox_line.getChildren().get(1);
                                last_line.getChildren().add(old_node);
                            }
                            for (int a = 0; a < (length-1)/2; a++) {
                                Node old_node = hbox_underline.getChildren().get(0);
                                last_underline.getChildren().add(old_node);
                            }
                            text_vbox.getChildren().remove(CURRENT_LINE);
                            CURRENT_LINE--;
                            last_line.getChildren().get(last_length - 1).requestFocus();
                        }
                    }
                    break;
                case DELETE:
                    pass = false;
                    if (num < length - 1) {
                        hbox_line.getChildren().remove(num + 2);
                        hbox_line.getChildren().remove(num + 1);
                    } else {
                        if (CURRENT_LINE != text_vbox.getChildren().size() - 1) {
                            HBox last_line = (HBox)((VBox) text_vbox.getChildren().get(CURRENT_LINE + 1)).getChildren().get(0);
                            HBox last_underline = (HBox)((VBox) text_vbox.getChildren().get(CURRENT_LINE + 1)).getChildren().get(1);
                            int last_length = last_line.getChildren().size();
                            for (int a = 1; a < last_length; a++) {
                                Node old_node = last_line.getChildren().get(1);
                                hbox_line.getChildren().add(old_node);
                            }

                            for (int a = 0; a < (last_length-1)/2; a++) {
                                Node old_node = last_underline.getChildren().get(0);
                                hbox_underline.getChildren().add(old_node);
                            }

                            text_vbox.getChildren().remove(CURRENT_LINE + 1);

                            hbox_line.getChildren().get(length - 1).requestFocus();
                        }
                    }
                    break;
                case RIGHT:
                    if (num < length - 1)
                        hbox_line.getChildren().get(num + 2).requestFocus();
                    else {
                        if (CURRENT_LINE != text_vbox.getChildren().size() - 1) {
                            CURRENT_LINE++;
                            hbox_line = (HBox)((VBox) text_vbox.getChildren().get(CURRENT_LINE)).getChildren().get(0);
                            hbox_line.getChildren().get(0).requestFocus();
                        }
                    }
                    break;
                case LEFT:
                    if (num > 0)
                        hbox_line.getChildren().get(num - 2).requestFocus();
                    else {
                        if (CURRENT_LINE != 0) {
                            CURRENT_LINE--;
                            hbox_line = (HBox)((VBox) text_vbox.getChildren().get(CURRENT_LINE)).getChildren().get(0);
                            hbox_line.getChildren().get(hbox_line.getChildren().size() - 1).requestFocus();
                        }
                    }
                    break;
            }
        } else if (e.getEventType().toString().equals("KEY_TYPED") && pass) {
            Text input = new Text(e.getCharacter());
            input.setFont(Font.font("Consolas", FontWeight.NORMAL, 25));

            Pane word = new Pane();
            word.setPrefHeight(input.getLayoutBounds().getHeight() + 5);
            word.setPrefWidth(0);
            word.getStyleClass().add("text_bg_input");
            word.setStyle("-fx-border-insets: 3 0 -10 0;");

            input.fontProperty().addListener((observable, oldValue, newValue)->{
                word.setPrefHeight(input.getLayoutBounds().getHeight() + 5);
            });

            if (num != length) num += 1;
            hbox_line.getChildren().add(num++, input);
            hbox_line.getChildren().add(num, word);

            Pane underline = new Pane();
            int width = (input.getFont().getSize()<10)?1:(int)(input.getFont().getSize()/10);
            underline.setPrefWidth(input.getLayoutBounds().getWidth()+1);
            underline.setStyle("-fx-border-width: 0 0 "+ width +" 0; -fx-border-color: #525252;");
            hbox_underline.getChildren().add(underline);
            setInputFocus(word, input);
            setInputListener(word);

        } else {
            pass = true;
        }
    }

    public void setRequestFocus(Pane line, Pane last){
        line.requestFocus();
//        clearSelectText(true);
//        int num = last.getChildren().indexOf(line.getParent());
//        select_text[0] = CURRENT_LINE;
//        select_text[1] = num;
    }

    public void setTextInputAnimation(Pane word) {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                word.getStyleClass().remove("text_bg_input");
                word.getStyleClass().add("text_bg_none");
                try {
                    Thread.sleep(300);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                word.getStyleClass().remove("text_bg_none");
                word.getStyleClass().add("text_bg_input");
            }
        };

        timer = new Timer();
        timer.schedule(task, 300, 600);
    }
}
