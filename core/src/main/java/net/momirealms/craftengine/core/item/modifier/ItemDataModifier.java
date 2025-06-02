package net.momirealms.craftengine.core.item.modifier;

import net.momirealms.craftengine.core.item.Item;
import net.momirealms.craftengine.core.item.ItemBuildContext;
import net.momirealms.sparrow.nbt.CompoundTag;

public interface ItemDataModifier<I> {

    String name();

    void apply(Item<I> item, ItemBuildContext context);

    default void prepareNetworkItem(Item<I> item, ItemBuildContext context, CompoundTag networkData) {
    }
}
