package net.momirealms.craftengine.core.item;

import net.momirealms.craftengine.core.plugin.CraftEngine;
import net.momirealms.craftengine.core.util.Key;

public class DelayedInitItem {
    private final Key itemId;
    private Item<?> item;

    public DelayedInitItem(Key itemId) {
        this.itemId = itemId;
    }

    public Item<?> getItem() {
        if (this.item == null) {
            this.item = CraftEngine.instance().itemManager().createWrappedItem(this.itemId, null);
            if (this.item == null) {
                CraftEngine.instance().logger().warn("Could not create item: " + this.itemId);
            }
        }
        return this.item;
    }
}
