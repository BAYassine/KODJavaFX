package Services;

import Core.DBConnection;

import java.sql.*;
import java.util.TimeZone;

public class Service {

    protected Connection connection;

    public Service(){
        DBConnection dbc = DBConnection.getInstance();
        this.connection = dbc.getConnection();
    }

    public void deleteObject(int id, String tablename){
        String sql = "DELETE FROM " + tablename + " WHERE " + tablename + ".id = ?";
        try {
            PreparedStatement ps = this.connection.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    public void saveAction(int childId, int actionId, long time, String sql) {
        try {
            PreparedStatement ps = this.connection.prepareStatement(sql);
            ps.setInt(1, childId);
            ps.setInt(2, actionId);
            java.util.Date current = new java.util.Date();
            ps.setDate(3, new Date(current.getTime()));
            TimeZone.setDefault(TimeZone.getTimeZone("GMT"));
            ps.setTime(4, new Time(time));
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
