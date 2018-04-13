/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import java.util.Date;

/**
 *
 * @author ASSUS
 */
public class Commentaire {
    
    private int id_commentaire ; 
    private Date created_at;
      private int state;
    private Thread idP ; 
    private String body ; 
    private int id_user;

    public Thread getIdP() {
        return idP;
    }

    public void setIdP(Thread idP) {
        this.idP = idP;
    }
  

    public String getAncestors() {
        return ancestors;
    }

    public void setAncestors(String ancestors) {
        this.ancestors = ancestors;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }
    private String ancestors;
    private int depth;

    public Commentaire(int id_commentaire, Date created_at, int state, Thread id_produit, String body, int id_user) {
        this.id_commentaire = id_commentaire;
        this.created_at = created_at;
        this.state = state;
        this.idP = id_produit;
        this.body = body;
        this.id_user = id_user;
    }

    public Commentaire( int id_user,int state, Thread  id_produit, String body,Date created_at) {
        this.created_at = created_at;
        this.state = state;
        this.idP= id_produit;
        this.body = body;
        this.id_user = id_user;
    }

    public Commentaire() {
   
    }

    public Commentaire(int aInt, int aInt0, int aInt1, String string) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


    public int getId_commentaire() {
        return id_commentaire;
    }

    public void setId_commentaire(int id_commentaire) {
        this.id_commentaire = id_commentaire;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }


    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    @Override
    public String toString() {
        return body;
    }
    
}
