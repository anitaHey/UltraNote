package Object;

import Controller.MainController;
import InsertObj.BasicNode;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;

public class CodeResultBlock extends BasicNode {
    private TextArea area;
    private Label className;

    public CodeResultBlock() {
        super("code_result");

        setHasMin(false);
        area = new TextArea();
        area.setEditable(false);
        className = new Label("Result for unknown class");
        className.setPrefHeight(30);

        VBox vbox = new VBox();
        vbox.getChildren().add(className);
        vbox.getChildren().add(area);
        vbox.getStyleClass().add("code_result");
        this.getMain_content().getChildren().add(new ScrollPane(vbox));

        this.setMinH(200, false);
        this.setMinW(200, false);

        MainController.getInstance().change_toolbar(MainController.Type.Code, false);

        getMain_content().heightProperty().addListener((obs, oldValue, newValue) -> {
            area.setPrefHeight(newValue.doubleValue() - 37);
        });

        getMain_content().widthProperty().addListener((obs, oldValue, newValue) -> {
            area.setPrefWidth(newValue.doubleValue()-7);
            className.setPrefWidth(newValue.doubleValue()-7);
        });

        this.setOnMouseClicked(e -> {
            MainController.getInstance().change_toolbar(MainController.Type.Code, false);
        });
    }

    public TextArea getArea(){
        return area;
    }

    public void setText(String input){
        getArea().setText(input);
    }

    public void setClassName(String input) {
        this.className.setText("Result for " + input + ".java");
    }
}
