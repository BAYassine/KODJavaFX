package Controllers;

import Core.Main;
import Entities.Cartoon;
import Entities.Child;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import de.jensd.fx.glyphs.octicons.OctIcon;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.io.IOException;

public class KidsspaceController {

    private static Child child;

    public ImageView kidImage;
    public ImageView bgImage;
    public Button gameBtn;
    public Button musicBtn;
    public Button cartoonBtn;
    public Button menuToggle;
    public MaterialDesignIconView menuIcon;
    public Button quizBtn;

    public static boolean isWatching;
    public static boolean isPlaying;
    public static boolean isDancing;

    private static StackPane space = null;
    private static StackPane backLayer = null;
    private static StackPane centerLayer = null;
    private static StackPane menuLayer = null;
    public Button exitBtn;

    public static Child getChild() {
        return child;
    }


    public void openSpace(Child c) {
        child = c;
        space = new StackPane();
        KeyCodeCombination kcc = new KeyCodeCombination(KeyCode.K, KeyCodeCombination.CONTROL_DOWN, KeyCodeCombination.ALT_DOWN);
        Main.window.setFullScreenExitHint("");
        Main.window.setFullScreenExitKeyCombination(kcc);
        Main.window.setFullScreen(true);
        new Main().showLoading();
        try {
            space = FXMLLoader.load(getClass().getResource("/GUI/kidspace.fxml"));
            menuLayer = (StackPane) space.lookup("#menuLayer");
            centerLayer = (StackPane) space.lookup("#centerLayer");
            backLayer = (StackPane) space.lookup("#backLayer");

            ImageView kidImage = (ImageView) backLayer.lookup("#kidImage");
            ImageView bgImage = (ImageView) backLayer.lookup("#bgImage");

            menuToggle = (Button) menuLayer.lookup("#menuToggle");
            menuToggle.setTranslateX(Main.scene.getWidth() / 2 - 60);
            gameBtn = (Button) menuLayer.lookup("#gameBtn");
            musicBtn = (Button) menuLayer.lookup("#musicBtn");
            quizBtn = (Button) menuLayer.lookup("#quizBtn");
            cartoonBtn = (Button) menuLayer.lookup("#cartoonBtn");
            exitBtn = (Button) menuLayer.lookup("#exitBtn");

            gameBtn.setTranslateX(menuToggle.getTranslateX());
            gameBtn.setTranslateY(0);
            gameBtn.setScaleX(0);gameBtn.setScaleY(0);

            musicBtn.setTranslateX(menuToggle.getTranslateX());
            musicBtn.setTranslateY(0);
            musicBtn.setScaleX(0);musicBtn.setScaleY(0);

            quizBtn.setTranslateX(menuToggle.getTranslateX());
            quizBtn.setTranslateY(0);
            quizBtn.setScaleX(0);quizBtn.setScaleY(0);

            cartoonBtn.setTranslateX(menuToggle.getTranslateX());
            cartoonBtn.setTranslateY(0);
            cartoonBtn.setScaleX(0);cartoonBtn.setScaleY(0);
            double diff = Main.scene.getWidth() / 2000;
            kidImage.setFitWidth(190 * diff);
            kidImage.setTranslateY((Main.scene.getHeight() / 2) * 0.3);

            gameBtn.setOnAction(e -> {
                toggleMenu();
                new GameController().showPlanet(centerLayer);
            });
            musicBtn.setOnAction(e -> {
                toggleMenu();
                new MusicController().showPlanet(centerLayer);
            });
            cartoonBtn.setOnAction(e -> {
                toggleMenu();
                new CartoonController().showPlanet(centerLayer);
            });
            quizBtn.setOnAction(e -> {
                toggleMenu();
                new QuizController().showPlanet(centerLayer);
            });
//            gameBtn.setOnAction(e -> new GameController().showPlanet(backpane));
            exitBtn.setTranslateX(- Main.scene.getWidth()/2);
            exitBtn.setTranslateY(- Main.scene.getHeight()/2);
            exitBtn.setOnAction(e -> exitSpace());

            if (!c.getGender()) {
                bgImage.setImage(new Image("/assets/images/kidspaceG.jpg"));
                kidImage.setImage(new Image("/assets/images/girl.png"));
            }
            bgImage.setFitWidth(Main.scene.getWidth());

        } catch (IOException e) {
            e.printStackTrace();
        }
        String styleClass = c.getGender() ? "boy-space" : "girl-space";
        space.getStyleClass().add(styleClass);
        Main.sp.getChildren().add(space);
    }

    private void exitSpace() {
        if (isPlaying)
            GameController.clearTraces();
        if (isWatching)
            CartoonController.clearTraces();
        if (isDancing)
            MusicController.clearTraces();
        child = null;
        Main.sp.getChildren().remove(space);
        Main.window.setFullScreen(false);
    }

    public void toggleMenu() {
        menuToggle = (Button) Main.scene.lookup("#menuToggle");
        menuIcon = (MaterialDesignIconView) Main.scene.lookup("#menuIcon");
        if (menuToggle.getStyleClass().contains("active")) {
            menuToggle.getStyleClass().remove("active");
            menuIcon.setIcon(MaterialDesignIcon.MENU);
            ScaleTransition st = new ScaleTransition(Duration.millis(200), menuToggle);
            st.setToX(1);
            st.setToY(1);
            st.play();
            ParallelTransition gameTransition = createMenuAnimation(
                    gameBtn,0,
                    menuToggle.getTranslateX(), 0
            );
            ParallelTransition musicTransition = createMenuAnimation(
                    musicBtn,0,
                    menuToggle.getTranslateX(), 0
            );
            ParallelTransition cartoonTransition = createMenuAnimation(
                    cartoonBtn,0,
                    menuToggle.getTranslateX(), 0
            );
            ParallelTransition quizTransition = createMenuAnimation(
                    quizBtn,0,
                    menuToggle.getTranslateX(), 0
            );
            SequentialTransition menuAnimation = new SequentialTransition();
            menuAnimation.getChildren().addAll(
                    gameTransition,
                    musicTransition,
                    quizTransition,
                    cartoonTransition
            );
            menuAnimation.play();
        } else {
            menuToggle.getStyleClass().add("active");
            menuIcon.setIcon(MaterialDesignIcon.CLOSE);
            ScaleTransition st = new ScaleTransition(Duration.millis(200), menuToggle);
            st.setToX(0.7);
            st.setToY(0.7);
            st.play();
            ParallelTransition gameTransition = createMenuAnimation(
                    gameBtn,1,
                    (Main.scene.getWidth() / 2) - 90, -(Main.scene.getHeight() / 2) * 0.75
            );
            ParallelTransition musicTransition = createMenuAnimation(
                    musicBtn,1,
                    (Main.scene.getWidth() / 2) - 150, -(Main.scene.getHeight() / 2) * 0.3
            );
            ParallelTransition quizTransition = createMenuAnimation(
                    quizBtn,1,
                    (Main.scene.getWidth() / 2) - 150, (Main.scene.getHeight() / 2) * 0.3
            );
            ParallelTransition cartoonTransition = createMenuAnimation(
                    cartoonBtn,1,
                    (Main.scene.getWidth() / 2) - 90, (Main.scene.getHeight() / 2) * 0.75
            );
            SequentialTransition menuAnimation = new SequentialTransition();
            menuAnimation.getChildren().addAll(
                    gameTransition,
                    musicTransition,
                    quizTransition,
                    cartoonTransition
            );
            menuAnimation.play();
        }
    }


    private ParallelTransition createMenuAnimation(Node n, double scale, double tx, double ty) {
        ScaleTransition stg = new ScaleTransition(Duration.millis(150), n);
        stg.setToX(scale);
        stg.setToY(scale);
        TranslateTransition ttg = new TranslateTransition(Duration.millis(150), n);
        ttg.setToX(tx);
        ttg.setToY(ty);
        ParallelTransition ptg = new ParallelTransition();
        ptg.getChildren().addAll(stg, ttg);
        return ptg;
    }
}
