package InsertObj;

import Controller.MainController;
import Controller.Toolbar_TextController;
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

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Text_box extends Pane {
    private int CURRENT_LINE = 0;
    Timer timer;
    HBox hbox_line;
    boolean pass = true;
    int[] select_text = {-1, -1};
    ArrayList<HBox> select_text_hbox = new ArrayList<>();
    Toolbar_TextController controller = Toolbar_TextController.getInstance();
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
        MainController.getInstance().change_toolbar(MainController.Type.Text,false);

        main_text.setOnMouseClicked(e -> {
            focus_border(true);
            checkClickLine(e);
            MainController.getInstance().change_toolbar(MainController.Type.Text,false);
        });

        main_text.setOnDragDetected(e -> {
            focus_border(true);
        });

        main_text.addEventHandler(MouseEvent.MOUSE_DRAGGED, e -> {
            if (!isBorder(e)) {
                for (int a = 0; a < text_vbox.getChildren().size(); a++) {
                    HBox tem = (HBox) text_vbox.getChildren().get(a);
                    for (int b = 0; b < tem.getChildren().size(); b++) {
                        HBox sm_tem = (HBox) tem.getChildren().get(b);
                        if (sm_tem.getChildren().size() > 0) {
                            Bounds bound_tem = sm_tem.localToParent(tem.getBoundsInParent());
                            if (bound_tem.contains(e.getX(), e.getY())) addTextSelect(a, b);
                        }
                    }
                }
            }
        });

        new Draggable.Move(main_text);
    }

    public void addTextSelect(int line, int num) {
        if (select_text[0] != -1) {
            clearSelectText(false);

            if (line == select_text[0]) {
                if (num > select_text[1])
                    for (int a = select_text[1] + 1; a <= num; a++)
                        try {
                            select_text_hbox.add((HBox) ((HBox) text_vbox.getChildren().get(line)).getChildren().get(a));
                        } catch (Exception e) {
                            System.out.println(e);
                        }
                else if (num < select_text[1])
                    for (int a = num; a <= select_text[1]; a++)
                        select_text_hbox.add((HBox) ((HBox) text_vbox.getChildren().get(line)).getChildren().get(a));
            } else if (line < select_text[0]) {
                HBox hbox = (HBox) text_vbox.getChildren().get(select_text[0]);
                for (int a = 0; a <= select_text[1]; a++)
                    select_text_hbox.add((HBox) hbox.getChildren().get(a));
                for (int a = line + 1; a < select_text[0]; a++) {
                    hbox = (HBox) text_vbox.getChildren().get(a);
                    int length = hbox.getChildren().size();
                    for (int b = 0; b < length; b++)
                        select_text_hbox.add((HBox) hbox.getChildren().get(b));
                }
                hbox = (HBox) text_vbox.getChildren().get(line);
                int length = hbox.getChildren().size();
                for (int a = num; a < length; a++)
                    select_text_hbox.add((HBox) hbox.getChildren().get(a));
            } else {
                HBox hbox = (HBox) text_vbox.getChildren().get(select_text[0]);
                int length = hbox.getChildren().size();
                for (int a = select_text[1] + 1; a < length; a++)
                    select_text_hbox.add((HBox) hbox.getChildren().get(a));
                for (int a = select_text[0] + 1; a < line; a++) {
                    hbox = (HBox) text_vbox.getChildren().get(a);
                    length = hbox.getChildren().size();
                    for (int b = 0; b < length; b++)
                        select_text_hbox.add((HBox) hbox.getChildren().get(b));
                }
                hbox = (HBox) text_vbox.getChildren().get(line);
                for (int a = 0; a <= num; a++)
                    select_text_hbox.add((HBox) hbox.getChildren().get(a));
            }
        }
        for (HBox selectTextHbox : select_text_hbox) selectTextHbox.getStyleClass().add("text_select");
        controller.setCurentText(select_text_hbox);
    }

    public void focus_border(boolean show) {
        if (show) {
            main_text.getStyleClass().add("text_border_focus");
            getStyleClass().remove("text_select");
            Paper.setFocusObject(this);
        } else {
            main_text.getStyleClass().clear();
//           main_text.getStyleClass().remove("text_border_focus");
        }
    }

    public void clearSelectText(boolean all) {
        for (HBox selectTextHbox : select_text_hbox) selectTextHbox.getStyleClass().remove("text_select");
        select_text_hbox.clear();
        controller.clearCurentText();
        if (all) select_text[0] = -1;
    }

    public void setHboxFocus(HBox line) {
        line.setOnMouseClicked(e -> {
            hbox_line = (HBox) text_vbox.getChildren().get(CURRENT_LINE);

            setRequestFocus(line, hbox_line);
        });

        line.focusedProperty().addListener((observable, wasFocused, focused) -> {
            if (focused) {
                focus_border(true);
                setTextInputAnimation(line);
            } else {
                focus_border(false);
                if (timer != null)
                    timer.cancel();
            }
        });

        line.requestFocus();
    }

    public void setTextInputAnimation(HBox line) {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                line.getStyleClass().remove("text_border_none");
                line.getStyleClass().add("text_border_input");
                try {
                    Thread.sleep(300);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                line.getStyleClass().add("text_border_none");
                line.getStyleClass().remove("text_border_input");
            }
        };

        timer = new Timer();
        timer.schedule(task, 300, 600);
    }

    public void setInputListener(HBox word_hbox) {
        word_hbox.addEventFilter(KeyEvent.ANY, e -> {
            input(e, word_hbox);
        });
    }

    public void checkClickLine(MouseEvent event) {
        double input_x = event.getScreenX();
        double input_y = event.getScreenY();

        for (int a = 0; a < text_vbox.getChildren().size(); a++) {
            HBox tem = (HBox) text_vbox.getChildren().get(a);
            Bounds bound_tem = tem.localToScreen(tem.getBoundsInLocal());

            if (input_y >= bound_tem.getMinY() && input_y <= bound_tem.getMaxY()) {
                CURRENT_LINE = a;
                HBox last = (HBox) tem.getChildren().get(tem.getChildren().size() - 1);
                Bounds bound_last = last.localToScreen(last.getBoundsInLocal());
                if (input_x >= bound_last.getMaxX())
                    setRequestFocus(last, tem);
                break;
            }
        }
    }

    public synchronized void input(KeyEvent e, HBox word_hbox) {
        hbox_line = (HBox) text_vbox.getChildren().get(CURRENT_LINE);
        int num = hbox_line.getChildren().indexOf(word_hbox);
        int length = hbox_line.getChildren().size();
        if (num == -1) num = 0;
        HBox word;

        if (e.getEventType().toString().equals("KEY_PRESSED")) {
            switch (e.getCode()) {
                case ENTER:
                    pass = false;
                    HBox hbox_new_line = new HBox();
                    hbox_new_line.setMinHeight(30);

                    HBox tem = new HBox();
                    tem.setPrefWidth(2);

                    tem.getStyleClass().add("text_border_none");

                    if (num != length - 1) {
                        for (int a = length - 1; a > num; a--) {
                            HBox old_hbox = (HBox) hbox_line.getChildren().get(a);
                            hbox_new_line.getChildren().add(0, old_hbox);
                        }
                    }

                    hbox_new_line.getChildren().add(0, tem);
                    CURRENT_LINE++;
                    text_vbox.getChildren().add(CURRENT_LINE, hbox_new_line);

                    setHboxFocus(tem);
                    setInputListener(tem);

                    hbox_new_line.getChildren().get(0).requestFocus();
                    break;
                case BACK_SPACE:
                    pass = false;
                    if (num > 0) {
                        int lastword = num - 1;
                        Platform.runLater(() -> hbox_line.getChildren().get(lastword).requestFocus());

                        hbox_line.getChildren().remove(num);

                    } else {
                        if (CURRENT_LINE != 0) {
                            HBox hbox_last_line = (HBox) text_vbox.getChildren().get(CURRENT_LINE - 1);
                            int last_length = hbox_last_line.getChildren().size();
                            for (int a = 1; a < length; a++) {
                                HBox old_hbox = (HBox) hbox_line.getChildren().get(1);
                                hbox_last_line.getChildren().add(old_hbox);
                            }
                            text_vbox.getChildren().remove(CURRENT_LINE);
                            CURRENT_LINE--;
                            hbox_last_line.getChildren().get(last_length - 1).requestFocus();
                        }
                    }
                    break;
                case DELETE:
                    pass = false;
                    if (num < length - 1)
                        hbox_line.getChildren().remove(num + 1);
                    else {
                        if (CURRENT_LINE != text_vbox.getChildren().size() - 1) {
                            HBox hbox_last_line = (HBox) text_vbox.getChildren().get(CURRENT_LINE + 1);
                            int last_length = hbox_last_line.getChildren().size();
                            for (int a = 1; a < last_length; a++) {
                                HBox old_hbox = (HBox) hbox_last_line.getChildren().get(1);
                                hbox_line.getChildren().add(old_hbox);
                            }
                            text_vbox.getChildren().remove(CURRENT_LINE + 1);

                            hbox_line.getChildren().get(length - 1).requestFocus();
                        }
                    }
                    break;
                case LEFT:
                    if (num > 0)
                        hbox_line.getChildren().get(num - 1).requestFocus();
                    else {
                        if (CURRENT_LINE != 0) {
                            CURRENT_LINE--;
                            hbox_line = (HBox) text_vbox.getChildren().get(CURRENT_LINE);
                            hbox_line.getChildren().get(hbox_line.getChildren().size() - 1).requestFocus();
                        }
                    }
                    break;
                case RIGHT:
                    if (num < length - 1)
                        hbox_line.getChildren().get(num + 1).requestFocus();
                    else {
                        if (CURRENT_LINE != text_vbox.getChildren().size() - 1) {
                            CURRENT_LINE++;
                            hbox_line = (HBox) text_vbox.getChildren().get(CURRENT_LINE);
                            hbox_line.getChildren().get(0).requestFocus();
                        }
                    }
                    break;
                case UP:
                    if (CURRENT_LINE > 0) {
                        CURRENT_LINE--;
                        hbox_line = (HBox) text_vbox.getChildren().get(CURRENT_LINE);
                        if (num >= hbox_line.getChildren().size())
                            hbox_line.getChildren().get(hbox_line.getChildren().size() - 1).requestFocus();
                        else
                            hbox_line.getChildren().get(num).requestFocus();
                    }
                    break;
                case DOWN:
                    if (CURRENT_LINE < text_vbox.getChildren().size() - 1) {
                        CURRENT_LINE++;
                        hbox_line = (HBox) text_vbox.getChildren().get(CURRENT_LINE);
                        if (num >= hbox_line.getChildren().size())
                            hbox_line.getChildren().get(hbox_line.getChildren().size() - 1).requestFocus();
                        else
                            hbox_line.getChildren().get(num).requestFocus();
                    }
                    break;
            }
        } else if (e.getEventType().toString().equals("KEY_TYPED") && pass) {
            Text input = new Text(e.getCharacter());
            input.setFont(Font.font("Consolas", FontWeight.NORMAL, 25));

            word = new HBox();
            word.getChildren().add(input);
            word.getStyleClass().add("text_border_none");

            if (num != length) num += 1;
            hbox_line.getChildren().add(num, word);

            setHboxFocus(word);
            setInputListener(word);
        } else {
            pass = true;
        }
    }

    public boolean isBorder(MouseEvent event) {
        Bounds dragNodeBounds = main_text.getBoundsInParent();
        Boolean top = (Math.abs(event.getY()) <= 15);
        Boolean bottom = (Math.abs(event.getY() - dragNodeBounds.getHeight()) <= 15);
        Boolean left = (Math.abs(event.getX()) <= 15);
        Boolean right = (Math.abs(event.getX() - dragNodeBounds.getWidth()) <= 15);

        return top || bottom || left || right;
    }

    public void setRequestFocus(HBox line, HBox last){
        line.requestFocus();
        clearSelectText(true);
        int num = last.getChildren().indexOf(line);
        select_text[0] = CURRENT_LINE;
        select_text[1] = num;
    }
}
