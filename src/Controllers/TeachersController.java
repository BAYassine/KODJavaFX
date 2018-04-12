package Controllers;

import Core.Main;
import Entities.Child;
import Entities.Photo;
import Services.ChildService;
import Services.PhotoService;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class TeachersController {
//    public void init(){
//
//        VBox center = new VBox();
//        ChildService cs = new ChildService();
//        ObservableList<Child> mykids = cs.findMykids(Main.user.getId());
//        try {
//            center = FXMLLoader.load(getClass().getResource("/GUI/teachers.fxml"));
//            center.setMinWidth(Main.screen.getWidth());
//            center.setMinHeight(Main.screen.getHeight());
//            GridPane list = new GridPane();
//            PhotoService is = new PhotoService();
//            list.setHgap(30);
//            list.setVgap(30);
//            list.setPadding(new Insets(10));
//            for (int i = 0; i < mykids.size(); i++) {
//                for (int j = i; j < 2 && j+i*2 < mykids.size(); j++) {
//                    card = FXMLLoader.load(getClass().getResource("/GUI/taecher-card.fxml"));
//                    image = (ImageView) card.getChildren().get(0);
//                    Photo img = is.findImage(mykids.get(j+i*2).getPhotoId());
//                    image.setImage(new Image(img.getWebPath()));
//                    name = (Label) right.getChildren().get(0);
//                    name.setText(mykids.get(j+i*2).getName());
//                }
//            }
//            center.getChildren().add(list);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        ScrollPane sp = new ScrollPane();
//        sp.setContent(center);
//        Main.pane.setCenter(sp);
//
//    }
}
