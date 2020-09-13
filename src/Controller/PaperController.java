package Controller;

import InsertObj.BasicNode;
import InsertObj.Paper;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class PaperController {
    private static PaperController instance;
    private Paper current;
    private BasicNode object;
    private BasicNode drop_node;

    public static PaperController getInstance() {
        if (instance == null) {
            instance = new PaperController();
        }
        return instance;
    }

    public static void setInstance(PaperController newInstance) {
        instance = newInstance;
    }

    public PaperController() {
        setInstance(this);
    }

    public Paper getCurentPaper() {
        return getInstance().current;
    }

    public void setCurentPaper(Paper pane) {
        getInstance().current = pane;
    }

    public void setFocusObject(BasicNode obj) {
        if (getInstance().getFocusObject() != null) {
            getInstance().getFocusObject().focus_border(false);
            getInstance().getFocusObject().removeEventFilter(MouseEvent.ANY, eventHandler);
            if (getInstance().getFocusObject().getNodeParent() != null)
                getInstance().getFocusObject().getNodeParent().setIsParent(false);
        }
        getInstance().object = obj;

        if (obj != null) {
            obj.focus_border(true);
            if (getInstance().getFocusObject().getNodeParent() == null)
                getInstance().getFocusObject().addEventFilter(MouseEvent.ANY, eventHandler);
            else
                getInstance().getFocusObject().getNodeParent().setIsParent(true);
        }
    }

    //TODO: BUUUUUUUUUUUUUUUUUUUUUUUUUG here. RRRRRRRRRRRRRRRRRR
    EventHandler<MouseEvent> eventHandler = mouseEvent -> {
        if (getInstance().getFocusObject() != null) {
            if (mouseEvent.getEventType() == MouseEvent.MOUSE_PRESSED) {
                getInstance().getFocusObject().setMouseTransparent(true);
            } else if (mouseEvent.getEventType() == MouseEvent.MOUSE_RELEASED) {
                if (getDropNode() != null) {
                    getDropNode().getInsert_part().getChildren().add(getInstance().getFocusObject());
                    getCurentPaper().getChildren().remove(getInstance().getFocusObject());

                    getInstance().getFocusObject().setNodeParent(getDropNode());
                    getInstance().getFocusObject().setTranslateX(0);
                    getInstance().getFocusObject().setTranslateY(0);
                    getInstance().getFocusObject().toFront();
                    getDropNode().setIsParent(true);
                }

                getInstance().getFocusObject().setMouseTransparent(false);
            } else if (mouseEvent.getEventType() == MouseEvent.DRAG_DETECTED) {
                getInstance().getFocusObject().startFullDrag();
            }
        }
    };

    public BasicNode getFocusObject() {
        return getInstance().object;
    }

    public void setDropNode(BasicNode obj) {
        drop_node = obj;
    }

    public BasicNode getDropNode() {
        return drop_node;
    }
}
