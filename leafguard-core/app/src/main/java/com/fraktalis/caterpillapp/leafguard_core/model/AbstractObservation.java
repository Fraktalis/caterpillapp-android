package com.fraktalis.caterpillapp.leafguard_core.model;

import java.util.Date;

/**
 * Created by vincentale on 29/01/18.
 * base classfor observations. Timestamp is generated at object instantiation.
 */

public abstract class AbstractObservation implements Identifiable {

    protected String uid;
    private long timestamp;
    private String partnerNumber;
    private float longitude;
    private float latitude;
    private float circumference;
    private float height;
    private String installationDate;
    private String schoolName;
    private String fullname;
    private String email;
    private float studentsAge;


    public AbstractObservation() {
        timestamp = (new Date()).getTime();
    }

    public long getTimestamp() {
        return timestamp;
    }


    private void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String getUid() {
        return uid;
    }

    @Override
    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPartnerNumber() {
        return partnerNumber;
    }

    public void setPartnerNumber(String partnerNumber) {
        this.partnerNumber = partnerNumber;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getCircumference() {
        return circumference;
    }

    public void setCircumference(float circumference) {
        this.circumference = circumference;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public String getInstallationDate() {
        return installationDate;
    }

    public void setInstallationDate(String installationDate) {
        this.installationDate = installationDate;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public float getStudentsAge() {
        return studentsAge;
    }

    public void setStudentsAge(float studentsAge) {
        this.studentsAge = studentsAge;
    }

    @Override
    public String toString() {
        return "AbstractObservation{" +
                "uid='" + uid + '\'' +
                ", timestamp=" + timestamp +
                ", partnerNumber='" + partnerNumber + '\'' +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", circumference=" + circumference +
                ", height=" + height +
                ", installationDate='" + installationDate + '\'' +
                ", schoolName='" + schoolName + '\'' +
                ", fullname='" + fullname + '\'' +
                ", email='" + email + '\'' +
                ", studentsAge=" + studentsAge +
                '}';
    }
}
