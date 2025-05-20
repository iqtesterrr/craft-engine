package net.momirealms.craftengine.bukkit.item;

import net.momirealms.craftengine.bukkit.plugin.BukkitCraftEngine;
import net.momirealms.craftengine.bukkit.util.KeyUtils;
import net.momirealms.craftengine.core.item.*;
import net.momirealms.craftengine.core.item.behavior.ItemBehavior;
import net.momirealms.craftengine.core.item.modifier.ItemDataModifier;
import net.momirealms.craftengine.core.plugin.context.PlayerOptionalContext;
import net.momirealms.craftengine.core.plugin.context.event.EventTrigger;
import net.momirealms.craftengine.core.plugin.context.function.Function;
import net.momirealms.craftengine.core.registry.Holder;
import net.momirealms.craftengine.core.util.Key;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class BukkitCustomItem extends AbstractCustomItem<ItemStack> {
    private final Material material;

    public BukkitCustomItem(Holder<Key> id, Key materialKey, Material material,
                            List<ItemBehavior> behaviors,
                            List<ItemDataModifier<ItemStack>> modifiers, List<ItemDataModifier<ItemStack>> clientBoundModifiers,
                            ItemSettings settings,
                            Map<EventTrigger, List<Function<PlayerOptionalContext>>> events) {
        super(id, materialKey, behaviors, modifiers, clientBoundModifiers, settings, events);
        this.material = material;
    }

    @Override
    public ItemStack buildItemStack(ItemBuildContext context, int count) {
        ItemStack item = new ItemStack(this.material);
        Item<ItemStack> wrapped = BukkitCraftEngine.instance().itemManager().wrap(item);
        wrapped.count(count);
        for (ItemDataModifier<ItemStack> modifier : this.modifiers) {
            modifier.apply(wrapped, context);
        }
        return wrapped.load();
    }

    @Override
    public Item<ItemStack> buildItem(ItemBuildContext context) {
        ItemStack item = new ItemStack(this.material);
        Item<ItemStack> wrapped = BukkitCraftEngine.instance().itemManager().wrap(item);
        for (ItemDataModifier<ItemStack> modifier : dataModifiers()) {
            modifier.apply(wrapped, context);
        }
        return BukkitCraftEngine.instance().itemManager().wrap(wrapped.load());
    }

    public static Builder<ItemStack> builder(Material material) {
        return new BuilderImpl(material);
    }

    public static class BuilderImpl implements Builder<ItemStack> {
        private Holder<Key> id;
        private Key materialKey;
        private final Material material;
        private final Map<EventTrigger, List<Function<PlayerOptionalContext>>> events = new EnumMap<>(EventTrigger.class);
        private final List<ItemBehavior> behaviors = new ArrayList<>(4);
        private final List<ItemDataModifier<ItemStack>> modifiers = new ArrayList<>(4);
        private final List<ItemDataModifier<ItemStack>> clientBoundModifiers = new ArrayList<>(4);
        private ItemSettings settings;

        public BuilderImpl(Material material) {
            this.material = material;
            this.materialKey = KeyUtils.namespacedKey2Key(material.getKey());
        }

        @Override
        public Builder<ItemStack> id(Holder<Key> id) {
            this.id = id;
            return this;
        }

        @Override
        public Builder<ItemStack> material(Key material) {
            this.materialKey = material;
            return this;
        }

        @Override
        public Builder<ItemStack> dataModifier(ItemDataModifier<ItemStack> modifier) {
            this.modifiers.add(modifier);
            return this;
        }

        @Override
        public Builder<ItemStack> dataModifiers(List<ItemDataModifier<ItemStack>> modifiers) {
            this.modifiers.addAll(modifiers);
            return this;
        }

        @Override
        public Builder<ItemStack> clientBoundDataModifier(ItemDataModifier<ItemStack> modifier) {
            this.clientBoundModifiers.add(modifier);
            return this;
        }

        @Override
        public Builder<ItemStack> clientBoundDataModifiers(List<ItemDataModifier<ItemStack>> modifiers) {
            this.clientBoundModifiers.addAll(modifiers);
            return null;
        }

        @Override
        public Builder<ItemStack> behavior(ItemBehavior behavior) {
            this.behaviors.add(behavior);
            return this;
        }

        @Override
        public Builder<ItemStack> behaviors(List<ItemBehavior> behaviors) {
            this.behaviors.addAll(behaviors);
            return this;
        }

        @Override
        public Builder<ItemStack> settings(ItemSettings settings) {
            this.settings = settings;
            return this;
        }

        @Override
        public Builder<ItemStack> events(Map<EventTrigger, List<Function<PlayerOptionalContext>>> events) {
            this.events.putAll(events);
            return this;
        }

        @Override
        public CustomItem<ItemStack> build() {
            this.modifiers.addAll(this.settings.modifiers());
            return new BukkitCustomItem(this.id, this.materialKey, this.material, List.copyOf(this.behaviors),
                    List.copyOf(this.modifiers), List.copyOf(this.clientBoundModifiers), this.settings, this.events);
        }
    }
}
