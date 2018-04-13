package Entities;

import Core.Security;
import Dependencies.pherialize.Pherialize;

import java.sql.Timestamp;
import java.util.*;

public class User {
    private int id;
    private String username;
    private String usernameCanonical;
    private String email;
    private String emailCanonical;
    private boolean enabled;
    private String salt;
    private String password;
    private Timestamp lastLogin;
    private String confirmationToken;
    private Timestamp passwordRequestedAt;
    private String roles;
    private UserInfos userInfos;
    private Integer authCode;

    public User(String username, String password, String email){
        this.username = username;
        this.usernameCanonical = username.toLowerCase();
        this.email = email;
        this.emailCanonical = email.toLowerCase();
        this.salt = Security.generateSalt();
        this.password = Security.hashPassword(password, salt);
        List<String> list = new ArrayList<String>();
        list.add("ROLE_PARENT");
        this.roles = Pherialize.serialize(list);
    }

    public User(int id, String username, String email, boolean enabled, String salt, String password, Timestamp lastLogin, String roles) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.enabled = enabled;
        this.salt = salt;
        this.password = password;
        this.lastLogin = lastLogin;
        this.roles = roles;
    }

    public User() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public String getUsernameCanonical() {
        return usernameCanonical;
    }

    public void setUsernameCanonical(String usernameCanonical) {
        this.usernameCanonical = usernameCanonical;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getEmailCanonical() {
        return emailCanonical;
    }

    public void setEmailCanonical(String emailCanonical) {
        this.emailCanonical = emailCanonical;
    }


    public boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }


    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public Timestamp getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Timestamp lastLogin) {
        this.lastLogin = lastLogin;
    }


    public String getConfirmationToken() {
        return confirmationToken;
    }

    public void setConfirmationToken(String confirmationToken) {
        this.confirmationToken = confirmationToken;
    }


    public Timestamp getPasswordRequestedAt() {
        return passwordRequestedAt;
    }

    public void setPasswordRequestedAt(Timestamp passwordRequestedAt) {
        this.passwordRequestedAt = passwordRequestedAt;
    }


    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }


    public UserInfos getUserInfos() {
        return userInfos;
    }

    public void setUserInfos(UserInfos userInfos) {
        this.userInfos = userInfos;
    }


    public Integer getAuthCode() {
        return authCode;
    }

    public void setAuthCode(Integer authCode) {
        this.authCode = authCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User that = (User) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }

    public boolean hasRole(String role){
        return this.roles.indexOf(role, 0) != -1;
    }

    public String getFullname() {
        if (userInfos != null)
            return this.userInfos.getFirstname() + " " + this.userInfos.getLastname();
        return username;
    }

    @Override
    public String toString(){
        return getFullname();
    }
}
