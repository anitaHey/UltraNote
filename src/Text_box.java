import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class Text_box extends TextFlow {
    private int CURRENT_LINE = 0;
    public Text_box(){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("TextBoxFxml.fxml"));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        }catch (Exception e){
            System.out.println(e);
        }
    }

    @FXML
    TextFlow main_text;
    @FXML
    VBox text_vbox;
    @FXML
    public void initialize() {
        main_text.setOnKeyPressed(e -> {
            if (isWordInput(e.getCode())) {
                Text input = new Text(e.getText());
                input.setFont(Font.font("Helvetica", FontWeight.BOLD, 30));
                HBox hbox = (HBox)text_vbox.getChildren().get(CURRENT_LINE);
                hbox.getChildren().add(input);
            }
        });
    }

    public boolean isWordInput(KeyCode input){
        return input.isDigitKey() || input.isLetterKey() || input.isWhitespaceKey();
    }

}
