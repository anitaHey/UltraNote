package InsertObj;

import Controller.PaperController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.List;

public class ResizeNode extends GridPane {
    private String type;
    private PaperController paper_controller = PaperController.getInstance();
    private Paper paper = paper_controller.getCurentPaper();
    private double lastMouseX = 0, lastMouseY = 0, lastMoveX = 0, lastMoveY = 0;
    private boolean dragging = false;
    private int cursor = -1;
    private boolean isRelease = true;

    public ResizeNode(String type) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../FXML/ResizeNodeFxml.fxml"));
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
    public void initialize() {
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

        setDrag();
    }

    public void setDrag() {
        this.addEventHandler(MouseEvent.ANY, event -> {
            if (MouseEvent.MOUSE_CLICKED == event.getEventType()) {
                paper_controller.setFocusObject(this);
            } else if (MouseEvent.MOUSE_MOVED == event.getEventType()) {
                paper.setClick(false);

                cursor = getResize(event);

                if (type.equals("text") && !isBorder(event)) {
                    paper.setCursor(Cursor.TEXT);
                } else if (cursor == -1) {
                    paper.setCursor(Cursor.MOVE);
                } else {
                    paper.setCursor(cursor_arr.get(cursor));
                }

            } else if (MouseEvent.MOUSE_PRESSED == event.getEventType()) {
                paper_controller.setFocusObject(this);
                if (this.contains(event.getX(), event.getY())) {
                    this.lastMouseX = event.getSceneX();
                    this.lastMouseY = event.getSceneY();
                    this.lastMoveX = event.getSceneX();
                    this.lastMoveY = event.getSceneY();
                    if (!(type.equals("text") && !isBorder(event)) && cursor == -1) {
                        paper.setCursor(Cursor.MOVE);
                        this.dragging = true;
                    }

                    event.consume();
                }
            } else if (MouseEvent.MOUSE_DRAGGED == event.getEventType()) {
                isRelease = false;
                paper_controller.setFocusObject(this);
                if (this.dragging) {
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
                } else if (cursor != -1) {
                    double moveX = (event.getSceneX() - this.lastMoveX) * 3;
                    double moveY = (event.getSceneY() - this.lastMoveY) * 3;

                    Bounds pane = this.getMain_content().getBoundsInParent();
                    final double width = pane.getWidth();
                    final double height = pane.getHeight();

                    switch (cursor) {
                        case 0:
                        case 2:
                        case 5:
                        case 7:
                            if (type.equals("text")) {
                                this.getMain_content().setMinWidth(width + moveX);
                                this.getMain_content().setMinHeight(height + moveY);
                            } else if (type.equals("picture")) {
                                this.getMain_content().setPrefWidth(width + moveX);
                                this.getMain_content().setPrefHeight(height + moveY);
                            }

                            this.lastMoveX = event.getSceneX();
                            this.lastMoveY = event.getSceneY();
                            break;
                        case 1:
                        case 6:
                            if (type.equals("text"))
                                this.getMain_content().setMinHeight(height + moveY);
                            else if (type.equals("picture"))
                                this.getMain_content().setPrefHeight(height + moveY);

                            this.lastMoveY = event.getSceneY();
                            break;
                        case 3:
                        case 4:
                            if (type.equals("text"))
                                this.getMain_content().setMinWidth(width + moveX);
                            else if (type.equals("picture"))
                                this.getMain_content().setPrefWidth(width + moveX);

                            this.lastMoveX = event.getSceneX();
                            break;
                    }

                    event.consume();
                }
            } else if (MouseEvent.MOUSE_RELEASED == event.getEventType()) {
                isRelease = true;
                paper_controller.setFocusObject(this);
                if (this.dragging || cursor != -1) {
                    event.consume();
                    this.dragging = false;
                }
            } else if (MouseEvent.MOUSE_EXITED == event.getEventType()) {
                if (isRelease) {
                    paper.setCursor(Cursor.DEFAULT);
                    paper.setClick(true);
                }
            }
        });
    }

    public boolean isBorder(MouseEvent event) {
        Bounds dragNodeBounds = this.getBoundsInParent();
        Boolean top = (Math.abs(event.getY()) <= 15);
        Boolean bottom = (Math.abs(event.getY() - dragNodeBounds.getHeight()) <= 15);
        Boolean left = (Math.abs(event.getX()) <= 15);
        Boolean right = (Math.abs(event.getX() - dragNodeBounds.getWidth()) <= 15);

        return top || bottom || left || right;
    }

    public int getResize(MouseEvent event) {
        int output = -1;
        Bounds dragNodeBounds = this.getBoundsInParent();
        double h_half = dragNodeBounds.getHeight() / 2;
        double w_half = dragNodeBounds.getWidth() / 2;

        if (Math.abs(event.getY()) <= 15) {
            if (Math.abs(event.getX()) <= 15) output = 0;
            else if (Math.abs(event.getX() - w_half) <= 15) output = 1;
            else if (Math.abs(event.getX() - dragNodeBounds.getWidth()) <= 15) output = 2;
        } else if (Math.abs(event.getY() - h_half) <= 15) {
            if (Math.abs(event.getX()) <= 15) output = 3;
            else if (Math.abs(event.getX() - dragNodeBounds.getWidth()) <= 15) output = 4;
        } else if (Math.abs(event.getY() - dragNodeBounds.getHeight()) <= 15) {
            if (Math.abs(event.getX()) <= 15) output = 5;
            else if (Math.abs(event.getX() - w_half) <= 15) output = 6;
            else if (Math.abs(event.getX() - dragNodeBounds.getWidth()) <= 15) output = 7;
        }

        return output;
    }

    public void focus_border(boolean show) {
        if (show) {
            this.getStyleClass().clear();
            this.getStyleClass().add("border_focus");

            for (Circle cir : cir_arr) {
                cir.getStyleClass().clear();
                cir.getStyleClass().add("show");
            }
        } else {
            this.getStyleClass().clear();
            this.getStyleClass().add("border_none");
            for (Circle cir : cir_arr) {
                cir.getStyleClass().clear();
                cir.getStyleClass().add("hide");
            }
        }
    }

    public Pane getMain_content() {
        return main_content;
    }
}
