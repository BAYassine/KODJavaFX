package Controllers.Admin;

import Controllers.AdminController;
import Core.Main;
import Dependencies.pherialize.MixedArray;
import Dependencies.pherialize.Pherialize;
import Entities.Complaint;
import Entities.Babysitter;
import Entities.Category;
import Entities.Game;
import Entities.Photo;
import Entities.User;
import Services.ComplaintService;
import Services.BabysitterService;
import Services.CategoryService;
import Services.GameService;
import Services.UserService;
import Services.sendmail;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;

import java.io.File;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.sql.Date;
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

public class ComplaintController {

    private File image = null;

    public void init() {
        VBox content = null;

        try {
            content = FXMLLoader.load(getClass().getResource("/GUI/admin/complaints-list.fxml"));
            VBox body = (VBox) content.lookup("#panelBody");
            content.setPrefWidth(AdminController.container.getWidth());
            content.setPrefHeight(AdminController.container.getHeight());
            ComplaintService cs = new ComplaintService();
            ObservableList<Complaint> complaints = null;
            try {
                complaints = new ComplaintService().findAll();
            } catch (SQLException ex) {
                Logger.getLogger(ComplaintController.class.getName()).log(Level.SEVERE, null, ex);
            }
            TableColumn<Babysitter, Integer> idColumn = new TableColumn<>("ID");
            idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
            idColumn.setMaxWidth(50);
            idColumn.setMinWidth(50);
            idColumn.setStyle("-fx-alignment : center");

            TableColumn<Complaint, Integer> id = new TableColumn<>("ID");
            TableColumn<Complaint, Date> date = new TableColumn<>("DATE");
            TableColumn<Complaint, String> subject = new TableColumn<>("SUJET");
            TableColumn<Complaint, Category> categorie = new TableColumn<>("CATEGORIE");
            TableColumn<Complaint, String> description = new TableColumn<>("DESCRIPTION");
            TableColumn<Complaint, String> state = new TableColumn<>("ETAT");
            TableColumn<Complaint, Integer> parent_id = new TableColumn<>("UTILISATEUR");
            id.setCellValueFactory(new PropertyValueFactory<>("id"));
            date.setCellValueFactory(new PropertyValueFactory<>("date"));
            subject.setCellValueFactory(new PropertyValueFactory<>("subject"));
            categorie.setCellValueFactory(new PropertyValueFactory<>("category"));
            description.setCellValueFactory(new PropertyValueFactory<>("description"));
            state.setCellValueFactory(new PropertyValueFactory<>("state"));

            parent_id.setCellValueFactory(new PropertyValueFactory<>("parent"));

            TableColumn<Complaint, User> actionsColumn = new TableColumn<>("Actions");
            actionsColumn.setCellValueFactory(new PropertyValueFactory<>("parent"));
            ObservableList<Complaint> fcomplaints = complaints;
            actionsColumn.setCellFactory(e -> new TableCell<Complaint, User>() {
                @Override
                public void updateItem(User item, boolean empty) {
                    super.updateItem(item, empty);
                    if (!empty && item != null) {
                        int index = getTableRow().getIndex();
                        VBox actions = new VBox();
                        actions.setSpacing(10);
                        actions.setAlignment(Pos.CENTER);
                        actions.setFillWidth(true);
                        Button deleteBtn = new Button("Supprimer");
                        deleteBtn.getStyleClass().addAll("btn", "btn-danger");
                        deleteBtn.setOnAction(e -> {
                            if ("traitee".equals(fcomplaints.get(index).getState())) {
                                new ComplaintService().deleteObject(fcomplaints.get(index).getId(), "complaint");
                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                alert.setTitle("Succès");
                                alert.setContentText("La réclamation a bien été supprimée");
                                alert.showAndWait();
                                init();
                            } else {
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("Erreur");
                                alert.setHeaderText("Impossible de supprimer cette réclamation");
                                alert.setContentText("La réclamation n'a pas encore été traitée");
                                alert.showAndWait();
                            }

                        });
                        Button updateBtn = new Button("Accusé");
                        updateBtn.getStyleClass().addAll("btn", "btn-success");

                        updateBtn.setOnAction(e -> {
                            if ("non_traitee".equals(fcomplaints.get(index).getState())) {

                                sendmail(item.getEmail(), fcomplaints.get(index));
                                fcomplaints.get(index).setState("traitee");
                                cs.updateState(fcomplaints.get(index));
                                init();

                            } else {
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("Erreur");
                                alert.setHeaderText("Réclamation déjà traitée");
                                alert.setContentText("La réclamation a déjà été traitée");
                                alert.showAndWait();
                            }

                        });
                        actions.getChildren().addAll(deleteBtn, updateBtn);
                        setGraphic(actions);
                    }
                }
            });
            TableView tableView = (TableView) body.lookup("#table");

            tableView.setItems(complaints);

            tableView.getColumns().addAll(id, parent_id, categorie, description, subject, date, state, actionsColumn);
        } catch (IOException e) {
            e.printStackTrace();
        }
        AdminController.container.getChildren().clear();
        AdminController.container.getChildren().add(content);
    }

    public void sendmail(String email, Complaint C) {
        sendmail s = new sendmail();
        s.send(email, C);
    }

}
