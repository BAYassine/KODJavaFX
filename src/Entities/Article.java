/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import java.sql.Date;
import java.util.Objects;

/**
 *
 * @author Meriem
 */
public class Article {
     private int id;
    private int category;
    private String description;
    private String src;
    private String title;
    private String subject;
    private String type;
    private String auteur;
    private int views;
    private Date date;
    private Photo photo; 

    public Article() {
    }

    public Article(int category, String description, String src, String title, String subject, String type, String auteur, int views, Date date, Photo photo) {
        this.category = category;
        this.description = description;
        this.src = src;
        this.title = title;
        this.subject = subject;
        this.type = type;
        this.auteur = auteur;
        this.views = views;
        this.date = date;
        this.photo = photo;
    }

    public Article(int id, int category, String description, String src, String title, String subject, String type, String auteur, int views, Date date, Photo photo) {
        this.id = id;
        this.category = category;
        this.description = description;
        this.src = src;
        this.title = title;
        this.subject = subject;
        this.type = type;
        this.auteur = auteur;
        this.views = views;
        this.date = date;
        this.photo = photo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAuteur() {
        return auteur;
    }

    public void setAuteur(String auteur) {
        this.auteur = auteur;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Photo getPhoto() {
        return photo;
    }

    public void setPhoto(Photo photo) {
        this.photo = photo;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 41 * hash + this.id;
        hash = 41 * hash + this.category;
        hash = 41 * hash + Objects.hashCode(this.description);
        hash = 41 * hash + Objects.hashCode(this.src);
        hash = 41 * hash + Objects.hashCode(this.title);
        hash = 41 * hash + Objects.hashCode(this.subject);
        hash = 41 * hash + Objects.hashCode(this.type);
        hash = 41 * hash + Objects.hashCode(this.auteur);
        hash = 41 * hash + this.views;
        hash = 41 * hash + Objects.hashCode(this.date);
        hash = 41 * hash + Objects.hashCode(this.photo);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Article other = (Article) obj;
        if (this.id != other.id) {
            return false;
        }
        if (this.category != other.category) {
            return false;
        }
        if (this.views != other.views) {
            return false;
        }
        if (!Objects.equals(this.description, other.description)) {
            return false;
        }
        if (!Objects.equals(this.src, other.src)) {
            return false;
        }
        if (!Objects.equals(this.title, other.title)) {
            return false;
        }
        if (!Objects.equals(this.subject, other.subject)) {
            return false;
        }
        if (!Objects.equals(this.type, other.type)) {
            return false;
        }
        if (!Objects.equals(this.auteur, other.auteur)) {
            return false;
        }
        if (!Objects.equals(this.date, other.date)) {
            return false;
        }
        if (!Objects.equals(this.photo, other.photo)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return title;
    }

  

   
}
