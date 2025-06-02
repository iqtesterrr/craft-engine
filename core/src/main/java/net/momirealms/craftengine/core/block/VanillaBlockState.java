package net.momirealms.craftengine.core.block;

import net.momirealms.craftengine.core.util.Key;

public class VanillaBlockState {
    private final Key type;
    private final String properties;
    private final int registryId;

    public VanillaBlockState(Key type, String properties, int registryId) {
        this.properties = properties;
        this.registryId = registryId;
        this.type = type;
    }

    public String properties() {
        return properties;
    }

    public int registryId() {
        return registryId;
    }

    public Key type() {
        return type;
    }
}
