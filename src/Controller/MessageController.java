package Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class MessageController {
    private String message_str;

    @FXML
    Button exit, enter;
    @FXML
    Label message;

    public MessageController(String input){
        this.message_str = input;
    }

    @FXML
    public void initialize() {
        message.setText(getMessage_str());
    }

    public String getMessage_str() {
        return message_str;
    }
}
