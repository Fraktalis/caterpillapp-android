package com.example.vincentale.leafguard_core.model;

import android.support.annotation.NonNull;

import com.example.vincentale.leafguard_core.util.DatabaseCallback;

import java.util.ArrayList;

/**
 * Interface to handle an object of type T into the database
 * @param <T>
 */
interface Manager<T extends Identifiable> {
    /**
     * Update all fields (Or create a new entry if necessary) of the {@link T} object
     * @param object
     */
    public void update(@NonNull  T object);

    /**
     * Delete all entries of this object in database
     * @param object
     */
    public void delete(@NonNull  T object);

    /**
     * Retrieve the object of type T with the given uid, and feed the given callback with it.
     * @param uid
     * @param callback
     */
    public void find(String uid, DatabaseCallback<T> callback);


    public ArrayList<T> findAll();
}
