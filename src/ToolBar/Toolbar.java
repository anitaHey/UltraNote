package ToolBar;

import Controller.ToolbarController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.HBox;

public class Toolbar extends HBox {
    ToolbarController controller;
    public enum Type{
        FileFXML("../FXML/Toolbar_FileFxml.fxml"),
        ViewFXML("../FXML/Toolbar_ViewFxml.fxml"),
        InsertFXML("../FXML/Toolbar_InsertFxml.fxml"),
        TextFXML("../FXML/Toolbar_TextFxml.fxml"),
        DrawFXML("../FXML/Toolbar_DrawFxml.fxml");

        private final String URL;

        Type(String url){
            this.URL = url;
        }
    }

    public Toolbar(Type type){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource(type.URL));
            controller = new ToolbarController();
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        }catch (Exception e){
            System.out.println(e);
        }
    }

//    public void change(Type type){
//        try{
//            FXMLLoader loader = new FXMLLoader(getClass().getResource(type.URL));
//            controller = new ToolbarController();
//            loader.setController(this);
//            loader.setRoot(this);
//            loader.load();
//        }catch (Exception e){
//            System.out.println(e);
//        }
//    }
}
