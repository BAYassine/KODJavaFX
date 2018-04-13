/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import java.util.Objects;

/**
 *
 * @author Meriem
 */
public class Address {
    private int id;
    private String description;
    private String name;
    private String phone;
    private String location;
    private Category category;
    private float lat;
    private float lng;
    private Photo photo;

    public Address() {
    }

    public Address(int id, String description, String name, String phone, String location, Category category, float lat, float lng, Photo photo) {
        this.id = id;
        this.description = description;
        this.name = name;
        this.phone = phone;
        this.location = location;
        this.category = category;
        this.lat = lat;
        this.lng = lng;
        this.photo = photo;
    }

    public Address(String description, String name, String phone, String location, Category category, float lat, float lng, Photo photo) {
        this.description = description;
        this.name = name;
        this.phone = phone;
        this.location = location;
        this.category = category;
        this.lat = lat;
        this.lng = lng;
        this.photo = photo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public float getLng() {
        return lng;
    }

    public void setLng(float lng) {
        this.lng = lng;
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
        hash = 53 * hash + this.id;
        hash = 53 * hash + Objects.hashCode(this.description);
        hash = 53 * hash + Objects.hashCode(this.name);
        hash = 53 * hash + Objects.hashCode(this.phone);
        hash = 53 * hash + Objects.hashCode(this.location);
        hash = 53 * hash + Objects.hashCode(this.category);
        hash = 53 * hash + Float.floatToIntBits(this.lat);
        hash = 53 * hash + Float.floatToIntBits(this.lng);
        hash = 53 * hash + Objects.hashCode(this.photo);
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
        final Address other = (Address) obj;
        if (this.id != other.id) {
            return false;
        }
        if (Float.floatToIntBits(this.lat) != Float.floatToIntBits(other.lat)) {
            return false;
        }
        if (Float.floatToIntBits(this.lng) != Float.floatToIntBits(other.lng)) {
            return false;
        }
        if (!Objects.equals(this.description, other.description)) {
            return false;
        }
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (!Objects.equals(this.phone, other.phone)) {
            return false;
        }
        if (!Objects.equals(this.location, other.location)) {
            return false;
        }
        if (!Objects.equals(this.category, other.category)) {
            return false;
        }
        if (!Objects.equals(this.photo, other.photo)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Address{" + "id=" + id + ", description=" + description + ", name=" + name + ", phone=" + phone + ", location=" + location + ", category=" + category + ", lat=" + lat + ", lng=" + lng + ", photo=" + photo + '}';
    }
    
}
