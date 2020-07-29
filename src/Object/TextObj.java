package Object;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

public class TextObj extends Label {
    TextProperty property = TextProperty.getInstance();

    InputMethodRequestsObject inputRequest;
    TextUnderline underline;
    boolean bool_under;
    FontWeight fontWeight;
    FontPosture fontPosture;
    String fontFamily;
    String fontColor;
    String lastFontFamily;
    int lastFontSize;
    int fontSize;

    public TextObj(String input) {
        super(input);

        bool_under = false;
        underline = null;
        fontWeight = null;
        fontPosture = null;
        fontFamily = "";
        fontSize = -1;
        lastFontFamily = "";
        lastFontSize = -1;

        bool_under = property.getUnderline();
        fontColor = property.getFontColor();
        this.setTextFont(property.getFontFamily(), property.getFontWeight(), property.getFontPosture(), property.getFontSize(), true);
        this.setFontColor(property.getFontColor());
        this.getStyleClass().add("text_bg_input");
        this.setMinWidth(USE_COMPUTED_SIZE);

        this.inputRequest = new InputMethodRequestsObject();
        this.setInputMethodRequests(this.inputRequest);
        this.setOnInputMethodTextChanged(e -> {
//            System.out.println(e);
        });

        this.layoutBoundsProperty().addListener((obs, oldValue, newValue)->{
            System.out.println(newValue.getMinX()+" "+newValue.getMaxY()+5);
            this.inputRequest.setPosition_x(newValue.getMinX());
            this.inputRequest.setPosition_y(newValue.getMaxY()+5);
        });
    }

    public void setTextFont(String family, FontWeight weight, FontPosture posture, int size, boolean save) {
        if (weight != null) fontWeight = weight;
        if (posture != null) fontPosture = posture;
        if (family != null) {
            if (save) lastFontFamily = (fontFamily.equals("")) ? family : fontFamily;
            fontFamily = family;
        }
        if (size != -1) {
            if (save) lastFontSize = (fontSize != -1) ? fontSize : size;
            fontSize = size;
            if (getUnderlineObj() != null) setUnderProperty();
        }

        this.setFont(Font.font(fontFamily, fontWeight, fontPosture, fontSize));
    }

    public void setTextFamily(String family, boolean save) {
        if (save) lastFontFamily = (fontFamily.equals("")) ? family : fontFamily;
        fontFamily = family;
        this.setFont(Font.font(fontFamily, fontWeight, fontPosture, fontSize));
    }

    public void setFontWeight(FontWeight weight) {
        fontWeight = weight;
        this.setFont(Font.font(fontFamily, fontWeight, fontPosture, fontSize));
    }

    public void setFontSize(int size, boolean save) {
        if (save) lastFontSize = (fontSize != -1) ? fontSize : size;
        fontSize = size;
        this.setFont(Font.font(fontFamily, fontWeight, fontPosture, fontSize));
    }

    public void setFontPosture(FontPosture posture) {
        fontPosture = posture;
        this.setFont(Font.font(fontFamily, fontWeight, fontPosture, fontSize));
    }

    public int getFontSize() {
        return fontSize;
    }

    public String getFontFamily() {
        return fontFamily;
    }

    public FontPosture getFontPosture() {
        return fontPosture;
    }

    public FontWeight getFontWeight() {
        return fontWeight;
    }

    public boolean isBold() {
        return getFontWeight() == FontWeight.BOLD;
    }

    public boolean isItalic() {
        return getFontPosture() == FontPosture.ITALIC;
    }

    public void setUnderlineObj(TextUnderline new_underline) {
        underline = new_underline;
        setUnderProperty();
        if (bool_under) this.setTextUnderline(true, null);
    }

    public TextUnderline getUnderlineObj() {
        return underline;
    }

    public boolean isTextUnderline() {
        return getUnderlineObj().isTextUnderline();
    }

    public void setTextUnderline(Boolean input, String color) {
        if (color == null) color = this.fontColor;
        getUnderlineObj().setUnderline(input, color);
    }

    public void setFontColor(String input) {
        fontColor = input;
        this.setTextFill(Color.web(input));
        if (getUnderlineObj() != null) getUnderlineObj().setUnderlineColor(input);
    }

    public String getFontColor() {
        return fontColor;
    }

    public void setUnderHeight() {
        int height = (getFontSize() < 10) ? 1 : (getFontSize() / 10);
        getUnderlineObj().setUnderlineHeight(height);
    }

    public void setUnderProperty() {
        this.widthProperty().addListener((obs, oldVal, newVal) -> {
            getUnderlineObj().setUnderlineWidth(newVal.intValue());
        });
        setUnderHeight();
    }

    public String getLastFontFamily() {
        return lastFontFamily;
    }

    public int getLastFontSize() {
        return lastFontSize;
    }
}