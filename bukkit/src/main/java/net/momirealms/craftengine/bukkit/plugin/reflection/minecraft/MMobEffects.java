package net.momirealms.craftengine.bukkit.plugin.reflection.minecraft;

import net.momirealms.craftengine.bukkit.nms.FastNMS;
import net.momirealms.craftengine.bukkit.plugin.reflection.ReflectionInitException;

public final class MMobEffects {
    private MMobEffects() {}

    public static final Object MINING_FATIGUE;
    public static final Object HASTE;
    public static final Object INVISIBILITY;

    private static Object getById(String id) throws ReflectiveOperationException {
        Object rl = FastNMS.INSTANCE.method$ResourceLocation$fromNamespaceAndPath("minecraft", id);
        return CoreReflections.method$Registry$get.invoke(MBuiltInRegistries.MOB_EFFECT, rl);
    }

    // for 1.20.1-1.20.4
    static {
        try {
            MINING_FATIGUE = getById("mining_fatigue");
            HASTE = getById("haste");
            INVISIBILITY = getById("invisibility");
        } catch (ReflectiveOperationException e) {
            throw new ReflectionInitException("Failed to init MobEffects", e);
        }
    }
}
