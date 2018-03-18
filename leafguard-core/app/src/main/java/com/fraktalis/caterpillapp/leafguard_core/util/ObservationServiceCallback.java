package com.fraktalis.caterpillapp.leafguard_core.util;


/**
 * Callback interface used in ObservationService
 */

public interface ObservationServiceCallback {
    public void onSuccess(String observationFullPath);
    public void onFailure(Exception ex);
}
