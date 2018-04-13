/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import Core.Main;
import Entities.Child;
import Entities.Event;
import Entities.Photo;
import Entities.Video;
import Services.ChildService;
import Services.EventService;
import Services.PhotoService;
import java.io.IOException;
import java.util.Date;
import java.util.Random;

import Services.ReservationService;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import sun.nio.cs.ext.MacArabic;

/**
 *
 * @author Rami
 */
public class EventController {
        
    public void init(){
        
        ScrollPane center = null;
        ObservableList<Event> events = new EventService().findAll();
        try {
            center = FXMLLoader.load(getClass().getResource("/GUI/list-events.fxml"));
            center.setPrefHeight(Main.screen.getHeight()-250);
            GridPane list = new GridPane();
            list.setHgap(30);
            list.setVgap(30);
            list.setPadding(new Insets(50));
            PhotoService is = new PhotoService();
            for (int i = 0; i < events.size(); i++) {
                for (int j = 0; j < 2 && j+i*2 < events.size(); j++) {
                    Event ev = events.get(j + i*2); 
                    HBox card = FXMLLoader.load(getClass().getResource("/GUI/event.fxml"));
                    ImageView image = (ImageView) card.lookup("#image");
                    Photo img = events.get(j+i*2).getPhoto();
                    
                    image.setImage(new Image(img.getWebPath()));
                    //ImagePattern ip = new ImagePattern( new Image(events.get( j+i*2 ).getPhoto().getWebPath()));
                    
                    
                    Label type = (Label) card.lookup("#type");
                    type.setText(events.get(j+i*2).getType());
                    Label address = (Label) (Label) card.lookup("#address");
                    address.setText(events.get(j+i*2).getLocation());
                    Label time = (Label) card.lookup("#time");
                    time.setText("Date : " + events.get(j + i*2).getStartDate());
                    Label partis = (Label) card.lookup("#partis");
                    partis.setText("Nombre de places disponibles : " +  events.get(j + i*2).getAvailablePlaces());
                    Button details = (Button) card.lookup("#details");
                    Button reserver = (Button) card.lookup("#reserver");
                    Button remove = (Button) card.lookup("#remove");
                    int finalJ = j;
                    //more.setOnAction(e -> this.showActivity(mykids.get(finalJ)));
                    reserver.setOnAction(e -> showModal(ev));
                    Random random = new Random();
                    card.getStyleClass().add("bg-color-" + (random.nextInt(5)+1));
                    list.add(card, j, i);
                }
                
                
//                
//                Button deleteBtn = new Button("Delete");
//                        deleteBtn.getStyleClass().addAll("btn", "btn-danger");
//                        deleteBtn.setOnAction(e -> {
//                            new EventService().deleteObject(item, "Event");
//                            init();
//                        });
//                     
//                        actions.getChildren().addAll(deleteBtn);
//                     setGraphic(actions);
                     
                     
            }
            VBox listV = (VBox) center.getContent();
            listV.setPrefWidth(Main.screen.getWidth());
            listV.getChildren().add(list);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Main.pane.setCenter(center);
    }
    
    
    public void deleteEvent(int id){
        EventService cs = new EventService();
        cs.deleteObject(id,"Event");
    }

    
     public void showEvent(int id){
        EventService cs = new EventService();
        Event e = cs.findEvent(id);
        System.out.println(e.getAge());
        System.out.println(e.getAvailablePlaces());

    }
     


     public void listEvents(){
        EventService es = new EventService();
        System.out.println("Liste des Evènements");
        ObservableList<Event> events = es.findAll();
        for (Event e : events){
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
            Button resBtn = (Button) modal.lookup("#resBtn");
            TextField parts = (TextField) modal.lookup("#parts");
            resBtn.setOnAction(e -> {
                new ReservationService().addReservation(ev.getId(), Main.user.getId(), Integer.parseInt(parts.getText()));
                Main.sp.getChildren().remove(finalModal);
            });
            close.setOnAction(e -> {
                Main.sp.getChildren().remove(finalModal);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        Main.sp.getChildren().add(modal);
    }

//    private PhotoService newPhotoService() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }

    
}

