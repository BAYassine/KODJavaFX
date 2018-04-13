/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Services;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author Rami
 */
public class ReservationService extends Service{
    
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
    
}
