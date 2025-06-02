package net.momirealms.craftengine.core.item;

import com.google.common.collect.ImmutableMap;
import net.momirealms.craftengine.core.item.behavior.ItemBehavior;
import net.momirealms.craftengine.core.item.modifier.ItemDataModifier;
import net.momirealms.craftengine.core.plugin.context.PlayerOptionalContext;
import net.momirealms.craftengine.core.plugin.context.event.EventTrigger;
import net.momirealms.craftengine.core.plugin.context.function.Function;
import net.momirealms.craftengine.core.registry.Holder;
import net.momirealms.craftengine.core.util.Key;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public abstract class AbstractCustomItem<I> implements CustomItem<I> {
    protected final Holder<Key> id;
    protected final Key material;
    protected final ItemDataModifier<I>[] modifiers;
    protected final Map<String, ItemDataModifier<I>> modifierMap;
    protected final ItemDataModifier<I>[] clientBoundModifiers;
    protected final Map<String, ItemDataModifier<I>> clientBoundModifierMap;
    protected final List<ItemBehavior> behaviors;
    protected final ItemSettings settings;
    protected final Map<EventTrigger, List<Function<PlayerOptionalContext>>> events;

    @SuppressWarnings("unchecked")
    public AbstractCustomItem(Holder<Key> id, Key material,
                              List<ItemBehavior> behaviors,
                              List<ItemDataModifier<I>> modifiers,
                              List<ItemDataModifier<I>> clientBoundModifiers,
                              ItemSettings settings,
                              Map<EventTrigger, List<Function<PlayerOptionalContext>>> events) {
        this.id = id;
        this.material = material;
        this.events = events;
        // unchecked cast
        this.modifiers = modifiers.toArray(new ItemDataModifier[0]);
        // unchecked cast
        this.clientBoundModifiers = clientBoundModifiers.toArray(new ItemDataModifier[0]);
        this.behaviors = List.copyOf(behaviors);
        this.settings = settings;
        ImmutableMap.Builder<String, ItemDataModifier<I>> modifierMapBuilder = ImmutableMap.builder();
        for (ItemDataModifier<I> modifier : modifiers) {
            modifierMapBuilder.put(modifier.name(), modifier);
        }
        this.modifierMap = modifierMapBuilder.build();
        ImmutableMap.Builder<String, ItemDataModifier<I>> clientSideModifierMapBuilder = ImmutableMap.builder();
        this.clientBoundModifierMap = clientSideModifierMapBuilder.build();

    }

    @Override
    public void execute(PlayerOptionalContext context, EventTrigger trigger) {
        for (Function<PlayerOptionalContext> function : Optional.ofNullable(this.events.get(trigger)).orElse(Collections.emptyList())) {
            function.run(context);
        }
    }

    @Override
    public Key id() {
        return this.id.value();
    }

    @Override
    public Holder<Key> idHolder() {
        return this.id;
    }

    @Override
    public Key material() {
        return this.material;
    }

    @Override
    public ItemDataModifier<I>[] dataModifiers() {
        return this.modifiers;
    }

    @Override
    public Map<String, ItemDataModifier<I>> dataModifierMap() {
        return this.modifierMap;
    }

    @Override
    public boolean hasClientBoundDataModifier() {
        return this.clientBoundModifiers.length != 0;
    }

    @Override
    public ItemDataModifier<I>[] clientBoundDataModifiers() {
        return this.clientBoundModifiers;
    }

    @Override
    public Map<String, ItemDataModifier<I>> clientBoundDataModifierMap() {
        return this.clientBoundModifierMap;
    }

    @Override
    public ItemSettings settings() {
        return this.settings;
    }

    @Override
    public @NotNull List<ItemBehavior> behaviors() {
        return this.behaviors;
    }
}
