package net.momirealms.craftengine.bukkit.plugin.reflection.minecraft;

import com.google.common.collect.ForwardingMultimap;
import com.google.common.collect.ImmutableList;
import com.google.gson.JsonElement;
import com.mojang.serialization.DynamicOps;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import net.momirealms.craftengine.bukkit.plugin.reflection.ReflectionInitException;
import net.momirealms.craftengine.bukkit.util.BukkitReflectionUtils;
import net.momirealms.craftengine.core.util.ReflectionUtils;
import net.momirealms.craftengine.core.util.VersionHelper;

import java.io.BufferedReader;
import java.lang.invoke.VarHandle;
import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static java.util.Objects.requireNonNull;

public final class CoreReflections {

    public static final Class<?> clazz$RandomSource = requireNonNull(
            ReflectionUtils.getClazz(BukkitReflectionUtils.assembleMCClass("util.RandomSource"))
    );

    public static final Class<?> clazz$BitStorage = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "util.DataBits",
                    "util.BitStorage"
            )
    );

    public static final Method method$BitStorage$getBits = requireNonNull(
            ReflectionUtils.getMethod(clazz$BitStorage, int.class)
    );

    public static final Method method$BitStorage$getRaw = requireNonNull(
            ReflectionUtils.getMethod(clazz$BitStorage, long[].class)
    );

    public static final Method method$RandomSource$nextFloat = requireNonNull(
            ReflectionUtils.getMethod(clazz$RandomSource, float.class)
    );

    public static final Class<?> clazz$CrudeIncrementalIntIdentityHashBiMap = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "util.RegistryID",
                    "util.CrudeIncrementalIntIdentityHashBiMap"
            )
    );

    public static final Field field$CrudeIncrementalIntIdentityHashBiMap$keys = requireNonNull(
            ReflectionUtils.getDeclaredField(clazz$CrudeIncrementalIntIdentityHashBiMap, Object.class.arrayType(), 0)
    );
    
    public static final Class<?> clazz$ResourceLocation = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "resources.MinecraftKey",
                    "resources.ResourceLocation"
            )
    );

    public static final Class<?> clazz$ResourceKey = requireNonNull(
            ReflectionUtils.getClazz(BukkitReflectionUtils.assembleMCClass("resources.ResourceKey"))
    );

    public static final Field field$ResourceKey$registry = requireNonNull(
            ReflectionUtils.getDeclaredField(clazz$ResourceKey, clazz$ResourceLocation, 0)
    );

    public static final Field field$ResourceKey$location = requireNonNull(
            ReflectionUtils.getDeclaredField(clazz$ResourceKey, clazz$ResourceLocation, 1)
    );

    public static final Method method$ResourceKey$create = requireNonNull(
            ReflectionUtils.getStaticMethod(clazz$ResourceKey, clazz$ResourceKey, clazz$ResourceKey, clazz$ResourceLocation)
    );

    public static final Method method$ResourceLocation$fromNamespaceAndPath = requireNonNull(
            ReflectionUtils.getStaticMethod(clazz$ResourceLocation, clazz$ResourceLocation, String.class, String.class)
    );

    public static final Class<?> clazz$FileToIdConverter = requireNonNull(
            ReflectionUtils.getClazz(BukkitReflectionUtils.assembleMCClass("resources.FileToIdConverter"))
    );

    public static final Method method$FileToIdConverter$json = requireNonNull(
            ReflectionUtils.getStaticMethod(clazz$FileToIdConverter, clazz$FileToIdConverter, String.class)
    );
    
    public static final Class<?> clazz$RegistryOps = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "resources.RegistryOps",
                    "resources.RegistryOps"
            )
    );

    public static final Class<?> clazz$SoundEvent = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "sounds.SoundEffect",
                    "sounds.SoundEvent"
            )
    );

    public static final Constructor<?> constructor$SoundEvent = requireNonNull(
            VersionHelper.isOrAbove1_21_2() ?
                    ReflectionUtils.getConstructor(clazz$SoundEvent, clazz$ResourceLocation, Optional.class) :
                    ReflectionUtils.getDeclaredConstructor(clazz$SoundEvent, clazz$ResourceLocation, float.class, boolean.class)
    );

    // 1.21.2+
    public static final Field field$SoundEvent$fixedRange = ReflectionUtils.getInstanceDeclaredField(
            clazz$SoundEvent, Optional.class, 0
    );

    // 1.21.2-
    public static final Field field$SoundEvent$range = ReflectionUtils.getInstanceDeclaredField(
            clazz$SoundEvent, float.class, 0
    );

    public static final Field field$SoundEvent$newSystem = ReflectionUtils.getInstanceDeclaredField(
            clazz$SoundEvent, boolean.class, 0
    );

    public static final Method method$SoundEvent$createVariableRangeEvent = requireNonNull(
            ReflectionUtils.getStaticMethod(
                    clazz$SoundEvent, clazz$SoundEvent, clazz$ResourceLocation
            )
    );

    public static final Field field$SoundEvent$location = requireNonNull(
            ReflectionUtils.getInstanceDeclaredField(clazz$SoundEvent, clazz$ResourceLocation, 0)
    );

    public static final Class<?> clazz$SoundSource = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "sounds.SoundCategory",
                    "sounds.SoundSource"
            )
    );

    public static final Method method$SoundSource$values = requireNonNull(
            ReflectionUtils.getStaticMethod(clazz$SoundSource, clazz$SoundSource.arrayType())
    );

    public static final Object instance$SoundSource$MASTER;
    public static final Object instance$SoundSource$MUSIC;
    public static final Object instance$SoundSource$RECORDS;
    public static final Object instance$SoundSource$WEATHER;
    public static final Object instance$SoundSource$BLOCKS;
    public static final Object instance$SoundSource$HOSTILE;
    public static final Object instance$SoundSource$NEUTRAL;
    public static final Object instance$SoundSource$PLAYERS;
    public static final Object instance$SoundSource$AMBIENT;
    public static final Object instance$SoundSource$VOICE;

    static {
        try {
            Object[] values = (Object[]) method$SoundSource$values.invoke(null);
            instance$SoundSource$MASTER = values[0];
            instance$SoundSource$MUSIC = values[1];
            instance$SoundSource$RECORDS = values[2];
            instance$SoundSource$WEATHER = values[3];
            instance$SoundSource$BLOCKS = values[4];
            instance$SoundSource$HOSTILE = values[5];
            instance$SoundSource$NEUTRAL = values[6];
            instance$SoundSource$PLAYERS = values[7];
            instance$SoundSource$AMBIENT = values[8];
            instance$SoundSource$VOICE = values[9];
        } catch (ReflectiveOperationException e) {
            throw new ReflectionInitException("Failed to init SoundSource", e);
        }
    }

    // 1.21+
    public static final Class<?> clazz$PacketReport =
            ReflectionUtils.getClazz(BukkitReflectionUtils.assembleMCClass("data.info.PacketReport"));

    // 1.21+
    public static final Constructor<?> constructor$PacketReport = Optional.ofNullable(clazz$PacketReport)
            .map(it -> ReflectionUtils.getConstructor(it, 0))
            .orElse(null);

    // 1.21+
    public static final Method method$PacketReport$serializePackets = Optional.ofNullable(clazz$PacketReport)
            .map(it -> ReflectionUtils.getDeclaredMethod(it, JsonElement.class))
            .orElse(null);

    public static final Class<?> clazz$Component = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "network.chat.IChatBaseComponent",
                    "network.chat.Component"
            )
    );

    public static final Class<?> clazz$Component$Serializer = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "network.chat.IChatBaseComponent$ChatSerializer",
                    "network.chat.Component$Serializer"
            )
    );

    public static final Class<?> clazz$ComponentContents = requireNonNull(
            ReflectionUtils.getClazz(
                    BukkitReflectionUtils.assembleMCClass("network.chat.ComponentContents")
            )
    );

    public static final Method method$Component$empty = requireNonNull(
            ReflectionUtils.getStaticMethod(clazz$Component, clazz$Component)
    );

    public static final Object instance$Component$empty;

    static {
        try {
            instance$Component$empty = method$Component$empty.invoke(null);
        } catch (ReflectiveOperationException e) {
            throw new ReflectionInitException("Failed to get empty component", e);
        }
    }

    public static final Method method$Component$getString = requireNonNull(
            ReflectionUtils.getMethod(clazz$Component, String.class, new String[]{"getString", "a"})
    );

    public static final Class<?> clazz$HolderLookup$Provider = BukkitReflectionUtils.findReobfOrMojmapClass(
            VersionHelper.isOrAbove1_20_5() ? "core.HolderLookup$a" : "core.HolderLookup$b",
            "core.HolderLookup$Provider"
    );

    public static final Class<?> clazz$Holder = requireNonNull(
            ReflectionUtils.getClazz(BukkitReflectionUtils.assembleMCClass("core.Holder"))
    );

    // 1.20.6+
    public static final Method method$Holder$getRegisteredName = ReflectionUtils.getMethod(clazz$Holder, String.class);

    public static final Class<?> clazz$Holder$Reference = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "core.Holder$c",
                    "core.Holder$Reference"
            )
    );

    public static final Method method$Holder$value = requireNonNull(
            ReflectionUtils.getMethod(clazz$Holder, new String[]{"a", "value"})
    );

    public static final Method method$Holder$direct = requireNonNull(
            ReflectionUtils.getStaticMethod(clazz$Holder, clazz$Holder, Object.class)
    );

    public static final Class<?> clazz$LayeredRegistryAccess = requireNonNull(
            ReflectionUtils.getClazz(BukkitReflectionUtils.assembleMCClass("core.LayeredRegistryAccess"))
    );

    public static final Class<?> clazz$RegistryAccess$Frozen = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "core.IRegistryCustom$Dimension",
                    "core.RegistryAccess$Frozen"
            )
    );

    public static final Class<?> clazz$RegistryAccess = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "core.IRegistryCustom",
                    "core.RegistryAccess"
            )
    );

    public static final Field field$LayeredRegistryAccess$composite = requireNonNull(
            ReflectionUtils.getDeclaredField(clazz$LayeredRegistryAccess, clazz$RegistryAccess$Frozen, 0)
    );

    public static final Class<?> clazz$Registry = requireNonNull(
            requireNonNull(BukkitReflectionUtils.findReobfOrMojmapClass(
                    "core.IRegistryWritable",
                    "core.WritableRegistry"
            )).getInterfaces()[0]
    );

    public static final Method method$RegistryAccess$registryOrThrow = requireNonNull(
            ReflectionUtils.getMethod(
                    clazz$RegistryAccess, clazz$Registry, clazz$ResourceKey
            )
    );

    public static final Method method$Registry$register = requireNonNull(
            ReflectionUtils.getStaticMethod(
                    clazz$Registry, Object.class, clazz$Registry, clazz$ResourceLocation, Object.class
            )
    );

    public static final Method method$Registry$registerForHolder = requireNonNull(
            ReflectionUtils.getStaticMethod(
                    clazz$Registry, clazz$Holder$Reference, clazz$Registry, clazz$ResourceLocation, Object.class
            )
    );

    public static final Method method$Holder$Reference$bindValue = requireNonNull(
            ReflectionUtils.getDeclaredMethod(
                    clazz$Holder$Reference, void.class, Object.class
            )
    );

    public static final Class<?> clazz$Registries = requireNonNull(
            ReflectionUtils.getClazz(BukkitReflectionUtils.assembleMCClass("core.registries.Registries"))
    );

    public static final Class<?> clazz$BuiltInRegistries = requireNonNull(
            ReflectionUtils.getClazz(BukkitReflectionUtils.assembleMCClass("core.registries.BuiltInRegistries"))
    );

    public static final Class<?> clazz$DefaultedRegistry = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "core.RegistryBlocks",
                    "core.DefaultedRegistry"
            )
    );

    public static final Method method$Registry$getKey = requireNonNull(
            ReflectionUtils.getMethod(clazz$Registry, clazz$ResourceLocation, Object.class)
    );

    public static final Method method$Registry$get = requireNonNull(
            ReflectionUtils.getMethods(
                    clazz$Registry, Object.class, clazz$ResourceLocation
            ).stream().filter(m -> m.getReturnType() != Optional.class).findAny().orElse(null)
    );

    // use ResourceLocation
    public static final Method method$Registry$getHolder0;
    // use ResourceKey
    public static final Method method$Registry$getHolder1;

    static {
        List<Method> methods = ReflectionUtils.getMethods(clazz$Registry, Optional.class, clazz$ResourceLocation);
        Method theMethod1 = null;
        for (Method method : methods) {
            Type returnType = method.getGenericReturnType();
            if (method.getParameterCount() == 1 && method.getParameterTypes()[0] == clazz$ResourceLocation) {
                if (returnType instanceof ParameterizedType parameterizedType) {
                    Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
                    if (actualTypeArguments.length == 1) {
                        if (actualTypeArguments[0] instanceof ParameterizedType) {
                            theMethod1 = method;
                        }
                    }
                }
            }
        }
        method$Registry$getHolder0 = theMethod1;
    }

    static {
        List<Method> methods = ReflectionUtils.getMethods(clazz$Registry, Optional.class, clazz$ResourceKey);
        Method theMethod1 = null;
        for (Method method : methods) {
            Type returnType = method.getGenericReturnType();
            if (method.getParameterCount() == 1 && method.getParameterTypes()[0] == clazz$ResourceKey) {
                if (returnType instanceof ParameterizedType parameterizedType) {
                    Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
                    if (actualTypeArguments.length == 1) {
                        if (actualTypeArguments[0] instanceof ParameterizedType) {
                            theMethod1 = method;
                        }
                    }
                }
            }
        }
        method$Registry$getHolder1 = theMethod1;
    }

    public static final Class<?> clazz$BlockPos = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "core.BlockPosition",
                    "core.BlockPos"
            )
    );

    public static final Class<?> clazz$SectionPos = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "core.SectionPosition",
                    "core.SectionPos"
            )
    );

    public static final Class<?> clazz$Vec3i = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "core.BaseBlockPosition",
                    "core.Vec3i"
            )
    );

    public static final Class<?> clazz$IdMapper = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "core.RegistryBlockID",
                    "core.IdMapper"
            )
    );

    public static final Class<?> clazz$IdMap = requireNonNull(
            clazz$IdMapper.getInterfaces()[0]
    );

    public static final Method method$IdMap$byId = requireNonNull(
            ReflectionUtils.getMethod(clazz$IdMap, Object.class, int.class)
    );

    public static final Method method$IdMap$size = requireNonNull(
            ReflectionUtils.getMethod(clazz$IdMap, int.class)
    );

    public static final Method method$IdMapper$size = requireNonNull(
            ReflectionUtils.getMethod(clazz$IdMapper, int.class)
    );

    public static final Method method$IdMapper$getId = requireNonNull(
            ReflectionUtils.getMethod(clazz$IdMapper, int.class, Object.class)
    );

    public static final Method method$IdMapper$byId = requireNonNull(
            ReflectionUtils.getMethod(clazz$IdMapper, Object.class, int.class)
    );

    public static final Method method$Registry$asHolderIdMap = requireNonNull(
            ReflectionUtils.getMethod(clazz$Registry, clazz$IdMap)
    );

    public static final Class<?> clazz$Direction = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "core.EnumDirection",
                    "core.Direction"
            )
    );

    public static final Method method$Direction$ordinal = requireNonNull(
            ReflectionUtils.getMethod(
                    clazz$Direction, new String[]{"ordinal"}
            )
    );

    public static final Method method$Direction$values = requireNonNull(
            ReflectionUtils.getStaticMethod(
                    clazz$Direction, clazz$Direction.arrayType()
            )
    );

    public static final Object instance$Direction$DOWN;
    public static final Object instance$Direction$UP;
    public static final Object instance$Direction$NORTH;
    public static final Object instance$Direction$SOUTH;
    public static final Object instance$Direction$WEST;
    public static final Object instance$Direction$EAST;
    public static final Object[] instance$Directions;

    static {
        try {
            instance$Directions = (Object[]) method$Direction$values.invoke(null);
            instance$Direction$DOWN = instance$Directions[0];
            instance$Direction$UP = instance$Directions[1];
            instance$Direction$NORTH = instance$Directions[2];
            instance$Direction$SOUTH = instance$Directions[3];
            instance$Direction$WEST = instance$Directions[4];
            instance$Direction$EAST = instance$Directions[5];
        } catch (ReflectiveOperationException e) {
            throw new ReflectionInitException("Failed to init Direction", e);
        }
    }

    public static final Method method$Vec3i$relative = requireNonNull(
            ReflectionUtils.getMethod(clazz$Vec3i, clazz$Vec3i, clazz$Direction)
    );

    public static final Method method$BlockPos$relative = requireNonNull(
            ReflectionUtils.getMethod(clazz$BlockPos, clazz$BlockPos, clazz$Direction)
    );

    public static final Class<?> clazz$ParticleType = requireNonNull(
            Optional.of(Objects.requireNonNull(BukkitReflectionUtils.findReobfOrMojmapClass("core.particles.Particle", "core.particles.ParticleType"))).map(it -> {
                if (it.getSuperclass() != Object.class) {
                    return it.getSuperclass();
                }
                return it;
            }).orElseThrow()
    );

    public static final Class<?> clazz$MappedRegistry = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "core.RegistryMaterials",
                    "core.MappedRegistry"
            )
    );

    public static final Field field$MappedRegistry$frozen = requireNonNull(
            ReflectionUtils.getDeclaredField(clazz$MappedRegistry, boolean.class, 0)
    );

    public static final Method method$MappedRegistry$freeze = requireNonNull(
            ReflectionUtils.getMethod(clazz$MappedRegistry, clazz$Registry)
    );

    public static final Field field$MappedRegistry$byValue = requireNonNull(
            ReflectionUtils.getDeclaredField(clazz$MappedRegistry, Map.class, 2)
    );

    public static final Field field$MappedRegistry$unregisteredIntrusiveHolders = requireNonNull(
            ReflectionUtils.getDeclaredField(clazz$MappedRegistry, Map.class, 5)
    );

    public static final Class<?> clazz$MappedRegistry$TagSet =
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "RegistryMaterials$a",
                    "core.MappedRegistry$TagSet"
            );

    public static final Field field$MappedRegistry$allTags = Optional.ofNullable(clazz$MappedRegistry$TagSet)
            .map(it -> ReflectionUtils.getDeclaredField(clazz$MappedRegistry, it, 0))
            .orElse(null);

    public static final Method method$MappedRegistry$TagSet$unbound = Optional.ofNullable(clazz$MappedRegistry$TagSet)
            .map(it -> ReflectionUtils.getStaticMethod(clazz$MappedRegistry$TagSet, clazz$MappedRegistry$TagSet))
            .orElse(null);

    public static final Method method$TagSet$forEach = Optional.ofNullable(clazz$MappedRegistry$TagSet)
            .map(it -> ReflectionUtils.getDeclaredMethod(clazz$MappedRegistry$TagSet, void.class, BiConsumer.class))
            .orElse(null);

    public static final Method method$Holder$Reference$bingTags = requireNonNull(
            ReflectionUtils.getDeclaredMethod(clazz$Holder$Reference, void.class, Collection.class)
    );

    public static final Class<?> clazz$ParticleOptions = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "core.particles.ParticleParam",
                    "core.particles.ParticleOptions"
            )
    );

    public static final Class<?> clazz$BlockParticleOption = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "core.particles.ParticleParamBlock",
                    "core.particles.BlockParticleOption"
            )
    );
    
    public static final Field field$Holder$Reference$tags = requireNonNull(
            ReflectionUtils.getDeclaredField(clazz$Holder$Reference, Set.class, 0)
    );

    public static final Class<?> clazz$TagKey = requireNonNull(
            ReflectionUtils.getClazz(BukkitReflectionUtils.assembleMCClass("tags.TagKey"))
    );

    public static final Field field$TagKey$location = requireNonNull(
            ReflectionUtils.getInstanceDeclaredField(clazz$TagKey, clazz$ResourceLocation, 0)
    );

    public static final Method method$TagKey$create = requireNonNull(
            ReflectionUtils.getStaticMethod(clazz$TagKey, clazz$TagKey, clazz$ResourceKey, clazz$ResourceLocation)
    );

    public static final Field field$Direction$data3d = requireNonNull(
            ReflectionUtils.getInstanceDeclaredField(clazz$Direction, int.class, 0)
    );

    public static final Field field$Holder$Reference$value = requireNonNull(
            ReflectionUtils.getDeclaredField(clazz$Holder$Reference, Object.class, 0)
    );

    public static final Class<?> clazz$MutableBlockPos = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "core.BlockPosition$MutableBlockPosition",
                    "core.BlockPos$MutableBlockPos"
            )
    );

    public static final Constructor<?> constructor$MutableBlockPos = requireNonNull(
            ReflectionUtils.getConstructor(clazz$MutableBlockPos)
    );

    public static final Method method$MutableBlockPos$setWithOffset = requireNonNull(
            ReflectionUtils.getMethod(clazz$MutableBlockPos, clazz$MutableBlockPos, clazz$Vec3i, clazz$Direction)
    );

    public static final Method method$BlockPos$mutable = requireNonNull(
            ReflectionUtils.getMethod(clazz$BlockPos, clazz$MutableBlockPos)
    );

    public static final Class<?> clazz$NonNullList = requireNonNull(
            ReflectionUtils.getClazz(BukkitReflectionUtils.assembleMCClass("core.NonNullList"))
    );

    public static final Method method$NonNullList$set = requireNonNull(
            ReflectionUtils.getMethod(clazz$NonNullList, Object.class, int.class, Object.class)
    );

    // 1.20.5+
    public static final Class<?> clazz$DataComponentPatch = ReflectionUtils.getClazz(
            BukkitReflectionUtils.assembleMCClass("core.component.DataComponentPatch")
    );

    public static final Class<?> clazz$DataComponentType = ReflectionUtils.getClazz(
            BukkitReflectionUtils.assembleMCClass("core.component.DataComponentType")
    );

    public static final Method method$Registry$getId = requireNonNull(
            ReflectionUtils.getMethod(clazz$Registry, int.class, Object.class)
    );

    public static final Class<?> clazz$Tag = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "nbt.NBTBase",
                    "nbt.Tag"
            )
    );

    public static final Class<?> clazz$EntityDataSerializers = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "network.syncher.DataWatcherRegistry",
                    "network.syncher.EntityDataSerializers"
            )
    );

    public static final Class<?> clazz$EntityDataSerializer = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "network.syncher.DataWatcherSerializer",
                    "network.syncher.EntityDataSerializer"
            )
    );

    public static final Class<?> clazz$EntityDataAccessor = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "network.syncher.DataWatcherObject",
                    "network.syncher.EntityDataAccessor"
            )
    );

    public static final Class<?> clazz$SynchedEntityData = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "network.syncher.DataWatcher",
                    "network.syncher.SynchedEntityData"
            )
    );

    public static final Method method$SynchedEntityData$get = requireNonNull(
            ReflectionUtils.getMethod(clazz$SynchedEntityData, Object.class, clazz$EntityDataAccessor)
    );

    public static final Class<?> clazz$SynchedEntityData$DataValue = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "network.syncher.DataWatcher$b",
                    "network.syncher.SynchedEntityData$DataValue"
            )
    );
    
    public static final Class<?> clazz$FriendlyByteBuf = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "network.PacketDataSerializer",
                    "network.FriendlyByteBuf"
            )
    );

    public static final Class<?> clazz$RegistryFriendlyByteBuf =
            ReflectionUtils.getClazz(
                    BukkitReflectionUtils.assembleMCClass("network.RegistryFriendlyByteBuf")
            );

    public static final Constructor<?> constructor$RegistryFriendlyByteBuf = Optional.ofNullable(clazz$RegistryFriendlyByteBuf)
            .map(it -> ReflectionUtils.getConstructor(it, 0))
            .orElse(null);

    public static final Constructor<?> constructor$FriendlyByteBuf = requireNonNull(
            ReflectionUtils.getConstructor(clazz$FriendlyByteBuf, ByteBuf.class)
    );

    public static final Method method$FriendlyByteBuf$writeByte = requireNonNull(
            ReflectionUtils.getMethod(clazz$FriendlyByteBuf, clazz$FriendlyByteBuf, int.class)
    );

    public static final Method method$FriendlyByteBuf$writeLongArray = requireNonNull(
            ReflectionUtils.getMethod(clazz$FriendlyByteBuf, clazz$FriendlyByteBuf, long[].class)
    );

    public static final Class<?> clazz$LevelWriter = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "world.level.IWorldWriter",
                    "world.level.LevelWriter"
            )
    );

    public static final Class<?> clazz$LevelReader = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "world.level.IWorldReader",
                    "world.level.LevelReader"
            )
    );

    public static final Class<?> clazz$DimensionType = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "world.level.dimension.DimensionManager",
                    "world.level.dimension.DimensionType"
            )
    );

    public static final Method method$$LevelReader$dimensionType = requireNonNull(
            ReflectionUtils.getMethod(
                    clazz$LevelReader, clazz$DimensionType
            )
    );

    public static final Field field$DimensionType$minY = requireNonNull(
            ReflectionUtils.getInstanceDeclaredField(
                    clazz$DimensionType, int.class, 0
            )
    );

    public static final Field field$DimensionType$height = requireNonNull(
            ReflectionUtils.getInstanceDeclaredField(
                    clazz$DimensionType, int.class, 1
            )
    );

    public static final Class<?> clazz$BossEvent$BossBarColor = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "world.BossBattle$BarColor",
                    "world.BossEvent$BossBarColor"
            )
    );

    public static final Method method$BossEvent$BossBarColor$valueOf = requireNonNull(
            ReflectionUtils.getMethod(
                    clazz$BossEvent$BossBarColor,
                    new String[]{"valueOf"},
                    String.class
            )
    );

    public static final Class<?> clazz$BossEvent$BossBarOverlay = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "world.BossBattle$BarStyle",
                    "world.BossEvent$BossBarOverlay"
            )
    );

    public static final Method method$BossEvent$BossBarOverlay$valueOf = requireNonNull(
            ReflectionUtils.getMethod(clazz$BossEvent$BossBarOverlay, new String[]{"valueOf"}, String.class)
    );

    public static final Class<?> clazz$EntityType = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "world.entity.EntityTypes",
                    "world.entity.EntityType"
            )
    );

    public static final Class<?> clazz$EntityType$EntityFactory = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "world.entity.EntityTypes$b",
                    "world.entity.EntityType$EntityFactory"
            )
    );

    public static final Field field$EntityType$factory = requireNonNull(
            ReflectionUtils.getDeclaredField(clazz$EntityType, clazz$EntityType$EntityFactory, 0)
    );

    public static final Class<?> clazz$VoxelShape = requireNonNull(
            ReflectionUtils.getClazz(BukkitReflectionUtils.assembleMCClass("world.phys.shapes.VoxelShape"))
    );

    public static final Class<?> clazz$Vec3 = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "world.phys.Vec3D",
                    "world.phys.Vec3"
            )
    );

    public static final Constructor<?> constructor$Vec3 = requireNonNull(
            ReflectionUtils.getConstructor(clazz$Vec3, double.class, double.class, double.class)
    );

    public static final Field field$Vec3$Zero = requireNonNull(
            ReflectionUtils.getDeclaredField(clazz$Vec3, clazz$Vec3, 0)
    );

    public static final Object instance$Vec3$Zero;

    static {
        try {
            instance$Vec3$Zero = field$Vec3$Zero.get(null);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Failed to get instance$Vec3$Zero", e);
        }
    }

    public static final Class<?> clazz$AttributeInstance = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "world.entity.ai.attributes.AttributeModifiable",
                    "world.entity.ai.attributes.AttributeInstance"
            )
    );

    public static final Method method$AttributeInstance$getValue = requireNonNull(
            ReflectionUtils.getMethod(clazz$AttributeInstance, double.class, new String[]{"getValue", "f"})
    );


    public static final Class<?> clazz$AttributeModifier = requireNonNull(
            ReflectionUtils.getClazz(
                    BukkitReflectionUtils.assembleMCClass("world.entity.ai.attributes.AttributeModifier")
            )
    );

    public static final Class<?> clazz$AttributeModifier$Operation = requireNonNull(
            ReflectionUtils.getClazz(BukkitReflectionUtils.assembleMCClass("world.entity.ai.attributes.AttributeModifier$Operation"))
    );

    public static final Method method$AttributeModifier$Operation$values = requireNonNull(
            ReflectionUtils.getStaticMethod(clazz$AttributeModifier$Operation, clazz$AttributeModifier$Operation.arrayType())
    );

    public static final Object instance$AttributeModifier$Operation$ADD_VALUE;

    static {
        try {
            Object[] values = (Object[]) method$AttributeModifier$Operation$values.invoke(null);
            instance$AttributeModifier$Operation$ADD_VALUE = values[0];
        } catch (ReflectiveOperationException e) {
            throw new AssertionError(e);
        }
    }

    public static final Constructor<?> constructor$AttributeModifier = requireNonNull(
            !VersionHelper.isOrAbove1_20_5() ? ReflectionUtils.getConstructor(clazz$AttributeModifier, String.class, double.class, clazz$AttributeModifier$Operation) :
                    (!VersionHelper.isOrAbove1_21() ? ReflectionUtils.getConstructor(clazz$AttributeModifier, UUID.class, String.class, double.class, clazz$AttributeModifier$Operation) :
                            (ReflectionUtils.getConstructor(clazz$AttributeModifier, clazz$ResourceLocation, double.class, clazz$AttributeModifier$Operation)))
    );

    public static final Class<?> clazz$Attribute = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "world.entity.ai.attributes.AttributeBase",
                    "world.entity.ai.attributes.Attribute"
            )
    );

    public static final Field field$Attribute$id = requireNonNull(
            ReflectionUtils.getDeclaredField(clazz$Attribute, String.class, 0)
    );

    public static final Field field$AttributeModifier$amount = requireNonNull(
            ReflectionUtils.getDeclaredField(clazz$AttributeModifier, double.class, 0)
    );

    public static final Class<?> clazz$GameType = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "world.level.EnumGamemode",
                    "world.level.GameType"
            )
    );

    public static final Method method$GameType$getId = requireNonNull(
            ReflectionUtils.getMethod(clazz$GameType, new String[] { "getId", "a" })
    );

    public static final Class<?> clazz$Biome = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "world.level.biome.BiomeBase",
                    "world.level.biome.Biome"
            )
    );

    public static final Class<?> clazz$Team$Visibility = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "world.scores.ScoreboardTeamBase$EnumTeamPush",
                    "world.scores.Team$Visibility"
            )
    );

    public static final Class<?> clazz$BlockState = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "world.level.block.state.IBlockData",
                    "world.level.block.state.BlockState"
            )
    );

    public static final Class<?> clazz$Block = requireNonNull(
            ReflectionUtils.getClazz(BukkitReflectionUtils.assembleMCClass("world.level.block.Block"))
    );

    public static final Field field$BLOCK_STATE_REGISTRY = requireNonNull(
            ReflectionUtils.getDeclaredField(clazz$Block, CoreReflections.clazz$IdMapper, 0)
    );

    public static final Method method$IdMapper$add = requireNonNull(
            ReflectionUtils.getMethod(CoreReflections.clazz$IdMapper, void.class, Object.class)
    );

    public static final Object instance$Block$BLOCK_STATE_REGISTRY;

    static {
        try {
            instance$Block$BLOCK_STATE_REGISTRY = field$BLOCK_STATE_REGISTRY.get(null);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    public static final Class<?> clazz$LevelAccessor = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "world.level.GeneratorAccess",
                    "world.level.LevelAccessor"
            )
    );

    public static final Class<?> clazz$PalettedContainer = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "world.level.chunk.DataPaletteBlock",
                    "world.level.chunk.PalettedContainer"
            )
    );

    public static final Class<?> clazz$PalettedContainer$Data = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "world.level.chunk.DataPaletteBlock$c",
                    "world.level.chunk.PalettedContainer$Data"
            )
    );

    public static final Class<?> clazz$Palette = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "world.level.chunk.DataPalette",
                    "world.level.chunk.Palette"
            )
    );

    public static final Field field$PalettedContainer$data = requireNonNull(
            ReflectionUtils.getDeclaredField(clazz$PalettedContainer, clazz$PalettedContainer$Data, 0)
    );

    public static final VarHandle varHandle$PalettedContainer$data = requireNonNull(
            ReflectionUtils.findVarHandle(field$PalettedContainer$data)
    );

    public static final Field field$PalettedContainer$Data$storage = requireNonNull(
            ReflectionUtils.getDeclaredField(clazz$PalettedContainer$Data, clazz$BitStorage, 0)
    );

    public static final Field field$PalettedContainer$Data$palette = requireNonNull(
            ReflectionUtils.getDeclaredField(clazz$PalettedContainer$Data, clazz$Palette, 0)
    );

    public static final Method method$Palette$write = requireNonNull(
            ReflectionUtils.getMethod(clazz$Palette, void.class, clazz$FriendlyByteBuf)
    );

    public static final Class<?> clazz$ChunkAccess = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "world.level.chunk.IChunkAccess",
                    "world.level.chunk.ChunkAccess"
            )
    );

    public static final Class<?> clazz$LevelChunk = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "world.level.chunk.Chunk",
                    "world.level.chunk.LevelChunk"
            )
    );

    public static final Class<?> clazz$LevelChunkSection = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "world.level.chunk.ChunkSection",
                    "world.level.chunk.LevelChunkSection"
            )
    );

    public static final Field field$ChunkAccess$sections = requireNonNull(
            ReflectionUtils.getDeclaredField(
                    clazz$ChunkAccess, clazz$LevelChunkSection.arrayType(), 0
            )
    );

    public static final Class<?> clazz$BlockEntity = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "world.level.block.entity.TileEntity",
                    "world.level.block.entity.BlockEntity"
            )
    );

    public static final Class<?> clazz$AbstractFurnaceBlockEntity = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "world.level.block.entity.TileEntityFurnace",
                    "world.level.block.entity.AbstractFurnaceBlockEntity"
            )
    );

    public static final Class<?> clazz$CampfireBlockEntity = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "world.level.block.entity.TileEntityCampfire",
                    "world.level.block.entity.CampfireBlockEntity"
            )
    );

    public static final Method method$LevelChunkSection$setBlockState = requireNonNull(
            ReflectionUtils.getMethod(clazz$LevelChunkSection, clazz$BlockState, int.class, int.class, int.class, clazz$BlockState, boolean.class)
    );

    public static final Method method$LevelChunkSection$getBlockState = requireNonNull(
            ReflectionUtils.getMethod(clazz$LevelChunkSection, clazz$BlockState, int.class, int.class, int.class)
    );

    public static final Class<?> clazz$StatePredicate = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "world.level.block.state.BlockBase$f",
                    "world.level.block.state.BlockBehaviour$StatePredicate"
            )
    );

    public static final Class<?> clazz$BlockBehaviour$Properties = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "world.level.block.state.BlockBase$Info",
                    "world.level.block.state.BlockBehaviour$Properties"
            )
    );

    public static final Class<?> clazz$BlockBehaviour = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "world.level.block.state.BlockBase",
                    "world.level.block.state.BlockBehaviour"
            )
    );

    public static final Method method$BlockBehaviour$Properties$of = requireNonNull(
            ReflectionUtils.getStaticMethod(clazz$BlockBehaviour$Properties, clazz$BlockBehaviour$Properties)
    );

    public static final Field field$BlockBehaviour$Properties$id = ReflectionUtils.getDeclaredField(
            clazz$BlockBehaviour$Properties, clazz$ResourceKey, 0
    );

    public static final Constructor<?> constructor$Block  = requireNonNull(
            ReflectionUtils.getConstructor(clazz$Block, clazz$BlockBehaviour$Properties)
    );

    public static final Class<?> clazz$MobEffect = requireNonNull(
            ReflectionUtils.getClazz(
                    BukkitReflectionUtils.assembleMCClass("world.effect.MobEffectList"),  // 这里paper会自动获取到NM.world.effect.MobEffect
                    BukkitReflectionUtils.assembleMCClass("world.effect.MobEffect") // 如果插件是mojmap就会走这个
            )
    );

    public static final Class<?> clazz$MobEffectInstance = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "world.effect.MobEffect",
                    "world.effect.MobEffectInstance"
            )
    );

    public static final Class<?> clazz$SoundType = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "world.level.block.SoundEffectType",
                    "world.level.block.SoundType"
            )
    );

    public static final Constructor<?> constructor$SoundType = requireNonNull(
            ReflectionUtils.getConstructor(clazz$SoundType, float.class, float.class, clazz$SoundEvent, clazz$SoundEvent, clazz$SoundEvent, clazz$SoundEvent, clazz$SoundEvent)
    );

    public static final Class<?> clazz$ItemLike = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "world.level.IMaterial",
                    "world.level.ItemLike"
            )
    );

    public static final Class<?> clazz$Item = requireNonNull(
            ReflectionUtils.getClazz(BukkitReflectionUtils.assembleMCClass("world.item.Item"))
    );

    public static final Class<?> clazz$FluidState = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "world.level.material.Fluid",
                    "world.level.material.FluidState"
            )
    );

    public static final Class<?> clazz$Fluid = requireNonNull(
            ReflectionUtils.getClazz(
                    BukkitReflectionUtils.assembleMCClass("world.level.material.FluidType"),  // 这里paper会自动获取到NM.world.level.material.Fluid
                    BukkitReflectionUtils.assembleMCClass("world.level.material.Fluid") // 如果插件是mojmap就会走这个
            )
    );

    public static final Class<?> clazz$RecipeType = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "world.item.crafting.Recipes",
                    "world.item.crafting.RecipeType"
            )
    );

    public static final Class<?> clazz$WorldGenLevel = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "world.level.GeneratorAccessSeed",
                    "world.level.WorldGenLevel"
            )
    );

    public static final Class<?> clazz$ChunkGenerator = requireNonNull(
            ReflectionUtils.getClazz(
                    BukkitReflectionUtils.assembleMCClass("world.level.chunk.ChunkGenerator")
            )
    );

    // 1.20.1-1.20.2
    public static final Class<?> clazz$AbstractTreeGrower =
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "world.level.block.grower.WorldGenTreeProvider",
                    "world.level.block.grower.AbstractTreeGrower"
            );

    public static final Class<?> clazz$ConfiguredFeature = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "world.level.levelgen.feature.WorldGenFeatureConfigured",
                    "world.level.levelgen.feature.ConfiguredFeature"
            )
    );

    public static final Class<?> clazz$PlacedFeature = requireNonNull(
            ReflectionUtils.getClazz(BukkitReflectionUtils.assembleMCClass("world.level.levelgen.placement.PlacedFeature"))
    );

    // 1.21+
    public static final Class<?> clazz$JukeboxSong = ReflectionUtils.getClazz(
            BukkitReflectionUtils.assembleMCClass("world.item.JukeboxSong")
    );

    public static final Class<?> clazz$StateDefinition = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "world.level.block.state.BlockStateList",
                    "world.level.block.state.StateDefinition"
            )
    );

    public static final Field field$Block$StateDefinition = requireNonNull(
            ReflectionUtils.getDeclaredField(clazz$Block, clazz$StateDefinition, 0)
    );

    public static final Field field$StateDefinition$states = requireNonNull(
            ReflectionUtils.getDeclaredField(clazz$StateDefinition, ImmutableList.class, 0)
    );

    public static final Class<?> clazz$MapColor = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "world.level.material.MaterialMapColor",
                    "world.level.material.MapColor"
            )
    );

    public static final Method method$MapColor$byId = requireNonNull(
            ReflectionUtils.getStaticMethod(clazz$MapColor, clazz$MapColor, int.class)
    );

    public static final Class<?> clazz$PushReaction = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "world.level.material.EnumPistonReaction",
                    "world.level.material.PushReaction"
            )
    );

    public static final Method method$PushReaction$values = requireNonNull(
            ReflectionUtils.getMethod(clazz$PushReaction, new String[] { "values" })
    );

    public static final Class<?> clazz$NoteBlockInstrument = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "world.level.block.state.properties.BlockPropertyInstrument",
                    "world.level.block.state.properties.NoteBlockInstrument"
            )
    );

    public static final Method method$NoteBlockInstrument$values = requireNonNull(
            ReflectionUtils.getMethod(clazz$NoteBlockInstrument, new String[] { "values" })
    );

    public static final Class<?> clazz$BlockStateBase = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "world.level.block.state.BlockBase$BlockData",
                    "world.level.block.state.BlockBehaviour$BlockStateBase"
            )
    );

    public static final Class<?> clazz$BlockStateBase$Cache = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "world.level.block.state.BlockBase$BlockData$Cache",
                    "world.level.block.state.BlockBehaviour$BlockStateBase$Cache"
            )
    );

    public static final Field field$BlockStateBase$cache = requireNonNull(
            ReflectionUtils.getDeclaredField(clazz$BlockStateBase, clazz$BlockStateBase$Cache, 0)
    );

    // 1.20-1.21.1
    public static final Field field$BlockStateBase$Cache$lightBlock =
            ReflectionUtils.getInstanceDeclaredField(clazz$BlockStateBase$Cache, int.class, 0);

    public static final Method method$BlockStateBase$initCache = requireNonNull(
            ReflectionUtils.getMethod(clazz$BlockStateBase, void.class, new String[] { "initCache", "a" })
    );

    public static final Field field$BlockStateBase$isRedstoneConductor = requireNonNull(
            ReflectionUtils.getDeclaredField(clazz$BlockStateBase, clazz$StatePredicate, 0)
    );

    public static final Field field$BlockStateBase$isSuffocating = requireNonNull(
            ReflectionUtils.getDeclaredField(clazz$BlockStateBase, clazz$StatePredicate, 1)
    );

    public static final Field field$BlockStateBase$isViewBlocking = requireNonNull(
            ReflectionUtils.getDeclaredField(clazz$BlockStateBase, clazz$StatePredicate, 2)
    );

    public static final Field field$BlockStateBase$fluidState = requireNonNull(
            ReflectionUtils.getDeclaredField(clazz$BlockStateBase, clazz$FluidState, 0)
    );

    public static final Field field$BlockStateBase$pushReaction = requireNonNull(
            ReflectionUtils.getDeclaredField(clazz$BlockStateBase, clazz$PushReaction, 0)
    );

    public static final Field field$BlockStateBase$mapColor = requireNonNull(
            ReflectionUtils.getDeclaredField(clazz$BlockStateBase, clazz$MapColor, 0)
    );


    public static final Field field$BlockStateBase$instrument = requireNonNull(
            ReflectionUtils.getDeclaredField(clazz$BlockStateBase, clazz$NoteBlockInstrument, 0)
    );

    public static final Field field$BlockStateBase$hardness = requireNonNull(
            ReflectionUtils.getDeclaredField(clazz$BlockStateBase, float.class, 0)
    );

    public static final Field field$BlockStateBase$burnable = requireNonNull(
            ReflectionUtils.getDeclaredField(clazz$BlockStateBase, boolean.class, 2)
    );

    public static final Field field$BlockStateBase$isRandomlyTicking = requireNonNull(
            ReflectionUtils.getDeclaredField(clazz$BlockStateBase, boolean.class, 9)
    );

    public static final Field field$BlockStateBase$propagatesSkylightDown = requireNonNull(
            ReflectionUtils.getDeclaredField(clazz$BlockStateBase, boolean.class, 11)
    );

    public static final Field field$BlockStateBase$requiresCorrectToolForDrops = requireNonNull(
            ReflectionUtils.getDeclaredField(clazz$BlockStateBase, boolean.class, 5)
    );

    public static final Field field$BlockStateBase$canOcclude = requireNonNull(
            ReflectionUtils.getDeclaredField(clazz$BlockStateBase, boolean.class, 6)
    );

    public static final Field field$BlockStateBase$replaceable = requireNonNull(
            ReflectionUtils.getDeclaredField(clazz$BlockStateBase, boolean.class, 8)
    );


    public static final Field field$BlockStateBase$lightEmission = requireNonNull(
            ReflectionUtils.getDeclaredField(clazz$BlockStateBase, int.class, 0)
    );

    // 1.21.2+
    public static final Field field$BlockStateBase$lightBlock =
            ReflectionUtils.getInstanceDeclaredField(clazz$BlockStateBase, int.class, 1);

    public static final Class<?> clazz$AABB = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "world.phys.AxisAlignedBB",
                    "world.phys.AABB"
            )
    );

    public static final Field field$AABB$minX = requireNonNull(
            ReflectionUtils.getInstanceDeclaredField(clazz$AABB, double.class, 0)
    );

    public static final Field field$AABB$minY = requireNonNull(
            ReflectionUtils.getInstanceDeclaredField(clazz$AABB, double.class, 1)
    );

    public static final Field field$AABB$minZ = requireNonNull(
            ReflectionUtils.getInstanceDeclaredField(clazz$AABB, double.class, 2)
    );

    public static final Field field$AABB$maxX = requireNonNull(
            ReflectionUtils.getInstanceDeclaredField(clazz$AABB, double.class, 3)
    );

    public static final Field field$AABB$maxY = requireNonNull(
            ReflectionUtils.getInstanceDeclaredField(clazz$AABB, double.class, 4)
    );

    public static final Field field$AABB$maxZ = requireNonNull(
            ReflectionUtils.getInstanceDeclaredField(clazz$AABB, double.class, 5)
    );

    public static final Method method$Block$box = requireNonNull(
            ReflectionUtils.getStaticMethod(clazz$Block, clazz$VoxelShape, double.class, double.class, double.class, double.class, double.class, double.class)
    );

    public static final Constructor<?> constructor$AABB = requireNonNull(
            ReflectionUtils.getConstructor(clazz$AABB, double.class, double.class, double.class, double.class, double.class, double.class)
    );

    public static final Class<?> clazz$BlockGetter = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "world.level.IBlockAccess",
                    "world.level.BlockGetter"
            )
    );

    public static final Class<?> clazz$StateHolder = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "world.level.block.state.IBlockDataHolder",
                    "world.level.block.state.StateHolder"
            )
    );

    public static final Class<?> clazz$CollisionContext = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "world.phys.shapes.VoxelShapeCollision",
                    "world.phys.shapes.CollisionContext"
            )
    );

    public static final Method method$BlockBehaviour$getShape = requireNonNull(
            ReflectionUtils.getDeclaredMethod(clazz$BlockBehaviour, clazz$VoxelShape, new String[]{"getShape", "a"}, clazz$BlockState, clazz$BlockGetter, CoreReflections.clazz$BlockPos, clazz$CollisionContext)
    );

    public static final Method method$BlockBehaviour$getCollisionShape = requireNonNull(
            ReflectionUtils.getDeclaredMethod(clazz$BlockBehaviour, clazz$VoxelShape, new String[]{"getCollisionShape", VersionHelper.isOrAbove1_20_3() ? "b" : "c"}, clazz$BlockState, clazz$BlockGetter, CoreReflections.clazz$BlockPos, clazz$CollisionContext)
    );

    public static final Method method$BlockBehaviour$getBlockSupportShape = requireNonNull(
            ReflectionUtils.getDeclaredMethod(clazz$BlockBehaviour, clazz$VoxelShape, new String[]{"getBlockSupportShape", "b_"}, clazz$BlockState, clazz$BlockGetter, CoreReflections.clazz$BlockPos)
    );

    public static final Method method$LevelAccessor$scheduleTick = requireNonNull(
            ReflectionUtils.getMethod(clazz$LevelAccessor, void.class, CoreReflections.clazz$BlockPos, clazz$Block, int.class)
    );

    public static final Field field$BlockBehaviour$properties = requireNonNull(
            ReflectionUtils.getInstanceDeclaredField(clazz$BlockBehaviour, clazz$BlockBehaviour$Properties, 0)
    );

    public static final Field field$BlockBehaviour$explosionResistance = requireNonNull(
            ReflectionUtils.getInstanceDeclaredField(clazz$BlockBehaviour, float.class, 0)
    );

    public static final Field field$BlockBehaviour$soundType = requireNonNull(
            ReflectionUtils.getInstanceDeclaredField(clazz$BlockBehaviour, clazz$SoundType, 0)
    );

    public static final Field field$SoundType$breakSound = requireNonNull(
            ReflectionUtils.getDeclaredField(clazz$SoundType, clazz$SoundEvent, 0)
    );

    public static final Field field$SoundType$stepSound = requireNonNull(
            ReflectionUtils.getDeclaredField(clazz$SoundType, clazz$SoundEvent, 1)
    );

    public static final Field field$SoundType$placeSound = requireNonNull(
            ReflectionUtils.getDeclaredField(clazz$SoundType, clazz$SoundEvent, 2)
    );

    public static final Field field$SoundType$hitSound = requireNonNull(
            ReflectionUtils.getDeclaredField(clazz$SoundType, clazz$SoundEvent, 3)
    );

    public static final Field field$SoundType$fallSound = requireNonNull(
            ReflectionUtils.getDeclaredField(clazz$SoundType, clazz$SoundEvent, 4)
    );

    public static final Field field$BlockBehaviour$Properties$hasCollision = requireNonNull(
            ReflectionUtils.getInstanceDeclaredField(clazz$BlockBehaviour$Properties, boolean.class, 0)
    );

    public static final Class<?> clazz$ChunkPos = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "world.level.ChunkCoordIntPair",
                    "world.level.ChunkPos"
            )
    );

    public static final Constructor<?> constructor$ChunkPos = requireNonNull(
            ReflectionUtils.getConstructor(clazz$ChunkPos, int.class, int.class)
    );

    public static final Class<?> clazz$LevelLightEngine = requireNonNull(
            ReflectionUtils.getClazz(BukkitReflectionUtils.assembleMCClass("world.level.lighting.LevelLightEngine"))
    );

    public static final Class<?> clazz$LightLayer = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "world.level.EnumSkyBlock",
                    "world.level.LightLayer"
            )
    );

    public static final Method method$LightLayer$values = requireNonNull(
            ReflectionUtils.getStaticMethod(clazz$LightLayer, clazz$LightLayer.arrayType())
    );

    public static final Object instance$LightLayer$BLOCK;

    static {
        try {
            instance$LightLayer$BLOCK = ((Object[]) method$LightLayer$values.invoke(null))[1];
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    public static final Class<?> clazz$Player = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "world.entity.player.EntityHuman",
                    "world.entity.player.Player"
            )
    );

    public static final Class<?> clazz$Entity = requireNonNull(
            ReflectionUtils.getClazz(BukkitReflectionUtils.assembleMCClass("world.entity.Entity"))
    );

    public static final Field field$Entity$ENTITY_COUNTER = requireNonNull(
            ReflectionUtils.getDeclaredField(clazz$Entity, AtomicInteger.class, 0)
    );

    public static final AtomicInteger instance$Entity$ENTITY_COUNTER;

    static {
        try {
            instance$Entity$ENTITY_COUNTER = (AtomicInteger) requireNonNull(field$Entity$ENTITY_COUNTER.get(null));
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static final Class<?> clazz$Level = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "world.level.World",
                    "world.level.Level"
            )
    );

    public static final Method method$Entity$level = requireNonNull(
            ReflectionUtils.getMethod(clazz$Entity, clazz$Level)
    );

    public static final Class<?> clazz$InteractionHand = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "world.EnumHand",
                    "world.InteractionHand"
            )
    );

    public static final Method method$InteractionHand$values = requireNonNull(
            ReflectionUtils.getStaticMethod(clazz$InteractionHand, clazz$InteractionHand.arrayType())
    );

    public static final Object instance$InteractionHand$MAIN_HAND;
    public static final Object instance$InteractionHand$OFF_HAND;

    static {
        try {
            Object[] values = (Object[]) method$InteractionHand$values.invoke(null);
            instance$InteractionHand$MAIN_HAND = values[0];
            instance$InteractionHand$OFF_HAND = values[1];
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    public static final Class<?> clazz$EquipmentSlot = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "world.entity.EnumItemSlot",
                    "world.entity.EquipmentSlot"
            )
    );

    public static final Method method$EquipmentSlot$values = requireNonNull(
            ReflectionUtils.getStaticMethod(
                    clazz$EquipmentSlot, clazz$EquipmentSlot.arrayType()
            )
    );

    public static final Object[] instance$EquipmentSlot$values;
    public static final Object instance$EquipmentSlot$MAINHAND;
    public static final Object instance$EquipmentSlot$OFFHAND;
    public static final Object instance$EquipmentSlot$FEET;
    public static final Object instance$EquipmentSlot$LEGS;
    public static final Object instance$EquipmentSlot$CHEST;
    public static final Object instance$EquipmentSlot$HEAD;
//    public static final Object instance$EquipmentSlot$BODY;

    static {
        try {
            instance$EquipmentSlot$values = (Object[]) method$EquipmentSlot$values.invoke(null);
            instance$EquipmentSlot$MAINHAND = instance$EquipmentSlot$values[0];
            instance$EquipmentSlot$OFFHAND = instance$EquipmentSlot$values[1];
            instance$EquipmentSlot$FEET = instance$EquipmentSlot$values[2];
            instance$EquipmentSlot$LEGS = instance$EquipmentSlot$values[3];
            instance$EquipmentSlot$CHEST = instance$EquipmentSlot$values[4];
            instance$EquipmentSlot$HEAD = instance$EquipmentSlot$values[5];
//            instance$EquipmentSlot$BODY = instance$EquipmentSlot$values[6];
        } catch (ReflectiveOperationException e) {
            throw new ReflectionInitException("Failed to init EquipmentSlot", e);
        }
    }

    public static final Method method$Block$defaultBlockState = requireNonNull(
            ReflectionUtils.getMethod(clazz$Block, clazz$BlockState)
    );

    public static final Method method$Entity$getOnPos = requireNonNull(
            ReflectionUtils.getDeclaredMethod(clazz$Entity, CoreReflections.clazz$BlockPos, float.class)
    );

    public static final Class<?> clazz$ItemStack = requireNonNull(
            ReflectionUtils.getClazz(BukkitReflectionUtils.assembleMCClass("world.item.ItemStack"))
    );

    public static final Object instance$ItemStack$EMPTY;

    static {
        try {
            instance$ItemStack$EMPTY = requireNonNull(ReflectionUtils.getDeclaredField(clazz$ItemStack, clazz$ItemStack, 0)).get(null);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    public static final Class<?> clazz$ItemEnchantments =
            ReflectionUtils.getClazz(BukkitReflectionUtils.assembleMCClass( "world.item.enchantment.ItemEnchantments"));

    public static final Field field$ItemEnchantments$enchantments = Optional.ofNullable(clazz$ItemEnchantments)
            .map(it -> ReflectionUtils.getInstanceDeclaredField(it, 0))
            .orElse(null);

    // 1.21.3+
    public static final Class<?> clazz$ScheduledTickAccess =
            ReflectionUtils.getClazz(BukkitReflectionUtils.assembleMCClass("world.level.ScheduledTickAccess"));

    public static final Method method$BlockBehaviour$updateShape = requireNonNull(
            VersionHelper.isOrAbove1_21_2() ?
                    ReflectionUtils.getDeclaredMethod(clazz$BlockBehaviour, clazz$BlockState, clazz$BlockState, clazz$LevelReader, clazz$ScheduledTickAccess, CoreReflections.clazz$BlockPos, CoreReflections.clazz$Direction, CoreReflections.clazz$BlockPos, clazz$BlockState, clazz$RandomSource) :
                    ReflectionUtils.getDeclaredMethod(clazz$BlockBehaviour, clazz$BlockState, clazz$BlockState, CoreReflections.clazz$Direction, clazz$BlockState, clazz$LevelAccessor, CoreReflections.clazz$BlockPos, CoreReflections.clazz$BlockPos)
    );

    public static final Class<?> clazz$Fallable = requireNonNull(
            ReflectionUtils.getClazz(BukkitReflectionUtils.assembleMCClass("world.level.block.Fallable"))
    );

    public static final Class<?> clazz$FallingBlock = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "world.level.block.BlockFalling",
                    "world.level.block.FallingBlock"
            )
    );

    public static final Method method$FallingBlock$isFree = requireNonNull(
            ReflectionUtils.getStaticMethod(clazz$FallingBlock, boolean.class, clazz$BlockState)
    );

    public static final Class<?> clazz$FallingBlockEntity = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "world.entity.item.EntityFallingBlock",
                    "world.entity.item.FallingBlockEntity"
            )
    );

    public static final Method method$FallingBlockEntity$fall = requireNonNull(
            ReflectionUtils.getStaticMethod(clazz$FallingBlockEntity, clazz$FallingBlockEntity, clazz$Level, CoreReflections.clazz$BlockPos, clazz$BlockState)
    );

    public static final Method method$FallingBlockEntity$setHurtsEntities = requireNonNull(
            ReflectionUtils.getMethod(clazz$FallingBlockEntity, void.class, float.class, int.class)
    );

    public static final Field field$FallingBlockEntity$blockState = requireNonNull(
            ReflectionUtils.getDeclaredField(clazz$FallingBlockEntity, clazz$BlockState, VersionHelper.isOrAbove1_21_5() ? 1 : 0)
    );

    public static final Field field$FallingBlockEntity$cancelDrop = requireNonNull(
            ReflectionUtils.getDeclaredField(clazz$FallingBlockEntity, boolean.class, 1)
    );

    public static final Field field$Entity$xo = requireNonNull(
            ReflectionUtils.getInstanceDeclaredField(clazz$Entity, double.class, 0)
    );

    public static final Field field$Entity$yo = requireNonNull(
            ReflectionUtils.getInstanceDeclaredField(clazz$Entity, double.class, 1)
    );

    public static final Field field$Entity$zo = requireNonNull(
            ReflectionUtils.getInstanceDeclaredField(clazz$Entity, double.class, 2)
    );

    public static final Method method$BlockStateBase$hasTag = requireNonNull(
            ReflectionUtils.getMethod(clazz$BlockStateBase, boolean.class, CoreReflections.clazz$TagKey)
    );

    public static final Method method$Level$removeBlock = requireNonNull(
            ReflectionUtils.getMethod(clazz$Level, boolean.class, CoreReflections.clazz$BlockPos, boolean.class)
    );

    public static final Class<?> clazz$LeavesBlock = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "world.level.block.BlockLeaves",
                    "world.level.block.LeavesBlock"
            )
    );

    public static final Class<?> clazz$IntegerProperty = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "world.level.block.state.properties.BlockStateInteger",
                    "world.level.block.state.properties.IntegerProperty"
            )
    );

    public static final Class<?> clazz$Property = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "world.level.block.state.properties.IBlockState",
                    "world.level.block.state.properties.Property"
            )
    );

    public static final Field field$LeavesBlock$DISTANCE = requireNonNull(
            ReflectionUtils.getDeclaredField(clazz$LeavesBlock, clazz$IntegerProperty, 0)
    );

    public static final Method method$StateHolder$hasProperty = requireNonNull(
            ReflectionUtils.getMethod(clazz$StateHolder, boolean.class, clazz$Property)
    );

    public static final Method method$StateHolder$getValue = requireNonNull(
            ReflectionUtils.getMethod(clazz$StateHolder, Object.class, new String[] {"getValue", "c"}, clazz$Property)
    );


    public static final Method method$Block$updateFromNeighbourShapes = requireNonNull(
            ReflectionUtils.getStaticMethod(clazz$Block, clazz$BlockState, clazz$BlockState, clazz$LevelAccessor, CoreReflections.clazz$BlockPos)
    );

    public static final Method method$BlockStateBase$updateNeighbourShapes = requireNonNull(
            ReflectionUtils.getMethod(
                    // flags   // depth
                    clazz$BlockStateBase, void.class, clazz$LevelAccessor, CoreReflections.clazz$BlockPos, int.class, int.class
            )
    );

    public static final Method method$BlockState$getShape = requireNonNull(
            ReflectionUtils.getMethod(clazz$BlockStateBase, clazz$VoxelShape, new String[]{"getShape", "a"}, clazz$BlockGetter, CoreReflections.clazz$BlockPos, clazz$CollisionContext)
    );

    public static final Method method$VoxelShape$isEmpty = requireNonNull(
            ReflectionUtils.getMethod(clazz$VoxelShape, boolean.class)
    );

    public static final Method method$VoxelShape$bounds = requireNonNull(
            ReflectionUtils.getMethod(clazz$VoxelShape, clazz$AABB)
    );

    public static final Method method$LevelWriter$setBlock = requireNonNull(
            ReflectionUtils.getMethod(clazz$LevelWriter, boolean.class, CoreReflections.clazz$BlockPos, clazz$BlockState, int.class)
    );

    public static final Method method$CollisionContext$of = requireNonNull(
            ReflectionUtils.getStaticMethod(clazz$CollisionContext, clazz$CollisionContext, clazz$Entity)
    );

    public static final Method method$CollisionContext$empty = requireNonNull(
            ReflectionUtils.getStaticMethod(clazz$CollisionContext, clazz$CollisionContext)
    );

    public static final Method method$BlockStateBase$canSurvive = requireNonNull(
            ReflectionUtils.getMethod(clazz$BlockStateBase, boolean.class, clazz$LevelReader, CoreReflections.clazz$BlockPos)
    );

    public static final Method method$BlockStateBase$onPlace = requireNonNull(
            ReflectionUtils.getMethod(clazz$BlockStateBase, void.class, clazz$Level, CoreReflections.clazz$BlockPos, clazz$BlockState, boolean.class)
    );

    public static final Method method$ItemStack$isTag = requireNonNull(
            ReflectionUtils.getMethod(clazz$ItemStack, boolean.class, CoreReflections.clazz$TagKey)
    );

    public static final Class<?> clazz$FireBlock = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "world.level.block.BlockFire",
                    "world.level.block.FireBlock"
            )
    );

    public static final Method method$FireBlock$setFlammable = requireNonNull(
            Optional.ofNullable(ReflectionUtils.getMethod(clazz$FireBlock, void.class, clazz$Block, int.class, int.class))
                    .orElse(ReflectionUtils.getDeclaredMethod(clazz$FireBlock, void.class, clazz$Block, int.class, int.class))
    );

    public static final Field field$LevelChunkSection$states = requireNonNull(
            ReflectionUtils.getDeclaredField(clazz$LevelChunkSection, clazz$PalettedContainer, 0)
    );

    public static final Constructor<?> constructor$ItemStack = requireNonNull(
            ReflectionUtils.getConstructor(clazz$ItemStack, clazz$ItemLike)
    );

    public static final Class<?> clazz$Display$ItemDisplay = requireNonNull(
            ReflectionUtils.getClazz(BukkitReflectionUtils.assembleMCClass("world.entity.Display$ItemDisplay"))
    );

    public static final Class<?> clazz$Abilities = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "world.entity.player.PlayerAbilities",
                    "world.entity.player.Abilities"
            )
    );

    public static final Field field$Abilities$invulnerable = requireNonNull(
            ReflectionUtils.getInstanceDeclaredField(clazz$Abilities, boolean.class, 0)
    );

    public static final Field field$Abilities$flying = requireNonNull(
            ReflectionUtils.getInstanceDeclaredField(clazz$Abilities, boolean.class, 1)
    );

    public static final Field field$Abilities$mayfly = requireNonNull(
            ReflectionUtils.getInstanceDeclaredField(clazz$Abilities, boolean.class, 2)
    );

    public static final Field field$Abilities$instabuild = requireNonNull(
            ReflectionUtils.getInstanceDeclaredField(clazz$Abilities, boolean.class, 3)
    );

    public static final Field field$Abilities$mayBuild = requireNonNull(
            ReflectionUtils.getInstanceDeclaredField(clazz$Abilities, boolean.class, 4)
    );

    public static final Field field$Player$abilities = requireNonNull(
            ReflectionUtils.getInstanceDeclaredField(clazz$Player, clazz$Abilities, 0)
    );

    public static final Class<?> clazz$FlowingFluid = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "world.level.material.FluidTypeFlowing",
                    "world.level.material.FlowingFluid"
            )
    );

    public static final Method method$FlowingFluid$getSource = requireNonNull(
            ReflectionUtils.getMethod(clazz$FlowingFluid, clazz$FluidState, boolean.class)
    );

    public static final Method method$Level$getFluidState = requireNonNull(
            ReflectionUtils.getMethod(clazz$Level, clazz$FluidState, CoreReflections.clazz$BlockPos)
    );

    public static final Method method$FluidState$isSource = requireNonNull(
            ReflectionUtils.getMethod(clazz$FluidState, boolean.class, new String[]{"isSource", "b"})
    );

    public static final Method method$FluidState$createLegacyBlock = requireNonNull(
            ReflectionUtils.getMethod(clazz$FluidState, clazz$BlockState)
    );

    public static final Class<?> clazz$RecipeManager = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "world.item.crafting.CraftingManager",
                    "world.item.crafting.RecipeManager"
            )
    );

    public static final Class<?> clazz$RecipeManager$CachedCheck = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "world.item.crafting.CraftingManager$a",
                    "world.item.crafting.RecipeManager$CachedCheck"
            )
    );

    public static final Method method$RecipeManager$finalizeRecipeLoading =
            ReflectionUtils.getMethod(clazz$RecipeManager, new String[]{"finalizeRecipeLoading"});

    public static final Class<?> clazz$RecipeMap =
            ReflectionUtils.getClazz(BukkitReflectionUtils.assembleMCClass("world.item.crafting.RecipeMap"));

    public static final Field field$RecipeManager$recipes = Optional.ofNullable(clazz$RecipeMap)
            .map(it -> ReflectionUtils.getDeclaredField(clazz$RecipeManager, it, 0))
            .orElse(null);

    public static final Method method$RecipeMap$removeRecipe = Optional.ofNullable(clazz$RecipeMap)
            .map(it -> ReflectionUtils.getMethod(it, boolean.class, clazz$ResourceKey))
            .orElse(null);

    public static final Class<?> clazz$FeatureFlagSet = requireNonNull(
            ReflectionUtils.getClazz(BukkitReflectionUtils.assembleMCClass("world.flag.FeatureFlagSet"))
    );

    public static final Field field$RecipeManager$featureflagset =
            ReflectionUtils.getDeclaredField(clazz$RecipeManager, clazz$FeatureFlagSet, 0);

    public static final Class<?> clazz$Inventory = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "world.entity.player.PlayerInventory",
                    "world.entity.player.Inventory"
            )
    );

    public static final Field field$Inventory$items = requireNonNull(
            ReflectionUtils.getInstanceDeclaredField(clazz$Inventory, CoreReflections.clazz$NonNullList, 0)
    );

    public static final Class<?> clazz$Ingredient = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "world.item.crafting.RecipeItemStack",
                    "world.item.crafting.Ingredient"
            )
    );

    // 1.20.1-1.21.1
    public static final Field field$Ingredient$itemStacks1_20_1 =
            ReflectionUtils.getDeclaredField(clazz$Ingredient, clazz$ItemStack.arrayType(), 0);

    // 1.21.2-1.21.3
    public static final Field field$Ingredient$itemStacks1_21_2 =
            ReflectionUtils.getDeclaredField(clazz$Ingredient, List.class, 1);

    // 1.21.4 paper
    public static final Field field$Ingredient$itemStacks1_21_4 =
            ReflectionUtils.getDeclaredField(clazz$Ingredient, Set.class, 0);

    // Since 1.21.2, exact has been removed
    public static final Field field$Ingredient$exact =
            ReflectionUtils.getDeclaredField(clazz$Ingredient, boolean.class, 0);

    public static final Class<?> clazz$ShapedRecipe = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "world.item.crafting.ShapedRecipes",
                    "world.item.crafting.ShapedRecipe"
            )
    );

    // 1.20.3+
    public static final Class<?> clazz$ShapedRecipePattern =
            ReflectionUtils.getClazz(BukkitReflectionUtils.assembleMCClass("world.item.crafting.ShapedRecipePattern"));

    // 1.20.1-1.20.2
    public static final Field field$1_20_1$ShapedRecipe$recipeItems=
            ReflectionUtils.getDeclaredField(clazz$ShapedRecipe, CoreReflections.clazz$NonNullList, 0);

    // 1.20.3+
    public static final Field field$1_20_3$ShapedRecipe$pattern=
            ReflectionUtils.getDeclaredField(clazz$ShapedRecipe, clazz$ShapedRecipePattern, 0);

    // 1.20.3-1.21.1
    public static final Field field$ShapedRecipePattern$ingredients1_20_3 = Optional.ofNullable(clazz$ShapedRecipePattern)
            .map(it -> ReflectionUtils.getDeclaredField(it, CoreReflections.clazz$NonNullList, 0))
            .orElse(null);

    // 1.21.2+
    public static final Field field$ShapedRecipePattern$ingredients1_21_2 = Optional.ofNullable(clazz$ShapedRecipePattern)
            .map(it -> ReflectionUtils.getDeclaredField(it, List.class, 0))
            .orElse(null);

    // 1.20.1-1.21.1
    public static final Field field$Ingredient$values =
            ReflectionUtils.getInstanceDeclaredField(clazz$Ingredient, 0);

    // 1.20.2+
    public static final Class<?> clazz$RecipeHolder = ReflectionUtils.getClazz(
            BukkitReflectionUtils.assembleMCClass("world.item.crafting.RecipeHolder")
    );

    // 1.20.2-1.21.1 resource location
    // 1.21.2+ resource key
    public static final Constructor<?> constructor$RecipeHolder = Optional.ofNullable(clazz$RecipeHolder)
            .map(it -> ReflectionUtils.getConstructor(it, 0))
            .orElse(null);

    // 1.20.2+
    public static final Field field$RecipeHolder$recipe = Optional.ofNullable(clazz$RecipeHolder)
            .map(it -> ReflectionUtils.getDeclaredField(it, 1))
            .orElse(null);

    public static final Field field$RecipeHolder$id = Optional.ofNullable(clazz$RecipeHolder)
            .map(it -> ReflectionUtils.getDeclaredField(it, 0))
            .orElse(null);

    public static final Class<?> clazz$ShapelessRecipe = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "world.item.crafting.ShapelessRecipes",
                    "world.item.crafting.ShapelessRecipe"
            )
    );

    public static final Class<?> clazz$PlacementInfo =
            ReflectionUtils.getClazz(BukkitReflectionUtils.assembleMCClass("world.item.crafting.PlacementInfo"));

    // 1.21.2+
    public static final Field field$ShapelessRecipe$placementInfo = Optional.ofNullable(clazz$PlacementInfo)
            .map(it -> ReflectionUtils.getDeclaredField(clazz$ShapelessRecipe, it, 0))
            .orElse(null);

    public static final Field field$ShapedRecipe$placementInfo = Optional.ofNullable(clazz$PlacementInfo)
            .map(it -> ReflectionUtils.getDeclaredField(clazz$ShapedRecipe, it, 0))
            .orElse(null);

    public static final Field field$ShapelessRecipe$ingredients =
            Optional.ofNullable(ReflectionUtils.getDeclaredField(clazz$ShapelessRecipe, List.class, 0))
                    .orElse(ReflectionUtils.getDeclaredField(clazz$ShapelessRecipe, CoreReflections.clazz$NonNullList, 0));

    // require ResourceLocation for 1.20.1-1.21.1
    // require ResourceKey for 1.21.2+
    public static final Method method$RecipeManager$byKey;

    static {
        Method method$RecipeManager$byKey0 = null;
        if (VersionHelper.isOrAbove1_21_2()) {
            for (Method method : clazz$RecipeManager.getMethods()) {
                if (method.getParameterCount() == 1 && method.getParameterTypes()[0] == clazz$ResourceKey) {
                    if (method.getReturnType() == Optional.class && method.getGenericReturnType() instanceof ParameterizedType type) {
                        Type[] actualTypeArguments = type.getActualTypeArguments();
                        if (actualTypeArguments.length == 1) {
                            method$RecipeManager$byKey0 = method;
                        }
                    }
                }
            }
        } else if (VersionHelper.isOrAbove1_20_2()) {
            for (Method method : clazz$RecipeManager.getMethods()) {
                if (method.getParameterCount() == 1 && method.getParameterTypes()[0] == clazz$ResourceLocation) {
                    if (method.getReturnType() == Optional.class && method.getGenericReturnType() instanceof ParameterizedType type) {
                        Type[] actualTypeArguments = type.getActualTypeArguments();
                        if (actualTypeArguments.length == 1) {
                            method$RecipeManager$byKey0 = method;
                        }
                    }
                }
            }
        } else {
            for (Method method : clazz$RecipeManager.getMethods()) {
                if (method.getParameterCount() == 1 && method.getParameterTypes()[0] == clazz$ResourceLocation) {
                    if (method.getReturnType() == Optional.class) {
                        method$RecipeManager$byKey0 = method;
                    }
                }
            }
        }
        method$RecipeManager$byKey = requireNonNull(method$RecipeManager$byKey0);
    }

    public static final Class<?> clazz$ResultContainer = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "world.inventory.InventoryCraftResult",
                    "world.inventory.ResultContainer"
            )
    );

    public static final Class<?> clazz$Container = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "world.IInventory",
                    "world.Container"
            )
    );

    public static final Class<?> clazz$Recipe = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "world.item.crafting.IRecipe",
                    "world.item.crafting.Recipe"
            )
    );

    public static final Field field$ResultContainer$recipeUsed =
            Optional.ofNullable(ReflectionUtils.getDeclaredField(clazz$ResultContainer, clazz$Recipe, 0))
                    .orElse(ReflectionUtils.getDeclaredField(clazz$ResultContainer, clazz$RecipeHolder, 0));

    public static final Class<?> clazz$LivingEntity = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "world.entity.EntityLiving",
                    "world.entity.LivingEntity"
            )
    );

    // 1.20.5+
    public static final Method method$ItemStack$hurtAndBreak =
            ReflectionUtils.getMethod(clazz$ItemStack, void.class, int.class, clazz$LivingEntity, clazz$EquipmentSlot);

    // for 1.20.1-1.21.1
    public static final Class<?> clazz$AbstractCookingRecipe = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "world.item.crafting.RecipeCooking",
                    "world.item.crafting.AbstractCookingRecipe"
            )
    );

    // for 1.20.1-1.21.1
    public static final Field field$AbstractCookingRecipe$input =
            ReflectionUtils.getDeclaredField(clazz$AbstractCookingRecipe, clazz$Ingredient, 0);

    // for 1.21.2+
    public static final Class<?> clazz$SingleItemRecipe =
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "world.item.crafting.RecipeSingleItem",
                    "world.item.crafting.SingleItemRecipe"
            );

    // for 1.21.2+
    public static final Field field$SingleItemRecipe$input =
            Optional.ofNullable(clazz$SingleItemRecipe)
                    .map(it -> ReflectionUtils.getDeclaredField(it, clazz$Ingredient, 0))
                    .orElse(null);

    public static final Field field$AbstractFurnaceBlockEntity$quickCheck = requireNonNull(
            ReflectionUtils.getDeclaredField(clazz$AbstractFurnaceBlockEntity, clazz$RecipeManager$CachedCheck, 0)
    );

    // 1.20.1-1.21.1
    public static final Field field$CampfireBlockEntity$quickCheck =
            ReflectionUtils.getDeclaredField(clazz$CampfireBlockEntity, clazz$RecipeManager$CachedCheck, 0);

    // 1.21+
    public static final Class<?> clazz$RecipeInput =
            ReflectionUtils.getClazz(BukkitReflectionUtils.assembleMCClass("world.item.crafting.RecipeInput"));

    public static final Class<?> clazz$SingleRecipeInput =
            ReflectionUtils.getClazz(BukkitReflectionUtils.assembleMCClass("world.item.crafting.SingleRecipeInput"));

    public static final Constructor<?> constructor$SingleRecipeInput = Optional.ofNullable(clazz$SingleRecipeInput)
            .map(it -> ReflectionUtils.getConstructor(it, clazz$ItemStack))
            .orElse(null);

    // 1.21+
    public static final Field field$SingleRecipeInput$item = Optional.ofNullable(clazz$SingleRecipeInput)
            .map(it -> ReflectionUtils.getDeclaredField(it, clazz$ItemStack, 0))
            .orElse(null);

    public static final Field field$AbstractFurnaceBlockEntity$items = requireNonNull(
            ReflectionUtils.getDeclaredField(clazz$AbstractFurnaceBlockEntity, CoreReflections.clazz$NonNullList, 0)
    );

    public static final Class<?> clazz$SimpleContainer = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "world.InventorySubcontainer",
                    "world.SimpleContainer"
            )
    );

    public static final Field field$SimpleContainer$items = requireNonNull(
            ReflectionUtils.getDeclaredField(clazz$SimpleContainer, CoreReflections.clazz$NonNullList, 0)
    );

    public static final Method method$LevelReader$getMaxLocalRawBrightness = requireNonNull(
            ReflectionUtils.getMethod(clazz$LevelReader, int.class, CoreReflections.clazz$BlockPos)
    );

    public static final Method method$ConfiguredFeature$place = requireNonNull(
            ReflectionUtils.getMethod(clazz$ConfiguredFeature, boolean.class, clazz$WorldGenLevel, clazz$ChunkGenerator, clazz$RandomSource, CoreReflections.clazz$BlockPos)
    );

    public static final Class<?> clazz$BonemealableBlock = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "world.level.block.IBlockFragilePlantElement",
                    "world.level.block.BonemealableBlock"
            )
    );

    public static final Method method$BonemealableBlock$isValidBonemealTarget = requireNonNull(
            VersionHelper.isOrAbove1_20_2() ?
                    ReflectionUtils.getInstanceMethod(clazz$BonemealableBlock, boolean.class, clazz$LevelReader, CoreReflections.clazz$BlockPos, clazz$BlockState) :
                    ReflectionUtils.getInstanceMethod(clazz$BonemealableBlock, boolean.class, clazz$LevelReader, CoreReflections.clazz$BlockPos, clazz$BlockState, boolean.class)
    );

    public static final Method method$BonemealableBlock$isBonemealSuccess = requireNonNull(
            ReflectionUtils.getMethod(clazz$BonemealableBlock, boolean.class, clazz$Level, clazz$RandomSource, CoreReflections.clazz$BlockPos, clazz$BlockState)
    );
    
    public static final Method method$PalettedContainer$getAndSet = Objects.requireNonNull(
            ReflectionUtils.getMethod(clazz$PalettedContainer, Object.class, new String[] {"a", "getAndSet"}, int.class, int.class, int.class, Object.class)
    );

    public static final Class<?> clazz$MenuType = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "world.inventory.Containers",
                    "world.inventory.MenuType"
            )
    );

    public static final Class<?> clazz$AbstractContainerMenu = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "world.inventory.Container",
                    "world.inventory.AbstractContainerMenu"
            )
    );

    public static final Field field$AbstractContainerMenu$title = requireNonNull(
            ReflectionUtils.getDeclaredField(clazz$AbstractContainerMenu, clazz$Component, 0)
    );

    public static final Field field$Player$containerMenu = requireNonNull(
            ReflectionUtils.getDeclaredField(clazz$Player, clazz$AbstractContainerMenu, 0)
    );

    public static final Field field$AbstractContainerMenu$containerId = requireNonNull(
            ReflectionUtils.getDeclaredField(clazz$AbstractContainerMenu, int.class, 1)
    );

    public static final Field field$AbstractContainerMenu$menuType = requireNonNull(
            ReflectionUtils.getDeclaredField(clazz$AbstractContainerMenu, clazz$MenuType, 0)
    );

    public static final Method method$AbstractContainerMenu$broadcastChanges = requireNonNull(
            ReflectionUtils.getMethod(clazz$AbstractContainerMenu, void.class, new String[]{ "broadcastChanges", "d" })
    );

    public static final Method method$AbstractContainerMenu$broadcastFullState = requireNonNull(
            ReflectionUtils.getMethod(clazz$AbstractContainerMenu, void.class, new String[]{ "broadcastFullState", "e" })
    );

    public static final Field field$AbstractContainerMenu$checkReachable = requireNonNull(
            ReflectionUtils.getDeclaredFieldBackwards(clazz$AbstractContainerMenu, boolean.class, 0)
    );

    public static final Constructor<?> constructor$JukeboxSong = Optional.ofNullable(clazz$JukeboxSong)
            .map(it -> ReflectionUtils.getConstructor(it, CoreReflections.clazz$Holder, clazz$Component, float.class, int.class))
            .orElse(null);

    public static final Field field$JukeboxSong$soundEvent = Optional.ofNullable(clazz$JukeboxSong)
            .map(it -> ReflectionUtils.getDeclaredField(it, CoreReflections.clazz$Holder, 0))
            .orElse(null);

    public static final Field field$JukeboxSong$description = Optional.ofNullable(clazz$JukeboxSong)
            .map(it -> ReflectionUtils.getDeclaredField(it, clazz$Component, 0))
            .orElse(null);

    public static final Field field$JukeboxSong$lengthInSeconds = Optional.ofNullable(clazz$JukeboxSong)
            .map(it -> ReflectionUtils.getDeclaredField(it, float.class, 0))
            .orElse(null);

    public static final Field field$JukeboxSong$comparatorOutput = Optional.ofNullable(clazz$JukeboxSong)
            .map(it -> ReflectionUtils.getDeclaredField(it, int.class, 0))
            .orElse(null);

    public static final Method method$FluidState$getType = requireNonNull(
            ReflectionUtils.getMethod(clazz$FluidState, clazz$Fluid)
    );

    public static final Class<?> clazz$CustomRecipe = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "world.item.crafting.IRecipeComplex",
                    "world.item.crafting.CustomRecipe"
            )
    );

    public static final Class<?> clazz$RepairItemRecipe = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "world.item.crafting.RecipeRepair",
                    "world.item.crafting.RepairItemRecipe"
            )
    );

    public static final Class<?> clazz$ArmorDyeRecipe = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "world.item.crafting.RecipeArmorDye",
                    "world.item.crafting.ArmorDyeRecipe"
            )
    );

    public static final Class<?> clazz$AnvilMenu = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "world.inventory.ContainerAnvil",
                    "world.inventory.AnvilMenu"
            )
    );

    public static final Class<?> clazz$SmithingTransformRecipe = requireNonNull(
            ReflectionUtils.getClazz(BukkitReflectionUtils.assembleMCClass("world.item.crafting.SmithingTransformRecipe"))
    );

    // 1.21.5+
    public static final Class<?> clazz$TransmuteResult =
            ReflectionUtils.getClazz(BukkitReflectionUtils.assembleMCClass("world.item.crafting.TransmuteResult"));

    public static final Constructor<?> constructor$TransmuteResult = Optional.ofNullable(clazz$TransmuteResult)
            .map(it -> ReflectionUtils.getConstructor(it, clazz$Item))
            .orElse(null);

    public static final Constructor<?> constructor$SmithingTransformRecipe = requireNonNull(
            VersionHelper.isOrAbove1_21_5()
                    ? ReflectionUtils.getConstructor(clazz$SmithingTransformRecipe, Optional.class, clazz$Ingredient, Optional.class, clazz$TransmuteResult)
                    : VersionHelper.isOrAbove1_21_2()
                    ? ReflectionUtils.getConstructor(clazz$SmithingTransformRecipe, Optional.class, Optional.class, Optional.class, clazz$ItemStack)
                    : VersionHelper.isOrAbove1_20_2()
                    ? ReflectionUtils.getConstructor(clazz$SmithingTransformRecipe, clazz$Ingredient, clazz$Ingredient, clazz$Ingredient, clazz$ItemStack)
                    : ReflectionUtils.getConstructor(clazz$SmithingTransformRecipe, clazz$ResourceLocation, clazz$Ingredient, clazz$Ingredient, clazz$Ingredient, clazz$ItemStack)
    );

    public static final Method method$RecipeManager$addRecipe = requireNonNull(
            VersionHelper.isOrAbove1_20_2() ?
                    ReflectionUtils.getMethod(clazz$RecipeManager, void.class, clazz$RecipeHolder) :
                    ReflectionUtils.getMethod(clazz$RecipeManager, void.class, clazz$Recipe)
    );

    public static final Method method$ItemStack$getItem = requireNonNull(
            ReflectionUtils.getMethod(clazz$ItemStack, clazz$Item)
    );

    public static final Class<?> clazz$BlockHitResult = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "world.phys.MovingObjectPositionBlock",
                    "world.phys.BlockHitResult"
            )
    );

    public static final Class<?> clazz$ClipContext$Fluid = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "world.level.RayTrace$FluidCollisionOption",
                    "world.level.ClipContext$Fluid"
            )
    );

    public static final Method method$ClipContext$Fluid$values = requireNonNull(
            ReflectionUtils.getStaticMethod(clazz$ClipContext$Fluid, clazz$ClipContext$Fluid.arrayType())
    );

    public static final Object instance$ClipContext$Fluid$NONE;
    public static final Object instance$ClipContext$Fluid$SOURCE_ONLY;
    public static final Object instance$ClipContext$Fluid$ANY;

    static {
        try {
            Object[] values = (Object[]) method$ClipContext$Fluid$values.invoke(null);
            instance$ClipContext$Fluid$NONE = values[0];
            instance$ClipContext$Fluid$SOURCE_ONLY = values[1];
            instance$ClipContext$Fluid$ANY = values[2];
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    public static final Method method$Item$getPlayerPOVHitResult = requireNonNull(
            ReflectionUtils.getDeclaredMethod(clazz$Item, clazz$BlockHitResult, clazz$Level, clazz$Player, clazz$ClipContext$Fluid)
    );

    public static final Method method$BlockHitResult$withPosition = requireNonNull(
            ReflectionUtils.getMethod(clazz$BlockHitResult, clazz$BlockHitResult, CoreReflections.clazz$BlockPos)
    );

    public static final Field field$BlockHitResul$blockPos = requireNonNull(
            ReflectionUtils.getDeclaredField(clazz$BlockHitResult, CoreReflections.clazz$BlockPos, 0)
    );

    public static final Field field$BlockHitResul$direction = requireNonNull(
            ReflectionUtils.getDeclaredField(clazz$BlockHitResult, CoreReflections.clazz$Direction, 0)
    );

    public static final Field field$BlockHitResul$miss = requireNonNull(
            ReflectionUtils.getDeclaredField(clazz$BlockHitResult, boolean.class, 0)
    );

    public static final Field field$BlockHitResul$inside = requireNonNull(
            ReflectionUtils.getDeclaredField(clazz$BlockHitResult, boolean.class, 1)
    );

    public static final Class<?> clazz$HitResult = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "world.phys.MovingObjectPosition",
                    "world.phys.HitResult"
            )
    );

    public static final Field field$HitResult$location = requireNonNull(
            ReflectionUtils.getDeclaredField(clazz$HitResult, clazz$Vec3, 0)
    );

    public static final Class<?> clazz$SimpleWaterloggedBlock = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "world.level.block.IBlockWaterlogged",
                    "world.level.block.SimpleWaterloggedBlock"
            )
    );

    public static final Method method$SimpleWaterloggedBlock$canPlaceLiquid = requireNonNull(
            VersionHelper.isOrAbove1_21_5()
                    ? ReflectionUtils.getMethod(clazz$SimpleWaterloggedBlock, boolean.class, clazz$LivingEntity, clazz$BlockGetter, CoreReflections.clazz$BlockPos, clazz$BlockState, clazz$Fluid)
                    : VersionHelper.isOrAbove1_20_2()
                    ? ReflectionUtils.getMethod(clazz$SimpleWaterloggedBlock, boolean.class, clazz$Player, clazz$BlockGetter, CoreReflections.clazz$BlockPos, clazz$BlockState, clazz$Fluid)
                    : ReflectionUtils.getMethod(clazz$SimpleWaterloggedBlock, boolean.class, clazz$BlockGetter, CoreReflections.clazz$BlockPos, clazz$BlockState, clazz$Fluid)
    );

    public static final Method method$SimpleWaterloggedBlock$placeLiquid = requireNonNull(
            ReflectionUtils.getMethod(clazz$SimpleWaterloggedBlock, boolean.class, clazz$LevelAccessor, CoreReflections.clazz$BlockPos, clazz$BlockState, clazz$FluidState)
    );

    public static final Method method$SimpleWaterloggedBlock$pickupBlock = requireNonNull(
            VersionHelper.isOrAbove1_21_5()
                    ? ReflectionUtils.getMethod(clazz$SimpleWaterloggedBlock, clazz$ItemStack, clazz$LivingEntity, clazz$LevelAccessor, CoreReflections.clazz$BlockPos, clazz$BlockState)
                    : VersionHelper.isOrAbove1_20_2()
                    ? ReflectionUtils.getMethod(clazz$SimpleWaterloggedBlock, clazz$ItemStack, clazz$Player, clazz$LevelAccessor, CoreReflections.clazz$BlockPos, clazz$BlockState)
                    : ReflectionUtils.getMethod(clazz$SimpleWaterloggedBlock, clazz$ItemStack, clazz$LevelAccessor, CoreReflections.clazz$BlockPos, clazz$BlockState)
    );

    public static final Method method$Fluid$getTickDelay = requireNonNull(
            ReflectionUtils.getMethod(clazz$Fluid, int.class, clazz$LevelReader)
    );

    public static final Method method$Fluid$defaultFluidState = requireNonNull(
            ReflectionUtils.getMethod(clazz$Fluid, clazz$FluidState, 0)
    );

    public static final Class<?> clazz$SingleValuePalette = requireNonNull(
            ReflectionUtils.getClazz(BukkitReflectionUtils.assembleMCClass("world.level.chunk.SingleValuePalette"))
    );

    public static final Field field$SingleValuePalette$value = requireNonNull(
            ReflectionUtils.getDeclaredField(clazz$SingleValuePalette, Object.class, 0)
    );

    public static final Class<?> clazz$HashMapPalette = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "world.level.chunk.DataPaletteHash",
                    "world.level.chunk.HashMapPalette"
            )
    );

    public static final Field field$HashMapPalette$values = requireNonNull(
            ReflectionUtils.getDeclaredField(clazz$HashMapPalette, clazz$CrudeIncrementalIntIdentityHashBiMap, 0)
    );

    public static final Class<?> clazz$LinearPalette = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "world.level.chunk.DataPaletteLinear",
                    "world.level.chunk.LinearPalette"
            )
    );

    public static final Field field$LinearPalette$values = requireNonNull(
            ReflectionUtils.getDeclaredField(clazz$LinearPalette, Object.class.arrayType(), 0)
    );

    public static final Object instance$Entity$DATA_SILENT;

    static {
        int i = 0;
        Field targetField = null;
        for (Field field : clazz$Entity.getDeclaredFields()) {
            Type fieldType = field.getGenericType();
            if (field.getType() == clazz$EntityDataAccessor && fieldType instanceof ParameterizedType paramType) {
                if (paramType.getActualTypeArguments()[0] == Boolean.class) {
                    i++;
                    if (i == 2) {
                        targetField = field;
                        break;
                    }
                }
            }
        }
        try {
            instance$Entity$DATA_SILENT = ReflectionUtils.setAccessible(requireNonNull(targetField)).get(null);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    public static final Field field$Entity$entityData = requireNonNull(
            ReflectionUtils.getDeclaredField(clazz$Entity, clazz$SynchedEntityData, 0)
    );

    public static final Class<?> clazz$SupportType = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "world.level.block.EnumBlockSupport",
                    "world.level.block.SupportType"
            )
    );

    public static final Method method$SupportType$values = requireNonNull(
            ReflectionUtils.getStaticMethod(clazz$SupportType, clazz$SupportType.arrayType())
    );

    public static final Object instance$SupportType$FULL;
    public static final Object instance$SupportType$CENTER;
    public static final Object instance$SupportType$RIGID;

    static {
        try {
            Object[] values = (Object[]) method$SupportType$values.invoke(null);
            instance$SupportType$FULL = values[0];
            instance$SupportType$CENTER = values[1];
            instance$SupportType$RIGID = values[2];
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    public static final Method method$BlockStateBase$isFaceSturdy = requireNonNull(
            ReflectionUtils.getMethod(clazz$BlockStateBase, boolean.class, clazz$BlockGetter, CoreReflections.clazz$BlockPos, CoreReflections.clazz$Direction, clazz$SupportType)
    );

    public static final Class<?> clazz$BlockInWorld = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "world.level.block.state.pattern.ShapeDetectorBlock",
                    "world.level.block.state.pattern.BlockInWorld"
            )
    );

    public static final Field field$BlockInWorld$state = requireNonNull(
            ReflectionUtils.getDeclaredField(clazz$BlockInWorld, clazz$BlockState, 0)
    );

    public static final Method method$BlockStateBase$getBlock = requireNonNull(
            ReflectionUtils.getMethod(clazz$BlockStateBase, clazz$Block)
    );

    public static final Method method$BlockBehaviour$getDescriptionId = requireNonNull(
            VersionHelper.isOrAbove1_21_2()
                    ? ReflectionUtils.getMethod(clazz$BlockBehaviour, String.class)
                    : ReflectionUtils.getMethod(clazz$Block, String.class)
    );

    public static final Class<?> clazz$BlockAndTintGetter = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "world.level.IBlockLightAccess",
                    "world.level.BlockAndTintGetter"
            )
    );

    public static final Method method$BlockAndTintGetter$getRawBrightness = requireNonNull(
            ReflectionUtils.getMethod(clazz$BlockAndTintGetter, int.class, CoreReflections.clazz$BlockPos, int.class)
    );

    public static final Field field$Entity$boundingBox = requireNonNull(
            ReflectionUtils.getInstanceDeclaredField(clazz$Entity, clazz$AABB, 0)
    );

    public static final Class<?> clazz$Shulker = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "world.entity.monster.EntityShulker",
                    "world.entity.monster.Shulker"
            )
    );

    public static final Class<?> clazz$Pose = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "world.entity.EntityPose",
                    "world.entity.Pose"
            )
    );

    public static final Method method$Pose$values = requireNonNull(
            ReflectionUtils.getStaticMethod(clazz$Pose, clazz$Pose.arrayType())
    );

    public static final Object instance$Pose$STANDING;
    public static final Object instance$Pose$FALL_FLYING;
    public static final Object instance$Pose$SLEEPING;
    public static final Object instance$Pose$SWIMMING;
    public static final Object instance$Pose$SPIN_ATTACK;
    public static final Object instance$Pose$CROUCHING;
    public static final Object instance$Pose$LONG_JUMPING;
    public static final Object instance$Pose$DYING;
    public static final Object instance$Pose$CROAKING;
    public static final Object instance$Pose$USING_TONGUE;
    public static final Object instance$Pose$SITTING;
    public static final Object instance$Pose$ROARING;
    public static final Object instance$Pose$SNIFFING;
    public static final Object instance$Pose$EMERGING;
    public static final Object instance$Pose$DIGGING;
    public static final Object instance$Pose$SLIDING;
    public static final Object instance$Pose$SHOOTING;
    public static final Object instance$Pose$INHALING;
    public static final Object[] instance$Poses;

    static {
        try {
            instance$Poses = (Object[]) method$Pose$values.invoke(null);
            instance$Pose$STANDING = instance$Poses[0];
            instance$Pose$FALL_FLYING = instance$Poses[1];
            instance$Pose$SLEEPING = instance$Poses[2];
            instance$Pose$SWIMMING = instance$Poses[3];
            instance$Pose$SPIN_ATTACK = instance$Poses[4];
            instance$Pose$CROUCHING = instance$Poses[5];
            instance$Pose$LONG_JUMPING = instance$Poses[6];
            instance$Pose$DYING = instance$Poses[7];
            instance$Pose$CROAKING = instance$Poses[8];
            instance$Pose$USING_TONGUE = instance$Poses[9];
            instance$Pose$SITTING = instance$Poses[10];
            instance$Pose$ROARING = instance$Poses[11];
            instance$Pose$SNIFFING = instance$Poses[12];
            instance$Pose$EMERGING = instance$Poses[13];
            instance$Pose$DIGGING = instance$Poses[14];
            if (VersionHelper.isOrAbove1_20_3()) {
                instance$Pose$SLIDING = instance$Poses[15];
                instance$Pose$SHOOTING = instance$Poses[16];
                instance$Pose$INHALING = instance$Poses[17];
            } else {
                instance$Pose$SLIDING = null;
                instance$Pose$SHOOTING = null;
                instance$Pose$INHALING = null;
            }
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    public static final Class<?> clazz$Attributes = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "world.entity.ai.attributes.GenericAttributes",
                    "world.entity.ai.attributes.Attributes"
            )
    );

    // 1.20.5+
    public static final Constructor<?> constructor$AttributeInstance =
            ReflectionUtils.getConstructor(clazz$AttributeInstance, CoreReflections.clazz$Holder, Consumer.class);

    public static final Method method$AttributeInstance$setBaseValue = requireNonNull(
            ReflectionUtils.getMethod(clazz$AttributeInstance, void.class, double.class)
    );

    public static final Method method$Entity$canBeCollidedWith = requireNonNull(
            VersionHelper.isOrAbove1_20_5()
                    ? ReflectionUtils.getMethod(clazz$Entity, boolean.class, new String[]{"canBeCollidedWith"})
                    : VersionHelper.isOrAbove1_20_3()
                    ? ReflectionUtils.getMethod(clazz$Entity, boolean.class, new String[]{"bz"})
                    : VersionHelper.isOrAbove1_20_2()
                    ? ReflectionUtils.getMethod(clazz$Entity, boolean.class, new String[]{"bx"})
                    : VersionHelper.isOrAbove1_20()
                    ? ReflectionUtils.getMethod(clazz$Entity, boolean.class, new String[]{"bu"})
                    : ReflectionUtils.getMethod(clazz$Entity, boolean.class, new String[]{"canBeCollidedWith", "bu", "bx", "bz"})
    );

    public static final Method method$Entity$getId = requireNonNull(
            VersionHelper.isOrAbove1_20_5()
                    ? ReflectionUtils.getMethod(clazz$Entity, int.class, new String[]{"getId"})
                    : VersionHelper.isOrAbove1_20_3()
                    ? ReflectionUtils.getMethod(clazz$Entity, int.class, new String[]{"aj"})
                    : VersionHelper.isOrAbove1_20_2()
                    ? ReflectionUtils.getMethod(clazz$Entity, int.class, new String[]{"ah"})
                    : VersionHelper.isOrAbove1_20()
                    ? ReflectionUtils.getMethod(clazz$Entity, int.class, new String[]{"af"})
                    : ReflectionUtils.getMethod(clazz$Entity, int.class, new String[]{"getId", "aj", "ah", "af"})
    );

    public static final Class<?> clazz$Rotation = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "world.level.block.EnumBlockRotation",
                    "world.level.block.Rotation"
            )
    );

    public static final Method method$Rotation$values = requireNonNull(
            ReflectionUtils.getStaticMethod(clazz$Rotation, clazz$Rotation.arrayType())
    );

    public static final Object instance$Rotation$NONE;
    public static final Object instance$Rotation$CLOCKWISE_90;
    public static final Object instance$Rotation$CLOCKWISE_180;
    public static final Object instance$Rotation$COUNTERCLOCKWISE_90;

    static {
        try {
            Object[] values = (Object[]) method$Rotation$values.invoke(null);
            instance$Rotation$NONE = values[0];
            instance$Rotation$CLOCKWISE_90 = values[1];
            instance$Rotation$CLOCKWISE_180 = values[2];
            instance$Rotation$COUNTERCLOCKWISE_90 = values[3];
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    public static final Method method$Rotation$ordinal = requireNonNull(
            ReflectionUtils.getMethod(clazz$Rotation, int.class, new String[]{"ordinal"})
    );

    public static final Class<?> clazz$Mirror = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "world.level.block.EnumBlockMirror",
                    "world.level.block.Mirror"
            )
    );


    public static final Method method$Mirror$values = requireNonNull(
            ReflectionUtils.getStaticMethod(
                    clazz$Mirror, clazz$Mirror.arrayType()
            )
    );

    public static final Object instance$Mirror$NONE;
    public static final Object instance$Mirror$LEFT_RIGHT;
    public static final Object instance$Mirror$FRONT_BACK;

    static {
        try {
            Object[] values = (Object[]) method$Mirror$values.invoke(null);
            instance$Mirror$NONE = values[0];
            instance$Mirror$LEFT_RIGHT = values[1];
            instance$Mirror$FRONT_BACK = values[2];
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    public static final Method method$Mirror$ordinal = requireNonNull(
            ReflectionUtils.getMethod(clazz$Mirror, int.class, new String[]{"ordinal"})
    );

    public static final Method method$BlockBehaviour$rotate = requireNonNull(
            ReflectionUtils.getDeclaredMethod(clazz$BlockBehaviour, clazz$BlockState, clazz$BlockState, clazz$Rotation)
    );

    public static final Method method$BlockBehaviour$mirror = requireNonNull(
            ReflectionUtils.getDeclaredMethod(clazz$BlockBehaviour, clazz$BlockState, clazz$BlockState, clazz$Mirror)
    );

    public static final Method method$BlockStateBase$rotate = requireNonNull(
            ReflectionUtils.getMethod(clazz$BlockStateBase, clazz$BlockState, clazz$Rotation)
    );

    public static final Method method$BlockStateBase$mirror = requireNonNull(
            ReflectionUtils.getMethod(clazz$BlockStateBase, clazz$BlockState, clazz$Mirror)
    );

    public static final Method method$Entity$getType = requireNonNull(
            ReflectionUtils.getMethod(clazz$Entity, clazz$EntityType)
    );

    public static final Class<?> clazz$AbstractArrow = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "world.entity.projectile.EntityArrow",
                    "world.entity.projectile.AbstractArrow"
            )
    );

    public static final Class<?> clazz$MoverType = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "world.entity.EnumMoveType",
                    "world.entity.MoverType"
            )
    );

    public static final Method method$MoverType$values = requireNonNull(
            ReflectionUtils.getStaticMethod(
                    clazz$MoverType, clazz$MoverType.arrayType()
            )
    );

    public static final Object instance$MoverType$SELF;
    public static final Object instance$MoverType$PLAYER;
    public static final Object instance$MoverType$PISTON;
    public static final Object instance$MoverType$SHULKER_BOX;
    public static final Object instance$MoverType$SHULKER;

    static {
        try {
            Object[] values = (Object[]) method$MoverType$values.invoke(null);
            instance$MoverType$SELF = values[0];
            instance$MoverType$PLAYER = values[1];
            instance$MoverType$PISTON = values[2];
            instance$MoverType$SHULKER_BOX = values[3];
            instance$MoverType$SHULKER = values[4];
        } catch (ReflectiveOperationException e) {
            throw new AssertionError(e);
        }
    }

    public static final Class<?> clazz$AbstractArrow$Pickup = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "world.entity.projectile.EntityArrow$PickupStatus",
                    "world.entity.projectile.AbstractArrow$Pickup"
            )
    );

    public static final Method method$AbstractArrow$Pickup$values = requireNonNull(
            ReflectionUtils.getStaticMethod(clazz$AbstractArrow$Pickup, clazz$AbstractArrow$Pickup.arrayType())
    );

    public static final Object instance$AbstractArrow$Pickup$DISALLOWED;
    public static final Object instance$AbstractArrow$Pickup$ALLOWED;
    public static final Object instance$AbstractArrow$Pickup$CREATIVE_ONLY;

    static {
        try {
            Object[] values = (Object[]) method$AbstractArrow$Pickup$values.invoke(null);
            instance$AbstractArrow$Pickup$DISALLOWED = values[0];
            instance$AbstractArrow$Pickup$ALLOWED = values[1];
            instance$AbstractArrow$Pickup$CREATIVE_ONLY = values[2];
        } catch (ReflectiveOperationException e) {
            throw new AssertionError(e);
        }
    }

    public static final Class<?> clazz$Orientation =
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "world.level.redstone.Orientation",
                    "world.level.redstone.Orientation"
            );

    public static final Method method$BlockBehaviour$neighborChanged = requireNonNull(
            VersionHelper.isOrAbove1_21_2() ?
                    ReflectionUtils.getDeclaredMethod(clazz$BlockBehaviour, void.class, clazz$BlockState, clazz$Level, CoreReflections.clazz$BlockPos, clazz$Block, clazz$Orientation, boolean.class) :
                    Optional.ofNullable(ReflectionUtils.getDeclaredMethod(clazz$BlockBehaviour, void.class, clazz$BlockState, clazz$Level, CoreReflections.clazz$BlockPos, clazz$Block, CoreReflections.clazz$BlockPos, boolean.class))
                            .orElse(ReflectionUtils.getMethod(clazz$BlockBehaviour, void.class, clazz$BlockState, clazz$Level, CoreReflections.clazz$BlockPos, clazz$Block, CoreReflections.clazz$BlockPos, boolean.class))
    );

    public static final Class<?> clazz$InventoryMenu = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "world.inventory.ContainerPlayer",
                    "world.inventory.InventoryMenu"
            )
    );

    public static final Field field$Player$inventoryMenu = requireNonNull(
            ReflectionUtils.getDeclaredField(clazz$Player, clazz$InventoryMenu, 0)
    );

    public static final Method method$AbstractContainerMenu$incrementStateId = requireNonNull(
            ReflectionUtils.getMethod(clazz$AbstractContainerMenu, int.class, new String[]{"incrementStateId", "k"})
    );

    public static final Field field$BlockParticleOption$blockState = requireNonNull(
            ReflectionUtils.getDeclaredField(clazz$BlockParticleOption, clazz$BlockState, 0)
    );

    public static final Class<?> clazz$ServerPlayer = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "server.level.EntityPlayer",
                    "server.level.ServerPlayer"
            )
    );

    public static final Class<?> clazz$ServerGamePacketListenerImpl = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "server.network.PlayerConnection",
                    "server.network.ServerGamePacketListenerImpl"
            )
    );

    public static final Class<?> clazz$ServerCommonPacketListenerImpl = requireNonNull(
            clazz$ServerGamePacketListenerImpl.getSuperclass()
    );

    public static final Field field$ServerPlayer$connection = requireNonNull(
            ReflectionUtils.getInstanceDeclaredField(clazz$ServerPlayer, clazz$ServerGamePacketListenerImpl, 0)
    );

    public static final Class<?> clazz$ServerLevel = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "server.level.WorldServer",
                    "server.level.ServerLevel"
            )
    );

    public static final Method method$ServerLevel$getNoiseBiome = requireNonNull(
            ReflectionUtils.getMethod(clazz$ServerLevel, CoreReflections.clazz$Holder, int.class, int.class, int.class)
    );

    public static final Class<?> clazz$MinecraftServer = requireNonNull(
            ReflectionUtils.getClazz(BukkitReflectionUtils.assembleMCClass("server.MinecraftServer"))
    );

    public static final Method method$MinecraftServer$getServer = requireNonNull(
            ReflectionUtils.getMethod(clazz$MinecraftServer, new String[] { "getServer" })
    );

    public static final Field field$MinecraftServer$registries = requireNonNull(
            ReflectionUtils.getDeclaredField(clazz$MinecraftServer, CoreReflections.clazz$LayeredRegistryAccess, 0)
    );

    public static final Class<?> clazz$ServerConnectionListener = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "server.network.ServerConnection",
                    "server.network.ServerConnectionListener"
            )
    );

    public static final Field field$MinecraftServer$connection = requireNonNull(
            ReflectionUtils.getDeclaredField(clazz$MinecraftServer, clazz$ServerConnectionListener, 0)
    );

    public static final Field field$ServerConnectionListener$channels;

    static {
        Field[] fields = clazz$ServerConnectionListener.getDeclaredFields();
        Field f = null;
        for (Field field : fields) {
            if (List.class.isAssignableFrom(field.getType())) {
                Type genericType = field.getGenericType();
                if (genericType instanceof ParameterizedType paramType) {
                    Type[] actualTypeArguments = paramType.getActualTypeArguments();
                    if (actualTypeArguments.length > 0 && actualTypeArguments[0] == ChannelFuture.class) {
                        f = ReflectionUtils.setAccessible(field);
                        break;
                    }
                }
            }
        }
        field$ServerConnectionListener$channels = requireNonNull(f);
    }

    public static final Class<?> clazz$ServerChunkCache = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "server.level.ChunkProviderServer",
                    "server.level.ServerChunkCache"
            )
    );

    public static final Class<?> clazz$ChunkHolder = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "server.level.PlayerChunk",
                    "server.level.ChunkHolder"
            )
    );

    public static final Class<?> clazz$ChunkMap = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "server.level.PlayerChunkMap",
                    "server.level.ChunkMap"
            )
    );

    public static final Class<?> clazz$ChunkHolder$PlayerProvider = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "server.level.PlayerChunk$d",
                    "server.level.ChunkHolder$PlayerProvider"
            )
    );

    public static final Field field$ChunkHolder$playerProvider = requireNonNull(
            ReflectionUtils.getDeclaredField(clazz$ChunkHolder, clazz$ChunkHolder$PlayerProvider, 0)
    );

    public static final Method method$ChunkHolder$PlayerProvider$getPlayers = requireNonNull(
            ReflectionUtils.getMethod(clazz$ChunkHolder$PlayerProvider, List.class, clazz$ChunkPos, boolean.class)
    );

    public static final Method method$ChunkHolder$getPlayers =
            ReflectionUtils.getMethod(clazz$ChunkHolder, List.class, boolean.class);

    public static final Field field$ChunkHolder$lightEngine = requireNonNull(
            ReflectionUtils.getDeclaredField(clazz$ChunkHolder, clazz$LevelLightEngine, 0)
    );

    public static final Field field$ChunkHolder$blockChangedLightSectionFilter = requireNonNull(
            ReflectionUtils.getDeclaredField(clazz$ChunkHolder, BitSet.class, 0)
    );

    public static final Field field$ChunkHolder$skyChangedLightSectionFilter = requireNonNull(
            ReflectionUtils.getDeclaredField(clazz$ChunkHolder, BitSet.class, 1)
    );

    public static final Method method$ServerChunkCache$getVisibleChunkIfPresent = requireNonNull(
            ReflectionUtils.getDeclaredMethod(clazz$ServerChunkCache, clazz$ChunkHolder, long.class)
    );

    public static final Method method$ChunkHolder$sectionLightChanged = requireNonNull(
            VersionHelper.isOrAbove1_21_2() ?
                    ReflectionUtils.getMethod(clazz$ChunkHolder, boolean.class, clazz$LightLayer, int.class) :
                    ReflectionUtils.getMethod(clazz$ChunkHolder, void.class, clazz$LightLayer, int.class)
    );

    public static final Method method$ServerPlayer$getAttribute = requireNonNull(
            VersionHelper.isOrAbove1_20_5() ?
                    ReflectionUtils.getMethod(clazz$ServerPlayer, clazz$AttributeInstance, CoreReflections.clazz$Holder) :
                    ReflectionUtils.getMethod(clazz$ServerPlayer, clazz$AttributeInstance, clazz$Attribute)
    );

    public static final Class<?> clazz$ServerPlayerGameMode = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "server.level.PlayerInteractManager",
                    "server.level.ServerPlayerGameMode"
            )
    );

    public static final Field field$ServerPlayer$gameMode = requireNonNull(
            ReflectionUtils.getDeclaredField(clazz$ServerPlayer, clazz$ServerPlayerGameMode, 0)
    );

    public static final Field field$ServerPlayerGameMode$destroyProgressStart = requireNonNull(
            ReflectionUtils.getDeclaredField(clazz$ServerPlayerGameMode, int.class, 0)
    );


    public static final Field field$ServerPlayerGameMode$gameTicks = requireNonNull(
            ReflectionUtils.getDeclaredField(clazz$ServerPlayerGameMode, int.class, 1)
    );

    public static final Field field$ServerPlayerGameMode$delayedTickStart = requireNonNull(
            ReflectionUtils.getDeclaredField(clazz$ServerPlayerGameMode, int.class, 2)
    );

    public static final Field field$ServerPlayerGameMode$isDestroyingBlock = requireNonNull(
            ReflectionUtils.getDeclaredField(clazz$ServerPlayerGameMode, boolean.class, 0)
    );

    public static final Field field$ServerPlayerGameMode$hasDelayedDestroy = requireNonNull(
            ReflectionUtils.getDeclaredField(clazz$ServerPlayerGameMode, boolean.class, 1)
    );

    public static final Method method$ServerPlayerGameMode$destroyBlock = requireNonNull(
            ReflectionUtils.getMethod(clazz$ServerPlayerGameMode, boolean.class, CoreReflections.clazz$BlockPos)
    );

    public static final Method method$ServerPlayer$getEffect = requireNonNull(
            !VersionHelper.isOrAbove1_20_5() ?
                    ReflectionUtils.getMethod(clazz$ServerPlayer, clazz$MobEffectInstance, clazz$MobEffect) :
                    ReflectionUtils.getMethod(clazz$ServerPlayer, clazz$MobEffectInstance, CoreReflections.clazz$Holder)
    );

    public static final Field field$ServerLevel$uuid = requireNonNull(
            ReflectionUtils.getDeclaredField(clazz$ServerLevel, UUID.class, 0)
    );

    public static final Method method$ServerLevel$checkEntityCollision = requireNonNull(
            ReflectionUtils.getMethod(clazz$ServerLevel, boolean.class, clazz$BlockState, clazz$Entity, clazz$CollisionContext, CoreReflections.clazz$BlockPos, boolean.class)
    );

    public static final Class<?> clazz$ResourceManager = requireNonNull(
            ReflectionUtils.getClazz(
                    BukkitReflectionUtils.assembleMCClass("server.packs.resources.IResourceManager"),  // 这里paper会自动获取到NM.server.packs.resources.ResourceManager
                    BukkitReflectionUtils.assembleMCClass("server.packs.resources.ResourceManager") // 如果插件是mojmap就会走这个
            )
    );

    public static final Class<?> clazz$Resource = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "server.packs.resources.IResource",
                    "server.packs.resources.Resource"
            )
    );

    public static final Method method$Resource$openAsReader = requireNonNull(
            ReflectionUtils.getMethod(clazz$Resource, BufferedReader.class)
    );

    public static final Class<?> clazz$MultiPackResourceManager = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "server.packs.resources.ResourceManager",
                    "server.packs.resources.MultiPackResourceManager"
            )
    );

    public static final Class<?> clazz$PackType = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "server.packs.EnumResourcePackType",
                    "server.packs.PackType"
            )
    );

    public static final Method method$PackType$values = requireNonNull(
            ReflectionUtils.getStaticMethod(clazz$PackType, clazz$PackType.arrayType())
    );

    public static final Object instance$PackType$SERVER_DATA;

    static {
        try {
            Object[] values = (Object[]) method$PackType$values.invoke(null);
            instance$PackType$SERVER_DATA = values[1];
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    public static final Class<?> clazz$PackRepository = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "server.packs.repository.ResourcePackRepository",
                    "server.packs.repository.PackRepository"
            )
    );

    public static final Method method$MinecraftServer$getPackRepository = requireNonNull(
            ReflectionUtils.getMethod(clazz$MinecraftServer, clazz$PackRepository)
    );

    public static final Field field$PackRepository$selected = requireNonNull(
            ReflectionUtils.getInstanceDeclaredField(clazz$PackRepository, List.class, 0)
    );

    public static final Class<?> clazz$Pack = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "server.packs.repository.ResourcePackLoader",
                    "server.packs.repository.Pack"
            )
    );

    public static final Method method$PackRepository$getPack = requireNonNull(
            ReflectionUtils.getMethod(clazz$PackRepository, clazz$Pack, String.class)
    );

    public static final Method method$Pack$getId = requireNonNull(
            ReflectionUtils.getMethod(clazz$Pack, String.class)
    );

    public static final Method method$MinecraftServer$reloadResources = requireNonNull(
            ReflectionUtils.getMethod(clazz$MinecraftServer, CompletableFuture.class, Collection.class)
    );

    public static final Class<?> clazz$PackResources = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "server.packs.IResourcePack",
                    "server.packs.PackResources"
            )
    );

    public static final Method method$Pack$open = requireNonNull(
            ReflectionUtils.getMethod(clazz$Pack, clazz$PackResources)
    );

    public static final Constructor<?> constructor$MultiPackResourceManager = requireNonNull(
            ReflectionUtils.getConstructor(clazz$MultiPackResourceManager, clazz$PackType, List.class)
    );

    public static final Method method$MinecraftServer$getRecipeManager = requireNonNull(
            ReflectionUtils.getMethod(clazz$MinecraftServer, clazz$RecipeManager)
    );

    public static final Class<?> clazz$DedicatedPlayerList = requireNonNull(
            ReflectionUtils.getClazz(BukkitReflectionUtils.assembleMCClass("server.dedicated.DedicatedPlayerList"))
    );

    public static final Method method$DedicatedPlayerList$reloadRecipes = requireNonNull(
            ReflectionUtils.getMethod(clazz$DedicatedPlayerList, new String[] {"reloadRecipeData", "reloadRecipes"})
    );

    public static final Method method$ServerChunkCache$getGenerator = requireNonNull(
            ReflectionUtils.getMethod(clazz$ServerChunkCache, clazz$ChunkGenerator)
    );

    public static final Method method$ServerLevel$sendBlockUpdated = requireNonNull(
            ReflectionUtils.getMethod(
                    clazz$ServerLevel, void.class, CoreReflections.clazz$BlockPos, clazz$BlockState, clazz$BlockState, int.class
            )
    );

    public static final Method method$ServerLevel$levelEvent = requireNonNull(
            VersionHelper.isOrAbove1_21_5()
                    ? ReflectionUtils.getMethod(clazz$ServerLevel, void.class, clazz$Entity, int.class, CoreReflections.clazz$BlockPos, int.class)
                    : ReflectionUtils.getMethod(clazz$ServerLevel, void.class, clazz$Player, int.class, CoreReflections.clazz$BlockPos, int.class)
    );

    public static final Method method$ServerGamePacketListenerImpl$tryPickItem =
            VersionHelper.isOrAbove1_21_5() ?
                    ReflectionUtils.getDeclaredMethod(clazz$ServerGamePacketListenerImpl, void.class, clazz$ItemStack, CoreReflections.clazz$BlockPos, clazz$Entity, boolean.class) :
                    ReflectionUtils.getDeclaredMethod(clazz$ServerGamePacketListenerImpl, void.class, clazz$ItemStack);

    public static final Method method$ServerPlayer$nextContainerCounter = requireNonNull(
            ReflectionUtils.getMethod(clazz$ServerPlayer, int.class, new String[] {"nextContainerCounter"})
    );

    public static final Method method$ServerPlayer$initMenu = requireNonNull(
            ReflectionUtils.getMethod(clazz$ServerPlayer, void.class, clazz$AbstractContainerMenu)
    );

    public static final Class<?> clazz$DedicatedServerProperties = requireNonNull(
            ReflectionUtils.getClazz(BukkitReflectionUtils.assembleMCClass("server.dedicated.DedicatedServerProperties"))
    );

    public static final Class<?> clazz$DedicatedServerSettings = requireNonNull(
            ReflectionUtils.getClazz(BukkitReflectionUtils.assembleMCClass("server.dedicated.DedicatedServerSettings"))
    );

    public static final Class<?> clazz$DedicatedServer = requireNonNull(
            ReflectionUtils.getClazz(BukkitReflectionUtils.assembleMCClass("server.dedicated.DedicatedServer"))
    );

    public static final Field field$DedicatedServerSettings$properties = requireNonNull(
            ReflectionUtils.getDeclaredField(clazz$DedicatedServerSettings, clazz$DedicatedServerProperties, 0)
    );

    public static final Field field$DedicatedServer$settings = requireNonNull(
            ReflectionUtils.getDeclaredField(clazz$DedicatedServer, clazz$DedicatedServerSettings, 0)
    );

    public static final Class<?> clazz$MinecraftServer$ServerResourcePackInfo = requireNonNull(
            ReflectionUtils.getClazz(BukkitReflectionUtils.assembleMCClass("server.MinecraftServer$ServerResourcePackInfo"))
    );

    public static final Field field$DedicatedServerProperties$serverResourcePackInfo = requireNonNull(
            ReflectionUtils.getDeclaredField(clazz$DedicatedServerProperties, Optional.class, 0)
    );

    public static final Constructor<?> constructor$ServerResourcePackInfo = requireNonNull(
            ReflectionUtils.getConstructor(clazz$MinecraftServer$ServerResourcePackInfo, 0)
    );

    // 1.20.2+
    // 从 1.21.2+ 才有 particleStatus
    public static final Class<?> clazz$ClientInformation =
            ReflectionUtils.getClazz(BukkitReflectionUtils.assembleMCClass("server.level.ClientInformation"));

    // 1.20.2+
    public static final Constructor<?> constructor$ClientInformation = Optional.ofNullable(clazz$ClientInformation)
            .map(it -> ReflectionUtils.getConstructor(it, 1))
            .orElse(null);

    // 1.20.2+ String
    public static final Field field$ClientInformation$language = Optional.ofNullable(clazz$ClientInformation)
            .map(it -> ReflectionUtils.getDeclaredField(it, 0))
            .orElse(null);

    // 1.20.2+ int
    public static final Field field$ClientInformation$viewDistance = Optional.ofNullable(clazz$ClientInformation)
            .map(it -> ReflectionUtils.getDeclaredField(it, 1))
            .orElse(null);

    // 1.20.2+ ChatVisiblity
    public static final Field field$ClientInformation$chatVisibility = Optional.ofNullable(clazz$ClientInformation)
            .map(it -> ReflectionUtils.getDeclaredField(it, 2))
            .orElse(null);

    // 1.20.2+ boolean
    public static final Field field$ClientInformation$chatColors = Optional.ofNullable(clazz$ClientInformation)
            .map(it -> ReflectionUtils.getDeclaredField(it, 3))
            .orElse(null);

    // 1.20.2+ int
    public static final Field field$ClientInformation$modelCustomisation = Optional.ofNullable(clazz$ClientInformation)
            .map(it -> ReflectionUtils.getDeclaredField(it, 4))
            .orElse(null);

    // 1.20.2+ HumanoidArm
    public static final Field field$ClientInformation$mainHand = Optional.ofNullable(clazz$ClientInformation)
            .map(it -> ReflectionUtils.getDeclaredField(it, 5))
            .orElse(null);

    // 1.20.2+ boolean
    public static final Field field$ClientInformation$textFilteringEnabled = Optional.ofNullable(clazz$ClientInformation)
            .map(it -> ReflectionUtils.getDeclaredField(it, 6))
            .orElse(null);

    // 1.20.2+ boolean
    public static final Field field$ClientInformation$allowsListing = Optional.ofNullable(clazz$ClientInformation)
            .map(it -> ReflectionUtils.getDeclaredField(it, 7))
            .orElse(null);

    // 1.21.2+
    public static final Class<?> clazz$ParticleStatus =
            ReflectionUtils.getClazz(BukkitReflectionUtils.assembleMCClass("server.level.ParticleStatus"));

    // 1.21.2+
    public static final Method method$ParticleStatus$values = Optional.ofNullable(clazz$ParticleStatus)
            .map(it -> ReflectionUtils.getStaticMethod(it, it.arrayType()))
            .orElse(null);

    // 1.21.2+
    public static final Object instance$ParticleStatus$ALL;
    public static final Object instance$ParticleStatus$DECREASED;
    public static final Object instance$ParticleStatus$MINIMAL;

    // 1.21.2+
    static {
        try {
            if (VersionHelper.isOrAbove1_21_2()) {
                Object[] values = (Object[]) method$ParticleStatus$values.invoke(null);
                instance$ParticleStatus$ALL = values[0];
                instance$ParticleStatus$DECREASED = values[1];
                instance$ParticleStatus$MINIMAL = values[2];
            } else {
                instance$ParticleStatus$ALL = null;
                instance$ParticleStatus$DECREASED = null;
                instance$ParticleStatus$MINIMAL = null;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // 1.21.2+ ParticleStatus
    public static final Field field$ClientInformation$particleStatus = Optional.ofNullable(clazz$ClientInformation)
            .map(it -> ReflectionUtils.getDeclaredField(it, 8))
            .orElse(null);
    
    public static final Class<?> clazz$ServerEntity = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "server.level.EntityTrackerEntry",
                    "server.level.ServerEntity")
    );

    public static final Field field$ServerEntity$updateInterval = requireNonNull(
            ReflectionUtils.getInstanceDeclaredField(
                    clazz$ServerEntity, int.class, 0
            )
    );

    public static final Method method$BonemealableBlock$performBonemeal = requireNonNull(
            ReflectionUtils.getMethod(clazz$BonemealableBlock, void.class, clazz$ServerLevel, clazz$RandomSource, CoreReflections.clazz$BlockPos, clazz$BlockState)
    );
    
    public static final Method method$BlockBehaviour$tick = requireNonNull(
            ReflectionUtils.getDeclaredMethod(clazz$BlockBehaviour, void.class, new String[]{"tick", "a"}, clazz$BlockState, clazz$ServerLevel, CoreReflections.clazz$BlockPos, clazz$RandomSource)
    );

    public static final Method method$BlockBehaviour$randomTick = requireNonNull(
            ReflectionUtils.getDeclaredMethod(clazz$BlockBehaviour, void.class, new String[]{"randomTick", "b"}, clazz$BlockState, clazz$ServerLevel, CoreReflections.clazz$BlockPos, clazz$RandomSource)
    );

    public static final Method method$FileToIdConverter$listMatchingResources = requireNonNull(
            ReflectionUtils.getMethod(clazz$FileToIdConverter, Map.class, new String[]{"listMatchingResources", "a"}, clazz$ResourceManager)
    );

    public static final Method method$RegistryOps$create = requireNonNull(
            ReflectionUtils.getStaticMethod(clazz$RegistryOps, clazz$RegistryOps, DynamicOps.class, CoreReflections.clazz$HolderLookup$Provider)
    );

    public static final Method method$DefaultedRegistry$get = requireNonNull(
            ReflectionUtils.getMethod(clazz$DefaultedRegistry, Object.class, clazz$ResourceLocation)
    );

    public static final Class<?> clazz$BlockStateParser = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                "commands.arguments.blocks.ArgumentBlock",
                    "commands.arguments.blocks.BlockStateParser"
            )
    );

    public static final Class<?> clazz$BlockStateParser$BlockResult = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "commands.arguments.blocks.ArgumentBlock$a",
                    "commands.arguments.blocks.BlockStateParser$BlockResult"
            )
    );

    public static final Class<?> clazz$HolderLookup = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "core.HolderLookup",
                    "core.HolderLookup"
            )
    );

    public static final Class<?> clazz$HolderLookup$RegistryLookup = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "core.HolderLookup$c",
                    "core.HolderLookup$RegistryLookup"
            )
    );

    public static final Method method$BlockStateParser$BlockResult$blockState = requireNonNull(
            ReflectionUtils.getMethod(clazz$BlockStateParser$BlockResult, clazz$BlockState, 0)
    );

    public static final Method method$BlockStateParser$parseForBlock = requireNonNull(
            ReflectionUtils.getStaticMethod(clazz$BlockStateParser, clazz$BlockStateParser$BlockResult, new String[]{"parseForBlock", "a"}, clazz$HolderLookup, String.class, boolean.class)
    );

    // 1.21.1-
    public static final Method method$Registry$asLookup = ReflectionUtils.getMethod(
            clazz$Registry, clazz$HolderLookup$RegistryLookup, new String[]{"asLookup", "p"}
    );

    public static final Method method$Entity$setInvisible = requireNonNull(
            ReflectionUtils.getMethod(
                    clazz$Entity, new String[]{"j", "setInvisible"}, boolean.class
            )
    );

    public static final Class<?> clazz$GameProfile = requireNonNull(
            ReflectionUtils.getClazz("com.mojang.authlib.GameProfile")
    );

    public static final Constructor<?> constructor$GameProfile = requireNonNull(
            ReflectionUtils.getConstructor(
                    clazz$GameProfile, 0
            )
    );

    public static final Method method$GameProfile$getProperties = requireNonNull(
            ReflectionUtils.getMethod(
                    clazz$GameProfile, ForwardingMultimap.class
            )
    );

    public static final Constructor<?> constructor$ServerPlayer = requireNonNull(
            ReflectionUtils.getConstructor(
                    clazz$ServerPlayer, 0
            )
    );

    public static final Method method$ServerPlayer$getGameProfile = requireNonNull(
            ReflectionUtils.getMethod(
                    clazz$ServerPlayer, clazz$GameProfile, 0
            )
    );

    // 1.20.2 +
    public static final Method method$ServerPlayer$clientInformation = VersionHelper.isOrAbove1_20_2() ? requireNonNull(ReflectionUtils.getMethod(
            clazz$ServerPlayer, clazz$ClientInformation, 0
    )) : null;

    public static final Class<?> clazz$CompoundTag = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "nbt.NBTTagCompound",
                    "nbt.CompoundTag"
            )
    );

    public static final Object instance$CompoundTag$Empty;

    static {
        try {
            instance$CompoundTag$Empty = CoreReflections.clazz$CompoundTag.getConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Failed to instantiate empty CompoundTag", e);
        }
    }

    public static final Method method$Entity$getEntityData = requireNonNull(
            ReflectionUtils.getMethod(
                    clazz$Entity, clazz$SynchedEntityData, 0
            )
    );

    public static final Method method$SynchedEntityData$set = requireNonNull(
            ReflectionUtils.getMethod(
                    clazz$SynchedEntityData, void.class, clazz$EntityDataAccessor, Object.class
            )
    );

    public static final Method method$SynchedEntityData$isDirty = requireNonNull(
            ReflectionUtils.getMethod(
                    clazz$SynchedEntityData, boolean.class
            )
    );

    public static final Method method$SynchedEntityData$packDirty = requireNonNull(
            ReflectionUtils.getMethod(
                    clazz$SynchedEntityData, List.class, new String[]{"b", "packDirty"}
            )
    );

    public static final Class<?> clazz$SynchedEntityData$DataItem = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "network.syncher.DataWatcher$Item",
                    "network.syncher.SynchedEntityData$DataItem"
            )
    );

    public static final Method method$SynchedEntityData$getItem = requireNonNull(
            ReflectionUtils.getDeclaredMethod(
                    clazz$SynchedEntityData, clazz$SynchedEntityData$DataItem, clazz$EntityDataAccessor)
    );

    public static final Method method$SynchedEntityData$DataItem$value = requireNonNull(
            ReflectionUtils.getMethod(
                    clazz$SynchedEntityData$DataItem, clazz$SynchedEntityData$DataValue, 0
            )
    );

    public static final Class<?> clazz$ClientBoundPlayerInfoRemovePacket = requireNonNull(
            ReflectionUtils.getClazz(
                    BukkitReflectionUtils.assembleMCClass("network.protocol.game.ClientboundPlayerInfoRemovePacket")
            )
    );

    public static final Constructor<?> constructor$ClientBoundPlayerInfoRemovePacket = requireNonNull(
            ReflectionUtils.getConstructor(clazz$ClientBoundPlayerInfoRemovePacket, List.class)
    );

    public static final Class<?> clazz$ClientBoundPlayerInfoUpdatePacket$Entry = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "network.protocol.game.ClientboundPlayerInfoUpdatePacket$b",
                    "network.protocol.game.ClientboundPlayerInfoUpdatePacket$Entry"
            )
    );

    public static final Class<?> clazz$RemoteChatSession$Data = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "network.chat.RemoteChatSession$a",
                    "network.chat.RemoteChatSession$Data"
            )
    );

    public static final Constructor<?> constructor$ClientBoundPlayerInfoUpdatePacket$Entry = requireNonNull(
            VersionHelper.isOrAbove1_21_3()
                    ? VersionHelper.isOrAbove1_21_4()
                    ? ReflectionUtils.getConstructor(clazz$ClientBoundPlayerInfoUpdatePacket$Entry, UUID.class, clazz$GameProfile, boolean.class, int.class, clazz$GameType, clazz$Component, boolean.class, int.class, clazz$RemoteChatSession$Data)
                    : ReflectionUtils.getConstructor(clazz$ClientBoundPlayerInfoUpdatePacket$Entry, UUID.class, clazz$GameProfile, boolean.class, int.class, clazz$GameType, clazz$Component, int.class, clazz$RemoteChatSession$Data)
                    : ReflectionUtils.getConstructor(clazz$ClientBoundPlayerInfoUpdatePacket$Entry, UUID.class, clazz$GameProfile, boolean.class, int.class, clazz$GameType, clazz$Component, clazz$RemoteChatSession$Data)
    );

    public static final Method method$GameType$values = requireNonNull(
            ReflectionUtils.getStaticMethod(
                    clazz$GameType, clazz$GameType.arrayType()
            )
    );

    public static final Object instance$GameType$SURVIVAL;
    public static final Object instance$GameType$CREATIVE;
    public static final Object instance$GameType$ADVENTURE;
    public static final Object instance$GameType$SPECTATOR;

    static {
        try {
            Object[] values = (Object[]) method$GameType$values.invoke(null);
            instance$GameType$SURVIVAL = values[0];
            instance$GameType$CREATIVE = values[1];
            instance$GameType$ADVENTURE = values[2];
            instance$GameType$SPECTATOR = values[3];
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    // 1.21.3 +
    public static final Class<?> clazz$PositionMoveRotation = VersionHelper.isOrAbove1_21_3() ? ReflectionUtils.getClazz(
            requireNonNull(BukkitReflectionUtils.assembleMCClass("world.entity.PositionMoveRotation"))) : null;

    public static final Method method$PositionMoveRotation$of = VersionHelper.isOrAbove1_21_3() ? requireNonNull(
            ReflectionUtils.getStaticMethod(clazz$PositionMoveRotation, clazz$PositionMoveRotation, clazz$Entity)) : null;

    public static final Method method$Entity$absSnapTo = requireNonNull(
            ReflectionUtils.getMethod(
                    clazz$Entity, void.class, double.class, double.class, double.class, float.class, float.class
            )
    );

    public static final Class<?> clazz$ClientboundTeleportEntityPacket = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "network.protocol.game.PacketPlayOutEntityTeleport",
                    "network.protocol.game.ClientboundTeleportEntityPacket"
            )
    );

    public static final Constructor<?> constructor$ClientboundTeleportEntityPacket = requireNonNull(
            VersionHelper.isOrAbove1_21_3()
                    ? ReflectionUtils.getConstructor(clazz$ClientboundTeleportEntityPacket, int.class, clazz$PositionMoveRotation, Set.class, boolean.class)
                    : ReflectionUtils.getConstructor(clazz$ClientboundTeleportEntityPacket, clazz$Entity)
    );

    public static final Method method$Entity$setSharedFlag = requireNonNull(
            ReflectionUtils.getDeclaredMethod(
                    clazz$Entity, void.class, int.class, boolean.class
            )
    );

    public static final Method method$ClientboundSetEquipmentPacket$getEntity = requireNonNull(
            ReflectionUtils.getMethod(
                    NetworkReflections.clazz$ClientboundSetEquipmentPacket, int.class
            )
    );

    public static final Method method$ClientboundSetEquipmentPacket$getSlots = requireNonNull(
            ReflectionUtils.getMethod(
                    NetworkReflections.clazz$ClientboundSetEquipmentPacket, List.class
            )
    );

    public static final Class<?> clazz$ClientboundContainerSetSlotPacket = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "network.protocol.game.PacketPlayOutSetSlot",
                    "network.protocol.game.ClientboundContainerSetSlotPacket"
            )
    );

    public static final Constructor<?> constructor$ClientboundContainerSetSlotPacket = requireNonNull(
            ReflectionUtils.getConstructor(
                    clazz$ClientboundContainerSetSlotPacket, int.class, int.class, int.class, clazz$ItemStack
            )
    );

    public static final Method method$ClientboundContainerSetSlotPacket$getContainerId = requireNonNull(
            ReflectionUtils.getMethod(
                    clazz$ClientboundContainerSetSlotPacket, int.class, new String[]{"a", "getContainerId"}
            )
    );

    public static final Method method$ClientboundContainerSetSlotPacket$getSlot = requireNonNull(
            ReflectionUtils.getMethod(
                    clazz$ClientboundContainerSetSlotPacket, int.class, new String[]{"c", "d", "getSlot"}
            )
    );

    public static final Method method$ClientboundContainerSetSlotPacket$getStateId = requireNonNull(
            ReflectionUtils.getMethod(
                    clazz$ClientboundContainerSetSlotPacket, int.class, new String[]{"e", "f", "getStateId"}
            )
    );

    public static final Class<?> clazz$ClientboundAnimatePacket = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "network.protocol.game.PacketPlayOutAnimation",
                    "network.protocol.game.ClientboundAnimatePacket")
    );

    public static final Constructor<?> constructor$ClientboundAnimatePacket = requireNonNull(
            ReflectionUtils.getConstructor(clazz$ClientboundAnimatePacket, clazz$Entity, int.class)
    );

    public static final Class<?> clazz$ClientboundRotateHeadPacket = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "network.protocol.game.PacketPlayOutEntityHeadRotation",
                    "network.protocol.game.ClientboundRotateHeadPacket"
            )
    );

    public static final Constructor<?> constructor$ClientboundRotateHeadPacket = requireNonNull(
            ReflectionUtils.getDeclaredConstructor(
                    clazz$ClientboundRotateHeadPacket, clazz$Entity, byte.class
            )
    );

    public static final Class<?> clazz$ClientboundBlockUpdatePacket = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "network.protocol.game.PacketPlayOutBlockChange",
                    "network.protocol.game.ClientboundBlockUpdatePacket"
            )
    );

    public static final Constructor<?> constructor$ClientboundBlockUpdatePacket = requireNonNull(
            ReflectionUtils.getConstructor(
                    clazz$ClientboundBlockUpdatePacket, clazz$BlockPos, clazz$BlockState
            )
    );

    public static final Class<?> clazz$ClientboundRemoveEntitiesPacket = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "network.protocol.game.PacketPlayOutEntityDestroy",
                    "network.protocol.game.ClientboundRemoveEntitiesPacket"
            )
    );

    public static final Constructor<?> constructor$ClientboundRemoveEntitiesPacket = requireNonNull(
            ReflectionUtils.getConstructor(clazz$ClientboundRemoveEntitiesPacket, int[].class)
    );

    public static final Class<?> clazz$ClientboundPlayerInfoUpdatePacket = requireNonNull(
            ReflectionUtils.getClazz(
                    BukkitReflectionUtils.assembleMCClass("network.protocol.game.ClientboundPlayerInfoUpdatePacket")
            )
    );

    public static final Field field$ClientboundPlayerInfoUpdatePacket$entries = requireNonNull(
            ReflectionUtils.getDeclaredField(
                    clazz$ClientboundPlayerInfoUpdatePacket, List.class, 0
            )
    );

    public static final Class<?> clazz$ClientboundPlayerInfoUpdatePacket$Action = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "network.protocol.game.ClientboundPlayerInfoUpdatePacket$a",
                    "network.protocol.game.ClientboundPlayerInfoUpdatePacket$Action"
            )
    );

    public static final Method method$ClientboundPlayerInfoUpdatePacket$Action$values = requireNonNull(
            ReflectionUtils.getStaticMethod(
                    clazz$ClientboundPlayerInfoUpdatePacket$Action, clazz$ClientboundPlayerInfoUpdatePacket$Action.arrayType()
            )
    );

    public static final Object instance$ClientboundPlayerInfoUpdatePacket$Action$ADD_PLAYER;
    public static final Object instance$ClientboundPlayerInfoUpdatePacket$Action$INITIALIZE_CHAT;
    public static final Object instance$ClientboundPlayerInfoUpdatePacket$Action$UPDATE_GAME_MODE;
    public static final Object instance$ClientboundPlayerInfoUpdatePacket$Action$UPDATE_LISTED;
    public static final Object instance$ClientboundPlayerInfoUpdatePacket$Action$UPDATE_LATENCY;
    public static final Object instance$ClientboundPlayerInfoUpdatePacket$Action$UPDATE_DISPLAY_NAME;
    //public static final Object instance$ClientboundPlayerInfoUpdatePacket$Action$UPDATE_LIST_ORDER;
    //public static final Object instance$ClientboundPlayerInfoUpdatePacket$Action$UPDATE_HAT;


    static {
        try {
            Object[] values = (Object[]) method$ClientboundPlayerInfoUpdatePacket$Action$values.invoke(null);
            instance$ClientboundPlayerInfoUpdatePacket$Action$ADD_PLAYER = values[0];
            instance$ClientboundPlayerInfoUpdatePacket$Action$INITIALIZE_CHAT = values[1];
            instance$ClientboundPlayerInfoUpdatePacket$Action$UPDATE_GAME_MODE = values[2];
            instance$ClientboundPlayerInfoUpdatePacket$Action$UPDATE_LISTED = values[3];
            instance$ClientboundPlayerInfoUpdatePacket$Action$UPDATE_LATENCY = values[4];
            instance$ClientboundPlayerInfoUpdatePacket$Action$UPDATE_DISPLAY_NAME = values[5];
            //1.21.3
            //instance$ClientboundPlayerInfoUpdatePacket$Action$UPDATE_LIST_ORDER = values[6];
            //1.21.4
            //instance$ClientboundPlayerInfoUpdatePacket$Action$UPDATE_HAT = values[7];
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }
}
