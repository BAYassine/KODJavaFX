/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import Core.Main;
import Entities.Teacher;
import Entities.Photo;
import Services.TeachersService;
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
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

/**
 *
 * @author ali
 */
public class TeacherController {

    public void init() {
        VBox center = null;
        TeachersService cs = new TeachersService();
        ObservableList<Teacher> teachers = cs.findAll();
        try {
            center = FXMLLoader.load(getClass().getResource("/GUI/list-teachers.fxml"));
            center.setMinWidth(Main.screen.getWidth());

            GridPane list = new GridPane();
            PhotoService is = new PhotoService();
            list.setHgap(30);
            list.setVgap(30);
            list.setPadding(new Insets(100));
            for (int i = 0; i < teachers.size(); i++) {
                for (int j = 0; j < 4 && j + i * 4 < teachers.size(); j++) {
                    VBox teacher = FXMLLoader.load(getClass().getResource("/GUI/teacher.fxml"));
                    Teacher t = teachers.get(i*4 + j);
                    Circle image = (Circle) teacher.lookup("#image");
                    Label name = (Label) teacher.lookup("#name");
                    Label price = (Label) teacher.lookup("#price");
                    Button aproposBtn = (Button) teacher.lookup("#aproposBtn");
                    ImagePattern ip = new ImagePattern(new Image(teachers.get(i*4+j).getPhoto().getWebPath()));
                    name.setText(teachers.get(i*4+j).getName());
                    price.setText(teachers.get(i*4+j).getPrice()+"");
                    image.setFill(ip);
                    ImageView img = (ImageView) teacher.lookup("#imv");
                    aproposBtn.setOnAction(e -> detailsProf(t));
                    list.add(teacher, j, i);
                }
            }
            center.getChildren().add(list);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ScrollPane sp = new ScrollPane();
        sp.setPrefWidth(Main.scene.getWidth());
        sp.setContent(center);
        Main.pane.setCenter(sp);
    }
    private void detailsProf(Teacher teacher) {
        HBox content = null;
        try {
            content = FXMLLoader.load(getClass().getResource("/GUI/details-prof.fxml"));
            content.setMinWidth(Main.screen.getWidth());
            GridPane list = new GridPane();
            PhotoService is = new PhotoService();
            list.setHgap(30);
            list.setVgap(30);
            list.setPadding(new Insets(10));
            ImageView img = (ImageView) content.lookup("#img");
            Label name = (Label) content.lookup("#name");
            Label name1 = (Label) content.lookup("#name1");
            Label name2 = (Label) content.lookup("#name2");
            Label name3 = (Label) content.lookup("#name3");
            Label name4 = (Label) content.lookup("#name4");
            name.setText(teacher.getName());
            name1.setText(teacher.getDegree());
            name2.setText(teacher.getExperience());
            name3.setText(teacher.getHobbies());
            name4.setText(teacher.getSubject().getName());
   
        } catch (IOException ex) {
            Logger.getLogger(TeacherController.class.getName()).log(Level.SEVERE, null, ex);
        }
        Main.pane.setCenter(content);
    }

}
