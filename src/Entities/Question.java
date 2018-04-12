package Entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Question {
    private int id;
    private Integer quizId;
    private String content;

    private List<Answer> answerList;

    public Question() {
        answerList = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public Integer getQuizId() {
        return quizId;
    }

    public void setQuizId(Integer quizId) {
        this.quizId = quizId;
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Question that = (Question) o;
        return id == that.id &&
                Objects.equals(quizId, that.quizId) &&
                Objects.equals(content, that.content);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, quizId, content);
    }

    public List<Answer> getAnswerList() {
        return answerList;
    }
}
