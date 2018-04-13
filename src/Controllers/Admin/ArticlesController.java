package Controllers.Admin;

import Controllers.AdminController;
import Core.Main;
import Dependencies.pherialize.MixedArray;
import Dependencies.pherialize.Pherialize;
import Entities.Article;
import Entities.Article;
import Entities.Category;
import Entities.Game;
import Entities.Photo;
import Services.ArticleService;
import Services.ArticleService;
import Services.CategoryService;
import Services.GameService;
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
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class ArticlesController {

    private File image = null;

    public void init() {
        VBox content = null;
        List<String> titles = new ArrayList<String>();
        List<Integer> views = new ArrayList<Integer>();
        try {
            content = FXMLLoader.load(getClass().getResource("/GUI/admin/articles-list.fxml"));
            VBox body = (VBox) content.lookup("#panelBody");
            content.setPrefWidth(AdminController.container.getWidth());
            content.setPrefHeight(AdminController.container.getHeight());
            Button addBtn = (Button) content.lookup("#addBtn");
            addBtn.setOnAction(e -> addArticle());
            Button testBtn = (Button) content.lookup("#test");
            ObservableList<Article> articles;
            try {
                articles = new ArticleService().findAll();
                for (int i = 0; i < articles.size(); i++) {
                    titles.add(articles.get(i).getTitle());
                    views.add(articles.get(i).getViews());

                }
            } catch (SQLException ex) {
                Logger.getLogger(ArticlesController.class.getName()).log(Level.SEVERE, null, ex);
            }

            testBtn.setOnAction(e -> pieChart());
            TableColumn<Article, Integer> idColumn = new TableColumn<>("ID");
            idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
            idColumn.setMaxWidth(50);
            idColumn.setMinWidth(50);
            idColumn.setStyle("-fx-alignment : center");

            TableColumn<Article, Category> CategoryColumn = new TableColumn<>("CATEGORIE");
            CategoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));

            TableColumn<Article, String> descriptionColumn = new TableColumn<>("DESCRIPTION");
            descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

            TableColumn<Article, String> srcColumn = new TableColumn<>("SRC");
            srcColumn.setCellValueFactory(new PropertyValueFactory<>("src"));

            TableColumn<Article, String> titleColumn = new TableColumn<>("TITRE");
            titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));

            TableColumn<Article, String> subjectColumn = new TableColumn<>("SUJET");
            subjectColumn.setCellValueFactory(new PropertyValueFactory<>("subject"));

            TableColumn<Article, String> typeColumn = new TableColumn<>("TYPE");
            typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));

            TableColumn<Article, String> auteurColumn = new TableColumn<>("AUTEUR");
            auteurColumn.setCellValueFactory(new PropertyValueFactory<>("auteur"));

            TableColumn<Article, String> viewsColumn = new TableColumn<>("NOMBRE DE VUES");
            viewsColumn.setCellValueFactory(new PropertyValueFactory<>("views"));

            TableColumn<Article, String> dateColumn = new TableColumn<>("DATE");
            dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));

            TableColumn<Article, Photo> photoColumn = new TableColumn<>("IMAGE");
            photoColumn.setCellValueFactory(new PropertyValueFactory<>("photo"));
            photoColumn.setCellFactory(e -> new TableCell<Article, Photo>() {
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

            TableColumn<Article, Integer> actionsColumn = new TableColumn<>("Actions");
            actionsColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
            actionsColumn.setCellFactory(e -> new TableCell<Article, Integer>() {
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
                            new ArticleService().deleteObject(item, "article");
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Succès");
                            alert.setHeaderText("Suppression réussie");
                            alert.setContentText("L'article a bien été supprimé");
                            alert.showAndWait();
                            init();
                        });
                        Button updateBtn = new Button("Update");
                        updateBtn.getStyleClass().addAll("btn", "btn-success");
                        updateBtn.setOnAction(e -> updateArticle(item));
                        actions.getChildren().addAll(updateBtn, deleteBtn);
                        setGraphic(actions);
                    }
                }
            });
            TableView tableView = (TableView) body.lookup("#table");
            try {
                tableView.setItems(new ArticleService().findAll());

            } catch (SQLException ex) {
                Logger.getLogger(ArticlesController.class.getName()).log(Level.SEVERE, null, ex);
            }
            tableView.getColumns().addAll(idColumn, CategoryColumn, descriptionColumn, srcColumn, titleColumn, subjectColumn, typeColumn, auteurColumn, viewsColumn, dateColumn, photoColumn, actionsColumn);

        } catch (IOException e) {
            e.printStackTrace();
        }

        ScrollPane sp = new ScrollPane();
        sp.setContent(content);
        AdminController.container.getChildren().clear();
        AdminController.container.getChildren().add(content);

    }

    public void addArticle() {

     
        try {
            // AdminController.treeView.getSelectionModel().select(3);
            VBox content = null;
            ArticleService bs = new ArticleService();
            CategoryService cv = new CategoryService();

            content = FXMLLoader.load(getClass().getResource("/GUI/admin/form-article.fxml"));
            content.setPrefWidth(AdminController.container.getWidth());
            content.setPrefHeight(AdminController.container.getHeight());
            TextField title = (TextField) content.lookup("#title");
            TextField subject = (TextField) content.lookup("#subject");
            TextField src = (TextField) content.lookup("#src");
            TextField views = (TextField) content.lookup("#views");
            DatePicker date = (DatePicker) content.lookup("#date");

            TextField auteur = (TextField) content.lookup("#auteur");
            TextField description = (TextField) content.lookup("#description");
            ComboBox categories = (ComboBox) content.lookup("#category");
            categories.setVisibleRowCount(25);
            ObservableList<Category> categories_dispo = cv.findCategory("Article");

            categories.setItems(categories_dispo);
            //afficher la liste des catégories dans la combobox

            ComboBox types = (ComboBox) content.lookup("#type");
            types.setVisibleRowCount(25);

            types.getItems().addAll("Aides Et Services", "Education");

            Button save = (Button) content.lookup("#save");
            Button icon = (Button) content.lookup("#icon");
            FileChooser fc = new FileChooser();
            fc.setTitle("Choisir une icone");
            FileChooser.ExtensionFilter fe = new FileChooser.ExtensionFilter("Image files", "*.png", ".jpg");
            fc.setSelectedExtensionFilter(fe);
            icon.setOnAction(e -> image = fc.showOpenDialog(Main.window));
            save.setOnAction(e -> {
                
                
                LocalDate localDate = date.getValue();
                Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
                java.util.Date date1 = Date.from(instant);
                java.sql.Date dtSql = new java.sql.Date(date1.getTime());
                Photo imageFile = new Photo(image);
                Date datee = new Date(new java.util.Date().getTime());
                if (!dtSql.before(datee)) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("ERREUR");
                    alert.setHeaderText("Choix erronné");
                    alert.setContentText("Veuillez saisir une date inférieure à aujourd'hui");
                    alert.show();
                    init();
                    
                    
                    
                }   else  if (description.getText().isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("ERREUR");
                    alert.setHeaderText("Description vide");
                    alert.setContentText("Veuillez saisir une description");
                    alert.show();
                    
                }
               else   if (date.getEditor().getText().length() == 0 )
                    
                {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("ERREUR");
                    alert.setHeaderText("Date vide");
                    alert.setContentText("Veuillez saisir une date");
                    alert.show();
                }
                
               else   if (src.getText().isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("ERREUR");
                    alert.setHeaderText("Source vide");
                    alert.setContentText("Veuillez saisir une source");
                    alert.show();  
                    
                }  else  if (title.getText().isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("ERREUR");
                    alert.setHeaderText("Titre vide");
                    alert.setContentText("Veuillez saisir un titre");
                    alert.show();
                    
                }
                
                 else  if (subject.getText().isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("ERREUR");
                    alert.setHeaderText("Champ sujet vide");
                    alert.setContentText("Veuillez saisir un sujet");
                    alert.show();
                    
                }  else  if (auteur.getText().isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("ERREUR");
                    alert.setHeaderText("Champ auteur vide");
                    alert.setContentText("Veuillez saisir un auteur");
                    alert.show();
                    
                }
                 else  if (types.getSelectionModel().isEmpty())
                {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("ERREUR");
                    alert.setHeaderText("Sélectionnez un type yarham bouk");
                    alert.setContentText("Veuillez saisir un type");
                    alert.show();
                }
                else if (categories.getSelectionModel().isEmpty())
                {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("ERREUR");
                    alert.setHeaderText("Sélectionnez un type yarham bouk");
                    alert.setContentText("Veuillez saisir une categorie");
                    alert.show();
                }
                
                
                else {
                    
                    try {
                        Article   u = new Article(cv.findByName( categories.getValue().toString()).getId(), description.getText(), src.getText(), title.getText(), subject.getText(), types.getValue().toString(), auteur.getText(), 0, dtSql, imageFile);
                        new ArticleService().addArticle(u);
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Ajout réussi");
                    alert.setHeaderText("Article ajouté avec succès");
                    alert.setContentText("Ajout réussi");
                    alert.show();
                        init();
                        
                    } catch (SQLException ex) {
                        Logger.getLogger(ArticlesController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                
                
                
                
                
                
            });
            

            AdminController.container.getChildren().clear();
            AdminController.container.getChildren().add(content);
        } catch (IOException ex) {
            Logger.getLogger(ArticlesController.class.getName()).log(Level.SEVERE, null, ex);
        }
        } 
        
    


    public void updateArticle(Integer id) {
        AdminController.treeView.getSelectionModel().select(5);
        VBox content = null;
        try {
            content = FXMLLoader.load(getClass().getResource("/GUI/admin/form-article.fxml"));
            Article g = new ArticleService().findArticle(id);
//            Label headTitle = (Label) content.lookup("#headTitle");
//            headTitle.setText("Modifier un babysitter");
            content.setPrefWidth(AdminController.container.getWidth());
            content.setPrefHeight(AdminController.container.getHeight());
            CategoryService cv = new CategoryService();

            TextField title = (TextField) content.lookup("#title");
            title.setText(g.getTitle());

            TextField subject = (TextField) content.lookup("#subject");
            subject.setText(g.getSubject());

            TextField src = (TextField) content.lookup("#src");
            src.setText(g.getSrc());

            DatePicker date = (DatePicker) content.lookup("#date");

            ComboBox types = (ComboBox) content.lookup("#type");
            types.setVisibleRowCount(25);

            types.getItems().addAll("Aides Et Services", "Education");
            types.setValue(g.getType());
            date.setValue(g.getDate().toLocalDate());

            TextField auteur = (TextField) content.lookup("#auteur");
            auteur.setText(g.getAuteur());
            TextField description = (TextField) content.lookup("#description");
            description.setText(g.getDescription());

            // TextField image= (TextField)center.lookup("#subject");
            ComboBox categories = (ComboBox) content.lookup("#category");
            categories.setVisibleRowCount(25);
            ObservableList<Category> categories_dispo = cv.findCategory("Article");

            categories.setItems(categories_dispo);
            categories.setValue(cv.findCategory(g.getCategory()).toString());

            Button icon = (Button) content.lookup("#icon");
            FileChooser fc = new FileChooser();
            fc.setTitle("Choisir une icone");
            FileChooser.ExtensionFilter fe = new FileChooser.ExtensionFilter("Image files", "*.png", ".jpg");
            fc.setSelectedExtensionFilter(fe);
            icon.setOnAction(e -> image = fc.showOpenDialog(Main.window));

            Button save = (Button) content.lookup("#save");

            save.setOnAction(e -> {

                try {
                    LocalDate localDate = date.getValue();
                    Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
                    java.util.Date date1 = Date.from(instant);
                    java.sql.Date dtSql = new java.sql.Date(date1.getTime());
                    if (image != null) {
                        Photo imageFile = new Photo(image);
                        g.setPhoto(imageFile);
                    } else {
                        g.setPhoto(null);
                    }
                    g.setAuteur(auteur.getText());
                    g.setCategory(cv.findByName(categories.getValue().toString()).getId());
                    g.setDate(dtSql);
                    g.setDescription(description.getText());
                    g.setSrc(src.getText());
                    g.setSubject(subject.getText());
                    g.setTitle(title.getText());
                    g.setType(types.getValue().toString());
                    new ArticleService().updateArticle(g);

                    init();
                } catch (SQLException ex) {
                    Logger.getLogger(ArticlesController.class.getName()).log(Level.SEVERE, null, ex);
                }

            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        AdminController.container.getChildren().clear();
        AdminController.container.getChildren().add(content);
    }

    public void pieChart() {

        try {
            VBox pie = new VBox();
            pie = FXMLLoader.load(getClass().getResource("/GUI/pie.fxml"));

            ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
            HashMap<Category, Integer> stats = new ArticleService().stat();
            stats.forEach((k, v) -> {
                pieChartData.add(new PieChart.Data(k.getName(), v));
            });
            final PieChart chart = new PieChart(pieChartData);
            chart.setTitle("Nombre de vues par article");
            chart.setLabelLineLength(10);
            chart.setLegendSide(Side.LEFT);
            pie.getChildren().add(chart);
            AdminController.container.getChildren().clear();
            AdminController.container.getChildren().add(pie);
        } catch (IOException ex) {
            Logger.getLogger(ArticlesController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
//     FXCollections.observableArrayList(new PieChart.Data(articles.get(i).getTitle()),Double.parseDouble(Integer.toString(articles.get(i).getViews())));
}
