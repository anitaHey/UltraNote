package InsertObj;

import Controller.PaperController;
import javafx.geometry.Insets;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class Layout_VBox extends BasicNode {
    private PaperController paper_controller = PaperController.getInstance();

    public Layout_VBox() {
        super("vbox");
        setHasMin(true);

        this.setMinW(100, true);
        this.setMinH(45, true);

        this.setMinH(155, false);
        this.setMinW(300, false);

        VBox vbox = new VBox();
        vbox.setPadding(new Insets(10, 10, 10, 10));
        vbox.setMinWidth(USE_COMPUTED_SIZE);
        vbox.setMinHeight(USE_COMPUTED_SIZE);
        vbox.setPrefWidth(300);
        vbox.setPrefHeight(155);
        getMain_content().getChildren().add(vbox);
        vbox.setFillWidth(true);

        vbox.heightProperty().addListener((obs, oldValue, newValue) -> {
            this.setMinH(newValue.doubleValue(), false);
        });

        vbox.widthProperty().addListener((obs, oldValue, newValue) -> {
            this.setMinW(newValue.doubleValue(), false);
        });

//        getMain_content().heightProperty().addListener((obs, oldValue, newValue) -> {
//            vbox.setPrefHeight(newValue.doubleValue());
//        });
//
//        getMain_content().widthProperty().addListener((obs, oldValue, newValue) -> {
//            vbox.setPrefWidth(newValue.doubleValue());
//        });

        setInsert_part(vbox);
    }

    @Override
    public void setIsParent(boolean input) {
        isParent = input;

        if(input){
            this.getInsert_part().setPrefHeight(Region.USE_COMPUTED_SIZE);
            this.getInsert_part().setPrefWidth(Region.USE_COMPUTED_SIZE);
        } else {
            this.getInsert_part().setPrefWidth(300);
            this.getInsert_part().setPrefHeight(155);
        }
    }
}
