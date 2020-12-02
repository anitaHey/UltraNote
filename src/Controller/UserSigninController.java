package Controller;

import API.communication.Response;
import API.communication.Status;
import API.user.User;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
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
    @FXML
    HBox email_error, password_error, password_blank_error, username_error, gender_error;

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

                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    public boolean checkData(){
        boolean check = true;

        email_error.setVisible(false);
        password_error.setVisible(false);
        password_blank_error.setVisible(false);
        username_error.setVisible(false);
        gender_error.setVisible(false);

        if(!email_format.matcher(email_text.getText()).matches()) {
            check = false;
            email_error.setVisible(true);
        }

        if(password_text.getText().trim().length() == 0) {
            check = false;
            password_error.setVisible(true);
        }

        if(password__check.getText().trim().length() == 0 || !password__check.getText().equals(password_text.getText())) {
            check = false;
            password_blank_error.setVisible(true);
        }

        if(name_text.getText().trim().length() == 0) {
            check = false;
            username_error.setVisible(true);
        }

        if(gender_group.getSelectedToggle() == null) {
            check = false;
            gender_error.setVisible(true);
        }

        return check;
    }
}
