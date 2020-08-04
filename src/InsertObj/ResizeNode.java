package InsertObj;

import Controller.PaperController;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;

public class ResizeNode extends BasicNode {
    private String type;
    private PaperController paper_controller = PaperController.getInstance();
    private Paper paper = paper_controller.getCurentPaper();
    private double lastMouseX = 0, lastMouseY = 0, minW = 0, minH = 0, lastRotateX = 0, lastRotateY = 0;
    private boolean dragging = false, cropping = false, rotating = false;
    public BooleanProperty isCrop = new SimpleBooleanProperty(false);
    private int cursor = -1;
    private Cursor rotate_cursor;
    private Rotate rotate;

    private VBox rotate_vbox;
    private Pane node_rotate;


    public ResizeNode(String type) {
        super(type);
        this.type = type;

        super.allInit();
    }

    @FXML
    VBox out_vbox;

    @FXML
    public void initialize() {
        rotate_vbox = new VBox();
        rotate_vbox.setPrefWidth(20);
        rotate_vbox.setPrefHeight(50);
        node_rotate = new Pane();

        Image img = new Image(getClass().getResource("../pic/rotate.png").toString());
        ImageView imgView = new ImageView();
        imgView.setImage(img);
        imgView.setFitWidth(25);
        imgView.setFitHeight(25);
        node_rotate.getChildren().add(imgView);

        Line line = new Line();
        line.setStartX(0);
        line.setStartY(0);
        line.setEndX(0);
        line.setEndY(20);

        rotate_vbox.setAlignment(Pos.CENTER);
        rotate_vbox.getChildren().add(node_rotate);
        rotate_vbox.getChildren().add(line);
        out_vbox.getChildren().add(0, rotate_vbox);

        Image tem = new Image("pic/rotate_cursor.png");
        rotate_cursor = new ImageCursor(tem, tem.getWidth() / 2, tem.getHeight() / 2);

        rotate = new Rotate();
        this.getTransforms().add(rotate);

        this.setDrag();
    }

    public VBox getRotate_vbox() {
        return rotate_vbox;
    }

    public Rotate getNodeRotate() {
        return rotate;
    }

    public boolean getIsRotating() {
        return rotating;
    }

    public void setIsCropping(boolean input) {
        isCrop.set(input);
    }

    @Override
    public void setMinW(double w, boolean set) {
        if (type.equals("text")) {
            if (set) this.getMain_content().setMinWidth(w);
            minW = w;
        } else if (type.equals("picture")) {
            this.getMain_content().setPrefWidth(w);
            minW = 0;
        }

    }

    @Override
    public void setMinH(double h, boolean set) {
        if (type.equals("text")) {
            if (set) this.getMain_content().setMinHeight(h);
            minH = h;
        } else if (type.equals("picture")) {
            this.getMain_content().setPrefHeight(h);
            minH = 0;
        }
    }

    public void setNodeRotate(double degree) {
        rotate.setPivotX(this.getWidth() / 2);
        rotate.setPivotY(this.getHeight() / 2);

        rotate.setAngle(degree);
    }

    @Override
    public void setDrag() {
        getGridpane().addEventHandler(MouseEvent.ANY, event -> {
            if (MouseEvent.MOUSE_CLICKED == event.getEventType()) {
                paper_controller.setFocusObject(this);
            } else if (MouseEvent.MOUSE_MOVED == event.getEventType()) {
                paper.setClick(false);

                cursor = getResize(event, 15);

                if (type.equals("text") && !isBorder(event)) {
                    paper.setCursor(Cursor.TEXT);
                } else if (!isCrop.getValue() && cursor == -1) {
                    paper.setCursor(Cursor.MOVE);
                } else if (!isCrop.getValue()) {
                    paper.setCursor(cursor_arr.get(cursor));
                }

                if (isCrop.getValue()) cropping = true;

            } else if (MouseEvent.MOUSE_PRESSED == event.getEventType()) {
                paper_controller.setFocusObject(this);
                if (getGridpane().contains(event.getX(), event.getY())) {
                    this.lastMouseX = event.getSceneX();
                    this.lastMouseY = event.getSceneY();

                    if (!(type.equals("text") && !isBorder(event))) {
                        if (cursor == -1 && !isCrop.getValue())
                            paper.setCursor(Cursor.MOVE);
                        this.dragging = true;
                    }

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
                } else if (this.dragging && !isCrop.getValue()) {
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

                    if (type.equals("text")) {
                        minX = (minWidth + moveMinX) > minW;
                        maxX = (minWidth + moveMaxX) > minW;
                        minY = (minHeight + moveMinY) > minH;
                        maxY = (minHeight + moveMaxY) > minH;
                    } else if (type.equals("picture")) {
                        minX = prefWidth > 0;
                        maxX = prefWidth > 0;
                        minY = prefHeight > 0;
                        maxY = prefHeight > 0;
                    }

                    switch (cursor) {
                        case 0:
                            if (type.equals("text")) {
                                if (minX) this.getMain_content().setMinWidth(minWidth + moveMinX);
                                if (minY) this.getMain_content().setMinHeight(minHeight + moveMinY);
                            } else if (type.equals("picture")) {
                                this.getMain_content().setPrefWidth(Math.max((prefWidth + moveMinX), 0));
                                this.getMain_content().setPrefHeight(Math.max((prefHeight + moveMinY), 0));
                            }

                            if (minX) this.setTranslateX(initialLayoutX - moveMinX);
                            if (minY) this.setTranslateY(initialLayoutY - moveMinY);
                            break;
                        case 1:
                            if (type.equals("text")) {
                                if (minY) this.getMain_content().setMinHeight(minHeight + moveMinY);
                            } else if (type.equals("picture"))
                                this.getMain_content().setPrefHeight(Math.max((prefHeight + moveMinY), 0));

                            if (minY) this.setTranslateY(initialLayoutY - moveMinY);
                            break;
                        case 2:
                            if (type.equals("text")) {
                                if (maxX) this.getMain_content().setMinWidth(width + moveMaxX);

                                if (minY) this.getMain_content().setMinHeight(minHeight + moveMinY);
                            } else if (type.equals("picture")) {
                                this.getMain_content().setPrefWidth(Math.max((width + moveMaxX), 0));
                                this.getMain_content().setPrefHeight(Math.max((prefHeight + moveMinY), 0));
                            }

                            if (minY) this.setTranslateY(initialLayoutY - moveMinY);
                            break;
                        case 3:
                            if (type.equals("text")) {
                                if (minX) this.getMain_content().setMinWidth(minWidth + moveMinX);
                            } else if (type.equals("picture"))
                                this.getMain_content().setPrefWidth(Math.max((prefWidth + moveMinX), 0));

                            if (minX) this.setTranslateX(initialLayoutX - moveMinX);
                            break;
                        case 4:
                            if (type.equals("text"))
                                this.getMain_content().setMinWidth(Math.max((width + moveMaxX), minW));
                            else if (type.equals("picture"))
                                this.getMain_content().setPrefWidth(Math.max((width + moveMaxX), 0));
                            break;
                        case 5:
                            if (type.equals("text")) {
                                if (minX) this.getMain_content().setMinWidth(minWidth + moveMinX);

                                if (maxY) this.getMain_content().setMinHeight(height + moveMaxY);
                            } else if (type.equals("picture")) {
                                this.getMain_content().setPrefWidth(Math.max((prefWidth + moveMinX), 0));
                                this.getMain_content().setPrefHeight(Math.max((height + moveMaxY), 0));
                            }

                            if (minX) this.setTranslateX(initialLayoutX - moveMinX);
                            break;
                        case 6:
                            if (type.equals("text"))
                                this.getMain_content().setMinHeight(Math.max((height + moveMaxY), minH));
                            else if (type.equals("picture"))
                                this.getMain_content().setPrefHeight(Math.max((height + moveMaxY), 0));

                            break;
                        case 7:
                            if (type.equals("text")) {
                                this.getMain_content().setMinWidth(Math.max((width + moveMaxX), minW));
                                this.getMain_content().setMinHeight(Math.max((height + moveMaxY), minH));
                            } else if (type.equals("picture")) {
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
                if (!dragging && !rotating) {
                    paper.setCursor(Cursor.DEFAULT);
                    paper.setClick(true);
                }

                if (isCrop.getValue()) cropping = false;
            }
        });

        node_rotate.addEventHandler(MouseEvent.ANY, event -> {
            if (MouseEvent.MOUSE_MOVED == event.getEventType()) {
                paper.setCursor(rotate_cursor);
            } else if (MouseEvent.MOUSE_CLICKED == event.getEventType()) {
                paper_controller.setFocusObject(this);
            } else if (MouseEvent.MOUSE_PRESSED == event.getEventType()) {
                paper_controller.setFocusObject(this);
                rotating = true;
                this.lastRotateX = event.getSceneX();
                this.lastRotateY = event.getSceneY();

                rotate.setPivotX(this.getWidth() / 2);
                rotate.setPivotY(this.getHeight() / 2);

            } else if (MouseEvent.MOUSE_DRAGGED == event.getEventType()) {
                paper_controller.setFocusObject(this);
                if (rotating) {
                    Transform localToScene = getLocalToSceneTransform();

                    double px = rotate.getPivotX() + localToScene.getTx();
                    double py = rotate.getPivotY() + localToScene.getTy();

                    double th1 = clockAngle(lastRotateX, lastRotateY, px, py);
                    double th2 = clockAngle(event.getSceneX(), event.getSceneY(), px, py);

                    double angle = rotate.getAngle();
                    angle += th2 - th1;

                    rotate.setAngle(angle);

                    this.lastRotateX = event.getSceneX();
                    this.lastRotateY = event.getSceneY();
                }
            } else if (MouseEvent.MOUSE_RELEASED == event.getEventType()) {
                if (rotating) {
                    event.consume();
                    rotating = false;
                }
            } else if (MouseEvent.MOUSE_EXITED == event.getEventType()) {
                if (!rotating)
                    paper.setCursor(Cursor.DEFAULT);
            }

        });
    }

    public double clockAngle(double x, double y, double px, double py) {
        double dx = x - px;
        double dy = y - py;

        double angle = Math.abs(Math.toDegrees(Math.atan2(dy, dx)));

        if (dy < 0) {
            angle = 360 - angle;
        }

        return angle;
    }

    @Override
    public void focus_border(boolean show) {
        if (show) {
            getGridpane().getStyleClass().clear();
            getGridpane().getStyleClass().add("border_focus");

            getRotate_vbox().getStyleClass().clear();
            getRotate_vbox().getStyleClass().add("show");

            for (Circle cir : cir_arr) {
                cir.getStyleClass().clear();
                cir.getStyleClass().add("show");
            }
        } else {
            getGridpane().getStyleClass().clear();
            getGridpane().getStyleClass().add("border_none");

            getRotate_vbox().getStyleClass().clear();
            getRotate_vbox().getStyleClass().add("hide");

            for (Circle cir : cir_arr) {
                cir.getStyleClass().clear();
                cir.getStyleClass().add("hide");
            }

            if (isCrop.getValue() && !cropping) setIsCropping(false);
        }
    }
}
