/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import Core.Main;
import Entities.Commande;
import Services.CommandeService;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author ASUS
 */
public class listeCommandeController implements Initializable {
    
    @FXML
    private TableView<Commande> table;
    @FXML
    private TableColumn<?, ?> id;
    @FXML
    private TableColumn<?, ?> total;
    @FXML
    private TableColumn<?, ?> date;
    @FXML
    private Button  checkout;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            afficherlisteCommande();
        } catch (IOException ex) {
            Logger.getLogger(listeCommandeController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }    
     public void afficherlisteCommande() throws IOException{
        
        ScrollPane main= null; 
      
        main = FXMLLoader.load(getClass().getResource("/GUI/listeCommande.fxml"));
     
        CommandeService c = new CommandeService();
        ObservableList<Commande> data = c.findAll();
        System.out.println(data.size());
        TableColumn<Commande, Integer> idColumn = new TableColumn<>("id");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        
        TableColumn<Commande, Date> durationColumn = new TableColumn<>("date");
        durationColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        
        TableColumn<Commande, Integer> totalColumn= new TableColumn<>("total");
        totalColumn.setCellValueFactory(new PropertyValueFactory<>("total"));
        
        TableView tableView= (TableView) main.getContent().lookup("#table");
        tableView.setItems(null);
        tableView.setItems(c.findAll());
        tableView.getColumns().addAll(idColumn,durationColumn,totalColumn);
         checkout = (Button) main.getContent().lookup("#checkout");
                    checkout.setOnAction(e -> {
                               Stage primaryStage = new Stage();
        Parent login = new Parent() {
};
        try {
         login = FXMLLoader.load(getClass().getResource("/GUI/Payment.fxml"));
        } catch (IOException ex) {
            Logger.getLogger(PaymentController.class.getName()).log(Level.SEVERE, null, ex);
        }
        Scene scene = new Scene(login);
        
        primaryStage.setTitle("LOGIN");
        primaryStage.setScene(scene);
        primaryStage.show();
                    });
        Main.pane.setCenter(main);
   
    }
       
}
