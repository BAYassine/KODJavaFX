package Controllers;

import Entities.Child;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class PlanetController {
    protected String bgPath;
    protected String titleText;
    protected String subTitleText;
    protected String roundImagePath;
    protected Button enterBtn = new Button("ENTRER CE MONDE");

    protected static StackPane planet = null;

    public void showPlanet(StackPane pane) {
        try {
            planet = FXMLLoader.load(getClass().getResource("/GUI/planet.fxml"));
            ImageView bgImage = (ImageView) planet.lookup("#bgImage");
            bgImage.setImage(new Image(this.bgPath));
            ImageView roundImage = (ImageView) planet.lookup("#roundImage");
            roundImage.setImage(new Image(this.roundImagePath));
            Label title = (Label) planet.lookup("#title");
            title.setText(this.titleText);
            Label subTitle = (Label) planet.lookup("#subTitle");
            subTitle.setText(this.subTitleText);
            VBox container = (VBox) planet.lookup("#container");
            enterBtn.getStyleClass().addAll("btn", "btn-info");
            container.getChildren().add(enterBtn);
        } catch (IOException e) {
            e.printStackTrace();
        }
        pane.getChildren().add(planet);
    }

}
