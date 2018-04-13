package Controllers.Admin;

import Controllers.AdminController;
import Core.Main;
import Dependencies.pherialize.MixedArray;
import Dependencies.pherialize.Pherialize;
import Entities.School;
import Services.Etablissementservice;

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
import Entities.Subject;
import Entities.Teacher;
import Services.PhotoService;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;

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
import org.controlsfx.control.PopOver;


public class EtablissementController {
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
            
            
               TableColumn<School, Integer> actionsColumn = new TableColumn<>("Actions");
            actionsColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
            actionsColumn.setCellFactory(e -> new TableCell<School, Integer>() {
                @Override
                public void updateItem(Integer item, boolean empty) {
                    super.updateItem(item, empty);
                    if (!empty && item != null) {
                        VBox actions = new VBox();
                        actions.setSpacing(10);
                        actions.setAlignment(Pos.CENTER);
                        actions.setFillWidth(true);
                        Button deleteBtn = new Button("Delete");
                        deleteBtn.getStyleClass().addAll("btn", "btn-danger");
                        deleteBtn.setOnAction(e -> {
                            new Etablissementservice().deleteObject(item, "School");
                            init();
                        });
                        Button updateBtn = new Button("Update");
                        updateBtn.getStyleClass().addAll("btn", "btn-success");
                        updateBtn.setOnAction(e -> updateSchool(item));
                        actions.getChildren().addAll(updateBtn, deleteBtn);
                        setGraphic(actions);
                    }
                }
            });
            
            
             

            TableView tableView = (TableView) body.lookup("#table");
            tableView.setItems(new Etablissementservice().findAll());
            tableView.getColumns().addAll(idColumn,schoolnameColumn,addressColumn, XschoolColumn,YschoolColumn,schoolphoneColumn,schoolmailceColumn,actionsColumn);
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
 
  public boolean controleSaisie(AnchorPane center) {
             TextField numtel = (TextField) center.lookup("#numtel");
             FontAwesomeIconView telw2 =(FontAwesomeIconView) center.lookup("#telw2");
             TextField email = (TextField) center.lookup("#email");
             FontAwesomeIconView mailw =(FontAwesomeIconView) center.lookup("#mailw");
             
       if ((!(numtel.getText().matches("[0-9]+"))) || numtel.getText().isEmpty()) {
            telw2.setVisible(true);
            numtel.setStyle("-jfx-focus-color:red");
            numtel.requestFocus();
            PopOver pop = new PopOver();
            pop.setContentNode(new Label("Veuillez saisir un numero de telephone valide"));
            telw2.setOnMouseEntered((event) -> {
                pop.show(telw2);
            });
            telw2.setOnMouseExited((event) -> {
                pop.hide();
            });
            numtel.setOnKeyTyped((event2) -> {
                telw2.setVisible(false);
                numtel.setStyle("-jfx-focus-color:green");
            });
            return false;

        }
       if (!(email.getText().matches("[@,a-z]")) || email.getText().isEmpty()) {
            mailw.setVisible(true);
            email.setStyle("-jfx-focus-color:red");
            email.requestFocus();
            PopOver pop = new PopOver();
            pop.setContentNode(new Label("Veuillez saisir un email valide"));

            mailw.setOnMouseEntered((event) -> {
                pop.show(mailw);
            });
            mailw.setOnMouseExited((event) -> {
                pop.hide();
            });
            email.setOnKeyTyped((event2) -> {
                mailw.setVisible(false);
                email.setStyle("-jfx-focus-color:green");
            });
            return false;

        }
      
        return true;
    }
 
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
            AnchorPane fcontent = center;
            save.setOnAction(e -> {
                if (controleSaisie(fcontent)) {
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
                  }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        AdminController.container.getChildren().clear();
        AdminController.container.getChildren().add(center);
    }
   
   
   //   School g = new Etablissementservice().findSchool(id);
   //            prenom.setText(g.getLastname());
   
   /*
    save.setOnAction(e -> {
                   g.setAccount(lien.getText());
                   g.setDegree(degree.getText());
                   g.setExperience(exp.getText());
                   g.setHobbies(hob.getText());
                   g.setLastname(prenom.getText());
                   g.setName(nom.getText());
                   g.setPhone(Integer.parseInt(num.getText()));
                   g.setPrice(Double.parseDouble(prix.getText()));
                   g.setSubject(subject.getValue());
                    
                    
                    new Etablissementservice().updateSchool(g);
                    
                    init();
                
            });
   */

   
    private void updateSchool(Integer id) {
         AnchorPane center = null;
        try {
    center = FXMLLoader.load(getClass().getResource("/GUI/Map.fxml"));
    School g = new Etablissementservice().findSchool(id);
//            Label headTitle = (Label) content.lookup("#headTitle");
//            headTitle.setText("Modifier un teacher");
             center.setPrefWidth(AdminController.container.getWidth());
            center.setPrefHeight(AdminController.container.getHeight());
            TextField nom = (TextField) center.lookup("#nom");
            nom.setText(g.getSchoolname());
            
         TextField numtel = (TextField) center.lookup("#numtel");
             numtel.setText(Integer.toString(g.getSchoolphone()));
            
            
             TextField email = (TextField) center.lookup("#email");
             email.setText(g.getSchoolmail());
             
             TextField address = (TextField) center.lookup("#address");
             address.setText(g.getAddress());
             
             Label longitudeLabel = (Label) center.lookup("#longitudeLabel");
             longitudeLabel.setText(Double.toString(g.getXschool()));
             
                 Label latitudeLabel = (Label) center.lookup("#latitudeLabel");
                          latitudeLabel.setText(Double.toString(g.getYschool()));
            
            Button save = (Button) center.lookup("#save");
            
            save.setOnAction(e -> {
                   g.setSchoolname(nom.getText());
                   g.setAddress(address.getText());
                   g.setSchoolmail(email.getText());
                   g.setSchoolphone(Integer.parseInt(numtel.getText()));
                   g.setXschool(Double.parseDouble(longitudeLabel.getText()));
                   g.setYschool(Double.parseDouble(latitudeLabel.getText()));
                   
                    new Etablissementservice().updateSchool(g);

                    
                    init();
                
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        AdminController.container.getChildren().clear();
        AdminController.container.getChildren().add(center);
    }
     
 
    }

