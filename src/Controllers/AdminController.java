package Controllers;

import Controllers.Admin.GamesController;
import Controllers.Admin.MusicController;
import Controllers.Admin.VideoEDUController;
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
            navigator.setPrefHeight(Main.scene.getHeight());
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

            TreeItem<String> enseignant = new TreeItem<>("Liste des enseignants");
            fa = new FontAwesomeIconView(FontAwesomeIcon.USER_PLUS);
            enseignant.setGraphic(fa);
            enseignant.setExpanded(true);

            TreeItem<String> schools = new TreeItem<>("Liste des ecoles");
            fa = new FontAwesomeIconView(FontAwesomeIcon.USER_PLUS);
            schools.setGraphic(fa);
            schools.setExpanded(true);
//
//            TreeItem<String> videos = new TreeItem<>("Liste des videos");
//            fa = new FontAwesomeIconView(FontAwesomeIcon.USER_PLUS);
//            videos.setGraphic(fa);
//            videos.setExpanded(true);
//
//            TreeItem<String> addvideo = new TreeItem<>("Ajouter une video");
//            fa = new FontAwesomeIconView(FontAwesomeIcon.PLUS_SQUARE);
//            addvideo.setGraphic(fa);
//            videos.getChildren().add(addvideo);

            TreeItem<String> addschool = new TreeItem<>("Ajouter une ecole");
            fa = new FontAwesomeIconView(FontAwesomeIcon.PLUS_SQUARE);
            addschool.setGraphic(fa);
            schools.getChildren().add(addschool);

            TreeItem<String> addenseignant = new TreeItem<>("Ajouter un enseignant");
            fa = new FontAwesomeIconView(FontAwesomeIcon.PLUS_SQUARE);
            addenseignant.setGraphic(fa);
            enseignant.getChildren().add(addenseignant);
            TreeItem<String> babysitters = new TreeItem<>("Babysitters");
            TreeItem<String> complaints = new TreeItem<>("Reclamations");
            TreeItem<String> addresses = new TreeItem<>("Adresses");
            addresses.setGraphic(fa);
            TreeItem<String> articles = new TreeItem<>("Articles");
            fa = new FontAwesomeIconView(FontAwesomeIcon.LIST);
            articles.setGraphic(fa);

            TreeItem<String> products = new TreeItem<>("Liste des produits");
            fa = new FontAwesomeIconView(FontAwesomeIcon.SHOPPING_BAG);
            products.setGraphic(fa);
            products.setExpanded(true);

            fa = new FontAwesomeIconView(FontAwesomeIcon.MUSIC);
            songs.setGraphic(fa);
            songs.setExpanded(true);

            TreeItem<String> addevent = new TreeItem<>("Ajouter un event");
            fa = new FontAwesomeIconView(FontAwesomeIcon.PLUS);
            addevent.setGraphic(fa);

            TreeItem<String> updateevent = new TreeItem<>("Modifier un event");
            fa = new FontAwesomeIconView(FontAwesomeIcon.PENCIL);
            updateevent.setGraphic(fa);

            users.setExpanded(true);

            TreeItem<String> root = new TreeItem<>();

            root.setExpanded(true);
            root.setGraphic(null);

            root.getChildren().addAll(dashboard, users, games,songs,enseignant,schools, babysitters, addresses, articles, complaints);
            root.getChildren().addAll(addevent,updateevent, products);
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
                    case "Liste des enseignants":
                        new Controllers.Admin.TeacherController().init();
                        break;
                    case "Liste des ecoles":
                        new Controllers.Admin.EtablissementController().init();
                        break;
                    case "Ajouter une ecole":
                        new Controllers.Admin.EtablissementController().addecole();
                        break;
//                    case "Liste des videos":
//                        new VideoEDUController().init();
//                        break;
//                    case "Ajouter une video":
//                        new Controllers.Admin.EtablissementController().addecole();
//                        break;
                    case "Ajouter un enseignant":
                        new Controllers.Admin.TeacherController().addprof();
                        break;
                    case "Babysitters":
                        new Controllers.Admin.BabysitterController().init();
                        break;
                    case "Adresses":
                        new Controllers.Admin.AddressController().init();
                        break;
                    case "Articles":
                        new Controllers.Admin.ArticlesController().init();
                        break;
                    case "Reclamations":
                        new Controllers.Admin.ComplaintController().init();
                        break;
                    case "Liste des produits":
                        new Controllers.Admin.ProduitsController().init();
                        break;
                    case "Ajouter un event":
                        new Controllers.Admin.EventController().addEvent();
                        break;
                    case "Modifier un event":
                        new Controllers.Admin.EventController().updateEvent(18);
                        break;
                }
            });
            treeView.setPrefHeight(Main.sp.getHeight());
            sidebar.getChildren().add(treeView);


        } catch (IOException e) {
            e.printStackTrace();
        }
        Main.sp.getChildren().add(layer);
    }

}
