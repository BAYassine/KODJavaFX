/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Services;

import Entities.Photo;
import Entities.Subject;
import Entities.Teacher;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author ali
 */
public class TeachersService extends Service {

    public void addTeacher(Teacher t) {

        String sqlPhoto = "INSERT INTO photos (url, alt) "
                + "VALUES (?,?)";

        String req = "INSERT INTO `teacher`(`subject_id`, `photo_id`, `price`, `name`, `degree`, `account`, `hobbies`, `experience`, `phone`, `lastname`) "
                + "VALUES (?,?,?,?,?,?,?,?,?,?)";
        try {
            PreparedStatement pst = this.connection.prepareStatement(sqlPhoto, Statement.RETURN_GENERATED_KEYS);
            pst.setString(1, t.getPhoto().getUrl());
            pst.setString(2, t.getPhoto().getAlt());
            pst.executeUpdate();
            int photoId = 0;
            ResultSet rs = pst.getGeneratedKeys();
            while (rs.next()) {
                photoId = rs.getInt(1);
            }
            t.getPhoto().setId(photoId);
            pst = this.connection.prepareStatement(req);
            pst.setInt(1, t.getSubject().getId());
            pst.setInt(2, t.getPhoto().getId());
            pst.setDouble(3, t.getPrice());
            pst.setString(4, t.getName());
            pst.setString(5, t.getDegree());
            pst.setString(6, t.getAccount());
            pst.setString(7, t.getHobbies());
            pst.setString(8, t.getExperience());
            pst.setInt(9, t.getPhone());
            pst.setString(10, t.getLastname());
            pst.executeUpdate();
            t.getPhoto().moveToServer();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateteacher(Teacher t) {
        String sql
                = "UPDATE teacher "
                + "SET subject_id=?, photo_id=?, price=?, name=?, degree=?, account=? , hobbies=? , experience=?, phone=?, lastname=?  "
                + "WHERE teacher.id = ?";
        try {
            PreparedStatement pst = this.connection.prepareStatement(sql);
            pst.setInt(1, t.getSubject().getId());
            pst.setInt(2, t.getPhoto().getId());
            pst.setDouble(3, t.getPrice());
            pst.setString(4, t.getName());
            pst.setString(5, t.getDegree());
            pst.setString(6, t.getAccount());
            pst.setString(7, t.getHobbies());
            pst.setString(8, t.getExperience());
            pst.setInt(9, t.getPhone());
            pst.setString(10, t.getLastname());
            pst.executeUpdate();
            System.out.println("Le jeu a été modifié avec succes");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Teacher findTeacher(int id) {
        String sql = "SELECT * FROM teacher WHERE id = " + id;
        Integer photoId = null;
        Teacher c = null;
        try {
            Statement stm = this.connection.createStatement();
            ResultSet rs = stm.executeQuery(sql);
            SubjectService ss= new SubjectService();
            while (rs.next()) {
                c = new Teacher();
                c.setId(rs.getInt("id"));
                c.setAccount(rs.getString("account"));
                c.setName(rs.getString("name"));
                c.setExperience(rs.getString("experience"));
                c.setHobbies(rs.getString("hobbies"));
                c.setLastname(rs.getString("lastname"));
                c.setPrice(rs.getDouble("price"));
                c.setPhone(rs.getInt("phone"));
                c.setSubject(ss.findSubject(rs.getInt("subject_id")));
                
                c.setDegree(rs.getString("degree"));
                
                
                photoId = rs.getInt("photo_id");

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

    public ObservableList<Teacher> findAll() {
        String sql = "SELECT * FROM teacher";
        ObservableList<Teacher> teachers = FXCollections.observableArrayList();
        try {
            Integer photoId = 0;
            PreparedStatement prs = this.connection.prepareStatement(sql);
            ResultSet rs = prs.executeQuery();
            while (rs.next()) {
                Teacher c = new Teacher();
                c.setId(rs.getInt("id"));
                c.setPrice(rs.getFloat("price"));
                c.setName(rs.getString("Name"));
                c.setDegree(rs.getString("degree"));
                c.setAccount(rs.getString("account"));
                c.setHobbies(rs.getString("hobbies"));
                c.setLastname(rs.getString("Lastname"));
                c.setPhone(rs.getInt("phone"));
                int subjectId = rs.getInt("subject_id");
                photoId = rs.getInt("photo_id");
                Photo p = null;
                Subject s = null;
                if (photoId != 0) {
                    p = new PhotoService().findImage(photoId);
                }
                if (subjectId != 0) {
                    s = new SubjectService().findSubject(subjectId);
                }
                c.setPhoto(p);
                c.setSubject(s);
                teachers.add(c);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return teachers;
    }

    public void updateTeacher(Teacher c) {
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
                photoText = ",photo_id=? ";
            }
            String sql
                    = "UPDATE teacher "
                    + "SET subject_id=?, price=?, name=?, account=?, phone=?, degree=?,  experience=?, hobbies=?,lastname=? " + photoText
                    + "WHERE teacher.id = ?";
            System.out.println(sql);
            ps = this.connection.prepareStatement(sql);
            ps.setInt(1, c.getSubject().getId());
            ps.setDouble(2, c.getPrice());
            ps.setString(3, c.getName());
            ps.setString(4, c.getAccount());
            ps.setInt(5, c.getPhone());
            ps.setString(6, c.getDegree());
            ps.setString(7, c.getExperience());
            ps.setString(8, c.getHobbies());
            ps.setString(9, c.getLastname());
            if (c.getPhoto() != null) {
                ps.setInt(10, c.getPhoto().getId());
                ps.setInt(11, c.getId());
                c.getPhoto().moveToServer();
            } else {
                ps.setInt(10, c.getId());
            }
            ps.executeUpdate();
            System.out.println("Le teacher a Ã©tÃ© modifiÃ© avec succes");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
