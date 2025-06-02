package net.momirealms.craftengine.core.item.modifier;

import net.momirealms.craftengine.core.item.ExternalItemProvider;
import net.momirealms.craftengine.core.item.Item;
import net.momirealms.craftengine.core.item.ItemBuildContext;
import net.momirealms.craftengine.core.plugin.CraftEngine;

public class ExternalModifier<I> implements ItemDataModifier<I> {
    private final String id;
    private final ExternalItemProvider<I> provider;

    public ExternalModifier(String id, ExternalItemProvider<I> provider) {
        this.id = id;
        this.provider = provider;
    }

    @Override
    public String name() {
        return "external";
    }

    @SuppressWarnings("unchecked")
    @Override
    public void apply(Item<I> item, ItemBuildContext context) {
        I another = this.provider.build(id, context);
        if (another == null) {
            CraftEngine.instance().logger().warn("'" + id + "' could not be found in " + provider.plugin());
            return;
        }
        Item<I> anotherWrapped = (Item<I>) CraftEngine.instance().itemManager().wrap(another);
        item.merge(anotherWrapped);
    }
}
