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

    public void Init() {
        setInstance(this);

        exit.setOnAction(actionEvent -> Platform.exit());
//        toolbar_tool = new Toolbar();
        toolbar_file.setOnAction(actionEvent -> {
            change_toolbar(1, toolbar_file, Toolbar.Type.File);
        });
        toolbar_view.setOnAction(actionEvent -> {
            change_toolbar(2, toolbar_view, Toolbar.Type.View);
        });
        toolbar_insert.setOnAction(actionEvent -> {
            change_toolbar(3, toolbar_insert, Toolbar.Type.Insert);
        });
        toolbar_text.setOnAction(actionEvent -> {
            change_toolbar(4, toolbar_text, Toolbar.Type.Text);
        });
        toolbar_draw.setOnAction(actionEvent -> {
            change_toolbar(5, toolbar_draw, Toolbar.Type.Draw);
        });

        Paper.setCurentPaper(paper_pane);
    }

    public void change_toolbar(int tool, Button press, Toolbar.Type type){
        if(toolbar == tool) {
            toolbar = -1;
            toolbar_vbox.getChildren().remove(2);
            work_scroll.setPrefHeight(615);
            press.getStyleClass().remove("toolbar_button_press");
        }else if(toolbar == -1){
            toolbar = tool;
            toolbar_tool = new Toolbar(type);
//            toolbar_tool.change(type);
            toolbar_vbox.getChildren().add(2, toolbar_tool);
            work_scroll.setPrefHeight(500);
            press.getStyleClass().add("toolbar_button_press");
        } else {
            toolbar_file.getStyleClass().remove("toolbar_button_press");
            toolbar_view.getStyleClass().remove("toolbar_button_press");
            toolbar_insert.getStyleClass().remove("toolbar_button_press");
            toolbar_text.getStyleClass().remove("toolbar_button_press");
            toolbar_draw.getStyleClass().remove("toolbar_button_press");

            toolbar = tool;
            toolbar_tool = new Toolbar(type);
            toolbar_vbox.getChildren().remove(2);
            toolbar_vbox.getChildren().add(2, toolbar_tool);
//            toolbar_tool.change(type);
            press.getStyleClass().add("toolbar_button_press");
        }
    }

    public void addText(Text_box input){
        paper_pane.getChildren().add(input);
    }
}
