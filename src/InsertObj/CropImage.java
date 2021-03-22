package InsertObj;

import Controller.PaperController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

public class CropImage extends GridPane {
    private PaperController paper_controller = PaperController.getInstance();
    private Paper paper = paper_controller.getCurrentPaper();

    private ImageView crop_pic;
    private ImageView background;
    private Rectangle2D rectangle;
    private ByteArrayInputStream inputStream;
    private int axis_x = 0, axis_y = 0;
    private int crop_width = 0, crop_height = 0;
    private double image_width = 0, image_height = 0;
    private double last_width = 0, last_height = 0;
    private boolean set = true;

    private List<Rectangle> crop_arr = new ArrayList<>();
    private List<Cursor> cursor_arr = new ArrayList<>();
    private int cursor;
    private boolean isCropping = false;

    public CropImage(ImageView crop_pic, ByteArrayInputStream inputStream, double border, double width, double height) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../FXML/CropImg.fxml"));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();

            this.background = new ImageView(crop_pic.getImage());
            this.crop_pic = crop_pic;
            this.inputStream = inputStream;

            this.setLayoutX(border);
            this.setLayoutY(border);
            background.setLayoutX(border);
            background.setLayoutY(border);

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

            cursor_arr.add(setCursor("pic/crop_cursor0.png"));
            cursor_arr.add(setCursor("pic/crop_cursor1.png"));
            cursor_arr.add(setCursor("pic/crop_cursor2.png"));
            cursor_arr.add(setCursor("pic/crop_cursor3.png"));
            cursor_arr.add(setCursor("pic/crop_cursor4.png"));
            cursor_arr.add(setCursor("pic/crop_cursor5.png"));
            cursor_arr.add(setCursor("pic/crop_cursor6.png"));
            cursor_arr.add(setCursor("pic/crop_cursor7.png"));

            this.crop_pic.setX(0);
            this.crop_pic.setY(0);

            image_width = width;
            image_height = height;

            inputStream.reset();
            Image image = new Image(inputStream, image_width, image_height, false, false);
            this.crop_pic.setImage(image);

            this.background.setFitHeight(image_height);
            this.background.setFitWidth(image_width);

            setCrop(0, 0, image_width, image_height);

            getCrop_pane().getChildren().clear();
            getCrop_pane().getChildren().add(this.crop_pic);

            for (Rectangle rec : crop_arr)
                rec.toFront();

            ColorAdjust blackout = new ColorAdjust();
            blackout.setBrightness(-0.5);
            background.setEffect(blackout);

            setStartCrop(true);
            setCropDrag();

            crop_pane.heightProperty().addListener((obs, oldVal, newVal) -> {
                if (isCropping)
                    crop_pic.setFitHeight(newVal.doubleValue());
            });

            crop_pane.widthProperty().addListener((obs, oldVal, newVal) -> {
                if (isCropping)
                    crop_pic.setFitWidth(newVal.doubleValue());
            });

        } catch (Exception e) {
            System.out.println(e);
        }

    }

    @FXML
    Rectangle rec00, rec01, rec1, rec20, rec21, rec3, rec4, rec50, rec51, rec6, rec70, rec71;
    @FXML
    Pane crop_pane;

    public void setInitImg(double width, double height, double border) {
        double scaleH = height / last_height;
        double scaleW = width / last_width;

        image_width *= scaleW;
        image_height *= scaleH;
        axis_x *= scaleW;
        axis_y *= scaleH;

        crop_width = (int) width;
        crop_height = (int) height;

        background.setFitWidth(image_width);
        background.setFitHeight(image_height);

        inputStream.reset();
        Image image = new Image(inputStream, image_width, image_height, false, false);
        crop_pic.setImage(image);

        this.setLayoutX(border);
        this.setLayoutY(border);
        background.setLayoutX(border);
        background.setLayoutY(border);

        set = false;
        getCrop_pane().getChildren().clear();
        setCrop(axis_x, axis_y, crop_width, crop_height);
        getCrop_pane().getChildren().add(crop_pic);
        set = true;

        setStartCrop(true);
    }

    public Pane getCrop_pane() {
        return crop_pane;
    }

    public ImageView getCropBackground() {
        return background;
    }

    public double getImage_width() {
        return image_width;
    }

    public double getImage_height() {
        return image_height;
    }

    public int getAxis_x() {
        return axis_x;
    }

    public int getAxis_y() {
        return axis_y;
    }

    public ImageCursor setCursor(String path) {
        Image tem = new Image(path);
        ImageCursor cursor = new ImageCursor(tem, tem.getWidth() / 2, tem.getHeight() / 2);

        return cursor;
    }

    public void setCropDrag() {
        this.addEventHandler(MouseEvent.ANY, event -> {
            if (isCropping) {
                if (MouseEvent.MOUSE_MOVED == event.getEventType()) {
                    cursor = getResize(event, 40);
                    if (cursor != -1)
                        paper.setCursor(cursor_arr.get(cursor));
                    else
                        paper.setCursor(Cursor.DEFAULT);
                } else if (MouseEvent.MOUSE_PRESSED == event.getEventType()) {
                    if (cursor != -1)
                        paper.setCursor(cursor_arr.get(cursor));
                    else
                        paper.setCursor(Cursor.MOVE);
                } else if (MouseEvent.MOUSE_DRAGGED == event.getEventType() && cursor != -1) {
                    double prefWidth = this.getCrop_pane().getPrefWidth();
                    double prefHeight = this.getCrop_pane().getPrefHeight();

                    Bounds pane = this.getBoundsInLocal();
                    double moveMinX = pane.getMinX() - event.getX();
                    double moveMinY = pane.getMinY() - event.getY();
                    double moveMaxX = event.getX() - pane.getMaxX();
                    double moveMaxY = event.getY() - pane.getMaxY();

                    final double initialLayoutX = this.getTranslateX();
                    final double initialLayoutY = this.getTranslateY();

                    double minX = (initialLayoutX - moveMinX < 0) ? 0 : (initialLayoutX - moveMinX);
                    double minW = (initialLayoutX - moveMinX < 0) ? prefWidth : Math.max((prefWidth + moveMinX), 0);

                    double minY = (initialLayoutY - moveMinY < 0) ? 0 : (initialLayoutY - moveMinY);
                    double minH = (initialLayoutY - moveMinY < 0) ? (prefHeight) : Math.max((prefHeight + moveMinY), 0);

                    double maxW = (this.getBoundsInParent().getMaxX() + moveMaxX > image_width) ? prefWidth : Math.max((this.getCrop_pane().getWidth() + moveMaxX), 0);
                    double maxH = (this.getBoundsInParent().getMaxY() + moveMaxY > image_height) ? prefHeight : Math.max((this.getCrop_pane().getHeight() + moveMaxY), 0);

                    switch (cursor) {
                        case 0:
                            setCrop(minX, minY, minW, minH);
                            break;
                        case 1:
                            setCrop(axis_x, minY, crop_width, minH);
                            break;
                        case 2:
                            setCrop(axis_x, minY, maxW, minH);
                            break;
                        case 3:
                            setCrop(minX, axis_y, minW, crop_height);
                            break;
                        case 4:
                            setCrop(axis_x, axis_y, maxW, crop_height);
                            break;
                        case 5:
                            setCrop(minX, axis_y, minW, maxH);
                            break;
                        case 6:
                            setCrop(axis_x, axis_y, crop_width, maxH);
                            break;
                        case 7:
                            setCrop(axis_x, axis_y, maxW, maxH);
                            break;
                    }
                    event.consume();
                } else if (MouseEvent.MOUSE_EXITED == event.getEventType()) {
                    paper.setCursor(Cursor.DEFAULT);
                }
            }
        });
    }

    public void setCrop(double x, double y, double width, double height) {
        if (width < 30) width = 30;
        if (height < 30) height = 30;

        if (x < 0) x = 0;
        else if (x > image_width - 30) x = image_width - 30;

        if (y < 0) y = 0;
        else if (y > image_height - 30) y = image_height - 30;

        axis_x = (int) x;
        axis_y = (int) y;
        crop_height = (int) height;
        crop_width = (int) width;
        last_height = crop_height;
        last_width = crop_width;

        rectangle = new Rectangle2D(axis_x, axis_y, crop_width, crop_height);
        crop_pic.setViewport(rectangle);
        crop_pic.setSmooth(true);

        this.getCrop_pane().setPrefWidth(crop_width);
        this.getCrop_pane().setPrefHeight(crop_height);
        this.setTranslateX(axis_x);
        this.setTranslateY(axis_y);
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

    public int getResize(MouseEvent event, int instance) {
        int output = -1;
        Bounds dragNodeBounds = this.getBoundsInParent();
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
}
