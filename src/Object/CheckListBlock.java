package Object;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

public class CheckListBlock extends HBox {
    public CheckListBlock(){
        try {
            FXMLLoader loader;
            loader = new FXMLLoader(getClass().getResource("../FXML/CheckListBlockFxml.fxml"));

            loader.setController(this);
            loader.setRoot(this);
            loader.load();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @FXML
    CheckBox check;
    @FXML
    Pane pane;

    @FXML
    public void initialize() {

    }
}
