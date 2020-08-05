package Controller;

import javafx.fxml.FXML;

public class Toolbar_CodeController {
    private static Toolbar_CodeController instance;

    public static Toolbar_CodeController getInstance() {
        if (instance == null) {
            instance = new Toolbar_CodeController();
        }
        return instance;
    }

    public static void setInstance(Toolbar_CodeController newInstance) {
        instance = newInstance;
    }

    @FXML
    public void initialize() {
        setInstance(this);
    }
}
