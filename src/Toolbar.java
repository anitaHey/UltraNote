import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class Toolbar extends HBox {
    private Controller controller = Controller.getInstance();

    public Toolbar(){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ToolbarFxml.fxml"));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        }catch (Exception e){

        }
    }

    @FXML
    VBox toolbar_insert_text;
    @FXML
    public void initialize() {
        toolbar_insert_text.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            Text_box tem = new Text_box();
            controller.addText(tem);
        });
    }
}
