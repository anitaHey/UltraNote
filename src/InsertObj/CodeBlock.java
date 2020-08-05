package InsertObj;

import Controller.MainController;
import Object.CodeBlockArea;
import javafx.scene.shape.Circle;
import org.fxmisc.flowless.VirtualizedScrollPane;

public class CodeBlock extends BasicNode {
    private CodeBlockArea codeArea;

    public CodeBlock() {
        super("code");
        setHasMin(false);

        codeArea = new CodeBlockArea();
        codeArea.getStyleClass().add("code_block_style");

        this.getMain_content().getChildren().add(new VirtualizedScrollPane<>(codeArea));
        this.setMinH(150, false);
        this.setMinW(500, false);

        MainController.getInstance().change_toolbar(MainController.Type.Code, false);

        getMain_content().heightProperty().addListener((obs, oldValue, newValue) -> {
            codeArea.setAreaPrefHeight(newValue.doubleValue());
        });

        getMain_content().widthProperty().addListener((obs, oldValue, newValue) -> {
            codeArea.setAreaPrefWidth(newValue.doubleValue());
        });

        this.getStylesheets().add("css/JavaKeyword.css");

        this.setOnMouseClicked(e -> {
            MainController.getInstance().change_toolbar(MainController.Type.Code, false);
        });
    }

    @Override
    public void focus_border(boolean show) {
        if (show) {
            getGridpane().getStyleClass().clear();
            getGridpane().getStyleClass().add("border_focus");

            for (Circle cir : cir_arr) {
                cir.getStyleClass().clear();
                cir.getStyleClass().add("show");
            }
            this.getStylesheets().remove("css/codeNoFocus.css");
        } else {
            getGridpane().getStyleClass().clear();
            getGridpane().getStyleClass().add("border_none");

            for (Circle cir : cir_arr) {
                cir.getStyleClass().clear();
                cir.getStyleClass().add("hide");
            }
            this.getStylesheets().add("css/codeNoFocus.css");
        }
    }
}
