/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Services;

import Core.DBConnection;
import Entities.Category;
import Entities.Thread;
import Entities.Commentaire;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.xml.stream.events.Comment;

/**
 * @author ASUS
 */
public class CommentaireService extends Service {

    public Connection con;
    PreparedStatement ste;
    private Statement ste2;

    public CommentaireService() {

        con = DBConnection.getInstance().getConnection();


    }

    public void AjouterCommentaire(Commentaire commentaire) throws SQLException {
        String req = "INSERT INTO comment(idUser,idP,body,depth,created_at,state,ancestors) VALUES (?,?,?,?,NOW(),0,'')";
        PreparedStatement pre = con.prepareStatement(req);
        pre.setInt(1, commentaire.getId_user());
        pre.setInt(2, Integer.parseInt(commentaire.getIdP().getId()));
        pre.setString(3, commentaire.getBody());
        pre.setInt(4, commentaire.getDepth());
        pre.executeUpdate();
    }


    public List<Commentaire> AfficherCommentaire(String idP) throws SQLException {
        List<Commentaire> commentaires = new ArrayList<>();
        Statement st = connection.createStatement();
        ResultSet rsCommentaire = st.executeQuery("select * from comment where idP='" + idP + "' and ancestors='' and state=0 order by id DESC");

        while (rsCommentaire.next()) {
            Commentaire com = new Commentaire();

            Thread Th = new Thread();

            com.setAncestors(rsCommentaire.getString("ancestors"));
            com.setBody(rsCommentaire.getString("body"));
            com.setCreated_at(rsCommentaire.getDate("created_at"));
            com.setDepth(rsCommentaire.getInt("depth"));
            com.setId_commentaire(rsCommentaire.getInt("id"));
            com.setState(rsCommentaire.getInt("state"));
            com.setState(rsCommentaire.getInt("idUser"));
            Statement st2 = connection.createStatement();
            ResultSet rsThread = st2.executeQuery("select * from thread where id = " + rsCommentaire.getInt("idP"));
            while (rsThread.next()) {
                Th.setId(rsThread.getString("id"));
                Th.setIsCommentable(rsThread.getBoolean("is_commentable"));
                Th.setLastCommentAt(rsThread.getDate("last_comment_at"));
                Th.setNumComments(rsThread.getInt("num_comments"));
                Th.setPermalink(rsThread.getString("permalink"));
            }
            com.setIdP(Th);
            commentaires.add(com);
        }

        return commentaires;
    }

    public int CountCommentaireProduit(int idP) throws SQLException {
        int count = 0;
        Statement ste = connection.createStatement();
        ResultSet rsCommentaire = ste.executeQuery("select count(*) from comment where idP=" + idP);
        while (rsCommentaire.next()) {
            count = rsCommentaire.getInt(1);
        }
        return count;
    }
          /*public ObservableList<Commentaire> showComments() {
    

           ObservableList<Commentaire> myList = FXCollections.observableArrayList();
            String req ="SELECT id,body,created_at,state,idP,idUser from comment";
            try{
                PreparedStatement pst = DBConnection.getInstance().getConnection().prepareStatement(req);
                ResultSet rs = pst.executeQuery(req);

                while(rs.next()){
                   Commentaire c = new Commentaire();
                   c.setId_commentaire(rs.getInt("id"));
                   c.setBody(rs.getString("body"));
                   c.setCreated_at(new Date(rs.getDate("created_at").getTime()));
                   c.setState(rs.getInt("state"));
                   c.setId_produit(rs.Integer.parseInt(c.getId_produit().getId()));
                   c.setId_user(rs.getInt("idUser"));
                    myList.add(c);
                }
            }catch (SQLException ex){
                System.out.println("Error"+ex.getMessage());
            }
            return myList;
            
        } */

    public ObservableList<Commentaire> getAllById(int id) throws SQLException {
        ObservableList<Commentaire> list = FXCollections.observableArrayList();
        Statement st;
        ResultSet result;
        st = connection.createStatement();
        try {
            result = st.executeQuery("select id,idP,idUser,body from comment where id_produit=" + id);

            while (result.next()) {
                Commentaire p = new Commentaire(result.getInt("id"), result.getInt("idP"), result.getInt("idUser"), result.getString("body"));
                list.add(p);
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return list;
    }

    public void UpdateCommentaire(Commentaire commentaire) throws SQLException {
        String req = "UPDATE comment SET body = ? WHERE id = ?";
        PreparedStatement pre = con.prepareStatement(req);
        pre.setString(1, commentaire.getBody());
        pre.setInt(2, commentaire.getId_commentaire());
        pre.executeUpdate();
    }

    public void SupprimerCommentaire(Commentaire commentaire) throws SQLException {
        String req = "UPDATE comment SET state = ? WHERE id = ?";
        PreparedStatement pre = con.prepareStatement(req);
        pre.setInt(1, commentaire.getState());
        pre.setInt(2, commentaire.getId_commentaire());
        pre.executeUpdate();
    }
    
       /*public Comment findComment(int id){
        String sql = "SELECT * FROM comment WHERE id = "+ id ;
        Commentaire c = null;
        try {
           Statement stm = this.connection.createStatement();
            ResultSet rs = stm.executeQuery(sql);
            while(rs.next()){
                c = new Commentaire();
                c.setId_commentaire(rs.getInt("id"));
                c.setId_produit(rs.getInt("idP"));
                c.setId_user(rs.getInt("idUser"));
                c.setBody(rs.getString("body"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return (Comment) c;
    }
      */


}
