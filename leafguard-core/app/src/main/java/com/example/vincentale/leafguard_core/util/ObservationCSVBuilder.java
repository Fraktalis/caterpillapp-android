package com.example.vincentale.leafguard_core.util;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.vincentale.leafguard_core.model.AbstractObservation;
import com.example.vincentale.leafguard_core.model.CaterpillarObservation;

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
            //TODO : Add leaves observation fields
    };

    private StringBuilder entryBuilder;

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
        entryBuilder.append("\n");
    }

    public ObservationCSVBuilder add(@NonNull CaterpillarObservation firstObservation, CaterpillarObservation secondObservation, AbstractObservation leavesObservation) {
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
                Log.e(TAG, "Error during adding field, ignoring", e);
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
                    Log.e(TAG, "Error during adding field, ignoring", e);
                }
            }

            if (i != secondObservationFields.length - 1) {
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
