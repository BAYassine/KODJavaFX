package Services;

import Entities.Cartoon;
import Entities.Photo;
import Entities.Video;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.TimeZone;

public class CartoonService extends Service {

    public void addCartoon(Cartoon c){
        String sql =
                "INSERT INTO cartoon (photo_id, name, summary, age, episodesCnt, gender) " +
                        "VALUES (?,?,?,?,?,?)";
        try {
            PreparedStatement ps = this.connection.prepareStatement(sql);
            ps.setInt(1, c.getPhoto().getId());
            ps.setString(2, c.getName());
            ps.setString(3, c.getSummary());
            ps.setInt(4, c.getAge());
            ps.setInt(5, c.getEpisodesCnt());
            ps.setInt(6, c.getGender());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateCartoon(Cartoon c){
        String sql =
                "UPDATE cartoon " +
                        "SET photo_id=?, name=?, summary=?, age=?, gender=? " +
                        "WHERE cartoon.id = ?";
        try {
            PreparedStatement ps = this.connection.prepareStatement(sql);
            ps.setInt(1, c.getPhoto().getId());
            ps.setString(2, c.getName());
            ps.setString(3, c.getSummary());
            ps.setInt(4, c.getAge());
            ps.setInt(5, c.getGender());
            ps.setInt(6, c.getId());
            ps.executeUpdate();
            System.out.println("Le cartoon a été modifié avec succes");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Cartoon findCartoon(int id){
        String sql = "SELECT * FROM cartoon WHERE id = "+ id ;
        Cartoon c = null;
        try {
            Statement stm = this.connection.createStatement();
            ResultSet rs = stm.executeQuery(sql);
            while(rs.next()){
                c = new Cartoon();
                c.setId(rs.getInt("id"));
                c.setAge(rs.getInt("age"));
                c.setGender(rs.getInt("gender"));
                c.setName(rs.getString("name"));
                c.setSummary(rs.getString("summary"));
                int photoId = rs.getInt("photo_id");
                Photo p = null;
                if (photoId != 0){
                    p = new PhotoService().findImage(photoId);
                }
                c.setPhoto(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return c;
    }

    public ObservableList<Cartoon> findAll(){
        String sql = "SELECT * FROM cartoon";
        ObservableList<Cartoon> cartoons = FXCollections.observableArrayList();
        try {
            Statement stmt = this.connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()){
                Cartoon c = new Cartoon();
                c.setId(rs.getInt("id"));
                c.setAge(rs.getInt("age"));
                c.setGender(rs.getInt("gender"));
                c.setName(rs.getString("name"));
                c.setSummary(rs.getString("summary"));
                c.setEpisodesCnt(rs.getInt("episodesCnt"));
                int photoId = rs.getInt("photo_id");
                Photo p = null;
                if (photoId != 0){
                    p = new PhotoService().findImage(photoId);
                }
                c.setPhoto(p);
                cartoons.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cartoons;
    }

    public ObservableList<Video> findEpisodes(int id) {
            String sql = "SELECT * FROM video WHERE cartoon_id = " + id;
            ObservableList<Video> episodes = FXCollections.observableArrayList();
            try {
                Statement stmt = this.connection.createStatement();
                ResultSet rs = stmt.executeQuery(sql);
                while(rs.next()){
                    Video v = new Video();
                    v.setId(rs.getInt("id"));
                    v.setUrl(rs.getString("url"));
                    v.setTitle(rs.getString("title"));
                    v.setViews(rs.getInt("views"));
                    v.setViews(rs.getInt("views"));
                    v.setDate(rs.getDate("date"));
                    v.setCartoonId(rs.getInt("cartoon_id"));
                    v.setCategoryId(rs.getInt("category_id"));
                    episodes.add(v);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return episodes;
    }

    public void saveCartoon(int childId, int videoId, long time) {
        String sql = "INSERT INTO child_video (child_id, video_id, date, duration)" +
                "  VALUES (?, ?, ? ,?)";
        saveAction(childId, videoId, time, sql);
    }


    public long getTotalTime(int childId){
        String sql = "SELECT SUM(time_to_sec(duration)) AS time, child_id " +
                "FROM child_video " +
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

}
