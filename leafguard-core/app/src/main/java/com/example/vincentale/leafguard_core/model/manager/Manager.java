package com.example.vincentale.leafguard_core.model.manager;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.vincentale.leafguard_core.model.Identifiable;
import com.example.vincentale.leafguard_core.util.DatabaseCallback;
import com.example.vincentale.leafguard_core.util.DatabaseListCallback;
import com.example.vincentale.leafguard_core.util.OnUpdateCallback;

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
    public void update(@NonNull  T object, @Nullable final OnUpdateCallback onUpdateCallback);

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


    public void findAll(DatabaseListCallback<T> listCallback);
}
