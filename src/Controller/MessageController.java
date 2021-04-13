package Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.io.IOException;

public class MessageController {
    private String message_str;
    private String page;
    private MainController mainController = MainController.getInstance();
    private boolean success;

    @FXML
    Button exit, enter;
    @FXML
    Label message;

    public MessageController(String input, String page, boolean success){
        this.message_str = input;
        this.page = page;
        this.success = success;
    }

    @FXML
    public void initialize() {
        message.setText(getMessage_str());

        exit.setOnAction(e-> {
            try {
                exitBtn();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        enter.setOnAction(e-> {
            try {
                exitBtn();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }

    public String getMessage_str() {
        return message_str;
    }

    private void exitBtn() throws IOException {
        if(page.equals("signin")){
            UserSigninController.getInstance().messageStage.close();
            if(success){
                mainController.getSigninStage().close();
                mainController.showLogin();
            }
        }
    }
}
