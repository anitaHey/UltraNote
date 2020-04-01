package Controller;

import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.util.ArrayList;

public class Toolbar_TextController {
  private static Toolbar_TextController instance;
  ArrayList<HBox> text_hbox = null;
  int[] font_size = {8, 9, 10, 11, 12, 14, 16, 18, 20, 22, 24, 26, 28, 36, 48, 72};

  Boolean press_bold;
  Boolean press_italic;
  Boolean press_underline;

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

    for (int size : font_size)
      font_size_combo.getItems().add(String.valueOf(size));
    font_size_combo.setValue("");

    text_bold.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
      setBoldPressed();
      FontPosture fontposture = null;
      if (getInstance().text_hbox != null) {
        for (HBox textHbox : getInstance().text_hbox) {
          if (textHbox.getChildren().size() > 0) {
            Text text = (Text) ((HBox)textHbox.getChildren().get(0)).getChildren().get(0);

            if (text.getFont().getStyle().contains("Italic")) fontposture = FontPosture.ITALIC;
            else fontposture = FontPosture.REGULAR;

            if (getInstance().press_bold)
              text.setFont(Font.font(text.getFont().getFamily(), FontWeight.NORMAL, fontposture, text.getFont().getSize()));
            else
              text.setFont(Font.font(text.getFont().getFamily(), FontWeight.BOLD, fontposture, text.getFont().getSize()));
          }
        }
        getInstance().text_hbox.get(0).getChildren().get(0).requestFocus();
        setBoldPressed();
      }
    });

    text_italic.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
      setItalicPressed();
      FontWeight fontweight = null;
      if (getInstance().text_hbox != null) {
        for (HBox textHbox : getInstance().text_hbox) {
          if (textHbox.getChildren().size() > 0) {
            Text text = (Text) ((HBox)textHbox.getChildren().get(0)).getChildren().get(0);

            if (text.getFont().getStyle().contains("Bold")) fontweight = FontWeight.BOLD;
            else fontweight = FontWeight.NORMAL;

            if (getInstance().press_italic)
              text.setFont(Font.font(text.getFont().getFamily(), fontweight, FontPosture.REGULAR, text.getFont().getSize()));
            else
              text.setFont(Font.font(text.getFont().getFamily(), fontweight, FontPosture.ITALIC, text.getFont().getSize()));
          }
        }
        getInstance().text_hbox.get(0).getChildren().get(0).requestFocus();
        setItalicPressed();
      }
    });

    text_underline.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
      setUnderlinePressed();
      if (getInstance().text_hbox != null) {
        for (HBox textHbox : getInstance().text_hbox) {
          if (textHbox.getChildren().size() > 0) {
            Text text = (Text) ((HBox)textHbox.getChildren().get(0)).getChildren().get(0);
            int width = (text.getFont().getSize()<10)?1:(int)(text.getFont().getSize()/10);

            if (getInstance().press_underline)
              textHbox.setStyle("");
            else
              textHbox.setStyle("-fx-border-width: 0 0 "+ width +" 0; -fx-border-color: #525252;");
          }
        }
        getInstance().text_hbox.get(0).getChildren().get(0).requestFocus();
        setUnderlinePressed();
      }
    });

    font_size_combo.valueProperty().addListener((observable, oldValue, newValue) -> {
      if(newValue.length()!= 0 && !oldValue.equals(newValue)){
        FontWeight fontweight;
        FontPosture fontposture;
        try{
          double tem_size = Double.parseDouble(newValue);
          if (getInstance().text_hbox != null) {
            for (HBox textHbox : getInstance().text_hbox) {
              if (textHbox.getChildren().size() > 0) {
                Text text = (Text) ((HBox)textHbox.getChildren().get(0)).getChildren().get(0);

                if (text.getFont().getStyle().contains("Bold")) fontweight = FontWeight.BOLD;
                else fontweight = FontWeight.NORMAL;

                if (text.getFont().getStyle().contains("Italic")) fontposture = FontPosture.ITALIC;
                else fontposture = FontPosture.REGULAR;

                text.setFont(Font.font(text.getFont().getFamily(), fontweight, fontposture, (int) tem_size));

                if(textHbox.getStyle().length()>0){
                  int width = (text.getFont().getSize()<10)?1:(int)(text.getFont().getSize()/10);
                  textHbox.setStyle("-fx-border-width: 0 0 "+ width +" 0; -fx-border-color: #525252;");
                }
              }
            }
          }
        }catch(Exception e){
          System.out.println(e);
        }
      }
    });
  }

  public void setCurentText(ArrayList<HBox> hbox) {
    getInstance().text_hbox = hbox;

    setBoldPressed();
    setItalicPressed();
    setUnderlinePressed();
    setFontSize();
  }

  public void setBoldPressed() {
    getInstance().text_bold.getStyleClass().remove("toolbar_sm_button_pressed");
    getInstance().press_bold = getInstance().text_hbox.size() != 0;

    if (getInstance().text_hbox != null) {
      for (HBox hBox : getInstance().text_hbox) {
        if (hBox.getChildren().size() > 0) {
          Text text = (Text) ((HBox)hBox.getChildren().get(0)).getChildren().get(0);
          if (!text.getFont().getStyle().contains("Bold")) {
            getInstance().press_bold = false;
            break;
          }
        }
      }
    }

    if (getInstance().press_bold)
      if (!getInstance().text_bold.getStyleClass().contains("toolbar_sm_button_pressed"))
        getInstance().text_bold.getStyleClass().add("toolbar_sm_button_pressed");
  }

  public void setItalicPressed() {
    getInstance().text_italic.getStyleClass().remove("toolbar_sm_button_pressed");
    getInstance().press_italic = getInstance().text_hbox.size() != 0;

    if (getInstance().text_hbox != null) {
      for (HBox hBox : getInstance().text_hbox) {
        if (hBox.getChildren().size() > 0) {
          Text text = (Text) ((HBox)hBox.getChildren().get(0)).getChildren().get(0);
          if (!text.getFont().getStyle().contains("Italic")) {
            getInstance().press_italic = false;
            break;
          }
        }
      }
    }
    if (getInstance().press_italic)
      if (!getInstance().text_italic.getStyleClass().contains("toolbar_sm_button_pressed"))
        getInstance().text_italic.getStyleClass().add("toolbar_sm_button_pressed");
  }

  public void setUnderlinePressed() {
    getInstance().text_underline.getStyleClass().remove("toolbar_sm_button_pressed");
    getInstance().press_underline = getInstance().text_hbox.size() != 0;

    if (getInstance().text_hbox != null) {
      for (HBox hBox : getInstance().text_hbox) {
        if (hBox.getChildren().size() > 0) {
          if (hBox.getStyle().length() == 0) {
            getInstance().press_underline = false;
            break;
          }
        }
      }
    }
    if (getInstance().press_underline)
      if (!getInstance().text_underline.getStyleClass().contains("toolbar_sm_button_pressed"))
        getInstance().text_underline.getStyleClass().add("toolbar_sm_button_pressed");
  }

  public synchronized void setFontSize() {
    double current_size = 0;
    if (getInstance().text_hbox != null) {
      for (HBox hBox : getInstance().text_hbox) {
        if (hBox.getChildren().size() > 0) {

          Text text = (Text) ((HBox)hBox.getChildren().get(0)).getChildren().get(0);
          double tem_size = text.getFont().getSize();

          if (current_size == 0) current_size = tem_size;
          else if (current_size != tem_size) {
            current_size = 0;
            break;
          }
        }
      }
    }

    if (current_size == 0)
      getInstance().font_size_combo.setValue("");
    else
      getInstance().font_size_combo.setValue(String.valueOf((int)current_size));
  }

  public void clearCurentText() {
    getInstance().text_hbox = null;
    getInstance().text_bold.getStyleClass().remove("toolbar_sm_button_pressed");
    getInstance().text_italic.getStyleClass().remove("toolbar_sm_button_pressed");
    getInstance().text_underline.getStyleClass().remove("toolbar_sm_button_pressed");
  }
}