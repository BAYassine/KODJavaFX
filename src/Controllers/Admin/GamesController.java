package Controllers.Admin;

import Controllers.AdminController;
import Core.Main;
import Dependencies.pherialize.MixedArray;
import Dependencies.pherialize.Pherialize;
import Entities.Category;
import Entities.Game;
import Entities.Photo;
import Services.CategoryService;
import Services.GameService;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GamesController {

    private File image = null;

    public void init() {
        VBox content = null;
        try {
            content = FXMLLoader.load(getClass().getResource("/GUI/Admin/games-list.fxml"));
            VBox body = (VBox) content.lookup("#panelBody");
            content.setPrefWidth(AdminController.container.getWidth());
            content.setPrefHeight(AdminController.container.getHeight());
            Button addBtn = (Button) content.lookup("#addBtn");
            addBtn.setOnAction(e -> addGame());

            TableColumn<Game, Integer> idColumn = new TableColumn<>("ID");
            idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
            idColumn.setMaxWidth(50);
            idColumn.setMinWidth(50);
            idColumn.setStyle("-fx-alignment : center");

            TableColumn<Game, Photo> iconColumn = new TableColumn<>("Icone");
            iconColumn.setCellValueFactory(new PropertyValueFactory<>("icon"));
            iconColumn.setCellFactory(e -> new TableCell<Game, Photo>() {
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

            TableColumn<Game, String> nameColumn = new TableColumn<>("Nom du jeu");
            nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

            TableColumn<Game, String> urlColumn = new TableColumn<>("Lien");
            urlColumn.setCellValueFactory(new PropertyValueFactory<>("url"));

            TableColumn<Game, Integer> ageColumn = new TableColumn<>("Age");
            ageColumn.setCellValueFactory(new PropertyValueFactory<>("age"));

            TableColumn<Game, String> deviceColumn = new TableColumn<>("Appareils");
            deviceColumn.setCellValueFactory(new PropertyValueFactory<>("device"));
            deviceColumn.setCellFactory(e -> new TableCell<Game, String>() {
                @Override
                public void updateItem(String roles, boolean empty) {
                    super.updateItem(roles, empty);
                    if (!empty) {
                        MixedArray rolesList = Pherialize.unserialize(roles).toArray();
                        String text = "";
                        for (int i = 0; i < rolesList.size(); i++) {
                            if (i > 0) text += "\n";
                            switch (rolesList.getInt(i)) {
                                case 1:
                                    text += "Mobile";
                                    break;
                                case 2:
                                    text += "Tablette";
                                    break;
                                case 3:
                                    text += "PC";
                                    break;
                            }
                        }
                        setText(text);
                    }
                }
            });

            TableColumn<Game, String> categoryColumn = new TableColumn<>("Categorie");
            categoryColumn.setCellValueFactory(new PropertyValueFactory<>("lastLogin"));

            TableColumn<Game, Integer> genderColumn = new TableColumn<>("Sexe");
            genderColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));
            genderColumn.setCellFactory(e -> new TableCell<Game, Integer>() {
                @Override
                public void updateItem(Integer gender, boolean empty) {
                    super.updateItem(gender, empty);
                    if (!empty && gender != null) {
                        String text = "";
                        switch (gender) {
                            case 0:
                                text += "Fille";
                                break;
                            case 1:
                                text += "Garçon";
                                break;
                            case 2:
                                text += "Les deux";
                        }
                        setText(text);
                    }
                }
            });
            TableColumn<Game, Integer> actionsColumn = new TableColumn<>("Actions");
            actionsColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
            actionsColumn.setCellFactory(e -> new TableCell<Game, Integer>() {
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
                            new GameService().deleteObject(item, "game");
                        });
                        Button updateBtn = new Button("Update");
                        updateBtn.getStyleClass().addAll("btn", "btn-success");
                        updateBtn.setOnAction(e -> updateGame(item));
                        actions.getChildren().addAll(updateBtn, deleteBtn);
                        setGraphic(actions);
                    }
                }
            });

            TableView tableView = (TableView) body.lookup("#table");
            tableView.setItems(new GameService().findAll());

            tableView.getColumns().addAll(idColumn, iconColumn, nameColumn, urlColumn, ageColumn, deviceColumn, /*categoryColumn,*/ genderColumn, actionsColumn);
        } catch (IOException e) {
            e.printStackTrace();
        }
        AdminController.container.getChildren().clear();
        AdminController.container.getChildren().add(content);
    }

    private void updateGame(Integer id) {
        AdminController.treeView.getSelectionModel().select(5);
        VBox content = null;
        try {
            content = FXMLLoader.load(getClass().getResource("/GUI/admin/form-game.fxml"));
            Game g = new GameService().findGame(id);
            Label headTitle = (Label) content.lookup("#headTitle");
            headTitle.setText("Modifier un jeu");
            content.setPrefWidth(AdminController.container.getWidth());
            content.setPrefHeight(AdminController.container.getHeight());
            TextField name = (TextField) content.lookup("#name");
            name.setText(g.getName());
            TextField url = (TextField) content.lookup("#url");
            url.setText(g.getUrl());
            ComboBox age = (ComboBox) content.lookup("#age");
            age.getItems().addAll(1, 2, 3);
            age.getSelectionModel().select(age.getItems().indexOf(g.getAge()));
            CheckBox smart = (CheckBox) content.lookup("#smart");
            CheckBox tab = (CheckBox) content.lookup("#tab");
            CheckBox pc = (CheckBox) content.lookup("#pc");
            Button icon = (Button) content.lookup("#icon");
            FileChooser fc = new FileChooser();
            fc.setTitle("Choisir une icone");
            FileChooser.ExtensionFilter fe = new FileChooser.ExtensionFilter("Image files", "*.png", ".jpg");
            fc.setSelectedExtensionFilter(fe);
            icon.setOnAction(e -> image = fc.showOpenDialog(Main.window));
            ComboBox<Category> category = (ComboBox) content.lookup("#category");
            RadioButton boy = (RadioButton) content.lookup("#boy");
            RadioButton girl = (RadioButton) content.lookup("#girl");
            RadioButton both = (RadioButton) content.lookup("#both");
            ToggleGroup genderGrp = new ToggleGroup();
            genderGrp.getToggles().addAll(both, boy, girl);
            Button save = (Button) content.lookup("#save");

            List list = new ArrayList<Integer>();
            if (smart.isSelected())
                list.add("1");
            if (tab.isSelected())
                list.add(2);
            if (pc.isSelected())
                list.add(3);

            String devs = Pherialize.serialize(list);
            int selectedGender = 2;
            if (genderGrp.getUserData() == "Fille")
                selectedGender = 0;
            else if (genderGrp.getUserData() == "Garçon")
                selectedGender = 1;
            int finalSelectedGender = selectedGender;
            save.setOnAction(e -> {
                Photo imageFile = new Photo(image);
                g.setIcon(imageFile);
                g.setName(name.getText());
                g.setUrl(url.getText());
                g.setAge(age.getSelectionModel().getSelectedIndex());
                g.setDevice(devs);
                g.setCategory(category.getSelectionModel().getSelectedItem());
                g.setGender(finalSelectedGender
                );
                new GameService().addGame(g);
                init();
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        AdminController.container.getChildren().clear();
        AdminController.container.getChildren().add(content);
    }

    public void addGame() {
        AdminController.treeView.getSelectionModel().select(5);
        VBox content = null;
        try {
            content = FXMLLoader.load(getClass().getResource("/GUI/admin/form-game.fxml"));
            content.setPrefWidth(AdminController.container.getWidth());
            content.setPrefHeight(AdminController.container.getHeight());
            TextField name = (TextField) content.lookup("#name");
            TextField url = (TextField) content.lookup("#url");
            ComboBox age = (ComboBox) content.lookup("#age");
            age.getItems().addAll(1, 2, 3);
            CheckBox smart = (CheckBox) content.lookup("#smart");
            CheckBox tab = (CheckBox) content.lookup("#tab");
            CheckBox pc = (CheckBox) content.lookup("#pc");
            Button icon = (Button) content.lookup("#icon");
            FileChooser fc = new FileChooser();
            fc.setTitle("Choisir une icone");
            FileChooser.ExtensionFilter fe = new FileChooser.ExtensionFilter("Image files", "*.png", ".jpg");
            fc.setSelectedExtensionFilter(fe);
            icon.setOnAction(e -> image = fc.showOpenDialog(Main.window));
            ComboBox<Category> category = (ComboBox<Category>) content.lookup("#category");
            category.setItems(new CategoryService().findCategory("Games"));
            RadioButton boy = (RadioButton) content.lookup("#boy");
            RadioButton girl = (RadioButton) content.lookup("#girl");
            RadioButton both = (RadioButton) content.lookup("#both");
            ToggleGroup genderGrp = new ToggleGroup();
            genderGrp.getToggles().addAll(both, boy, girl);
            Button save = (Button) content.lookup("#save");

            List list = new ArrayList<Integer>();
            if (smart.isSelected())
                list.add("1");
            if (tab.isSelected())
                list.add(2);
            if (pc.isSelected())
                list.add(3);

            String devs = Pherialize.serialize(list);
            int selectedGender = 2;
            if (genderGrp.getUserData() == "Fille")
                selectedGender = 0;
            else if (genderGrp.getUserData() == "Garçon")
                selectedGender = 1;
            int finalSelectedGender = selectedGender;
            save.setOnAction(e -> {
                Photo imageFile = new Photo(image);
                Game g = new Game(
                        imageFile,
                        name.getText(),
                        url.getText(),
                        age.getSelectionModel().getSelectedIndex(),
                        devs,
                        category.getSelectionModel().getSelectedItem(),
                        finalSelectedGender
                );
                new GameService().addGame(g);
                init();
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        AdminController.container.getChildren().clear();
        AdminController.container.getChildren().add(content);
    }


}

