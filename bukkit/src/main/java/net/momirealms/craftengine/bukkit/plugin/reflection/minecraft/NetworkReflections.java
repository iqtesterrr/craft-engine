package net.momirealms.craftengine.bukkit.plugin.reflection.minecraft;

import io.netty.buffer.ByteBuf;
import net.momirealms.craftengine.bukkit.util.BukkitReflectionUtils;
import net.momirealms.craftengine.core.util.ReflectionUtils;
import net.momirealms.craftengine.core.util.VersionHelper;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.Instant;
import java.util.*;

import static java.util.Objects.requireNonNull;

public final class NetworkReflections {

    private NetworkReflections() {}

    public static final Class<?> clazz$Connection = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "network.NetworkManager",
                    "network.Connection"
            )
    );

    public static final Class<?> clazz$ClientboundSetActionBarTextPacket = requireNonNull(
            ReflectionUtils.getClazz(
                    BukkitReflectionUtils.assembleMCClass("network.protocol.game.ClientboundSetActionBarTextPacket")
            )
    );

    public static final Field field$ClientboundSetActionBarTextPacket$text = requireNonNull(
            ReflectionUtils.getDeclaredField(
                    clazz$ClientboundSetActionBarTextPacket, CoreReflections.clazz$Component, 0
            )
    );

    public static final Constructor<?> constructor$ClientboundSetActionBarTextPacket = requireNonNull(
            ReflectionUtils.getConstructor(
                    clazz$ClientboundSetActionBarTextPacket, CoreReflections.clazz$Component
            )
    );

    public static final Class<?> clazz$ClientboundSystemChatPacket = requireNonNull(
            ReflectionUtils.getClazz(
                    BukkitReflectionUtils.assembleMCClass("network.protocol.game.ClientboundSystemChatPacket")
            )
    );

    public static final Constructor<?> constructor$ClientboundSystemChatPacket = requireNonNull(
            ReflectionUtils.getConstructor(clazz$ClientboundSystemChatPacket, CoreReflections.clazz$Component, boolean.class)
    );

    public static final Field field$ClientboundSystemChatPacket$overlay = requireNonNull(
            ReflectionUtils.getDeclaredField(
                    clazz$ClientboundSystemChatPacket, boolean.class, 0
            )
    );

    public static final Field field$ClientboundSystemChatPacket$component =
            ReflectionUtils.getDeclaredField(
                    clazz$ClientboundSystemChatPacket, CoreReflections.clazz$Component, 0
            );

    public static final Field field$ClientboundSystemChatPacket$text =
            ReflectionUtils.getDeclaredField(
                    clazz$ClientboundSystemChatPacket, String.class, 0
            );

    public static final Class<?> clazz$ClientboundBossEventPacket = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "network.protocol.game.PacketPlayOutBoss",
                    "network.protocol.game.ClientboundBossEventPacket"
            )
    );

    public static final Class<?> clazz$ClientboundBossEventPacket$Operation = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "network.protocol.game.PacketPlayOutBoss$Action",
                    "network.protocol.game.ClientboundBossEventPacket$Operation"
            )
    );

    public static final Constructor<?> constructor$ClientboundBossEventPacket = requireNonNull(
            ReflectionUtils.getDeclaredConstructor(
                    clazz$ClientboundBossEventPacket,
                    UUID.class, clazz$ClientboundBossEventPacket$Operation
            )
    );

    public static final Class<?> clazz$ClientboundBossEventPacket$AddOperation = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "network.protocol.game.PacketPlayOutBoss$a",
                    "network.protocol.game.ClientboundBossEventPacket$AddOperation"
            )
    );


    public static final Field field$ClientboundBossEventPacket$AddOperation$name = requireNonNull(
            ReflectionUtils.getDeclaredField(clazz$ClientboundBossEventPacket$AddOperation, 0)
    );

    public static final Field field$ClientboundBossEventPacket$AddOperation$progress = requireNonNull(
            ReflectionUtils.getDeclaredField(clazz$ClientboundBossEventPacket$AddOperation, 1)
    );

    public static final Field field$ClientboundBossEventPacket$AddOperation$color = requireNonNull(
            ReflectionUtils.getDeclaredField(clazz$ClientboundBossEventPacket$AddOperation, 2)
    );

    public static final Field field$ClientboundBossEventPacket$AddOperation$overlay = requireNonNull(
            ReflectionUtils.getDeclaredField(clazz$ClientboundBossEventPacket$AddOperation, 3)
    );

    public static final Field field$ClientboundBossEventPacket$AddOperation$darkenScreen = requireNonNull(
            ReflectionUtils.getDeclaredField(clazz$ClientboundBossEventPacket$AddOperation, 4)
    );

    public static final Field field$ClientboundBossEventPacket$AddOperation$playMusic = requireNonNull(
            ReflectionUtils.getDeclaredField(clazz$ClientboundBossEventPacket$AddOperation, 5)
    );

    public static final Field field$ClientboundBossEventPacket$AddOperation$createWorldFog = requireNonNull(
            ReflectionUtils.getDeclaredField(clazz$ClientboundBossEventPacket$AddOperation, 6)
    );

    public static Object allocateAddOperationInstance() throws InstantiationException {
        return ReflectionUtils.UNSAFE.allocateInstance(clazz$ClientboundBossEventPacket$AddOperation);
    }

    public static final Class<?> clazz$ClientboundBossEventPacket$UpdateNameOperation = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "network.protocol.game.PacketPlayOutBoss$e",
                    "network.protocol.game.ClientboundBossEventPacket$UpdateNameOperation"
            )
    );

    public static final Constructor<?> constructor$ClientboundBossEventPacket$UpdateNameOperation = requireNonNull(
            ReflectionUtils.getDeclaredConstructor(
                    clazz$ClientboundBossEventPacket$UpdateNameOperation,
                    CoreReflections.clazz$Component
            )
    );

    public static final Class<?> clazz$ClientboundBundlePacket = requireNonNull(
            ReflectionUtils.getClazz(BukkitReflectionUtils.assembleMCClass("network.protocol.game.ClientboundBundlePacket"))
    );

    public static final Class<?> clazz$Packet = requireNonNull(
            ReflectionUtils.getClazz(BukkitReflectionUtils.assembleMCClass("network.protocol.Packet"))
    );

    public static final Class<?> clazz$PacketSendListener = requireNonNull(
            ReflectionUtils.getClazz(BukkitReflectionUtils.assembleMCClass("network.PacketSendListener"))
    );

    public static final Field field$BundlePacket$packets = requireNonNull(
            ReflectionUtils.getDeclaredField(clazz$ClientboundBundlePacket.getSuperclass(), Iterable.class, 0)
    );

    public static final Class<?> clazz$ClientboundAddEntityPacket = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "network.protocol.game.PacketPlayOutSpawnEntity",
                    "network.protocol.game.ClientboundAddEntityPacket"
            )
    );

    public static final Field field$ClientboundAddEntityPacket$type = requireNonNull(
            ReflectionUtils.getDeclaredField(clazz$ClientboundAddEntityPacket, CoreReflections.clazz$EntityType, 0)
    );

    public static final Class<?> clazz$ClientboundAddPlayerPacket = BukkitReflectionUtils.findReobfOrMojmapClass(
            "network.protocol.game.PacketPlayOutNamedEntitySpawn",
            "network.protocol.game.ClientboundAddPlayerPacket"
    );

    public static final Class<?> clazz$ClientboundRemoveEntitiesPacket = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "network.protocol.game.PacketPlayOutEntityDestroy",
                    "network.protocol.game.ClientboundRemoveEntitiesPacket"
            )
    );

    public static final Field field$ClientboundAddPlayerPacket$entityId = Optional.ofNullable(clazz$ClientboundAddPlayerPacket)
            .map(it -> ReflectionUtils.getDeclaredField(it, int.class, 0))
            .orElse(null);

    public static final Constructor<?> constructor$ClientboundRemoveEntitiesPacket = requireNonNull(
            ReflectionUtils.getConstructor(clazz$ClientboundRemoveEntitiesPacket, int[].class)
    );

    public static final Class<?> clazz$ClientboundSetPassengersPacket = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "network.protocol.game.PacketPlayOutMount",
                    "network.protocol.game.ClientboundSetPassengersPacket"
            )
    );

    public static final Field field$ClientboundSetPassengersPacket$vehicle = requireNonNull(
            ReflectionUtils.getInstanceDeclaredField(clazz$ClientboundSetPassengersPacket, 0)
    );

    public static final Field field$ClientboundSetPassengersPacket$passengers = requireNonNull(
            ReflectionUtils.getInstanceDeclaredField(clazz$ClientboundSetPassengersPacket, 1)
    );

    public static final Class<?> clazz$ClientboundSetEntityDataPacket = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "network.protocol.game.PacketPlayOutEntityMetadata",
                    "network.protocol.game.ClientboundSetEntityDataPacket"
            )
    );
    
    public static final Class<?> clazz$ClientboundUpdateAttributesPacket = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "network.protocol.game.PacketPlayOutUpdateAttributes",
                    "network.protocol.game.ClientboundUpdateAttributesPacket"
            )
    );

    public static final Constructor<?> constructor$ClientboundUpdateAttributesPacket0 = requireNonNull(
            ReflectionUtils.getConstructor(clazz$ClientboundUpdateAttributesPacket, 0)
    );

    public static final Constructor<?> constructor$ClientboundUpdateAttributesPacket1 = requireNonNull(
            ReflectionUtils.getConstructor(clazz$ClientboundUpdateAttributesPacket, 1)
    );

    public static final Field field$ClientboundUpdateAttributesPacket$id = requireNonNull(
            ReflectionUtils.getDeclaredField(clazz$ClientboundUpdateAttributesPacket, int.class, 0)
    );

    public static final Field field$ClientboundUpdateAttributesPacket$attributes = requireNonNull(
            ReflectionUtils.getDeclaredField(clazz$ClientboundUpdateAttributesPacket, List.class, 0)
    );

    public static final Class<?> clazz$ClientboundUpdateAttributesPacket$AttributeSnapshot = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "network.protocol.game.PacketPlayOutUpdateAttributes$AttributeSnapshot",
                    "network.protocol.game.ClientboundUpdateAttributesPacket$AttributeSnapshot"
            )
    );

    public static final Field field$ClientboundUpdateAttributesPacket$AttributeSnapshot$attribute =
            ReflectionUtils.getDeclaredField(clazz$ClientboundUpdateAttributesPacket$AttributeSnapshot, CoreReflections.clazz$Holder, 0);

    public static final Field field$ClientboundUpdateAttributesPacket$AttributeSnapshot$base =
            ReflectionUtils.getDeclaredField(clazz$ClientboundUpdateAttributesPacket$AttributeSnapshot, double.class, 0);

    public static final Field field$ClientboundUpdateAttributesPacket$AttributeSnapshot$modifiers =
            ReflectionUtils.getDeclaredField(clazz$ClientboundUpdateAttributesPacket$AttributeSnapshot, Collection.class, 0);

    public static final Constructor<?> constructor$ClientboundUpdateAttributesPacket$AttributeSnapshot = requireNonNull(
            VersionHelper.isOrAbove1_20_5() ?
                    ReflectionUtils.getConstructor(clazz$ClientboundUpdateAttributesPacket$AttributeSnapshot, CoreReflections.clazz$Holder, double.class, Collection.class) :
                    ReflectionUtils.getConstructor(clazz$ClientboundUpdateAttributesPacket$AttributeSnapshot, CoreReflections.clazz$Attribute, double.class, Collection.class)
    );

    public static final Class<?> clazz$ClientboundGameEventPacket = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "network.protocol.game.PacketPlayOutGameStateChange",
                    "network.protocol.game.ClientboundGameEventPacket"
            )
    );

    public static final Class<?> clazz$ClientboundGameEventPacket$Type = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "network.protocol.game.PacketPlayOutGameStateChange$a",
                    "network.protocol.game.ClientboundGameEventPacket$Type"
            )
    );

    public static final Field field$ClientboundGameEventPacket$event = requireNonNull(
            ReflectionUtils.getDeclaredField(clazz$ClientboundGameEventPacket, clazz$ClientboundGameEventPacket$Type, 0)
    );


    public static final Field field$ClientboundGameEventPacket$param = requireNonNull(
            ReflectionUtils.getDeclaredField(clazz$ClientboundGameEventPacket, float.class, 0)
    );

    public static final Field field$ClientboundGameEventPacket$Type$id = requireNonNull(
            ReflectionUtils.getDeclaredField(clazz$ClientboundGameEventPacket$Type, int.class, 0)
    );
    
    public static final Class<?> clazz$ClientboundSetPlayerTeamPacket = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "network.protocol.game.PacketPlayOutScoreboardTeam",
                    "network.protocol.game.ClientboundSetPlayerTeamPacket"
            )
    );

    public static final Field field$ClientboundSetPlayerTeamPacket$method = requireNonNull(
            ReflectionUtils.getInstanceDeclaredField(clazz$ClientboundSetPlayerTeamPacket, int.class, 0)
    );

    public static final Field field$ClientboundSetPlayerTeamPacket$players = requireNonNull(
            ReflectionUtils.getInstanceDeclaredField(clazz$ClientboundSetPlayerTeamPacket, Collection.class, 0)
    );

    public static final Field field$ClientboundSetPlayerTeamPacket$parameters = requireNonNull(
            ReflectionUtils.getInstanceDeclaredField(clazz$ClientboundSetPlayerTeamPacket, Optional.class, 0)
    );

    public static final Class<?> clazz$ClientboundSetPlayerTeamPacket$Parameters = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "network.protocol.game.PacketPlayOutScoreboardTeam$b",
                    "network.protocol.game.ClientboundSetPlayerTeamPacket$Parameters"
            )
    );

    public static final Field field$ClientboundSetPlayerTeamPacket$Parameters$nametagVisibility = requireNonNull(
            ReflectionUtils.getInstanceDeclaredField(clazz$ClientboundSetPlayerTeamPacket$Parameters, VersionHelper.isOrAbove1_21_5() ? CoreReflections.clazz$Team$Visibility : String.class, 0)
    );

    public static final Class<?> clazz$ClientboundBlockUpdatePacket = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "network.protocol.game.PacketPlayOutBlockChange",
                    "network.protocol.game.ClientboundBlockUpdatePacket"
            )
    );

    public static final Class<?> clazz$ClientboundSectionBlocksUpdatePacket = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "network.protocol.game.PacketPlayOutMultiBlockChange",
                    "network.protocol.game.ClientboundSectionBlocksUpdatePacket"
            )
    );

    public static final Field field$ClientboundSectionBlocksUpdatePacket$positions = requireNonNull(
            ReflectionUtils.getDeclaredField(clazz$ClientboundSectionBlocksUpdatePacket, short[].class, 0)
    );

    public static final Field field$ClientboundSectionBlocksUpdatePacket$states = requireNonNull(
            ReflectionUtils.getDeclaredField(clazz$ClientboundSectionBlocksUpdatePacket, CoreReflections.clazz$BlockState.arrayType(), 0)
    );

    public static final Field field$ClientboundSectionBlocksUpdatePacket$sectionPos = requireNonNull(
            ReflectionUtils.getDeclaredField(clazz$ClientboundSectionBlocksUpdatePacket, CoreReflections.clazz$SectionPos, 0)
    );

    public static final Field field$ClientboundBlockUpdatePacket$blockstate = requireNonNull(
            ReflectionUtils.getDeclaredField(clazz$ClientboundBlockUpdatePacket, CoreReflections.clazz$BlockState, 0)
    );

    public static final Field field$ClientboundBlockUpdatePacket$blockPos = requireNonNull(
            ReflectionUtils.getDeclaredField(clazz$ClientboundBlockUpdatePacket, CoreReflections.clazz$BlockPos, 0)
    );

    public static final Constructor<?> constructor$ClientboundBlockUpdatePacket = requireNonNull(
            ReflectionUtils.getConstructor(clazz$ClientboundBlockUpdatePacket, CoreReflections.clazz$BlockPos, CoreReflections.clazz$BlockState)
    );

    public static final Class<?> clazz$ClientboundLevelChunkWithLightPacket = requireNonNull(
            ReflectionUtils.getClazz(BukkitReflectionUtils.assembleMCClass("network.protocol.game.ClientboundLevelChunkWithLightPacket"))
    );

    public static final Class<?> clazz$ClientboundLevelChunkPacketData = requireNonNull(
            ReflectionUtils.getClazz(BukkitReflectionUtils.assembleMCClass("network.protocol.game.ClientboundLevelChunkPacketData"))
    );

    public static final Class<?> clazz$ClientboundPlayerInfoUpdatePacket = requireNonNull(
            ReflectionUtils.getClazz(BukkitReflectionUtils.assembleMCClass("network.protocol.game.ClientboundPlayerInfoUpdatePacket"))
    );

    public static final Field field$ClientboundPlayerInfoUpdatePacket$entries = requireNonNull(
            ReflectionUtils.getDeclaredField(clazz$ClientboundPlayerInfoUpdatePacket, List.class, 0)
    );

    public static final Class<?> clazz$ClientboundPlayerInfoUpdatePacket$Action = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "network.protocol.game.ClientboundPlayerInfoUpdatePacket$a",
                    "network.protocol.game.ClientboundPlayerInfoUpdatePacket$Action"
            )
    );

    public static final Method method$ClientboundPlayerInfoUpdatePacket$Action$values = requireNonNull(
            ReflectionUtils.getStaticMethod(clazz$ClientboundPlayerInfoUpdatePacket$Action, clazz$ClientboundPlayerInfoUpdatePacket$Action.arrayType())
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

    public static final Field field$ClientboundLevelChunkWithLightPacket$chunkData = requireNonNull(
            ReflectionUtils.getDeclaredField(clazz$ClientboundLevelChunkWithLightPacket, clazz$ClientboundLevelChunkPacketData, 0)
    );


    public static final Field field$ClientboundLevelChunkWithLightPacket$x = requireNonNull(
            ReflectionUtils.getDeclaredField(clazz$ClientboundLevelChunkWithLightPacket, int.class, 0)
    );

    public static final Field field$ClientboundLevelChunkWithLightPacket$z = requireNonNull(
            ReflectionUtils.getDeclaredField(clazz$ClientboundLevelChunkWithLightPacket, int.class, 1)
    );

    public static final Field field$ClientboundLevelChunkPacketData$buffer = requireNonNull(
            ReflectionUtils.getDeclaredField(clazz$ClientboundLevelChunkPacketData, byte[].class, 0)
    );

    public static final Class<?> clazz$ClientboundLevelParticlesPacket = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "network.protocol.game.PacketPlayOutWorldParticles",
                    "network.protocol.game.ClientboundLevelParticlesPacket"
            )
    );

    public static final Constructor<?> constructor$ClientboundLevelParticlesPacket = requireNonNull(
            VersionHelper.isOrAbove1_20_5() ?
                    ReflectionUtils.getDeclaredConstructor(clazz$ClientboundLevelParticlesPacket, CoreReflections.clazz$RegistryFriendlyByteBuf) :
                    ReflectionUtils.getConstructor(clazz$ClientboundLevelParticlesPacket, CoreReflections.clazz$FriendlyByteBuf)
    );

    public static final Field field$ClientboundLevelParticlesPacket$particle = requireNonNull(
            ReflectionUtils.getDeclaredField(clazz$ClientboundLevelParticlesPacket, CoreReflections.clazz$ParticleOptions, 0)
    );

    public static final Class<?> clazz$ClientboundLightUpdatePacket = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "network.protocol.game.PacketPlayOutLightUpdate",
                    "network.protocol.game.ClientboundLightUpdatePacket"
            )
    );

    public static final Constructor<?> constructor$ClientboundLightUpdatePacket = requireNonNull(
            ReflectionUtils.getConstructor(clazz$ClientboundLightUpdatePacket, CoreReflections.clazz$ChunkPos, CoreReflections.clazz$LevelLightEngine, BitSet.class, BitSet.class)
    );

    public static final Class<?> clazz$ServerboundPlayerActionPacket = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "network.protocol.game.PacketPlayInBlockDig",
                    "network.protocol.game.ServerboundPlayerActionPacket"
            )
    );

    public static final Class<?> clazz$ServerboundPlayerActionPacket$Action = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "network.protocol.game.PacketPlayInBlockDig$EnumPlayerDigType",
                    "network.protocol.game.ServerboundPlayerActionPacket$Action"
            )
    );

    public static final Field field$ServerboundPlayerActionPacket$pos = requireNonNull(
            ReflectionUtils.getDeclaredField(clazz$ServerboundPlayerActionPacket, CoreReflections.clazz$BlockPos, 0)
    );

    public static final Field field$ServerboundPlayerActionPacket$action = requireNonNull(
            ReflectionUtils.getDeclaredField(clazz$ServerboundPlayerActionPacket, clazz$ServerboundPlayerActionPacket$Action, 0)
    );

    public static final Method method$ServerboundPlayerActionPacket$Action$values = requireNonNull(
            ReflectionUtils.getStaticMethod(clazz$ServerboundPlayerActionPacket$Action, clazz$ServerboundPlayerActionPacket$Action.arrayType())
    );

    public static final Object instance$ServerboundPlayerActionPacket$Action$START_DESTROY_BLOCK;
    public static final Object instance$ServerboundPlayerActionPacket$Action$ABORT_DESTROY_BLOCK;
    public static final Object instance$ServerboundPlayerActionPacket$Action$STOP_DESTROY_BLOCK;

    static {
        try {
            Object[] values = (Object[]) method$ServerboundPlayerActionPacket$Action$values.invoke(null);
            instance$ServerboundPlayerActionPacket$Action$START_DESTROY_BLOCK = values[0];
            instance$ServerboundPlayerActionPacket$Action$ABORT_DESTROY_BLOCK = values[1];
            instance$ServerboundPlayerActionPacket$Action$STOP_DESTROY_BLOCK = values[2];
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    public static final Class<?> clazz$ClientboundBlockDestructionPacket = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "network.protocol.game.PacketPlayOutBlockBreakAnimation",
                    "network.protocol.game.ClientboundBlockDestructionPacket"
            )
    );

    public static final Class<?> clazz$ServerboundSwingPacket = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "network.protocol.game.PacketPlayInArmAnimation",
                    "network.protocol.game.ServerboundSwingPacket"
            )
    );

    public static final Field field$ServerboundSwingPacket$hand = requireNonNull(
            ReflectionUtils.getDeclaredField(clazz$ServerboundSwingPacket, CoreReflections.clazz$InteractionHand, 0)
    );

    public static final Class<?> clazz$ClientboundSetEquipmentPacket = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "network.protocol.game.PacketPlayOutEntityEquipment",
                    "network.protocol.game.ClientboundSetEquipmentPacket"
            )
    );

    public static final Constructor<?> constructor$ClientboundSetEquipmentPacket = requireNonNull(
            ReflectionUtils.getConstructor(clazz$ClientboundSetEquipmentPacket, int.class, List.class)
    );

    public static final Class<?> clazz$ClientboundEntityEventPacket = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "network.protocol.game.PacketPlayOutEntityStatus",
                    "network.protocol.game.ClientboundEntityEventPacket"
            )
    );

    public static final Field field$ClientboundEntityEventPacket$entityId = requireNonNull(
            ReflectionUtils.getDeclaredField(clazz$ClientboundEntityEventPacket, int.class, 0)
    );

    public static final Field field$ClientboundEntityEventPacket$eventId = requireNonNull(
            ReflectionUtils.getDeclaredField(clazz$ClientboundEntityEventPacket, byte.class, 0)
    );

    public static final Constructor<?> constructor$ClientboundEntityEventPacket = requireNonNull(
            ReflectionUtils.getConstructor(clazz$ClientboundEntityEventPacket, CoreReflections.clazz$Entity, byte.class)
    );

    public static final Class<?> clazz$ServerboundInteractPacket = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "network.protocol.game.PacketPlayInUseEntity",
                    "network.protocol.game.ServerboundInteractPacket"
            )
    );

    public static final Class<?> clazz$ServerboundInteractPacket$Action = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "network.protocol.game.PacketPlayInUseEntity$EnumEntityUseAction",
                    "network.protocol.game.ServerboundInteractPacket$Action"
            )
    );

    public static final Class<?> clazz$ServerboundInteractPacket$InteractionAction = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "network.protocol.game.PacketPlayInUseEntity$d",
                    "network.protocol.game.ServerboundInteractPacket$InteractionAction"
            )
    );

    public static final Field field$ServerboundInteractPacket$InteractionAction$hand = requireNonNull(
            ReflectionUtils.getDeclaredField(clazz$ServerboundInteractPacket$InteractionAction, CoreReflections.clazz$InteractionHand, 0)
    );

    public static final Class<?> clazz$ServerboundInteractPacket$InteractionAtLocationAction = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "network.protocol.game.PacketPlayInUseEntity$e",
                    "network.protocol.game.ServerboundInteractPacket$InteractionAtLocationAction"
            )
    );

    public static final Field field$ServerboundInteractPacket$InteractionAtLocationAction$hand = requireNonNull(
            ReflectionUtils.getDeclaredField(clazz$ServerboundInteractPacket$InteractionAtLocationAction, CoreReflections.clazz$InteractionHand, 0)
    );

    public static final Field field$ServerboundInteractPacket$InteractionAtLocationAction$location = requireNonNull(
            ReflectionUtils.getDeclaredField(clazz$ServerboundInteractPacket$InteractionAtLocationAction, CoreReflections.clazz$Vec3, 0)
    );

    public static final Class<?> clazz$ServerboundInteractPacket$ActionType = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "network.protocol.game.PacketPlayInUseEntity$b",
                    "network.protocol.game.ServerboundInteractPacket$ActionType"
            )
    );

    public static final Method method$ServerboundInteractPacket$ActionType$values = requireNonNull(
            ReflectionUtils.getStaticMethod(
                    clazz$ServerboundInteractPacket$ActionType, clazz$ServerboundInteractPacket$ActionType.arrayType()
            )
    );

    public static final Object instance$ServerboundInteractPacket$ActionType$INTERACT;
    public static final Object instance$ServerboundInteractPacket$ActionType$ATTACK;
    public static final Object instance$ServerboundInteractPacket$ActionType$INTERACT_AT;

    static {
        try {
            Object[] values = (Object[]) method$ServerboundInteractPacket$ActionType$values.invoke(null);
            instance$ServerboundInteractPacket$ActionType$INTERACT = values[0];
            instance$ServerboundInteractPacket$ActionType$ATTACK = values[1];
            instance$ServerboundInteractPacket$ActionType$INTERACT_AT = values[2];
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    public static final Field field$ServerboundInteractPacket$usingSecondaryAction = requireNonNull(
            ReflectionUtils.getInstanceDeclaredField(
                    clazz$ServerboundInteractPacket, boolean.class, 0
            )
    );

    public static final Field field$ServerboundInteractPacket$action = requireNonNull(
            ReflectionUtils.getInstanceDeclaredField(
                    clazz$ServerboundInteractPacket, clazz$ServerboundInteractPacket$Action, 0
            )
    );

    public static final Method method$ServerboundInteractPacket$Action$getType = requireNonNull(
            ReflectionUtils.getDeclaredMethod(
                    clazz$ServerboundInteractPacket$Action, clazz$ServerboundInteractPacket$ActionType
            )
    );

    public static final Class<?> clazz$ClientboundUpdateMobEffectPacket = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "network.protocol.game.PacketPlayOutEntityEffect",
                    "network.protocol.game.ClientboundUpdateMobEffectPacket"
            )
    );

    public static final Class<?> clazz$ClientboundRemoveMobEffectPacket = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "network.protocol.game.PacketPlayOutRemoveEntityEffect",
                    "network.protocol.game.ClientboundRemoveMobEffectPacket"
            )
    );

    public static Object allocateClientboundUpdateMobEffectPacketInstance() throws InstantiationException {
        return ReflectionUtils.UNSAFE.allocateInstance(clazz$ClientboundUpdateMobEffectPacket);
    }

    public static final Constructor<?> constructor$ClientboundRemoveMobEffectPacket = requireNonNull(
            ReflectionUtils.getConstructor(
                    clazz$ClientboundRemoveMobEffectPacket, 0
            )
    );

    public static final Constructor<?> constructor$ClientboundUpdateMobEffectPacket = requireNonNull(
            !VersionHelper.isOrAbove1_20_5() ?
                    ReflectionUtils.getConstructor(clazz$ClientboundUpdateMobEffectPacket, int.class, CoreReflections.clazz$MobEffectInstance) :
                    ReflectionUtils.getConstructor(clazz$ClientboundUpdateMobEffectPacket, int.class, CoreReflections.clazz$MobEffectInstance, boolean.class)
    );

    public static final Field field$ClientboundUpdateMobEffectPacket$entityId = requireNonNull(
            ReflectionUtils.getInstanceDeclaredField(clazz$ClientboundUpdateMobEffectPacket, int.class, 0)
    );

    public static final Field field$ClientboundUpdateMobEffectPacket$effect = requireNonNull(
            !VersionHelper.isOrAbove1_20_5() ?
                    ReflectionUtils.getInstanceDeclaredField(clazz$ClientboundUpdateMobEffectPacket, CoreReflections.clazz$MobEffect, 0) :
                    ReflectionUtils.getInstanceDeclaredField(clazz$ClientboundUpdateMobEffectPacket, CoreReflections.clazz$Holder, 0)
    );

    public static final Field field$ClientboundUpdateMobEffectPacket$amplifier = requireNonNull(
            !VersionHelper.isOrAbove1_20_5() ?
                    ReflectionUtils.getInstanceDeclaredField(clazz$ClientboundUpdateMobEffectPacket, byte.class, 0) :
                    ReflectionUtils.getInstanceDeclaredField(clazz$ClientboundUpdateMobEffectPacket, int.class, 1)
    );

    public static final Field field$ClientboundUpdateMobEffectPacket$duration = requireNonNull(
            !VersionHelper.isOrAbove1_20_5() ?
                    ReflectionUtils.getInstanceDeclaredField(clazz$ClientboundUpdateMobEffectPacket, int.class, 1) :
                    ReflectionUtils.getInstanceDeclaredField(clazz$ClientboundUpdateMobEffectPacket, int.class, 2)
    );

    public static final Field field$ClientboundUpdateMobEffectPacket$flags = requireNonNull(
            !VersionHelper.isOrAbove1_20_5() ?
                    ReflectionUtils.getInstanceDeclaredField(clazz$ClientboundUpdateMobEffectPacket, byte.class, 1) :
                    ReflectionUtils.getInstanceDeclaredField(clazz$ClientboundUpdateMobEffectPacket, byte.class, 0)
    );

    // 1.21.4+
    public static final Class<?> clazz$ServerboundPickItemFromBlockPacket =
            ReflectionUtils.getClazz(BukkitReflectionUtils.assembleMCClass("network.protocol.game.ServerboundPickItemFromBlockPacket"));

    public static final Field field$ServerboundPickItemFromBlockPacket$pos = Optional.ofNullable(clazz$ServerboundPickItemFromBlockPacket)
            .map(it -> ReflectionUtils.getDeclaredField(it, CoreReflections.clazz$BlockPos, 0))
            .orElse(null);

    public static final Class<?> clazz$ServerboundSetCreativeModeSlotPacket = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "network.protocol.game.PacketPlayInSetCreativeSlot",
                    "network.protocol.game.ServerboundSetCreativeModeSlotPacket"
            )
    );

    public static final Field field$ServerboundSetCreativeModeSlotPacket$slotNum = requireNonNull(
            VersionHelper.isOrAbove1_20_5() ?
                    ReflectionUtils.getDeclaredField(clazz$ServerboundSetCreativeModeSlotPacket, short.class, 0) :
                    ReflectionUtils.getDeclaredField(clazz$ServerboundSetCreativeModeSlotPacket, int.class, 0)
    );

    public static final Field field$ServerboundSetCreativeModeSlotPacket$itemStack = requireNonNull(
            ReflectionUtils.getDeclaredField(clazz$ServerboundSetCreativeModeSlotPacket, CoreReflections.clazz$ItemStack, 0)
    );

    public static final Class<?> clazz$ClientboundRespawnPacket = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "network.protocol.game.PacketPlayOutRespawn",
                    "network.protocol.game.ClientboundRespawnPacket"
            )
    );

    public static final Class<?> clazz$ClientboundLoginPacket = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "network.protocol.game.PacketPlayOutLogin",
                    "network.protocol.game.ClientboundLoginPacket"
            )
    );

    // 1.20
    public static final Field field$ClientboundRespawnPacket$dimension =
            ReflectionUtils.getDeclaredField(clazz$ClientboundRespawnPacket, CoreReflections.clazz$ResourceKey, 1);

    // 1.20
    public static final Field field$ClientboundLoginPacket$dimension =
            ReflectionUtils.getDeclaredField(clazz$ClientboundLoginPacket, CoreReflections.clazz$ResourceKey, 1);

    // 1.20.2+
    public static final Class<?> clazz$CommonPlayerSpawnInfo =
            ReflectionUtils.getClazz(BukkitReflectionUtils.assembleMCClass("network.protocol.game.CommonPlayerSpawnInfo"));

    // 1.20.2+
    public static final Field field$ClientboundRespawnPacket$commonPlayerSpawnInfo = Optional.ofNullable(clazz$CommonPlayerSpawnInfo)
            .map(it -> ReflectionUtils.getDeclaredField(clazz$ClientboundRespawnPacket, it, 0))
            .orElse(null);

    // 1.20.2+
    public static final Field field$CommonPlayerSpawnInfo$dimension = Optional.ofNullable(clazz$CommonPlayerSpawnInfo)
            .map(it -> {
                if (VersionHelper.isOrAbove1_20_5()) {
                    return ReflectionUtils.getDeclaredField(it, CoreReflections.clazz$ResourceKey, 0);
                } else {
                    return ReflectionUtils.getDeclaredField(it, CoreReflections.clazz$ResourceKey, 1);
                }
            })
            .orElse(null);

    // 1.20.2+
    public static final Field field$ClientboundLoginPacket$commonPlayerSpawnInfo = Optional.ofNullable(clazz$CommonPlayerSpawnInfo)
            .map(it -> ReflectionUtils.getDeclaredField(clazz$ClientboundLoginPacket, it, 0))
            .orElse(null);

    // 1.20-1.20.4
    public static final Method method$Packet$write =
            ReflectionUtils.getMethod(clazz$Packet, void.class, CoreReflections.clazz$FriendlyByteBuf);

    // 1.20.5+
    public static final Method method$ClientboundLevelParticlesPacket$write = Optional.ofNullable(CoreReflections.clazz$RegistryFriendlyByteBuf)
            .map(it -> ReflectionUtils.getDeclaredMethod(clazz$ClientboundLevelParticlesPacket, void.class, it))
            .orElse(null);

    // 1.21.2+
    public static final Class<?> clazz$ClientboundEntityPositionSyncPacket =
            ReflectionUtils.getClazz(BukkitReflectionUtils.assembleMCClass("network.protocol.game.ClientboundEntityPositionSyncPacket"));

    public static final Field field$ClientboundEntityPositionSyncPacket$id = Optional.ofNullable(clazz$ClientboundEntityPositionSyncPacket)
            .map(it -> ReflectionUtils.getInstanceDeclaredField(it, int.class, 0))
            .orElse(null);

    public static final Class<?> clazz$ClientboundMoveEntityPacket = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "network.protocol.game.PacketPlayOutEntity",
                    "network.protocol.game.ClientboundMoveEntityPacket"
            )
    );

    public static final Field field$ClientboundMoveEntityPacket$entityId = requireNonNull(
            ReflectionUtils.getInstanceDeclaredField(clazz$ClientboundMoveEntityPacket, int.class, 0)
    );

    public static final Class<?> clazz$ClientboundMoveEntityPacket$Pos = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "network.protocol.game.PacketPlayOutEntity$PacketPlayOutRelEntityMove",
                    "network.protocol.game.ClientboundMoveEntityPacket$Pos"
            )
    );

    public static final Class<?> clazz$ClientboundMoveEntityPacket$Rot = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "network.protocol.game.PacketPlayOutEntity$PacketPlayOutEntityLook",
                    "network.protocol.game.ClientboundMoveEntityPacket$Rot"
            )
    );

    public static final Constructor<?> constructor$ClientboundMoveEntityPacket$Rot = requireNonNull(
            ReflectionUtils.getConstructor(clazz$ClientboundMoveEntityPacket$Rot, int.class, byte.class, byte.class, boolean.class)
    );

    public static final Class<?> clazz$ServerboundUseItemOnPacket = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "network.protocol.game.PacketPlayInUseItem",
                    "network.protocol.game.ServerboundUseItemOnPacket"
            )
    );

    // 1.21.4+
    public static final Class<?> clazz$ServerboundPickItemFromEntityPacket =
            ReflectionUtils.getClazz(BukkitReflectionUtils.assembleMCClass("network.protocol.game.ServerboundPickItemFromEntityPacket"));

    public static final Field field$ServerboundPickItemFromEntityPacket$id = Optional.ofNullable(clazz$ServerboundPickItemFromEntityPacket)
            .map(it -> ReflectionUtils.getInstanceDeclaredField(it, int.class, 0))
            .orElse(null);

    public static final Class<?> clazz$ClientboundSoundPacket = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "network.protocol.game.PacketPlayOutNamedSoundEffect",
                    "network.protocol.game.ClientboundSoundPacket"
            )
    );

    public static final Constructor<?> constructor$ClientboundSoundPacket = requireNonNull(
            ReflectionUtils.getConstructor(clazz$ClientboundSoundPacket, CoreReflections.clazz$Holder, CoreReflections.clazz$SoundSource, double.class, double.class, double.class, float.class, float.class, long.class)
    );

    public static final Field field$ClientboundSoundPacket$sound = requireNonNull(
            ReflectionUtils.getInstanceDeclaredField(clazz$ClientboundSoundPacket, CoreReflections.clazz$Holder, 0)
    );

    public static final Field field$ClientboundSoundPacket$source = requireNonNull(
            ReflectionUtils.getInstanceDeclaredField(clazz$ClientboundSoundPacket, CoreReflections.clazz$SoundSource, 0)
    );

    public static final Field field$ClientboundSoundPacket$x = requireNonNull(
            ReflectionUtils.getInstanceDeclaredField(clazz$ClientboundSoundPacket, int.class, 0)
    );

    public static final Field field$ClientboundSoundPacket$y = requireNonNull(
            ReflectionUtils.getInstanceDeclaredField(clazz$ClientboundSoundPacket, int.class, 1)
    );

    public static final Field field$ClientboundSoundPacket$z = requireNonNull(
            ReflectionUtils.getInstanceDeclaredField(clazz$ClientboundSoundPacket, int.class, 2)
    );

    public static final Field field$ClientboundSoundPacket$volume = requireNonNull(
            ReflectionUtils.getInstanceDeclaredField(clazz$ClientboundSoundPacket, float.class, 0)
    );

    public static final Field field$ClientboundSoundPacket$pitch = requireNonNull(
            ReflectionUtils.getInstanceDeclaredField(clazz$ClientboundSoundPacket, float.class, 1)
    );

    public static final Field field$ClientboundSoundPacket$seed = requireNonNull(
            ReflectionUtils.getInstanceDeclaredField(clazz$ClientboundSoundPacket, long.class, 0)
    );

    public static final Class<?> clazz$ClientboundLevelEventPacket = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "network.protocol.game.PacketPlayOutWorldEvent",
                    "network.protocol.game.ClientboundLevelEventPacket"
            )
    );

    public static final Field field$ClientboundLevelEventPacket$eventId = requireNonNull(
            ReflectionUtils.getDeclaredField(clazz$ClientboundLevelEventPacket, int.class, 0)
    );

    public static final Field field$ClientboundLevelEventPacket$data = requireNonNull(
            ReflectionUtils.getDeclaredField(clazz$ClientboundLevelEventPacket, int.class, 1)
    );

    public static final Field field$ClientboundLevelEventPacket$global = requireNonNull(
            ReflectionUtils.getDeclaredField(clazz$ClientboundLevelEventPacket, boolean.class, 0)
    );

    public static final Class<?> clazz$ClientboundOpenScreenPacket = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "network.protocol.game.PacketPlayOutOpenWindow",
                    "network.protocol.game.ClientboundOpenScreenPacket"
            )
    );

    public static final Constructor<?> constructor$ClientboundOpenScreenPacket = requireNonNull(
            ReflectionUtils.getConstructor(clazz$ClientboundOpenScreenPacket, int.class, CoreReflections.clazz$MenuType, CoreReflections.clazz$Component)
    );

    public static final Class<?> clazz$ClientboundResourcePackPushPacket = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    List.of("network.protocol.game.PacketPlayOutResourcePackSend", "network.protocol.common.ClientboundResourcePackPacket", "network.protocol.common.ClientboundResourcePackPushPacket"),
                    List.of("network.protocol.common.ClientboundResourcePackPacket", "network.protocol.common.ClientboundResourcePackPushPacket")
            )
    );

    public static final Constructor<?> constructor$ClientboundResourcePackPushPacket = requireNonNull(
            VersionHelper.isOrAbove1_20_5() ?
                    ReflectionUtils.getConstructor(clazz$ClientboundResourcePackPushPacket, UUID.class, String.class, String.class, boolean.class, Optional.class) :
                    VersionHelper.isOrAbove1_20_3() ?
                            ReflectionUtils.getConstructor(clazz$ClientboundResourcePackPushPacket, UUID.class, String.class, String.class, boolean.class, CoreReflections.clazz$Component) :
                            ReflectionUtils.getConstructor(clazz$ClientboundResourcePackPushPacket, String.class, String.class, boolean.class, CoreReflections.clazz$Component)
    );

    public static final Class<?> clazz$ClientboundResourcePackPopPacket =
            ReflectionUtils.getClazz(BukkitReflectionUtils.assembleMCClass("network.protocol.common.ClientboundResourcePackPopPacket"));

    public static final Constructor<?> constructor$ClientboundResourcePackPopPacket = Optional.ofNullable(clazz$ClientboundResourcePackPopPacket)
            .map(it -> ReflectionUtils.getConstructor(it, Optional.class))
            .orElse(null);

    public static final Class<?> clazz$MessageSignature = requireNonNull(
            ReflectionUtils.getClazz(BukkitReflectionUtils.assembleMCClass("network.chat.MessageSignature"))
    );

    public static final Class<?> clazz$LastSeenMessages$Update = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "network.chat.LastSeenMessages$b",
                    "network.chat.LastSeenMessages$Update"
            )
    );

    public static final Class<?> clazz$ServerboundChatPacket = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "network.protocol.game.PacketPlayInChat",
                    "network.protocol.game.ServerboundChatPacket"
            )
    );

    public static final Constructor<?> constructor$ServerboundChatPacket = requireNonNull(
            ReflectionUtils.getConstructor(clazz$ServerboundChatPacket, String.class, Instant.class, long.class, clazz$MessageSignature, clazz$LastSeenMessages$Update)
    );

    public static final Field field$ServerboundChatPacket$message = requireNonNull(
            ReflectionUtils.getDeclaredField(clazz$ServerboundChatPacket, String.class, 0)
    );

    public static final Field field$ServerboundChatPacket$timeStamp = requireNonNull(
            ReflectionUtils.getDeclaredField(clazz$ServerboundChatPacket, Instant.class, 0)
    );

    public static final Field field$ServerboundChatPacket$salt = requireNonNull(
            ReflectionUtils.getDeclaredField(clazz$ServerboundChatPacket, long.class, 0)
    );

    public static final Field field$ServerboundChatPacket$signature = requireNonNull(
            ReflectionUtils.getDeclaredField(clazz$ServerboundChatPacket, clazz$MessageSignature, 0)
    );

    public static final Field field$ServerboundChatPacket$lastSeenMessages = requireNonNull(
            ReflectionUtils.getDeclaredField(clazz$ServerboundChatPacket, clazz$LastSeenMessages$Update, 0)
    );

    public static final Class<?> clazz$ServerboundRenameItemPacket = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "network.protocol.game.PacketPlayInItemName",
                    "network.protocol.game.ServerboundRenameItemPacket"
            )
    );

    public static final Field field$ServerboundRenameItemPacket$name = requireNonNull(
            ReflectionUtils.getDeclaredField(clazz$ServerboundRenameItemPacket, String.class, 0)
    );

    public static final Class<?> clazz$ServerboundSignUpdatePacket = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "network.protocol.game.PacketPlayInUpdateSign",
                    "network.protocol.game.ServerboundSignUpdatePacket"
            )
    );

    public static final Field field$ServerboundSignUpdatePacket$lines = requireNonNull(
            ReflectionUtils.getDeclaredField(clazz$ServerboundSignUpdatePacket, String[].class, 0)
    );

    public static final Class<?> clazz$ServerboundEditBookPacket = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "network.protocol.game.PacketPlayInBEdit",
                    "network.protocol.game.ServerboundEditBookPacket"
            )
    );

    public static final Field field$ServerboundEditBookPacket$slot = requireNonNull(
            ReflectionUtils.getDeclaredField(clazz$ServerboundEditBookPacket, int.class, 0)
    );

    public static final Field field$ServerboundEditBookPacket$pages = requireNonNull(
            ReflectionUtils.getDeclaredField(clazz$ServerboundEditBookPacket, List.class, 0)
    );

    public static final Field field$ServerboundEditBookPacket$title = requireNonNull(
            ReflectionUtils.getDeclaredField(clazz$ServerboundEditBookPacket, Optional.class, 0)
    );

    public static final Constructor<?> constructor$ServerboundEditBookPacket = requireNonNull(
            ReflectionUtils.getConstructor(clazz$ServerboundEditBookPacket, int.class, List.class, Optional.class)
    );

    public static final Constructor<?> constructor$ClientboundLevelChunkWithLightPacket = requireNonNull(
            ReflectionUtils.getConstructor(clazz$ClientboundLevelChunkWithLightPacket, CoreReflections.clazz$LevelChunk, CoreReflections.clazz$LevelLightEngine, BitSet.class, BitSet.class)
    );

    public static final Class<?> clazz$ServerboundCustomPayloadPacket = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    List.of("network.protocol.game.PacketPlayInCustomPayload", "network.protocol.common.ServerboundCustomPayloadPacket"),
                    List.of("network.protocol.game.ServerboundCustomPayloadPacket", "network.protocol.common.ServerboundCustomPayloadPacket")
            )
    );

    // 1.20.2+
    public static final Class<?> clazz$CustomPacketPayload =
            ReflectionUtils.getClazz(BukkitReflectionUtils.assembleMCClass("network.protocol.common.custom.CustomPacketPayload"));

    // 1.20.5+
    public static final Class<?> clazz$CustomPacketPayload$Type = BukkitReflectionUtils.findReobfOrMojmapClass(
            "network.protocol.common.custom.CustomPacketPayload$b",
            "network.protocol.common.custom.CustomPacketPayload$Type"
    );

    // 1.20.2+
    public static final Field field$ServerboundCustomPayloadPacket$payload = Optional.ofNullable(clazz$CustomPacketPayload)
            .map(it -> ReflectionUtils.getDeclaredField(clazz$ServerboundCustomPayloadPacket, it, 0))
            .orElse(null);

    // 1.20.2+
    public static final Class<?> clazz$DiscardedPayload =
            ReflectionUtils.getClazz(BukkitReflectionUtils.assembleMCClass("network.protocol.common.custom.DiscardedPayload"));


    // 1.20.5+
    public static final Method method$CustomPacketPayload$type = Optional.ofNullable(clazz$CustomPacketPayload$Type)
            .map(it -> ReflectionUtils.getMethod(clazz$CustomPacketPayload, it))
            .orElse(null);

    // 1.20.5+
    public static final Method method$CustomPacketPayload$Type$id = Optional.ofNullable(clazz$CustomPacketPayload$Type)
            .map(it -> ReflectionUtils.getMethod(it, CoreReflections.clazz$ResourceLocation))
            .orElse(null);

    // 1.20.5~1.21.4#221
    public static final Method method$DiscardedPayload$data = Optional.ofNullable(clazz$DiscardedPayload)
            .map(it -> ReflectionUtils.getMethod(it, ByteBuf.class))
            .orElse(null);

    // 1.21.4#222+
    public static final Method method$DiscardedPayload$dataByteArray = Optional.ofNullable(method$DiscardedPayload$data)
            .map(m -> (Method) null)
            .orElseGet(() -> Optional.ofNullable(clazz$DiscardedPayload)
                    .map(clazz -> ReflectionUtils.getMethod(clazz, byte[].class))
                    .orElse(null)
            );

    public static final Class<?> clazz$ClientboundDisconnectPacket = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    List.of("network.protocol.game.PacketPlayOutKickDisconnect", "network.protocol.common.ClientboundDisconnectPacket"),
                    List.of("network.protocol.game.ClientboundDisconnectPacket", "network.protocol.common.ClientboundDisconnectPacket")
            )
    );

    public static final Constructor<?> constructor$ClientboundDisconnectPacket = requireNonNull(
            ReflectionUtils.getConstructor(clazz$ClientboundDisconnectPacket, CoreReflections.clazz$Component)
    );

    public static final Class<?> clazz$ClientboundMoveEntityPacket$PosRot = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "network.protocol.game.PacketPlayOutEntity$PacketPlayOutRelEntityMoveLook",
                    "network.protocol.game.ClientboundMoveEntityPacket$PosRot"
            )
    );


    public static final Constructor<?> constructor$ClientboundMoveEntityPacket$PosRot = requireNonNull(
            ReflectionUtils.getTheOnlyConstructor(clazz$ClientboundMoveEntityPacket$PosRot)
    );

    public static final Class<?> clazz$ClientboundRotateHeadPacket = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "network.protocol.game.PacketPlayOutEntityHeadRotation",
                    "network.protocol.game.ClientboundRotateHeadPacket"
            )
    );

    public static final Field field$ClientboundRotateHeadPacket$entityId = requireNonNull(
            ReflectionUtils.getDeclaredField(clazz$ClientboundRotateHeadPacket, int.class, 0)
    );

    public static final Class<?> clazz$ClientboundSetEntityMotionPacket = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "network.protocol.game.PacketPlayOutEntityVelocity",
                    "network.protocol.game.ClientboundSetEntityMotionPacket"
            )
    );

    public static final Field field$ClientboundSetEntityMotionPacket$id = requireNonNull(
            ReflectionUtils.getDeclaredField(clazz$ClientboundSetEntityMotionPacket, int.class, 0)
    );

    public static final Constructor<?> constructor$ClientboundMoveEntityPacket$Pos = requireNonNull(
            ReflectionUtils.getDeclaredConstructor(clazz$ClientboundMoveEntityPacket$Pos, int.class, short.class, short.class, short.class, boolean.class)
    );

    // 1.20.2+
    public static final Class<?> clazz$ServerboundClientInformationPacket =
            ReflectionUtils.getClazz(BukkitReflectionUtils.assembleMCClass("network.protocol.common.ServerboundClientInformationPacket"));

    // 1.20.2+
    public static final Constructor<?> constructor$ServerboundClientInformationPacket = Optional.ofNullable(clazz$ServerboundClientInformationPacket)
            .map(it -> ReflectionUtils.getConstructor(it, 1))
            .orElse(null);

    // 1.20.2+
    public static final Field field$ServerboundClientInformationPacket$information = Optional.ofNullable(clazz$ServerboundClientInformationPacket)
            .map(it -> ReflectionUtils.getDeclaredField(it, 0))
            .orElse(null);


    public static final Class<?> clazz$ClientboundSetTitleTextPacket = requireNonNull(
            ReflectionUtils.getClazz(
                    BukkitReflectionUtils.assembleMCClass("network.protocol.game.ClientboundSetTitleTextPacket")
            )
    );

    public static final Class<?> clazz$ClientboundSetSubtitleTextPacket = requireNonNull(
            ReflectionUtils.getClazz(
                    BukkitReflectionUtils.assembleMCClass("network.protocol.game.ClientboundSetSubtitleTextPacket")
            )
    );

    public static final Class<?> clazz$ClientboundTabListPacket = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "network.protocol.game.PacketPlayOutPlayerListHeaderFooter",
                    "network.protocol.game.ClientboundTabListPacket"
            )
    );

    public static final Class<?> clazz$ClientboundSetObjectivePacket = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "network.protocol.game.PacketPlayOutScoreboardObjective",
                    "network.protocol.game.ClientboundSetObjectivePacket"
            )
    );

    public static final Class<?> clazz$ClientboundSetScorePacket = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "network.protocol.game.PacketPlayOutScoreboardScore",
                    "network.protocol.game.ClientboundSetScorePacket"
            )
    );

    public static final Class<?> clazz$ServerboundHelloPacket = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "network.protocol.login.PacketLoginInStart",
                    "network.protocol.login.ServerboundHelloPacket"
            )
    );

    public static final Field field$ServerboundHelloPacket$name = requireNonNull(
            ReflectionUtils.getDeclaredField(
                    clazz$ServerboundHelloPacket, String.class, 0
            )
    );

    public static final Field field$ServerboundHelloPacket$uuid = requireNonNull(
            VersionHelper.isOrAbove1_20_2() ?
                    ReflectionUtils.getDeclaredField(clazz$ServerboundHelloPacket, UUID.class, 0) :
                    ReflectionUtils.getDeclaredField(clazz$ServerboundHelloPacket, Optional.class, 0)
    );

    public static final Field field$ClientboundResourcePackPushPacket$id =
            ReflectionUtils.getDeclaredField(clazz$ClientboundResourcePackPushPacket, UUID.class, 0);

    public static final Field field$ClientboundResourcePackPushPacket$prompt = requireNonNull(
            ReflectionUtils.getDeclaredField(clazz$ClientboundResourcePackPushPacket, VersionHelper.isOrAbove1_20_5() ? Optional.class : CoreReflections.clazz$Component, 0)
    );


    public static final Class<?> clazz$ServerboundResourcePackPacket = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    List.of("network.protocol.game.PacketPlayInResourcePackStatus", "network.protocol.common.ServerboundResourcePackPacket"),
                    List.of("network.protocol.game.ServerboundResourcePackPacket", "network.protocol.common.ServerboundResourcePackPacket")
            )
    );

    public static final Class<?> clazz$ServerboundResourcePackPacket$Action = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    List.of("network.protocol.game.PacketPlayInResourcePackStatus$EnumResourcePackStatus", "network.protocol.common.ServerboundResourcePackPacket$a"),
                    List.of("network.protocol.game.ServerboundResourcePackPacket$Action", "network.protocol.common.ServerboundResourcePackPacket$Action")
            )
    );

    public static final Method method$ServerboundResourcePackPacket$Action$values = requireNonNull(
            ReflectionUtils.getStaticMethod(
                    clazz$ServerboundResourcePackPacket$Action, clazz$ServerboundResourcePackPacket$Action.arrayType()
            )
    );

    public static final Object instance$ServerboundResourcePackPacket$Action$SUCCESSFULLY_LOADED;
    public static final Object instance$ServerboundResourcePackPacket$Action$DECLINED;
    public static final Object instance$ServerboundResourcePackPacket$Action$FAILED_DOWNLOAD;
    public static final Object instance$ServerboundResourcePackPacket$Action$ACCEPTED;
    public static final Object instance$ServerboundResourcePackPacket$Action$DOWNLOADED;
    public static final Object instance$ServerboundResourcePackPacket$Action$INVALID_URL;
    public static final Object instance$ServerboundResourcePackPacket$Action$FAILED_RELOAD;
    public static final Object instance$ServerboundResourcePackPacket$Action$DISCARDED;

    static {
        try {
            Object[] values = (Object[]) method$ServerboundResourcePackPacket$Action$values.invoke(null);
            instance$ServerboundResourcePackPacket$Action$SUCCESSFULLY_LOADED = values[0];
            instance$ServerboundResourcePackPacket$Action$DECLINED = values[1];
            instance$ServerboundResourcePackPacket$Action$FAILED_DOWNLOAD = values[2];
            instance$ServerboundResourcePackPacket$Action$ACCEPTED = values[3];
            if (VersionHelper.isOrAbove1_20_3()) {
                instance$ServerboundResourcePackPacket$Action$DOWNLOADED = values[4];
                instance$ServerboundResourcePackPacket$Action$INVALID_URL = values[5];
                instance$ServerboundResourcePackPacket$Action$FAILED_RELOAD = values[6];
                instance$ServerboundResourcePackPacket$Action$DISCARDED = values[7];
            } else {
                instance$ServerboundResourcePackPacket$Action$DOWNLOADED = null;
                instance$ServerboundResourcePackPacket$Action$INVALID_URL = null;
                instance$ServerboundResourcePackPacket$Action$FAILED_RELOAD = null;
                instance$ServerboundResourcePackPacket$Action$DISCARDED = null;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static final Constructor<?> constructor$ServerboundResourcePackPacket = requireNonNull(
            field$ClientboundResourcePackPushPacket$id != null
                    ? ReflectionUtils.getConstructor(clazz$ServerboundResourcePackPacket, UUID.class, clazz$ServerboundResourcePackPacket$Action)
                    : ReflectionUtils.getConstructor(clazz$ServerboundResourcePackPacket, clazz$ServerboundResourcePackPacket$Action)
    );

    public static final Class<?> clazz$ClientIntentionPacket = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "network.protocol.handshake.PacketHandshakingInSetProtocol",
                    "network.protocol.handshake.ClientIntentionPacket"
            )
    );

    public static final Field field$ClientIntentionPacket$protocolVersion = requireNonNull(
            ReflectionUtils.getDeclaredField(clazz$ClientIntentionPacket, int.class, VersionHelper.isOrAbove1_20_2() ? 0 : 1)
    );

    // 1.20.2+
    public static final Class<?> clazz$ServerboundLoginAcknowledgedPacket =
            ReflectionUtils.getClazz(BukkitReflectionUtils.assembleMCClass("network.protocol.login.ServerboundLoginAcknowledgedPacket"));

    public static final Field field$ServerboundResourcePackPacket$action = requireNonNull(
            ReflectionUtils.getDeclaredField(clazz$ServerboundResourcePackPacket, clazz$ServerboundResourcePackPacket$Action, 0)
    );

    public static final Class<?> clazz$ClientboundCustomPayloadPacket = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    List.of("network.protocol.game.PacketPlayOutCustomPayload", "network.protocol.common.ClientboundCustomPayloadPacket"),
                    List.of("network.protocol.game.ClientboundCustomPayloadPacket", "network.protocol.common.ClientboundCustomPayloadPacket")
            )
    );

    public static final Constructor<?> constructor$ClientboundCustomPayloadPacket = requireNonNull(
            ReflectionUtils.getConstructor(clazz$ClientboundCustomPayloadPacket, 0)
    );

    // 1.20.2+
    public static final Constructor<?> constructor$DiscardedPayload = Optional.ofNullable(clazz$DiscardedPayload)
            .map(ReflectionUtils::getTheOnlyConstructor)
            .orElse(null);

    public static final Class<?> clazz$ClientboundContainerSetContentPacket = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "network.protocol.game.PacketPlayOutWindowItems",
                    "network.protocol.game.ClientboundContainerSetContentPacket"
            )
    );

    public static final Class<?> clazz$ClientboundContainerSetSlotPacket = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "network.protocol.game.PacketPlayOutSetSlot",
                    "network.protocol.game.ClientboundContainerSetSlotPacket"
            )
    );

    public static final Constructor<?> constructor$ClientboundContainerSetSlotPacket = requireNonNull(
            ReflectionUtils.getConstructor(clazz$ClientboundContainerSetSlotPacket, int.class, int.class, int.class, CoreReflections.clazz$ItemStack)
    );

    // 1.21.2+
    public static final Class<?> clazz$ClientboundSetCursorItemPacket =
            ReflectionUtils.getClazz(BukkitReflectionUtils.assembleMCClass("network.protocol.game.ClientboundSetCursorItemPacket"));

    // 1.21.2+
    public static final Class<?> clazz$ClientboundSetPlayerInventoryPacket =
            ReflectionUtils.getClazz(BukkitReflectionUtils.assembleMCClass("network.protocol.game.ClientboundSetPlayerInventoryPacket"));

    public static final Class<?> clazz$ServerboundContainerClickPacket = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "network.protocol.game.PacketPlayInWindowClick",
                    "network.protocol.game.ServerboundContainerClickPacket"
            )
    );

    public static final Class<?> clazz$ClientboundTickingStatePacket =
            ReflectionUtils.getClazz(BukkitReflectionUtils.assembleMCClass("network.protocol.game.ClientboundTickingStatePacket"));

    public static final Constructor<?> constructor$ClientboundTickingStatePacket = Optional.ofNullable(clazz$ClientboundTickingStatePacket)
            .map(it -> ReflectionUtils.getConstructor(it, float.class, boolean.class))
            .orElse(null);

    public static final Class<?> clazz$ClientBoundPlayerInfoUpdatePacket$Entry = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "network.protocol.game.ClientboundPlayerInfoUpdatePacket$b",
                    "network.protocol.game.ClientboundPlayerInfoUpdatePacket$Entry"
            )
    );

    public static final Constructor<?> constructor$ClientBoundPlayerInfoUpdatePacket$Entry = requireNonNull(
            VersionHelper.isOrAbove1_21_3()
                    ? VersionHelper.isOrAbove1_21_4()
                    ? ReflectionUtils.getConstructor(clazz$ClientBoundPlayerInfoUpdatePacket$Entry, UUID.class, CoreReflections.clazz$GameProfile, boolean.class, int.class, CoreReflections.clazz$GameType, CoreReflections.clazz$Component, boolean.class, int.class, CoreReflections.clazz$RemoteChatSession$Data)
                    : ReflectionUtils.getConstructor(clazz$ClientBoundPlayerInfoUpdatePacket$Entry, UUID.class, CoreReflections.clazz$GameProfile, boolean.class, int.class, CoreReflections.clazz$GameType, CoreReflections.clazz$Component, int.class, CoreReflections.clazz$RemoteChatSession$Data)
                    : ReflectionUtils.getConstructor(clazz$ClientBoundPlayerInfoUpdatePacket$Entry, UUID.class, CoreReflections.clazz$GameProfile, boolean.class, int.class, CoreReflections.clazz$GameType, CoreReflections.clazz$Component, CoreReflections.clazz$RemoteChatSession$Data)
    );

    public static final Class<?> clazz$ClientboundTeleportEntityPacket = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "network.protocol.game.PacketPlayOutEntityTeleport",
                    "network.protocol.game.ClientboundTeleportEntityPacket"
            )
    );

    public static final Constructor<?> constructor$ClientboundTeleportEntityPacket = requireNonNull(
            VersionHelper.isOrAbove1_21_3()
                    ? ReflectionUtils.getConstructor(clazz$ClientboundTeleportEntityPacket, int.class, CoreReflections.clazz$PositionMoveRotation, Set.class, boolean.class)
                    : ReflectionUtils.getConstructor(clazz$ClientboundTeleportEntityPacket, CoreReflections.clazz$Entity)
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
            ReflectionUtils.getConstructor(clazz$ClientboundAnimatePacket, CoreReflections.clazz$Entity, int.class)
    );

    public static final Constructor<?> constructor$ClientboundRotateHeadPacket = requireNonNull(
            ReflectionUtils.getDeclaredConstructor(
                    clazz$ClientboundRotateHeadPacket, CoreReflections.clazz$Entity, byte.class
            )
    );
}
