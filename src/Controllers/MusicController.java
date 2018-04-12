package Controllers;

import Core.Main;
import Entities.Music;
import javafx.collections.ObservableList;
import Services.MusicService;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.io.IOException;

public class MusicController extends PlanetController{

    private int current_song = 0;
    private ObservableList<Music> songs;

    private static MediaPlayer mediaPlayer;
    private StackPane content;
    private Button play;
    private boolean playing = false;
    private long played = 0;
    private long start;

    public MusicController() {
        this.bgPath = "/assets/images/games-planet.jpg";
        this.roundImagePath = "/assets/images/dice.png";
        this.titleText = "LE MONDE DE LA MUSIQUE";
        this.subTitleText = "LE MONDE DES JEUX";
        this.enterBtn.setOnAction(e -> openPlanet());
        songs = new MusicService().findAll();
    }

    private void openPlanet() {
        try {
            content = FXMLLoader.load(getClass().getResource("/GUI/music-player.fxml"));
            content.setMaxWidth(Main.scene.getWidth());
            content.setMaxHeight(Main.scene.getHeight());
            HBox bottom = (HBox) content.lookup("#bottom");
            ImageView image = (ImageView) content.lookup("#image");
            Label title = (Label) content.lookup("#title");
            play = (Button) content.lookup("#play");
            Button next = (Button) content.lookup("#next");
            Button prev = (Button) content.lookup("#prev");
            Button playlistBtn = (Button) content.lookup("#playlist");

            title.setTranslateY(- ( Main.scene.getHeight() / 2 ) + 70);
            image.setTranslateY(- ( Main.scene.getHeight() / 2 ) + 200);
            image.setTranslateX(- ( Main.scene.getWidth() / 2) + 240);
            bottom.setTranslateY(Main.scene.getHeight()/2 - 70);
            play.setOnAction(e -> playSong(-1,'C'));
            next.setOnAction(e -> playSong(-1,'N'));
            prev.setOnAction(e -> playSong(-1,'P'));
            playlistBtn.setOnAction(e -> showPlaylist(songs));
        } catch (IOException e) {
            e.printStackTrace();
        }
        MusicController.planet.getChildren().add(content);
    }

    private void playSong(int index, char dir) {
        if(dir != 'C'){
            if(index != -1) {
                current_song = index;
            }else if(dir == 'P' || dir == 'N'){
                if(dir == 'P' && current_song > 0)
                    current_song--;
                else if(dir == 'P')
                    current_song = songs.size()-1;
                if(dir == 'N' && current_song < songs.size()-1)
                    current_song++;
                else if (dir == 'N')
                    current_song = 0;
            }
            if(mediaPlayer != null)
                mediaPlayer.stop();
            ImageView image = (ImageView) content.lookup("#image");
            Label title = (Label) content.lookup("#title");
            if(songs.get(current_song).getPhoto() != null)
                image.setImage(new Image(songs.get(current_song).getPhoto().getWebPath()));
            title.setText(songs.get(current_song).getTitle());
            Media hit = new Media(new File(songs.get(current_song).getWebPath()).toURI().toString());
            mediaPlayer = new MediaPlayer(hit);
            mediaPlayer.play();
            ImageView playImage = new ImageView(new Image("assets/images/pause.png"));
            play.setGraphic(playImage);
            playing = !playing;
        }
        if(!playing && dir == 'C'){
            ImageView playImage = new ImageView(new Image("assets/images/pause.png"));
            play.setGraphic(playImage);
            playing = !playing;
            if(mediaPlayer == null){
                ImageView image = (ImageView) content.lookup("#image");
                Label title = (Label) content.lookup("#title");
                if(songs.get(current_song).getPhoto() != null)
                    image.setImage(new Image(songs.get(current_song).getPhoto().getWebPath()));
                title.setText(songs.get(current_song).getTitle());
                Media hit = new Media(new File(songs.get(current_song).getWebPath()).toURI().toString());
                mediaPlayer = new MediaPlayer(hit);
            }
            mediaPlayer.play();
        }
        else if (playing && dir == 'C') {
            ImageView playImage = new ImageView(new Image("assets/images/play.png"));
            playing = !playing;
            play.setGraphic(playImage);
            mediaPlayer.pause();
        }
    }

    public void showPlaylist(ObservableList<Music> songs) {
        ScrollPane playlist = null;
        try {
            playlist = FXMLLoader.load(getClass().getResource("/GUI/playlist.fxml"));
            VBox container = (VBox) playlist.getContent().lookup("#container");
            container.setPrefWidth(Main.scene.getWidth());
            for (int i = 0; i < songs.size(); i++) {
                Music song = songs.get(i);
                Button songBtn = new Button(song.getTitle());
                int finalI = i;
                ScrollPane finalPlaylist = playlist;
                songBtn.setOnAction(e -> {
                    content.getChildren().remove(finalPlaylist);
                    playSong(finalI, 'V');
                });
                container.getChildren().add(songBtn);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        content.getChildren().add(playlist);

    }


//    public void addMusic(){
//        Music c = new Music("music", 220, "singer", 18, "url", "music");
//        MusicService ms = new MusicService();
//
//        ms.addMusic(c);
//    }

    public void deleteMusic(int id){
        MusicService ms = new MusicService();
        ms.deleteObject(id,"Music");
    }

    public void showMusic(int id){
        MusicService ms = new MusicService();
        Music c = ms.findMusic(id);
        System.out.println(c);
    }

    public void updateMusic(int id) {

        MusicService ms = new MusicService();
        Music c = ms.findMusic(id);
        c.setTitle("new music");
        ms.updateMusic(c);
        c = ms.findMusic(id);
        System.out.print(c);
    }


    public static void clearTraces() {
        mediaPlayer.stop();
    }
}
