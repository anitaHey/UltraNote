package Controller;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import Object.TextObj;
import Object.TextLine;
import Object.TextProperty;
import Object.TextColorPicker;

import java.util.ArrayList;

public class Toolbar_TextController {
    private static Toolbar_TextController instance;
    ArrayList<TextObj> text_hbox = null;
    ArrayList<TextLine> text_line = null;
    TextProperty property = TextProperty.getInstance();
    TextObj currentText = null;
    int[] font_size = {8, 9, 10, 11, 12, 14, 16, 18, 20, 22, 24, 26, 28, 36, 48, 72};

    Boolean cuurentSizeSet = true;
    Boolean cuurentFamilySet = true;
    private BooleanProperty boldProperty = new SimpleBooleanProperty(false);
    private BooleanProperty italicProperty = new SimpleBooleanProperty(false);
    private BooleanProperty underlineProperty = new SimpleBooleanProperty(false);

    public static Toolbar_TextController getInstance() {
        if (instance == null) {
            instance = new Toolbar_TextController();
        }
        return instance;
    }

    public static void setInstance(Toolbar_TextController newInstance) {
        instance = newInstance;
    }

    @FXML
    Button text_bold, text_italic, text_underline;
    @FXML
    ComboBox<String> text_font_combo;
    @FXML
    ComboBox<String> font_size_combo;
    @FXML
    VBox text_color;
    @FXML
    Pane text_color_pane;

    @FXML
    public void initialize() {
        setInstance(this);

        text_color_pane.setStyle("-fx-background-color:"+property.getFontColor()+";");

        boldProperty.addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                if (!getInstance().text_bold.getStyleClass().contains("toolbar_sm_button_pressed"))
                    getInstance().text_bold.getStyleClass().add("toolbar_sm_button_pressed");
            } else {
                getInstance().text_bold.getStyleClass().remove("toolbar_sm_button_pressed");
            }
        });

        italicProperty.addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                if (!getInstance().text_italic.getStyleClass().contains("toolbar_sm_button_pressed"))
                    getInstance().text_italic.getStyleClass().add("toolbar_sm_button_pressed");
            } else {
                getInstance().text_italic.getStyleClass().remove("toolbar_sm_button_pressed");
            }
        });

        underlineProperty.addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                if (!getInstance().text_underline.getStyleClass().contains("toolbar_sm_button_pressed"))
                    getInstance().text_underline.getStyleClass().add("toolbar_sm_button_pressed");
            } else {
                getInstance().text_underline.getStyleClass().remove("toolbar_sm_button_pressed");
            }
        });

        for (int size : font_size)
            font_size_combo.getItems().add(String.valueOf(size));
        font_size_combo.setValue("");

        text_bold.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if (getInstance().text_hbox != null) {
                for (TextObj word : getInstance().text_hbox) {
                    if (getInstance().boldProperty.getValue())
                        word.setFontWeight(FontWeight.NORMAL);
                    else
                        word.setFontWeight(FontWeight.BOLD);
                }
                getInstance().text_hbox.get(0).requestFocus();
                setBoldPressed();
            } else if (getInstance().currentText != null) {
                getInstance().currentText.requestFocus();
                if(getInstance().boldProperty.getValue()){
                    property.setCurrent(null, FontWeight.NORMAL, null, null, null, -1);
                    getInstance().boldProperty.set(false);
                }else{
                    property.setCurrent(null, FontWeight.BOLD, null, null, null, -1);
                    getInstance().boldProperty.set(true);
                }
            }
        });

        text_italic.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if (getInstance().text_hbox != null) {
                for (TextObj word : getInstance().text_hbox) {
                    if (getInstance().italicProperty.getValue())
                        word.setFontPosture(FontPosture.REGULAR);
                    else
                        word.setFontPosture(FontPosture.ITALIC);
                }
                getInstance().text_hbox.get(0).requestFocus();
                setItalicPressed();
            } else if (getInstance().currentText != null) {
                getInstance().currentText.requestFocus();
                if(getInstance().italicProperty.getValue()){
                    property.setCurrent(null, null, FontPosture.REGULAR, null, null, -1);
                    getInstance().italicProperty.set(false);
                }else{
                    property.setCurrent(null, null, FontPosture.ITALIC, null, null, -1);
                    getInstance().italicProperty.set(true);
                }
            }
        });

        text_underline.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if (getInstance().text_hbox != null) {
                for (TextObj word : getInstance().text_hbox)
                    word.setTextUnderline(!getInstance().underlineProperty.getValue(), null);

                checkUnderline();
                getInstance().text_hbox.get(0).requestFocus();
                setUnderlinePressed();
            } else if (getInstance().currentText != null) {
                getInstance().currentText.requestFocus();
                if(getInstance().underlineProperty.getValue()){
                    property.setCurrent(false, null, null, null, null, -1);
                    getInstance().underlineProperty.set(false);
                }else{
                    property.setCurrent(true, null, null, null, null, -1);
                    getInstance().underlineProperty.set(true);
                }

            }
        });

        font_size_combo.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (!getInstance().cuurentSizeSet) getInstance().cuurentSizeSet = true;
            else {
                if (newValue.length() != 0 && !oldValue.equals(newValue)) {
                    try {
                        int tem_size = Integer.parseInt(newValue);
                        if (getInstance().text_hbox != null) {
                            for (TextObj word : getInstance().text_hbox)
                                word.setFontSize(tem_size);

                            checkUnderline();
                            Platform.runLater(() -> {
                                getInstance().text_hbox.get(0).requestFocus();
                            });
                        } else if (getInstance().currentText != null) {
                            Platform.runLater(() -> {
                                getInstance().currentText.requestFocus();
                                property.setCurrent(null, null, null, null, null, Integer.parseInt(newValue));
                            });
                        }
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                }
            }
        });

        TextColorPicker colorPicker = new TextColorPicker();
        ContextMenu
        text_color.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            colorPicker.
            popUp.show(text_color, event.getScreenX(), event.getScreenY());
        });
    }

    public void checkUnderline() {
        int length = getInstance().text_line.size();
        TextLine line = null;

        for (int a = 0; a < length; a++) {
            ArrayList<TextObj> change = new ArrayList<>();
            line = getInstance().text_line.get(a);
            int max = -1;

            if (a == 0 && a == length - 1) {
                int index = line.getTextIndex(getInstance().text_hbox.get(0));
                for(int b = index-1; b>=0;b--){
                    TextObj word = line.getIndex(b);
                    if(!word.isTextUnderline()) break;
                    max = Math.max(word.getFontSize(), max);
                    change.add(word);
                }
                for(int b = index; b<= line.getHBoxSize()-1;b++){
                    TextObj word = line.getIndex(b);
                    if(!word.isTextUnderline()) break;
                    max = Math.max(word.getFontSize(), max);
                    change.add(word);
                }
            } else if (a == 0) {
                for(int b = line.getHBoxSize()-1; b>=0;b--){
                    TextObj word = line.getIndex(b);
                    if(!word.isTextUnderline()) break;
                    max = Math.max(word.getFontSize(), max);
                    change.add(word);
                }
            } else if (a == length - 1) {
                for(int b = 0; b<line.getHBoxSize();b++){
                    TextObj word = line.getIndex(b);
                    if(!word.isTextUnderline()) break;
                    max = Math.max(word.getFontSize(), max);
                    change.add(word);
                }
            } else {
                for(int b = 0; b<line.getHBoxSize();b++){
                    TextObj word = line.getIndex(b);
                    max = Math.max(word.getFontSize(), max);
                    change.add(word);
                }
            }

            int height = (max < 10) ? 1 : (max / 10);
            for(TextObj word : change)
                word.getUnderlineObj().setUnderlineHeight(height);
            change.clear();
        }
    }

    public void setSelectText(ArrayList<TextObj> hbox, ArrayList<TextLine> line) {
        getInstance().text_hbox = hbox;
        getInstance().text_line = line;
        setBoldPressed();
        setItalicPressed();
        setUnderlinePressed();
        setFontSize();
    }

    public void setCurrentText(TextObj text) {
        getInstance().currentText = text;

        if (!String.valueOf(property.getFontSize()).equals(getInstance().font_size_combo.getValue()))
            getInstance().cuurentSizeSet = false;

        getInstance().font_size_combo.setValue(String.valueOf(property.getFontSize()));
        getInstance().text_font_combo.setValue(property.getFontFamily());
        getInstance().boldProperty.set(property.isBold());
        getInstance().italicProperty.set(property.isItalic());
        getInstance().underlineProperty.set(property.getUnderline());
    }

    public void setBoldPressed() {
        boolean tem = getInstance().text_hbox.size() != 0;
        if (getInstance().text_hbox != null) {
            for (TextObj word : getInstance().text_hbox) {
                if (!word.isBold()) {
                    tem = false;
                    break;
                }
            }
        }
        getInstance().boldProperty.set(tem);
    }

    public void setItalicPressed() {
        boolean tem = getInstance().text_hbox.size() != 0;

        if (getInstance().text_hbox != null) {
            for (TextObj word : getInstance().text_hbox) {
                if (!word.isItalic()) {
                    tem = false;
                    break;
                }
            }
        }
        getInstance().italicProperty.set(tem);
    }

    public void setUnderlinePressed() {
        boolean tem = getInstance().text_hbox.size() != 0;

        if (getInstance().text_hbox != null) {
            for (TextObj word : getInstance().text_hbox) {
                if (!word.isTextUnderline()) {
                    tem = false;
                    break;
                }
            }
        }
        getInstance().underlineProperty.set(tem);
    }

    public synchronized void setFontSize() {
        double current_size = 0;
        if (getInstance().text_hbox != null) {
            for (TextObj word : getInstance().text_hbox) {
                double tem_size = word.getFontSize();
                if (current_size == 0) current_size = tem_size;
                else if (current_size != tem_size) {
                    current_size = 0;
                    break;
                }
            }
        }

        if (current_size == 0)
            getInstance().font_size_combo.setValue("");
        else
            getInstance().font_size_combo.setValue(String.valueOf((int) current_size));
    }

    public void clearCurentText() {
        getInstance().text_hbox = null;
        getInstance().text_line = null;
        getInstance().currentText = null;
        getInstance().underlineProperty.set(false);
        getInstance().italicProperty.set(false);
        getInstance().underlineProperty.set(false);
    }
}