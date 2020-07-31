package Controller;

import InsertObj.Text_box;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public class Toolbar_ViewController {
    private static Toolbar_ViewController instance;

    public static Toolbar_ViewController getInstance() {
        if (instance == null) {
            instance = new Toolbar_ViewController();
        }
        return instance;
    }

    public static void setInstance(Toolbar_ViewController newInstance) {
        instance = newInstance;
    }

    @FXML
    public void initialize() {
        setInstance(this);
    }
}
