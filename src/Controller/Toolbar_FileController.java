package Controller;

import javafx.fxml.FXML;

public class Toolbar_FileController {
    private static Toolbar_FileController instance;

    public static Toolbar_FileController getInstance() {
        if (instance == null) {
            instance = new Toolbar_FileController();
        }
        return instance;
    }

    public static void setInstance(Toolbar_FileController newInstance) {
        instance = newInstance;
    }

    @FXML
    public void initialize() {
        setInstance(this);
    }
}
