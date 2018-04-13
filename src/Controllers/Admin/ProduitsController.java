package Controllers.Admin;

import Controllers.AdminController;
import Core.Main;
import Dependencies.pherialize.MixedArray;
import Dependencies.pherialize.Pherialize;
import Entities.Category;
import Entities.Game;
import Entities.Game;
import Entities.Photo;
import Entities.Product;
import Services.CategoryService;
import Services.GameService;
import Services.GameService;
import Services.ProductServices;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Pos;

public class ProduitsController {

    private File image = null;
    public void init() {
        VBox content = null;
        try {
            content = FXMLLoader.load(getClass().getResource("/GUI/admin/produits-list.fxml"));
            VBox body = (VBox) content.lookup("#panelBody");
            content.setPrefWidth(AdminController.container.getWidth());
            content.setPrefHeight(AdminController.container.getHeight());
            Button addBtn = (Button) content.lookup("#addBtn");
            addBtn.setOnAction(e -> addProduit());
            
            TableColumn<Game,   Integer> idColumn = new TableColumn<>("ID");
            idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
            idColumn.setMaxWidth(50);
            idColumn.setMinWidth(50);
            idColumn.setStyle("-fx-alignment : center");

            TableColumn<Product, Photo> iconColumn = new TableColumn<>("Photo");
            iconColumn.setCellValueFactory(new PropertyValueFactory<>("img"));
            iconColumn.setCellFactory(e -> new TableCell<Product, Photo>(){
                @Override
                public void updateItem(Photo item, boolean empty){
                    super.updateItem(item, empty);
                    if(!empty && item!= null){
                        ImageView iv = new ImageView(new Image(item.getWebPath()));
                        iv.setFitHeight(150);
                        iv.setPreserveRatio(true);
                        setText("");
                        setGraphic(iv);
                    }
                }
            }); 

            TableColumn<Product, String> nameColumn = new TableColumn<>("Nom produit");
            nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

            TableColumn<Product, String> descriptionColumn = new TableColumn<>(" Description");
            descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

            TableColumn<Product, Integer> priceColumn = new TableColumn<>("Price");
            priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

            TableColumn<Product, String> ageColumn = new TableColumn<>("age");
            ageColumn.setCellValueFactory(new PropertyValueFactory<>("age"));
          
            TableColumn<Product, String> genderColumn = new TableColumn<>("gender");
            genderColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));
             

            TableColumn<Product, Integer> tvaColumn = new TableColumn<>("tva");
            tvaColumn.setCellValueFactory(new PropertyValueFactory<>("tva"));
          
           TableColumn<Product, Integer> availableColumn = new TableColumn<>("available");
           availableColumn.setCellValueFactory(new PropertyValueFactory<>("available"));
            
           TableColumn<Product, Integer> quantColumn = new TableColumn<>("quantite");
           quantColumn.setCellValueFactory(new PropertyValueFactory<>("quantite"));
           TableColumn<Product, Integer> actionsColumn = new TableColumn<>("Actions");
           actionsColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
           actionsColumn.setCellFactory(e -> new TableCell<Product, Integer>() {
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
                        new ProductServices().deleteObject(item, "product");
                        init();
                        }); 
                        Button updateBtn = new Button("Update");
                        updateBtn.getStyleClass().addAll("btn", "btn-success");
                        updateBtn.setOnAction(e -> updateProduct(item));
                        actions.getChildren().addAll(updateBtn,deleteBtn);
                        setGraphic(actions);
                    }
                }
            });
            TableView tableView = (TableView) body.lookup("#table");
            tableView.setItems(new ProductServices().showProducts());

            tableView.getColumns().addAll(idColumn,nameColumn,iconColumn, descriptionColumn , ageColumn, priceColumn,tvaColumn,availableColumn , genderColumn,quantColumn,actionsColumn );
        } catch (IOException e) {
            e.printStackTrace();
        }
        AdminController.container.getChildren().clear();
        AdminController.container.getChildren().add(content);
    }
    

    private void updateProduct(Integer id) {
       // AdminController.treeView.getSelectionModel().select(5);
        VBox content = null;
        try {
            content = FXMLLoader.load(getClass().getResource("/GUI/admin/form-product.fxml"));
            Product p= new ProductServices().search(id);
//            Label headTitle = (Label) content.lookup("#headTitle");
//            headTitle.setText("Modifier un babysitter");
            content.setPrefWidth(AdminController.container.getWidth());
            content.setPrefHeight(AdminController.container.getHeight());
            TextField name = (TextField) content.lookup("#name");
            name.setText(p.getName());
            TextArea desc= (TextArea) content.lookup("#desc");
            desc.setText(p.getDescription());
             TextField price= (TextField) content.lookup("#price");
            price.setText(Integer.toString(p.getPrice()));
            TextField tva = (TextField) content.lookup("#tva");
            tva.setText(Integer.toString(p.getTva()));
            ComboBox age = (ComboBox) content.lookup("#age");
            age.getItems().addAll(1, 2, 3);
            age.getSelectionModel().getSelectedIndex();
            Button icon = (Button) content.lookup("#icon");
            FileChooser fc = new FileChooser();
            fc.setTitle("Choisir une icone");
            FileChooser.ExtensionFilter fe = new FileChooser.ExtensionFilter("Image files", "*.png", ".jpg");
            fc.setSelectedExtensionFilter(fe);
            icon.setOnAction(e -> image = fc.showOpenDialog(Main.window));
            RadioButton boy = (RadioButton) content.lookup("#boy");
            RadioButton girl = (RadioButton) content.lookup("#girl");
            RadioButton both = (RadioButton) content.lookup("#both");
            ToggleGroup genderGrp = new ToggleGroup();
            genderGrp.getToggles().addAll(both, boy, girl);
               int selectedGender = 2;
            if (genderGrp.getUserData() == "Fille")
                selectedGender = 0;
            else if (genderGrp.getUserData() == "Garçon")
                selectedGender = 1;
            int finalGender = selectedGender;
             ComboBox<Category> category = (ComboBox<Category>) content.lookup("#category");
             category.setItems(new CategoryService().findCategory("Produits"));
              TextField qnt= (TextField) content.lookup("#qnt");
              qnt.setText(Integer.toString(p.getQuantite()));
             Button save = (Button) content.lookup("#save");

            save.setOnAction(e -> {
                
                   if (image != null)
                   {
                       Photo imageFile = new Photo(image);
                       p.setImg(imageFile);
                   }
                   else 
                        p.setName(name.getText()); 
                        p.setDescription(desc.getText());
                        p.setPrice( Integer.parseInt(price.getText())); 
                        p.setAge(age.getSelectionModel().getSelectedIndex());  
                        p.setGender(finalGender);
                        p.setCategory(category.getSelectionModel().getSelectedItem()); 
                        p.setQuantite(Integer.parseInt(tva.getText()));  
                        p.setQuantite(Integer.parseInt(qnt.getText()));  
                        p.setImg(null);
                        new ProductServices().updateProduct(p);
                    
                          init();
                
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        AdminController.container.getChildren().clear();
        AdminController.container.getChildren().add(content);
    }

    public void addProduit(){
        AdminController.treeView.getSelectionModel().select(5);
        VBox content = null;
        try {
            content = FXMLLoader.load(getClass().getResource("/GUI/admin/form-product.fxml"));
            content.setPrefWidth(AdminController.container.getWidth());
            content.setPrefHeight(AdminController.container.getHeight());
            TextField name = (TextField) content.lookup("#name");
            TextField price = (TextField) content.lookup("#price");
            TextField qnt= (TextField) content.lookup("#qnt");
            TextField tva = (TextField) content.lookup("#tva");
            TextArea descreption = (TextArea) content.lookup("#desc");
            ComboBox age = (ComboBox) content.lookup("#age");
            age.getItems().addAll(1, 2, 3);
            Button icon = (Button) content.lookup("#icon");
            FileChooser fc = new FileChooser();
            fc.setTitle("Choisir une icone");
            FileChooser.ExtensionFilter fe = new FileChooser.ExtensionFilter("Image files", "*.png", ".jpg");
            fc.setSelectedExtensionFilter(fe);
            icon.setOnAction(e -> image = fc.showOpenDialog(Main.window));
            ComboBox<Category> category = (ComboBox<Category>) content.lookup("#category");
            category.setItems(new CategoryService().findCategory("Produits"));
            RadioButton boy = (RadioButton) content.lookup("#boy");
            RadioButton girl = (RadioButton) content.lookup("#girl");
            RadioButton both = (RadioButton) content.lookup("#both");
            ToggleGroup genderGrp = new ToggleGroup();
            genderGrp.getToggles().addAll(both, boy, girl);
            Button save = (Button) content.lookup("#save");

            int selectedGender = 2;
            if (genderGrp.getUserData() == "Fille")
                selectedGender = 0;
            else if (genderGrp.getUserData() == "Garçon")
                selectedGender = 1;
            int finalGender = selectedGender;
            save.setOnAction(e -> {
                Photo imageFile = new Photo(image);
                Product p = new Product(
                        name.getText(), 
                        descreption.getText(), 
                        Integer.parseInt(price.getText()), 
                        age.getSelectionModel().getSelectedIndex(), 
                        category.getSelectionModel().getSelectedItem(), 
                        finalGender, 
                        Integer.parseInt(tva.getText()), 
                        imageFile, 
                        Integer.parseInt(qnt.getText()),
                        true
                    );
                new ProductServices().addProduct(p);
                init();
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        AdminController.container.getChildren().clear();
        AdminController.container.getChildren().add(content);
    }

}

