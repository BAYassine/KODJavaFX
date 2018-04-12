package Controllers;

import Controllers.Admin.GamesController;
import Controllers.Admin.MusicController;
import Core.Main;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class AdminController {

    public static Pane container = null;
    public static TreeView<String> treeView;

    public void init () {
        HBox layer = null;
        try {
            layer = FXMLLoader.load(getClass().getResource("/GUI/admin.fxml"));
            ScrollPane navigator = (ScrollPane) layer.getChildren().get(0);
            ScrollPane rightPane = (ScrollPane) layer.getChildren().get(1);
            Button homeBtn = (Button) navigator.getContent().lookup("#home");
            HBox finalLayer = layer;
            homeBtn.setOnAction(e -> Main.sp.getChildren().remove(finalLayer));
            container = (Pane) rightPane.getContent().lookup("#content");
            VBox sidebar = (VBox) navigator.getContent().lookup("#sidebar");
            HBox header = (HBox) rightPane.getContent().lookup("#header");
            header.setPrefWidth(Main.scene.getWidth()-280);
            container.setPrefWidth(Main.scene.getWidth()-280);

            treeView = new TreeView<>();
            TreeItem<String> dashboard = new TreeItem<>("Dashboard");
            TreeItem<String> users = new TreeItem<>("Gestion des Membres");
            FontAwesomeIconView fa = new FontAwesomeIconView(FontAwesomeIcon.USERS);
            users.setGraphic(fa);

            TreeItem<String> listUsers = new TreeItem<>("Liste des utilisateurs");
            fa = new FontAwesomeIconView(FontAwesomeIcon.LIST);
            listUsers.setGraphic(fa);


            TreeItem<String> addUser = new TreeItem<>("Ajouter un membre");
            fa = new FontAwesomeIconView(FontAwesomeIcon.USER_PLUS);
            addUser.setGraphic(fa);

            users.getChildren().addAll(listUsers, addUser);
            users.setExpanded(true);

            TreeItem<String> listGames = new TreeItem<>("Liste des jeux");
            fa = new FontAwesomeIconView(FontAwesomeIcon.LIST);
            listGames.setGraphic(fa);

            TreeItem<String> addGame = new TreeItem<>("Ajouter un jeu");
            fa = new FontAwesomeIconView(FontAwesomeIcon.PLUS_SQUARE);
            addGame.setGraphic(fa);

            TreeItem<String> games = new TreeItem<>("Gestion des jeux");
            fa = new FontAwesomeIconView(FontAwesomeIcon.GAMEPAD);
            games.setGraphic(fa);
            games.setExpanded(true);
            games.getChildren().addAll(listGames, addGame);

            TreeItem<String> songs = new TreeItem<>("Liste des chansons");
            fa = new FontAwesomeIconView(FontAwesomeIcon.MUSIC);
            songs.setGraphic(fa);
            songs.setExpanded(true);

            TreeItem<String> root = new TreeItem<>();

            root.setExpanded(true);
            root.setGraphic(null);

            root.getChildren().addAll(dashboard, users, games,songs);
            treeView.setRoot(root);
            treeView.setShowRoot(false);
            treeView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                switch (newValue.getValue()){
                    case "Liste des utilisateurs":
                        new Controllers.Admin.UsersController().init();
                        break;
                    case "Ajouter un membre":
                        new Controllers.Admin.UsersController().addUser();
                        break;
                    case "Liste des jeux":
                        new GamesController().init();
                        break;
                    case "Ajouter un jeu":
                        new GamesController().addGame();
                        break;
                    case "Liste des chansons":
                        new MusicController().init();
                        break;
                }
            });
            sidebar.getChildren().add(treeView);


        } catch (IOException e) {
            e.printStackTrace();
        }
        Main.sp.getChildren().add(layer);
    }

}
