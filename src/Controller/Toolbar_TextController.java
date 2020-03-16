package Controller;

import InsertObj.Paper;
import InsertObj.Text_box;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.ArrayList;

public class Toolbar_TextController {
    private static Toolbar_TextController instance;
    ArrayList<HBox> text_hbox = null;
    Boolean press;

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
    Button text_bold;

    @FXML
    public void initialize() {
        setInstance(this);

        text_bold.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if (getInstance().text_hbox != null) {
                for (HBox textHbox : getInstance().text_hbox) {
                    if (textHbox.getChildren().size() > 0) {
                        Text text = (Text) textHbox.getChildren().get(0);
                        System.out.println(text);
                        if (getInstance().press)
                            text.getStyleClass().remove("text_bold");
                        else if (!text.getStyleClass().contains("text_bold")) text.getStyleClass().add("text_bold");
                    }
                }
            }
        });
    }

    public void setCurentText(ArrayList<HBox> hbox) {
        getInstance().text_hbox = hbox;
        getInstance().text_bold.getStyleClass().remove("toolbar_sm_button_pressed");

        getInstance().press = true;
        for (HBox hBox : hbox) {
            if (hBox.getChildren().size() > 0) {
                Text text = (Text) hBox.getChildren().get(0);
                if (!text.getStyleClass().contains("text_bold")) {
                    getInstance().press = false;
                    break;
                }
            }
        }

        if (getInstance().press) getInstance().text_bold.getStyleClass().add("toolbar_sm_button_pressed");
    }

    public void clearCurentText() {
        getInstance().text_hbox = null;
    }
}
