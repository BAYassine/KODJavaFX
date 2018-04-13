/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import Core.Main;
import Entities.Babysitter;
import Entities.Child;
import Entities.Photo;
import Services.BabysitterService;
import Services.ChildService;
import Services.PhotoService;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 *
 * @author Meriem
 */
public class BabysitterController {

    public void init() {
        try {
            VBox center = new VBox();
            BabysitterService cs = new BabysitterService();
            HBox card = new HBox();
            ImageView image = new ImageView();
            ObservableList<Babysitter> babysitters = cs.findAll();
            try {
                center = FXMLLoader.load(getClass().getResource("/GUI/ListBabysitters.fxml"));
                center.setMinWidth(Main.screen.getWidth());
                center.setMinHeight(Main.screen.getHeight());
                Label name;
                Label phone;
                Label price;
                Label address;

                GridPane list = new GridPane();
                PhotoService is = new PhotoService();
                list.setHgap(30);
                list.setVgap(30);
                list.setPadding(new Insets(10));
                for (int i = 0; i < babysitters.size(); i++) {
                    for (int j = i; j < 3 && j + i * 3 < babysitters.size(); j++) {

                        card = FXMLLoader.load(getClass().getResource("/GUI/Babysitter.fxml"));
                        image = (ImageView) card.getChildren().get(0);
                        Photo img = is.findImage(babysitters.get(j + i * 2).getPhoto().getId());
                        image.setImage(new Image(img.getWebPath()));
                        VBox right = (VBox) card.getChildren().get(1);
                        name = (Label) right.getChildren().get(0);
                        name.setText(babysitters.get(j + i * 2).getFirstName() + " " + babysitters.get(j + i * 2).getLastName());
                        phone = (Label) right.getChildren().get(1);
                        phone.setText(babysitters.get(j + i * 2).getPhone());
                        price = (Label) right.getChildren().get(2);

                        price.setText(Integer.toString(babysitters.get(j + i * 2).getPrice()));
                        address = (Label) right.getChildren().get(3);
                        address.setText(babysitters.get(i * 2 + j).getAddress());
//                    more = (Button) card.lookup("#more");
//                    int finalJ = j;
//                    more.setOnAction(e -> this.showMore(mykids.get(finalJ)));
//                    space = (Button) card.lookup("#space");
//                    space.setOnAction(e -> this.showSpace(mykids.get(finalJ)));

                        list.add(card, j, i);

                    }

                }
                center.getChildren().add(list);
            } catch (IOException e) {
                e.printStackTrace();
            }
            ScrollPane sp = new ScrollPane();
            sp.setContent(center);
            Main.pane.setCenter(sp);
        } catch (SQLException ex) {
            Logger.getLogger(BabysitterController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
 }
