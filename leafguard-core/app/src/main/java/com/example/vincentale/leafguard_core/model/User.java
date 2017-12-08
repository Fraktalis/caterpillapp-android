package com.example.vincentale.leafguard_core.model;

import com.google.firebase.database.Exclude;

public class User implements Identifiable {
    public static final int ROLE_USER = 0;
    public static final int ROLE_ADMIN = 1;

    private String uid;
    private String name;
    private String surname;
    private String email;
    @Exclude
    private Oak oak;
    private String oakId;
    private int role = ROLE_USER;

    public  User() {

    }

    public User(String uid) {
        this.uid = uid;
    }

    public User(User u) {
        this.uid = u.getUid();
        this.name = u.getName();
        this.surname = u.getSurname();
        this.role = u.getRole();
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public User setName(String name) {
        this.name = name;

        return this;
    }

    public String getSurname() {
        return surname;
    }

    public User setSurname(String surname) {
        this.surname = surname;

        return this;
    }

    public int getRole() {
        return role;
    }

    public User setRole(int role) {
        this.role = role;

        return this;
    }

    public String getEmail() {
        return email;
    }

    public User setEmail(String email) {
        this.email = email;

        return this;
    }

    public Oak getOak() {
        return oak;
    }

    public void setOak(Oak oak) {
        this.oak = oak;
        setOakId(oak.getUid());
    }

    public String getOakId() {
        return oakId;
    }

    public void setOakId(String oakId) {
        this.oakId = oakId;
    }

    public String getDisplayName() {
        return surname + " " + name;
    }

    @Override
    public String toString() {
        return "User{" +
                "uid='" + uid + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", role=" + role +
                ", oakId=" + oakId +
                ", oak=" + oak +
                '}';
    }
}
