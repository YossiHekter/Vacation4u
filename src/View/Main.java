package View;

import Controller.*;
import Model.Model;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.net.URL;
import java.util.ResourceBundle;

public class Main extends Application implements Initializable {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Model model = new Model();
        //--------------
        primaryStage.setTitle("Vication4U");
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = fxmlLoader.load(getClass().getResource("sample.fxml").openStream());
        Scene scene = new Scene(root, 600, 450);
        primaryStage.setScene(scene);
        root.setStyle("-fx-background-color: white");
        primaryStage.setResizable(false);
        //-------
        Controller controller = fxmlLoader.getController();
        controller.setModel(model);
        controller.setLogo();
        model.addObserver(controller);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void initialize(URL location, ResourceBundle resources) {
        Image logo = new Image(Main.class.getClassLoader().getResourceAsStream("logo.jpeg"));
        //img_logo.setImage(logo);
    }
}