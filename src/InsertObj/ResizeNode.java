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
    private double lastMouseX = 0, lastMouseY = 0;
    private boolean dragging = false;
    private int cursor = -1;

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

    @FXML
    Circle cir1, cir2, cir3, cir4, cir5, cir6, cir7, cir8;
    @FXML
    Pane main_content;

    @FXML
    public void initialize() {
        focus_border(true);
        paper_controller.setFocusObject(this);

        cir1.setOnMouseMoved(e -> {
            paper.setCursor(Cursor.NW_RESIZE);
            cursor = 1;
        });

        cir2.setOnMouseMoved(e -> {
            paper.setCursor(Cursor.N_RESIZE);
            cursor = 2;
        });

        cir3.setOnMouseMoved(e -> {
            paper.setCursor(Cursor.NE_RESIZE);
            cursor = 3;
        });

        cir4.setOnMouseMoved(e -> {
            paper.setCursor(Cursor.W_RESIZE);
            cursor = 4;
        });

        cir5.setOnMouseMoved(e -> {
            paper.setCursor(Cursor.E_RESIZE);
            cursor = 5;
        });

        cir6.setOnMouseMoved(e -> {
            paper.setCursor(Cursor.SW_RESIZE);
            cursor = 6;
        });

        cir7.setOnMouseMoved(e -> {
            paper.setCursor(Cursor.S_RESIZE);
            cursor = 7;
        });

        cir8.setOnMouseMoved(e -> {
            paper.setCursor(Cursor.SE_RESIZE);
            cursor = 8;
        });

        cir_arr.add(cir1);
        cir_arr.add(cir2);
        cir_arr.add(cir3);
        cir_arr.add(cir4);
        cir_arr.add(cir5);
        cir_arr.add(cir6);
        cir_arr.add(cir7);
        cir_arr.add(cir8);

        for (Circle cir : cir_arr)
            cir.setOnMouseExited(e -> {
                cursor = -1;
            });

        setDrag();
    }

    public void setDrag() {
        this.addEventHandler(MouseEvent.ANY, event -> {
            if (MouseEvent.MOUSE_CLICKED == event.getEventType()) {
                paper_controller.setFocusObject(this);
            } else if (MouseEvent.MOUSE_MOVED == event.getEventType()) {
                paper.setClick(false);
                if (type.equals("text") && !isBorder(event)) {
                    paper.setCursor(Cursor.TEXT);
                } else if (cursor == -1) {
                    paper.setCursor(Cursor.MOVE);
                }

            } else if (MouseEvent.MOUSE_PRESSED == event.getEventType()) {
                paper_controller.setFocusObject(this);
                if (this.contains(event.getX(), event.getY())) {
                    this.lastMouseX = event.getSceneX();
                    this.lastMouseY = event.getSceneY();
                    if (!(type.equals("text") && !isBorder(event)) && cursor == -1) {
                        paper.setCursor(Cursor.MOVE);
                        this.dragging = true;
                    }

                    event.consume();
                }
            } else if (MouseEvent.MOUSE_DRAGGED == event.getEventType()) {
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
                }else if(cursor != -1){
                    //resize
                }
            } else if (MouseEvent.MOUSE_RELEASED == event.getEventType()) {
                paper_controller.setFocusObject(this);
                if (this.dragging) {
                    event.consume();
                    this.dragging = false;
                }else if(cursor != -1){
                    //resize
                }
            } else if (MouseEvent.MOUSE_EXITED == event.getEventType()) {
                paper.setCursor(Cursor.DEFAULT);
                paper.setClick(true);
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
