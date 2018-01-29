package com.example.vincentale.leafguard_core.model;

import java.util.Date;

/**
 * Created by vincentale on 29/01/18.
 * base classfor observations. Timestamp is generated at object instantiation.
 */

public abstract class AbstractObservation implements Identifiable {

    protected String uid;
    private long timestamp;

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
}
