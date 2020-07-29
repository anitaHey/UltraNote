import Controller.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.awt.*;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) throws Exception{
        Rectangle screen = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
        Scene scene = new Scene(new StackPane(),screen.getWidth(), screen.getHeight());
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
        controller.addPaper(screen.getWidth(),screen.getHeight());

        primaryStage.show();
    }
}