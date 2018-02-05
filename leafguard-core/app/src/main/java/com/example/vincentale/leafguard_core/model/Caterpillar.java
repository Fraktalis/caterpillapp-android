package com.example.vincentale.leafguard_core.model;

/**
 * Created by mathilde on 08/01/18.
 */

public class Caterpillar implements Identifiable {
    public static final int INDEX_LIMIT = 20;
    private int index;
    private int observationIndex;
    private String uid;
    private String oakUid;
    private  Oak oak;
    private boolean woundByMammal=false;
    private boolean woundByInsect=false;
    private boolean woundByBird=false;
    private boolean woundByLizard = false;
    private boolean woundByOther=false;
    private boolean edited=false;
    private boolean catterpillarMissing=false;

    public Caterpillar() {
    }

    /**
     * Constructor of a Cattepillar.
     * UId of the caterpillar is created with parent Oak uid and index
     * @param parentOak : Oak on which caterpillar is set
     * @param observationIndex : index of the observation
     * @param caterpillarIndex : index of the caterpillar
     */
    public Caterpillar(Oak parentOak, int observationIndex,  int caterpillarIndex) {
        if (caterpillarIndex < 1 || caterpillarIndex > INDEX_LIMIT) {
            throw new IllegalArgumentException("Wrong index for caterpillar. Excepted between 1 and " + INDEX_LIMIT + ", got " + caterpillarIndex);
        }
        if (observationIndex < 1 || observationIndex > CaterpillarObservation.OBSERVATION_LIMIT) {
            throw new IllegalArgumentException("Wrong index for observation. Excepted between 1 and " + CaterpillarObservation.OBSERVATION_LIMIT + ", got " + observationIndex);
        }
        this.index = caterpillarIndex;
        this.observationIndex = observationIndex;
        this.uid = parentOak.getUid() + "_" + observationIndex + "_" + caterpillarIndex;
        this.oak = parentOak;
        this.oakUid = parentOak.getUid();
    }

    @Override
    public String getUid() {
        return uid;
    }

    @Override
    public void setUid(String uid) {
        this.uid=uid;
    }

    public boolean isWoundByMammal() {
        return woundByMammal;
    }

    public void setWoundByMammal(boolean woundByMammal) {
        this.woundByMammal = woundByMammal;
    }

    public boolean isWoundByInsect() {
        return woundByInsect;
    }

    public void setWoundByInsect(boolean woundByInsect) {
        this.woundByInsect = woundByInsect;
    }

    public boolean isWoundByBird() {
        return woundByBird;
    }

    public void setWoundByBird(boolean woundByBird) {
        this.woundByBird = woundByBird;
    }

    public boolean isWoundByLizard() {
        return woundByLizard;
    }

    public void setWoundByLizard(boolean woundByLizard) {
        this.woundByLizard = woundByLizard;
    }

    public boolean isWoundByOther() {
        return woundByOther;
    }

    public void setWoundByOther(boolean woundByOther) {
        this.woundByOther = woundByOther;
    }

    public boolean isCatterpillarMissing() {
        return catterpillarMissing;
    }

    public void setCatterpillarMissing(boolean catterpillarMissing) {
        this.catterpillarMissing = catterpillarMissing;
    }

    public boolean isEdited() {
        return edited;
    }

    public void setEdited(boolean edited) {
        this.edited = edited;
    }

    public String getOakUid() {
        return oakUid;
    }

    public void setOakUid(String oakUid) {
        this.oakUid = oakUid;
    }

    public Oak getOak() {
        return oak;
    }

    public void setOak(Oak oak) {
        this.oak = oak;
    }

    @Override
    public String toString() {
        return "Caterpillar{" +
                "index=" + index +
                ", observationIndex=" + observationIndex +
                ", uid='" + uid + '\'' +
                ", oakUid='" + oakUid + '\'' +
                ", oak=" + oak +
                ", woundByMammal=" + woundByMammal +
                ", woundByInsect=" + woundByInsect +
                ", woundByBird=" + woundByBird +
                ", woundByLizard=" + woundByLizard +
                ", woundByOther=" + woundByOther +
                ", edited=" + edited +
                ", catterpillarMissing=" + catterpillarMissing +
                '}';
    }

    public int getIndex() {
        return index;
    }

    public int getObservationIndex() {
        return observationIndex;
    }
}
