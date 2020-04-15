package InsertObj;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;

public class CropImage extends GridPane {
    private ImageView image;
    private Rectangle2D rectangle;
    private int axis_x = 0;
    private int axis_y = 0;
    private int crop_width = 0;
    private int crop_height = 0;
    private boolean isCropping = false;

    List<Rectangle> crop_arr = new ArrayList<>();
    List<Cursor> cursor_arr = new ArrayList<>();


    public CropImage(ImageView pixel) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../FXML/CropImg.fxml"));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();

            this.image = image;
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

        setCrop(0, 0, (int) image.getFitWidth(), (int) image.getFitHeight());
        setStartCrop(false);

        crop_pane.getChildren().add(image);
    }

    public void setCrop(int x, int y, int width, int height) {
        axis_x = x;
        axis_y = y;
        crop_height = height;
        crop_width = width;

        rectangle = new Rectangle2D(axis_x, axis_y, crop_width, crop_height);
        image.setViewport(rectangle);
        image.setSmooth(true);
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
}
