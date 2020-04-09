import Controller.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) throws Exception{
        Rectangle2D screenBounds = Screen.getPrimary().getBounds();
        Scene scene = new Scene(new StackPane(), screenBounds.getWidth(), screenBounds.getHeight());
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXML/MainFxml.fxml"));
        scene.setRoot(loader.load());

        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setScene(scene);
        primaryStage.setX(0);
        primaryStage.setY(0);

        MainController controller = loader.getController();
        controller.Init();
        controller.setStage(primaryStage);
        controller.setSize();
        controller.add(screenBounds.getWidth(),screenBounds.getHeight());

        primaryStage.show();
    }
}
