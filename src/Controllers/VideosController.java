package Controllers;

import Core.Main;
import Entities.Category;
import Entities.Video;
import Services.CategoryService;
import Services.VideoService;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.web.WebView;

import java.io.IOException;

public class VideosController{
    public void init(){
        ScrollPane center = null;
        try{
            center = FXMLLoader.load(getClass().getResource("/GUI/videos.fxml"));
            center.setPrefWidth(Main.scene.getWidth());
            center.setPrefHeight(Main.scene.getHeight());
            VBox content = (VBox) center.getContent();
            content.setPrefWidth(Main.scene.getWidth());
            content.setPrefHeight(Main.scene.getHeight());
            GridPane grid = new GridPane();
            ColumnConstraints cc = new ColumnConstraints();
            grid.setMaxWidth(Main.scene.getWidth()-100);
            grid.setPrefHeight(Region.USE_COMPUTED_SIZE);
            cc.setPercentWidth(25);
            grid.getColumnConstraints().addAll(cc,cc,cc,cc);
            grid.setHgap(30);
            grid.setVgap(30);
            grid.setPadding(new Insets(10));
            ObservableList<Video> videos = new VideoService().findCourses();
            TabPane tabPane = (TabPane) center.getContent().lookup("#videosPane");
            Tab allTab = tabPane.getTabs().get(0);
            for(int i = 0; i< videos.size();i++){
                for(int j = 0; j < 4 && j + i*4 < videos.size(); j++){
                    Video v = videos.get(j + i*4);
                    StackPane block = FXMLLoader.load(getClass().getResource("/GUI/video-block.fxml"));
                    block.setMinHeight(300);
                    WebView thumb = (WebView)block.lookup("#thumb");
                    thumb.getEngine().load(v.getUrl());
                    block.setOnMouseClicked(e -> showPlayer(v));
                    grid.add(block, j, i);
                }
            }
            allTab.setContent(grid);
            ObservableList<Category> categories = new CategoryService().findCategory("subjects");
            for(int k = 0; k < categories.size(); k++){
                Tab catTab = new Tab(categories.get(k).getName());
                GridPane catgrid = new GridPane();
                catgrid.setPrefHeight(Region.USE_COMPUTED_SIZE);
                catgrid.setMaxWidth(Main.scene.getWidth()-100);
                cc.setPercentWidth(25);
                catgrid.getColumnConstraints().addAll(cc,cc,cc,cc);
                catgrid.setHgap(30);
                catgrid.setVgap(30);
                catgrid.setPadding(new Insets(10));
                ObservableList<Video> catvideos = new VideoService().findCoursesByCat(categories.get(k).getId());
                for(int i = 0; i< videos.size();i++){
                    for(int j = 0; j < 4 && j + i*4 < catvideos.size(); j++){
                        Video v = catvideos.get(j + i*4);
                        StackPane block = FXMLLoader.load(getClass().getResource("/GUI/video-block.fxml"));
                        block.setMinHeight(300);
                        WebView thumb = (WebView)block.lookup("#thumb");
                        thumb.getEngine().load(v.getUrl());
                        block.setOnMouseClicked(e -> showPlayer(v));
                        catgrid.add(block, j, i);
                    }
                }
                catTab.setContent(catgrid);
                tabPane.getTabs().add(catTab);
            }
        }catch(IOException e){
            e.printStackTrace();
        }
        Main.pane.setCenter(center);
    }

    private void showPlayer(Video v) {
        try {
            FlowPane player = FXMLLoader.load(getClass().getResource("/GUI/video-player.fxml"));
            player.setPrefHeight(Main.scene.getHeight());
            player.setPrefWidth(Main.scene.getWidth());
            WebView wv = (WebView) player.lookup("#browser");
            Button close = (Button) player.lookup("#close");
            Label name = (Label) player.lookup("#name");
            name.setText(v.getTitle());
            wv.getEngine().load(v.getUrl());
            close.setOnAction(e -> {
                Main.sp.getChildren().remove(player);
                wv.getEngine().load("");
            });
            Main.sp.getChildren().add(player);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}