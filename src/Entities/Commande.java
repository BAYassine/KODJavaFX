/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import java.sql.Date;
import java.sql.Timestamp;

/**
 *
 * @author abir
 */
public class Commande {
    
    private int id ; 
    private int user_id ; 
    private float total ; 
    private Date date ; 
    private int reference;
    
    public Commande(int user_id,Date date,  float total) {
        this.user_id = user_id;
          this.date = date;
        this.total = total;
      
       
    }

    public Commande() {
    }
    

    public int getReference() {
        return reference;
    }

    public Commande(int user_id, float total) {
        this.user_id = user_id;
        this.total = total;
    }

    public void setReference(int reference) {
        this.reference = reference;
    }

    public Commande(int id, int user_id, float total, Date date) {
        this.id = id;
        this.user_id = user_id;
        this.total = total;
        this.date = date;
    }
 
    
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public  Date getDate() {
        return date;
    }

    public void setDate( Date date) {
        this.date = date;
    }

   
    
    
    
    
}
