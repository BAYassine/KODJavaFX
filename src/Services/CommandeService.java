package Services;

import Controllers.Admin.ProduitsController;
import Core.Main;
import Entities.ChildGame;
import Entities.Commande;
import Entities.Game;
import Entities.LigneCommande;
import Entities.Photo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CommandeService extends Service {

    public void addOrder(Commande c ){

        String sqlOrder = "INSERT INTO orders (user_id,confirmed,date,reference,total,token) " +
                "VALUES (?,?,?,?,?,?)";

        String sql =
                "INSERT INTO product_order(product_id,price,quantity,user_id, order_id, date, provider_id) " +
                "VALUES (?,?,?,?,?,?,?)";
        try {
            PreparedStatement ps = this.connection.prepareStatement(sqlOrder, Statement.RETURN_GENERATED_KEYS);

              ps.setInt(1, c.getUser_id());
              ps.setInt(2, 0);
              ps.setDate(3, new Date(c.getDate().getTime()));
              ps.setInt(4, 0);
              ps.setInt(5, (int) c.getTotal());
              ps.setString(6, "a");
              ps.executeUpdate();
            int order_id = 0;
            ResultSet rs = ps.getGeneratedKeys();
            while (rs.next()){
                order_id = rs.getInt(1);
            }
            for (LigneCommande lc : Main.panier)
            {
                lc.setCommandeId(order_id);
                ps = this.connection.prepareStatement(sql);
                ps.setInt(1, lc.getProduct().getId());
                ps.setDouble(2, lc.getPrice());
                ps.setInt(3, lc.getQuantite());
                ps.setInt(4, lc.getUserID());
                ps.setInt(5, order_id);
                ps.setDate(6, new Date(lc.getDate().getTime()));
                ps.setInt(7, lc.getProviderId());
                ps.executeUpdate();

                ps.executeUpdate();
            }
            System.out.println("La commande est ajouté avec succes");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    

    

    public ObservableList<Commande> findAll(){
        String sql = "SELECT id,total,date from orders";
        ObservableList<Commande> commandes = FXCollections.observableArrayList();
        try {
            Statement stm = connection.createStatement();
            ResultSet rs = stm.executeQuery(sql);
            while(rs.next()){
                Commande c = new Commande();
               c.setId(rs.getInt("id"));
            
               c.setTotal(rs.getFloat("total")); 
               c.setDate(rs.getDate("date"));
               
                commandes.add(c);
                
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
      
        return commandes;
    }

 
    public  float getTotal(int id)
    {
        float total = 0;   
        String sql = "select total from orders where user_id = ?";
      
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
               ps.setFloat(1, id);
               ResultSet rs  = ps.executeQuery();
               while(rs.next())
               {
                   total=rs.getFloat("total");
               }
        } catch (SQLException ex) {
            Logger.getLogger(CommandeService.class.getName()).log(Level.SEVERE, null, ex);
        }
return total;
    }
    
}
