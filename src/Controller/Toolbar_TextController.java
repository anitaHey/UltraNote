package Controller;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import Object.TextObj;
import Object.TextLine;
import Object.TextProperty;
import Object.TextColorPicker;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Toolbar_TextController {
    private static Toolbar_TextController instance;
    ArrayList<TextObj> text_hbox = null;
    ArrayList<TextLine> text_line = null;
    TextProperty property = TextProperty.getInstance();
    TextObj currentText = null;
    int[] font_size = {8, 9, 10, 11, 12, 14, 16, 18, 20, 22, 24, 26, 28, 36, 48, 72};
    String last_size = "", last_font = "";
    Boolean combo_size = true;
    Boolean combo_font = true;

    Boolean currentSizeSet = true;
    Boolean currentFamilySet = true;
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
    ColorPicker text_color;

    @FXML
    public void initialize() {
        setInstance(this);
        String[] fontFanily = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        text_color.setValue(Color.web(property.getFontColor()));

        for (String family : fontFanily)
            text_font_combo.getItems().add(family);
        text_font_combo.setValue("");

        for (int size : font_size)
            font_size_combo.getItems().add(String.valueOf(size));
        font_size_combo.setValue("");

        text_font_combo.setCellFactory(lv -> {
            ListCell<String> cell = new ListCell<>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(item);
                }
            };

            cell.addEventFilter(MouseEvent.MOUSE_ENTERED, e -> {
                if (getInstance().text_hbox != null) {
                    for (TextObj word : getInstance().text_hbox)
                        word.setTextFamily(cell.getText(), false);
                }
            });

            cell.addEventFilter(MouseEvent.MOUSE_EXITED, e -> {
                if (getInstance().text_hbox != null) {
                    for (TextObj word : getInstance().text_hbox)
                        word.setTextFamily(word.getLastFontFamily(), false);
                }
            });

            return cell;
        });

        font_size_combo.setCellFactory(lv -> {
            ListCell<String> cell = new ListCell<>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(item);
                }
            };

            cell.addEventFilter(MouseEvent.MOUSE_ENTERED, e -> {
                if (getInstance().text_hbox != null) {
                    for (TextObj word : getInstance().text_hbox)
                        word.setFontSize(Integer.parseInt(cell.getText()), false);

                    checkUnderline();
                }
            });

            cell.addEventFilter(MouseEvent.MOUSE_EXITED, e -> {
                if (getInstance().text_hbox != null) {
                    for (TextObj word : getInstance().text_hbox)
                        word.setFontSize(word.getLastFontSize(), false);

                    checkUnderline();
                }
            });

            return cell;
        });

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
                if (getInstance().boldProperty.getValue()) {
                    property.setCurrent(null, FontWeight.NORMAL, null, null, null, -1);
                    getInstance().boldProperty.set(false);
                } else {
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
                if (getInstance().italicProperty.getValue()) {
                    property.setCurrent(null, null, FontPosture.REGULAR, null, null, -1);
                    getInstance().italicProperty.set(false);
                } else {
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
                if (getInstance().underlineProperty.getValue()) {
                    property.setCurrent(false, null, null, null, null, -1);
                    getInstance().underlineProperty.set(false);
                } else {
                    property.setCurrent(true, null, null, null, null, -1);
                    getInstance().underlineProperty.set(true);
                }

            }
        });

        text_font_combo.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (!getInstance().currentFamilySet) getInstance().currentFamilySet = true;
            else {
                if (newValue.length() != 0 && !oldValue.equals(newValue)) {
                    try {
                        if (getInstance().text_hbox != null) {
                            for (TextObj word : getInstance().text_hbox)
                                word.setTextFamily(newValue, true);

                            Platform.runLater(() -> {
                                getInstance().text_hbox.get(0).requestFocus();
                            });
                        } else if (getInstance().currentText != null) {
                            Platform.runLater(() -> {
                                getInstance().currentText.requestFocus();
                                property.setCurrent(null, null, null, newValue, null, -1);
                            });
                        }
                    } catch (Exception e) {
                        property.setCurrent(null, null, null, property.getFontFamily(), null, -1);
                        System.out.println(e);
                    }
                }
            }
        });

        font_size_combo.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (!getInstance().currentSizeSet) getInstance().currentSizeSet = true;
            else {
                if (newValue.length() != 0 && !oldValue.equals(newValue)) {
                    try {
                        int tem_size = Integer.parseInt(newValue);
                        if (getInstance().text_hbox != null) {
                            for (TextObj word : getInstance().text_hbox)
                                word.setFontSize(tem_size, true);

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

        text_color.valueProperty().addListener((observable, oldValue, newValue) -> {
            try {
                if (getInstance().text_hbox != null) {
                    for (TextObj word : getInstance().text_hbox)
                        word.setFontColor(toRGBCode(newValue));

                    Platform.runLater(() -> {
                        getInstance().text_hbox.get(0).requestFocus();
                    });
                } else if (getInstance().currentText != null) {
                    Platform.runLater(() -> {
                        getInstance().currentText.requestFocus();
                        property.setCurrent(null, null, null, null, toRGBCode(newValue), -1);
                    });
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        });

        /* TODO: Custom color picker.
        CustomMenuItem item = new CustomMenuItem(new TextColorPicker());
        item.getStyleClass().add("outer_item");
        item.getStyleClass().add("menu");
        text_color.getStyleClass().add("menu");
        text_color.getItems().add(item);
        */
    }

    public static String toRGBCode(Color color) {
        return String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
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
                for (int b = index - 1; b >= 0; b--) {
                    TextObj word = line.getIndex(b);
                    if (!word.isTextUnderline()) break;
                    max = Math.max(word.getFontSize(), max);
                    change.add(word);
                }
                for (int b = index; b <= line.getHBoxSize() - 1; b++) {
                    TextObj word = line.getIndex(b);
                    if (!word.isTextUnderline()) break;
                    max = Math.max(word.getFontSize(), max);
                    change.add(word);
                }
            } else if (a == 0) {
                for (int b = line.getHBoxSize() - 1; b >= 0; b--) {
                    TextObj word = line.getIndex(b);
                    if (!word.isTextUnderline()) break;
                    max = Math.max(word.getFontSize(), max);
                    change.add(word);
                }
            } else if (a == length - 1) {
                for (int b = 0; b < line.getHBoxSize(); b++) {
                    TextObj word = line.getIndex(b);
                    if (!word.isTextUnderline()) break;
                    max = Math.max(word.getFontSize(), max);
                    change.add(word);
                }
            } else {
                for (int b = 0; b < line.getHBoxSize(); b++) {
                    TextObj word = line.getIndex(b);
                    max = Math.max(word.getFontSize(), max);
                    change.add(word);
                }
            }

            int height = (max < 10) ? 1 : (max / 10);
            for (TextObj word : change)
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
        setFontFamily();
    }

    public void setCurrentText(TextObj text) {
        getInstance().currentText = text;

        if (!String.valueOf(property.getFontSize()).equals(getInstance().font_size_combo.getValue()))
            getInstance().currentSizeSet = false;

        if (!String.valueOf(property.getFontFamily()).equals(getInstance().text_font_combo.getValue()))
            getInstance().currentFamilySet = false;

        getInstance().font_size_combo.setValue(String.valueOf(property.getFontSize()));
        getInstance().text_font_combo.setValue(property.getFontFamily());
        getInstance().boldProperty.set(property.isBold());
        getInstance().italicProperty.set(property.isItalic());
        getInstance().underlineProperty.set(property.getUnderline());
        getInstance().text_color.setValue(Color.web(property.getFontColor()));
        getInstance().text_font_combo.setValue(property.getFontFamily());
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

    public void setFontFamily() {
        String current_family = "";
        if (getInstance().text_hbox != null) {
            for (TextObj word : getInstance().text_hbox) {
                String tem_family = word.getFontFamily();
                if (current_family.equals("")) current_family = tem_family;
                else if (!current_family.equals(tem_family)) {
                    current_family = "";
                    break;
                }
            }
        }

        getInstance().text_font_combo.setValue(current_family);
    }

    public void clearCurentText() {
        getInstance().text_hbox = null;
        getInstance().text_line = null;
        getInstance().currentText = null;
        getInstance().boldProperty.set(false);
        getInstance().italicProperty.set(false);
        getInstance().underlineProperty.set(false);
    }
}