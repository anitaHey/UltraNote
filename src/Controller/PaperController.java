package Controller;

import InsertObj.Paper;
import javafx.scene.layout.Pane;

public class PaperController {
    private static PaperController instance;
    private  Paper current;
    private  Object object;

    public static PaperController getInstance() {
        if (instance == null) {
            instance = new PaperController();
        }
        return instance;
    }

    public static void setInstance(PaperController newInstance) {
        instance = newInstance;
    }

    public PaperController(){
        setInstance(this);
    }

    public Paper getCurentPaper(){
        return getInstance().current;
    }

    public void setCurentPaper(Paper pane){
        getInstance().current = pane;
    }

    public void setFocusObject(Object obj){
        getInstance().object = obj;
    }

    public Object getFocusObject(){
        return getInstance().object;
    }
}
