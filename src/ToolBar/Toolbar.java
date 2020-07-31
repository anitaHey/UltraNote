package ToolBar;

import Controller.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;

public class Toolbar extends HBox {
    FXMLLoader loader;
    public Toolbar(){
//        try{
//            FXMLLoader loader = new FXMLLoader(getClass().getResource(type.getURL()));
//            loader.setController(type.getController());
//            loader.setRoot(this);
//            loader.load();
//        }catch (Exception e){
//            System.out.println(e);
//        }
    }

    public void change(MainController.Type type){
        try{
            loader = new FXMLLoader(getClass().getResource(type.getURL()));
            loader.setController(type.getController());
//            loader.setRoot(this);
            loader.load();
        }catch (Exception e){
            System.out.println(e);
        }
    }
}
