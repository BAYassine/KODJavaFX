/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Services;

import Core.DBConnection;
import Entities.Article;
import Entities.Article;
import Entities.Babysitter;
import Entities.Category;
import Entities.Photo;
import Entities.Video;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Meriem
 */
public class ArticleService extends Service {

    public void addArticle(Article article) throws SQLException {
        String sqlPhoto = "INSERT INTO photos (url, alt) "
                + "VALUES (?,?)";
        String req = "INSERT INTO `article`(`image_id`,`category`, `description`, `src`, `title`, `subject`, `type`, `auteur`, `views`, `date`) values (?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement ps = this.connection.prepareStatement(sqlPhoto, Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, article.getPhoto().getUrl());
        ps.setString(2, article.getPhoto().getAlt());
        ps.executeUpdate();
        int photoId = 0;
        ResultSet rs = ps.getGeneratedKeys();
        while (rs.next()) {
            photoId = rs.getInt(1);
        }
        article.getPhoto().setId(photoId);
        PreparedStatement pst = this.connection.prepareStatement(req);
        pst.setInt(1, article.getPhoto().getId());
        pst.setInt(2, article.getCategory());
        pst.setString(3, article.getDescription());
        pst.setString(4, article.getSrc());
        pst.setString(5, article.getTitle());
        pst.setString(6, article.getSubject());
        pst.setString(7, article.getType());
        pst.setString(8, article.getAuteur());
        pst.setInt(9, article.getViews());
        pst.setDate(10, article.getDate());

        pst.executeUpdate();
        article.getPhoto().moveToServer();
        System.out.println("c bon");
    }

    public Article findArticle(int id) {
        String sql = "SELECT * FROM article WHERE id = " + id;
        Integer photoId = 0;
        Article c = null;
        try {
            Statement stm = this.connection.createStatement();
            ResultSet rs = stm.executeQuery(sql);
            while (rs.next()) {
                c = new Article();
                c.setId(rs.getInt("id"));
                c.setCategory(rs.getInt("category"));
                c.setDescription(rs.getString("description"));
                c.setSrc(rs.getString("src"));
                c.setTitle(rs.getString("title"));
                c.setSubject(rs.getString("subject"));
                c.setType(rs.getString("type"));
                c.setAuteur(rs.getString("auteur"));
                c.setViews(rs.getInt("views"));
                photoId = rs.getInt("image_id");
                c.setDate(rs.getDate("date"));
            }
            Photo p = null;
            if (photoId != 0) {
                ResultSet rs1 = stm.executeQuery("SELECT * FROM photos WHERE id =" + photoId);
                while (rs1.next()) {
                    p = new Photo();
                    p.setId(photoId);
                    p.setAlt(rs1.getString("alt"));
                    p.setUrl(rs1.getString("url"));
                }
            }
            c.setPhoto(p);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return c;
    }

    public ObservableList<Article> showArticles() {

        ObservableList<Article> myList = FXCollections.observableArrayList();
        Integer photoId = null;
        String req = "SELECT * FROM `article`";
        try {
            PreparedStatement pst = DBConnection.getInstance().getConnection().prepareStatement(req);
            Statement stm = this.connection.createStatement();
            ResultSet rs = pst.executeQuery(req);

            while (rs.next()) {
                Article article = new Article();
                article.setId(rs.getInt("id"));
                article.setCategory(rs.getInt("category"));
                article.setDescription(rs.getString("description"));
                article.setSrc(rs.getString("src"));
                article.setTitle(rs.getString("title"));
                article.setSubject(rs.getString("subject"));
                article.setType(rs.getString("type"));
                article.setAuteur(rs.getString("auteur"));
                article.setViews(rs.getInt("views"));
                article.setDate(rs.getDate("date"));
                photoId = rs.getInt("image_id");
                Photo p = null;
                if (photoId != 0) {
                    rs = stm.executeQuery("SELECT * FROM photos WHERE id =" + photoId);
                    while (rs.next()) {
                        p = new Photo();
                        p.setId(photoId);
                        p.setAlt(rs.getString("alt"));
                        p.setUrl(rs.getString("url"));
                    }
                }
                article.setPhoto(p);
                myList.add(article);
            }
        } catch (SQLException ex) {
            System.out.println("Error" + ex.getMessage());
        }
        return myList;

    }

    public ObservableList<Article> findAll() throws SQLException {
        Integer photoId = 0;
        ObservableList<Article> articles = FXCollections.observableArrayList();
        String sql = "SELECT * FROM article";
        PreparedStatement prs = this.connection.prepareStatement(sql);
        ResultSet resultat = prs.executeQuery();

        while (resultat.next()) {
            Article c = new Article();
            c.setId(resultat.getInt("id"));
            c.setCategory(resultat.getInt("category"));
            c.setDescription(resultat.getString("description"));
            c.setSrc(resultat.getString("src"));
            c.setTitle(resultat.getString("title"));
            c.setSubject(resultat.getString("subject"));
            c.setType(resultat.getString("type"));
            c.setAuteur(resultat.getString("auteur"));
            c.setViews(resultat.getInt("views"));
            c.setDate(resultat.getDate("date"));
            photoId = resultat.getInt("image_id");
            Photo p = null;
            if (photoId != 0) {
                String rs = "SELECT * FROM photos WHERE id =" + photoId;
                prs = this.connection.prepareStatement(rs);
                ResultSet resultat1 = prs.executeQuery();
                while (resultat1.next()) {
                    p = new Photo();
                    p.setId(photoId);
                    p.setAlt(resultat1.getString("alt"));
                    p.setUrl(resultat1.getString("url"));
                }
            }
            c.setPhoto(p);

            articles.add(c);
        }
        return articles;
    }

    public ObservableList<Article> findMostViewed() throws SQLException {
        ObservableList<Article> articles = FXCollections.observableArrayList();
        Article a = new Article();
        String sql = "SELECT * FROM article WHERE views=(select MAX(views) FROM article)";
        Statement stmt = this.connection.createStatement();
        Integer photoId = 0;
        Integer categoryId = 0;
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            a.setId(rs.getInt("id"));
            a.setDescription(rs.getString("description"));
            a.setType(rs.getString("type"));
            a.setSrc(rs.getString("src"));
            a.setTitle(rs.getString("title"));
            a.setAuteur(rs.getString("auteur"));
            a.setDate(rs.getDate("date"));
            a.setViews(rs.getInt("views"));
            photoId = rs.getInt("image_id");
            categoryId = rs.getInt("category");

        }
        Photo p = null;
        if (photoId != 0) {
            ResultSet rs1 = stmt.executeQuery("SELECT * FROM photos WHERE id =" + photoId);
            while (rs1.next()) {
                p = new Photo();
                p.setId(photoId);
                p.setAlt(rs1.getString("alt"));
                p.setUrl(rs1.getString("url"));
            }
        }
        a.setPhoto(p);
        Category c = null;
        if (categoryId != 0) {
            ResultSet rs2 = stmt.executeQuery("SELECT * FROM category WHERE id =" + categoryId);
            while (rs2.next()) {
                c = new Category();
                c.setId(categoryId);
                c.setName(rs2.getString("name"));
                c.setType(rs2.getString("type"));
            }
        }
        a.setCategory(c.getId());
        articles.add(a);
        return articles;

    }

    public ObservableList<Article> moreArticle(int id) {
        ObservableList<Article> articles = FXCollections.observableArrayList();

        try {

            Article a = new Article();
            String sql = "SELECT * FROM article WHERE id =" + id;
            Statement stmt = this.connection.createStatement();
            Integer photoId = 0;
            Integer categoryId = 0;
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                a.setId(rs.getInt("id"));
                a.setDescription(rs.getString("description"));
                a.setSrc(rs.getString("src"));
                a.setAuteur(rs.getString("auteur"));
                a.setDate(rs.getDate("date"));
                a.setSubject(rs.getString("subject"));
                a.setTitle(rs.getString("title"));
                a.setType(rs.getString("type"));
                a.setViews(rs.getInt("views"));

                photoId = rs.getInt("image_id");
                categoryId = rs.getInt("category");

                Photo p = null;
                if (photoId != 0) {
                    p = new PhotoService().findImage(photoId);
                }
                a.setPhoto(p);
                Category c = null;
                if (categoryId != 0) {
                    c = new CategoryService().findCategory(categoryId);
                }
                a.setCategory(c.getId());

            }
            articles.add(a);

        } catch (SQLException ex) {
            Logger.getLogger(ArticleService.class.getName()).log(Level.SEVERE, null, ex);
        }

        return articles;

    }

    public void updateViews(Article c) {

        String sql
                = "UPDATE article "
                + "SET views=? "
                + "WHERE article.id = ?";
        System.out.println(sql);

        try {
            PreparedStatement ps = this.connection.prepareStatement(sql);
            ps.setInt(1, c.getViews());
            ps.setInt(2, c.getId());
            ps.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(ArticleService.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void updateArticle(Article c) {
        String photoText = "";
        String sqlPhoto = "INSERT INTO photos (url, alt) "
                + "VALUES (?,?)";

        try {
            System.out.println(c.getPhoto());
            PreparedStatement ps = null;
            if (c.getPhoto() != null) {
                ps = this.connection.prepareStatement(sqlPhoto, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, c.getPhoto().getUrl());
                ps.setString(2, c.getPhoto().getAlt());
                ps.executeUpdate();
                int photoId = 0;
                ResultSet rs = ps.getGeneratedKeys();
                while (rs.next()) {
                    photoId = rs.getInt(1);
                }
                System.out.println();
                c.getPhoto().setId(photoId);
                photoText = ",image_id=? ";
            }

            String sql
                    = "UPDATE article "
                    + "SET category=?, description=?, title=?, src=?, subject=?, date=?, type=?, auteur=? " + photoText
                    + "WHERE article.id = ?";
            System.out.println(sql);
            ps = this.connection.prepareStatement(sql);
            ps.setInt(1, c.getCategory());
            ps.setString(2, c.getDescription());
            ps.setString(3, c.getTitle());
            ps.setString(4, c.getSrc());
            ps.setString(5, c.getSubject());
            ps.setDate(6, c.getDate());
            ps.setString(7, c.getType());
            ps.setString(8, c.getAuteur());
            if (c.getPhoto() != null) {
                ps.setInt(9, c.getPhoto().getId());
                ps.setInt(10, c.getId());
                c.getPhoto().moveToServer();
            } else {
                ps.setInt(9, c.getId());
            }
            ps.executeUpdate();
            System.out.println("L'article a été modifié avec succes");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ObservableList<Article> findArticleByCat(int id) {
        ObservableList<Article> articles = FXCollections.observableArrayList();

        try {

            Article a = new Article();
            String sql = "SELECT * FROM article WHERE category = " + id;
            Statement stmt = this.connection.createStatement();
            Integer photoId = 0;
            Integer categoryId = 0;
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                a.setId(rs.getInt("id"));
                a.setDescription(rs.getString("description"));
                a.setSrc(rs.getString("src"));
                a.setAuteur(rs.getString("auteur"));
                a.setDate(rs.getDate("date"));
                a.setSubject(rs.getString("subject"));
                a.setTitle(rs.getString("title"));
                a.setType(rs.getString("type"));
                a.setViews(rs.getInt("views"));

                photoId = rs.getInt("image_id");
                categoryId = rs.getInt("category");

                Photo p = null;
                if (photoId != 0) {
                    p = new PhotoService().findImage(photoId);
                }
                a.setPhoto(p);
                Category c = null;
                if (categoryId != 0) {
                    c = new CategoryService().findCategory(categoryId);
                }
                a.setCategory(c.getId());

            }
            articles.add(a);

        } catch (SQLException ex) {
            Logger.getLogger(ArticleService.class.getName()).log(Level.SEVERE, null, ex);
        }

        return articles;

    }
    
    public ObservableList<Article> findRelated(int categoryId){
        String sql = "SELECT * FROM article WHERE category = " + categoryId + " LIMIT 3";
        ObservableList<Article> articles = FXCollections.observableArrayList();
        try{
            Statement prs = this.connection.createStatement();
            ResultSet resultat = prs.executeQuery(sql);

            while (resultat.next()) {
                Article c = new Article();
                c.setId(resultat.getInt("id"));
                c.setCategory(resultat.getInt("category"));
                c.setDescription(resultat.getString("description"));
                c.setSrc(resultat.getString("src"));
                c.setTitle(resultat.getString("title"));
                c.setSubject(resultat.getString("subject"));
                c.setType(resultat.getString("type"));
                c.setAuteur(resultat.getString("auteur"));
                c.setViews(resultat.getInt("views"));
                c.setDate(resultat.getDate("date"));
                articles.add(c);
            }
            return articles;

        }   catch (SQLException ex) { 
            Logger.getLogger(ArticleService.class.getName()).log(Level.SEVERE, null, ex);
        }   
        return articles;
    }


    public HashMap<Category,Integer> stat() {
        HashMap<Category , Integer> articles = new HashMap<Category, Integer>();
        try {
            String sql = "SELECT sum(views)/(SELECT SUM(views) FROM article) as views , category FROM article GROUP BY category";

            Statement stmt = this.connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                int categoryId = rs.getInt("category");
                int views = (int)( rs.getDouble("views") * 100);
                Category c = new CategoryService().findCategory(categoryId);
                articles.put(c, views);
            }

        } catch (SQLException ex) {
            Logger.getLogger(ArticleService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return articles;
    }

}
