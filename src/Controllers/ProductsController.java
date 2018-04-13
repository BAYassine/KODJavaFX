/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import Core.Main;
import Entities.Category;
import Entities.Thread;
import Entities.Commentaire;
import Entities.LigneCommande;
import Entities.Photo;
import Entities.Product;
import Services.CategoryService;
import Services.CommentaireService;
import Services.LigneCommandeService;
import Services.PhotoService;
import Services.ProductServices;
import Services.ThreadService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.controlsfx.control.Rating;

/**
 * FXML Controller class
 *
 * @author ASUS
 */
public class ProductsController {

    @FXML
    private VBox card;
    @FXML
    private ImageView image;
    @FXML
    private Label name;
    @FXML
    private Label quantite;
    @FXML
    private Button add;
    @FXML
    private Button ajout;
    @FXML
    private Label prix;
    @FXML
    private ComboBox<Integer> comb;
    @FXML
    private AnchorPane AnchorPane1;
    @FXML
    private ChoiceBox<String> combo_rech;
    ObservableList<String> comboList = FXCollections.observableArrayList("nom", "sexe");
    @FXML
    private TextField id;
    @FXML
    private Rating rating;

    /**
     * Initializes the controller class.
     */
    /*  LoggedUser.setId_produit(liste1.getSelectionModel().getSelectedItem().getId_produit());
        LoggedUser.setNom(liste1.getSelectionModel().getSelectedItem().getNom());
        LoggedUser.setCatégorie(liste1.getSelectionModel().getSelectedItem().getCatégorie());
        LoggedUser.setPrix_produit(liste1.getSelectionModel().getSelectedItem().getPrix_produit()); 
        LoggedUser.setDescription(liste1.getSelectionModel().getSelectedItem().getDescription());*/
    public void init() {
        CategoryService cv = new CategoryService();
        VBox center = new VBox();
        ProductServices cs = new ProductServices();
        ObservableList<Product> produits = cs.showProducts();
        try {
            center = FXMLLoader.load(getClass().getResource("/GUI/gird.fxml"));
            center.setPrefWidth(Main.screen.getWidth());
            center.setPrefHeight(Main.screen.getHeight());
            GridPane grid = new GridPane();
            VBox list = (VBox) center.lookup("#list");
            ColumnConstraints cc = new ColumnConstraints();
            cc.setPercentWidth(33.333);
            grid.getColumnConstraints().addAll(cc, cc, cc, cc);
            grid.setHgap(80);
            grid.setVgap(30);
            grid.setPrefWidth(Main.scene.getWidth() - 100);
            grid.setPadding(new Insets(10));
            for (int i = 0; i < produits.size(); i++) {
                for (int j = 0; j < 3 && j + i * 3 < produits.size(); j++) {
                    Product p = produits.get(i * 3 + j);
                    card = FXMLLoader.load(getClass().getResource("/GUI/yassintest.fxml"));
                    image = (ImageView) card.getChildren().get(0);
                    Photo img = p.getImg();
                    image.setImage(new Image(img.getWebPath()));
                    VBox right = (VBox) card.getChildren().get(1);
                    name = (Label) right.getChildren().get(0);
                    name.setText(p.getName());
                    prix = (Label) right.getChildren().get(1);
                    prix.setText(Integer.toString(p.getPrice()) + "DT");
                    quantite = (Label) right.getChildren().get(2);
                    quantite.setText(Integer.toString(p.getQuantite()) + "produits disponibles");
                    add = (Button) card.lookup("#add");
                    ObservableList<Category> categories = cv.findCategory("Produits");
                    ListView<Category> categories_container = new ListView<Category>();
                    categories_container = (ListView) center.lookup("#categories");
                    categories_container.setItems(categories);

                    int finalJ = j;
                    image.setOnMouseClicked(e -> {
                        try {
                            this.showMore(p.getId());
                        } catch (SQLException ex) {
                            Logger.getLogger(ProductsController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        ThreadService ts = new ThreadService();
                        try {
                            if (ts.ThreadExiste(String.valueOf(p.getId())) == null) {
                                ts.AjouterThread(new Entities.Thread(String.valueOf(p.getId())));
                            }
                        } catch (SQLException ex) {
                            Logger.getLogger(ProductsController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    });
                    add.setOnAction(e -> {
                        LigneCommande ligne;
                        ligne = new LigneCommande(p, 1);
                        ligne.setUserID(Main.user.getId());
                        ligne.setProviderId(p.getProviderId());
                        Main.panier.add(ligne);
                        new LigneCommandesController().afficherPanier();
                    });
                    Random random = new Random();
                    card.getStyleClass().add("bg-color-" + (random.nextInt(5) + 1));
                    grid.add(card, j, i);
                }
            }
            list.getChildren().add(grid);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ScrollPane sp = new ScrollPane();
        sp.setContent(center);
        Main.pane.setCenter(sp);
    }

    private void showMore(int id) throws SQLException {

        try {

            ProductServices cs = new ProductServices();
            ImageView image;
            PhotoService is = new PhotoService();
            Product produit = cs.moreProduct(id);
            VBox card = FXMLLoader.load(getClass().getResource("/GUI/moreProduct.fxml"));
            card.setPrefWidth(Main.scene.getWidth());
            /*   final Rating rating = new Rating();
                rating.addEventHandler(MouseEvent.MOUSE_CLICKED, (event) -> {
                    
                    System.out.println(event.getEventType());
                    System.out.println("HI");
                    System.out.println(rating.getRating());     
                    ProduitService.updateMoyenne(rating.getRating(), Integer.parseInt(bouton.getAccessibleHelp()));
                }); */
            image = (ImageView) card.lookup("#image");
            Photo img = is.findImage(produit.getImg().getId());
            image.setImage(new Image(img.getWebPath()));

            ComboBox<Integer> combo = (ComboBox<Integer>) card.lookup("#quantite");

            for (int j = 1; j < new ProductServices().findquantite(id); j++) {
                combo.getItems().add(j);
            }

            Label name = (Label) card.lookup("#name");
            Label price = (Label) card.lookup("#price");

            Label description = (Label) card.lookup("#description");
            TextArea commentaire = (TextArea) card.lookup("#commentaire");
            //Label date = (Label) card.lookup("#phone");
            name.setText(produit.getName());
            price.setText(Integer.toString(produit.getPrice()) + "DT");
            description.setText(produit.getDescription());
            ListView listeC = (ListView) card.lookup("#listeC");
            CommentaireService c = new CommentaireService();
            List<Commentaire> listCom = c.AfficherCommentaire(String.valueOf(produit.getId()));
            ObservableList<Commentaire> data = FXCollections.observableArrayList(listCom);

            listeC.setItems(data);
            Label username = (Label) card.lookup("#username ");
            username.setText(Main.user.getUsername());
            Label dateC = (Label) card.lookup("#dateC");
            Commentaire com = new Commentaire();


            int nbre = 0;
            nbre = c.CountCommentaireProduit(produit.getId());
            Label nbr = (Label) card.lookup("#nbr");
            nbr.setText(String.valueOf(nbre));
            ajout = (Button) card.lookup("#ajout");

            ajout.setOnAction(e -> {


                Thread thread = new Thread(String.valueOf(produit.getId()));
                Date date = new Date();
                Commentaire com1 = new Commentaire(Main.user.getId(), 1, thread, commentaire.getText(), date);
                try {
                    c.AjouterCommentaire(com1);
                    
                   
                             
                   
                    /*listeC.setCellFactory((ListView<Commentaire> param) -> {
                     ListCell<Commentaire> cell = new ListCell<Commentaire>() {
                     @Override
                     protected void updateItem(Commentaire p , boolean bl) {
                      
                      setText(commentaire);
                       return cell ;
                        listeC.setItems();
                  }}
                      });*/

                } catch (SQLException ex) {
                    Logger.getLogger(ProductsController.class.getName()).log(Level.SEVERE, null, ex);
                }

            });

            add = (Button) card.lookup("#add");

            add.setOnAction(e -> {
                LigneCommande ligne = new LigneCommande(produit, combo.getSelectionModel().getSelectedItem());
                ligne.setUserID(Main.user.getId());
                ligne.setProviderId(produit.getProviderId());
                Main.panier.add(ligne);
                new LigneCommandesController().afficherPanier();
            });
            ScrollPane sp = new ScrollPane();
            sp.setPrefHeight(Main.scene.getHeight());
            sp.setPrefWidth(Main.scene.getWidth());
            sp.setContent(card);
            Main.pane.setCenter(sp);
        } catch (IOException ex) {
            Logger.getLogger(ProductsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void show() {

        //VBox center = null ;
        AnchorPane center = null;
        try {
            center = FXMLLoader.load(getClass().getResource("/GUI/yassintest.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Main.pane.setCenter(center);

    }

    public void onClick(Product p, int quantite) {
        ProductServices ps = new ProductServices();
        LigneCommandeService ls = new LigneCommandeService();

        // System.out.println(p);
        LigneCommande l = new LigneCommande(p.getId(), p.getPrice(), quantite);
        //  System.out.println(l);
        ls.insert(l);
        AnchorPane center = null;
        //  System.out.println(LoggedUser.getId_produit());
        try {

            center = FXMLLoader.load(getClass().getResource("/GUI/ligneCommandes.fxml"));

        } catch (IOException ex) {
            Logger.getLogger(Main.user.getUsername()).log(Level.SEVERE, null, ex);
        }

    }


}
