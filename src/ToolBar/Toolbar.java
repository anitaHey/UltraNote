package ToolBar;

import Controller.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;

public class Toolbar extends HBox {
    public enum Type{
        File("../FXML/Toolbar_FileFxml.fxml", new Toolbar_FileController()),
        View("../FXML/Toolbar_ViewFxml.fxml", new Toolbar_ViewController()),
        Insert("../FXML/Toolbar_InsertFxml.fxml", new Toolbar_InsertController()),
        Text("../FXML/Toolbar_TextFxml.fxml", new Toolbar_TextController()),
        Draw("../FXML/Toolbar_DrawFxml.fxml", new Toolbar_DrawController());

        private final String URL;
        private final Object controller;

        Type(String url, Object controller){
            this.URL = url;
            this.controller = controller;
        }
    }

    public Toolbar(Type type){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource(type.URL));
            loader.setController(type.controller);
            loader.setRoot(this);
            loader.load();
        }catch (Exception e){
//            System.out.println(e);
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
