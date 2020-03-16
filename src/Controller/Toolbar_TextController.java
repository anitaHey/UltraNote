package Controller;

import InsertObj.Paper;
import InsertObj.Text_box;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Toolbar_TextController {
    private static Toolbar_TextController instance;
    ArrayList<HBox> text_hbox = null;
    Boolean press_bold;
    Boolean press_italic;

    public static Toolbar_TextController getInstance() {
        if (instance == null) {
            instance = new Toolbar_TextController();
        }
        return instance;
    }

    public static void setInstance(Toolbar_TextController newInstance) {
        instance = newInstance;
    }

    @FXML
    Button text_bold, text_italic;

    @FXML
    public void initialize() {
        setInstance(this);

//        press.put("bold",false);
//        press.put("italic",false);

        text_bold.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            setBoldPressed(getInstance().text_hbox);
            if (getInstance().text_hbox != null) {
                for (HBox textHbox : getInstance().text_hbox) {
                    if (textHbox.getChildren().size() > 0) {
                        Text text = (Text) textHbox.getChildren().get(0);
                        if (getInstance().press_bold)
                            text.getStyleClass().remove("text_bold");
                        else if (!text.getStyleClass().contains("text_bold")){
                            text.getStyleClass().add("text_bold");
                        }
                        System.out.println(text);
                    }
                }
                text_hbox.get(0).requestFocus();
                setBoldPressed(getInstance().text_hbox);
            }
        });

        text_italic.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            setItalicPressed(getInstance().text_hbox);
            if (getInstance().text_hbox != null) {
                for (HBox textHbox : getInstance().text_hbox) {
                    if (textHbox.getChildren().size() > 0) {
                        Text text = (Text) textHbox.getChildren().get(0);
                        if (getInstance().press_italic)
                            text.setFont(Font.font(text.getFont().getFamily(), FontPosture.REGULAR, text.getFont().getSize()));
                        else {
                            text.setFont(Font.font(text.getFont().getFamily(), FontPosture.ITALIC, text.getFont().getSize()));
//                            text.getStyleClass().add("text_italic");

//                            text.setFont(Font.font("serif", FontPosture.ITALIC, 14));
                        }
//                        System.out.println(text);
                    }
                }
                text_hbox.get(0).requestFocus();
                setItalicPressed(getInstance().text_hbox);
            }
        });
    }

    public void setCurentText(ArrayList<HBox> hbox) {
        getInstance().text_hbox = hbox;

        setBoldPressed(hbox);
        setItalicPressed(hbox);
    }

    public void setBoldPressed(ArrayList<HBox> hbox){
        getInstance().text_bold.getStyleClass().remove("toolbar_sm_button_pressed");
        getInstance().press_bold = hbox.size() != 0;
        for (HBox hBox : hbox) {
            if (hBox.getChildren().size() > 0) {
                Text text = (Text) hBox.getChildren().get(0);
                if (!text.getStyleClass().contains("text_bold")) {
                    getInstance().press_bold = false;
                    break;
                }
            }
        }

        if (getInstance().press_bold)
            if(!getInstance().text_bold.getStyleClass().contains("toolbar_sm_button_pressed"))
                getInstance().text_bold.getStyleClass().add("toolbar_sm_button_pressed");
    }

    public void setItalicPressed(ArrayList<HBox> hbox){
        getInstance().text_italic.getStyleClass().remove("toolbar_sm_button_pressed");
        getInstance().press_italic = hbox.size() != 0;
        for (HBox hBox : hbox) {
            if (hBox.getChildren().size() > 0) {
                Text text = (Text) hBox.getChildren().get(0);
                if(!text.getFont().getStyle().contains("Italic")){
//                if (!text.getStyleClass().contains("text_italic")) {
                    getInstance().press_italic = false;
                    break;
                }
            }
        }
        if (getInstance().press_italic)
            if(!getInstance().text_italic.getStyleClass().contains("toolbar_sm_button_pressed"))
                getInstance().text_italic.getStyleClass().add("toolbar_sm_button_pressed");
    }

    public void clearCurentText() {
        getInstance().text_hbox = null;
    }
}
