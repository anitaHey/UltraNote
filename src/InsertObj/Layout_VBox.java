package InsertObj;

import Controller.PaperController;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.layout.VBox;

public class Layout_VBox extends BasicNode {
    private PaperController paper_controller = PaperController.getInstance();

    public Layout_VBox() {
        super("vbox");
        setHasMin(false);

        this.setMinH(155, false);
        this.setMinW(500, false);

        VBox vbox = new VBox();
        getMain_content().getChildren().add(vbox);
        vbox.setFillWidth(true);

        getMain_content().heightProperty().addListener((obs, oldValue, newValue) -> {
            vbox.setPrefHeight(newValue.doubleValue());
        });

        getMain_content().widthProperty().addListener((obs, oldValue, newValue) -> {
            vbox.setPrefWidth(newValue.doubleValue());
        });

        setInsert_part(vbox);
    }
}
