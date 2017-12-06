package com.example.vincentale.leafguard_core.model;

import android.support.annotation.NonNull;

/**
 * Created by vincentale on 06/12/17.
 */

public class Oak implements Identifiable{

    private Oak() {

    }

    public Oak(@NonNull User user, float longitude, float latitude) {
        setUid(user.getUid() + "_" + (int)longitude + "_" + (int)latitude);
        setLongitude(longitude);
        setLatitude(latitude);
    }

    private String uid;

    private float longitude;

    private float latitude;

    private float oakCircumference;

    private float oakHeight;

    private long installationDate;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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

    public float getOakCircumference() {
        return oakCircumference;
    }

    public void setOakCircumference(float oakCircumference) {
        this.oakCircumference = oakCircumference;
    }

    public float getOakHeight() {
        return oakHeight;
    }

    public void setOakHeight(float oakHeight) {
        this.oakHeight = oakHeight;
    }

    public long getInstallationDate() {
        return installationDate;
    }

    public void setInstallationDate(long installationDate) {
        this.installationDate = installationDate;
    }
}
