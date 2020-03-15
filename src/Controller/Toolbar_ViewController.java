package Controller;

import InsertObj.Text_box;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public class Toolbar_ViewController {
    private MainController controller = MainController.getInstance();
    @FXML
    VBox toolbar_insert_text;

    @FXML
    public void initialize() {
        toolbar_insert_text.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            Text_box tem = new Text_box();
            tem.getStyleClass().add("text_border_focus");
            tem.requestFocus();
            controller.addText(tem);
        });
    }
}
