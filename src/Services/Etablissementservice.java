/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Services;

import Entities.School;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.scene.control.Alert;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

/**
 *
 * @author ali
 */
public class Etablissementservice extends Service {
   
    
     private ObservableList<School> etablissement;
   
 
    
    public void addschool(School e){
        String sql =
             "INSERT INTO school (schoolname,address,schoolphone,Xschool,Yschool,schoolmail)"+
                        " VALUES (?,?,?,?,?,?)";
        try {
            PreparedStatement pre = this.connection.prepareStatement(sql);
          pre.setString(1,e.getSchoolname());
            pre.setString(2,e.getAddress());
            pre.setInt(3,e.getSchoolphone());
            pre.setDouble(4,e.getXschool());
            pre.setDouble(5,e.getYschool());                         
            pre.setString(6,e.getSchoolmail());
            pre.executeUpdate();
            System.out.println("L'ecole a été ajouté avec succes");
        } catch (SQLException c) {
            c.printStackTrace();
        }
    }
    
     public ObservableList <School> findAll(){
        String sql = "SELECT * FROM school";
        ObservableList <School> Schools = FXCollections.observableArrayList();
        try {
            Integer photoId = 0;
            PreparedStatement prs = this.connection.prepareStatement(sql);
            ResultSet rs = prs.executeQuery();
            while (rs.next()) {
                School c = new School();
            c.setId(rs.getInt("id"));
            c.setSchoolname(rs.getString("schoolname"));
            c.setAddress(rs.getString("address"));
            c.setSchoolphone(rs.getInt("schoolphone"));
            c.setXschool(rs.getDouble("Xschool"));
            c.setYschool(rs.getDouble("Yschool"));                         
            c.setSchoolmail(rs.getString("schoolmail"));
                Schools.add(c);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return Schools;
     }
     
     
      public School findSchool(int id) {
        String sql = "SELECT * FROM school WHERE id = " + id;
        
        School c = null;
        try {
            Statement stm = this.connection.createStatement();
            ResultSet rs = stm.executeQuery(sql);
            while (rs.next()) {
                c = new School();
                c.setId(rs.getInt("id"));
                c.setSchoolname(rs.getString("schoolname"));
            c.setAddress(rs.getString("address"));
            c.setSchoolphone(rs.getInt("schoolphone"));
            c.setXschool(rs.getDouble("Xschool"));
            c.setYschool(rs.getDouble("Yschool"));                         
            c.setSchoolmail(rs.getString("schoolmail"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return c;
    }
     
     
     public void updateSchool(School g) {
       

        try {
            PreparedStatement ps = null;
         
            String sql
                    = "UPDATE school "
                    + "SET schoolname=?, address=?, schoolphone=?, Xschool=?, Yschool=?, schoolmail=?" + ""
                    + "WHERE id = ?";
            System.out.println(sql);
            ps = this.connection.prepareStatement(sql);
            ps.setString(1, g.getSchoolname());
            ps.setString(2, g.getAddress());
            ps.setInt(3, g.getSchoolphone());
            ps.setDouble(4, g.getXschool());
            ps.setDouble(5, g.getYschool());
            ps.setString(6, g.getSchoolmail());
            ps.setInt(7, g.getId());
            ps.executeUpdate();
            System.out.println("Le teacher a Ã©tÃ© modifiÃ© avec succes");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    
     
    
}




   
  

