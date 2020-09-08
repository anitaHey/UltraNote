package InsertObj;

import Controller.PaperController;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.List;

public class BasicNode extends VBox {
    private String type;
    private boolean hasMin;
    private PaperController paper_controller = PaperController.getInstance();
    private Paper paper = paper_controller.getCurentPaper();
    private double lastMouseX = 0, lastMouseY = 0, minW = 0, minH = 0;
    private boolean dragging = false, cropping = false, rotating = false;
    private int cursor = -1;
    private Node parent;


    public BasicNode(String type) {
        try {
            FXMLLoader loader;
            loader = new FXMLLoader(getClass().getResource("../FXML/BasicNodeFxml.fxml"));

            loader.setController(this);
            loader.setRoot(this);
            loader.load();

            this.type = type;
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    List<Circle> cir_arr = new ArrayList<>();
    List<Cursor> cursor_arr = new ArrayList<>();

    @FXML
    Circle cir1, cir2, cir3, cir4, cir5, cir6, cir7, cir8;
    @FXML
    Pane main_content;
    @FXML
    GridPane gridpane;

    @FXML
    public void initialize() {
        allInit();
        setDrag();
    }

    public void allInit() {
        focus_border(true);
        paper_controller.setFocusObject(this);

        cir_arr.add(cir1);
        cir_arr.add(cir2);
        cir_arr.add(cir3);
        cir_arr.add(cir4);
        cir_arr.add(cir5);
        cir_arr.add(cir6);
        cir_arr.add(cir7);
        cir_arr.add(cir8);

        cursor_arr.add(Cursor.NW_RESIZE);
        cursor_arr.add(Cursor.N_RESIZE);
        cursor_arr.add(Cursor.NE_RESIZE);
        cursor_arr.add(Cursor.W_RESIZE);
        cursor_arr.add(Cursor.E_RESIZE);
        cursor_arr.add(Cursor.SW_RESIZE);
        cursor_arr.add(Cursor.S_RESIZE);
        cursor_arr.add(Cursor.SE_RESIZE);

        for (Circle cir : cir_arr)
            cir.toFront();

        this.setLayoutX(5);
        this.setLayoutY(5);
    }

    public GridPane getGridpane() {
        return gridpane;
    }

    public Pane getMain_content() {
        return main_content;
    }

    public boolean getIsDragging() {
        return dragging;
    }

    public String getNodeType() {
        return type;
    }

    public boolean getHasMin() {
        return hasMin;
    }

    public void setHasMin(boolean input){
        hasMin = input;
    }

    public void setMinW(double w, boolean set) {
        if (getHasMin()) {
            if (set) this.getMain_content().setMinWidth(w);
            minW = w;
        } else {
            this.getMain_content().setPrefWidth(w);
            minW = 0;
        }
    }

    public void setNodeParent(Node node) {
        parent = node;
    }

    public Node getNodeParent() {
        return parent;
    }

    public void setMinH(double h, boolean set) {
        if (getHasMin()) {
            if (set) this.getMain_content().setMinHeight(h);
            minH = h;
        } else {
            this.getMain_content().setPrefHeight(h);
            minH = 0;
        }
    }

    public void setDrag() {
        getGridpane().addEventHandler(MouseEvent.ANY, mouseDragHandler);
    }

    public void cancelDrag() {
        getGridpane().removeEventHandler(MouseEvent.ANY, mouseDragHandler);
    }

    EventHandler<MouseEvent> mouseDragHandler = event -> {
        if (MouseEvent.MOUSE_CLICKED == event.getEventType()) {
            paper_controller.setFocusObject(this);
        } else if (MouseEvent.MOUSE_MOVED == event.getEventType()) {
            paper.setClick(false);

            cursor = getResize(event, 15);

            if (cursor == -1) {
                paper.setCursor(Cursor.MOVE);
            } else {
                paper.setCursor(cursor_arr.get(cursor));
            }
        } else if (MouseEvent.MOUSE_PRESSED == event.getEventType()) {
            paper_controller.setFocusObject(this);
            if (getGridpane().contains(event.getX(), event.getY())) {
                this.lastMouseX = event.getSceneX();
                this.lastMouseY = event.getSceneY();

                if (cursor == -1)
                    paper.setCursor(Cursor.MOVE);
                this.dragging = true;

                event.consume();
            }
        } else if (MouseEvent.MOUSE_DRAGGED == event.getEventType()) {
            paper_controller.setFocusObject(this);
            if (this.dragging && cursor == -1) {
                double deltaX = event.getSceneX() - this.lastMouseX;
                double deltaY = event.getSceneY() - this.lastMouseY;

                final double initialTranslateX = this.getTranslateX();
                final double initialTranslateY = this.getTranslateY();

                Bounds dragNodeBounds = this.getBoundsInParent();

                if (dragNodeBounds.getMinX() < 0) deltaX = 1;
                else if (dragNodeBounds.getMaxX() > paper.getWidth()) deltaX = -1;

                if (dragNodeBounds.getMinY() < 0) deltaY = 1;
                else if (dragNodeBounds.getMaxY() > paper.getHeight()) deltaY = -1;


                this.setTranslateX(initialTranslateX + deltaX);
                this.setTranslateY(initialTranslateY + deltaY);

                this.lastMouseX = event.getSceneX();
                this.lastMouseY = event.getSceneY();

                event.consume();
            } else if (this.dragging) {
                double minWidth = this.getMain_content().getMinWidth();
                double minHeight = this.getMain_content().getMinHeight();

                double width = this.getMain_content().getWidth();
                double height = this.getMain_content().getHeight();

                double prefWidth = this.getMain_content().getPrefWidth();
                double prefHeight = this.getMain_content().getPrefHeight();

                Bounds pane = this.getBoundsInLocal();
                double moveMinX = pane.getMinX() - event.getX();
                double moveMinY = pane.getMinY() - event.getY();
                double moveMaxX = event.getX() - pane.getMaxX();
                double moveMaxY = event.getY() - pane.getMaxY();

                final double initialLayoutX = this.getTranslateX();
                final double initialLayoutY = this.getTranslateY();

                boolean minX = false, maxX = false, minY = false, maxY = false;

                if (getHasMin()) {
                    minX = (minWidth + moveMinX) > minW;
                    maxX = (minWidth + moveMaxX) > minW;
                    minY = (minHeight + moveMinY) > minH;
                    maxY = (minHeight + moveMaxY) > minH;
                } else {
                    minX = prefWidth > 0;
                    maxX = prefWidth > 0;
                    minY = prefHeight > 0;
                    maxY = prefHeight > 0;
                }

                switch (cursor) {
                    case 0:
                        if (getHasMin()) {
                            if (minX) this.getMain_content().setMinWidth(minWidth + moveMinX);
                            if (minY) this.getMain_content().setMinHeight(minHeight + moveMinY);
                        } else {
                            this.getMain_content().setPrefWidth(Math.max((prefWidth + moveMinX), 0));
                            this.getMain_content().setPrefHeight(Math.max((prefHeight + moveMinY), 0));
                        }

                        if (minX) this.setTranslateX(initialLayoutX - moveMinX);
                        if (minY) this.setTranslateY(initialLayoutY - moveMinY);
                        break;
                    case 1:
                        if (getHasMin()) {
                            if (minY) this.getMain_content().setMinHeight(minHeight + moveMinY);
                        } else
                            this.getMain_content().setPrefHeight(Math.max((prefHeight + moveMinY), 0));

                        if (minY) this.setTranslateY(initialLayoutY - moveMinY);
                        break;
                    case 2:
                        if (getHasMin()) {
                            if (maxX) this.getMain_content().setMinWidth(width + moveMaxX);

                            if (minY) this.getMain_content().setMinHeight(minHeight + moveMinY);
                        } else {
                            this.getMain_content().setPrefWidth(Math.max((width + moveMaxX), 0));
                            this.getMain_content().setPrefHeight(Math.max((prefHeight + moveMinY), 0));
                        }

                        if (minY) this.setTranslateY(initialLayoutY - moveMinY);
                        break;
                    case 3:
                        if (getHasMin()) {
                            if (minX) this.getMain_content().setMinWidth(minWidth + moveMinX);
                        } else
                            this.getMain_content().setPrefWidth(Math.max((prefWidth + moveMinX), 0));

                        if (minX) this.setTranslateX(initialLayoutX - moveMinX);
                        break;
                    case 4:
                        if (getHasMin())
                            this.getMain_content().setMinWidth(Math.max((width + moveMaxX), minW));
                        else
                            this.getMain_content().setPrefWidth(Math.max((width + moveMaxX), 0));
                        break;
                    case 5:
                        if (getHasMin()) {
                            if (minX) this.getMain_content().setMinWidth(minWidth + moveMinX);

                            if (maxY) this.getMain_content().setMinHeight(height + moveMaxY);
                        } else {
                            this.getMain_content().setPrefWidth(Math.max((prefWidth + moveMinX), 0));
                            this.getMain_content().setPrefHeight(Math.max((height + moveMaxY), 0));
                        }

                        if (minX) this.setTranslateX(initialLayoutX - moveMinX);
                        break;
                    case 6:
                        if (getHasMin())
                            this.getMain_content().setMinHeight(Math.max((height + moveMaxY), minH));
                        else
                            this.getMain_content().setPrefHeight(Math.max((height + moveMaxY), 0));

                        break;
                    case 7:
                        if (getHasMin()) {
                            this.getMain_content().setMinWidth(Math.max((width + moveMaxX), minW));
                            this.getMain_content().setMinHeight(Math.max((height + moveMaxY), minH));
                        } else {
                            this.getMain_content().setPrefWidth(Math.max((width + moveMaxX), 0));
                            this.getMain_content().setPrefHeight(Math.max((height + moveMaxY), 0));
                        }

                        break;
                }
                event.consume();
            }
        } else if (MouseEvent.MOUSE_RELEASED == event.getEventType()) {
            paper_controller.setFocusObject(this);
            if (dragging) {
                event.consume();
                dragging = false;
            }
        } else if (MouseEvent.MOUSE_EXITED == event.getEventType()) {
            if (!dragging) {
                paper.setCursor(Cursor.DEFAULT);
                paper.setClick(true);
            }
        }
    };

    public boolean isBorder(MouseEvent event) {
        Bounds dragNodeBounds = getGridpane().getBoundsInParent();
        Boolean top = (Math.abs(event.getY()) <= 15);
        Boolean bottom = (Math.abs(event.getY() - dragNodeBounds.getHeight()) <= 15);
        Boolean left = (Math.abs(event.getX()) <= 15);
        Boolean right = (Math.abs(event.getX() - dragNodeBounds.getWidth()) <= 15);

        return top || bottom || left || right;
    }

    public int getResize(MouseEvent event, int instance) {
        int output = -1;
        Bounds dragNodeBounds = getGridpane().getBoundsInParent();
        double h_half = dragNodeBounds.getHeight() / 2;
        double w_half = dragNodeBounds.getWidth() / 2;

        if (Math.abs(event.getY()) <= instance) {
            if (Math.abs(event.getX()) <= instance) output = 0;
            else if (Math.abs(event.getX() - w_half) <= instance) output = 1;
            else if (Math.abs(event.getX() - dragNodeBounds.getWidth()) <= instance) output = 2;
        } else if (Math.abs(event.getY() - h_half) <= instance) {
            if (Math.abs(event.getX()) <= instance) output = 3;
            else if (Math.abs(event.getX() - dragNodeBounds.getWidth()) <= instance) output = 4;
        } else if (Math.abs(event.getY() - dragNodeBounds.getHeight()) <= instance) {
            if (Math.abs(event.getX()) <= instance) output = 5;
            else if (Math.abs(event.getX() - w_half) <= instance) output = 6;
            else if (Math.abs(event.getX() - dragNodeBounds.getWidth()) <= instance) output = 7;
        }

        return output;
    }

    public void focus_border(boolean show) {
        if (show) {
            getGridpane().getStyleClass().clear();
            getGridpane().getStyleClass().add("border_focus");

            for (Circle cir : cir_arr) {
                cir.getStyleClass().clear();
                cir.getStyleClass().add("show");
            }
        } else {
            getGridpane().getStyleClass().clear();
            getGridpane().getStyleClass().add("border_none");

            for (Circle cir : cir_arr) {
                cir.getStyleClass().clear();
                cir.getStyleClass().add("hide");
            }
        }
    }
}
