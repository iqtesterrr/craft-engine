package net.momirealms.craftengine.bukkit.plugin.reflection.minecraft;

import net.momirealms.craftengine.bukkit.nms.FastNMS;
import net.momirealms.craftengine.bukkit.plugin.reflection.ReflectionInitException;

public final class MItems {
    private MItems() {}

    public static final Object AIR;
    public static final Object Air$ItemStack;
    public static final Object WATER_BUCKET;

    private static Object getById(String id) throws ReflectiveOperationException {
        Object rl = FastNMS.INSTANCE.method$ResourceLocation$fromNamespaceAndPath("minecraft", id);
        return CoreReflections.method$Registry$get.invoke(MBuiltInRegistries.ITEM, rl);
    }

    static {
        try {
            AIR = getById("air");
            Air$ItemStack = CoreReflections.constructor$ItemStack.newInstance(AIR);
            WATER_BUCKET = getById("water_bucket");
        } catch (ReflectiveOperationException e) {
            throw new ReflectionInitException("Failed to init Items", e);
        }
    }
}
