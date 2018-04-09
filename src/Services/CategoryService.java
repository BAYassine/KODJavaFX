/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Services;

import Entities.Category;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author Meriem
 */
public class CategoryService extends Service {

    public Category findCategory(int id) {
        String sql = "SELECT * FROM category WHERE id = " + id;
        Category c = null;
        try {
            Statement stm = this.connection.createStatement();
            ResultSet rs = stm.executeQuery(sql);
            while (rs.next()) {
                c = new Category();
                c.setId(rs.getInt("id"));
                c.setName(rs.getString("name"));
                c.setType(rs.getString("type"));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return c;
    }

    public ObservableList<Category> findCategory(String type) {
        String sql = "SELECT * FROM category WHERE type = '" + type + "'";
        ObservableList<Category> categories = FXCollections.observableArrayList();
        try {
            Statement stmt = this.connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Category c = new Category();
                c.setId(rs.getInt("id"));
                c.setName(rs.getString("name"));
                c.setType(rs.getString("type"));
                categories.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categories;
    }

    public Category findByName(String name) throws SQLException {
        Category c = new Category();
        String sql = "SELECT * FROM category WHERE name = '" + name + "'";
        Statement stmt = this.connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            c.setId(rs.getInt("id"));
            c.setName(rs.getString("name"));
            c.setType(rs.getString("type"));
        }
        return c;
    }

}
