package Object;

import Controller.PaperController;
import InsertObj.BasicNode;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class CheckListBlock extends BasicNode {
    private PaperController paper_controller = PaperController.getInstance();

    CheckBox check;
    Pane pane;

    public CheckListBlock(){
        super("checklistblock");
        setHasMin(true);

        this.setMinH(50, false);
        this.setMinW(200, false);

        HBox hbox = new HBox();
        hbox.setMinHeight(50);
        hbox.setMinWidth(200);
        hbox.setPrefHeight(Region.USE_COMPUTED_SIZE);
        hbox.setPrefWidth(Region.USE_COMPUTED_SIZE);

        check = new CheckBox();
        hbox.getChildren().add(check);

        pane = new Pane();
        pane.setMinHeight(50);
        pane.setMinWidth(200);
        pane.setPrefHeight(Region.USE_COMPUTED_SIZE);
        pane.setPrefWidth(Region.USE_COMPUTED_SIZE);
        hbox.getChildren().add(pane);

        getMain_content().getChildren().add(hbox);
        hbox.setFillHeight(true);
        hbox.setAlignment(Pos.CENTER);

        setInsert_part(pane);
        cancelDrag();
    }

    @Override
    public void setIsParent(boolean input) {
        isParent = input;

        if(input){
            this.getInsert_part().setPrefHeight(Region.USE_COMPUTED_SIZE);
            this.getInsert_part().setPrefWidth(Region.USE_COMPUTED_SIZE);
        } else {
            this.getInsert_part().setPrefWidth(50);
            this.getInsert_part().setPrefHeight(200);
        }
    }
}
