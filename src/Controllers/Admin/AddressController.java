package Controllers.Admin;

import Controllers.AdminController;
import Core.Main;
import Dependencies.pherialize.MixedArray;
import Dependencies.pherialize.Pherialize;
import Entities.Address;
import Entities.Babysitter;
import Entities.Category;
import Entities.Game;
import Entities.Photo;
import Services.AddressService;
import Services.BabysitterService;
import Services.CategoryService;
import Services.GameService;
import com.lynden.gmapsfx.GoogleMapView;
import com.lynden.gmapsfx.javascript.object.GoogleMap;
import com.lynden.gmapsfx.javascript.object.LatLong;
import com.lynden.gmapsfx.javascript.object.MapOptions;
import com.lynden.gmapsfx.javascript.object.MapTypeIdEnum;
import com.lynden.gmapsfx.javascript.object.Marker;
import com.lynden.gmapsfx.javascript.object.MarkerOptions;
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


public class AddressController {

    private File image = null;

    public void init() {
        VBox content = null;

        try {
            content = FXMLLoader.load(getClass().getResource("/GUI/admin/address-list.fxml"));
            VBox body = (VBox) content.lookup("#panelBody");
            content.setPrefWidth(AdminController.container.getWidth());
            content.setPrefHeight(AdminController.container.getHeight());
            Button addBtn = (Button) content.lookup("#addBtn");
            addBtn.setOnAction(e -> addAddress());

            TableColumn<Babysitter, Integer> idColumn = new TableColumn<>("ID");
            idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
            idColumn.setMaxWidth(50);
            idColumn.setMinWidth(50);
            idColumn.setStyle("-fx-alignment : center");

            TableColumn<Babysitter,Category> CategoryColumn = new TableColumn<>("CATEGORIE");
            CategoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));

            TableColumn<Babysitter, String> descriptionColumn = new TableColumn<>("DESCRIPTION");
            descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

            TableColumn<Babysitter, String> addressColumn = new TableColumn<>("ADRESSE");
            addressColumn.setCellValueFactory(new PropertyValueFactory<>("location"));

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

            TableColumn<Babysitter, String> nameColumn = new TableColumn<>("NOM");
            nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
            
            
            
            TableColumn<Babysitter, String> phoneColumn = new TableColumn<>("TELEPHONE");
            phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
            
             TableColumn<Babysitter, String> latColumn = new TableColumn<>("LATITUDE");
              latColumn.setCellValueFactory(new PropertyValueFactory<>("lat"));
            
            
             TableColumn<Babysitter, String> lngColumn = new TableColumn<>("LONGITUDE");
            lngColumn.setCellValueFactory(new PropertyValueFactory<>("lng"));
            
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
                            new AddressService().deleteObject(item, "address");
                            init();
                        });
                        Button updateBtn = new Button("Update");
                        updateBtn.getStyleClass().addAll("btn", "btn-success");
                        updateBtn.setOnAction(e -> updateAddress(item));
                        actions.getChildren().addAll(updateBtn, deleteBtn);
                        setGraphic(actions);
                    }
                }
            });
            TableView tableView = (TableView) body.lookup("#table");
            try {
                tableView.setItems(new AddressService().findAll());
            } catch (SQLException ex) {
                Logger.getLogger(AddressController.class.getName()).log(Level.SEVERE, null, ex);
            }
            tableView.getColumns().addAll(idColumn, nameColumn,descriptionColumn, phoneColumn, CategoryColumn, addressColumn, photoColumn, actionsColumn);
        } catch (IOException e) {
            e.printStackTrace();
        }
        AdminController.container.getChildren().clear();
        AdminController.container.getChildren().add(content);
    }

    public void addAddress() {

        try {
           // AdminController.treeView.getSelectionModel().select(3);
            VBox content = null;
            AddressService bs = new AddressService();
            CategoryService cv= new CategoryService();

            content = FXMLLoader.load(getClass().getResource("/GUI/admin/form-address.fxml"));
            content.setPrefWidth(AdminController.container.getWidth());
            content.setPrefHeight(AdminController.container.getHeight());
            TextField name = (TextField) content.lookup("#name");
            TextField description = (TextField) content.lookup("#description");
            TextField location = (TextField) content.lookup("#address");
            TextField phone = (TextField) content.lookup("#phone");
            
            TextField lat = (TextField) content.lookup("#lat");
            TextField lng = (TextField) content.lookup("#lng");
            
            ComboBox categories = (ComboBox) content.lookup("#category");
            categories.setVisibleRowCount(25);
             ObservableList <Category> categories_dispo= cv.findCategory("Adresse");
                
             categories.setItems(categories_dispo);
            //afficher la liste des catégories dans la combobox

            Button save = (Button) content.lookup("#save");
            Button icon = (Button) content.lookup("#icon");
            FileChooser fc = new FileChooser();
            fc.setTitle("Choisir une icone");
            FileChooser.ExtensionFilter fe = new FileChooser.ExtensionFilter("Image files", "*.png", ".jpg");
            fc.setSelectedExtensionFilter(fe);
            icon.setOnAction(e -> image = fc.showOpenDialog(Main.window));
            save.setOnAction(e -> {

                try {
                    Photo imageFile = new Photo(image);
                    Address u= new Address(description.getText(),name.getText(),phone.getText(),location.getText(),cv.findByName(categories.getValue().toString()),Float.parseFloat(lat.getText()),Float.parseFloat(lng.getText()),imageFile);
                    
                    new AddressService().addAddress(u);
                    init();
                } catch (SQLException ex) {
                    Logger.getLogger(AddressController.class.getName()).log(Level.SEVERE, null, ex);
                }

            });

            AdminController.container.getChildren().clear();
            AdminController.container.getChildren().add(content);
        } catch (IOException ex) {
            Logger.getLogger(AddressController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
   private void updateAddress(Integer id) {
        AdminController.treeView.getSelectionModel().select(5);
        VBox content = null;
        try {
            content = FXMLLoader.load(getClass().getResource("/GUI/admin/form-address.fxml"));
            Address g = new AddressService().findAddress(id);
//            Label headTitle = (Label) content.lookup("#headTitle");
//            headTitle.setText("Modifier un babysitter");
            content.setPrefWidth(AdminController.container.getWidth());
            content.setPrefHeight(AdminController.container.getHeight());
           CategoryService cv= new CategoryService();
            
            
            TextField name = (TextField) content.lookup("#name");
            name.setText(g.getName());
            System.out.println(g.getName());
            TextField description = (TextField) content.lookup("#description");
            description.setText(g.getDescription());
            
            TextField location = (TextField) content.lookup("#address");
            location.setText(g.getLocation());
            
            TextField phone = (TextField) content.lookup("#phone");
             phone.setText(g.getPhone());
             
             TextField lat = (TextField) content.lookup("#lat");
             lat.setText(g.getLat()+"");
             
              TextField lng = (TextField) content.lookup("#lng");
             lng.setText(g.getLng()+"");
            
            // TextField image= (TextField)center.lookup("#subject");

            
             ComboBox categories = (ComboBox) content.lookup("#category");
            categories.setVisibleRowCount(25);
             ObservableList <Category> categories_dispo= cv.findCategory("Adresse");
                
             categories.setItems(categories_dispo);
             categories.setValue(g.getCategory());
          

            Button icon = (Button) content.lookup("#icon");
            FileChooser fc = new FileChooser();
            fc.setTitle("Choisir une icone");
            FileChooser.ExtensionFilter fe = new FileChooser.ExtensionFilter("Image files", "*.png", ".jpg");
            fc.setSelectedExtensionFilter(fe);
            icon.setOnAction(e -> image = fc.showOpenDialog(Main.window));

            Button save = (Button) content.lookup("#save");

            save.setOnAction(e -> {
                
                try {
                    if (image != null)
                    {
                        Photo imageFile = new Photo(image);
                        g.setPhoto(imageFile);
                    }
                    else g.setPhoto(null);
                    g.setCategory(cv.findByName(categories.getValue().toString()));
                    g.setDescription(description.getText());
                    g.setLat(Float.parseFloat(lat.getText()));
                    g.setLng(Float.parseFloat(lng.getText()));
                    g.setName(name.getText());
                    g.setLocation(location.getText());
                    g.setPhone(phone.getText());
                    
                    new AddressService().updateAddress(g);
                    
                    init();
                } catch (SQLException ex) {
                    Logger.getLogger(AddressController.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        AdminController.container.getChildren().clear();
        AdminController.container.getChildren().add(content);
    }

}
