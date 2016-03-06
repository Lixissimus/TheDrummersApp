package com.lixissimus.thedrummersapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p/>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class TunerContentProvider {

    /**
     * An array of drum items.
     */
    public static final List<DrumItem> ITEMS = new ArrayList<DrumItem>();

    /**
     * A map of drum items, by ID.
     */
    public static final Map<String, DrumItem> ITEM_MAP = new HashMap<String, DrumItem>();

    private static final int COUNT = 4;

    static {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            DrumItem drum = new DrumItem(String.valueOf(i), "Tom " + i, 220 + 10*i, 230 + 10*i);
            addItem(drum);
        }
    }

    private static void addItem(DrumItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    /**
     * A drum item representing a drum.
     */
    public static class DrumItem {
        public final String id;
        public final String name;
        public final int freqBatter;
        public final int freqReso;

        public DrumItem(String id, String name, int freqBatter, int freqReso) {
            this.id = id;
            this.name = name;
            this.freqBatter = freqBatter;
            this.freqReso = freqReso;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
