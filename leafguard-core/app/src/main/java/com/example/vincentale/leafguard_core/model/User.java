package com.example.vincentale.leafguard_core.model;

import android.util.Log;

import com.example.vincentale.leafguard_core.util.StringHelper;
import com.google.firebase.database.Exclude;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class User implements Identifiable {
    public static final int ROLE_USER = 0;
    public static final int ROLE_ADMIN = 1;

    private String observationUids;
    private Set<String> observationUidSet = new HashSet<>();
    private String uid;
    private String name;
    private String surname;
    private String email;
    private String partnerId;
    private String schoolName;
    private String schoolLevel;

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
        this.oakId = u.getOakId();
        this.oak = u.getOak();
        this.partnerId = u.getPartnerId();
        this.schoolName = u.getSchoolName();
        this.schoolLevel = u.getSchoolLevel();
        setObservationUids(u.getObservationUids());
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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

    public String getPartnerId() {
        return partnerId;
    }

    public User setPartnerId(String partnerId) {
        this.partnerId = partnerId;

        return this;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public User setSchoolName(String schoolName) {
        this.schoolName = schoolName;

        return this;
    }

    public String getSchoolLevel() {
        return schoolLevel;
    }

    public User setSchoolLevel(String schoolLevel) {
        this.schoolLevel = schoolLevel;

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

    public boolean isAdmin() {
        return role >= ROLE_ADMIN;
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
                ", schoolName=" + schoolName +
                ", observationUids=" + observationUids +
                '}';
    }

    /**
     * Add the given observation id into a set of sent observation.
     * it's not the real uid, but a combination of observation UId and index.
     * @param observationUid
     */
    public void addObservation(String observationUid) {
        observationUidSet.add(observationUid);
    }

    public boolean hasObservation(String observationUid) {
        return observationUidSet.contains(observationUid);
    }

    public Set<String> getObservationUidSet() {
        return observationUidSet;
    }

    public String getObservationUids() {
        return observationUidSet.toString();
    }

    public void setObservationUids(String observationUids) {
        this.observationUids = observationUids;
        for (String s : StringHelper.parse(observationUids)) {
            Log.d("USER", "setObservationUids: " + s);
            addObservation(s);
        }
    }
}
