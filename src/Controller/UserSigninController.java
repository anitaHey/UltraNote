package Controller;

import API.communication.Response;
import API.communication.Status;
import API.user.User;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class UserSigninController {
    private static UserSigninController instance;
    private Map<String, Integer> gender_str = new HashMap<>();
    Pattern email_format = Pattern.compile("\\d+@\\d+.\\d+");

    @FXML
    TextField email_text, name_text;
    @FXML
    PasswordField password_text, password__check;
    @FXML
    Button register_btn, exit, back_btn;
    @FXML
    Label error_word;
    @FXML
    ToggleGroup gender_group;

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

        gender_str.put("Male", 0);
        gender_str.put("Female", 1);
        gender_str.put("Other", 2);

        exit.setOnAction(actionEvent -> {
            MainController.loginStage.close();
        });

        register_btn.setOnAction(actionEvent -> {
            if(checkData()){
                String email = email_text.getText();
                String password = password_text.getText();
                String name = name_text.getText();
                int gender = gender_str.get(((RadioButton)gender_group.getSelectedToggle()).getText());

                try {
                    Response register = User.register(email, password, name, gender);

                    if(register.success()) {
                        error_word.setText("");
                    } else {
                        if(register.getStatus() == Status.VALIDATION_ERR){
                            error_word.setText("Wrong email or wrong password.");
                        } else {
                            error_word.setText("Connect error. Please try again.");
                        }
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    public boolean checkData(){

        return true;
    }
}
