package Controllers;

import Core.Exceptions.*;
import Core.Main;
import Core.Security;
import Entities.User;
import Services.UserService;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class UsersController{

    private boolean emailValid = false, usernameValid = false;

    @FXML
    public Button registerLink;

    @FXML
    public Button loginBtn;

    @FXML
    public Label passError;

    @FXML
    public Label userError;

    @FXML
    public Label emailError;

    @FXML
    private TextField signEmail;

    @FXML
    private TextField signUser;

    @FXML
    private PasswordField firstPass;

    @FXML
    private PasswordField secondPass;

    @FXML
    private Button signBtn;

    @FXML
    private TextField loginEmail;

    @FXML
    private PasswordField loginPass;

    public void showLogin(){

        VBox center = null ;
        try {
            center = FXMLLoader.load(getClass().getResource("/GUI/login.fxml"));
            loginBtn = (Button) center.lookup("#loginBtn");
            loginEmail = (TextField) center.lookup("#loginEmail");
            loginPass = (PasswordField) center.lookup("#loginPass");
            loginBtn.setOnAction(e -> {
                try {
                    authenticate(null, loginEmail.getText(), loginPass.getText());
                } catch (UserNotFoundException | WrongPasswordException ex) {
                    Label loginError = (Label) Main.scene.lookup("#loginError");
                    loginError.setPrefHeight(0);
                    loginError.setText(ex.getMessage());
                    loginError.setPrefHeight(Region.USE_COMPUTED_SIZE);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        Main.pane.setCenter(center);

    }

    public void showRegister(){
        VBox center = null ;
        try {
            center = FXMLLoader.load(getClass().getResource("/GUI/register.fxml"));
            Main.pane.setCenter(center);
            signEmail = (TextField) center.lookup("#signEmail");
            emailError = (Label) center.lookup("#emailError");
            signEmail.focusedProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue && !signEmail.getText().equals("")){
                    try {
                        emailValid = Security.emailValid(signEmail.getText());
                        emailError.setPrefHeight(0);
                    } catch (EmailInvalidException | EmailUsedException e) {
                        emailError.setText(e.getMessage());
                        emailError.setPrefHeight(25);
//                    e.printStackTrace();
                    }
                }
            });
            signUser = (TextField) center.lookup("#signUser");
            userError = (Label) center.lookup("#userError");
            signUser.focusedProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue && !signUser.getText().equals("")){
                    try {
                        usernameValid = Security.usernameValid(signUser.getText());
                        userError.setPrefHeight(0);
                    } catch (UsernameInvalidException | UsernameUsedException e) {
                        userError.setText(e.getMessage());
                        userError.setPrefHeight(25);
//                    e.printStackTrace();
                    }
                }
            });
            signBtn = (Button) center.lookup("#signBtn");
            signBtn.setOnAction(e -> {
                firstPass = (PasswordField) Main.pane.lookup("#firstPass");
                secondPass = (PasswordField) Main.pane.lookup("#secondPass");
                String passOne = firstPass.getText();
                String passTwo = secondPass.getText();
                passError = (Label) Main.sp.lookup("#passError");
                if( passOne.equals("") || passTwo.equals("") || signEmail.getText().equals("") || signUser.getText().equals("")){
                    passError.setText("Tous les champs sont obligatoires");
                    passError.setPrefHeight(25);
                }
                else if (!passOne.equals(passTwo)){
                    passError.setText("Les mots de passe ne sont pas identiques");
                    passError.setPrefHeight(25);
                }
                else if (emailValid && usernameValid){
                    User u = new User(signUser.getText(), passOne, signEmail.getText());
                    UserService us = new UserService();
                    us.registerUser(u);
                    try {
                        authenticate(null, signUser.getText(), passOne);
                    } catch (UserNotFoundException | WrongPasswordException e1) {
                        e1.printStackTrace();
                    }
                    Button loginLink = (Button) Main.pane.lookup("#loginLink");
                    loginLink.setPrefWidth(0);
                    Button signOutBtn = (Button) Main.pane.lookup("#signOutBtn");
                    signOutBtn.setPrefWidth(Region.USE_COMPUTED_SIZE);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void authenticate(User u, String login, String pass) throws UserNotFoundException, WrongPasswordException {
        if(u == null){
            UserService uc = new UserService();
            u =  uc.findLogin(login);
            Security.checkPassword(pass, u);
            new Main().showLoading();
            new IndexController().init();
        }
        new Main().showLoading();
        new IndexController().init();
        Main.user = u;
        Button profileBtn = (Button) Main.sp.lookup("#profileBtn");
        FontAwesomeIconView signIcon = (FontAwesomeIconView) Main.sp.lookup("#signIcon");
        signIcon.setIcon(FontAwesomeIcon.SIGN_OUT);
        Button loginLink = (Button) Main.sp.lookup("#loginLink");
        loginLink.setPrefWidth(0);
        Button signOutBtn = (Button) Main.sp.lookup("#signOutBtn");
        signOutBtn.setPrefWidth(Region.USE_COMPUTED_SIZE);
        HBox profileHolder = (HBox) Main.scene.lookup("#profileHolder");
        profileHolder.setPrefWidth(Region.USE_COMPUTED_SIZE);
        if(u.hasRole("ADMIN")){
            HBox adminLink = (HBox) Main.sp.lookup("#adminLink");
            adminLink.setPrefWidth(Region.USE_COMPUTED_SIZE);
        }
        if(u.getUserInfos() != null){
            profileBtn.setText(u.getFullname());
            profileBtn.setPrefWidth(Region.USE_COMPUTED_SIZE);
        }
        else {
            Button editProfile = (Button) Main.scene.lookup("#editProfileBtn");
            editProfile.setPrefWidth(Region.USE_COMPUTED_SIZE);
        }
    }

    public void logOut() {
        new Main().showLoading();
        Main.user = null;
        FontAwesomeIconView signIcon = (FontAwesomeIconView) Main.sp.lookup("#signIcon");
        Button signOutBtn = (Button) Main.sp.lookup("#signOutBtn");
        Button loginLink = (Button) Main.sp.lookup("#loginLink");
        signIcon.setIcon(FontAwesomeIcon.SIGN_IN);
        signOutBtn.setPrefWidth(0);
        HBox profileHolder = (HBox) Main.scene.lookup("#profileHolder");
        profileHolder.setPrefWidth(0);
        HBox adminLink = (HBox) Main.sp.lookup("#adminLink");
        adminLink.setPrefWidth(0);

        loginLink.setPrefWidth(Region.USE_COMPUTED_SIZE);
        Button profileBtn = (Button) Main.sp.lookup("#profileBtn");
        profileBtn.setText("");
        Button editProfile = (Button) Main.scene.lookup("#editProfileBtn");
        editProfile.setPrefWidth(0);
        new IndexController().init();
    }
}
