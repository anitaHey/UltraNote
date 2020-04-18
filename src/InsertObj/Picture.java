package InsertObj;

import Controller.MainController;
import Controller.PaperController;
import Controller.Toolbar_PictureController;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;

public class Picture extends ResizeNode {
    private static Toolbar_PictureController picture_controller = Toolbar_PictureController.getInstance();
    private ImageView image;
    private String picture_path;

    private CropImage crop_img = null;
    private double borderWidth = 0;
    private String borderColor = "#000";
    private String borderType = "segments(1,1,1,1)  line-cap round";
    private int[] borderTypeNum = {1, 1};
    private boolean[] setBorder = {true, true};

    public Picture(String path) {
        super("picture");
        this.picture_path = path;

        Init();
    }

    public void Init() {
        image = new ImageView(picture_path);
        image.setSmooth(true);

        setMinH(image.getImage().getHeight(), false);
        setMinW(image.getImage().getWidth(), false);

        getMain_content().widthProperty().addListener((obs, oldValue, newValue) -> {
            if (setBorder[0]) {
                image.setFitWidth(newValue.doubleValue() - borderWidth * 2);
            }

            setBorder[0] = true;
        });

        getMain_content().heightProperty().addListener((obs, oldValue, newValue) -> {
            if (setBorder[1]) {
                image.setFitHeight(newValue.doubleValue() - borderWidth * 2);
            }

            setBorder[1] = true;
        });

        getMain_content().getChildren().add(image);

        picture_controller.setSelectPicture(this);
        MainController.getInstance().change_toolbar(MainController.Type.Picture, false);

        this.setOnMouseClicked(e -> {
            picture_controller.setSelectPicture(this);
            MainController.getInstance().change_toolbar(MainController.Type.Picture, false);
        });
    }

    public void setMainSize(double width, double height){
        setBorder[0] = false;
        setBorder[1] = false;
        getMain_content().setPrefHeight(height);
        getMain_content().setPrefWidth(width);
    }

    public void setBorder(double width, String color, int num1, int num2) {
        borderTypeNum[0] = num1;
        borderTypeNum[1] = num2;

        borderWidth = width;
        borderColor = color;
        borderType = String.format("segments(%d,%d,%d,%d)  line-cap round", num1, num2, num1, num2);

        setMainSize((image.getFitHeight() + borderWidth * 2), (image.getFitWidth() + borderWidth * 2));
        image.setX(borderWidth);
        image.setY(borderWidth);

        getMain_content().setStyle("-fx-border-color: " + borderColor + ";" + "-fx-border-style: " + borderType + ";" + "-fx-border-width: " + borderWidth + ";");
    }

    public void setBorderWidth(double width) {
        borderWidth = width;

        setMainSize((image.getFitHeight() + borderWidth * 2), (image.getFitWidth() + borderWidth * 2));
        image.setX(borderWidth);
        image.setY(borderWidth);

        getMain_content().setStyle("-fx-border-color: " + borderColor + ";" + "-fx-border-style: " + borderType + ";" + "-fx-border-width: " + borderWidth + ";");
    }

    public void setBorderColor(String color) {
        if (borderWidth == 0) setBorderWidth(1);
        borderColor = color;

        getMain_content().setStyle("-fx-border-color: " + borderColor + ";" + "-fx-border-style: " + borderType + ";" + "-fx-border-width: " + borderWidth + ";");
    }

    public void setBorderType(int num1, int num2) {
        if (borderWidth == 0) setBorderWidth(1);

        borderTypeNum[0] = num1;
        borderTypeNum[1] = num2;
        borderType = String.format("segments(%d,%d,%d,%d)  line-cap round", num1, num2, num1, num2);

        getMain_content().setStyle("-fx-border-color: " + borderColor + ";" + "-fx-border-style: " + borderType + ";" + "-fx-border-width: " + borderWidth + ";");
    }

    public double getBorderWidth() {
        return borderWidth;
    }

    public int[] getBorderTypeNum() {
        return new int[]{borderTypeNum[0], borderTypeNum[1]};
    }

    public ImageView getPictureImage() {
        return image;
    }

    public void startCrop() {
        if (crop_img == null)
            crop_img = new CropImage(getPictureImage(), borderWidth);
        else
            crop_img.setInitImg(getPictureImage().getFitWidth(), getPictureImage().getFitHeight(), borderWidth);

        setIsCropping(true);
        getMain_content().getChildren().clear();
        getMain_content().getChildren().add(crop_img.getCropBackground());
        getMain_content().getChildren().add(crop_img);
        crop_img.toFront();
        crop_img.getCropBackground().toBack();

        setMainSize(crop_img.getImage_width(), crop_img.getImage_height());
    }
}
