package Controllers.Admin;

import Controllers.AdminController;
import Core.Main;
import Dependencies.pherialize.MixedArray;
import Dependencies.pherialize.Pherialize;
import Entities.Photo;
import Entities.Subject;
import Entities.Teacher;
import Services.SubjectService;
import Services.TeachersService;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import javafx.stage.FileChooser;
import java.io.File;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.controlsfx.control.PopOver;
import sun.plugin.dom.exception.NoModificationAllowedException;
import org.controlsfx.control.PopOver;

public class TeacherController {
    
     private File image = null;
    public void init() {
        VBox content = null;
        try {
            content = FXMLLoader.load(getClass().getResource("/GUI/admin/teacher-list.fxml"));
            VBox body = (VBox) content.lookup("#panelBody");
            content.setPrefWidth(AdminController.container.getWidth());
            content.setPrefHeight(AdminController.container.getHeight());
            Button addBtn = (Button) content.lookup("#addBtn");
            addBtn.setOnAction(e -> addprof());

            TableColumn<Teacher, Integer> idColumn = new TableColumn<>("ID");
            idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
            idColumn.setMaxWidth(50);
            idColumn.setMinWidth(50);
            idColumn.setStyle("-fx-alignment : center");

            TableColumn<Teacher, Photo> photoColumn = new TableColumn<>("Photo");
            photoColumn.setCellValueFactory(new PropertyValueFactory<>("photo"));
            photoColumn.setCellFactory(e -> new TableCell<Teacher, Photo>(){
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
            
            TableColumn<Teacher, String> nameColumn = new TableColumn<>("name");
            nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
            
            TableColumn<Teacher, Double> prixColumn = new TableColumn<>("prix");
            prixColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
            
            TableColumn<Teacher, String> accountColumn = new TableColumn<>("account");
            accountColumn.setCellValueFactory(new PropertyValueFactory<>("account"));

            TableColumn<Teacher, String> experienceColumn = new TableColumn<>("degree");
            experienceColumn.setCellValueFactory(new PropertyValueFactory<>("degree"));
            
  
            
            
            TableColumn<Teacher, Integer> actionsColumn = new TableColumn<>("Actions");
            actionsColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
            actionsColumn.setCellFactory(e -> new TableCell<Teacher, Integer>() {
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
                            new TeachersService().deleteObject(item, "teacher");
                            init();
                        });
                        Button updateBtn = new Button("Update");
                        updateBtn.getStyleClass().addAll("btn", "btn-success");
                        updateBtn.setOnAction(e -> updateTeacher(item));
                        actions.getChildren().addAll(updateBtn, deleteBtn);
                        setGraphic(actions);
                    }
                }
            });
            
            
            
            TableView tableView = (TableView) body.lookup("#table");
            tableView.setItems(new TeachersService().findAll());
            tableView.getColumns().addAll(idColumn, photoColumn,nameColumn,accountColumn, experienceColumn,prixColumn,actionsColumn);
        } catch (IOException e) {
            e.printStackTrace();
        }
        AdminController.container.getChildren().clear();
        AdminController.container.getChildren().add(content);
    }
    
    
         public boolean controleSaisie(VBox content) {
             TextField num = (TextField) content.lookup("#num");
             FontAwesomeIconView phonew =(FontAwesomeIconView) content.lookup("#phonew");
       if ((!(num.getText().matches("[0-9]+"))) || num.getText().isEmpty()) {
            phonew.setVisible(true);
            num.setStyle("-jfx-focus-color:red");
            num.requestFocus();
            PopOver pop = new PopOver();
            pop.setContentNode(new Label("Veuillez saisir un numero de telephone valide"));
            phonew.setOnMouseEntered((event) -> {
                pop.show(phonew);
            });
            phonew.setOnMouseExited((event) -> {
                pop.hide();
            });
            num.setOnKeyTyped((event2) -> {
                phonew.setVisible(false);
                num.setStyle("-jfx-focus-color:green");
            });
            return false;

        }
      
        return true;
    }

         public void addprof(){

  AdminController.treeView.getSelectionModel().select(3);
        VBox content = null;
        try {
            content = FXMLLoader.load(getClass().getResource("/GUI/admin/form-teacher.fxml"));
            content.setPrefWidth(AdminController.container.getWidth());
            content.setPrefHeight(AdminController.container.getHeight());
            ComboBox<Subject> subject = (ComboBox<Subject>) content.lookup("#subject");
            subject.getItems().addAll(new SubjectService().findAll());
            TextField prenom = (TextField) content.lookup("#prenom");
            TextField nom = (TextField) content.lookup("#nom");
            Button icon = (Button) content.lookup("#icon");
            FileChooser fc = new FileChooser();
            fc.setTitle("Choisir une icone");
            FileChooser.ExtensionFilter fe = new FileChooser.ExtensionFilter("Image files", "*.png", ".jpg");
            fc.setSelectedExtensionFilter(fe);
            icon.setOnAction(e -> image = fc.showOpenDialog(Main.window));
            TextField lien = (TextField) content.lookup("#lien");
            TextField num = (TextField) content.lookup("#num");
            TextField degree = (TextField) content.lookup("#degree");
            TextField exp = (TextField) content.lookup("#exp");
            TextField hob = (TextField) content.lookup("#hob");
            TextField prix = (TextField) content.lookup("#prix");
            Button save = (Button) content.lookup("#save");
            VBox fcontent = content;
            save.setOnAction(e -> {
                if (controleSaisie(fcontent)) {
                Photo imageFile = new Photo(image);
                   
                Teacher u = new Teacher(
                     
                    nom.getText(),
                    prenom.getText(),
                    Double.parseDouble(prix.getText()),
                    lien.getText(),
                    Integer.parseInt(num.getText()),
                    degree.getText(),
                    exp.getText(),
                    hob.getText(),
                    imageFile,
                    subject.getSelectionModel().getSelectedItem());
                new TeachersService().addTeacher(u);
                init();
                 }
            });
             
        } catch (IOException e) {
            e.printStackTrace();
        }
        AdminController.container.getChildren().clear();
        AdminController.container.getChildren().add(content);
       
    }

     private void updateTeacher(Integer id) {
        AdminController.treeView.getSelectionModel().select(5);
        VBox content = null;
        try {
            content = FXMLLoader.load(getClass().getResource("/GUI/admin/form-teacher.fxml"));
            Teacher g = new TeachersService().findTeacher(id);
//            Label headTitle = (Label) content.lookup("#headTitle");
//            headTitle.setText("Modifier un teacher");
            content.setPrefWidth(AdminController.container.getWidth());
            content.setPrefHeight(AdminController.container.getHeight());
            TextField prenom = (TextField) content.lookup("#prenom");
            prenom.setText(g.getLastname());
            TextField nom = (TextField) content.lookup("#nom");
            nom.setText(g.getName());
            TextField lien = (TextField) content.lookup("#lien");
            lien.setText(g.getAccount());
            TextField num = (TextField) content.lookup("#num");
            num.setText(Integer.toString(g.getPhone()));
            TextField degree = (TextField) content.lookup("#degree");
            degree.setText(g.getDegree());
            TextField exp = (TextField) content.lookup("#exp");
            exp.setText(g.getExperience());
            TextField hob = (TextField) content.lookup("#hob");
            hob.setText(g.getHobbies());
            TextField prix = (TextField) content.lookup("#prix");
            prix.setText(Double.toString(g.getPrice()));
            
            
             ComboBox<Subject> subject = (ComboBox<Subject>) content.lookup("#subject");
             subject.setItems(new SubjectService().findAll());
            
            
            subject.setValue(g.getSubject());
           
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
                   SubjectService ss= new SubjectService();
                   g.setAccount(lien.getText());
                   g.setDegree(degree.getText());
                   g.setExperience(exp.getText());
                   g.setHobbies(hob.getText());
                   g.setLastname(prenom.getText());
                   g.setName(nom.getText());
                   g.setPhone(Integer.parseInt(num.getText()));
                   g.setPrice(Double.parseDouble(prix.getText()));
                   g.setSubject(subject.getValue());
                   
                    new TeachersService().updateTeacher(g);
                    
                    init();
                
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        AdminController.container.getChildren().clear();
        AdminController.container.getChildren().add(content);
    }
    
    
    
}
    

