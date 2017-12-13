package com.example.vincentale.leafguard_core.model;

public class Observation implements Identifiable {

    private String uid;

    private int caterpillarNumber;

    private int totalPredationMarkNumber;

    private int birdPredationMarkNumber;

    private int arthropodPredationMarkNumber;

    private int mammalPredationMarkNumber;

    private int lizardPredationMarkNumber;

    private int unknownPredationMarkNumber;

    private int lostCaterpillar;

    private Observation() {
    }

    public Observation(User user, int observationNumber) {
        if (observationNumber < 0) {
            throw new IllegalArgumentException("ObservationNumber can't be negative");
        }
        if (user == null) {
            throw new IllegalArgumentException("User can't be null");
        }

        setUid(user.getUid() + "_" + observationNumber);
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getCaterpillarNumber() {
        return caterpillarNumber;
    }

    public void setCaterpillarNumber(int caterpillarNumber) {
        this.caterpillarNumber = caterpillarNumber;
    }

    public int getTotalPredationMarkNumber() {
        return totalPredationMarkNumber;
    }

    public void setTotalPredationMarkNumber(int totalPredationMarkNumber) {
        this.totalPredationMarkNumber = totalPredationMarkNumber;
    }

    public int getBirdPredationMarkNumber() {
        return birdPredationMarkNumber;
    }

    public void setBirdPredationMarkNumber(int birdPredationMarkNumber) {
        this.birdPredationMarkNumber = birdPredationMarkNumber;
    }

    public int getArthropodPredationMarkNumber() {
        return arthropodPredationMarkNumber;
    }

    public void setArthropodPredationMarkNumber(int arthropodPredationMarkNumber) {
        this.arthropodPredationMarkNumber = arthropodPredationMarkNumber;
    }

    public int getMammalPredationMarkNumber() {
        return mammalPredationMarkNumber;
    }

    public void setMammalPredationMarkNumber(int mammalPredationMarkNumber) {
        this.mammalPredationMarkNumber = mammalPredationMarkNumber;
    }

    public int getLizardPredationMarkNumber() {
        return lizardPredationMarkNumber;
    }

    public void setLizardPredationMarkNumber(int lizardPredationMarkNumber) {
        this.lizardPredationMarkNumber = lizardPredationMarkNumber;
    }

    public int getUnknownPredationMarkNumber() {
        return unknownPredationMarkNumber;
    }

    public void setUnknownPredationMarkNumber(int unknownPredationMarkNumber) {
        this.unknownPredationMarkNumber = unknownPredationMarkNumber;
    }

    public int getLostCaterpillar() {
        return lostCaterpillar;
    }

    public void setLostCaterpillar(int lostCaterpillar) {
        this.lostCaterpillar = lostCaterpillar;
    }
}
