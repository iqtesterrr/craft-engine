package net.momirealms.craftengine.core.item.modifier;

import net.momirealms.craftengine.core.item.EquipmentData;
import net.momirealms.craftengine.core.item.Item;
import net.momirealms.craftengine.core.item.ItemBuildContext;

public class EquippableModifier<I> implements ItemDataModifier<I> {
    private final EquipmentData data;

    public EquippableModifier(EquipmentData data) {
        this.data = data;
    }

    @Override
    public String name() {
        return "equippable";
    }

    @Override
    public void apply(Item<I> item, ItemBuildContext context) {
        item.equippable(this.data);
    }
}
