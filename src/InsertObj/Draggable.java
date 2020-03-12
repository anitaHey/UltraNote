package InsertObj;

import javafx.event.EventHandler;
import javafx.geometry.Bounds;
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
            if (MouseEvent.MOUSE_PRESSED == event.getEventType()) {
                if (this.enabled && this.eventNode.contains(event.getX(), event.getY())) {
                    this.lastMouseX = event.getSceneX();
                    this.lastMouseY = event.getSceneY();
                    event.consume();
                }
            } else if (MouseEvent.MOUSE_DRAGGED == event.getEventType()) {
                if (!this.dragging) {
                    this.dragging = true;
                    for (final Listener listener : this.dragListeners) {
                        listener.accept(this, Draggable.Event.DragStart);
                    }
                }
                if (this.dragging) {

                    double deltaX = event.getSceneX() - this.lastMouseX;
                    double deltaY = event.getSceneY() - this.lastMouseY;
//                    System.out.println("X : "+event.getSceneX()+" "+this.lastMouseX+" = "+deltaX);
//                    System.out.println("Y : "+event.getSceneY()+" "+this.lastMouseY+" = "+deltaY);

                    for (final Node dragNode : this.dragNodes) {
                        final double initialTranslateX = dragNode.getTranslateX();
                        final double initialTranslateY = dragNode.getTranslateY();

                        Bounds bounds = paper.getBoundsInLocal();
                        Bounds dragNodeBounds = dragNode.localToScreen(bounds);

                        System.out.println("X: "+bounds.getMinX()+","+dragNodeBounds.getMinX());
                        System.out.println("Y: "+bounds.getMinY()+","+dragNodeBounds.getMinY());
                        if(dragNodeBounds.getMinX()<bounds.getMinX()||dragNodeBounds.getMaxX()>bounds.getMaxX()) deltaX = 0;
                        if(dragNodeBounds.getMinY()<bounds.getMinY()||dragNodeBounds.getMaxY()>bounds.getMaxY()) deltaY = 0;

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
            }
        }
    }
}