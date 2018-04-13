/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Services;

import Core.DBConnection;
import Core.Main;
import Entities.Category;
import Entities.Photo;
import Entities.Product;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author ASUS
 */
public class ProductServices extends Service{

    public Connection con;
    PreparedStatement ste;

    public ProductServices() {

        con = DBConnection.getInstance().getConnection();

    }

    public void addProduct(Product product) {

        String sqlPhoto = "INSERT INTO photos (url, alt) "
                + "VALUES (?,?)";

        String req = "INSERT INTO `product`(`name`,`img_id`, `description`, `price`, `available`, `tva`, `gender`, `age`, `idCategory`, `user_id`,`quantite`) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
        try {
            PreparedStatement pst = this.connection.prepareStatement(sqlPhoto, Statement.RETURN_GENERATED_KEYS);
            pst.setString(1, product.getImg().getUrl());
            pst.setString(2, product.getImg().getAlt());
            pst.executeUpdate();
            int photoId = 0;
            ResultSet rs = pst.getGeneratedKeys();
            while (rs.next()) {
                photoId = rs.getInt(1);
            }
            product.getImg().setId(photoId);
            pst = this.connection.prepareStatement(req);
            pst.setString(1, product.getName());
            pst.setInt(2, product.getImg().getId());
            pst.setString(3, product.getDescription());
            pst.setInt(4, product.getPrice());
            pst.setBoolean(5, true);
            pst.setInt(6, product.getTva());
            pst.setInt(7, product.getGender());
            pst.setInt(8, product.getAge());
            pst.setInt(9, product.getCategory().getId());
            pst.setInt(10, Main.user.getId());
            pst.setInt(11, product.getQuantite());
            pst.executeUpdate();
            product.getImg().moveToServer();
            System.out.println("c bon");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ObservableList<Product> showProducts() {
        ObservableList<Product> myList = FXCollections.observableArrayList();
        String req = "SELECT * FROM `product`";
        try {
            PreparedStatement pst = DBConnection.getInstance().getConnection().prepareStatement(req);
            ResultSet rs = pst.executeQuery(req);

            while (rs.next()) {
                Product product = new Product();
                product.setId(rs.getInt("id"));
                product.setName(rs.getString("name"));
                product.setDescription(rs.getString("description"));
                product.setPrice(rs.getInt("price"));
                product.setQuantite(rs.getInt("quantite"));
                product.setProviderId(rs.getInt("user_id"));
                product.setAge(rs.getInt("age"));
                int categoryId = rs.getInt("idCategory");
                Category c = null;
                if (categoryId != 0) {
                    c = new CategoryService().findCategory(categoryId);
                }
                product.setCategory(c);
                product.setAvailable(rs.getBoolean("available"));
                product.setTva(rs.getInt("tva"));
                product.setGender(rs.getInt("gender"));
                int photoId = rs.getInt("img_id");
                Photo p = null;
                if (photoId != 0) {
                    p = new PhotoService().findImage(photoId);
                }
                product.setImg(p);
                myList.add(product);

            }
        } catch (SQLException ex) {
            System.out.println("Error" + ex.getMessage());
        }
        return myList;

    }

    public Product moreProduct(int id) {
        Product p = null;
        try {
            String sql = "SELECT * FROM product WHERE id =" + id;
            Statement stmt = this.connection.createStatement();
            Integer photoId = 0;
            Integer categoryId = 0;
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                p = new Product();
                p.setId(rs.getInt("id"));
                p.setDescription(rs.getString("description"));
                p.setName(rs.getString("name"));
                p.setPrice(rs.getInt("price"));
                photoId = rs.getInt("img_id");
                categoryId = rs.getInt("idCategory");

                Photo pO = null;
                if (photoId != 0) {
                    pO = new PhotoService().findImage(photoId);
                }
                p.setImg(pO);
                Category c = null;
                if (categoryId != 0) {
                    c = new CategoryService().findCategory(categoryId);
                }
                 p.setCategory(c);

            }

        } catch (SQLException ex) {
            Logger.getLogger(ProductServices.class.getName()).log(Level.SEVERE, null, ex);
        }

        return p;

    }

    public int findquantite(int id) {
        String sql = "SELECT quantite FROM product WHERE id = '" + id + "'";
        int qtt = 0;
        try {
            Statement stmt = this.connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                qtt = rs.getInt("quantite");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return qtt;
    }
 public void DeleteBabysitter(int id) throws SQLException {

        String req="Delete From  product where id=(?)";
            PreparedStatement prepared= this.connection.prepareStatement(req);
            prepared.setInt(1,id);
            prepared.executeUpdate();

    }
 
    public Product search(int id){
        String sql = "SELECT * FROM product WHERE id = "+ id ;

        Product p = null;
        try {
            Statement stm = this.connection.createStatement();
            ResultSet rs = stm.executeQuery(sql);
            while(rs.next()){
                p = new Product();
                p.setId(rs.getInt("id"));
                p.setName(rs.getString("name"));
                p.setPrice(rs.getInt("price"));
                p.setQuantite(rs.getInt("quantite"));
                p.setProviderId(rs.getInt("user_id"));
                p.setDescription(rs.getString("description"));
                p.setGender(rs.getInt("gender"));
                p.setAge(rs.getInt("age"));
                p.setTva(rs.getInt("tva"));
                int categoryId = rs.getInt("idCategory");
                Category c = null;
                if(categoryId != 0){
                    c = new CategoryService().findCategory(categoryId);
                }
                p.setCategory(c);
                
                Integer iconId = rs.getInt("img_id");
                Photo icon = null;
                if (iconId != 0)
                    icon = new PhotoService().findImage(iconId);
                p.setImg(icon);
            
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return p;
    }
      public void updateProduct(Product p){
        String photoText = "";
        String sqlPhoto = "INSERT INTO photos (url, alt) " +
                "VALUES (?,?)";
             
        try {
            System.out.println(p.getImg());
            PreparedStatement ps = null;
            if(p.getImg()!= null){
                ps = this.connection.prepareStatement(sqlPhoto, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, p.getImg().getUrl());
                ps.setString(2, p.getImg().getAlt());
                ps.executeUpdate();
                int photoId = 0;
                ResultSet rs = ps.getGeneratedKeys();
                while (rs.next()){
                    photoId = rs.getInt(1);
                }
                System.out.println();
                p.getImg().setId(photoId);
                photoText = ",img_id=? " ;
            }
             String sql =
                "UPDATE product " +
                        "SET name=?, description=?, price=?, tva=?, gender=? ,age=? ,idCategory=?, quantite=? " + photoText +
                        "WHERE product.id=? ";
            System.out.println(sql);
            ps = this.connection.prepareStatement(sql);
            ps.setString(1, p.getName());
            ps.setString(2,p.getDescription());
            ps.setInt(3, p.getPrice());
            ps.setInt(4, p.getTva());
            ps.setInt(5, p.getGender());
            ps.setInt(6, p.getAge());
            ps.setInt(7, p.getCategory().getId());
            ps.setInt(8, p.getQuantite());
            if(p.getImg()!= null){
                ps.setInt(9, p.getImg().getId());
                ps.setInt(10,p.getId());
                p.getImg().moveToServer();
            }else
            ps.setInt(9,p.getId());
            ps.executeUpdate();      
            System.out.println("le produit a été modifié avec succes");
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    
}
