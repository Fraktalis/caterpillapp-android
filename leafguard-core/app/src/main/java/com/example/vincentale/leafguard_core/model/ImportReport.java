package com.example.vincentale.leafguard_core.model;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by vincentale on 03/03/18.
 */

public class ImportReport implements Identifiable {

    private String uid;
    private HashSet<String> importedEmails = new HashSet<>();
    private HashSet<String> ignoredEmails = new HashSet<>();
    private int errors;

    @Override
    public String getUid() {
        return uid;
    }

    @Override
    public void setUid(String uid) {
        this.uid = uid;
    }

    public HashSet<String> getImportedEmails() {
        return importedEmails;
    }

    public ImportReport setImportedEmails(HashSet<String> importedEmails) {
        this.importedEmails = importedEmails;
        return this;
    }

    public ImportReport addImportedEmail(String email) {
        this.importedEmails.add(email);
        return this;
    }

    public HashSet<String> getIgnoredEmails() {
        return ignoredEmails;
    }

    public ImportReport setIgnoredEmails(HashSet<String> ignoredEmails) {
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
                ", importedEmails =" + importedEmails +
                ", ignoredEmails=" + ignoredEmails +
                ", errors=" + errors +
                '}';
    }
}
