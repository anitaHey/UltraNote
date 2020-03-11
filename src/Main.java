import Controller.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) throws Exception{
        Scene scene = new Scene(new StackPane());
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXML/MainFxml.fxml"));
        scene.setRoot(loader.load());
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setScene(scene);
        MainController controller = loader.getController();
        controller.Init();
        primaryStage.show();
    }
}
