package Controller;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import Object.TextObj;
import Object.TextProperty;

import java.util.ArrayList;

public class Toolbar_TextController {
    private static Toolbar_TextController instance;
    ArrayList<TextObj> text_hbox = null;
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
    public void initialize() {
        setInstance(this);

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
              property.setCurrent(null, null, null, null, null, -1);
            }
        });

        text_underline.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if (getInstance().text_hbox != null) {
                for (TextObj word : getInstance().text_hbox)
                    word.setTextUnderline(!getInstance().underlineProperty.getValue(), "#293462");

                getInstance().text_hbox.get(0).requestFocus();
                setUnderlinePressed();
            } else if (getInstance().currentText != null) {
                getInstance().currentText.requestFocus();
              property.setCurrent(true, null, null, null, null, -1);
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
    }

    public void setSelectText(ArrayList<TextObj> hbox) {
        getInstance().text_hbox = hbox;

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
        getInstance().currentText = null;
        getInstance().underlineProperty.set(false);
        getInstance().italicProperty.set(false);
        getInstance().underlineProperty.set(false);
    }
}