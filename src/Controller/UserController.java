package Controller;

import API.communication.Response;
import API.user.User;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class UserController {
    private static UserController instance;

    @FXML
    TextField email_text;
    @FXML
    PasswordField password_text;
    @FXML
    Button login_btn, exit;

    public static UserController getInstance() {
        if (instance == null) {
            instance = new UserController();
        }
        return instance;
    }

    public static void setInstance(UserController newInstance) {
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
                System.out.println(login.getContent().toString());

            } catch (Exception e){
                System.out.println(e);
            }
        });

    }

}
