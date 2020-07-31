package Controller;

import InsertObj.Picture;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.Arrays;

public class Toolbar_PictureController {
    private static Toolbar_PictureController instance;
    private Picture selectPicture = null;
    private double last_width = 0;
    private double last_degree = 0;
    private int[] last_scale = {1, 1};
    private int[] last_dot = {1, 1};

    public static Toolbar_PictureController getInstance() {
        if (instance == null) {
            instance = new Toolbar_PictureController();
        }
        return instance;
    }

    public static void setInstance(Toolbar_PictureController newInstance) {
        instance = newInstance;
    }

    @FXML
    MenuItem picture_border05, picture_border1, picture_border15, picture_border2, picture_border3, picture_border5, picture_border0, picture_dot_0, picture_dot_1_10, picture_dot_5_10, picture_dot_10_10, picture_dot_17_17, picture_rotate_left, picture_rotate_right, picture_rotate_vir, picture_rotate_hor;
    @FXML
    HBox hbox_border05, hbox_border1, hbox_border15, hbox_border2, hbox_border3, hbox_border5, hbox_border0, hbox_dot_0, hbox_dot_1_10, hbox_dot_5_10, hbox_dot_10_10, hbox_dot_17_17, hbox_rotate_left, hbox_rotate_right, hbox_rotate_vir, hbox_rotate_hor;
    @FXML
    VBox picture_crop;

    @FXML
    public void initialize() {
        setInstance(this);
        picture_border05.setOnAction(e -> change_border(0.5, true));
        picture_border1.setOnAction(e -> change_border(1, true));
        picture_border15.setOnAction(e -> change_border(1.5, true));
        picture_border2.setOnAction(e -> change_border(2, true));
        picture_border3.setOnAction(e -> change_border(3, true));
        picture_border5.setOnAction(e -> change_border(5, true));
        picture_border0.setOnAction(e -> change_border(0, true));

        hbox_border05.setOnMouseEntered(e -> change_border(0.5, false));
        hbox_border1.setOnMouseEntered(e -> change_border(1, false));
        hbox_border15.setOnMouseEntered(e -> change_border(1.5, false));
        hbox_border2.setOnMouseEntered(e -> change_border(2, false));
        hbox_border3.setOnMouseEntered(e -> change_border(3, false));
        hbox_border5.setOnMouseEntered(e -> change_border(5, false));
        hbox_border0.setOnMouseEntered(e -> change_border(0, false));

        hbox_border05.setOnMouseExited(e -> border_restore());
        hbox_border1.setOnMouseExited(e -> border_restore());
        hbox_border15.setOnMouseExited(e -> border_restore());
        hbox_border2.setOnMouseExited(e -> border_restore());
        hbox_border3.setOnMouseExited(e -> border_restore());
        hbox_border5.setOnMouseExited(e -> border_restore());
        hbox_border0.setOnMouseExited(e -> border_restore());

        picture_dot_0.setOnAction(e -> change_dot(1, 1, true));
        picture_dot_1_10.setOnAction(e -> change_dot(1, 10, true));
        picture_dot_5_10.setOnAction(e -> change_dot(5, 10, true));
        picture_dot_10_10.setOnAction(e -> change_dot(10, 10, true));
        picture_dot_17_17.setOnAction(e -> change_dot(17, 17, true));

        hbox_dot_0.setOnMouseEntered(e -> change_dot(1, 1, false));
        hbox_dot_1_10.setOnMouseEntered(e -> change_dot(1, 10, false));
        hbox_dot_5_10.setOnMouseEntered(e -> change_dot(5, 10, false));
        hbox_dot_10_10.setOnMouseEntered(e -> change_dot(10, 10, false));
        hbox_dot_17_17.setOnMouseEntered(e -> change_dot(17, 17, false));

        hbox_dot_0.setOnMouseExited(e -> dot_restore());
        hbox_dot_1_10.setOnMouseExited(e -> dot_restore());
        hbox_dot_5_10.setOnMouseExited(e -> dot_restore());
        hbox_dot_10_10.setOnMouseExited(e -> dot_restore());
        hbox_dot_17_17.setOnMouseExited(e -> dot_restore());

        picture_rotate_left.setOnAction(e -> picture_rotate(-90, true));
        picture_rotate_right.setOnAction(e -> picture_rotate(90, true));
        picture_rotate_vir.setOnAction(e -> picture_scale(true, false, true));
        picture_rotate_hor.setOnAction(e -> picture_scale(false, true, true));

        hbox_rotate_left.setOnMouseEntered(e -> picture_rotate(-90, false));
        hbox_rotate_right.setOnMouseEntered(e -> picture_rotate(90, false));
        hbox_rotate_vir.setOnMouseEntered(e -> picture_scale(true, false, false));
        hbox_rotate_hor.setOnMouseEntered(e -> picture_scale(false, true, false));

        hbox_rotate_left.setOnMouseExited(e -> rotate_restore());
        hbox_rotate_right.setOnMouseExited(e -> rotate_restore());
        hbox_rotate_vir.setOnMouseExited(e -> scale_restore());
        hbox_rotate_hor.setOnMouseExited(e -> scale_restore());

        picture_crop.setOnMouseClicked(e -> {
            if (getInstance().selectPicture != null) {
                getInstance().selectPicture.startCrop();
            }
        });
    }

    public void change_border(double size, boolean change) {
        if (getInstance().selectPicture != null) {
            if (change) {
                getInstance().last_width = getInstance().selectPicture.getBorderWidth();
            }

            getInstance().selectPicture.setBorderWidth(size);
        }
    }

    public void border_restore() {
        if (getInstance().selectPicture != null)
            getInstance().selectPicture.setBorderWidth(getInstance().last_width);
    }

    public void dot_restore() {
        if (getInstance().selectPicture != null)
            getInstance().selectPicture.setBorderType(getInstance().last_dot[0], getInstance().last_dot[1]);
    }

    public void change_dot(int num1, int num2, boolean change) {
        if (getInstance().selectPicture != null) {
            if (change) {
                getInstance().last_dot = getInstance().selectPicture.getBorderTypeNum();
            }

            getInstance().selectPicture.setBorderType(num1, num2);
        }
    }

    public void rotate_restore() {
        if (getInstance().selectPicture != null)
            getInstance().selectPicture.setNodeRotate(getInstance().last_degree);
    }

    public void picture_rotate(int degree, boolean change) {
        if (getInstance().selectPicture != null) {
            if (change) {
                getInstance().last_degree = getInstance().selectPicture.getNodeRotate().getAngle();
            }

            getInstance().selectPicture.setNodeRotate(getInstance().last_degree + degree);
        }
    }

    public void scale_restore() {
        if (getInstance().selectPicture != null)
            getInstance().selectPicture.setPicScale(getInstance().last_scale[0], getInstance().last_scale[1]);
    }

    public void picture_scale(boolean scaleX, boolean scaleY, boolean change) {
        if (getInstance().selectPicture != null) {
            if (change) {
                getInstance().last_scale = getInstance().selectPicture.getPicScale();
            }

            getInstance().selectPicture.setPicScale(scaleX, scaleY);
        }
    }

    public void setSelectPicture(Picture input) {
        getInstance().selectPicture = input;
    }
}
