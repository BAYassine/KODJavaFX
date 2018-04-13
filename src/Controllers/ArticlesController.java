/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import Core.Main;
import Entities.Article;
import Entities.Category;
import Entities.Photo;
import Services.ArticleService;
import Services.CategoryService;
import Services.PhotoService;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;

/**
 *
 * @author Meriem
 */
public class ArticlesController {

    public void init() throws SQLException {

        ScrollPane center = null;
        HBox card = null;
        ObservableList<Article> recherches = FXCollections.observableArrayList();
        ArticleService cs = new ArticleService();
        ImageView image;
        PhotoService is = new PhotoService();
        ObservableList<Article> articles = cs.findAll();

        try {
            center = FXMLLoader.load(getClass().getResource("/GUI/Article.fxml"));
            center.setMaxHeight(Main.scene.getHeight());
            VBox content = (VBox) center.getContent();
            center.setPrefWidth(Main.scene.getWidth());
            for (int i = 0; i < articles.size(); i += 1) {
                Article a = articles.get(i);
                card = FXMLLoader.load(getClass().getResource("/GUI/listArticles.fxml"));
                image = (ImageView) card.lookup("#image");
                Photo img = is.findImage(articles.get(i).getPhoto().getId());
                image.setImage(new Image(img.getWebPath()));
                Label title = (Label) card.lookup("#title");
                // Label description = (Label) card.lookup("#description");
                Label subject = (Label) card.lookup("#subject");
                Label auteur = (Label) card.lookup("#auteur");
                title.setText(articles.get(i).getTitle());
                //description.setText(articles.get(i).getDescription());

               
               
            Label desc = (Label) card.lookup("#desc");
            desc.setText("Description: "+ articles.get(i).getDescription());
                System.out.println(articles.get(i).getDescription());

                subject.setText("Sujet: "+ articles.get(i).getSubject());
                auteur.setText("Auteur: "+articles.get(i).getAuteur());
                Button more = (Button) card.lookup("#more");
                int finalI = i;
                System.out.println(a.getId());
                more.setOnAction(e -> {
                    a.setViews(a.getViews() + 1);

                    System.out.println(a.getViews());
                    cs.updateViews(a);
                    showMore(a.getId());
                });
                content.getChildren().add(card);

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        ScrollPane sp = new ScrollPane();
        sp.setContent(center);
        Main.pane.setCenter(sp);
    }

    private void showMore(int id) {

        try {
            ScrollPane center = null;
            HBox body = null;
            CategoryService cv = new CategoryService();
            ArticleService cs = new ArticleService();
            ImageView image;
            ObservableList<Article> articles = cs.moreArticle(id);

            center = FXMLLoader.load(getClass().getResource("/GUI/MoreArticle.fxml"));

            body = (HBox) center.getContent();
            center.setPrefWidth(Main.scene.getWidth());
            center.setPrefHeight(Main.scene.getHeight() - 160);
            body.setPrefWidth(Main.scene.getWidth());
            image = (ImageView) body.lookup("#image");
            image.setImage(new Image(articles.get(0).getPhoto().getWebPath()));
            Label desc = (Label) body.lookup("#desc");
            desc.setText(articles.get(0).getDescription());
            
            Label title = (Label) body.lookup("#title");
            Label subject = (Label) body.lookup("#subject");
            Label auteur = (Label) body.lookup("#auteur");
            title.setText(articles.get(0).getTitle());
            subject.setText("Sujet: "+ articles.get(0).getSubject());
            auteur.setText("Auteur: " + articles.get(0).getAuteur());
            ObservableList<Category> categories = cv.findCategory("Article");
            ListView<Category> categories_container = new ListView<Category>();
            categories_container = (ListView) body.lookup("#categories");
            categories_container.setItems(categories);
            categories_container.setPrefWidth(100);
            categories_container.setPrefHeight(70);

            //center.setContent(body);
            VBox views = (VBox) body.lookup("#views");
            //views.getChildren().add(categories_container);

            VBox views1 = (VBox) body.lookup("#views1");
            ListView<Article> recommended_container = (ListView) views1.lookup("#list1");
            recommended_container.setItems(new ArticleService().findRelated(articles.get(0).getCategory()));
            ObservableList<Article> articleslist = new ArticleService().findRelated(articles.get(0).getCategory());
            System.out.println("size : " + articles.get(0).getCategory());
            ObservableList<Article> mostviewed;
            try {
                mostviewed = cs.findMostViewed();
                ListView<String> articles_container = new ListView<String>();
                articles_container = (ListView) views.lookup("#list");
                ObservableList<String> salut = FXCollections.observableArrayList();
                salut.add(mostviewed.get(0).getTitle());
                articles_container.setItems(salut);
                articles_container.setPrefWidth(100);
                articles_container.setPrefHeight(70);
                articles_container.setItems(salut);

                //body.getChildren().add(views);
            } catch (SQLException ex) {
                Logger.getLogger(ArticlesController.class.getName()).log(Level.SEVERE, null, ex);
            }
            Main.pane.setCenter(center);

//             for (int i = 0; i < categories.size(); i += 1) {
//               
//                Label name=(Label) card.lookup("#cat");
//                name.setText(categories.get(i).getName()+"\n");
//                categories_container.getChildren().add(name);   
//                
//                
//
//            }
        } catch (IOException ex) {
            Logger.getLogger(ArticlesController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void rechercher(String title) {

        TextField filterField;
    }

}
