package Controller;

import InsertObj.Paper;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javax.tools.Tool;
import java.io.IOException;

public class MainController {
    private static MainController instance;
    private Stage stage;
    private int toolbar_height = 138;

    @FXML
    Button exit, toolbar_file, toolbar_view, toolbar_insert, toolbar_text, toolbar_draw, toolbar_picture;
    @FXML
    VBox main_vbox, toolbar_vbox, work_vbox;
    @FXML
    ScrollPane work_scroll;

    int toolbar = -1;

    public static MainController getInstance() {
        if (instance == null) {
            instance = new MainController();
        }
        return instance;
    }

    public static void setInstance(MainController newInstance) {
        instance = newInstance;
    }

    public void setStage(Stage stage) {
        getInstance().stage = stage;
    }

    public Stage getStage() {
        return stage;
    }

    public enum Type {
        File(1, "../FXML/Toolbar_FileFxml.fxml", new Toolbar_FileController()),
        View(2, "../FXML/Toolbar_ViewFxml.fxml", new Toolbar_ViewController()),
        Insert(3, "../FXML/Toolbar_InsertFxml.fxml", new Toolbar_InsertController()),
        Text(4, "../FXML/Toolbar_TextFxml.fxml", new Toolbar_TextController()),
        Picture(5, "../FXML/Toolbar_PictureFxml.fxml", new Toolbar_PictureController()),
        Draw(6, "../FXML/Toolbar_DrawFxml.fxml", new Toolbar_DrawController());

        private final int id;
        private Button btn;
        private final String URL;
        private final Object controller;
        private HBox toolbarUI;

        Type(int id, String url, Object controller) {
            this.id = id;
            this.URL = url;
            this.controller = controller;

            try {
                toolbarUI = FXMLLoader.load(getClass().getResource(URL));
            } catch (Exception e) {
                System.out.println(e);
            }
        }

        public void setBtn(Button btn) {
            this.btn = btn;
        }

        public String getURL() {
            return URL;
        }

        public HBox getToolbarUI() {
            return toolbarUI;
        }

        public Object getController() {
            return controller;
        }

        public int getId() {
            return id;
        }

        public Button getButton() {
            return btn;
        }
    }

    public void Init() {
        setInstance(this);

        Type.File.setBtn(toolbar_file);
        Type.View.setBtn(toolbar_view);
        Type.Insert.setBtn(toolbar_insert);
        Type.Text.setBtn(toolbar_text);
        Type.Picture.setBtn(toolbar_picture);
        Type.Draw.setBtn(toolbar_draw);

        exit.setOnAction(actionEvent -> Platform.exit());

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
        toolbar_picture.setOnAction(actionEvent -> {
            change_toolbar(Type.Picture, true);
        });
        toolbar_draw.setOnAction(actionEvent -> {
            change_toolbar(Type.Draw, true);
        });

        work_scroll.getStyleClass().add("word_area");
        work_scroll.getStyleClass().add("no_focus");
    }

    public void setSize() {
        getStage().widthProperty().addListener((ob, oldValue, newValue) -> {
            main_vbox.setPrefWidth(newValue.doubleValue());
            exit.setLayoutX(newValue.doubleValue() - 41);
        });

        getStage().heightProperty().addListener((ob, oldValue, newValue) -> {
            main_vbox.setPrefHeight(newValue.doubleValue());
            if (toolbar == -1) {
                work_scroll.setPrefHeight(newValue.doubleValue() - 70);
            } else {
                work_scroll.setPrefHeight(newValue.doubleValue() - (toolbar_height + 70));
            }

        });
    }

    public void addPaper(double width, double height) {
        work_scroll.setPrefHeight(height - 70);
        Paper paper = new Paper(width, height);
        work_vbox.getChildren().add(paper);
    }

    public void change_toolbar(Type type, boolean close) {
        if (toolbar == type.getId()) {
            if (close) {
                toolbar = -1;
                toolbar_vbox.getChildren().remove(2);
                work_scroll.setPrefHeight(work_scroll.getPrefHeight() + toolbar_height);
                type.getButton().getStyleClass().remove("toolbar_button_press");
            }
        } else if (toolbar == -1) {
            toolbar = type.getId();
            toolbar_vbox.getChildren().add(2, type.getToolbarUI());

            work_scroll.setPrefHeight(work_scroll.getPrefHeight() - toolbar_height);
            type.getButton().getStyleClass().add("toolbar_button_press");
        } else {
            toolbar_file.getStyleClass().remove("toolbar_button_press");
            toolbar_view.getStyleClass().remove("toolbar_button_press");
            toolbar_insert.getStyleClass().remove("toolbar_button_press");
            toolbar_text.getStyleClass().remove("toolbar_button_press");
            toolbar_draw.getStyleClass().remove("toolbar_button_press");
            toolbar_picture.getStyleClass().remove("toolbar_button_press");

            toolbar = type.getId();
            toolbar_vbox.getChildren().remove(2);
            toolbar_vbox.getChildren().add(2, type.getToolbarUI());
            type.getButton().getStyleClass().add("toolbar_button_press");
        }
    }
}
