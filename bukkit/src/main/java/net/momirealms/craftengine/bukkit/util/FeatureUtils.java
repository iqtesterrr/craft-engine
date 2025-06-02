package net.momirealms.craftengine.bukkit.util;

import net.momirealms.craftengine.bukkit.plugin.reflection.minecraft.CoreReflections;
import net.momirealms.craftengine.bukkit.plugin.reflection.minecraft.MRegistries;
import net.momirealms.craftengine.core.util.Key;

public class FeatureUtils {

    private FeatureUtils() {}

    public static Object createFeatureKey(Key id) {
        try {
            return CoreReflections.method$ResourceKey$create.invoke(null, MRegistries.instance$Registries$CONFIGURED_FEATURE, KeyUtils.toResourceLocation(id));
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }
}
