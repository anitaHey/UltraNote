package Controller;

import InsertObj.BasicNode;
import InsertObj.Paper;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

public class PaperController {
    private static PaperController instance;
    private Paper current;
    private BasicNode object;
    private Pane drop_node;

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
        if (getInstance().getFocusObject() != null){
            getInstance().getFocusObject().focus_border(false);
            getInstance().getFocusObject().removeEventFilter(MouseEvent.ANY, eventHandler);
        }
        getInstance().object = obj;

        if (obj != null){
            obj.focus_border(true);
            getInstance().getFocusObject().addEventFilter(MouseEvent.ANY, eventHandler);
        }
    }
    //TODO: BUG here.
    EventHandler<MouseEvent> eventHandler = mouseEvent -> {
        if(getInstance().getFocusObject() != null){
            if(mouseEvent.getEventType() == MouseEvent.MOUSE_PRESSED){
                getInstance().getFocusObject().setMouseTransparent(true);
            } else if(mouseEvent.getEventType() == MouseEvent.MOUSE_RELEASED){
                if(getDropNode() != null){
                    getDropNode().getChildren().add(getInstance().getFocusObject());
                    getInstance().getFocusObject().setTranslateX(0);
                    getInstance().getFocusObject().setTranslateY(0);
                    getInstance().getFocusObject().toFront();
                }
                getInstance().getFocusObject().setMouseTransparent(false);
            } else if(mouseEvent.getEventType() == MouseEvent.DRAG_DETECTED){
                getInstance().getFocusObject().startFullDrag();
            }
        }
    };

    public BasicNode getFocusObject() {
        return getInstance().object;
    }

    public void setDropNode(Pane obj) {
        drop_node = obj;
    }

    public Pane getDropNode() {
        return drop_node;
    }
}
