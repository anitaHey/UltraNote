package Object;

import Controller.PaperController;
import InsertObj.BasicNode;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.CheckBox;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class CheckListBlock extends HBox {
    private PaperController paper_controller = PaperController.getInstance();

    public CheckListBlock(){
        try {
            FXMLLoader loader;
            loader = new FXMLLoader(getClass().getResource("../FXML/CheckListBlockFxml.fxml"));

            loader.setController(this);
            loader.setRoot(this);
            loader.load();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @FXML
    CheckBox check;
    @FXML
    VBox pane;

    @FXML
    public void initialize() {
        pane.toFront();
        pane.addEventFilter(MouseDragEvent.MOUSE_DRAG_ENTERED,  event -> {
            paper_controller.setDropNode((BasicNode)this.getParent());
            pane.getStyleClass().add("drag_detect");
        });

        pane.addEventFilter(MouseDragEvent.MOUSE_DRAG_EXITED,  event -> {
            paper_controller.setDropNode(null);
            pane.getStyleClass().remove("drag_detect");
        });
    }
}
