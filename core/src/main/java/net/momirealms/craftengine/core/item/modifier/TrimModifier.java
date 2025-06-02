package net.momirealms.craftengine.core.item.modifier;

import net.momirealms.craftengine.core.item.*;
import net.momirealms.craftengine.core.util.VersionHelper;
import net.momirealms.sparrow.nbt.CompoundTag;
import net.momirealms.sparrow.nbt.Tag;

public class TrimModifier<I> implements ItemDataModifier<I> {
    private final String material;
    private final String pattern;

    public TrimModifier(String material, String pattern) {
        this.material = material;
        this.pattern = pattern;
    }

    @Override
    public String name() {
        return "trim";
    }

    @Override
    public void apply(Item<I> item, ItemBuildContext context) {
        item.trim(new Trim(this.material, this.pattern));
    }

    @Override
    public void prepareNetworkItem(Item<I> item, ItemBuildContext context, CompoundTag networkData) {
        if (VersionHelper.isOrAbove1_20_5()) {
            Tag previous = item.getNBTComponent(ComponentKeys.TRIM);
            if (previous != null) {
                networkData.put(ComponentKeys.TRIM.asString(), NetworkItemHandler.pack(NetworkItemHandler.Operation.ADD, previous));
            } else {
                networkData.put(ComponentKeys.TRIM.asString(), NetworkItemHandler.pack(NetworkItemHandler.Operation.REMOVE));
            }
        } else {
            Tag previous = item.getNBTTag("Trim");
            if (previous != null) {
                networkData.put("Trim", NetworkItemHandler.pack(NetworkItemHandler.Operation.ADD, previous));
            } else {
                networkData.put("Trim", NetworkItemHandler.pack(NetworkItemHandler.Operation.REMOVE));
            }
        }
    }
}
