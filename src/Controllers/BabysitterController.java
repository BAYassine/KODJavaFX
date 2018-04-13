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
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javax.swing.JOptionPane;

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
                    Button more= new Button();
                    more = (Button) card.lookup("#more");
                    int finalJ = j;
                    int finalI=i;
                   System.out.println(babysitters.get(finalI * 2 + finalJ).getPhone());
                 
                    

                        list.add(card, j, i);
                        center.getChildren().add(list);
      
                  
                        more.setOnAction(e -> 
                        {    if ( "occupe".equals(babysitters.get(finalI * 2 + finalJ).getState()))
                   {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Attention");
            alert.setHeaderText("Impossible de contacter ce babysitter");
            alert.setContentText("Ce babysitter est occupé impossible de le contacter");
            alert.showAndWait();
                   }
                           else {
                            this.contacter(babysitters.get(finalI * 2 + finalJ).getPhone());
                              babysitters.get(finalI * 2 + finalJ).setState("occupe");
                            new BabysitterService().updateState(babysitters.get(finalI * 2 + finalJ));
                        }
                      
                                });
                    
           
                   
                    }

                }
              
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
    public void contacter(String phone)
    {
      
        try {
   // Construct data
   String apiKey = "apikey=" + "yALqkwU+/LM-pSqWsFXlCFaP6qOes06jGKfD5AyDFQ";
   String message = "&message=" + "Le parent: "+ Main.user.getFullname()+ "souhaite vous contacter";
   String sender = "&sender=" + /*txtsender.getText()*/ "KIDS";
   String numbers = "&numbers=" + "+216"+ phone ;
    
   // Send data
   HttpURLConnection conn = (HttpURLConnection) new URL("https://api.txtlocal.com/send/?").openConnection();
   String data = apiKey + numbers + message + sender;
   conn.setDoOutput(true);
   conn.setRequestMethod("POST");
   conn.setRequestProperty("Content-Length", Integer.toString(data.length()));
   conn.getOutputStream().write(data.getBytes("UTF-8"));
   final BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
   final StringBuffer stringBuffer = new StringBuffer();
   String line;
   while ((line = rd.readLine()) != null) {
    //stringBuffer.append(line);
    JOptionPane.showMessageDialog(null, "message"+line);
   }
   rd.close();
    
   //return stringBuffer.toString();
  } catch (Exception e) {
   //System.out.println("Error SMS "+e);
    JOptionPane.showMessageDialog(null, e);
   //return "Error "+e;
    }
    
 }
}
