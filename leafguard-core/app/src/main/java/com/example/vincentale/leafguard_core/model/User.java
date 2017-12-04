package com.example.vincentale.leafguard_core.model;

public class User {
    public static final int ROLE_USER = 0;
    public static final int ROLE_ADMIN = 1;

    private String uid;
    private String name;
    private String surname;
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

    public User setUid(String uid) {
        this.uid = uid;

        return this;
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

    @Override
    public String toString() {
        return "User{" +
                "uid='" + uid + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", role=" + role +
                '}';
    }
}
