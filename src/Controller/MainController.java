package Controller;

import InsertObj.Paper;
import InsertObj.Text_box;
import ToolBar.Toolbar;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class MainController {
    private static MainController instance;

    @FXML
    Button exit, toolbar_file, toolbar_view, toolbar_insert, toolbar_text, toolbar_draw;
    @FXML
    VBox main_vbox, toolbar_vbox;
    @FXML
    ScrollPane work_scroll;
    @FXML
    Pane paper_pane;

    Toolbar toolbar_tool;
    int toolbar = -1;

    public static MainController getInstance(){
        if(instance == null){
            instance = new MainController();
        }
        return instance;
    }

    public static void setInstance(MainController newInstance){
        instance = newInstance;
    }

    public enum Type{
        File(1,"../FXML/Toolbar_FileFxml.fxml", new Toolbar_FileController()),
        View(2,"../FXML/Toolbar_ViewFxml.fxml", new Toolbar_ViewController()),
        Insert(3,"../FXML/Toolbar_InsertFxml.fxml", new Toolbar_InsertController()),
        Text(4,"../FXML/Toolbar_TextFxml.fxml", new Toolbar_TextController()),
        Draw(5,"../FXML/Toolbar_DrawFxml.fxml", new Toolbar_DrawController());

        private final int id;
        private Button btn;
        private final String URL;
        private final Object controller;

        Type(int id, String url, Object controller){
            this.id = id;
            this.URL = url;
            this.controller = controller;
        }

        public void setBtn(Button btn){
            this.btn = btn;
        }

        public String getURL(){
            return URL;
        }

        public Object getController(){
            return controller;
        }

        public int getId(){
            return id;
        }

        public Button getButton(){
            return btn;
        }
    }

    public void Init() {
        setInstance(this);

        Type.File.setBtn(toolbar_file);
        Type.View.setBtn(toolbar_view);
        Type.Insert.setBtn(toolbar_insert);
        Type.Text.setBtn(toolbar_text);
        Type.Draw.setBtn(toolbar_draw);

        exit.setOnAction(actionEvent -> Platform.exit());
//        toolbar_tool = new Toolbar();
        toolbar_file.setOnAction(actionEvent -> {
            change_toolbar(Type.File, true);
        });
        toolbar_view.setOnAction(actionEvent -> {
            change_toolbar(Type.View, true);
        });
        toolbar_insert.setOnAction(actionEvent -> {
            change_toolbar(Type.Insert, true);
        });
        toolbar_text.setOnAction(actionEvent -> {
            change_toolbar(Type.Text, true);
        });
        toolbar_draw.setOnAction(actionEvent -> {
            change_toolbar(Type.Draw, true);
        });

        Paper.setCurentPaper(paper_pane);
    }

    public void change_toolbar(Type type, boolean close){
        if(toolbar == type.getId()) {
            if(close){
                toolbar = -1;
                toolbar_vbox.getChildren().remove(2);
                work_scroll.setPrefHeight(615);
                type.getButton().getStyleClass().remove("toolbar_button_press");
            }
        }else if(toolbar == -1){
            toolbar = type.getId();
            toolbar_tool = new Toolbar(type);
//            toolbar_tool.change(type);
            toolbar_vbox.getChildren().add(2, toolbar_tool);
            work_scroll.setPrefHeight(500);
            type.getButton().getStyleClass().add("toolbar_button_press");
        } else {
            toolbar_file.getStyleClass().remove("toolbar_button_press");
            toolbar_view.getStyleClass().remove("toolbar_button_press");
            toolbar_insert.getStyleClass().remove("toolbar_button_press");
            toolbar_text.getStyleClass().remove("toolbar_button_press");
            toolbar_draw.getStyleClass().remove("toolbar_button_press");

            toolbar = type.getId();
            toolbar_tool = new Toolbar(type);
            toolbar_vbox.getChildren().remove(2);
            toolbar_vbox.getChildren().add(2, toolbar_tool);
//            toolbar_tool.change(type);
            type.getButton().getStyleClass().add("toolbar_button_press");
        }
    }

    public void addText(Text_box input){
        paper_pane.getChildren().add(input);
    }
}
