package Services;

import Entities.Category;
import Entities.ChildGame;
import Entities.Game;
import Entities.Photo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.chart.XYChart;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class GameService extends Service {

    public void addGame(Game g){

        String sqlPhoto = "INSERT INTO photos (url, alt) " +
                "VALUES (?,?)";

        String sql =
                "INSERT INTO game (icon_id, name, url, age, device, category_id, gender) " +
                "VALUES (?,?,?,?,?,?,?)";
        try {
            PreparedStatement ps = this.connection.prepareStatement(sqlPhoto, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, g.getIcon().getUrl());
            ps.setString(2, g.getIcon().getAlt());
            ps.executeUpdate();
            int photoId = 0;
            ResultSet rs = ps.getGeneratedKeys();
            while (rs.next()){
                photoId = rs.getInt(1);
            }
            g.getIcon().setId(photoId);
            ps = this.connection.prepareStatement(sql);
            ps.setInt(1, photoId);
            ps.setString(2, g.getName());
            ps.setString(3, g.getUrl());
            ps.setInt(4, g.getAge());
            ps.setString(5, g.getDevice());
            ps.setInt(6, g.getCategory().getId());
            ps.setInt(7, g.getGender());
            ps.executeUpdate();
            g.getIcon().moveToServer();
            System.out.println("Le jeu a été ajouté avec succes");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateGame(Game g){
        String photoText = "";
        String sqlPhoto = "INSERT INTO photos (url, alt) "
                + "VALUES (?,?)";
        
        try {
            PreparedStatement ps = null;
            if (g.getIcon() != null) {
                ps = this.connection.prepareStatement(sqlPhoto, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, g.getIcon().getUrl());
                ps.setString(2, g.getIcon().getAlt());
                ps.executeUpdate();
                int photoId = 0;
                ResultSet rs = ps.getGeneratedKeys();
                while (rs.next()) {
                    photoId = rs.getInt(1);
                }
                System.out.println();
                g.getIcon().setId(photoId);
                photoText = ",image_id=? ";
            }

            String sql
                    = "UPDATE game "
                    + "SET name=?, url=?, age=?, device=?, category_id=?, gender=?, id=?" + photoText
                    + "WHERE game.id = ?";
            PreparedStatement ps1 = this.connection.prepareStatement(sql);
            ps1.setString(1, g.getName());
            ps1.setString(2, g.getUrl());
            ps1.setInt(3, g.getAge());
            ps1.setString(4, g.getDevice());
            ps1.setInt(5, g.getCategory().getId());
            ps1.setInt(6, g.getGender());
            ps1.setInt(7, g.getId());
            if (g.getIcon() != null) {
                ps.setInt(8, g.getIcon().getId());
                ps.setInt(9, g.getId());
                g.getIcon().moveToServer();
            } else {
                ps.setInt(9, g.getId());
            }
            ps1.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Game findGame(int id){
        String sql = "SELECT * FROM Game WHERE id = "+ id ;

        Game g = null;
        try {
            Statement stm = this.connection.createStatement();
            ResultSet rs = stm.executeQuery(sql);
            while(rs.next()){
                g = new Game();
                g.setId(rs.getInt("id"));
                g.setAge(rs.getInt("age"));
                g.setDevice(rs.getString("device"));
                int categoryId = rs.getInt("category_id");
                Category c = null;
                if(categoryId != 0){
                    c = new CategoryService().findCategory(categoryId);
                }
                g.setCategory(c);
                g.setGender(rs.getInt("gender"));
                Integer iconId = rs.getInt("icon_id");
                Photo icon = null;
                if (iconId != 0)
                    icon = new PhotoService().findImage(iconId);
                g.setIcon(icon);
                g.setName(rs.getString("name"));
                g.setUrl(rs.getString("url"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return g;
    }

    public ObservableList<Game> findAll(){
        String sql = "SELECT * FROM game LIMIT 30";
        ObservableList<Game> games = null;
        try {
            games = getList(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return games;
    }

    public ObservableList<Game> findAllowedGames(int childId){
        String blocked_games = "";
        String sql = "SELECT * FROM game ";
        ObservableList<Game> games = FXCollections.observableArrayList();
        try {
            Statement stm = connection.createStatement();
            ResultSet rs = stm.executeQuery("SELECT  blocked_g FROM child WHERE child.id = "+ childId);
            while (rs.next()){
                blocked_games = rs.getString(1);
            }
            if (blocked_games != ""){
                String sqlBlocked = "WHERE id NOT IN (" + blocked_games + ") ";
                sql += sqlBlocked;
            }
            sql += "LIMIT 30";
            games = getList(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return games;
    }

    private ObservableList<Game> getList(String sql) throws SQLException{
        ObservableList<Game> games = FXCollections.observableArrayList();
        Statement stm = this.connection.createStatement();
        ResultSet rs = stm.executeQuery(sql);
        while(rs.next()){
            Game g = new Game();
            g.setId(rs.getInt("id"));
            g.setAge(rs.getInt("age"));
            g.setDevice(rs.getString("device"));
            int categoryId = rs.getInt("category_id");
            Category c = null;
            if(categoryId != 0){
                c = new CategoryService().findCategory(categoryId);
            }
            g.setCategory(c);
            g.setGender(rs.getInt("gender"));
            Integer iconId = rs.getInt("icon_id");
            Photo icon = null;
            if (iconId != 0)
                icon = new PhotoService().findImage(iconId);
            g.setIcon(icon);
            g.setName(rs.getString("name"));
            g.setUrl(rs.getString("url"));
            games.add(g);
        }
        return games;
    }

    public void saveGame(int childId, int gameId, long time) {
        String sql = "INSERT INTO child_game (child_id, game_id, date, duration)" +
                "  VALUES (?, ?, ? ,?)";
        this.saveAction(childId, gameId, time, sql);
    }

    public ObservableList<ChildGame> getRecent(int childId){
        String sql = "SELECT game.id gid, icon_id, name, url, age, device, url,category_id, gender, cg.id cid, date, duration , child_id " +
                "FROM game LEFT JOIN child_game cg ON cg.game_id = game.id " +
                "WHERE cg.child_id = " + childId + " ORDER BY cg.date DESC ";
        ObservableList<ChildGame> childGames = FXCollections.observableArrayList();
        try {
            Statement stm = connection.createStatement();
            ResultSet rs = stm.executeQuery(sql);
            while(rs.next()){
                ChildGame cg = new ChildGame();
                cg.setId(rs.getInt("cid"));
                cg.setChildId(rs.getInt("child_id"));
                cg.setDate(rs.getDate("date"));
                cg.setDuration(rs.getTime("duration"));
                Game g = new Game();
                g.setId(rs.getInt("gid"));
                g.setAge(rs.getInt("age"));
                g.setDevice(rs.getString("device"));
                int categoryId = rs.getInt("category_id");
                Category c = null;
                if(categoryId != 0){
                    c = new CategoryService().findCategory(categoryId);
                }
                g.setCategory(c);
                g.setGender(rs.getInt("gender"));
                Integer iconId = rs.getInt("icon_id");
                Photo icon = null;
                if (iconId != 0) {
                    icon = new PhotoService().findImage(iconId);
                }
                g.setIcon(icon);
                g.setName(rs.getString("name"));
                g.setUrl(rs.getString("url"));
                cg.setGame(g);
                childGames.add(cg);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return childGames;
    }

    public void blockGame(int childId, int gameId){
        String sql = "Update child SET blocked_g = ? WHERE child.id = ?";
        try {
            String blocked_games = getBlockedGames(childId);
            PreparedStatement ps = this.connection.prepareStatement(sql);
            if (blocked_games != "")
                ps.setString(1, blocked_games +","+ gameId);
            else ps.setString(1, gameId + "");
            ps.setInt(2, childId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getBlockedGames(int id) {
        String blocked_games = "";
        try {
            Statement stm = connection.createStatement();
            ResultSet rs = stm.executeQuery("SELECT  blocked_g FROM child WHERE child.id = " + id);
            blocked_games = null;
            while (rs.next()) {
                blocked_games = rs.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return blocked_games;
    }

    public HashMap<String, Integer> getPlayingStats(int childId){
        String sql = "SELECT child_id, SUM(time_to_sec(duration)) as total, date(child_game.date) as date " +
                "FROM `child_game`\n" +
                "GROUP BY date(child_game.date)" +
                "HAVING child_id = " + childId +
                " LIMIT 7";
        HashMap<String, Integer> stats = new HashMap<>();
        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()){
                stats.put(rs.getDate("date").toString(), rs.getInt("total"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stats;
    }


    public long getTotalTime(int childId){
        String sql = "SELECT SUM(time_to_sec(duration)) AS time, child_id " +
                "FROM child_game " +
                "GROUP BY child_id " +
                "HAVING child_id = "+ childId ;
        long time = 0;
        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while(rs.next()){
                time = rs.getInt("time");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return time;
    }
//
//
//    public XYChart.Series<String, Integer> getTimeStats(){
//        String sqlGame = "SELECT child_id, SUM(time_to_sec(duration)) FROM child_game\n" +
//                "GROUP BY child_id" +
//                "HAVING child_id = 2";
//    }
//

}
