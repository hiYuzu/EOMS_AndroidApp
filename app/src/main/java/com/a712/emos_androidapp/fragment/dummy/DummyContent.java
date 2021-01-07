package com.a712.emos_androidapp.fragment.dummy;

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
    public static final List<DummyItem> ITEMS = new ArrayList<DummyItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, DummyItem> ITEM_MAP = new HashMap<String, DummyItem>();

    private static final int COUNT = 25;

    static {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            addItem(createDummyItem(i));
        }
    }

    private static void addItem(DummyItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static DummyItem createDummyItem(int position) {
        return new DummyItem(String.valueOf(position), false,"Item " + position, makeModels(position));
    }

    private static String makeModels(int position) {
//        StringBuilder builder = new StringBuilder();
//        builder.append("Models about Item: ").append(position);
//        for (int i = 0; i < position; i++) {
//            builder.append("\nMore models information here.");
//        }
//        return builder.toString();
        return "model"+position;
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class DummyItem {
        public final String id;
        public final boolean checked;
        public final String content;
        public final String model;

        public DummyItem(String id, boolean checked,String content, String model) {
            this.id = id;
            this.checked = checked;
            this.content = content;
            this.model = model;
        }

        @Override
        public String toString() {
            return content;
        }
    }
}
