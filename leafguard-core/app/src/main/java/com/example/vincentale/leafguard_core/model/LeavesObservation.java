package com.example.vincentale.leafguard_core.model;

import android.text.Editable;

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

    public LeavesObservation(int leavesTotal, int gallsTotal, int minesTotal, int leavesAClassNumber, int leavesBClassNumber, int leavesCClassNumber, int leavesDClassNumber, int leavesEClassNumber, int leavesFClassNumber, int leavesGClassNumber, int leavesHClassNumber) {
        this.leavesTotal = leavesTotal;
        this.gallsTotal = gallsTotal;
        this.minesTotal = minesTotal;
        this.leavesAClassNumber = leavesAClassNumber;
        this.leavesBClassNumber = leavesBClassNumber;
        this.leavesCClassNumber = leavesCClassNumber;
        this.leavesDClassNumber = leavesDClassNumber;
        this.leavesEClassNumber = leavesEClassNumber;
        this.leavesFClassNumber = leavesFClassNumber;
        this.leavesGClassNumber = leavesGClassNumber;
        this.leavesHClassNumber = leavesHClassNumber;
    }

    public LeavesObservation() {
        this.leavesTotal = 0;
        this.gallsTotal = 0;
        this.minesTotal = 0;
        this.leavesAClassNumber = 0;
        this.leavesBClassNumber = 0;
        this.leavesCClassNumber = 0;
        this.leavesDClassNumber = 0;
        this.leavesEClassNumber = 0;
        this.leavesFClassNumber = 0;
        this.leavesGClassNumber = 0;
        this.leavesHClassNumber = 0;
    }
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
