/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Services;

import Core.DBConnection;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import Entities.Note;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Molka
 */
public class NoteService implements IAllForKids<Note>{
    
    Connection connexion;
    Statement st;
    ResultSet result;
     static NoteService instance;
 public static NoteService getInstance()
    {
    if(instance==null)
    {
    instance = new NoteService();}
    return instance;
    
    }
 
public NoteService() 
{
    connexion=DBConnection.getInstance().getConnection();
    try {
        st=connexion.createStatement();
    } catch (SQLException ex) {
        Logger.getLogger(NoteService.class.getName()).log(Level.SEVERE, null, ex);
    }
}

    @Override
    public void insert(Note p) {
         String req = "insert into notes(id_parent,id_produit,nb_etoiles) values ('" + p.getId_parent()+ "','" + p.getId_produit()+ "','" + p.getNb_etoiles()+ "')";
        //System.out.println(req);
        try {
            st.executeUpdate(req);
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }

    @Override
    public List<Note> getAll() {
         List<Note> list = new ArrayList<>();
        try {
            result = st.executeQuery("select * from notes");
           /* ResultSetMetaData resultMeta = result.getMetaData() ; 
            System.out.println("**********");
            for(int i = 1 ; i<= resultMeta.getColumnCount() ; i++){
                System.out.println(resultMeta.getColumnName(i).toUpperCase());
            }
            System.out.println("***********"); */
            while (result.next()) {
                Note p = new Note(result.getInt("id_note"),result.getInt("id_parent"), result.getInt("id_produit"), result.getString("nb_etoiles"));
                list.add(p);
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return list;
    }

    @Override
    public Note search(int id) {
        Note p = null ; 
    String req = "select * from notes where id_note="+id ; 
    try{
        result = st.executeQuery(req) ; 
        result.next() ; 
        p = new Note(result.getInt("id_note"),result.getInt("id_parent"), result.getInt("id_produit"), result.getString("nb_etoiles")) ; 
    }   catch (SQLException ex) {
            Logger.getLogger(NoteService.class.getName()).log(Level.SEVERE, null, ex);
        }
    return p ; 
    }

    @Override
    public boolean delete(int id) {
        Note p=search(id);
   if(p!=null)
   {
       try {
           st.executeUpdate("delete from notes where id_note="+id);
            return true;
       } catch (SQLException ex) {
           Logger.getLogger(NoteService.class.getName()).log(Level.SEVERE, null, ex);
       }
  
   }return false; 
    }

    @Override
    public boolean update(Note p) {
        Note p1 = search(p.getId_note()) ; 
    if(p1!=null){
        try{
            st.executeUpdate("update notes set id_parent='"+p.getId_parent()+"', id_produit='"+p.getId_produit()+"', nb_etoiles='"+p.getNb_etoiles()+"' where id_note="+p.getId_note()) ; 
        } catch (SQLException ex) {
            Logger.getLogger(NoteService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true ; 
    }
    return false ; 
    }

    @Override
    public Map<String, Note> getAllMap() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Note getbyPseudo(String pseudo) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    public ObservableList<Note> getAllById(int id) {
       ObservableList<Note> list = FXCollections.observableArrayList();
        try {
            result = st.executeQuery("select * from notes where id_produit="+id);
           /* ResultSetMetaData resultMeta = result.getMetaData() ; 
            System.out.println("**********");
            for(int i = 1 ; i<= resultMeta.getColumnCount() ; i++){
                System.out.println(resultMeta.getColumnName(i).toUpperCase());
            }
            System.out.println("***********"); */
            while (result.next()) {
                Note p = new Note(result.getInt("id_note"),result.getInt("id_parent"), result.getInt("id_produit"), result.getString("nb_etoiles"));
                list.add(p);
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return list;
    }

    
    
}
