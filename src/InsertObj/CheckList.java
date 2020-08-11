package InsertObj;

import Controller.PaperController;
import Object.CheckListBlock;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;

public class CheckList extends BasicNode {
    private PaperController paper_controller = PaperController.getInstance();
    public CheckList() {
        super("checkList");
        setHasMin(false);

        this.setMinH(155, false);
        this.setMinW(400, false);

        this.getMain_content().getChildren().add(new CheckListBlock());
    }
}
