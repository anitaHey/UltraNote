import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class Toolbar extends HBox {

    public Toolbar(){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ToolbarFxml.fxml"));
            ToolbarController controller = new ToolbarController();
            loader.setController(controller);
            loader.setRoot(this);
            loader.load();
        }catch (Exception e){

        }
    }
}
