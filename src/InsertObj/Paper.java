package InsertObj;

import javafx.scene.layout.Pane;

public class Paper extends Pane {
    private static Pane current;

    public static Pane getCurentPaper(){
        return current;
    }

    public static void setCurentPaper(Pane pane){
        current = pane;
    }
}
