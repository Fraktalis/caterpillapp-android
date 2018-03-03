package com.example.vincentale.leafguard_core.model;

import java.util.ArrayList;

/**
 * Created by vincentale on 03/03/18.
 */

public class ImportReport implements Identifiable {

    private String uid;
    private ArrayList<String> importedEmails = new ArrayList<>();
    private ArrayList<String> ignoredEmails = new ArrayList<>();
    private int errors;

    @Override
    public String getUid() {
        return uid;
    }

    @Override
    public void setUid(String uid) {
        this.uid = uid;
    }

    public ArrayList<String> getImportedEmails() {
        return importedEmails;
    }

    public ImportReport setImportedEmails(ArrayList<String> importedEmails) {
        this.importedEmails = importedEmails;
        return this;
    }

    public ImportReport addImportedEmail(String email) {
        this.importedEmails.add(email);
        return this;
    }

    public ArrayList<String> getIgnoredEmails() {
        return ignoredEmails;
    }

    public ImportReport setIgnoredEmails(ArrayList<String> ignoredEmails) {
        this.ignoredEmails = ignoredEmails;
        return this;
    }

    public ImportReport addIgnoredEmail(String email) {
        this.ignoredEmails.add(email);
        return this;
    }

    public int getErrors() {
        return errors;
    }

    public ImportReport setErrors(int errors) {
        this.errors = errors;
        return this;
    }

    @Override
    public String toString() {
        return "ImportReport{" +
                "uid='" + uid + '\'' +
                ", importedEmails =" + importedEmails.size() +
                ", ignoredEmails=" + ignoredEmails.size() +
                ", errors=" + errors +
                '}';
    }
}
