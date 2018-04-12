package Services;

import Entities.Video;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDate;

public class VideoService  extends Service {

    public void addEpisode(Video v){
        String sql =
                "INSERT INTO video (cartoon_id, url, date, views, category_id, title) " +
                        "VALUES (?,?,?,?,?,?)";
        try {
            PreparedStatement ps = this.connection.prepareStatement(sql);
            ps.setInt(1, v.getCartoonId());
            ps.setString(2, v.getUrl());
            int d = LocalDate.now().getDayOfMonth();
            int m = LocalDate.now().getMonthValue();
            int y = LocalDate.now().getYear();
            ps.setDate(3, new Date(d,m,y));
            ps.setInt(4, 0);
            ps.setInt(5, v.getCategoryId());
            ps.setString(6, v.getTitle());
            ps.executeUpdate();
            System.out.println("Le video a été ajouté avec succes");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateVideo(Video v){
        String sql =
                "UPDATE video " +
                        "SET title=?, url=?,category_id=?, subject_id=? , cartoon_id =?" +
                        "WHERE video.id = ?";
        try {
            PreparedStatement ps = this.connection.prepareStatement(sql);
            ps.setString(1, v.getTitle());
            ps.setString(2, v.getUrl());
            ps.setInt(3, v.getCartoonId());
            ps.setInt(4, v.getSubjectId());
            ps.setInt(5, v.getCartoonId());
            ps.executeUpdate();
            System.out.println("Le video a été modifié avec succes");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Video findVideo(int id){
        String sql = "SELECT * FROM video WHERE id = "+ id ;

        Video v = null;
        try {
            Statement stm = this.connection.createStatement();
            ResultSet rs = stm.executeQuery(sql);
            while(rs.next()){
                v = new Video();
                v.setId(rs.getInt("id"));
                v.setTitle(rs.getString("title"));
                v.setUrl(rs.getString("url"));
                v.setCartoonId(rs.getInt("cartoon_id"));
                v.setSubjectId(rs.getInt("subject_id"));
                v.setDate(rs.getDate("date"));
                v.setViews(rs.getInt("views"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return v;
    }

    public ObservableList<Video> findAll(){
        String sql = "SELECT * FROM video";
        return getList(sql);
    }

    public ObservableList<Video> findCourses(){
        String sql = "SELECT * FROM video Where cartoon_id IS NULL";
        return getList(sql);
    }


    public ObservableList<Video> findEpisodes(int id) {
        String sql = "SELECT * FROM video WHERE cartoon_id = " + id;
        return getList(sql);
    }

    private ObservableList<Video> getList(String sql) {
        ObservableList<Video> videos = FXCollections.observableArrayList();
        try {
            Statement stmt = this.connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()){
                Video v = new Video();
                v.setId(rs.getInt("id"));
                v.setTitle(rs.getString("title"));
                v.setUrl(rs.getString("url"));
                v.setCartoonId(rs.getInt("cartoon_id"));
                v.setSubjectId(rs.getInt("subject_id"));
                v.setDate(rs.getDate("date"));
                v.setViews(rs.getInt("views"));
                videos.add(v);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return videos;
    }

    public ObservableList<Video> findCoursesByCat(int id) {
        String sql = "SELECT * FROM video WHERE category_id = " + id;
        return getList(sql);
    }
}
