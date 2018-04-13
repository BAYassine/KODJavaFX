/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Services;

import Entities.Address;
import Entities.Article;
import Entities.Babysitter;
import Entities.Category;
import Entities.Photo;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Meriem
 */
public class AddressService extends Service {

    public ObservableList<Address> findAll() throws SQLException {
        Integer photoId = 0;
        Integer CategoryId = 0;
        ObservableList<Address> addresses = FXCollections.observableArrayList();
        String sql = "SELECT * FROM address";
        PreparedStatement prs = this.connection.prepareStatement(sql);
        ResultSet resultat = prs.executeQuery();

        while (resultat.next()) {
            Address c = new Address();
            c.setId(resultat.getInt("id"));
            //c.setCategory(resultat.getInt("category"));
            c.setDescription(resultat.getString("description"));
            c.setLocation(resultat.getString("location"));
            c.setName(resultat.getString("name"));
            c.setPhone(resultat.getString("phone"));
            c.setLat(resultat.getFloat("lat"));
            c.setLng(resultat.getFloat("lng"));
            CategoryId = resultat.getInt("category");
            Category cat = null;
            if (CategoryId != 0) {
                String rs = "SELECT * FROM category WHERE id =" + CategoryId;
                prs = this.connection.prepareStatement(rs);
                ResultSet resultat1 = prs.executeQuery();
                while (resultat1.next()) {
                    cat = new Category();
                    cat.setId(CategoryId);
                    cat.setName(resultat1.getString("name"));
                    cat.setType(resultat1.getString("type"));
                }
            }
            c.setCategory(cat);
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
            addresses.add(c);
        }
        return addresses;
    }

    public ObservableList<Address> moreAddress(int id) throws SQLException {
        ObservableList<Address> addresses = FXCollections.observableArrayList();
        Address a = new Address();
        String sql = "SELECT * FROM address WHERE id =" + id;
        Statement stmt = this.connection.createStatement();
        Integer photoId = 0;
        Integer categoryId = 0;
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            a.setId(rs.getInt("id"));
            a.setDescription(rs.getString("description"));
            a.setLocation(rs.getString("location"));
            a.setName(rs.getString("name"));
            a.setPhone(rs.getString("phone"));
            a.setLat(rs.getFloat("lat"));
            a.setLng(rs.getFloat("lng"));
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
            a.setCategory(c);
        }

        addresses.add(a);
        return addresses;
    }

    public void addAddress(Address g) {

        String sqlPhoto = "INSERT INTO photos (url, alt) "
                + "VALUES (?,?)";

        String sql
                = "INSERT INTO `address`(`category`,`description`, `location`, `image_id`, `name`, `phone`, `lat`, `lng`)"
                + "VALUES (?,?,?,?,?,?,?,?)";
        try {
            PreparedStatement ps = this.connection.prepareStatement(sqlPhoto, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, g.getPhoto().getUrl());
            ps.setString(2, g.getPhoto().getAlt());
            ps.executeUpdate();
            int photoId = 0;
            ResultSet rs = ps.getGeneratedKeys();
            while (rs.next()) {
                photoId = rs.getInt(1);
            }
            g.getPhoto().setId(photoId);
            PreparedStatement pst = this.connection.prepareStatement(sql);
            pst.setInt(1, g.getCategory().getId());
            pst.setString(2, g.getDescription());
            pst.setString(3, g.getLocation());
            pst.setInt(4, g.getPhoto().getId());
            pst.setString(5, g.getName());
            pst.setString(6, g.getPhone());
            pst.setFloat(7, g.getLat());
            pst.setFloat(8, g.getLng());
            pst.executeUpdate();

            g.getPhoto().moveToServer();
            System.out.println("Adresse ajoutée avec succès");
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void updateAddress(Address c) {
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
                    = "UPDATE address "
                    + "SET category=?, description=?, location=?, name=?, phone=?, lng=?, lat=? " + photoText
                    + "WHERE address.id = ?";
            System.out.println(sql);
            ps = this.connection.prepareStatement(sql);
            ps.setInt(1, c.getCategory().getId());
            ps.setString(2, c.getDescription());
            ps.setString(3, c.getLocation());
            ps.setString(4, c.getName());
            ps.setString(5, c.getPhone());
            ps.setFloat(6, c.getLng());
            ps.setFloat(7, c.getLat());
            if (c.getPhoto() != null) {
                ps.setInt(8, c.getPhoto().getId());
                ps.setInt(9, c.getId());
                c.getPhoto().moveToServer();
            } else {
                ps.setInt(10, c.getId());
            }
            ps.executeUpdate();
            System.out.println("L'adresse a été modifiée avec succes");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Address findAddress(int id) {
        String sql = "SELECT * FROM address WHERE id = " + id;
        Integer photoId = null;
        Address c = null;
        CategoryService cv= new CategoryService();
        try {
            Statement stm = this.connection.createStatement();
            ResultSet rs = stm.executeQuery(sql);
            while (rs.next()) {
                c = new Address();
                c.setId(rs.getInt("id"));
                c.setCategory(cv.findCategory(rs.getInt("category")));
                c.setDescription(rs.getString("description"));
                c.setName(rs.getString("name"));
                c.setLat(rs.getFloat("lat"));
                c.setLng(rs.getFloat("lng"));
                c.setLocation(rs.getString("location"));
                c.setPhone(rs.getString("phone"));
                photoId = rs.getInt("image_id");

            }
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
            c.setPhoto(p);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return c;
    }

}
