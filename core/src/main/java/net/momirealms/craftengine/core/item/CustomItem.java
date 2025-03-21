package net.momirealms.craftengine.core.item;

import net.momirealms.craftengine.core.entity.player.Player;
import net.momirealms.craftengine.core.item.behavior.ItemBehavior;
import net.momirealms.craftengine.core.item.modifier.ItemModifier;
import net.momirealms.craftengine.core.util.Key;
import net.momirealms.craftengine.core.util.context.ContextHolder;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface CustomItem<I> extends BuildableItem<I> {

    Key id();

    Key material();

    List<ItemModifier<I>> modifiers();

    ItemSettings settings();

    default boolean is(Key tag) {
        return settings().tags().contains(tag);
    }

    default Item<I> buildItem(Player player) {
        return buildItem(new ItemBuildContext(player, ContextHolder.EMPTY));
    }

    Item<I> buildItem(ItemBuildContext context);

    @NotNull
    List<ItemBehavior> behaviors();

    interface Builder<I> {
        Builder<I> id(Key id);

        Builder<I> material(Key material);

        Builder<I> modifiers(List<ItemModifier<I>> modifiers);

        Builder<I> modifier(ItemModifier<I> modifier);

        Builder<I> behavior(ItemBehavior behavior);

        Builder<I> behavior(List<ItemBehavior> behaviors);

        Builder<I> settings(ItemSettings settings);

        CustomItem<I> build();
    }
}
