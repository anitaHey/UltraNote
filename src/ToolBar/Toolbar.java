package ToolBar;

import Controller.ToolbarController;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;

public class Toolbar extends HBox {

    public Toolbar(){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../FXML/ToolbarFxml.fxml"));
            ToolbarController controller = new ToolbarController();
            loader.setController(controller);
            loader.setRoot(this);
            loader.load();
        }catch (Exception e){

        }
    }
}
