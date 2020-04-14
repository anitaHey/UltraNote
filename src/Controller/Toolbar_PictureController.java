package Controller;

import InsertObj.Picture;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.HBox;

public class Toolbar_PictureController {
    private static Toolbar_PictureController instance;
    private Picture selectPicture = null;
    private boolean check = true;
    private double last_width = 0;

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
    MenuItem picture_border05, picture_border1, picture_border15, picture_border2, picture_border3, picture_border5, picture_border0;
    @FXML
    HBox hbox_border05, hbox_border1, hbox_border15, hbox_border2, hbox_border3, hbox_border5, hbox_border0;

    @FXML
    public void initialize() {
        setInstance(this);
        picture_border05.setOnAction(e->{
            change_border(0.5, true);
        });
        picture_border1.setOnAction(e->{
            change_border(1, true);
        });
        picture_border15.setOnAction(e->{
            change_border(1.5, true);
        });
        picture_border2.setOnAction(e->{
            change_border(2, true);
        });
        picture_border3.setOnAction(e->{
            change_border(3, true);
        });
        picture_border5.setOnAction(e->{
            change_border(5, true);
        });
        picture_border0.setOnAction(e->{
            change_border(0, true);
        });

        hbox_border05.setOnMouseEntered(e->{
            change_border(0.5, false);
        });
        hbox_border1.setOnMouseEntered(e->{
            change_border(1, false);
        });
        hbox_border15.setOnMouseEntered(e->{
            change_border(1.5, false);
        });
        hbox_border2.setOnMouseEntered(e->{
            change_border(2, false);
        });
        hbox_border3.setOnMouseEntered(e->{
            change_border(3, false);
        });
        hbox_border5.setOnMouseEntered(e->{
            change_border(5, false);
        });
        hbox_border0.setOnMouseEntered(e->{
            change_border(0, false);
        });

        hbox_border05.setOnMouseExited(e->{
            border_restore();
        });
        hbox_border1.setOnMouseExited(e->{
            border_restore();
        });
        hbox_border15.setOnMouseExited(e->{
            border_restore();
        });
        hbox_border2.setOnMouseExited(e->{
            border_restore();
        });
        hbox_border3.setOnMouseExited(e->{
            border_restore();
        });
        hbox_border5.setOnMouseExited(e->{
            border_restore();
        });
        hbox_border0.setOnMouseExited(e->{
            border_restore();
        });
    }

    public void change_border(double size, boolean change){
        if(getInstance().selectPicture != null){
            if(change) {
                getInstance().last_width = getInstance().selectPicture.getBorderWidth();
            }

            getInstance().selectPicture.setBorderWidth(size);
        }
    }

    public void border_restore(){
        if(getInstance().selectPicture != null)
            getInstance().selectPicture.setBorderWidth(getInstance().last_width);
    }

    public void setSelectPicture(Picture input){
        getInstance().selectPicture = input;
    }
}
