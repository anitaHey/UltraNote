package InsertObj;

import Controller.MainController;
import Controller.PaperController;
import Controller.Toolbar_CodeController;
import Object.CodeBlockArea;
import Object.CodeResultBlock;
import javafx.scene.shape.Circle;
import org.fxmisc.flowless.VirtualizedScrollPane;

public class CodeBlock extends BasicNode {
    private Toolbar_CodeController code_controller = Toolbar_CodeController.getInstance();
    private PaperController paper_controller = PaperController.getInstance();

    private CodeBlockArea codeArea;
    private CodeResultBlock resultBlock;
    private String className;
    private boolean isShowResult;

    public CodeBlock() {
        super("code");
        setHasMin(false);

        codeArea = new CodeBlockArea();
        codeArea.getStyleClass().add("code_block_style");

        resultBlock = new CodeResultBlock();

        this.getMain_content().getChildren().add(new VirtualizedScrollPane<>(codeArea));
        this.setMinH(155, false);
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
            code_controller.setNoSet(true);
            code_controller.setShowResult(isShowResult);
            code_controller.setNoSet(false);
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

    public CodeBlockArea getCodeArea() {
        return codeArea;
    }

    public String getCodeString(){
        return getCodeArea().getText();
    }

    public void setClassName(String input){
        className = input;
        getResultBlock().setClassName(input);
    }

    public String getClassName(){
        return className;
    }

    public CodeResultBlock getResultBlock(){
        return resultBlock;
    }

    public boolean getShowResult(){
        return isShowResult;
    }

    public void setShowResult(boolean input){
        isShowResult = input;

        if(input) {
            paper_controller.getCurentPaper().addNode(getResultBlock());
        } else {
            paper_controller.getCurentPaper().removeNode(getResultBlock());
        }
    }
}
