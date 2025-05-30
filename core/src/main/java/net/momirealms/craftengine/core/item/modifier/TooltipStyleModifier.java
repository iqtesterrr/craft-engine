package net.momirealms.craftengine.core.item.modifier;

import net.momirealms.craftengine.core.item.Item;
import net.momirealms.craftengine.core.item.ItemBuildContext;
import net.momirealms.craftengine.core.util.Key;

public class TooltipStyleModifier<I> implements ItemDataModifier<I> {
    private final Key argument;

    public TooltipStyleModifier(Key argument) {
        this.argument = argument;
    }

    @Override
    public String name() {
        return "tooltip-style";
    }

    @Override
    public void apply(Item<I> item, ItemBuildContext context) {
        item.tooltipStyle(argument.toString());
    }

    @Override
    public void remove(Item<I> item) {
        item.tooltipStyle(null);
    }
}
