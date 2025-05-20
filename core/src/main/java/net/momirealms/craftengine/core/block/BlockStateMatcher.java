package net.momirealms.craftengine.core.block;

import net.momirealms.craftengine.core.block.properties.Property;
import net.momirealms.craftengine.core.registry.BuiltInRegistries;
import net.momirealms.craftengine.core.registry.Holder;
import net.momirealms.craftengine.core.util.Key;
import net.momirealms.craftengine.core.util.Pair;
import net.momirealms.craftengine.core.util.StringReader;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class BlockStateMatcher {
    private final List<Pair<Property<?>, Comparable<?>>> properties;
    private final Key id;

    public BlockStateMatcher(Key id, List<Pair<Property<?>, Comparable<?>>> properties) {
        this.properties = properties;
        this.id = id;
    }

    public boolean matches(ImmutableBlockState state) {
        if (!state.owner().value().id().equals(this.id)) {
            return false;
        }
        for (Pair<Property<?>, Comparable<?>> pair : this.properties) {
            if (!state.get(pair.left()).equals(pair.right())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        if (properties.isEmpty()) {
            return id.toString();
        }
        return id + "[" + getPropertiesAsString() + "]";
    }

    public String getPropertiesAsString() {
        return properties.stream()
                .map(entry -> {
                    Property<?> property = entry.left();
                    return property.name() + "=" + Property.formatValue(property, entry.right());
                })
                .collect(Collectors.joining(","));
    }

    @SuppressWarnings("DuplicatedCode")
    @Nullable
    public static BlockStateMatcher deserialize(@NotNull String data) {
        StringReader reader = StringReader.simple(data);
        String blockIdString = reader.readUnquotedString();
        if (reader.canRead() && reader.peek() == ':') {
            reader.skip();
            blockIdString = blockIdString + ":" + reader.readUnquotedString();
        }
        Optional<Holder.Reference<CustomBlock>> optional = BuiltInRegistries.BLOCK.get(Key.from(blockIdString));
        if (optional.isEmpty()) {
            return null;
        }
        Holder<CustomBlock> holder = optional.get();
        List<Pair<Property<?>, Comparable<?>>> properties = new ArrayList<>();
        if (reader.canRead() && reader.peek() == '[') {
            reader.skip();
            while (reader.canRead()) {
                reader.skipWhitespace();
                if (reader.peek() == ']') break;
                String propertyName = reader.readUnquotedString();
                reader.skipWhitespace();
                if (!reader.canRead() || reader.peek() != '=') {
                    return null;
                }
                reader.skip();
                reader.skipWhitespace();
                String propertyValue = reader.readUnquotedString();
                Property<?> property = holder.value().getProperty(propertyName);
                if (property != null) {
                    Optional<?> optionalValue = property.optional(propertyValue);
                    if (optionalValue.isEmpty()) {
                        return null;
                    } else {
                        properties.add(Pair.of(property, (Comparable<?>) optionalValue.get()));
                    }
                } else {
                    return null;
                }
                reader.skipWhitespace();
                if (reader.canRead() && reader.peek() == ',') {
                    reader.skip();
                }
            }
            reader.skipWhitespace();
            if (reader.canRead() && reader.peek() == ']') {
                reader.skip();
            } else {
                return null;
            }
        }
        return new BlockStateMatcher(holder.value().id(), properties);
    }
}
