package Controller;

import API.communication.Response;
import API.communication.Status;
import API.user.User;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class UserSigninController {
    private static UserSigninController instance;

    @FXML
    TextField email_text;
    @FXML
    PasswordField password_text;
    @FXML
    Button login_btn, exit, sign_btn;
    @FXML
    Label error_word;

    public static UserSigninController getInstance() {
        if (instance == null) {
            instance = new UserSigninController();
        }
        return instance;
    }

    public static void setInstance(UserSigninController newInstance) {
        instance = newInstance;
    }

    @FXML
    public void initialize() {
        setInstance(this);

        exit.setOnAction(actionEvent -> {
            MainController.loginStage.close();
        });

        login_btn.setOnAction(actionEvent -> {
            String email = email_text.getText();
            String password = password_text.getText();

            try {
                Response login = User.login(email, password);

                if(login.success()) {
                    error_word.setText("");
                } else {
                    if(login.getStatus() == Status.VALIDATION_ERR){
                        error_word.setText("Wrong email or wrong password.");
                    } else {
                        error_word.setText("Connect error. Please try again.");
                    }
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        });
    }
}
