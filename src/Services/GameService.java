package Services;

import Entities.Category;
import Entities.ChildGame;
import Entities.Game;
import Entities.Photo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
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
        String sql =
                "UPDATE game " +
                "SET icon_id=?, name=?, url=?, age=?, device=?, category_id=?, gender=? " +
                "WHERE game.id = ?";
        try {
            PreparedStatement ps = this.connection.prepareStatement(sql);
            ps.setInt(1, g.getIcon().getId());
            ps.setString(2, g.getName());
            ps.setString(3, g.getUrl());
            ps.setInt(4, g.getAge());
            ps.setString(5, g.getDevice());
            ps.setInt(6, g.getCategory().getId());
            ps.setInt(7, g.getGender());
            ps.setInt(8, g.getId());
            ps.executeUpdate();
            System.out.println("Le jeu a été modifié avec succes");
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
        ObservableList<Game> games = FXCollections.observableArrayList();
        try {
            Statement stm = connection.createStatement();
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return games;
    }

    public void saveGame(int childId, int gameId, long time) {
        String sql = "INSERT INTO child_game (child_id, game_id, date, duration)" +
                "  VALUES (?, ?, ? ,?)";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, childId);
            ps.setInt(2, gameId);
            java.util.Date current = new java.util.Date();
            ps.setDate(3, new Date(current.getTime()));
            TimeZone.setDefault(TimeZone.getTimeZone("GMT"));
            ps.setTime(4, new Time(time));
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
}
