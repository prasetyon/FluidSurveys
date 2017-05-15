package com.practice.android.angkatanku;

import java.util.Date;

/**
 * Created by prasetyon on 8/28/2015.
 */
public class User {
    private int id;
    private String nrp;
    String name;
    private String photo;
    private String hp;
    private String email;
    private Date tglLahir;
    private String originAdd;
    private String nowAdd;
    private String gender;

    public User() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNrp() {
        return nrp;
    }

    public void setNrp(String nrp) {
        this.nrp = nrp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getHp() {
        return hp;
    }

    public void setHp(String hp) {
        this.hp = hp;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getTglLahir() {
        return tglLahir;
    }

    public void setTglLahir(Date tglLahir) {
        this.tglLahir = tglLahir;
    }

    public String getOriginAdd() {
        return originAdd;
    }

    public void setOriginAdd(String originAdd) {
        this.originAdd = originAdd;
    }

    public String getNowAdd() {
        return nowAdd;
    }

    public void setNowAdd(String nowAdd) {
        this.nowAdd = nowAdd;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
