package com.example.vincentale.leafguard_core.model;

import android.support.annotation.NonNull;

import com.example.vincentale.leafguard_core.util.DatabaseCallback;

import java.util.ArrayList;

/**
 * Created by mathilde on 22/01/18.
 */

public class CaterpillarManager implements Manager<Catterpillar>{
    @Override
    public void update(@NonNull Catterpillar object) {

    }

    @Override
    public void delete(@NonNull Catterpillar object) {

    }

    @Override
    public Catterpillar find(String uid, DatabaseCallback<Catterpillar> callback) {
        return null;
    }

    @Override
    public ArrayList<Catterpillar> findAll() {
        return null;
    }
}
