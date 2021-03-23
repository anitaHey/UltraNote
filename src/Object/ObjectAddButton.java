package Object;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;

public class ObjectAddButton extends HBox {
    public ObjectAddButton(){
        try {
            FXMLLoader loader;
            loader = new FXMLLoader(getClass().getResource("../FXML/ObjectAddBtn.fxml"));

            loader.setController(this);
            loader.setRoot(this);
            loader.load();

        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
