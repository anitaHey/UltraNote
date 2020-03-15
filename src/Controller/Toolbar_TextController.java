package Controller;

import InsertObj.Text_box;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public class Toolbar_TextController {
    private MainController controller = MainController.getInstance();
    @FXML
    Button text_bold;

    @FXML
    public void initialize() {
        text_bold.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {

        });
    }
}
