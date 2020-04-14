package InsertObj;

import javafx.scene.image.ImageView;

public class Picture extends ResizeNode {
    private String picture_path;
    private int borderWidth = 1;
    private String borderColor = "#000";
    private String borderType = "solid";
    public Picture(String path) {
        super("picture");

        this.picture_path = path;

        Init();
    }

    public void Init() {
        ImageView image = new ImageView(picture_path);
        image.setSmooth(true);
        setMinH(image.getImage().getHeight(), false);
        setMinW(image.getImage().getWidth(), false);

        getMain_content().widthProperty().addListener((obs, oldValue, newValue) -> {
            image.setFitWidth(newValue.doubleValue());
        });

        getMain_content().heightProperty().addListener((obs, oldValue, newValue) -> {
            image.setFitHeight(newValue.doubleValue());
        });
        getMain_content().getChildren().add(image);
    }

    public void setBorder(int width, String color, String type){
        borderWidth = width;
        borderColor = color;
        borderType = type;

        this.setStyle("-fx-border-color: "+ borderColor + ";"+"-fx-border-style: " + borderType + ";"+"-fx-border-width: "+ borderWidth +";");
    }

    public void setBorderWidth(int width){
        borderWidth = width;

        this.setStyle("-fx-border-color: "+ borderColor + ";"+"-fx-border-style: " + borderType + ";"+"-fx-border-width: "+ borderWidth +";");
    }

    public void setBorderColor(String color){
        borderColor = color;

        this.setStyle("-fx-border-color: "+ borderColor + ";"+"-fx-border-style: " + borderType + ";"+"-fx-border-width: "+ borderWidth +";");
    }

    public void setBorderType(String type){
        borderType = type;

        this.setStyle("-fx-border-color: "+ borderColor + ";"+"-fx-border-style: " + borderType + ";"+"-fx-border-width: "+ borderWidth +";");
    }
}
