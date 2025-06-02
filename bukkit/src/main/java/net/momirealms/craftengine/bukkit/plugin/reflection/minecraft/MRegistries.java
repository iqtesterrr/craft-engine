package net.momirealms.craftengine.bukkit.plugin.reflection.minecraft;

import net.momirealms.craftengine.core.util.VersionHelper;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import static java.util.Objects.requireNonNull;

public final class MRegistries {
    public static final Object instance$Registries$BLOCK;
    public static final Object instance$Registries$ITEM;
    public static final Object instance$Registries$ATTRIBUTE;
    public static final Object instance$Registries$BIOME;
    public static final Object instance$Registries$MOB_EFFECT;
    public static final Object instance$Registries$SOUND_EVENT;
    public static final Object instance$Registries$PARTICLE_TYPE;
    public static final Object instance$Registries$ENTITY_TYPE;
    public static final Object instance$Registries$FLUID;
    public static final Object instance$Registries$RECIPE_TYPE;
    public static final Object instance$Registries$DIMENSION_TYPE;
    public static final Object instance$Registries$CONFIGURED_FEATURE;
    public static final Object instance$Registries$PLACED_FEATURE;
    @Nullable // 1.21+
    public static final Object instance$Registries$JUKEBOX_SONG;

    static {
        Field[] fields = CoreReflections.clazz$Registries.getDeclaredFields();
        try {
            Object registries$Block = null;
            Object registries$Attribute  = null;
            Object registries$Biome  = null;
            Object registries$MobEffect  = null;
            Object registries$SoundEvent  = null;
            Object registries$DimensionType  = null;
            Object registries$ParticleType  = null;
            Object registries$EntityType  = null;
            Object registries$Item  = null;
            Object registries$Fluid  = null;
            Object registries$RecipeType  = null;
            Object registries$ConfiguredFeature  = null;
            Object registries$PlacedFeature  = null;
            Object registries$JukeboxSong  = null;
            for (Field field : fields) {
                Type fieldType = field.getGenericType();
                if (fieldType instanceof ParameterizedType paramType) {
                    if (paramType.getRawType() == CoreReflections.clazz$ResourceKey) {
                        Type[] actualTypeArguments = paramType.getActualTypeArguments();
                        if (actualTypeArguments.length == 1 && actualTypeArguments[0] instanceof ParameterizedType registryType) {
                            Type type = registryType.getActualTypeArguments()[0];
                            if (type instanceof ParameterizedType parameterizedType) {
                                Type rawType = parameterizedType.getRawType();
                                if (rawType == CoreReflections.clazz$ParticleType) {
                                    registries$ParticleType = field.get(null);
                                } else if (rawType == CoreReflections.clazz$EntityType) {
                                    registries$EntityType = field.get(null);
                                } else if (rawType == CoreReflections.clazz$RecipeType) {
                                    registries$RecipeType = field.get(null);
                                } else if (rawType == CoreReflections.clazz$ConfiguredFeature) {
                                    registries$ConfiguredFeature = field.get(null);
                                }
                            } else {
                                if (type == CoreReflections.clazz$Block) {
                                    registries$Block = field.get(null);
                                } else if (type == CoreReflections.clazz$Attribute) {
                                    registries$Attribute = field.get(null);
                                } else if (type == CoreReflections.clazz$Biome) {
                                    registries$Biome = field.get(null);
                                } else if (type == CoreReflections.clazz$MobEffect) {
                                    registries$MobEffect = field.get(null);
                                } else if (type == CoreReflections.clazz$SoundEvent) {
                                    registries$SoundEvent = field.get(null);
                                } else if (type == CoreReflections.clazz$DimensionType) {
                                    registries$DimensionType = field.get(null);
                                } else if (type == CoreReflections.clazz$Item) {
                                    registries$Item = field.get(null);
                                } else if (type == CoreReflections.clazz$Fluid) {
                                    registries$Fluid = field.get(null);
                                } else if (VersionHelper.isOrAbove1_21() && type == CoreReflections.clazz$JukeboxSong) {
                                    registries$JukeboxSong = field.get(null);
                                } else if (type == CoreReflections.clazz$PlacedFeature) {
                                    registries$PlacedFeature = field.get(null);
                                }
                            }
                        }
                    }
                }
            }
            instance$Registries$BLOCK = requireNonNull(registries$Block);
            instance$Registries$ITEM = requireNonNull(registries$Item);
            instance$Registries$ATTRIBUTE = requireNonNull(registries$Attribute);
            instance$Registries$BIOME = requireNonNull(registries$Biome);
            instance$Registries$MOB_EFFECT = requireNonNull(registries$MobEffect);
            instance$Registries$SOUND_EVENT = requireNonNull(registries$SoundEvent);
            instance$Registries$DIMENSION_TYPE = requireNonNull(registries$DimensionType);
            instance$Registries$PARTICLE_TYPE = requireNonNull(registries$ParticleType);
            instance$Registries$ENTITY_TYPE = requireNonNull(registries$EntityType);
            instance$Registries$FLUID = requireNonNull(registries$Fluid);
            instance$Registries$RECIPE_TYPE = requireNonNull(registries$RecipeType);
            instance$Registries$CONFIGURED_FEATURE = requireNonNull(registries$ConfiguredFeature);
            instance$Registries$PLACED_FEATURE = requireNonNull(registries$PlacedFeature);
            instance$Registries$JUKEBOX_SONG = registries$JukeboxSong;
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }
}
