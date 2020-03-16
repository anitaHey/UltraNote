package InsertObj;

import javafx.scene.layout.Pane;

public class Paper extends Pane {
    private static Pane current;
    private static Object object;

    public static Pane getCurentPaper(){
        return current;
    }

    public static void setCurentPaper(Pane pane){
        current = pane;
    }

    public static void setFocusObject(Object obj){
        object = obj;
    }

    public static Object getFocusObject(){
        return object;
    }
}
