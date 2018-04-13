/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI.admin;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ResourceBundle;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javax.imageio.ImageIO;

/**
 * FXML Controller class
 *
 * @author Rami
 */
public class AjoutEventController implements Initializable {
    @FXML
    private Label ajout;
    @FXML
    private VBox panelBody;
    @FXML
    private TextField type;
    @FXML
    private TextField sujet;
    @FXML
    private DatePicker dated;
    @FXML
    private DatePicker datef;
    @FXML
    private Label location;
    @FXML
    private TextField lieu;
    @FXML
    private Button icon;
    @FXML
    private TextField participants;
    @FXML
    private TextField dispo;
    @FXML
    private TextField age;
    @FXML
    private Button save;
    private String url_image;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void AjouterEvent(ActionEvent event) {
           final FileChooser fileChooser = new FileChooser();
        configureFileChooser(fileChooser);
        File file = fileChooser.showOpenDialog(panelBody.getScene().getWindow());
        if (file != null) {
            openFile(file);
        }
    }

    @FXML
    private void ajouterimage(MouseEvent event) {
    }
    
    
      private void openFile(File file) {
        FileInputStream input;
        try {
            File dest = new File("C:/wamp64/www/KOD/KODproject/web/uploads/" + file.getName());
            Files.copy(file.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
            url_image = dest.toPath().toString();
            System.out.println("Image enregistrée avec succés");
            System.out.println(url_image);
            input = new FileInputStream(url_image);
            Image img_produit = SwingFXUtils.toFXImage(ImageIO.read(input), null);
            
        } catch (IOException ex) {
            System.err.println("Erreur d'enregistrement d'image");
        }
    }

    private static void configureFileChooser(final FileChooser fileChooser) {
        fileChooser.setTitle("Choisir une image");
        fileChooser.setInitialDirectory(
                new File("C:\\Users\\RAMI\\Desktop")
        );
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("JPEG", "*.jpeg"),
                new FileChooser.ExtensionFilter("PNG", "*.png")
        );
    }

    
}
