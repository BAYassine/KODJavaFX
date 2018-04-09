package Controllers.Admin;

import Controllers.AdminController;
import Dependencies.pherialize.MixedArray;
import Dependencies.pherialize.Pherialize;
import Entities.User;
import Services.UserService;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class UsersController {

    public void init() {
        VBox content = null;
        try {
            content = FXMLLoader.load(getClass().getResource("/GUI/Admin/users-list.fxml"));
            VBox body = (VBox) content.lookup("#panelBody");
            content.setPrefWidth(AdminController.container.getWidth());
            content.setPrefHeight(AdminController.container.getHeight());
            Button addBtn = (Button) content.lookup("#addBtn");
            addBtn.setOnAction(e -> addUser());

            TableColumn<User, Integer> idColumn = new TableColumn<>("ID");
            idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
            idColumn.setMaxWidth(50);
            idColumn.setMinWidth(50);
            idColumn.setStyle("-fx-alignment : center");

            TableColumn<User, String> emailColumn = new TableColumn<>("EMAIL");
            emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

            TableColumn<User, String> loginColumn = new TableColumn<>("PSEUDO");
            loginColumn.setCellValueFactory(new PropertyValueFactory<>("username"));

            TableColumn<User, Boolean> stateColumn = new TableColumn<>("ETAT");
            stateColumn.setCellValueFactory(new PropertyValueFactory<>("enabled"));
            stateColumn.setCellFactory(e -> new TableCell<User, Boolean>(){
                @Override
                public void updateItem(Boolean item, boolean empty){
                    super.updateItem(item, empty);
                    setText(empty ? null : item ? "Activé" : "Désactivé");
                }
            });

            TableColumn<User, String> rolesColumn = new TableColumn<>("PRIVILEGES");
            rolesColumn.setCellValueFactory(new PropertyValueFactory<>("roles"));
            rolesColumn.setCellFactory(e -> new TableCell<User, String>(){
                @Override
                public void updateItem(String roles, boolean empty){
                    super.updateItem(roles, empty);
                    if (!empty){
                        MixedArray rolesList = Pherialize.unserialize(roles).toArray();
                        String text = "";
                        for (int i = 0; i < rolesList.size(); i++){
                            if (i > 0) text += ", ";
                            switch (rolesList.getString(i)){
                                case "ROLE_SUPER_ADMIN" :
                                    text += "Super admin";
                                    break;
                                case "ROLE_ADMIN" :
                                    text += "Admin";
                                    break;
                                case "ROLE_PARENT" :
                                    text += "Parent";
                                    break;
                                case "ROLE_PROVIDER" :
                                    text += "Fournisseur";
                                    break;
                                default: text += "Utilisateur";
                            }
                        }
                        setText(text);
                    }
                }
            });

            TableColumn<User, Timestamp> dateColumn = new TableColumn<>("DERNIERE CONNEXION");
            dateColumn.setCellValueFactory(new PropertyValueFactory<>("lastLogin"));
            dateColumn.setCellFactory(e -> new TableCell<User, Timestamp>(){
                @Override
                protected void updateItem(Timestamp item, boolean empty) {
                    super.updateItem(item, empty);
                    if (!empty && item != null){
                        SimpleDateFormat dt = new SimpleDateFormat("d MMMM, y");
                        setText(dt.format(item));
                    }
                    else if (!empty) setText("Aucune connexion");
                }
            });
            TableView tableView = (TableView) body.lookup("#table");
            tableView.setItems(new UserService().findAll());
            tableView.getColumns().addAll(idColumn,emailColumn,loginColumn, stateColumn, rolesColumn, dateColumn);
        } catch (IOException e) {
            e.printStackTrace();
        }
        AdminController.container.getChildren().clear();
        AdminController.container.getChildren().add(content);
    }

    public void addUser(){
        AdminController.treeView.getSelectionModel().select(3);
        VBox content = null;
        try {
            content = FXMLLoader.load(getClass().getResource("/GUI/Admin/form-user.fxml"));
            content.setPrefWidth(AdminController.container.getWidth());
            content.setPrefHeight(AdminController.container.getHeight());
            TextField email = (TextField) content.lookup("#email");
            TextField login = (TextField) content.lookup("#login");
            TextField passwd = (TextField) content.lookup("#passwd");
            ComboBox roles = (ComboBox) content.lookup("#roles");
            Button save = (Button) content.lookup("#save");

            save.setOnAction(e -> {
                User u = new User(login.getText(), passwd.getText(), email.getText());
                new UserService().registerUser(u);
                init();
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        AdminController.container.getChildren().clear();
        AdminController.container.getChildren().add(content);
    }


}

