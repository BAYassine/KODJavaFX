/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileOutputStream;
import Core.Main;
import Entities.Complaint;
import Entities.Category;
import Entities.User;
import Services.CategoryService;
import Services.ComplaintService;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

/**
 *
 * @author Meriem
 */
public class ComplaintController {

    public void addComplaint() {
        ComplaintService cs = new ComplaintService();
        CategoryService cv = new CategoryService();

        Date date = new Date(new java.util.Date().getTime());
        ArrayList<String> names = new ArrayList<>();
        VBox center = new VBox();
        try {
            center = FXMLLoader.load(getClass().getResource("/GUI/AddComplaint.fxml"));
        } catch (IOException ex) {
            Logger.getLogger(ComplaintController.class.getName()).log(Level.SEVERE, null, ex);
        }
        TextField description = (TextField) center.lookup("#description");
         TextField subject = (TextField) center.lookup("#subject");
        ComboBox categories = (ComboBox) center.lookup("#category");
        categories.setVisibleRowCount(25);
        ObservableList<Category> categories_dispo = cv.findCategory("Complaint");
        System.out.println(cv.findCategory("Complaint"));
        categories.setItems(categories_dispo);
      
        //fonction de sélection d'une catégorie

        Button submit = (Button) center.lookup("#submit");
        //  Button history=(Button) center.lookup("#history");
        submit.setOnAction(e -> {

            try {
           if (description.getText().isEmpty()) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("ERREUR");
                        alert.setHeaderText("Description vide");
                        alert.setContentText("Veuillez saisir une description");
                        alert.show();
                    
                    }
         if (subject.getText().isEmpty()){
             Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("ERREUR");
                        alert.setHeaderText("Sujet vide");
                        alert.setContentText("Veuillez saisir un sujet");
                        alert.show();
        }
       if (categories.getSelectionModel().isEmpty()){
             Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("ERREUR");
                        alert.setHeaderText("Catégorie vide");
                        alert.setContentText("Veuillez saisir une catégorie");
                        alert.show();
        }
        if (categories.getSelectionModel().isEmpty() && subject.getText().isEmpty() && description.getText().isEmpty())
       {
             Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("ERREUR");
                        alert.setHeaderText("Champs vides");
                        alert.setContentText("Veuillez remplir les champs");
                        alert.show();
       }
       else  {
            Complaint comp = new Complaint(description.getText(), subject.getText(), date, "non_traitee", Main.user, cv.findByName(categories.getValue().toString()));
            cs.addComplaint(comp);
//                      try {
//                          //showHistory();
//                      } catch (IOException ex) {
//                          Logger.getLogger(ComplaintController.class.getName()).log(Level.SEVERE, null, ex);
//                      }
       }
               

               
            } catch (SQLException ex) {
                Logger.getLogger(ComplaintController.class.getName()).log(Level.SEVERE, null, ex);
            }

        });

        Main.pane.setCenter(center);
    }

    public void showHistory() throws IOException {
        VBox center = new VBox();

       

        ComplaintService cv = new ComplaintService();
        ObservableList<Complaint> data = cv.findByUser(Main.user.getId());

        TableView<Complaint> table = new TableView<Complaint>();
        VBox tableC = new VBox();
        tableC = FXMLLoader.load(getClass().getResource("/GUI/ComplaintsHistorique.fxml"));

        TableColumn<Complaint, Integer> id = new TableColumn<Complaint, Integer>("ID");
        TableColumn<Complaint, Date> date = new TableColumn<Complaint, Date>("DATE");
        TableColumn<Complaint, String> subject = new TableColumn<Complaint, String>("SUJET");
        TableColumn<Complaint, Category> categorie = new TableColumn<Complaint, Category>("CATEGORIE");
        TableColumn<Complaint, String> description = new TableColumn<Complaint, String>("DESCRIPTION");
        TableColumn<Complaint, String> state = new TableColumn<Complaint, String>("ETAT");
        TableColumn<Complaint, User> parent_id = new TableColumn<Complaint, User>("UTILISATEUR");
        id.setCellValueFactory(new PropertyValueFactory<Complaint, Integer>("id"));
        date.setCellValueFactory(new PropertyValueFactory<Complaint, Date>("date"));
        subject.setCellValueFactory(new PropertyValueFactory<Complaint, String>("subject"));
        categorie.setCellValueFactory(new PropertyValueFactory<Complaint, Category>("category"));
        description.setCellValueFactory(new PropertyValueFactory<Complaint, String>("description"));
        state.setCellValueFactory(new PropertyValueFactory<Complaint, String>("state"));

        parent_id.setCellValueFactory(new PropertyValueFactory<Complaint, User>("parent"));
        table.setItems(null);
        table.setItems(data);
        table.getColumns().addAll(id, parent_id, categorie, description, subject, date, state);

        tableC.getChildren().add(table);
        center.getChildren().clear();
        center.getChildren().add(tableC);
        Button pdf = new Button();
        pdf = (Button) center.lookup("#pdf");
        pdf.setOnAction(e -> {
            System.out.println("tenzel");

            pdf();
        });
        ScrollPane sp = new ScrollPane();

        sp.setContent(tableC);
        Main.pane.setCenter(sp);

    }

    public void pdf() {
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream("mohsen.pdf"));
            document.open();
            Paragraph p1 = new Paragraph("Hello World!");
            Paragraph p2 = new Paragraph("saluut");
            document.add(p1);
            document.add(p2);
        } catch (Exception e) {
            System.out.println(e);
        }
        document.close();
        
        
        
    }

}
