/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Services;

import Entities.Complaint;
import Entities.Article;
import Entities.Category;
import Entities.Complaint;
import Entities.Photo;
import Entities.User;
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
 * @author Meriem
 */
public class ComplaintService extends Service{
    public void addComplaint( Complaint complaint ) throws SQLException
    {
    String req ="INSERT INTO `complaint`(`user_id`,`category`, `description`, `subject`, `date`, `state`) values (?,?,?,?,?,?)";
        
        PreparedStatement pst = this.connection.prepareStatement(req);
        
      
        pst.setInt(1, complaint.getParent().getId());
        pst.setInt(2, complaint.getCategory().getId());
        pst.setString(3, complaint.getDescription());
        pst.setString(4, complaint.getSubject());
        pst.setDate(5,complaint.getDate());
        pst.setString(6,complaint.getState());
        
        pst.executeUpdate();
        System.out.println("ajouté bb");
    }
    public ObservableList <Complaint> findByUser(int user)
    {    ObservableList<Complaint> complaints = FXCollections.observableArrayList(); 
        try {
       
        String sql = "SELECT * FROM complaint WHERE user_id = "+ user ;
        Complaint c = null;
        Integer parentId= null; 
        Integer categoryId=null;
        Statement stm = this.connection.createStatement();
        ResultSet rs = stm.executeQuery(sql);
        while(rs.next()){
            c = new Complaint();
            c.setId(rs.getInt("id"));
            c.setDescription(rs.getString("description"));
            c.setDate(rs.getDate("date"));
            c.setState(rs.getString("state"));
            c.setSubject(rs.getString("subject"));
            parentId = rs.getInt("user_id");
            categoryId=rs.getInt("category");
            
            User p = null;
            if (parentId != 0) {
                p = new UserService().findUser(parentId);
            }
            c.setParent(p);
            
            Category ca = null;
            if (categoryId != 0) {
                ca = new CategoryService().findCategory(categoryId);
            }
            c.setCategory(ca);
            complaints.add(c);
        }
        
        
        } catch (SQLException ex) {
            Logger.getLogger(ComplaintService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return complaints;
    }
    
     public ObservableList<Complaint> findAll() throws SQLException {
        Integer photoId = 0;
        Integer CategoryId = 0;
        UserService us= new UserService();
        ObservableList<Complaint> complaints = FXCollections.observableArrayList();
        String sql = "SELECT * FROM complaint";
        PreparedStatement prs = this.connection.prepareStatement(sql);
        ResultSet resultat = prs.executeQuery();

        while (resultat.next()) {
            Complaint c = new Complaint();
            c.setId(resultat.getInt("id"));
            //c.setCategory(resultat.getInt("category"));
            c.setDescription(resultat.getString("description"));
            c.setDate(resultat.getDate("date"));
            
            
            c.setParent(us.findUser(resultat.getInt("user_id")));
            c.setState(resultat.getString("state"));
            c.setSubject(resultat.getString("subject"));
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
          
           
            complaints.add(c);
        }
        return complaints;
    }
           public void updateState(Complaint c) {
      
          String sql
                  = "UPDATE complaint "
                  + "SET state=? "
                  + "WHERE complaint.id = ?";
          
          
          try {
              PreparedStatement ps = this.connection.prepareStatement(sql);
              ps.setString(1, c.getState());
              ps.setInt(2, c.getId());
              ps.executeUpdate();
              
              
          } catch (SQLException ex) {
              Logger.getLogger(ArticleService.class.getName()).log(Level.SEVERE, null, ex);
          }
          
       
    }
    
    
    
}
