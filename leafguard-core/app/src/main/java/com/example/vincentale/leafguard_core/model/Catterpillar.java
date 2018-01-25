package com.example.vincentale.leafguard_core.model;

/**
 * Created by mathilde on 08/01/18.
 */

public class Catterpillar implements Identifiable {
    public static final int INDEX_LIMIT = 20;
    private int index;
    private String uid;
    private String oakUid;
    private  Oak oak;
    private boolean woundByMammal=false;
    private boolean woundByInsect=false;
    private boolean woundByBird=false;
    private boolean woundByOther=false;
    private boolean edited=false;
    private boolean catterpillarMissing=false;

    public Catterpillar() {
    }

    /**
     * Constructor of a Cattepillar.
     * UId of the caterpillar is created with parent Oak uid and index
     * @param parentOak
     * @param index
     */
    public Catterpillar(Oak parentOak, int index) {
        if (index < 1 || index > INDEX_LIMIT) {
            throw new IllegalArgumentException("Wrong index for catterpilar. Excepted between 1 and " + INDEX_LIMIT + ", got " + index);
        }
        this.index = index;
        this.uid = parentOak.getUid() + "_" + index;
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
        return String.valueOf(index);
    }

    public int getIndex() {
        return index;
    }
}
