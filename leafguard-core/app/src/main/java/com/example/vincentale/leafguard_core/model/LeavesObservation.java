package com.example.vincentale.leafguard_core.model;

/**
 * Created by vincentale on 20/02/18.
 */

public class LeavesObservation extends AbstractObservation {

    private int leavesTotal;
    private int gallsTotal;
    private int minesTotal;
    //class A : leaf not dommaged
    private int leavesAClassNumber;
    //class B : leaf dommaged by 1%-5%
    private int leavesBClassNumber;
    //class C : leaf dommaged by 6%-10%
    private int leavesCClassNumber;
    //class D : leaf dommaged by 11%-15%
    private int leavesDClassNumber;
    //class E : leaf dommaged by 16%-25%
    private int leavesEClassNumber;
    //class F : leaf dommaged by 26%-50%
    private int leavesFClassNumber;
    //class G : leaf dommaged by 51%-75%
    private int leavesGClassNumber;
    //class H : leaf dommaged by more than 75%
    private int leavesHClassNumber;

    public int getLeavesTotal() {
        return leavesTotal;
    }

    public void setLeavesTotal(int leavesTotal) {
        this.leavesTotal = leavesTotal;
    }

    public int getGallsTotal() {
        return gallsTotal;
    }

    public void setGallsTotal(int gallsTotal) {
        this.gallsTotal = gallsTotal;
    }

    public int getMinesTotal() {
        return minesTotal;
    }

    public void setMinesTotal(int minesTotal) {
        this.minesTotal = minesTotal;
    }

    public int getLeavesAClassNumber() {
        return leavesAClassNumber;
    }

    public void setLeavesAClassNumber(int leavesAClassNumber) {
        this.leavesAClassNumber = leavesAClassNumber;
    }

    public int getLeavesBClassNumber() {
        return leavesBClassNumber;
    }

    public void setLeavesBClassNumber(int leavesBClassNumber) {
        this.leavesBClassNumber = leavesBClassNumber;
    }

    public int getLeavesCClassNumber() {
        return leavesCClassNumber;
    }

    public void setLeavesCClassNumber(int leavesCClassNumber) {
        this.leavesCClassNumber = leavesCClassNumber;
    }

    public int getLeavesDClassNumber() {
        return leavesDClassNumber;
    }

    public void setLeavesDClassNumber(int leavesDClassNumber) {
        this.leavesDClassNumber = leavesDClassNumber;
    }

    public int getLeavesEClassNumber() {
        return leavesEClassNumber;
    }

    public void setLeavesEClassNumber(int leavesEClassNumber) {
        this.leavesEClassNumber = leavesEClassNumber;
    }

    public int getLeavesFClassNumber() {
        return leavesFClassNumber;
    }

    public void setLeavesFClassNumber(int leavesFClassNumber) {
        this.leavesFClassNumber = leavesFClassNumber;
    }

    public int getLeavesGClassNumber() {
        return leavesGClassNumber;
    }

    public void setLeavesGClassNumber(int leavesGClassNumber) {
        this.leavesGClassNumber = leavesGClassNumber;
    }

    public int getLeavesHClassNumber() {
        return leavesHClassNumber;
    }

    public void setLeavesHClassNumber(int leavesHClassNumber) {
        this.leavesHClassNumber = leavesHClassNumber;
    }
}
