package Controller;

import InsertObj.BasicNode;
import InsertObj.Paper;

public class PaperController {
    private static PaperController instance;
    private Paper current;
    private BasicNode object;

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

    public void setFocusObject(BasicNode obj){
        if(getInstance().getFocusObject() != null)
            getInstance().getFocusObject().focus_border(false);
        getInstance().object = obj;

        if(obj != null)
            obj.focus_border(true);
    }
    public BasicNode getFocusObject(){
        return getInstance().object;
    }
}
