package net.momirealms.craftengine.bukkit.plugin.reflection.minecraft;

import net.momirealms.craftengine.bukkit.nms.FastNMS;
import net.momirealms.craftengine.bukkit.plugin.reflection.ReflectionInitException;
import net.momirealms.craftengine.core.util.VersionHelper;

public final class MBlocks {
    private MBlocks() {}

    public static final Object AIR;
    public static final Object AIR$defaultState;
    public static final Object STONE;
    public static final Object STONE$defaultState;
    public static final Object FIRE;
    public static final Object SOUL_FIRE;
    public static final Object ICE;
    public static final Object SHORT_GRASS;
    public static final Object SHORT_GRASS$defaultState;

    private static Object getById(String id) throws ReflectiveOperationException {
        Object rl = FastNMS.INSTANCE.method$ResourceLocation$fromNamespaceAndPath("minecraft", id);
        return CoreReflections.method$Registry$get.invoke(MBuiltInRegistries.BLOCK, rl);
    }

    static {
        try {
            AIR = getById("air");
            AIR$defaultState = CoreReflections.method$Block$defaultBlockState.invoke(AIR);
            FIRE = getById("fire");
            SOUL_FIRE = getById("soul_fire");
            STONE = getById("stone");
            STONE$defaultState = CoreReflections.method$Block$defaultBlockState.invoke(STONE);
            ICE = getById("ice");
            SHORT_GRASS = getById(VersionHelper.isOrAbove1_20_3() ? "short_grass" : "grass");
            SHORT_GRASS$defaultState = CoreReflections.method$Block$defaultBlockState.invoke(SHORT_GRASS);
        } catch (ReflectiveOperationException e) {
            throw new ReflectionInitException("Failed to init Blocks", e);
        }
    }
}
