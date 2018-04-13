/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Services;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import Entities.*;
import java.sql.Date;
import java.sql.PreparedStatement;

import java.util.Map;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 *
 */
public class LigneCommandeService extends Service {

    /*public Produit selectPrix(){
        Produit p= null ; 
        try {
            ResultSet result = st.executeQuery("select prix_produit from produits join LigneCommandes on LignesCommandes.id_produit=produits.id_produit ") ; 
            //result.next() ;
              p = new Produit(result.getInt("id_produit"),result.getFloat("prix_produit")) ;  
       } catch (SQLException ex) {
            System.out.println(ex) ;
        }
        return p ; 
    }*/

 /* public void insertPrix(Produit p1) {
       String req = "update ligneCommandes set prix_produit = '" +p1.getPrix_produit()+ "' where id_produit = '" +p1.getId_produit()+ "' " ; 
        //String req = "insert into ligneCommandes(prix_produit) values ('" +p1.getPrix_produit()+ "') where id_produit='2'";
        //System.out.println(req);
        try {
            st.executeUpdate(req);
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }*/
    public void insert(LigneCommande p) {
        String req = "insert into product_order(product_id,price,quantity,user_id, order_id, date, provider_id) "
                + "values (?,?,?,?,?,?,?)";
        try {
            PreparedStatement ps = this.connection.prepareStatement(req);
            ps.setInt(1, p.getProduct().getId());
            ps.setDouble(2, p.getPrice());
            ps.setInt(3, p.getQuantite());
            ps.setInt(4, p.getUserID());
            ps.setInt(5, p.getCommandeId());
            ps.setDate(6, new Date(p.getDate().getTime()));
            ps.setInt(7, p.getProviderId());
            ps.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }

    public ObservableList<LigneCommande> getAll() {
        ObservableList<LigneCommande> list = FXCollections.observableArrayList();
        try {
            Statement st = connection.createStatement();
            ResultSet result = st.executeQuery("select * from product_order");

            while (result.next()) {
                LigneCommande p = new LigneCommande();
                p.setId(result.getInt("id"));
                int productId = result.getInt("product_id");
                Product pr = null;
                if (productId != 0) {
                    pr = new ProductServices().search(productId);
                }
                p.setProduct(pr);
                p.setPrice(result.getFloat("price"));
                p.setQuantite(result.getInt("quantity"));
                list.add(p);
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return list;
    }

    public ObservableList<LigneCommande> seachId(int id) {
        ObservableList<LigneCommande> list = FXCollections.observableArrayList();
        try {
            Statement st = this.connection.createStatement();
            ResultSet result = st.executeQuery("select * from product_order where id=" + id);

            while (result.next()) {
                LigneCommande p = new LigneCommande(result.getInt("id"), result.getInt("product_id"), result.getFloat("price"), result.getInt("quantity"));
                list.add(p);
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return list;
    }

    public ObservableList<LigneCommande> seachCategorie(String c) {
        ObservableList<LigneCommande> list = FXCollections.observableArrayList();
        try {
            Statement st = this.connection.createStatement();
            ResultSet result = st.executeQuery("select * from product_order join product on product_order.product_id = product.id where product.categorie like '" + c + "' ");

            while (result.next()) {
                LigneCommande p = new LigneCommande(result.getInt("id"), result.getInt("product_id"), result.getFloat("price"), result.getInt("quantity"));
                list.add(p);
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return list;
    }

    public LigneCommande search(int id) {
        LigneCommande p = null;
        String req = "select * from product_order where id=" + id;
        try {
            Statement st = this.connection.createStatement();
            ResultSet result = st.executeQuery(req);
            result.next();
            p = new LigneCommande(result.getInt("id"), result.getInt("product_id"), result.getFloat("price"), result.getInt("quantity"));
        } catch (SQLException ex) {
            Logger.getLogger(LigneCommandeService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return p;
    }

    /* public LigneCommande searchId(int id) {
        LigneCommande p = null ; 
    String req = "select * from ligneCommandes where id_produit="+id ; 
    try{
        ResultSet result = st.executeQuery(req) ; 
        result.next() ; 
        p = new LigneCommande(result.getInt("id_ligne"),result.getInt("id_produit"), result.getFloat("prix_produit"), result.getInt("quantite")) ; 
    }   catch (SQLException ex) {
            Logger.getLogger(LigneCommandeService.class.getName()).log(Level.SEVERE, null, ex);
        }
    return p ; 
    }*/
    public boolean delete(int id) {
        LigneCommande p = search(id);
        if (p != null) {
            try {
                Statement st = this.connection.createStatement();
                st.executeUpdate("delete from product_order where id=" + id);
                return true;
            } catch (SQLException ex) {
                Logger.getLogger(LigneCommandeService.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        return false;
    }

    public boolean update(LigneCommande p) {
        LigneCommande p1 = search(p.getId());
        if (p1 != null) {
            try {
                Statement st = this.connection.createStatement();
                st.executeUpdate("update product_order set product_id='" + p.getProduct().getId()+ "', price='" + p.getPrice()+ "', quantity='" + p.getQuantite() + "' where id=" + p.getId());
            } catch (SQLException ex) {
                Logger.getLogger(LigneCommandeService.class.getName()).log(Level.SEVERE, null, ex);
            }
            return true;
        }
        return false;
    }

    public Map<String, LigneCommande> getAllMap() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public LigneCommande getbyPseudo(String pseudo) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
