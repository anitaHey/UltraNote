package InsertObj;

import Controller.PaperController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;

public class CropImage extends GridPane {
    private ImageView crop_pic;
    private Rectangle2D rectangle;
    private int axis_x = 0;
    private int axis_y = 0;
    private int crop_width = 0;
    private int crop_height = 0;
    private boolean isCropping = false;
    private double image_width = 0, image_height = 0;

    private PaperController paper_controller = PaperController.getInstance();
    private Paper paper = paper_controller.getCurentPaper();
    private int cursor = -1;

    List<Rectangle> crop_arr = new ArrayList<>();
    List<Cursor> cursor_arr = new ArrayList<>();

    public CropImage(ImageView crop_pic, int x, int y) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../FXML/CropImg.fxml"));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();

            this.crop_pic = crop_pic;
            setCrop(x, y, (int) crop_pic.getFitWidth(), (int) crop_pic.getFitHeight());
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @FXML
    Rectangle rec00, rec01, rec1, rec20, rec21, rec3, rec4, rec50, rec51, rec6, rec70, rec71;
    @FXML
    Pane crop_pane;

    @FXML
    public void initialize() {
        crop_arr.add(rec00);
        crop_arr.add(rec01);
        crop_arr.add(rec1);
        crop_arr.add(rec20);
        crop_arr.add(rec21);
        crop_arr.add(rec3);
        crop_arr.add(rec4);
        crop_arr.add(rec50);
        crop_arr.add(rec51);
        crop_arr.add(rec6);
        crop_arr.add(rec70);
        crop_arr.add(rec71);

        setStartCrop(true);
        System.out.println(crop_pic.getFitHeight()+" "+ crop_pic.getFitWidth());
        crop_pane.getChildren().add(crop_pic);

        cursor_arr.add(new ImageCursor(new Image("../pic/crop_cursor0.png")));
        cursor_arr.add(new ImageCursor(new Image("../pic/crop_cursor1.png")));
        cursor_arr.add(new ImageCursor(new Image("../pic/crop_cursor2.png")));
        cursor_arr.add(new ImageCursor(new Image("../pic/crop_cursor3.png")));
        cursor_arr.add(new ImageCursor(new Image("../pic/crop_cursor4.png")));
        cursor_arr.add(new ImageCursor(new Image("../pic/crop_cursor5.png")));
        cursor_arr.add(new ImageCursor(new Image("../pic/crop_cursor6.png")));
        cursor_arr.add(new ImageCursor(new Image("../pic/crop_cursor7.png")));

        setDrag();
    }

    public Pane getCrop_pane() {
        return crop_pane;
    }

    public void setCrop(int x, int y, int width, int height) {
        axis_x = x;
        axis_y = y;
        crop_height = height;
        crop_width = width;

        rectangle = new Rectangle2D(axis_x, axis_y, crop_width, crop_height);
        crop_pic.setViewport(rectangle);
        crop_pic.setSmooth(true);
    }

    public void setStartCrop(boolean input) {
        if (input) {
            isCropping = true;
            for (Rectangle rec : crop_arr) {
                rec.getStyleClass().clear();
                rec.getStyleClass().add("show");
            }
        } else {
            isCropping = false;
            for (Rectangle rec : crop_arr) {
                rec.getStyleClass().clear();
                rec.getStyleClass().add("hide");
            }
        }
    }

    public void setPictureValue(double width, double height){
        this.image_height = height;
        this.image_width = width;
    }

    public void setDrag() {
        this.addEventHandler(MouseEvent.ANY, event -> {
            if(isCropping){
                if (MouseEvent.MOUSE_MOVED == event.getEventType()) {
                    cursor = getResize(event);

                    if (cursor == -1) {
                        paper.setCursor(Cursor.MOVE);
                    } else {
                        paper.setCursor(cursor_arr.get(cursor));
                    }
                } else if (MouseEvent.MOUSE_PRESSED == event.getEventType()) {
                    if (this.contains(event.getX(), event.getY())) {
                        if (!isBorder(event) && cursor == -1) {
                            paper.setCursor(Cursor.MOVE);
                        }

                        event.consume();
                    }
                } else if (MouseEvent.MOUSE_DRAGGED == event.getEventType() && cursor != -1) {
                    double maxWidth = this.crop_pic.getFitWidth();
                    double maxHeight = this.crop_pic.getFitHeight();

                    double width = this.getCrop_pane().getWidth();
                    double height = this.getCrop_pane().getHeight();

                    double prefWidth = this.getCrop_pane().getPrefWidth();
                    double prefHeight = this.getCrop_pane().getPrefHeight();

                    Bounds pane = this.getBoundsInParent();
                    double moveMinX = pane.getMinX() - event.getSceneX();
                    double moveMinY = pane.getMinY() - event.getSceneY();
                    double moveMaxX = event.getSceneX() - pane.getMaxX();
                    double moveMaxY = event.getSceneY() - pane.getMaxY();

                    boolean minX = pane.getMinX() >= 0;
                    boolean minY = pane.getMinY() >= 0;
                    boolean maxX = pane.getMaxX() <= image_width;
                    boolean maxY = pane.getMaxY() <= image_height;

                    final double initialLayoutX = this.getTranslateX();
                    final double initialLayoutY = this.getTranslateY();

                    boolean checkW, checkH;

                    checkW = prefWidth >= 0 && prefWidth <= maxWidth && minX && maxX;
                    checkH = prefHeight >= 0 && prefHeight <= maxHeight && minY && maxY;

                    switch (cursor) {
                        case 0:
                            if (checkW) {
                                this.getCrop_pane().setPrefWidth(Math.max((prefWidth + moveMinX), 0));
                                this.setTranslateX(initialLayoutX - moveMinX);
                            }

                            if (checkH) {
                                this.getCrop_pane().setPrefHeight(Math.max((prefHeight + moveMinY), 0));
                                this.setTranslateY(initialLayoutY - moveMinY);
                            }

                            break;
                        case 1:
                            if (checkH) {
                                this.getCrop_pane().setPrefHeight(Math.max((prefHeight + moveMinY), 0));
                                this.setTranslateY(initialLayoutY - moveMinY);
                            }

                            break;
                        case 2:
                            if (checkH) {
                                this.getCrop_pane().setPrefHeight(Math.max((prefHeight + moveMinY), 0));
                                this.setTranslateY(initialLayoutY - moveMinY);
                            }
                            if (checkW)
                                this.getCrop_pane().setPrefWidth(Math.max((width + moveMaxX), 0));

                            break;
                        case 3:
                            if (checkW) {
                                this.getCrop_pane().setPrefWidth(Math.max((prefWidth + moveMinX), 0));
                                this.setTranslateX(initialLayoutX - moveMinX);
                            }

                            break;
                        case 4:
                            if (checkW)
                                this.getCrop_pane().setPrefWidth(Math.max((width + moveMaxX), 0));
                            break;
                        case 5:
                            if (checkW) {
                                this.getCrop_pane().setPrefWidth(Math.max((prefWidth + moveMinX), 0));
                                this.setTranslateX(initialLayoutX - moveMinX);
                            }
                            if (checkH)
                                this.getCrop_pane().setPrefHeight(Math.max((height + moveMaxY), 0));

                            break;
                        case 6:
                            if (checkH)
                                this.getCrop_pane().setPrefHeight(Math.max((height + moveMaxY), 0));

                            break;
                        case 7:
                            if (checkW)
                                this.getCrop_pane().setPrefWidth(Math.max((width + moveMaxX), 0));

                            if (checkH)
                                this.getCrop_pane().setPrefHeight(Math.max((height + moveMaxY), 0));

                            break;
                    }
                    event.consume();
                }
            }
        });
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

    public boolean isBorder(MouseEvent event) {
        Bounds dragNodeBounds = this.getBoundsInParent();
        Boolean top = (Math.abs(event.getY()) <= 15);
        Boolean bottom = (Math.abs(event.getY() - dragNodeBounds.getHeight()) <= 15);
        Boolean left = (Math.abs(event.getX()) <= 15);
        Boolean right = (Math.abs(event.getX() - dragNodeBounds.getWidth()) <= 15);

        return top || bottom || left || right;
    }
}
