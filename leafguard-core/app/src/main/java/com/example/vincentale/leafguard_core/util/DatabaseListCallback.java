package com.example.vincentale.leafguard_core.util;

import com.example.vincentale.leafguard_core.model.Identifiable;
import com.google.firebase.database.DatabaseError;

import java.util.List;

/**
 * Created by mathilde on 16/02/18.
 */

public interface DatabaseListCallback <T extends Identifiable> {

    /**
     * callback method when the list of objects is successfully retrieved from the database
     *
     * @param identifiables
     */
    void onSuccess(List<T> identifiables);

    /**
     * callback method when an error is thrown during the retrieving of the object
     *
     * @param error
     */
    void onFailure(DatabaseError error);

}
