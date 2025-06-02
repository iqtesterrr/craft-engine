package net.momirealms.craftengine.core.item.modifier;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.momirealms.craftengine.core.item.ComponentKeys;
import net.momirealms.craftengine.core.item.Item;
import net.momirealms.craftengine.core.item.ItemBuildContext;
import net.momirealms.craftengine.core.item.NetworkItemHandler;
import net.momirealms.craftengine.core.util.GsonHelper;
import net.momirealms.craftengine.core.util.Key;
import net.momirealms.craftengine.core.util.Pair;
import net.momirealms.sparrow.nbt.CompoundTag;
import net.momirealms.sparrow.nbt.Tag;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ComponentModifier<I> implements ItemDataModifier<I> {
    private final List<Pair<Key, Object>> arguments;
    private JsonObject customData = null;

    public ComponentModifier(Map<String, Object> arguments) {
        List<Pair<Key, Object>> pairs = new ArrayList<>(arguments.size());
        for (Map.Entry<String, Object> entry : arguments.entrySet()) {
            Key key = Key.of(entry.getKey());
            if (key.equals(ComponentKeys.CUSTOM_DATA)) {
                this.customData = parseJsonObjectValue(entry.getValue());
            } else {
                pairs.add(new Pair<>(key, parseValue(entry.getValue())));
            }
        }
        this.arguments = pairs;
    }

    public List<Pair<Key, Object>> arguments() {
        return arguments;
    }

    private Object parseValue(Object value) {
        if (value instanceof String string) {
            if (string.startsWith("(json) ")) {
                return GsonHelper.get().fromJson(string.substring("(json) ".length()), JsonElement.class);
            }
        }
        return value;
    }

    private JsonObject parseJsonObjectValue(Object value) {
        if (value instanceof String string) {
            if (string.startsWith("(json) ")) {
                return GsonHelper.get().fromJson(string.substring("(json) ".length()), JsonObject.class);
            }
        } else if (value instanceof Map<?,?> map) {
            return (JsonObject) GsonHelper.get().toJsonTree(map, Map.class);
        }
        throw new UnsupportedOperationException("Invalid minecraft:custom_data value: " + value.toString());
    }

    @Override
    public String name() {
        return "components";
    }

    @Override
    public void apply(Item<I> item, ItemBuildContext context) {
        for (Pair<Key, Object> entry : this.arguments) {
            item.setComponent(entry.left(), entry.right());
        }
        if (this.customData != null) {
            JsonObject tag = (JsonObject) item.getJsonComponent(ComponentKeys.CUSTOM_DATA);
            if (tag != null) {
                item.setComponent(ComponentKeys.CUSTOM_DATA, GsonHelper.shallowMerge(this.customData, tag));
            } else {
                item.setComponent(ComponentKeys.CUSTOM_DATA, this.customData);
            }
        }
    }

    @Override
    public void prepareNetworkItem(Item<I> item, ItemBuildContext context, CompoundTag networkData) {
        for (Pair<Key, Object> entry : this.arguments) {
            Tag previous = item.getNBTComponent(entry.left());
            if (previous != null) {
                networkData.put(entry.left().asString(), NetworkItemHandler.pack(NetworkItemHandler.Operation.ADD, previous));
            } else {
                networkData.put(entry.left().asString(), NetworkItemHandler.pack(NetworkItemHandler.Operation.REMOVE));
            }
        }
    }
}
