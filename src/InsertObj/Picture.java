package InsertObj;
import javafx.scene.image.ImageView;

public class Picture extends ResizeNode{
    private String picture_path;
    public Picture(String path){
        super("picture");

        this.picture_path = path;

        Init();
    }

    public void Init(){
        ImageView image = new ImageView(picture_path);
        getMain_content().getChildren().add(image);
    }
}
