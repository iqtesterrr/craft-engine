package net.momirealms.craftengine.bukkit.plugin.reflection.minecraft;

import net.momirealms.craftengine.bukkit.nms.FastNMS;
import net.momirealms.craftengine.bukkit.plugin.reflection.ReflectionInitException;

public final class MFluids {
    private MFluids() {}

    public static final Object instance$Fluids$WATER;
    public static final Object instance$Fluids$FLOWING_WATER;
    public static final Object instance$Fluids$LAVA;
    public static final Object instance$Fluids$FLOWING_LAVA;
    public static final Object instance$Fluids$EMPTY;
    public static final Object instance$Fluids$EMPTY$defaultState;

    private static Object getById(String id) throws ReflectiveOperationException {
        Object rl = FastNMS.INSTANCE.method$ResourceLocation$fromNamespaceAndPath("minecraft", id);
        return CoreReflections.method$Registry$get.invoke(MBuiltInRegistries.FLUID, rl);
    }

    static {
        try {
            instance$Fluids$WATER = getById("water");
            instance$Fluids$FLOWING_WATER = getById("flowing_water");
            instance$Fluids$LAVA = getById("lava");
            instance$Fluids$FLOWING_LAVA = getById("flowing_lava");
            instance$Fluids$EMPTY = getById("empty");
            instance$Fluids$EMPTY$defaultState = CoreReflections.method$Fluid$defaultFluidState.invoke(instance$Fluids$EMPTY);
        } catch (ReflectiveOperationException e) {
            throw new ReflectionInitException("Failed to init Fluids", e);
        }
    }
}
