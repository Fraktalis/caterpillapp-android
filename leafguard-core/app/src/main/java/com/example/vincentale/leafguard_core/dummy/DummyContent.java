package com.example.vincentale.leafguard_core.dummy;

import com.example.vincentale.leafguard_core.model.Catterpillar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DummyContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<com.example.vincentale.leafguard_core.model.Catterpillar> ITEMS = new ArrayList<com.example.vincentale.leafguard_core.model.Catterpillar>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, com.example.vincentale.leafguard_core.model.Catterpillar> ITEM_MAP = new HashMap<String, com.example.vincentale.leafguard_core.model.Catterpillar>();

    private static final int COUNT = 20;

    static {
        // Add some sample items.
        addItem(new Catterpillar(Integer.toString(1), Integer.toString(1),3,true,false,false,true,true,false));

        addItem(new Catterpillar(Integer.toString(2), Integer.toString(2),3,false,true,false,true,true,false));
        for (int i = 3; i <= COUNT; i++) {
            addItem(new Catterpillar(Integer.toString(i), Integer.toString(i)));
        }
    }

    private static void addItem(Catterpillar item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.getiD(), item);
    }


}


