package Services;

import Entities.Answer;
import Entities.Question;
import Entities.Quiz;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.StackPane;

import java.sql.*;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

public class QuizService extends Service {
    public ObservableList<Quiz> findByCategory(int categoryId) {
        String sql = "SELECT * FROM quiz WHERE category_id = " + categoryId;
        return getList(sql);
    }

    private ObservableList<Quiz> getList(String sql) {
        ObservableList<Quiz> quizzes = FXCollections.observableArrayList();
        try {
            Statement stmt = this.connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Quiz q = new Quiz();
                q.setId(rs.getInt("id"));
                q.setName(rs.getString("name"));
                q.setDifficulty(rs.getInt("difficulty"));
                q.setCategoryId(rs.getInt("category_id"));
                quizzes.add(q);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return quizzes;
    }

    public ObservableList<Question> findQuestions(int quizId) {
        ObservableList<Question> questions = FXCollections.observableArrayList();
        String sql = "SELECT  * FROM question WHERE quiz_id = " + quizId;
        try {
            Statement stm = connection.createStatement();
            ResultSet rs = stm.executeQuery(sql);
            while (rs.next()){
                Question q = new Question();
                q.setId(rs.getInt("id"));
                q.setContent(rs.getString("content"));
                q.setQuizId(quizId);
                Statement st = connection.createStatement();
                ResultSet rsa = st.executeQuery("SELECT * FROM answer WHERE question_id = "+ q.getId());
                while (rsa.next()){
                    Answer a = new Answer();
                    a.setId(rsa.getInt("id"));
                    a.setContent(rsa.getString("content"));
                    a.setCorrect(rsa.getBoolean("correct"));
                    a.setQuestionId(q.getId());
                    q.getAnswerList().add(a);
                }
                questions.add(q);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return questions;
    }

    public void saveQuiz(Map<Question, Answer> answers, int childId, int score) {
        String sqlAttempt = "INSERT INTO attempt (child_id, date, score, quiz_id) VALUES (?,?,?,?)";
        String sqlAnswer = "INSERT INTO child_answer (child_id, answer_id, attempt_id) VALUES (?,?,?)";
        try {
            PreparedStatement ps = connection.prepareStatement(sqlAttempt, Statement.RETURN_GENERATED_KEYS);
            Question attempt = (Question) answers.keySet().toArray()[0];
            ps.setInt(1,childId);
            ps.setDate(2,new java.sql.Date(new Date().getTime()));
            ps.setInt(3, score);
            ps.setInt(4, attempt.getId());
            ps.executeUpdate();
            int id_attempt = 0;
            ResultSet rs = ps.getGeneratedKeys();
            while (rs.next()){
                id_attempt = rs.getInt(1);
            }
            for (Map.Entry<Question, Answer> entry : answers.entrySet()){
                PreparedStatement ps1 = connection.prepareStatement(sqlAnswer);
                ps1.setInt(1,childId);
                ps1.setInt(2,entry.getValue().getId());
                ps1.setInt(3,id_attempt);
                ps1.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveDuration(int childId, long time) {
        String sql = "INSERT INTO child_quiz (child_id, duration)" +
                "  VALUES (?, ?)";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1,childId);
            TimeZone.setDefault(TimeZone.getTimeZone("GMT"));
            ps.setTimestamp(2, new Timestamp(time));
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public long getTotalTime(int childId){
        String sql = "SELECT SUM(time_to_sec(duration)) AS time, child_id " +
                "FROM child_quiz " +
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
