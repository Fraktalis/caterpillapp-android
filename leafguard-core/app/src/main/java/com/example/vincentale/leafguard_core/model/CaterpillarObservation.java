package com.example.vincentale.leafguard_core.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vincentale on 29/01/18.
 * Observation for caterpillars data summary
 */

public class CaterpillarObservation extends AbstractObservation {

    public static final int OBSERVATION_LIMIT = 2;
    private ArrayList<Caterpillar> caterpillars;
    /**
     * Counters for data post processing
     */
    private int totalCaterpillars = 0;
    private int missingCaterpillars = 0;
    private int totalPredationMarks = 0;
    private int birdPredationMarks = 0;
    private int arthropodPredationMarks = 0;
    private int mammalPredationMarks = 0;
    private int lizardPredationMarks = 0;


    public CaterpillarObservation(Oak referedOak, int observationIndex) {
        super();
        if (observationIndex <= 0 || observationIndex > OBSERVATION_LIMIT) {
            throw new IllegalArgumentException("observationIndex is not valid. Expected between 0 and " + OBSERVATION_LIMIT + ", got : " + observationIndex);
        }
        this.uid = referedOak.getUid() + "_" + observationIndex;
        this.caterpillars = new ArrayList<>(Caterpillar.INDEX_LIMIT);
    }

    public int getTotalCaterpillars() {
        return totalCaterpillars;
    }

    public int getMissingCaterpillars() {
        return missingCaterpillars;
    }

    public int getTotalPredationMarks() {
        return totalPredationMarks;
    }

    public int getBirdPredationMarks() {
        return birdPredationMarks;
    }

    public int getArthropodPredationMarks() {
        return arthropodPredationMarks;
    }

    public int getMammalPredationMarks() {
        return mammalPredationMarks;
    }

    public int getLizardPredationMarks() {
        return lizardPredationMarks;
    }

    public CaterpillarObservation addCaterpillar(Caterpillar caterpillar) {
        caterpillars.add(caterpillar);
        totalCaterpillars++;
        if (caterpillar.isCatterpillarMissing()) {
            missingCaterpillars++;
        } else {
            if (caterpillar.isCatterpillarMissing()) {
                missingCaterpillars++;
            }
            if (caterpillar.isWoundByBird()) {
                birdPredationMarks++;
                totalPredationMarks++;
            }
            if (caterpillar.isWoundByInsect()) {
                arthropodPredationMarks++;
                totalPredationMarks++;
            }
            if (caterpillar.isWoundByMammal()) {
                mammalPredationMarks++;
                totalPredationMarks++;
            }
            if (caterpillar.isWoundByLizard()) {
                lizardPredationMarks++;
                totalPredationMarks++;
            }
        }
        return this;
    }

    public CaterpillarObservation setCaterpillars(List<Caterpillar> caterpillarList) {
        for (Caterpillar c : caterpillarList) {
            addCaterpillar(c);
        }
        return this;
    }


}