package InsertObj;

import Controller.MainController;
import Controller.Toolbar_PictureController;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.*;


public class Picture extends ResizeNode {
    private static Toolbar_PictureController picture_controller = Toolbar_PictureController.getInstance();
    private ImageView image;
    private ByteArrayInputStream inputStream;

    private CropImage crop_img = null;
    private double borderWidth = 0;
    private String borderColor = "#000";
    private String borderType = "segments(1,1,1,1)  line-cap round";
    private int[] borderTypeNum = {1, 1};
    private boolean[] setBorder = {true, true};
    public int[] picScale = {1, 1};

    public Picture(InputStream inputStream) throws IOException {
        super("picture");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        org.apache.commons.io.IOUtils.copy(inputStream, baos);
        byte[] bytes = baos.toByteArray();

        this.inputStream = new ByteArrayInputStream(bytes);
        ;

        Init();
    }

    public void Init() {
        inputStream.reset();
        Image img = new Image(inputStream);

        image = new ImageView(img);
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

        isCrop.addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                getMain_content().getChildren().clear();
                getMain_content().getChildren().add(getPictureImage());
                getPictureImage().setX(borderWidth);
                getPictureImage().setY(borderWidth);

                setMainSize((getPictureImage().getFitWidth() + borderWidth * 2), (getPictureImage().getFitHeight() + borderWidth * 2));
                this.setTranslateX(getTranslateX() + crop_img.getAxis_x());
                this.setTranslateY(getTranslateY() + crop_img.getAxis_y());
            }
        });
    }

    public void setMainSize(double width, double height) {
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

        setMainSize((image.getFitWidth() + borderWidth * 2), (image.getFitHeight() + borderWidth * 2));
        image.setX(borderWidth);
        image.setY(borderWidth);

        getMain_content().setStyle("-fx-border-color: " + borderColor + ";" + "-fx-border-style: " + borderType + ";" + "-fx-border-width: " + borderWidth + ";");
    }

    public void setBorderWidth(double width) {
        borderWidth = width;

        setMainSize((image.getFitWidth() + borderWidth * 2), (image.getFitHeight() + borderWidth * 2));
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

    public int[] getPicScale() {
        return new int[]{picScale[0], picScale[1]};
    }

    public void setPicScale(boolean changeX, boolean changeY) {
        int scaleX = (changeX) ? -picScale[0] : picScale[0];
        int scaleY = (changeY) ? -picScale[1] : picScale[1];

        picScale[0] = scaleX;
        picScale[1] = scaleY;

        this.setScaleX(scaleX);
        this.setScaleY(scaleY);
    }

    public void setPicScale(int scaleX, int scaleY) {
        picScale[0] = scaleX;
        picScale[1] = scaleY;

        this.setScaleX(scaleX);
        this.setScaleY(scaleY);
    }

    public ImageView getPictureImage() {
        return image;
    }

    public ByteArrayInputStream getInputStream() {
        return inputStream;
    }

    ;

    public void startCrop() {
        getMain_content().getChildren().clear();
        if (crop_img == null)
            crop_img = new CropImage(getPictureImage(), inputStream, borderWidth, getPictureImage().getFitWidth(), getPictureImage().getFitHeight());
        else
            crop_img.setInitImg(getPictureImage().getFitWidth(), getPictureImage().getFitHeight(), borderWidth);

        setIsCropping(true);
        getMain_content().getChildren().add(crop_img.getCropBackground());
        getMain_content().getChildren().add(crop_img);

        this.setTranslateX(Math.max(getTranslateX() - crop_img.getAxis_x(), 0));
        this.setTranslateY(Math.max(getTranslateY() - crop_img.getAxis_y(), 0));
        crop_img.toFront();
        crop_img.getCropBackground().toBack();

        setMainSize(crop_img.getImage_width() + borderWidth * 2, crop_img.getImage_height() + borderWidth * 2);
    }
}
