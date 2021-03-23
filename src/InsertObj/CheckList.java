package InsertObj;

import Controller.PaperController;
import Object.CheckListBlock;
import Object.ObjectAddButton;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public class CheckList extends BasicNode {
    private PaperController paper_controller = PaperController.getInstance();
    private ObjectAddButton addBtn;
    private boolean add;
    private VBox vbox;

    public CheckList() {
        super("checkList");
        setHasMin(false);

        this.setMinH(155, false);
        this.setMinW(400, false);
        addBtn = new ObjectAddButton();

        vbox = new VBox();
        vbox.getChildren().add(new CheckListBlock());
        this.getMain_content().getChildren().add(vbox);

        this.widthProperty().addListener((obs, oldValue, newValue) -> {
            addBtn.setPrefWidth((Double) newValue-20);
        });

        add = false;

        this.addEventHandler(MouseEvent.MOUSE_MOVED, event -> {
            if (!add)
                vbox.getChildren().add(vbox.getChildren().size(), addBtn);
            add = true;
        });

        this.addEventHandler(MouseEvent.MOUSE_EXITED, event -> {
            vbox.getChildren().remove(addBtn);
            add = false;
        });
    }
}
