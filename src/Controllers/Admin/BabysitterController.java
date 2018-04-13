package Controllers.Admin;

import Controllers.AdminController;
import Core.Main;
import Dependencies.pherialize.MixedArray;
import Dependencies.pherialize.Pherialize;
import Entities.Babysitter;
import Entities.Category;
import Entities.Game;
import Entities.Photo;
import Services.BabysitterService;
import Services.GameService;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import java.io.File;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

public class BabysitterController {

    private File image = null;

    public void init() {
        VBox content = null;

        try {
            content = FXMLLoader.load(getClass().getResource("/GUI/admin/babysitter-list.fxml"));
            VBox body = (VBox) content.lookup("#panelBody");
            content.setPrefWidth(AdminController.container.getWidth());
            content.setPrefHeight(AdminController.container.getHeight());
            Button addBtn = (Button) content.lookup("#addBtn");
            addBtn.setOnAction(e -> addBabysitter());

            TableColumn<Babysitter, Integer> idColumn = new TableColumn<>("ID");
            idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
            idColumn.setMaxWidth(50);
            idColumn.setMinWidth(50);
            idColumn.setStyle("-fx-alignment : center");

            TableColumn<Babysitter, String> lastnameColumn = new TableColumn<>("NOM");
            lastnameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));

            TableColumn<Babysitter, String> firstnameColumn = new TableColumn<>("PRENOM");
            firstnameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));

            TableColumn<Babysitter, String> phoneColumn = new TableColumn<>("TELEPHONE");
            phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));

            TableColumn<Babysitter, String> addressColumn = new TableColumn<>("REGION");
            addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));

            TableColumn<Babysitter, Integer> priceColumn = new TableColumn<>("SALAIRE");
            priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

            TableColumn<Babysitter, Photo> photoColumn = new TableColumn<>("IMAGE");
            photoColumn.setCellValueFactory(new PropertyValueFactory<>("photo"));
            photoColumn.setCellFactory(e -> new TableCell<Babysitter, Photo>() {
                @Override
                public void updateItem(Photo item, boolean empty) {
                    super.updateItem(item, empty);
                    if (!empty && item != null) {
                        ImageView iv = new ImageView(new Image(item.getWebPath()));
                        iv.setFitHeight(150);
                        iv.setPreserveRatio(true);
                        setText("");
                        setGraphic(iv);
                    }
                }
            });

            TableColumn<Babysitter, String> stateColumn = new TableColumn<>("ETAT");
            stateColumn.setCellValueFactory(new PropertyValueFactory<>("state"));

            TableColumn<Babysitter, Integer> actionsColumn = new TableColumn<>("Actions");
            actionsColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
            actionsColumn.setCellFactory(e -> new TableCell<Babysitter, Integer>() {
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
                            new BabysitterService().deleteObject(item, "babysitter");
                                Alert alert = new Alert(Alert.AlertType.NONE);
            alert.setTitle("Succès");
            //alert.setHeaderText("Impossible de supprimer cette réclamation");
            alert.setContentText("Le babysitter a bien été supprimé");
            alert.showAndWait();
                            init();
                        });
                        Button updateBtn = new Button("Update");
                        updateBtn.getStyleClass().addAll("btn", "btn-success");
                        updateBtn.setOnAction(e -> updateBabysitter(item));
                        actions.getChildren().addAll(updateBtn, deleteBtn);
                        setGraphic(actions);
                    }
                }
            });
            TableView tableView = (TableView) body.lookup("#table");
            try {
                tableView.setItems(new BabysitterService().findAll());
            } catch (SQLException ex) {
                Logger.getLogger(BabysitterController.class.getName()).log(Level.SEVERE, null, ex);
            }
            tableView.getColumns().addAll(idColumn, lastnameColumn, firstnameColumn, phoneColumn, addressColumn, priceColumn, photoColumn, stateColumn, actionsColumn);
        } catch (IOException e) {
            e.printStackTrace();
        }
        AdminController.container.getChildren().clear();
        AdminController.container.getChildren().add(content);
    }

    public void addBabysitter() {

        try {
            AdminController.treeView.getSelectionModel().select(3);
            VBox content = null;
            BabysitterService bs = new BabysitterService();

            content = FXMLLoader.load(getClass().getResource("/GUI/admin/form-babysitter.fxml"));
            content.setPrefWidth(AdminController.container.getWidth());
            content.setPrefHeight(AdminController.container.getHeight());
            TextField prenom = (TextField) content.lookup("#firstname");
            TextField nom = (TextField) content.lookup("#lastname");
            TextField telephone = (TextField) content.lookup("#phone");
            // TextField image= (TextField)center.lookup("#subject");

            TextField salaire = (TextField) content.lookup("#sal");
            ComboBox address = (ComboBox) content.lookup("#address");
            address.setVisibleRowCount(25);

            address.getItems().addAll("Ariana", "Ben Arous", "Mahdia", "Gabes", "Sousse");

            ComboBox<String> etat = (ComboBox) content.lookup("#state");

            Button save = (Button) content.lookup("#save");
            Button icon = (Button) content.lookup("#icon");
            FileChooser fc = new FileChooser();
            fc.setTitle("Choisir une icone");
            FileChooser.ExtensionFilter fe = new FileChooser.ExtensionFilter("Image files", "*.png", ".jpg");
            fc.setSelectedExtensionFilter(fe);
            icon.setOnAction(e -> image = fc.showOpenDialog(Main.window));
            save.setOnAction(e -> {

                Photo imageFile = new Photo(image);
                Babysitter u = new Babysitter(prenom.getText(), nom.getText(),
                        address.getValue().toString(), Integer.parseInt(salaire.getText()), telephone.getText(), etat.getSelectionModel().getSelectedItem(), imageFile);

                new BabysitterService().addBabysitter(u);
                init();

            });

            AdminController.container.getChildren().clear();
            AdminController.container.getChildren().add(content);
        } catch (IOException ex) {
            Logger.getLogger(BabysitterController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void updateBabysitter(Integer id) {
        AdminController.treeView.getSelectionModel().select(5);
        VBox content = null;
        try {
            content = FXMLLoader.load(getClass().getResource("/GUI/admin/form-babysitter.fxml"));
            Babysitter g = new BabysitterService().findBabysitter(id);
//            Label headTitle = (Label) content.lookup("#headTitle");
//            headTitle.setText("Modifier un babysitter");
            content.setPrefWidth(AdminController.container.getWidth());
            content.setPrefHeight(AdminController.container.getHeight());
            TextField prenom = (TextField) content.lookup("#firstname");
            prenom.setText(g.getFirstName());
            TextField nom = (TextField) content.lookup("#lastname");
            nom.setText(g.getLastName());
            TextField telephone = (TextField) content.lookup("#phone");
            telephone.setText(g.getPhone());
            // TextField image= (TextField)center.lookup("#subject");

            TextField salaire = (TextField) content.lookup("#sal");
            salaire.setText(Integer.toString(g.getPrice()));
            ComboBox address = (ComboBox) content.lookup("#address");
            address.setVisibleRowCount(25);
            address.getItems().addAll("Ariana", "Ben Arous", "Mahdia", "Gabes", "Sousse");
            address.setValue(g.getAddress());
            ComboBox states = (ComboBox) content.lookup("#state");
            states.setVisibleRowCount(25);
            states.getItems().addAll("Occupée", "Libre");
            states.setValue(g.getState());

            Button icon = (Button) content.lookup("#icon");
            FileChooser fc = new FileChooser();
            fc.setTitle("Choisir une icone");
            FileChooser.ExtensionFilter fe = new FileChooser.ExtensionFilter("Image files", "*.png", ".jpg");
            fc.setSelectedExtensionFilter(fe);
            icon.setOnAction(e -> image = fc.showOpenDialog(Main.window));

            Button save = (Button) content.lookup("#save");

            save.setOnAction(e -> {
                
                   if (image != null)
                   {
                       Photo imageFile = new Photo(image);
                       g.setPhoto(imageFile);
                   }
                   else g.setPhoto(null);
                    g.setAddress(address.getValue().toString());
                    g.setFirstName(prenom.getText());
                    g.setLastName(nom.getText());
                    g.setPhone(telephone.getText());
                    g.setPrice(Integer.parseInt(salaire.getText()));
                    g.setState(states.getValue().toString());
                    
                    new BabysitterService().updateBabysitter(g);
                    
                    init();
                
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        AdminController.container.getChildren().clear();
        AdminController.container.getChildren().add(content);
    }
}
