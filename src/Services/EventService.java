/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Services ;

import Entities.Event;
import Entities.Game;
import Entities.Photo;
import Entities.User;
import java.sql.Date;
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
 * @author Rami
 */
public class EventService extends Service{
    
    
    //public void addEvent()

    public Event findEvent(int id){
        String sql = "SELECT * FROM event WHERE id = "+ id ;

        Event e = null;
        try {
            Statement stm = this.connection.createStatement();
            ResultSet rs = stm.executeQuery(sql);
            while(rs.next()){
                e = new Event();
                e.setId(rs.getInt("id"));
                e.setAge(rs.getInt("age"));
                e.setSubject(rs.getString("subject"));
                e.setType(rs.getString("type"));
                e.setStartDate(rs.getDate("startDate"));
                e.setEndDate(rs.getDate("endDate"));
                e.setLocation(rs.getString("location"));
                e.setNbrParticipants(rs.getInt("nbrParticipants"));
                e.setAvailablePlaces(rs.getInt("availablePlaces"));
                int photoId = rs.getInt("photo_id");
                Photo p = null;
                if (photoId != 0){
                    p = new PhotoService().findImage(photoId);
                }
                e.setPhoto(p);
                Integer userId = rs.getInt("user_id");
                User user = null;
                if (userId != 0)
                    user = new UserService().findUser(userId);
                e.setUser(user);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return e;
    }
    
    //public ObservableList<Event> findAll()
    
    
    
    public void addEvent(Event e) throws SQLException{
        String sql =
                "INSERT INTO event (subject, type, startDate, endDate, location, nbrParticipants, availablePlaces,photo_id, age,user_id) " +
                        "VALUES (?,?,?,?,?,?,?,null,?,1)";
        try {
            PreparedStatement ps = this.connection.prepareStatement(sql);
            ps.setString(1, e.getSubject());
            ps.setString(2, e.getType());
            ps.setDate(3, new java.sql.Date(e.getStartDate().getTime()));
            ps.setDate(4, new java.sql.Date(e.getEndDate().getTime()));
            ps.setString(5, e.getLocation());
            ps.setInt(6, e.getNbrParticipants());
            ps.setInt(7, e.getAvailablePlaces());
            ps.setInt(8, e.getAge());
            System.out.println(" "+e.toString());
            System.out.println(ps.toString());
            //ps.setInt(10, 1);
            ps.toString();
            
            
            ps.executeUpdate();
            System.out.println("L'évènement a été ajouté avec succes");
        } catch (SQLException ee) {
        }
    }
    
    
    
    public void updateEvent(Event e){
        String sql =
                "UPDATE event set " +
                        " subject=?, type=?, startDate=?, endDate=?, location=?, nbrParticipants=?, availablePlaces=?," +
                /*" photo_id=?,"*/" age=?, user_id=1" +
                        " WHERE event.id = ?";
        try {
            PreparedStatement ps = this.connection.prepareStatement(sql);
            
            ps.setString(1, e.getSubject());
            ps.setString(2, e.getType());
            ps.setDate(3, (java.sql.Date) e.getStartDate());
            ps.setDate(4, (java.sql.Date) e.getEndDate());
            ps.setString(5, e.getLocation());
            ps.setInt(6, e.getNbrParticipants());
            ps.setInt(7, e.getAvailablePlaces());
            ps.setInt(8, e.getAge());
            ps.setInt(9, 15);
            ps.executeUpdate();
            
            System.out.println("L'enfant a été modifié avec succes");
        } catch (SQLException ec) {
            ec.printStackTrace();
        }
    }
    
    public ObservableList<Event> findAll(){
        String sql = "SELECT * FROM Event";
        return getList(sql);
    }

    private ObservableList<Event> getList(String sql){
            ObservableList<Event> events = FXCollections.observableArrayList();
        try {
            Statement stm = this.connection.createStatement();
            ResultSet rs = stm.executeQuery(sql);
            while(rs.next()){
                Event e = new Event();
                e.setId(rs.getInt("id"));
                e.setAge(rs.getInt("age"));
                e.setSubject(rs.getString("subject"));
                e.setType(rs.getString("type"));
                e.setStartDate(rs.getDate("startDate"));
                e.setEndDate(rs.getDate("endDate"));
                e.setLocation(rs.getString("location"));
                e.setNbrParticipants(rs.getInt("nbrParticipants"));
                e.setAvailablePlaces(rs.getInt("availablePlaces"));
                int photoId = rs.getInt("photo_id");
                Photo p = null;
                if (photoId != 0){
                    p = new PhotoService().findImage(photoId);
                }
                e.setPhoto(p);
                Integer userId = rs.getInt("user_id");
                User user = null;
                if (userId != 0)
                    user = new UserService().findUser(userId);
                e.setUser(user);
                events.add(e);
            }
        } catch (SQLException ex) {
            Logger.getLogger(EventService.class.getName()).log(Level.SEVERE, null, ex);
        }
            return events;
        
    }
    
    public void addReservation(int id_evenement,int id_user,int part){
        String sql =
                "INSERT INTO reservation (user_id, event_id, participants) " +
                        "VALUES (?,?,?)";
        try {
            PreparedStatement ps = this.connection.prepareStatement(sql);
            ps.setInt(1, id_user);
            ps.setInt(2, id_evenement);
            ps.setInt(3, part);
         
            ps.executeUpdate();
            System.out.println("votre réservation a été ajoutée avec succés");
        } catch (SQLException ee) {
            ee.printStackTrace();
        }
    }
    public void delete(int id_evenement){
        String sql =
                "delete from event where id=?";
        try {
            PreparedStatement ps = this.connection.prepareStatement(sql);
            ps.setInt(1, id_evenement);
     
         
            ps.executeUpdate();
            System.out.println("votre réservation a été ajoutée avec succés");
        } catch (SQLException ee) {
            ee.printStackTrace();
        }
    }
    
}