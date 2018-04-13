/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import Core.Main;
import Entities.Commande;

import Entities.LigneCommande;
import Services.CommandeService;

import java.io.IOException;
import java.sql.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 *
 */
public class LigneCommandesController {

    @FXML
    private Button payer;

    @FXML
    private HBox a;

    public void afficherPanier() {
        ScrollPane sp = null;

        try {
            sp = FXMLLoader.load(getClass().getResource("/GUI/panier.fxml"));
            VBox list = (VBox) sp.getContent().lookup("#list");
            for (int i = 0; i < Main.panier.size(); i++) {
                LigneCommande ligne = Main.panier.get(i);
                HBox item = FXMLLoader.load(getClass().getResource("/GUI/panier-item.fxml"));
                ImageView img = (ImageView) item.lookup("#image");
                Button delete = (Button) item.lookup("#delete");
               
                Label name = (Label) item.lookup("#name");
                Label price = (Label) item.lookup("#price");
                Label qntt = (Label) item.lookup("#qntt");
                Label total = (Label) item.lookup("#total");
                Label totals = (Label) sp.getContent().lookup("#totals");
                img.setImage(new Image(ligne.getProduct().getImg().getWebPath()));
                name.setText(ligne.getProduct().getName());
                price.setText(ligne.getProduct().getPrice() + "");
                qntt.setText(ligne.getQuantite() + "");
                float totalf=0;
                totalf+=(float)ligne.getPrice();
                total.setText(ligne.getPrice() + "");
                System.out.println(totalf);
                totals.setText("Total :" + totalf + "");
                list.getChildren().add(item);

            
            payer = (Button) sp.getContent().lookup("#payer");
            float finaltotal = totalf;
            payer.setOnAction(e -> {
            
            CommandeService os= new CommandeService();
             Date date= new Date(new java.util.Date().getTime());
             Commande o = new Commande (Main.user.getId(),date,finaltotal);
             System.out.println(o);
             os.addOrder(o);
                    try {
                        /*
                        Alert alertE = new Alert(Alert.AlertType.CONFIRMATION);
                        alertE.setTitle("Commande valider ");
                        alertE.setHeaderText(" Commande valider");
                        Optional<ButtonType> result = alertE.showAndWait();
                        */
                        new listeCommandeController().afficherlisteCommande();
                    } catch (IOException ex) {
                        Logger.getLogger(LigneCommandesController.class.getName()).log(Level.SEVERE, null, ex);
                    }
            });

        } }catch (IOException ex) {
            Logger.getLogger(LigneCommandesController.class.getName()).log(Level.SEVERE, null, ex);
        }
        Main.pane.setCenter(sp);
    }
}

//    public void afficherPanier() {
//        LigneCommandeService ls = new LigneCommandeService();
//        ObservableList<LigneCommande> lc = ls.getAll();
//
//        wishlist.setCellFactory((ListView<LigneCommande> param) -> {
//            ListCell<LigneCommande> cell = new ListCell<LigneCommande>() {
//                @Override
//                protected void updateItem(LigneCommande p, boolean bl) {
//                    super.updateItem(p, bl);
//                    if (p != null) {
//
//                        ProductServices ps = new ProductServices();
//                        System.out.println(p.getId_produit());
//                        Product p2 = ps.search(p.getId_produit());
//                        CommandeService cs = new CommandeService();
//                        Commande c = cs.searchIdLigne(p.getId_ligne());
//                        if (c != null) {
//                            //System.out.println(p2);
//
//                            Image img = new Image(p2.getImg().getWebPath(), 300, 300, true, true, true);
//                            ImageView imgV = new ImageView(img);
//                            setGraphic(imgV);
//                            setText("Nom : " + p2.getName() + "\n Catégorie : " + p2.getCategory() + "\n prix : " + p.getPrix_produit() + "\n" + "Quantite : " + p.getQuantite() + "\n" + "Description : " + p2.getDescription() + "\n\n\n" + "                                                                                                                      Commandé! ");
//                            // setText("Prix : "+p.getPrix_produit());
//                        } else {
//
//                            Image img = new Image(p2.getImg().getWebPath(), 300, 300, true, true, true);
//                            ImageView imgV = new ImageView(img);
//                            setGraphic(imgV);
//                            setText("Nom : " + p2.getName() + "\n Catégorie : " + p2.getCategory() + "\n prix : " + p.getPrix_produit() + "\n" + "Quantite : " + p.getQuantite() + "\n" + "Description : " + p2.getDescription());
//
//                        }
//                    }
//                }
//            };
//            return cell;
//        });
//        wishlist.setItems(lc);
//
//    }
//    @FXML
//    public void supprimerProduit() {
//        LigneCommandeService ls = new LigneCommandeService();
//        ObservableList<LigneCommande> selection = wishlist.getSelectionModel().getSelectedItems();
//        for (LigneCommande l : selection) {
//            ls.delete(l.getId_ligne());
//        }
//        // LigneCommande l = new LigneCommande() ; 
//        //ls.delete(l.getId_ligne()) ; 
//    }
//      @FXML
//    private void afficher(ActionEvent event) {
//        AnchorPane2.setVisible(true);
//       retire.setVisible(true);
//    }
//
//    @FXML 
//     public void rechercherProduit(){
//         LigneCommandeService ls = new LigneCommandeService()  ; 
//       // ObservableList<LigneCommande> selection = wishlist.getSelectionModel().getSelectedItems() ;
//       //  for(LigneCommande l : selection){
//        ObservableList<LigneCommande> lc =  ls.seachCategorie(search.getText()) ; 
//      wishlist.setCellFactory((ListView<LigneCommande> param) -> {
//              ListCell<LigneCommande> cell = new ListCell<LigneCommande>() {
//                  @Override
//                  protected void updateItem(LigneCommande p , boolean bl) {
//                      super.updateItem(p, bl);
//                      if(p!=null){
//                         ProductServices ps = new ProductServices() ; 
//                         Product p2 = ps.search(p.getId_produit()) ;
//                          //System.out.println(p2);
//                          Image img = new Image(p2.getImg().getWebPath(), 300, 300, true, true, true) ;
//                          ImageView imgV = new ImageView(img) ;
//                          setGraphic(imgV);
//                          setText("Nom : "+p2.getName()+"\n Catégorie : "+p2.getCategory()+"\n prix : "+p.getPrix_produit()+"\n"+"Quantite : "+p.getQuantite()+"Description : "+p2.getDescription()); 
//                        // setText("Prix : "+p.getPrix_produit());
//                      }
//                  }
//              } ; return cell ;
//          });
//      wishlist.setItems(lc);
//           
//       }
//     

