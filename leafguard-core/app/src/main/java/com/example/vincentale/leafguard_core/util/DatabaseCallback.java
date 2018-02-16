package com.example.vincentale.leafguard_core.util;

import com.example.vincentale.leafguard_core.model.Identifiable;
import com.google.firebase.database.DatabaseError;

/**
 * Created by vincentale on 11/12/17.
 * Represent a callback used to manipulate a single isntance of a T object retrieved from Firebase
 */

public interface DatabaseCallback<T extends Identifiable> {
    /**
     * callback method when the object is successfuly retrieved from the database
     * @param identifiable
     */
    void onSuccess(T identifiable);

    /**
     * callback method when an error is thrown during the retrieving of the object
     * @param error
     */
    void onFailure(DatabaseError error);
}
