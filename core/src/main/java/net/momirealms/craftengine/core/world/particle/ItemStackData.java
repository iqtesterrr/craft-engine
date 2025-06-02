package net.momirealms.craftengine.core.world.particle;

import net.momirealms.craftengine.core.item.DelayedInitItem;
import net.momirealms.craftengine.core.item.Item;

public class ItemStackData implements ParticleData {
    private final DelayedInitItem item;

    public ItemStackData(DelayedInitItem item) {
        this.item = item;
    }

    public Item<?> item() {
        return item.getItem();
    }
}
