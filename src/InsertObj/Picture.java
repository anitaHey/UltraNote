package InsertObj;

import javafx.scene.image.ImageView;

public class Picture extends ResizeNode {
    private String picture_path;

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
}
