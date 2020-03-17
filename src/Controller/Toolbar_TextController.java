package Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.util.ArrayList;

public class Toolbar_TextController {
    private static Toolbar_TextController instance;
    ArrayList<HBox> text_hbox = null;
    Boolean press_bold;
    Boolean press_italic;
    Boolean press_underline;

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
    Button text_bold, text_italic, text_underline;

    @FXML
    public void initialize() {
        setInstance(this);

        text_bold.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            setBoldPressed(getInstance().text_hbox);
            FontPosture fontposture = null;
            if (getInstance().text_hbox != null) {
                for (HBox textHbox : getInstance().text_hbox) {
                    if (textHbox.getChildren().size() > 0) {
                        Text text = (Text) textHbox.getChildren().get(0);

                        if (text.getFont().getStyle().contains("Italic")) fontposture = FontPosture.ITALIC;
                        else fontposture = FontPosture.REGULAR;

                        if (getInstance().press_bold)
                            text.setFont(Font.font(text.getFont().getFamily(), FontWeight.NORMAL, fontposture, text.getFont().getSize()));
                        else
                            text.setFont(Font.font(text.getFont().getFamily(), FontWeight.BOLD, fontposture, text.getFont().getSize()));
                    }
                }
                text_hbox.get(0).requestFocus();
                setBoldPressed(getInstance().text_hbox);
            }
        });

        text_italic.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            setItalicPressed(getInstance().text_hbox);
            FontWeight fontweight = null;
            if (getInstance().text_hbox != null) {
                for (HBox textHbox : getInstance().text_hbox) {
                    if (textHbox.getChildren().size() > 0) {
                        Text text = (Text) textHbox.getChildren().get(0);

                        if (text.getFont().getStyle().contains("Bold")) fontweight = FontWeight.BOLD;
                        else fontweight = FontWeight.NORMAL;

                        if (getInstance().press_italic)
                            text.setFont(Font.font(text.getFont().getFamily(), fontweight, FontPosture.REGULAR, text.getFont().getSize()));
                        else
                            text.setFont(Font.font(text.getFont().getFamily(), fontweight, FontPosture.ITALIC, text.getFont().getSize()));
                    }
                }
                text_hbox.get(0).requestFocus();
                setItalicPressed(getInstance().text_hbox);
            }
        });

        text_underline.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            setUnderlinePressed(getInstance().text_hbox);
            if (getInstance().text_hbox != null) {
                for (HBox textHbox : getInstance().text_hbox) {
                    if (textHbox.getChildren().size() > 0) {
                        Text text = (Text) textHbox.getChildren().get(0);

                        if (getInstance().press_underline)
                            text.setUnderline(false);
                        else
                            text.setUnderline(true);
                    }
                }
                text_hbox.get(0).requestFocus();
                setUnderlinePressed(getInstance().text_hbox);
            }
        });
    }

    public void setCurentText(ArrayList<HBox> hbox) {
        getInstance().text_hbox = hbox;

        setBoldPressed(hbox);
        setItalicPressed(hbox);
        setUnderlinePressed(hbox);
    }

    public void setBoldPressed(ArrayList<HBox> hbox) {
        getInstance().text_bold.getStyleClass().remove("toolbar_sm_button_pressed");
        getInstance().press_bold = hbox.size() != 0;
        for (HBox hBox : hbox) {
            if (hBox.getChildren().size() > 0) {
                Text text = (Text) hBox.getChildren().get(0);
                if (!text.getFont().getStyle().contains("Bold")) {
                    getInstance().press_bold = false;
                    break;
                }
            }
        }

        if (getInstance().press_bold)
            if (!getInstance().text_bold.getStyleClass().contains("toolbar_sm_button_pressed"))
                getInstance().text_bold.getStyleClass().add("toolbar_sm_button_pressed");
    }

    public void setItalicPressed(ArrayList<HBox> hbox) {
        getInstance().text_italic.getStyleClass().remove("toolbar_sm_button_pressed");
        getInstance().press_italic = hbox.size() != 0;
        for (HBox hBox : hbox) {
            if (hBox.getChildren().size() > 0) {
                Text text = (Text) hBox.getChildren().get(0);
                if (!text.getFont().getStyle().contains("Italic")) {
                    getInstance().press_italic = false;
                    break;
                }
            }
        }
        if (getInstance().press_italic)
            if (!getInstance().text_italic.getStyleClass().contains("toolbar_sm_button_pressed"))
                getInstance().text_italic.getStyleClass().add("toolbar_sm_button_pressed");
    }

    public void setUnderlinePressed(ArrayList<HBox> hbox) {
        getInstance().text_underline.getStyleClass().remove("toolbar_sm_button_pressed");
        getInstance().press_underline = hbox.size() != 0;
        for (HBox hBox : hbox) {
            if (hBox.getChildren().size() > 0) {
                Text text = (Text) hBox.getChildren().get(0);
                if (!text.isUnderline()) {
                    getInstance().press_underline = false;
                    break;
                }
            }
        }
        if (getInstance().press_underline)
            if (!getInstance().text_underline.getStyleClass().contains("toolbar_sm_button_pressed"))
                getInstance().text_underline.getStyleClass().add("toolbar_sm_button_pressed");
    }

    public void clearCurentText() {
        getInstance().text_hbox = null;
        getInstance().text_bold.getStyleClass().remove("toolbar_sm_button_pressed");
        getInstance().text_italic.getStyleClass().remove("toolbar_sm_button_pressed");
        getInstance().text_underline.getStyleClass().remove("toolbar_sm_button_pressed");
    }
}
