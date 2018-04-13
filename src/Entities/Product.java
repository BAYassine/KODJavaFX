/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

/**
 *
 * @author ASUS
 */
public class Product {
    private int id;
    private String name;
    private String description;

    public Product(String name, String description, int price, int age, Category category, Integer gender, int tva, Photo img, int quantite, boolean available) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.age = age;
        this.category = category;
        this.gender = gender;
        this.tva = tva;
        this.img = img;
        this.quantite = quantite;
        this.available = available;
    }
    private int price;
    private int age;
    private int provider_id;
    private Category category;
    private Integer gender;
    private int tva;
    private Photo img;
    private int quantite ;  
    

    public int getProviderId() {
        return provider_id;
    }

    public void setProviderId(int provider_id) {
        this.provider_id = provider_id;
    }
    
    

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public Product(int id, String name, String description, int price, int age, Category category, Integer gender, int tva, Photo img, int quantite, boolean available) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.age = age;
        this.category = category;
        this.gender = gender;
        this.tva = tva;
        this.img = img;
        this.quantite = quantite;
        this.available = available;
    }

    public Product(String name, String description, int price, int age, Category category, Integer gender, int tva, Photo img, boolean available) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.age = age;
        this.category = category;
        this.gender = gender;
        this.tva = tva;
        this.img = img;
        this.available = available;
    }

    public Photo getImg() {
        return img;
    }

    public void setImg(Photo img) {
        this.img = img;
    }
    private boolean available;

    public Product() {
    }

    public Product(String name, String description, int price, int age, Category category, Integer gender, int tva, boolean available) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.age = age;
        this.category = category;
        this.gender = gender;
        this.tva = tva;
        this.available = available;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public int getTva() {
        return tva;
    }

    public void setTva(int tva) {
        this.tva = tva;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
    
    

    public void setId(int id) {
        this.id = id;
    }

    
    public int getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
    
    
    
}
