/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers.Admin;

import Controllers.*;
import Core.Main;
import Entities.Child;
import Entities.Event;
import Entities.Photo;
import Entities.Video;
import Services.ChildService;
import Services.EventService;
import Services.PhotoService;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;

/**
 *
 * @author Rami
 */
public class EventController {

    private File image;
        
    public void init() {
        
        ScrollPane center = null;
        ObservableList<Event> events = new EventService().findAll();
        try {
            center = FXMLLoader.load(getClass().getResource("/GUI/list-events.fxml"));
            center.setPrefHeight(Main.screen.getHeight() - 250);
            GridPane list = new GridPane();
            list.setHgap(30);
            list.setVgap(30);
            list.setPadding(new Insets(50));
            for (int i = 0; i < events.size(); i++) {
                for (int j = 0; j < 2 && j + i * 2 < events.size(); j++) {
                    Event ev = events.get(j + i * 2);
                    HBox card = FXMLLoader.load(getClass().getResource("/GUI/event.fxml"));
                    ImageView image = (ImageView) card.lookup("#image");
                    Photo img = events.get(j + i * 2).getPhoto();
                    //image.setImage(new Image(img.getWebPath()));
                    Label type = (Label) card.lookup("#type");
                    type.setText(events.get(j + i * 2).getType());
                    Label address = (Label) (Label) card.lookup("#address");
                    address.setText("Adresse : " + events.get(j + i * 2).getLocation());
                    Label time = (Label) card.lookup("#time");
                    time.setText("Date : " + events.get(j + i * 2).getStartDate());
                    Label partis = (Label) card.lookup("#partis");
                    partis.setText("Nombre de places disponibles : " + events.get(j + i * 2).getAvailablePlaces());
                    Button details = (Button) card.lookup("#details");
                    Button reserver = (Button) card.lookup("#reserver");
                    Button remove = (Button) card.lookup("#remove");
                    int finalJ = j;
                    //more.setOnAction(e -> this.showActivity(mykids.get(finalJ)));
                    reserver.setOnAction(e -> showModal(ev));
                    Random random = new Random();
                    card.getStyleClass().add("bg-color-" + (random.nextInt(5) + 1));
                    list.add(card, j, i);
                }
            }
            VBox listV = (VBox) center.getContent();
            listV.setPrefWidth(Main.screen.getWidth());
            listV.getChildren().add(list);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Main.pane.setCenter(center);
    }
    
    public void deleteEvent(int id) {
        EventService cs = new EventService();
        cs.deleteObject(id, "Event");
    }

    public void showEvent(int id) {
        EventService cs = new EventService();
        Event e = cs.findEvent(id);
        System.out.println(e.getAge());
        System.out.println(e.getAvailablePlaces());

    }
     

    public void listEvents() {
        EventService es = new EventService();
        System.out.println("Liste des Evènements");
        ObservableList<Event> events = es.findAll();
        for (Event e : events) {
            System.out.println(e);
        }
    }

    private void showModal(Event ev) {
        FlowPane modal = null;
        try {
            modal = FXMLLoader.load(getClass().getResource("/GUI/reservation.fxml"));
            modal.setPrefHeight(Main.scene.getHeight());
            modal.setPrefWidth(Main.scene.getWidth());
            Button close = (Button) modal.lookup("#close");
            FlowPane finalModal = modal;
            close.setOnAction(e -> {
                Main.sp.getChildren().remove(finalModal);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        Main.sp.getChildren().add(modal);
    }

     public void addEvent() {

        try {
            VBox content = null;
            EventService es = new EventService();

            content = FXMLLoader.load(getClass().getResource("/GUI/admin/ajoutEvent.fxml"));
            content.setPrefWidth(AdminController.container.getWidth());
            content.setPrefHeight(AdminController.container.getHeight());
            TextField type = (TextField) content.lookup("#type");
            TextField sujet = (TextField) content.lookup("#sujet");
            DatePicker dated = (DatePicker) content.lookup("#dated");
            DatePicker datef = (DatePicker) content.lookup("#datef");
            // TextField image= (TextField)center.lookup("#subject");
            TextField location = (TextField) content.lookup("#lieu");
            TextField participants = (TextField) content.lookup("#participants");
            TextField dispo = (TextField) content.lookup("#dispo");
            TextField age = (TextField) content.lookup("#age");

            Button save = (Button) content.lookup("#save");
            Button icon = (Button) content.lookup("#icon");
            FileChooser fc = new FileChooser();
            fc.setTitle("Choisir une icone");
            FileChooser.ExtensionFilter fe = new FileChooser.ExtensionFilter("Image files", "*.png", ".jpg");
            fc.setSelectedExtensionFilter(fe);
            
            icon.setOnAction(e -> image = fc.showOpenDialog(Main.window));
            save.setOnAction(e -> {

                //Photo imageFile = new Photo(image);
                //Event es = new Event(sujet.getText(), type.getText(), Date.from(dated.getValue().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()), Date.from(datef.getValue().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()), location.getText(), Integer.parseInt(participants.getText()), Integer.parseInt(dispo.getText()), imageFile, Integer.parseInt(age.getText()));
                try {
                    new EventService().addEvent(new Event(sujet.getText(), type.getText(), Date.from(dated.getValue().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()), Date.from(datef.getValue().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()), location.getText(), Integer.parseInt(participants.getText()), Integer.parseInt(dispo.getText()), null, Integer.parseInt(age.getText())));
                } catch (SQLException ex) {
                    Logger.getLogger(EventController.class.getName()).log(Level.SEVERE, null, ex);
                }
                init();

            });

            AdminController.container.getChildren().clear();
            AdminController.container.getChildren().add(content);
        } catch (IOException ex) {
            Logger.getLogger(EventController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
     
    public void updateEvent(Integer id) {
        VBox content = null;
        try {
            content = FXMLLoader.load(getClass().getResource("/GUI/admin/ajoutEvent.fxml"));
            Event e = new EventService().findEvent(18);
//            Label headTitle = (Label) content.lookup("#headTitle");
//            headTitle.setText("Modifier un babysitter");
            content.setPrefWidth(AdminController.container.getWidth());
            content.setPrefHeight(AdminController.container.getHeight());
            TextField type = (TextField) content.lookup("#type");
            type.setText(e.getType());
            TextField sujet = (TextField) content.lookup("#sujet");
            sujet.setText(e.getSubject());
            /*DatePicker dated = (DatePicker) content.lookup("#dated");
            dated.setValue(e.getStartDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
            DatePicker datef = (DatePicker) content.lookup("#datef");
            datef.setValue(e.getStartDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());*/
            // TextField image= (TextField)center.lookup("#subject");

            TextField lieu = (TextField) content.lookup("#lieu");
            lieu.setText(e.getLocation());
            TextField participants = (TextField) content.lookup("#participants");
            participants.setText(Integer.toString(e.getNbrParticipants()));
            
            TextField dispo = (TextField) content.lookup("#dispo");
            dispo.setText(Integer.toString(e.getAvailablePlaces()));
            
            TextField age = (TextField) content.lookup("#age");
            age.setText(e.getAge()+"");
            
            
           

            Button icon = (Button) content.lookup("#icon");
            FileChooser fc = new FileChooser();
            fc.setTitle("Choisir une icone");
            FileChooser.ExtensionFilter fe = new FileChooser.ExtensionFilter("Image files", "*.png", ".jpg");
            fc.setSelectedExtensionFilter(fe);
            icon.setOnAction(i -> image = fc.showOpenDialog(Main.window));

            Button save = (Button) content.lookup("#save");

            save.setOnAction((ActionEvent i) -> {
                
                if (image != null) {
                       Photo imageFile = new Photo(image);
                       e.setPhoto(imageFile);
                } else {
                    e.setPhoto(null);
                   }
                e.setId(24);
                    e.setSubject(sujet.getText());
                    e.setType(type.getText());
                    e.setStartDate(e.getStartDate());
                    e.setEndDate(e.getEndDate());
                    e.setLocation(lieu.getText());
                    e.setNbrParticipants(Integer.parseInt(participants.getText()));
                    e.setAvailablePlaces(Integer.parseInt(dispo.getText()));
                    e.setAge(Integer.parseInt(age.getText()));
                    System.err.println(e.toString());
                    
                    
                    new EventService().updateEvent(e);
                    
                    init();
                
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        AdminController.container.getChildren().clear();
        AdminController.container.getChildren().add(content);
    }
     
}

