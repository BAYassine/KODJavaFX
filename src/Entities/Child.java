package Entities;

import java.util.Objects;

public class Child {

    private int id;
    private Integer parentId;
    private String name;
    private int age;
    private boolean gender;
    private Integer photoId;

    public Child(Integer parentId, String name, int age, boolean gender, Integer photoId) {
        this.parentId = parentId;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.photoId = photoId;
    }

    public Child(int id, Integer parentId, String name, int age, boolean gender, Integer photoId) {
        this.id = id;
        this.parentId = parentId;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.photoId = photoId;
    }

    public Child() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    
    
    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    
    
    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    
    
    public boolean getGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    
    
    public Integer getPhotoId() {
        return photoId;
    }

    public void setPhotoId(Integer photoId) {
        this.photoId = photoId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Child that = (Child) o;
        return id == that.id &&
                age == that.age &&
                gender == that.gender &&
                Objects.equals(parentId, that.parentId) &&
                Objects.equals(name, that.name) &&
                Objects.equals(photoId, that.photoId);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, parentId, name, age, gender, photoId);
    }
}
