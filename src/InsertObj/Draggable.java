package InsertObj;

import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Draggable {
    public enum Event {
        None, DragStart, Drag, DragEnd
    }

    public interface Listener {
        public void accept(Move draggableNature, Event dragEvent);
    }

    public static final class Move implements EventHandler<MouseEvent> {
        private Pane paper = Paper.getCurentPaper();
        private double lastMouseX = 0, lastMouseY = 0;
        private boolean dragging = false;

        private final boolean enabled = true;
        private final Node eventNode;
        private final List<Node> dragNodes = new ArrayList<>();
        private final List<Listener> dragListeners = new ArrayList<>();

        public Move(final Node node) {
            this(node, node);
        }

        public Move(final Node eventNode, final Node... dragNodes) {
            this.eventNode = eventNode;
            this.dragNodes.addAll(Arrays.asList(dragNodes));
            this.eventNode.addEventHandler(MouseEvent.ANY, this);
        }

        @Override
        public final void handle(final MouseEvent event) {
            if(MouseEvent.MOUSE_MOVED == event.getEventType()){
                for (final Node dragNode : this.dragNodes) {
                    if (isBorder(dragNode, event)) {
                        paper.setCursor(Cursor.MOVE);
                    }else{
                        paper.setCursor(Cursor.TEXT);
                    }
                }
            } else if (MouseEvent.MOUSE_PRESSED == event.getEventType()) {
                if (this.enabled && this.eventNode.contains(event.getX(), event.getY())) {
                    this.lastMouseX = event.getSceneX();
                    this.lastMouseY = event.getSceneY();
                    for (final Node dragNode : this.dragNodes) {
                        if (isBorder(dragNode, event)) {
                            paper.setCursor(Cursor.MOVE);
                            this.dragging = true;
                            for (final Listener listener : this.dragListeners) {
                                listener.accept(this, Draggable.Event.DragStart);
                            }
                        }
                    }
                    event.consume();
                }
            } else if (MouseEvent.MOUSE_DRAGGED == event.getEventType()) {
                if (this.dragging) {
                    double deltaX = event.getSceneX() - this.lastMouseX;
                    double deltaY = event.getSceneY() - this.lastMouseY;

                    for (final Node dragNode : this.dragNodes) {
                        final double initialTranslateX = dragNode.getTranslateX();
                        final double initialTranslateY = dragNode.getTranslateY();

                        Bounds dragNodeBounds = dragNode.getBoundsInParent();

                        if(dragNodeBounds.getMinX()< 0) deltaX = 1;
                        else if(dragNodeBounds.getMaxX()>paper.getWidth()) deltaX = -1;

                        if(dragNodeBounds.getMinY()< 0) deltaY = 1;
                        else if(dragNodeBounds.getMaxY()>paper.getHeight()) deltaY = -1;

                        dragNode.setTranslateX(initialTranslateX + deltaX);
                        dragNode.setTranslateY(initialTranslateY + deltaY);
                    }

                    this.lastMouseX = event.getSceneX();
                    this.lastMouseY = event.getSceneY();

                    event.consume();
                    for (final Listener listener : this.dragListeners) {
                        listener.accept(this, Draggable.Event.Drag);
                    }
                }
            } else if (MouseEvent.MOUSE_RELEASED == event.getEventType()) {
                if (this.dragging) {
                    event.consume();
                    this.dragging = false;
                    for (final Listener listener : this.dragListeners) {
                        listener.accept(this, Draggable.Event.DragEnd);
                    }
                }
            }else if(MouseEvent.MOUSE_EXITED == event.getEventType()){
                paper.setCursor(Cursor.DEFAULT);
            }
        }

        public boolean isBorder(Node dragNode, MouseEvent event){
                Bounds dragNodeBounds = dragNode.getBoundsInParent();
                Boolean top = (Math.abs(event.getY()) <= 15);
                Boolean bottom = (Math.abs(event.getY() - dragNodeBounds.getHeight()) <= 15);
                Boolean left = (Math.abs(event.getX()) <= 15);
                Boolean right = (Math.abs(event.getX() - dragNodeBounds.getWidth()) <= 15);

            return top || bottom || left || right;
        }
    }
}