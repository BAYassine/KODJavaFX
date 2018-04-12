package Core;

import Controllers.HeaderController;
import Controllers.IndexController;
import Core.Exceptions.EmailInvalidException;
import Entities.User;
import Services.GameService;
import Services.UserService;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.swing.*;
import javax.swing.text.html.ImageView;
import java.io.IOException;
import java.util.concurrent.TimeUnit;


public class Main extends Application {

    public static Stage window;
    public static Rectangle2D screen;
    public static BorderPane pane;
    public static StackPane sp;
    public static User user = null;
    public static Scene scene;

    @Override
    public void start(Stage primaryStage) {
        window = primaryStage;
        screen = Screen.getPrimary().getBounds();
        sp = new StackPane();
        pane = new BorderPane();
//        pane.getStylesheets().add("https://fonts.googleapis.com/css?family=Open+Sans:300,400,600,700");
//        pane.getStylesheets().add("https://fonts.googleapis.com/css?family=Dosis:300,400,600,700,800");
//        pane.getStylesheets().add("https://fonts.googleapis.com/css?family=Roboto:300,400,500,700");
        pane.getStylesheets().add("/assets/css/styles.css");
        VBox top = null;
        try {
            top = FXMLLoader.load(getClass().getResource("/GUI/header.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        pane.setTop(top);
        sp.getChildren().add(pane);
        scene = new Scene(sp);
        window.setX(screen.getMinX() - 10);
        window.setY(screen.getMinY());
        window.setMaximized(true);
        window.setTitle("Kidzo");
        window.setScene(scene);
        new IndexController().init();
        window.show();
        new HeaderController().init();
        Security.checkRemembered();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public synchronized void showLoading(){
        try {
//            Main.pane.setOpacity(0);
            FlowPane loader = FXMLLoader.load(getClass().getResource("/GUI/loading.fxml"));
            Main.sp.getChildren().add(loader);
            Main.sp.setAlignment(Pos.CENTER);
            PauseTransition pt = new PauseTransition(Duration.seconds(2));
            pt.play();
            pt.setOnFinished(e -> {
                Main.sp.getChildren().remove(loader);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
