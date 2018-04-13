/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import java.io.Serializable;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;

/**
 *
 * @author Rami
 */

public class Event implements Serializable {
    private static final long serialVersionUID = 1L;

    private User user;
    private Photo photo;
    private Integer id;
    private String subject;
    private String type;
    private Date startDate;
    private Date endDate;
    private String location;
    private Double price;
    private int nbrParticipants;
    private int availablePlaces;
    private int age;
    private int idUser;

    public Event() {
    }

    public Event(Integer id) {
        this.id = id;
    }
    //new Event(sujet.getText(), type.getText(),  location.getText(), Integer.parseInt(participants.getText()), Integer.parseInt(dispo.getText()), imageFile, Integer.parseInt(age.getText()))

    public Event(String subject, String type, Date startDate, Date endDate, String location, int nbrParticipants, int availablePlaces, Photo photo, int age) {
      this.subject = subject;
        this.type = type;
        this.startDate = startDate;
        this.endDate = endDate;
        this.location = location;
        this.nbrParticipants = nbrParticipants;
        this.availablePlaces = availablePlaces;
        this.photo = photo;
        this.age = age;
        //this.idUser = user;
    }

    


    

 

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public int getNbrParticipants() {
        return nbrParticipants;
    }

    public void setNbrParticipants(int nbrParticipants) {
        this.nbrParticipants = nbrParticipants;
    }

    public int getAvailablePlaces() {
        return availablePlaces;
    }

    public void setAvailablePlaces(int availablePlaces) {
        this.availablePlaces = availablePlaces;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Photo getPhoto() {
        return photo;
    }

    public void setPhoto(Photo photo) {
        this.photo = photo;
    }
    
    
  
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + Objects.hashCode(this.id);
        hash = 41 * hash + Objects.hashCode(this.subject);
        hash = 41 * hash + Objects.hashCode(this.type);
        hash = 41 * hash + Objects.hashCode(this.startDate);
        hash = 41 * hash + Objects.hashCode(this.endDate);
        hash = 41 * hash + Objects.hashCode(this.location);
        hash = 41 * hash + Objects.hashCode(this.price);
        hash = 41 * hash + this.nbrParticipants;
        hash = 41 * hash + this.availablePlaces;
        hash = 41 * hash + this.age;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Event other = (Event) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.subject, other.subject)) {
            return false;
        }
        if (!Objects.equals(this.type, other.type)) {
            return false;
        }
        if (!Objects.equals(this.startDate, other.startDate)) {
            return false;
        }
        if (!Objects.equals(this.endDate, other.endDate)) {
            return false;
        }
        if (!Objects.equals(this.location, other.location)) {
            return false;
        }
        if (!Objects.equals(this.price, other.price)) {
            return false;
        }
        if (this.nbrParticipants != other.nbrParticipants) {
            return false;
        }
        if (this.availablePlaces != other.availablePlaces) {
            return false;
        }
        if (this.age != other.age) {
            return false;
        }
        return true;
    }

 
   

    @Override
    public String toString() {
        return "Event{" + "id=" + id + ", subject=" + subject + ", type=" + type + ", startDate=" + startDate + ", endDate=" + endDate + ", location=" + location + ", price=" + price + ", nbrParticipants=" + nbrParticipants + ", availablePlaces=" + availablePlaces + ", age=" + age + '}';
    }

   
    
}
