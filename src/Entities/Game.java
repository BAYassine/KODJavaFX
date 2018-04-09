package Entities;

import java.util.Objects;

public class Game {

    public static String noPhoto = "file:/assets/images/default-game.png";

    private int id;
    private Photo icon;
    private String name;
    private String url;
    private int age;
    private String device;
    private Category category;
    private int gender;

    public Game(){

    }

    public Game(Photo icon, String name, String url, int age, String device, Category category, int gender) {
        this.icon = icon;
        this.name = name;
        this.url = url;
        this.age = age;
        this.device = device;
        this.category = category;
        this.gender = gender;
    }

    public Game(int id, Photo icon, String name, String url, int age, String device, Category category, int gender) {
        this.id = id;
        this.icon = icon;
        this.name = name;
        this.url = url;
        this.age = age;
        this.device = device;
        this.category = category;
        this.gender = gender;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public Photo getIcon() {
        return icon;
    }

    public void setIcon(Photo icon) {
        this.icon = icon;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }


    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }


    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }


    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Game that = (Game) o;
        return id == that.id &&
                age == that.age &&
                gender == that.gender &&
                Objects.equals(icon, that.icon) &&
                Objects.equals(name, that.name) &&
                Objects.equals(url, that.url) &&
                Objects.equals(device, that.device) &&
                Objects.equals(category, that.category);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, icon, name, url, age, device, category, gender);
    }

    @Override
    public String toString() {
        return "Game{" +
                "id=" + id +
                ", icon=" + icon +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", age=" + age +
                ", device='" + device + '\'' +
                ", category=" + category +
                ", gender=" + gender +
                '}';
    }
}
