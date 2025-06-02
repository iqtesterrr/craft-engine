package net.momirealms.craftengine.bukkit.plugin.reflection.minecraft;

import net.momirealms.craftengine.bukkit.nms.FastNMS;
import net.momirealms.craftengine.bukkit.plugin.reflection.ReflectionInitException;

public final class MSoundEvents {
    private MSoundEvents() {}

    public static final Object instance$SoundEvent$EMPTY;
    public static final Object instance$SoundEvent$TRIDENT_RIPTIDE_1;
    public static final Object instance$SoundEvent$TRIDENT_RIPTIDE_2;
    public static final Object instance$SoundEvent$TRIDENT_RIPTIDE_3;
    public static final Object instance$SoundEvent$TRIDENT_THROW;

    private static Object getById(String id) throws ReflectiveOperationException {
        Object rl = FastNMS.INSTANCE.method$ResourceLocation$fromNamespaceAndPath("minecraft", id);
        return CoreReflections.method$Registry$get.invoke(MBuiltInRegistries.SOUND_EVENT, rl);
    }

    static {
        try {
            instance$SoundEvent$EMPTY = getById("intentionally_empty");
            instance$SoundEvent$TRIDENT_RIPTIDE_1 = getById("item.trident_riptide_1");
            instance$SoundEvent$TRIDENT_RIPTIDE_2 = getById("item.trident_riptide_2");
            instance$SoundEvent$TRIDENT_RIPTIDE_3 = getById("item.trident.riptide_3");
            instance$SoundEvent$TRIDENT_THROW = getById("item.trident.throw");
        } catch (ReflectiveOperationException e) {
            throw new ReflectionInitException("Failed to init SoundEvents", e);
        }
    }
}
