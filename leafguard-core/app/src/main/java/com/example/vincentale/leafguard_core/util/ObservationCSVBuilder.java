package com.example.vincentale.leafguard_core.util;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.vincentale.leafguard_core.model.AbstractObservation;
import com.example.vincentale.leafguard_core.model.CaterpillarObservation;
import com.example.vincentale.leafguard_core.model.LeavesObservation;

import java.lang.reflect.InvocationTargetException;

/**
 * Utility class to convert an observation to a CSV entry
 */
public class ObservationCSVBuilder {

    public static final String TAG = "ObservationCSVBuilder";

    private String[] firstObservationFields = {
            "partnerNumber",
            "latitude",
            "longitude",
            "circumference",
            "height",
            "schoolName",
            "fullname",
            "email",
            "studentsAge",
            "installationDate",
            "firstObservationDate",
            //"leavesObservationDate",
            "totalCaterpillars",
            "totalPredationMarks",
            "birdPredationMarks",
            "arthropodPredationMarks",
            "mammalPredationMarks",
            "lizardPredationMarks",
            "missingCaterpillars",
    };

    private String[] secondObservationFields = {
            "secondObservationDate",
            "totalCaterpillars",
            "totalPredationMarks",
            "birdPredationMarks",
            "arthropodPredationMarks",
            "mammalPredationMarks",
            "lizardPredationMarks",
            "missingCaterpillars",
    };

    private String[] leavesObservationFields = {
        "leavesObservationDate",
        "leavesTotal",
        "gallsTotal",
        "minesTotal",
        "leavesAClassNumber",
        "leavesBClassNumber",
        "leavesCClassNumber",
        "leavesDClassNumber",
        "leavesEClassNumber",
        "leavesFClassNumber",
        "leavesGClassNumber",
        "leavesHClassNumber"
    };

    private StringBuilder entryBuilder;

    /**
     * We use the constructor to generate the header of the CSV File
     */
    public ObservationCSVBuilder() {
        entryBuilder = new StringBuilder();
        for (int i = 0; i < firstObservationFields.length; i++) {
            entryBuilder.append(firstObservationFields[i]).append(",");
        }

        for (int i = 0; i < secondObservationFields.length; i++) {
            entryBuilder.append(secondObservationFields[i]).append("_2");

            if (i != secondObservationFields.length - 1) {
                entryBuilder.append(",");
            }
        }

        for (int i = 0; i < leavesObservationFields.length; i++) {
            entryBuilder.append(leavesObservationFields[i]).append(",");
        }

        entryBuilder.append("\n");
    }

    public ObservationCSVBuilder add(@NonNull CaterpillarObservation firstObservation, CaterpillarObservation secondObservation, LeavesObservation leavesObservation) {
        Log.d(TAG, "add: called");
        for (String firstObservationField : firstObservationFields) {
            try {
                Log.d(TAG, "add: ");
                if (firstObservationField.equals("firstObservationDate")) {
                    entryBuilder.append(firstObservation.getTimestamp()).append(",");
                } else {
                    entryBuilder.append(ReflectionHelper.invokeGetter(firstObservationField, firstObservation, CaterpillarObservation.class)).append(",");
                }
            } catch (Exception e) {
                Log.e(TAG, "Error during adding observation 1 at field" + firstObservationField +", ignoring", e);
            }
        }

        for (int i=0; i < secondObservationFields.length; i++) {
            if (secondObservation != null) {
                try {
                    if (secondObservationFields[i].equals("secondObservationDate")) {
                        entryBuilder.append(secondObservation.getTimestamp());
                    } else {
                        entryBuilder.append(ReflectionHelper.invokeGetter(secondObservationFields[i], secondObservation, CaterpillarObservation.class));
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Error during adding observation 2 at field" + secondObservationFields[i] + ", ignoring", e);
                }
            }

            if (i != secondObservationFields.length - 1) {
                entryBuilder.append(",");
            }
        }

        for (int i=0; i < leavesObservationFields.length; i++) {
            if (leavesObservation != null) {
                try {
                    if (leavesObservationFields[i].equals("leavesObservationDate")) {
                        entryBuilder.append(leavesObservation.getTimestamp());
                    } else {
                        entryBuilder.append(ReflectionHelper.invokeGetter(leavesObservationFields[i], leavesObservation, LeavesObservation.class));
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Error during adding observation leaves at  field" + leavesObservationFields[i] + ", ignoring", e);
                }
            }

            if (i != leavesObservationFields.length - 1) {
                entryBuilder.append(",");
            }
        }

        entryBuilder.append("\n");

        return this;
    }

    public ObservationCSVBuilder add(@NonNull CaterpillarObservation firstObservation, CaterpillarObservation secondObservation) {
        return add(firstObservation, secondObservation, null);
    }

    public ObservationCSVBuilder add(@NonNull CaterpillarObservation firstObservation) {
        return add(firstObservation, null, null);
    }

    public String toString() {
        return entryBuilder.toString();
    }
}
