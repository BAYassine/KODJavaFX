/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import java.util.Objects;
import javafx.collections.ObservableList;

/**
 *
 * @author ali
 */
public class School {
    private int id;
    private String schoolname;
    private String address;
    private Integer schoolphone;
    private String schoolmail;
    private Double Xschool ;
    private Double Yschool;
    private ObservableList<School> etablissement;

    public School() {
    }

    public School(int id, String schoolname, String address, Integer schoolphone, String schoolmail, Double Xschool, Double Yschool) {
        this.id = id;
        this.schoolname = schoolname;
        this.address = address;
        this.schoolphone = schoolphone;
        this.schoolmail = schoolmail;
        this.Xschool = Xschool;
        this.Yschool = Yschool;
    }

    public School(String schoolname, String address, Integer schoolphone, String schoolmail, Double Xschool, Double Yschool) {
        this.schoolname = schoolname;
        this.address = address;
        this.schoolphone = schoolphone;
        this.schoolmail = schoolmail;
        this.Xschool = Xschool;
        this.Yschool = Yschool;
    }
    
    
    

    public int getId() {
        return id;
    }

    public String getSchoolname() {
        return schoolname;
    }

    public String getAddress() {
        return address;
    }

    public Integer getSchoolphone() {
        return schoolphone;
    }

    public String getSchoolmail() {
        return schoolmail;
    }

    public Double getXschool() {
        return Xschool;
    }

    public Double getYschool() {
        return Yschool;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setSchoolname(String schoolname) {
        this.schoolname = schoolname;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setSchoolphone(Integer schoolphone) {
        this.schoolphone = schoolphone;
    }

    public void setSchoolmail(String schoolmail) {
        this.schoolmail = schoolmail;
    }

    public void setXschool(Double Xschool) {
        this.Xschool = Xschool;
    }

    public void setYschool(Double Yschool) {
        this.Yschool = Yschool;
    }

    @Override
    public String toString() {
        return "etablissement{" + "id=" + id + ", schoolname=" + schoolname + ", address=" + address + ", schoolphone=" + schoolphone + ", schoolmail=" + schoolmail + ", Xschool=" + Xschool + ", Yschool=" + Yschool + '}';
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + this.id;
        hash = 89 * hash + Objects.hashCode(this.schoolname);
        hash = 89 * hash + Objects.hashCode(this.address);
        hash = 89 * hash + Objects.hashCode(this.schoolphone);
        hash = 89 * hash + Objects.hashCode(this.schoolmail);
        hash = 89 * hash + Objects.hashCode(this.Xschool);
        hash = 89 * hash + Objects.hashCode(this.Yschool);
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
        final School other = (School) obj;
        if (this.id != other.id) {
            return false;
        }
        if (!Objects.equals(this.schoolname, other.schoolname)) {
            return false;
        }
        if (!Objects.equals(this.address, other.address)) {
            return false;
        }
        if (!Objects.equals(this.schoolmail, other.schoolmail)) {
            return false;
        }
        if (!Objects.equals(this.Xschool, other.Xschool)) {
            return false;
        }
        if (!Objects.equals(this.Yschool, other.Yschool)) {
            return false;
        }
        return true;
    } 

   
    
    
    
    
}
