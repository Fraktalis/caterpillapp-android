package com.fraktalis.caterpillapp.leafguard_core.model;

import android.support.annotation.NonNull;

import com.fraktalis.caterpillapp.leafguard_core.util.DatabaseCallback;
import com.fraktalis.caterpillapp.leafguard_core.util.DatabaseListCallback;

import java.util.ArrayList;

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
    public T find(String uid, DatabaseCallback<T> callback);
    public ArrayList<T> findAll(DatabaseListCallback<T> callback);
}
