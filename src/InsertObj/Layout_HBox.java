package InsertObj;

import Controller.PaperController;
import javafx.geometry.Insets;
import javafx.scene.layout.Region;
import javafx.scene.layout.HBox;

public class Layout_HBox extends BasicNode {
//    private PaperController paper_controller = PaperController.getInstance();

    public Layout_HBox() {
        super("hbox");
        setHasMin(true);

        this.setMinW(100, true);
        this.setMinH(45, true);

        this.setMinH(155, false);
        this.setMinW(300, false);

        HBox hbox = new HBox();
        hbox.setPadding(new Insets(10, 10, 10, 10));
        hbox.setMinWidth(USE_COMPUTED_SIZE);
        hbox.setMinHeight(USE_COMPUTED_SIZE);
        hbox.setPrefWidth(155);
        hbox.setPrefHeight(300);
        getMain_content().getChildren().add(hbox);
        hbox.setFillHeight(true);

        hbox.heightProperty().addListener((obs, oldValue, newValue) -> {
            this.setMinH(newValue.doubleValue(), false);
        });

        hbox.widthProperty().addListener((obs, oldValue, newValue) -> {
            this.setMinW(newValue.doubleValue(), false);
        });

        setInsert_part(hbox);
    }

    @Override
    public void setIsParent(boolean input) {
        isParent = input;

        if(input){
            this.getInsert_part().setPrefHeight(Region.USE_COMPUTED_SIZE);
            this.getInsert_part().setPrefWidth(Region.USE_COMPUTED_SIZE);
        } else {
            this.getInsert_part().setPrefWidth(155);
            this.getInsert_part().setPrefHeight(300);
        }
    }
}
