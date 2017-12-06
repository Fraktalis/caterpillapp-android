package com.example.vincentale.leafguard_core.model;

import android.support.annotation.NonNull;

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
    public T find(String uid);
    public ArrayList<T> findAll();
}
