package InsertObj;

import Controller.Toolbar_DrawController;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Path;

public class DrawLine extends BasicNode{
    Toolbar_DrawController draw_controll = Toolbar_DrawController.getInstance();
    private Group group;
    private Path path;

    public DrawLine() {
        super("draw");

        this.cancelDrag();
        group = new Group();
        path = new Path();
        group.getChildren().add(path);
    }

    public Group getGroup(){
        return group;
    }

    public Path getPath(){
        return path;
    }

    public void setStroke(double width, Color color){
        getPath().setStrokeWidth(width);
        getPath().setStroke(color);
    }

    public void setSize(){
        this.setTranslateX(getGroup().getBoundsInParent().getMinX() - 16);
        this.setTranslateY(getGroup().getBoundsInParent().getMinY() - 16);
        this.setMinH(getGroup().getBoundsInParent().getHeight(), true);
        this.setMinW(getGroup().getBoundsInParent().getWidth(), true);
        getGroup().setLayoutX(-getGroup().getBoundsInParent().getMinX());
        getGroup().setLayoutY(-getGroup().getBoundsInParent().getMinY());
        this.getMain_content().getChildren().add(getGroup());
    }

    public void setErase(boolean set){
        if(set) {
            getPath().addEventFilter(MouseDragEvent.MOUSE_DRAG_ENTERED, eraserHandler);
        } else {
            getPath().removeEventFilter(MouseDragEvent.MOUSE_DRAG_ENTERED, eraserHandler);
        }
    }

    EventHandler<MouseDragEvent> eraserHandler = event -> {
        draw_controll.deleteDrawLine(this);
    };

}
