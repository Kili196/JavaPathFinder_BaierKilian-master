package Sample;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class Sample extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        String fxmlPath = "../test.fxml";

        VBox root = FXMLLoader.load(getClass().getResource(fxmlPath));

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Unknown");
        stage.setResizable(true);
        stage.show();
    }
}
