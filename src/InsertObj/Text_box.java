package InsertObj;

import Controller.MainController;
import Controller.Toolbar_TextController;
import Object.TextLine;
import Object.TextObj;
import Object.TextUnderline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Text_box extends Pane {
    TextLine line;
    private int CURRENT_LINE = 0;
    TextLine hbox_line;
    boolean pass = true;
    int[] select_text = {-1, -1};
    ArrayList<TextObj> select_text_hbox = new ArrayList<>();
    Toolbar_TextController controller = Toolbar_TextController.getInstance();
    Timer timer;

    public Text_box(){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../FXML/TextBoxFxml.fxml"));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        } catch (Exception e){
            System.out.println(e);
        }

    }

    @FXML
    Pane main_text;
    @FXML
    VBox text_vbox;

    @FXML
    public void initialize() {
        line = new TextLine();
        text_vbox.getChildren().add(line);

        setInputFocus(line.getIndex(0));
        setInputListener(line.getIndex(0));

        Platform.runLater(() -> line.getIndex(0).requestFocus());
        MainController.getInstance().change_toolbar(MainController.Type.Text,false);

        main_text.setOnMouseClicked(e->{
            focus_border(true);
            checkClickLine(e);
            MainController.getInstance().change_toolbar(MainController.Type.Text,false);
        });

        main_text.setOnDragDetected(e -> {
            focus_border(true);
        });

        main_text.addEventHandler(MouseEvent.MOUSE_DRAGGED, e -> {
            if (!isBorder(e)) {
                for (int a = 0; a < getLineSize(); a++) {
                    TextLine tem = getLine(a);
                    for (int b = 0; b < tem.getHBoxSize(); b++) {
                        Node sm_tem = tem.getIndex(b);

                        Bounds bound_tem = sm_tem.localToParent(tem.getBoundsInParent());
                        if (bound_tem.contains(e.getX(), e.getY())) addTextSelect(a, b);
                    }
                }
            }
        });

        new Draggable.Move(main_text);
    }

    public TextLine getLine(int index){
        return (TextLine)text_vbox.getChildren().get(index);
    }
    public int getLineSize(){ return text_vbox.getChildren().size(); }

    public void addTextSelect(int line, int num) {
        if (select_text[0] != -1) {
            clearSelectText(false);

            if (line == select_text[0]) {
                if (num > select_text[1])
                    for (int a = select_text[1] + 1; a <= num; a++)
                        try {
                            select_text_hbox.add(getLine(line).getIndex(a));
                        } catch (Exception e) {
                            System.out.println(e);
                        }
                else if (num < select_text[1])
                    for (int a = num; a <= select_text[1]; a++)
                        select_text_hbox.add(getLine(line).getIndex(a));
                else
                    select_text_hbox.add(getLine(line).getIndex(num));
            } else if (line < select_text[0]) {
                TextLine hbox = getLine(select_text[0]);

                for (int a = 0; a <= select_text[1]; a++)
                    select_text_hbox.add(hbox.getIndex(a));

                for (int a = line + 1; a < select_text[0]; a++) {
                    hbox = getLine(a);
                    for (int b = 0; b < hbox.getHBoxSize(); b++)
                        select_text_hbox.add(hbox.getIndex(b));
                }
                hbox = getLine(line);
                for (int a = num; a < hbox.getHBoxSize(); a++)
                    select_text_hbox.add(hbox.getIndex(a));
            } else {
                TextLine hbox = getLine(select_text[0]);

                for (int a = select_text[1] + 1; a < hbox.getHBoxSize(); a++)
                    select_text_hbox.add(hbox.getIndex(a));

                for (int a = select_text[0] + 1; a < line; a++) {
                    hbox = getLine(a);
                    for (int b = 0; b < hbox.getHBoxSize(); b++)
                        select_text_hbox.add(hbox.getIndex(b));
                }
                hbox = getLine(line);
                for (int a = 0; a <= num; a++)
                    select_text_hbox.add(hbox.getIndex(a));
            }
        }
        for (Node selectTextHbox : select_text_hbox){
            selectTextHbox.getStyleClass().add("text_select");
        }
        controller.setCurrentText(select_text_hbox);
    }

    public void focus_border(boolean show) {
        if (show) {
            main_text.getStyleClass().add("text_border_focus");
        } else {
            main_text.getStyleClass().clear();
        }
    }

    public void setInputFocus(TextObj text) {
        text.setOnMouseClicked(e -> {
            hbox_line = getLine(CURRENT_LINE);

            setRequestFocus(text, hbox_line);
        });

        text.focusedProperty().addListener((observable, wasFocused, focused) -> {
            if (focused) {
                focus_border(true);
                text.getStyleClass().add("text_bg_input");
                text.getStyleClass().remove("text_bg_none");
                setTextInputAnimation(text);
            } else {
                focus_border(false);
                text.getStyleClass().remove("text_bg_input");
                text.getStyleClass().add("text_bg_none");
                if (timer != null)
                    timer.cancel();
            }
        });

        text.requestFocus();
    }

    public void setInputListener(TextObj word_pane) {
        word_pane.addEventFilter(KeyEvent.ANY, e -> {
            input(e, word_pane);
        });
    }

    public void checkClickLine(MouseEvent event) {
        double input_x = event.getScreenX();
        double input_y = event.getScreenY();

        for (int a = 0; a < getLineSize(); a++) {
            TextLine tem = getLine(a);
            Bounds bound_tem = tem.localToScreen(tem.getBoundsInLocal());

            if (input_y >= bound_tem.getMinY() && input_y <= bound_tem.getMaxY()) {
                CURRENT_LINE = a;
                TextObj last = tem.getIndex(tem.getHBoxSize() -1);
                Bounds bound_last = last.localToScreen(last.getBoundsInLocal());
                if (input_x >= bound_last.getMaxX())
                    setRequestFocus(last, tem);
                break;
            }
        }
    }

    public synchronized void input(KeyEvent e, TextObj word_pane) {
        hbox_line = getLine(CURRENT_LINE);
        int num = hbox_line.getTextIndex(word_pane);
        int length = hbox_line.getHBoxSize();
        if (num == -1) num = 0;

        if (e.getEventType().toString().equals("KEY_PRESSED")) {
            switch (e.getCode()) {
                case ENTER:
                    pass = false;
                    line = new TextLine();

                    if (num != length - 1) {
                        for (int a = length - 1; a > num; a--) {
                            TextObj old_node = hbox_line.getIndex(a);
                            line.addWord(old_node, old_node.getUnderlineObj(), 1);
                        }
                    }

                    CURRENT_LINE++;
                    text_vbox.getChildren().add(CURRENT_LINE, line);

                    setInputFocus(line.getIndex(0));
                    setInputListener(line.getIndex(0));
                    line.getIndex(0).requestFocus();
                    break;
                case BACK_SPACE:
                    pass = false;
                    if (num > 0) {
                        int lastword = num - 1;
                        Platform.runLater(() -> hbox_line.getIndex(lastword).requestFocus());

                        hbox_line.removeIndex(num);
                    } else {
                        if (CURRENT_LINE != 0) {
                            TextLine last_line = getLine(CURRENT_LINE - 1);

                            for (int a = 1; a < length; a++) {
                                TextObj old_node = hbox_line.getIndex(1);
                                last_line.addWord(old_node, old_node.getUnderlineObj(), -1);
                            }

                            text_vbox.getChildren().remove(CURRENT_LINE);
                            CURRENT_LINE--;
                            last_line.getIndex(last_line.getHBoxSize()-1).requestFocus();
                        }
                    }
                    break;
                case DELETE:
                    pass = false;
                    if (num < length - 1) {
                        hbox_line.removeIndex(num+1);
                    } else {
                        if (CURRENT_LINE != text_vbox.getChildren().size() - 1) {
                            TextLine last_line = getLine(CURRENT_LINE + 1);
                            int last_length = last_line.getHBoxSize();
                            for (int a = 1; a < last_length; a++) {
                                TextObj old_node = last_line.getIndex(1);
                                hbox_line.addWord(old_node, old_node.getUnderlineObj(), -1);
                            }

                            text_vbox.getChildren().remove(CURRENT_LINE + 1);
                            hbox_line.getIndex(length - 1).requestFocus();
                        }
                    }
                    break;
                case RIGHT:
                    if (num < length - 1)
                        hbox_line.getIndex(num + 1).requestFocus();
                    else {
                        if (CURRENT_LINE != text_vbox.getChildren().size() - 1) {
                            CURRENT_LINE++;
                            hbox_line = getLine(CURRENT_LINE);
                            hbox_line.getIndex(0).requestFocus();
                        }
                    }
                    break;
                case LEFT:
                    if (num > 0)
                        hbox_line.getIndex(num - 1).requestFocus();
                    else {
                        if (CURRENT_LINE != 0) {
                            CURRENT_LINE--;
                            hbox_line =getLine(CURRENT_LINE);
                            hbox_line.getIndex(hbox_line.getHBoxSize() - 1).requestFocus();
                        }
                    }
                    break;
                case UP:
                    if (CURRENT_LINE > 0) {
                        CURRENT_LINE--;
                        hbox_line = getLine(CURRENT_LINE);
                        if (num >= hbox_line.getHBoxSize())
                            hbox_line.getIndex(hbox_line.getHBoxSize() - 1).requestFocus();
                        else
                            hbox_line.getIndex(num).requestFocus();
                    }
                    break;
                case DOWN:
                    if (CURRENT_LINE < getLineSize() - 1) {
                        CURRENT_LINE++;
                        hbox_line = getLine(CURRENT_LINE);
                        if (num >= hbox_line.getHBoxSize())
                            hbox_line.getIndex(hbox_line.getHBoxSize() - 1).requestFocus();
                        else
                            hbox_line.getIndex(num).requestFocus();
                    }
                    break;
            }
        } else if (e.getEventType().toString().equals("KEY_TYPED") && pass) {
            TextObj input = new TextObj(e.getCharacter());
            TextUnderline under = new TextUnderline();
            input.setUnderlineObj(under);

            if (num != length) num += 1;
            hbox_line.addWord(input, input.getUnderlineObj(), num);
            setInputFocus(input);
            setInputListener(input);
        } else {
            pass = true;
        }
    }

    public void setRequestFocus(TextObj line, TextLine last){
        line.requestFocus();
        clearSelectText(true);
        select_text[0] = CURRENT_LINE;
        select_text[1] = last.getTextIndex(line);
    }

    public void setTextInputAnimation(TextObj word) {
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

    public boolean isBorder(MouseEvent event) {
        Bounds dragNodeBounds = main_text.getBoundsInParent();
        Boolean top = (Math.abs(event.getY()) <= 15);
        Boolean bottom = (Math.abs(event.getY() - dragNodeBounds.getHeight()) <= 15);
        Boolean left = (Math.abs(event.getX()) <= 15);
        Boolean right = (Math.abs(event.getX() - dragNodeBounds.getWidth()) <= 15);
        return top || bottom || left || right;
    }

    public void clearSelectText(boolean all) {
        for (TextObj selectTextHbox : select_text_hbox) selectTextHbox.getStyleClass().remove("text_select");
        select_text_hbox.clear();
        controller.clearCurentText();
        if (all) select_text[0] = -1;
    }
}
