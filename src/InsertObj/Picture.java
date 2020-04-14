package InsertObj;

import Controller.MainController;
import Controller.Toolbar_PictureController;
import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class Picture extends ResizeNode {
    private static Toolbar_PictureController picture_controller = Toolbar_PictureController.getInstance();
    ImageView image;
    private String picture_path;
    private double borderWidth = 0;
    private String borderColor = "#000";
    private String borderType = "solid";
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
            if(setBorder[0])
                image.setFitWidth(newValue.doubleValue()-borderWidth*2);
            setBorder[0] = true;
        });

        getMain_content().heightProperty().addListener((obs, oldValue, newValue) -> {
            if(setBorder[1])
                image.setFitHeight(newValue.doubleValue()-borderWidth*2);
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

    public void setBorder(double width, String color, String type){
        borderWidth = width;
        borderColor = color;
        borderType = type;

        setBorder[0] = false;
        setBorder[1] = false;
        getMain_content().setPrefHeight(image.getFitHeight() + borderWidth*2);
        getMain_content().setPrefWidth(image.getFitWidth() + borderWidth*2);
        image.setX(borderWidth);
        image.setY(borderWidth);

        getMain_content().setStyle("-fx-border-color: "+ borderColor + ";"+"-fx-border-style: " + borderType + ";"+"-fx-border-width: "+ borderWidth +";");
    }

    public void setBorderWidth(double width){
        borderWidth = width;

        setBorder[0] = false;
        setBorder[1] = false;
        getMain_content().setPrefHeight(image.getFitHeight() + borderWidth*2);
        getMain_content().setPrefWidth(image.getFitWidth() + borderWidth*2);
        image.setX(borderWidth);
        image.setY(borderWidth);

        getMain_content().setStyle("-fx-border-color: "+ borderColor + ";"+"-fx-border-style: " + borderType + ";"+"-fx-border-width: "+ borderWidth +";");
    }

    public void setBorderColor(String color){
        borderColor = color;

        getMain_content().setStyle("-fx-border-color: "+ borderColor + ";"+"-fx-border-style: " + borderType + ";"+"-fx-border-width: "+ borderWidth +";");
    }

    public void setBorderType(String type){
        borderType = type;

        getMain_content().setStyle("-fx-border-color: "+ borderColor + ";"+"-fx-border-style: " + borderType + ";"+"-fx-border-width: "+ borderWidth +";");
    }

    public double getBorderWidth(){
        return borderWidth;
    }
}
