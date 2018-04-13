/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import java.sql.Timestamp;
import java.util.Date;

/**
 *
 * @author Molka
 */
public class LigneCommande {
    
    private int id;
    private int commandeId;
    private Product product;
    private double price; 
    private int userID;
    private int providerId;
    private Date  date;
    private int quantite;

    public LigneCommande(int quantite) {
        this.quantite = quantite;
    }
    
    
    

    public LigneCommande(int id, int commandeId, Product product, double price, int userID, int providerId,  Date date, int quantite) {
        this.id = id;
        this.commandeId = commandeId;
        this.product = product;
        this.price = price;
        this.userID = userID;
        this.providerId = providerId;
        this.date = date;
        this.quantite = quantite;
    }

    public LigneCommande() {
        
    }

    public LigneCommande(int aInt, int aInt0, float aFloat, int aInt1) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public LigneCommande(Product p, int i) {
        this.product = p;
        this.quantite = i;
        this.price = (float) i * p.getPrice();
        this.date = new Date();
    }

    public LigneCommande(int id, int price, int quantite) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCommandeId() {
        return commandeId;
    }

    public void setCommandeId(int commande) {
        this.commandeId = commande;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getProviderId() {
        return providerId;
    }

    public void setProviderId(int providerId) {
        this.providerId = providerId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate( Date date) {
        this.date = date;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }
    
    
    
}
