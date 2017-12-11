package com.example.vincentale.leafguard_core.util;

import com.example.vincentale.leafguard_core.model.Identifiable;
import com.google.firebase.database.DatabaseError;

/**
 * Created by vincentale on 11/12/17.
 */

public interface DatabaseCallback<T extends Identifiable> {
    void onSuccess(T identifiable);

    void onFailure(DatabaseError error);
}
