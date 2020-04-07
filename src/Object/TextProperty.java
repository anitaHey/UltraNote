package Object;

import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

public class TextProperty {
    private static TextProperty instance;
    Boolean underline;
    FontWeight fontWeight;
    FontPosture fontPosture;
    String fontFamily;
    String fontColor;
    int fontSize;

    public static TextProperty getInstance() {
        if (instance == null) {
            instance = new TextProperty();
        }
        return instance;
    }

    public static void setInstance(TextProperty newInstance) {
        instance = newInstance;
    }

    public TextProperty() {
        setInstance(this);
        setDefault();
    }

    public void setDefault(){
        getInstance().underline = false;
        getInstance().fontWeight = FontWeight.NORMAL;
        getInstance().fontPosture = FontPosture.REGULAR;
        getInstance().fontFamily = "Consolas";
        getInstance().fontColor = "#000";
        getInstance().fontSize = 25;
    }

    public void setCurrent(Boolean line, FontWeight weight, FontPosture posture, String family, String color, int size) {
        if (line != null) getInstance().underline = line;
        if (weight != null) getInstance().fontWeight = weight;
        if (posture != null) getInstance().fontPosture = posture;
        if (family != null) getInstance().fontFamily = family;
        if (color != null) getInstance().fontColor = color;
        if (size != -1) getInstance().fontSize = size;
    }

    public Boolean getUnderline() {
        return getInstance().underline;
    }

    public FontWeight getFontWeight() {
        return getInstance().fontWeight;
    }

    public FontPosture getFontPosture() {
        return getInstance().fontPosture;
    }

    public String getFontFamily() {
        return getInstance().fontFamily;
    }

    public String getFontColor() {
        return getInstance().fontColor;
    }

    public int getFontSize() {
        return getInstance().fontSize;
    }

    public boolean isBold() {
        return getFontWeight() == FontWeight.BOLD;
    }

    public boolean isItalic() {
        return getFontPosture() == FontPosture.ITALIC;
    }
}
