package Controllers.Admin;

import Controllers.AdminController;
import Core.Main;
import Dependencies.pherialize.MixedArray;
import Dependencies.pherialize.Pherialize;
import Entities.Photo;
import Entities.School;
import Services.Etablissementservice;
import Services.SubjectService;
import Services.TeachersService;
import com.lynden.gmapsfx.GoogleMapView;
import com.lynden.gmapsfx.javascript.object.GoogleMap;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import javafx.scene.transform.Scale;
import javafx.collections.ObservableList;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.StageStyle;



import Core.Main;
import Entities.Teacher;
import Services.PhotoService;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class VideoEDUController {
      public void init() {
        VBox content = null;
        try {
            content = FXMLLoader.load(getClass().getResource("/GUI/admin/school-list.fxml"));
            VBox body = (VBox) content.lookup("#panelBody");
            content.setPrefWidth(AdminController.container.getWidth());
            content.setPrefHeight(AdminController.container.getHeight());
            Button addBtn = (Button) content.lookup("#addBtn");
            addBtn.setOnAction(e -> addecole());
            TableColumn<School, Integer> idColumn = new TableColumn<>("ID");
            idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
            idColumn.setMaxWidth(50);
            idColumn.setMinWidth(50);
            idColumn.setStyle("-fx-alignment : center");

            TableColumn<School, String> schoolnameColumn = new TableColumn<>("schoolname");
            schoolnameColumn.setCellValueFactory(new PropertyValueFactory<>("schoolname"));
            
            TableColumn<School, String> addressColumn = new TableColumn<>("address");
         addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
            
            TableColumn<School, Double> XschoolColumn = new TableColumn<>("Xschool");
           XschoolColumn.setCellValueFactory(new PropertyValueFactory<>("Xschool"));

            TableColumn<School, Double> YschoolColumn = new TableColumn<>("Yschool");
            YschoolColumn.setCellValueFactory(new PropertyValueFactory<>("Yschool"));
            
             TableColumn<School, Integer> schoolphoneColumn = new TableColumn<>("schoolphone");
            schoolphoneColumn.setCellValueFactory(new PropertyValueFactory<>("schoolphone"));
               
             TableColumn<School, String> schoolmailceColumn = new TableColumn<>("schoolmail");
            schoolmailceColumn.setCellValueFactory(new PropertyValueFactory<>("schoolmail"));
            
            
             

            TableView tableView = (TableView) body.lookup("#table");
            tableView.setItems(new Etablissementservice().findAll());
            tableView.getColumns().addAll(idColumn,schoolnameColumn,addressColumn, XschoolColumn,YschoolColumn,schoolphoneColumn,schoolmailceColumn);
        } catch (IOException e) {
            e.printStackTrace();
        }
        AdminController.container.getChildren().clear();
        AdminController.container.getChildren().add(content);
    }
      
      
   /*   
      
 public void addecole()   
 {
  AnchorPane center = null;
        try {
            center = FXMLLoader.load(getClass().getResource("/GUI/Map.fxml"));
             center.setPrefWidth(AdminController.container.getWidth());
            center.setPrefHeight(AdminController.container.getHeight());
        } catch (IOException ex) {
            Logger.getLogger(Controllers.Admin.EtablissementController.class.getName()).log(Level.SEVERE, null, ex);
        }
    
        AdminController.container.getChildren().add(center);
}
        
    
 */
 
 
 
   public void addecole()   
    {
        AnchorPane center = null;
        try {
           center = FXMLLoader.load(getClass().getResource("/GUI/Map.fxml"));
             center.setPrefWidth(AdminController.container.getWidth());
            center.setPrefHeight(AdminController.container.getHeight());
            TextField nom = (TextField) center.lookup("#nom");
            TextField numtel = (TextField) center.lookup("#numtel");
            TextField email = (TextField) center.lookup("#email");
            
            
            TextField address = (TextField) center.lookup("#address");
            Label longitudeLabel = (Label) center.lookup("#longitudeLabel");
            Label latitudeLabel = (Label) center.lookup("#latitudeLabel");
            Button save = (Button) center.lookup("#save");
            save.setOnAction(e -> {
                School u = new School(
                    nom.getText(),
                    address.getText(),
                    Integer.parseInt(numtel.getText()),
                    email.getText(),     
                    Double.parseDouble(longitudeLabel.getText()),
                    Double.parseDouble(latitudeLabel.getText())
                );
                new Etablissementservice().addschool(u);
                init();
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        AdminController.container.getChildren().clear();
        AdminController.container.getChildren().add(center);
    }
     
 
    }

