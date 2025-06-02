package net.momirealms.craftengine.bukkit.item.factory;

import net.momirealms.craftengine.bukkit.item.ComponentItemWrapper;
import net.momirealms.craftengine.bukkit.item.ComponentTypes;
import net.momirealms.craftengine.core.item.EquipmentData;
import net.momirealms.craftengine.core.plugin.CraftEngine;

import java.util.Optional;

public class ComponentItemFactory1_21_2 extends ComponentItemFactory1_21 {

    public ComponentItemFactory1_21_2(CraftEngine plugin) {
        super(plugin);
    }

    @Override
    protected void tooltipStyle(ComponentItemWrapper item, String data) {
        if (data == null) {
            item.resetComponent(ComponentTypes.TOOLTIP_STYLE);
        } else {
            item.setJavaComponent(ComponentTypes.TOOLTIP_STYLE, data);
        }
    }

    @Override
    protected Optional<String> tooltipStyle(ComponentItemWrapper item) {
        return item.getJavaComponent(ComponentTypes.TOOLTIP_STYLE);
    }

    @Override
    protected void itemModel(ComponentItemWrapper item, String data) {
        if (data == null) {
            item.resetComponent(ComponentTypes.ITEM_MODEL);
        } else {
            item.setJavaComponent(ComponentTypes.ITEM_MODEL, data);
        }
    }

    @Override
    protected Optional<String> itemModel(ComponentItemWrapper item) {
        return item.getJavaComponent(ComponentTypes.ITEM_MODEL);
    }

    @Override
    protected void equippable(ComponentItemWrapper item, EquipmentData data) {
        if (data == null) {
            item.resetComponent(ComponentTypes.EQUIPPABLE);
        } else {
            item.setJavaComponent(ComponentTypes.EQUIPPABLE, data.toMap());
        }
    }

    @Override
    protected Optional<EquipmentData> equippable(ComponentItemWrapper item) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }
}