package Controllers;

import Core.Main;
import Entities.Cartoon;
import Entities.Video;
import Services.CartoonService;
import Services.GameService;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.web.WebView;

import java.io.IOException;
import java.util.Random;

public class CartoonController extends PlanetController{

    private static int currentEpisodeId;
    private static long start;
    private static ScrollPane content;
    private static FlowPane player;
    private static WebView wv;

    public CartoonController() {
        this.bgPath = "/assets/images/games-planet.jpg";
        this.roundImagePath = "/assets/images/dice.png";
        this.titleText = "LE MONDE DE";
        this.subTitleText = "LE MONDE DES JEUX";
        this.enterBtn.setOnAction(e -> openPlanet());
    }

    private void openPlanet() {
        content = null;
        try {
            content = FXMLLoader.load(getClass().getResource("/GUI/cartoon-list.fxml"));
            content.setMaxWidth(Main.scene.getWidth());
            ObservableList<Cartoon> cartoons = new CartoonService().findAll();
            VBox listContainer = (VBox) content.getContent();
            listContainer.setPrefWidth(Main.scene.getWidth());
            GridPane cartoonList =  new GridPane();
            ColumnConstraints cc = new ColumnConstraints();
            cc.setPercentWidth(20);
            cartoonList.getColumnConstraints().addAll(cc,cc,cc,cc);
            cartoonList.setHgap(30);
            cartoonList.setVgap(30);
            cartoonList.setMaxWidth(Main.scene.getWidth()-100);
            cartoonList.setPadding(new Insets(10));
            for (int i = 0; i < cartoons.size(); i++) {
                for (int j = 0; j < 5 && i*5+j < cartoons.size(); j++) {
                    Cartoon cartoon = cartoons.get(i*5+j);
                    VBox card = FXMLLoader.load(getClass().getResource("/GUI/cartoon-card.fxml"));
                    Rectangle rect = (Rectangle) card.lookup("#imageHolder");
                    Rectangle overlay = (Rectangle) card.lookup("#overlay");
                    if (cartoon.getPhoto() != null) {
                        ImagePattern ip = new ImagePattern(new Image(cartoon.getPhoto().getWebPath()));
                        rect.setFill(ip);
                        ImageView noPhoto = (ImageView) card.lookup("#noPhoto");
                        noPhoto.setOpacity(0);
                    }
                    rect.widthProperty().bind(cartoonList.widthProperty().divide(5).subtract(30));
                    overlay.widthProperty().bind(cartoonList.widthProperty().divide(5).subtract(30));
                    Label name = (Label) card.lookup("#name");
                    Label episodes = (Label) card.lookup("#episodes");
                    name.setText(cartoon.getName());
                    episodes.setText(cartoon.getEpisodesCnt() + " episodes");
                    Random random = new Random();
                    card.getStyleClass().add("bg-color-" + (random.nextInt(5)+1));
                    cartoonList.add(card, j, i);
                    card.setOnMouseClicked(e -> showCartoon(cartoon));
                }
            }
            listContainer.getChildren().add(cartoonList);
        } catch (IOException e) {
            e.printStackTrace();
        }
        content.setMaxHeight(Main.scene.getHeight());
        CartoonController.planet.getChildren().add(content);

    }

//    public void addCartoon(){
//        Cartoon c = new Cartoon(3, "url", 60, 2, 1, "cartoon");
//        CartoonService cs = new CartoonService();
//
//        cs.addCartoon(c);
//    }
//
//    public void deleteCartoon(int id){
//        CartoonService cs = new CartoonService();
//        cs.deleteObject(id,"Cartoon");
//    }

    public void showCartoon(Cartoon cartoon){
        StackPane content = null;
        try {
            content = FXMLLoader.load(getClass().getResource("/GUI/cartoon-details.fxml"));
            content.setMaxHeight(Main.scene.getHeight());
            content.setMaxWidth(Main.scene.getWidth());
            Label title = (Label) content.lookup("#title");
            Label episodesCnt = (Label) content.lookup("#count");
            Label resume = (Label) content.lookup("#resume");
            ImageView imageView = (ImageView) content.lookup("#image");

            imageView.setImage(new Image(cartoon.getPhoto().getWebPath()));
            title.setPrefWidth(Main.scene.getWidth() - 250 );
            title.setText(cartoon.getName().toUpperCase());

            episodesCnt.setText(episodesCnt.getText() + cartoon.getEpisodesCnt());
            resume.setText("Barcha Klem");

            GridPane gridPane = new GridPane();
            gridPane.setPadding(new Insets(50,0,50,0));
            gridPane.setHgap(50);

            ObservableList<Video> episodes = new CartoonService().findEpisodes(cartoon.getId());

            for (int i = 0; i < episodes.size(); i++){
                for (int j = 0; j < 3 && i*3+j < episodes.size(); j++){
                    Button b = FXMLLoader.load(getClass().getResource("/GUI/episode-btn.fxml"));
                    b.setText("Episode " + i*3 + j);
                    Video episode = episodes.get(i*3 + j);
                    b.setOnAction(e -> playEpisode(episode));
                    b.getStyleClass().addAll("btn", "btn-info");
                    gridPane.add(b, j, i);
                }
            }
            
            VBox details = (VBox) content.lookup("#infosBox");
            Button closeBtn = (Button) content.lookup("#closeBtn");
            closeBtn.setTranslateX(Main.scene.getWidth()/2);
            closeBtn.setTranslateY(- Main.scene.getHeight()/2);

            StackPane finalContent = content;
            closeBtn.setOnAction(e -> GameController.planet.getChildren().remove(finalContent));
            details.getChildren().add(gridPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
        CartoonController.planet.getChildren().add(content);
    }

    private void playEpisode(Video v) {
        KidsspaceController.isWatching = true;
        player = null;
        try {
            player = FXMLLoader.load(getClass().getResource("/GUI/video-player.fxml"));
            start = System.currentTimeMillis();
            currentEpisodeId = v.getId();
            wv = (WebView) player.lookup("#browser");
            Button close = (Button) player.lookup("#close");
            wv.getEngine().load(v.getUrl());
            close.setOnAction(e -> {
                KidsspaceController.isWatching = true;
                CartoonController.planet.getChildren().remove(player);
                wv.getEngine().load("");
                new CartoonService().saveCartoon(
                    KidsspaceController.getChild().getId(),
                    v.getId(),
                    System.currentTimeMillis() - start
                );
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        CartoonController.planet.getChildren().add(player);
    }


    public static void clearTraces(){
        KidsspaceController.isWatching = false;
        new GameService().saveGame(
            KidsspaceController.getChild().getId(),
            currentEpisodeId,
            System.currentTimeMillis() - start
        );
        CartoonController.planet.getChildren().remove(player);
        wv.getEngine().load("");
    }

//    public void updateCartoon(int id) {
//
//        CartoonService cs = new CartoonService();
//        Cartoon c = cs.findCartoon(id);
//        c.setName("new came");
//        cs.updateCartoon(c);
//        c = cs.findCartoon(id);
//        System.out.print(c);
//    }
//
//    public void listCartoons(){
//        CartoonService cs = new CartoonService();
//        System.out.println("Liste des cartoon");
//        ObservableList<Cartoon> cartoons = cs.findAll();
//        for (Cartoon c : cartoons){
//            System.out.println(c);
//        }
//    }
//
//    public void addEpisode(int id){
//        VideoService vs = new VideoService();
//        Video v = new Video(null, "url",null, 0, id, 4, "Episode 1");
//        vs.addEpisode(v);
//    }
//
//
//    public void listEpisodes(int id) {
//        VideoService vs = new VideoService();
//        ObservableList<Video> episodes = vs.findEpisodes(id);
//        for (Video v : episodes){
//            System.out.println(v);
//        }
//    }
//
//    public void deleteEpisode(int id){
//        VideoService vs = new VideoService();
//        vs.deleteObject(id, "video");
//    }
}
