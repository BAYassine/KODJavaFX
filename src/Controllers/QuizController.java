package Controllers;

import Core.Main;
import Entities.Answer;
import Entities.Category;
import Entities.Question;
import Entities.Quiz;
import Services.CategoryService;
import Services.QuizService;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class QuizController extends  PlanetController{

    private VBox[] slides;
    Map<Question, Answer> answers = new HashMap<>();

    public QuizController() {
        this.bgPath = "/assets/images/quiz-planet.jpg";
        this.roundImagePath = "/assets/images/dice.png";
        this.titleText = "LE MONDE DES QUIZS";
        this.subTitleText = "Enrichissez vos connaissances";
        this.enterBtn.setOnAction(e -> openPlanet());
    }

    private void openPlanet() {
        ScrollPane center = null;
        try {
            center = FXMLLoader.load(getClass().getResource("/GUI/quiz-list.fxml"));
            center.setMaxWidth(Main.scene.getWidth());
            VBox content = (VBox) center.getContent();
            content.setPrefWidth(Main.scene.getWidth());
            ObservableList<Category> categories = new CategoryService().findCategory("Quiz");
            for(Category c : categories){
                VBox quizCategory = FXMLLoader.load(getClass().getResource("/GUI/quiz-category.fxml"));
                Label name = (Label) quizCategory.lookup("#name");
                Random random = new Random();
                int color = random.nextInt(5)+1;
                name.getStyleClass().add("bg-color-" + color);
                quizCategory.getStyleClass().add("bdr-color-" + color);
                name.setText(c.getName());
                GridPane gridPane = new GridPane();
                gridPane.setPadding(new Insets(50));
                gridPane.setHgap(50);
                gridPane.setVgap(30);
                ObservableList<Quiz> categoryQuizes = new QuizService().findByCategory(c.getId());

                for (int i = 0; i < categoryQuizes.size(); i++){
                    for (int j = 0; j < 6 && i*6+j < categoryQuizes.size(); j++){
                        Quiz quiz = categoryQuizes.get(i*6 + j);
                        Button b = new Button();
                        b.setText(quiz.getName());
                        b.setOnAction(e -> showQuiz(quiz));
                        b.getStyleClass().addAll("btn", "btn-info");
                        gridPane.add(b, j, i);
                    }
                }
                quizCategory.getChildren().add(gridPane);
                content.getChildren().add(quizCategory);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        center.setMaxHeight(Main.scene.getHeight());
        QuizController.planet.getChildren().add(center);

    }

    private void showQuiz(Quiz quiz) {
        StackPane center = null;
        try {
            long start = System.currentTimeMillis();
            center = FXMLLoader.load(getClass().getResource("/GUI/quiz.fxml"));
            center.setMaxWidth(Main.scene.getWidth());
            center.setMaxHeight(Main.scene.getHeight());
            StackPane slider = (StackPane) center.lookup("#slider");
            VBox scoreBox = (VBox) center.lookup("#scorebox");
            ImageView iv = (ImageView) center.lookup("#scoreImage");
            Label message = (Label) center.lookup("#message");
            Label score = (Label) center.lookup("#score");
            Button quitter = (Button) center.lookup("#quitBtn");
            StackPane finalCenter = center;
            quitter.setOnAction(e -> QuizController.planet.getChildren().remove(finalCenter));

            ObservableList<Question> questions = new QuizService().findQuestions(quiz.getId());
            Label qztitle = (Label) center.lookup("#title");
            qztitle.setText(quiz.getName());
            ProgressBar progress = (ProgressBar) center.lookup("#progress");
            Button endBtn = (Button) center.lookup("#endBtn");
            endBtn.setOnAction(e -> {
                saveAnswers();
                int count = saveAnswers();
                if (((double)count/ questions.size()) < 0.5){
                    message.setText("On sait tu peut faire mieux la prochaine fois");
                    scoreBox.getStyleClass().add("bg-color-3");
                }else {
                    scoreBox.getStyleClass().add("bg-color-2");
                }
                scoreBox.setOpacity(1);
                score.setText("Ton score est : " + count);
                new QuizService().saveQuiz(answers, KidsspaceController.getChild().getId(), count);
                new QuizService().saveDuration(KidsspaceController.getChild().getId(),
                        System.currentTimeMillis() - start);
            });

            slides = new VBox[questions.size()];
            for (int i = 0 ; i< questions.size(); i++){
                Question q = questions.get(i);
                VBox box = FXMLLoader.load(getClass().getResource("/GUI/question.fxml"));
                box.setPrefWidth(Main.scene.getWidth());
                slides[i] = box;
                if (i!=0)
                    box.setTranslateX(Main.scene.getWidth() + 200);
                Label qtitle = (Label) box.lookup("#questionTitle");
                qtitle.setText(q.getContent());
                for(int j = 0; j< q.getAnswerList().size(); j++){
                    Answer a = q.getAnswerList().get(j);
                    Button answerBtn = (Button) box.lookup("#answer"+ j);
                    answerBtn.setText(a.getContent());
                    int finali = i;
                    answerBtn.setOnAction(e -> {
                        if (finali < questions.size()-1){
                            slideNext(finali);
                            progress.setProgress(((double) (finali+1))/questions.size());
                        }
                        else {
                            endBtn.setPrefHeight(Region.USE_COMPUTED_SIZE);
                            progress.setProgress(1);
                        }
                        answers.put(q, a);
                    });
                }
                slider.getChildren().add(box);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        center.setStyle("-fx-background-color: white");
        QuizController.planet.getChildren().add(center);
    }

    private int saveAnswers() {
        final int[] count = {0};
        answers.forEach((k,v)->{
            if(v.getCorrect())
                count[0]++;
        });
        return count[0];
    }

    private void slideNext(int index) {
        TranslateTransition tt1 = new TranslateTransition(Duration.millis(200),slides[index]);
        TranslateTransition tt2 = new TranslateTransition(Duration.millis(200),slides[index+1]);
        tt1.setToX(-Main.scene.getWidth());
        tt2.setToX(0);
        ParallelTransition pt = new ParallelTransition();
        pt.getChildren().addAll(tt1, tt2);
        pt.play();
    }

}
