package com.example.vincentale.leafguard_core.model;

/**
 * Created by mathilde on 08/01/18.
 */

public class Catterpillar {
    private String location;
    private String iD;
    private int numberOfWound=0;
    private boolean woundByMammal=false;
    private boolean woundByInsect=false;
    private boolean woundByBird=false;
    private boolean woundByOther=false;
    private boolean edited=false;
    private boolean catterpillarMissing=false;



    public Catterpillar(String location, String iD) {
        this.location = location;
        this.iD = iD;
    }

    public Catterpillar(String location, String iD, int numberOfWound) {
        this.location = location;
        this.iD = iD;
        this.numberOfWound = numberOfWound;
        this.woundByMammal = woundByMammal;
        this.woundByInsect = woundByInsect;
        this.woundByBird = woundByBird;
        this.woundByOther = woundByOther;
        this.catterpillarMissing = catterpillarMissing;
    }

    public Catterpillar(String location, String iD, int numberOfWound, boolean woundByMammal, boolean woundByInsect, boolean woundByBird, boolean woundByOther, boolean edited, boolean catterpillarMissing) {
        this.location = location;
        this.iD = iD;
        this.numberOfWound = numberOfWound;
        this.woundByMammal = woundByMammal;
        this.woundByInsect = woundByInsect;
        this.woundByBird = woundByBird;
        this.woundByOther = woundByOther;
        this.edited = edited;
        this.catterpillarMissing = catterpillarMissing;
    }

    public String getiD() {
        return iD;
    }

    public int getWounds() {
        return numberOfWound;
    }

    public void setNumberOfWWound(int wound) {
        numberOfWound = wound;
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
}
