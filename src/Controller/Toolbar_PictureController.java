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
    MenuItem picture_border05, picture_border1, picture_border15, picture_border2, picture_border3, picture_border5, picture_border0, picture_dot_0, picture_dot_1_10, picture_dot_5_10, picture_dot_10_10, picture_dot_17_17;
    @FXML
    HBox hbox_border05, hbox_border1, hbox_border15, hbox_border2, hbox_border3, hbox_border5, hbox_border0, hbox_dot_0, hbox_dot_1_10, hbox_dot_5_10, hbox_dot_10_10, hbox_dot_17_17;
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

        picture_crop.setOnMouseClicked(e -> {

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

    public void setSelectPicture(Picture input) {
        getInstance().selectPicture = input;
    }
}
