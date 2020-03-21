package ToolBar;

import Controller.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;

public class Toolbar extends HBox {
    public Toolbar(MainController.Type type){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource(type.getURL()));
            loader.setController(type.getController());
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