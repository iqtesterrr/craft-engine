package net.momirealms.craftengine.bukkit.plugin.network;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Either;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntList;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslationArgument;
import net.momirealms.craftengine.bukkit.api.CraftEngineFurniture;
import net.momirealms.craftengine.bukkit.api.event.FurnitureBreakEvent;
import net.momirealms.craftengine.bukkit.api.event.FurnitureInteractEvent;
import net.momirealms.craftengine.bukkit.block.BukkitBlockManager;
import net.momirealms.craftengine.bukkit.entity.furniture.BukkitFurniture;
import net.momirealms.craftengine.bukkit.entity.furniture.BukkitFurnitureManager;
import net.momirealms.craftengine.bukkit.entity.projectile.BukkitProjectileManager;
import net.momirealms.craftengine.bukkit.item.BukkitItemManager;
import net.momirealms.craftengine.bukkit.item.behavior.FurnitureItemBehavior;
import net.momirealms.craftengine.bukkit.nms.FastNMS;
import net.momirealms.craftengine.bukkit.pack.BukkitPackManager;
import net.momirealms.craftengine.bukkit.plugin.BukkitCraftEngine;
import net.momirealms.craftengine.bukkit.plugin.injector.ProtectedFieldVisitor;
import net.momirealms.craftengine.bukkit.plugin.network.handler.*;
import net.momirealms.craftengine.bukkit.plugin.network.payload.DiscardedPayload;
import net.momirealms.craftengine.bukkit.plugin.network.payload.NetWorkDataTypes;
import net.momirealms.craftengine.bukkit.plugin.network.payload.Payload;
import net.momirealms.craftengine.bukkit.plugin.reflection.minecraft.CoreReflections;
import net.momirealms.craftengine.bukkit.plugin.reflection.minecraft.MBuiltInRegistries;
import net.momirealms.craftengine.bukkit.plugin.reflection.minecraft.MEntityTypes;
import net.momirealms.craftengine.bukkit.plugin.reflection.minecraft.NetworkReflections;
import net.momirealms.craftengine.bukkit.plugin.user.BukkitServerPlayer;
import net.momirealms.craftengine.bukkit.util.*;
import net.momirealms.craftengine.core.block.ImmutableBlockState;
import net.momirealms.craftengine.core.entity.player.InteractionHand;
import net.momirealms.craftengine.core.entity.seat.SeatEntity;
import net.momirealms.craftengine.core.font.FontManager;
import net.momirealms.craftengine.core.font.IllegalCharacterProcessResult;
import net.momirealms.craftengine.core.item.CustomItem;
import net.momirealms.craftengine.core.item.Item;
import net.momirealms.craftengine.core.item.behavior.ItemBehavior;
import net.momirealms.craftengine.core.item.context.UseOnContext;
import net.momirealms.craftengine.core.pack.host.ResourcePackDownloadData;
import net.momirealms.craftengine.core.pack.host.ResourcePackHost;
import net.momirealms.craftengine.core.plugin.CraftEngine;
import net.momirealms.craftengine.core.plugin.config.Config;
import net.momirealms.craftengine.core.plugin.context.ContextHolder;
import net.momirealms.craftengine.core.plugin.context.PlayerOptionalContext;
import net.momirealms.craftengine.core.plugin.context.event.EventTrigger;
import net.momirealms.craftengine.core.plugin.context.parameter.DirectContextParameters;
import net.momirealms.craftengine.core.plugin.network.*;
import net.momirealms.craftengine.core.util.*;
import net.momirealms.craftengine.core.world.BlockHitResult;
import net.momirealms.craftengine.core.world.BlockPos;
import net.momirealms.craftengine.core.world.EntityHitResult;
import net.momirealms.craftengine.core.world.WorldEvents;
import net.momirealms.craftengine.core.world.chunk.Palette;
import net.momirealms.craftengine.core.world.chunk.PalettedContainer;
import net.momirealms.craftengine.core.world.chunk.packet.BlockEntityData;
import net.momirealms.craftengine.core.world.chunk.packet.MCSection;
import net.momirealms.craftengine.core.world.collision.AABB;
import net.momirealms.sparrow.nbt.Tag;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.BiConsumer;

public class PacketConsumers {
    private static BukkitNetworkManager.Handlers[] ADD_ENTITY_HANDLERS;
    private static int[] mappings;
    private static int[] mappingsMOD;
    private static IntIdentityList BLOCK_LIST;
    private static IntIdentityList BIOME_LIST;

    public static void initEntities(int registrySize) {
        ADD_ENTITY_HANDLERS = new BukkitNetworkManager.Handlers[registrySize];
        Arrays.fill(ADD_ENTITY_HANDLERS, BukkitNetworkManager.Handlers.DO_NOTHING);
        ADD_ENTITY_HANDLERS[MEntityTypes.instance$EntityType$FALLING_BLOCK$registryId] = (user, event) -> {
            FriendlyByteBuf buf = event.getBuffer();
            int id = buf.readVarInt();
            UUID uuid = buf.readUUID();
            int type = buf.readVarInt();
            double x = buf.readDouble();
            double y = buf.readDouble();
            double z = buf.readDouble();
            byte xRot = buf.readByte();
            byte yRot = buf.readByte();
            byte yHeadRot = buf.readByte();
            int data = buf.readVarInt();
            // Falling blocks
            int remapped = remap(data);
            if (remapped != data) {
                int xa = buf.readShort();
                int ya = buf.readShort();
                int za = buf.readShort();
                event.setChanged(true);
                buf.clear();
                buf.writeVarInt(event.packetID());
                buf.writeVarInt(id);
                buf.writeUUID(uuid);
                buf.writeVarInt(type);
                buf.writeDouble(x);
                buf.writeDouble(y);
                buf.writeDouble(z);
                buf.writeByte(xRot);
                buf.writeByte(yRot);
                buf.writeByte(yHeadRot);
                buf.writeVarInt(remapped);
                buf.writeShort(xa);
                buf.writeShort(ya);
                buf.writeShort(za);
            }
        };

        ADD_ENTITY_HANDLERS[MEntityTypes.instance$EntityType$BLOCK_DISPLAY$registryId] = simpleAddEntityHandler(BlockDisplayPacketHandler.INSTANCE);
        ADD_ENTITY_HANDLERS[MEntityTypes.instance$EntityType$TEXT_DISPLAY$registryId] = simpleAddEntityHandler(TextDisplayPacketHandler.INSTANCE);
        ADD_ENTITY_HANDLERS[MEntityTypes.instance$EntityType$ARMOR_STAND$registryId] = simpleAddEntityHandler(ArmorStandPacketHandler.INSTANCE);
        ADD_ENTITY_HANDLERS[MEntityTypes.instance$EntityType$ITEM_DISPLAY$registryId] = simpleAddEntityHandler(ItemDisplayPacketHandler.INSTANCE);
        ADD_ENTITY_HANDLERS[MEntityTypes.instance$EntityType$ITEM$registryId] = simpleAddEntityHandler(CommonItemPacketHandler.INSTANCE);
        ADD_ENTITY_HANDLERS[MEntityTypes.instance$EntityType$ITEM_FRAME$registryId] = simpleAddEntityHandler(CommonItemPacketHandler.INSTANCE);
        ADD_ENTITY_HANDLERS[MEntityTypes.instance$EntityType$GLOW_ITEM_FRAME$registryId] = simpleAddEntityHandler(CommonItemPacketHandler.INSTANCE);
        ADD_ENTITY_HANDLERS[MEntityTypes.instance$EntityType$FIREBALL$registryId] = createOptionalCustomProjectileEntityHandler();
        ADD_ENTITY_HANDLERS[MEntityTypes.instance$EntityType$EYE_OF_ENDER$registryId] = createOptionalCustomProjectileEntityHandler();
        ADD_ENTITY_HANDLERS[MEntityTypes.instance$EntityType$FIREWORK_ROCKET$registryId] = createOptionalCustomProjectileEntityHandler();
        ADD_ENTITY_HANDLERS[MEntityTypes.instance$EntityType$SMALL_FIREBALL$registryId] = createOptionalCustomProjectileEntityHandler();
        ADD_ENTITY_HANDLERS[MEntityTypes.instance$EntityType$EGG$registryId] = createOptionalCustomProjectileEntityHandler();
        ADD_ENTITY_HANDLERS[MEntityTypes.instance$EntityType$ENDER_PEARL$registryId] = createOptionalCustomProjectileEntityHandler();
        ADD_ENTITY_HANDLERS[MEntityTypes.instance$EntityType$EXPERIENCE_BOTTLE$registryId] = createOptionalCustomProjectileEntityHandler();
        ADD_ENTITY_HANDLERS[MEntityTypes.instance$EntityType$SNOWBALL$registryId] = createOptionalCustomProjectileEntityHandler();
        ADD_ENTITY_HANDLERS[MEntityTypes.instance$EntityType$POTION$registryId] = createOptionalCustomProjectileEntityHandler();
        ADD_ENTITY_HANDLERS[MEntityTypes.instance$EntityType$TRIDENT$registryId] = createOptionalCustomProjectileEntityHandler();
        if (VersionHelper.isOrAbove1_20_5()) {
            ADD_ENTITY_HANDLERS[MEntityTypes.instance$EntityType$OMINOUS_ITEM_SPAWNER$registryId] = simpleAddEntityHandler(CommonItemPacketHandler.INSTANCE);
        }

        ADD_ENTITY_HANDLERS[MEntityTypes.instance$EntityType$ITEM_DISPLAY$registryId] = (user, event) -> {
            FriendlyByteBuf buf = event.getBuffer();
            int id = buf.readVarInt();
            BukkitFurniture furniture = BukkitFurnitureManager.instance().loadedFurnitureByRealEntityId(id);
            if (furniture != null) {
                user.entityPacketHandlers().put(id, new FurniturePacketHandler(furniture.fakeEntityIds()));
                user.sendPacket(furniture.spawnPacket((Player) user.platformPlayer()), false);
                if (Config.hideBaseEntity() && !furniture.hasExternalModel()) {
                    event.setCancelled(true);
                }
            }
        };
        ADD_ENTITY_HANDLERS[MEntityTypes.instance$EntityType$INTERACTION$registryId] = (user, event) -> {
            if (BukkitFurnitureManager.NMS_COLLISION_ENTITY_TYPE != MEntityTypes.instance$EntityType$INTERACTION) return;
            FriendlyByteBuf buf = event.getBuffer();
            int id = buf.readVarInt();
            // Cancel collider entity packet
            BukkitFurniture furniture = BukkitFurnitureManager.instance().loadedFurnitureByRealEntityId(id);
            if (furniture != null) {
                event.setCancelled(true);
                user.entityPacketHandlers().put(id, FurnitureCollisionPacketHandler.INSTANCE);
            }
        };
        ADD_ENTITY_HANDLERS[MEntityTypes.instance$EntityType$OAK_BOAT$registryId] = (user, event) -> {
            if (BukkitFurnitureManager.NMS_COLLISION_ENTITY_TYPE != MEntityTypes.instance$EntityType$OAK_BOAT) return;
            FriendlyByteBuf buf = event.getBuffer();
            int id = buf.readVarInt();
            // Cancel collider entity packet
            BukkitFurniture furniture = BukkitFurnitureManager.instance().loadedFurnitureByRealEntityId(id);
            if (furniture != null) {
                event.setCancelled(true);
                user.entityPacketHandlers().put(id, FurnitureCollisionPacketHandler.INSTANCE);
            }
        };
        ADD_ENTITY_HANDLERS[MEntityTypes.instance$EntityType$PLAYER$registryId] = (user, event) -> {
            FriendlyByteBuf buf = event.getBuffer();
            buf.readVarInt();
            UUID uuid = buf.readUUID();
            BukkitServerPlayer player = (BukkitServerPlayer) BukkitCraftEngine.instance().networkManager().getOnlineUser(uuid);
            if (player == null) return;
            SeatEntity seat = player.seat();
            if (seat == null) return;
            user.entityPacketHandlers().put(seat.playerID(), seat);
            seat.add(player, user);
        };
    }

    private static BukkitNetworkManager.Handlers simpleAddEntityHandler(EntityPacketHandler handler) {
        return (user, event) -> {
            FriendlyByteBuf buf = event.getBuffer();
            user.entityPacketHandlers().put(buf.readVarInt(), handler);
        };
    }

    private static BukkitNetworkManager.Handlers createOptionalCustomProjectileEntityHandler() {
        return (user, event) -> {
            FriendlyByteBuf buf = event.getBuffer();
            int id = buf.readVarInt();
            BukkitProjectileManager.instance().projectileByEntityId(id).ifPresentOrElse(customProjectile -> {
                ProjectilePacketHandler handler = new ProjectilePacketHandler(customProjectile, id);
                handler.convertAddCustomProjectilePacket(buf, event);
                user.entityPacketHandlers().put(id, handler);
            }, () -> {
                user.entityPacketHandlers().put(id, CommonItemPacketHandler.INSTANCE);
            });
        };
    }

    public static void initBlocks(Map<Integer, Integer> map, int registrySize) {
        mappings = new int[registrySize];
        for (int i = 0; i < registrySize; i++) {
            mappings[i] = i;
        }
        mappingsMOD = Arrays.copyOf(mappings, registrySize);
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            mappings[entry.getKey()] = entry.getValue();
            if (BlockStateUtils.isVanillaBlock(entry.getKey())) {
                mappingsMOD[entry.getKey()] = entry.getValue();
            }
        }
        for (int i = 0; i < mappingsMOD.length; i++) {
            if (BlockStateUtils.isVanillaBlock(i)) {
                mappingsMOD[i] = remap(i);
            }
        }
        BLOCK_LIST = new IntIdentityList(registrySize);
        BIOME_LIST = new IntIdentityList(RegistryUtils.currentBiomeRegistrySize());
    }

    public static int remap(int stateId) {
        return mappings[stateId];
    }

    public static int remapMOD(int stateId) {
        return mappingsMOD[stateId];
    }

    public static final BiConsumer<NetWorkUser, ByteBufPacketEvent> LEVEL_CHUNK_WITH_LIGHT = (user, event) -> {
        try {
            BukkitServerPlayer player = (BukkitServerPlayer) user;
            FriendlyByteBuf buf = event.getBuffer();
            int chunkX = buf.readInt();
            int chunkZ = buf.readInt();
            boolean named = !VersionHelper.isOrAbove1_20_2();
            // ClientboundLevelChunkPacketData
            int heightmapsCount = 0;
            Map<Integer, long[]> heightmapsMap = null;
            Tag heightmaps = null;
            if (VersionHelper.isOrAbove1_21_5()) {
                heightmapsMap = new HashMap<>();
                heightmapsCount = buf.readVarInt();
                for (int i = 0; i < heightmapsCount; i++) {
                    int key = buf.readVarInt();
                    long[] value = buf.readLongArray();
                    heightmapsMap.put(key, value);
                }
            } else {
                heightmaps = buf.readNbt(named);
            }

            int varInt = buf.readVarInt();
            byte[] buffer = new byte[varInt];
            buf.readBytes(buffer);
            int blockEntitiesDataCount = buf.readVarInt();
            List<BlockEntityData> blockEntitiesData = new ArrayList<>();
            for (int i = 0; i < blockEntitiesDataCount; i++) {
                byte packedXZ = buf.readByte();
                short y = buf.readShort();
                int type = buf.readVarInt();
                Tag tag = buf.readNbt(named);
                BlockEntityData blockEntityData = new BlockEntityData(packedXZ, y, type, tag);
                blockEntitiesData.add(blockEntityData);
            }
            // ClientboundLightUpdatePacketData
            BitSet skyYMask = buf.readBitSet();
            BitSet blockYMask = buf.readBitSet();
            BitSet emptySkyYMask = buf.readBitSet();
            BitSet emptyBlockYMask = buf.readBitSet();
            List<byte[]> skyUpdates = buf.readByteArrayList(2048);
            List<byte[]> blockUpdates = buf.readByteArrayList(2048);
            // 开始处理
            if (user.clientModEnabled()) {
                ByteBuf byteBuf = Unpooled.copiedBuffer(buffer);
                FriendlyByteBuf friendlyByteBuf = new FriendlyByteBuf(byteBuf);
                FriendlyByteBuf newBuf = new FriendlyByteBuf(Unpooled.buffer());
                for (int i = 0, count = player.clientSideSectionCount(); i < count; i++) {
                    try {
                        MCSection mcSection = new MCSection(BLOCK_LIST, BIOME_LIST);
                        mcSection.readPacket(friendlyByteBuf);
                        PalettedContainer<Integer> container = mcSection.blockStateContainer();
                        Palette<Integer> palette = container.data().palette();
                        if (palette.canRemap()) {
                            palette.remap(PacketConsumers::remapMOD);
                        } else {
                            for (int j = 0; j < 4096; j++) {
                                int state = container.get(j);
                                int newState = remapMOD(state);
                                if (newState != state) {
                                    container.set(j, newState);
                                }
                            }
                        }
                        mcSection.writePacket(newBuf);
                    } catch (Exception e) {
                        break;
                    }
                }
                buffer = newBuf.array();
            } else {
                ByteBuf byteBuf = Unpooled.copiedBuffer(buffer);
                FriendlyByteBuf friendlyByteBuf = new FriendlyByteBuf(byteBuf);
                FriendlyByteBuf newBuf = new FriendlyByteBuf(Unpooled.buffer());
                for (int i = 0, count = player.clientSideSectionCount(); i < count; i++) {
                    try {
                        MCSection mcSection = new MCSection(BLOCK_LIST, BIOME_LIST);
                        mcSection.readPacket(friendlyByteBuf);
                        PalettedContainer<Integer> container = mcSection.blockStateContainer();
                        Palette<Integer> palette = container.data().palette();
                        if (palette.canRemap()) {
                            palette.remap(PacketConsumers::remap);
                        } else {
                            for (int j = 0; j < 4096; j++) {
                                int state = container.get(j);
                                int newState = remap(state);
                                if (newState != state) {
                                    container.set(j, newState);
                                }
                            }
                        }
                        mcSection.writePacket(newBuf);
                    } catch (Exception e) {
                        break;
                    }
                }
                buffer = newBuf.array();
            }
            buf.clear();
            buf.writeVarInt(event.packetID());
            buf.writeInt(chunkX);
            buf.writeInt(chunkZ);
            if (VersionHelper.isOrAbove1_21_5()) {
                buf.writeVarInt(heightmapsCount);
                for (Map.Entry<Integer, long[]> entry : heightmapsMap.entrySet()) {
                    buf.writeVarInt(entry.getKey());
                    buf.writeLongArray(entry.getValue());
                }
            } else {
                buf.writeNbt(heightmaps, named);
            }
            buf.writeVarInt(buffer.length);
            buf.writeBytes(buffer);
            buf.writeVarInt(blockEntitiesDataCount);
            for (BlockEntityData blockEntityData : blockEntitiesData) {
                buf.writeByte(blockEntityData.packedXZ());
                buf.writeShort(blockEntityData.y());
                buf.writeVarInt(blockEntityData.type());
                buf.writeNbt(blockEntityData.tag(), named);
            }
            buf.writeBitSet(skyYMask);
            buf.writeBitSet(blockYMask);
            buf.writeBitSet(emptySkyYMask);
            buf.writeBitSet(emptyBlockYMask);
            buf.writeByteArrayList(skyUpdates);
            buf.writeByteArrayList(blockUpdates);
            event.setChanged(true);
        } catch (Exception e) {
            CraftEngine.instance().logger().warn("Failed to handle ClientboundLevelChunkWithLightPacket", e);
        }
    };

    public static final BiConsumer<NetWorkUser, ByteBufPacketEvent> SECTION_BLOCK_UPDATE = (user, event) -> {
        try {
            if (user.clientModEnabled()) {
                FriendlyByteBuf buf = event.getBuffer();
                long pos = buf.readLong();
                int blocks = buf.readVarInt();
                short[] positions = new short[blocks];
                int[] states = new int[blocks];
                for (int i = 0; i < blocks; i++) {
                    long k = buf.readVarLong();
                    positions[i] = (short) ((int) (k & 4095L));
                    states[i] = remapMOD((int) (k >>> 12));
                }
                buf.clear();
                buf.writeVarInt(event.packetID());
                buf.writeLong(pos);
                buf.writeVarInt(blocks);
                for (int i = 0; i < blocks; i++) {
                    buf.writeVarLong((long) states[i] << 12 | positions[i]);
                }
                event.setChanged(true);
            } else {
                FriendlyByteBuf buf = event.getBuffer();
                long pos = buf.readLong();
                int blocks = buf.readVarInt();
                short[] positions = new short[blocks];
                int[] states = new int[blocks];
                for (int i = 0; i < blocks; i++) {
                    long k = buf.readVarLong();
                    positions[i] = (short) ((int) (k & 4095L));
                    states[i] = remap((int) (k >>> 12));
                }
                buf.clear();
                buf.writeVarInt(event.packetID());
                buf.writeLong(pos);
                buf.writeVarInt(blocks);
                for (int i = 0; i < blocks; i++) {
                    buf.writeVarLong((long) states[i] << 12 | positions[i]);
                }
                event.setChanged(true);
            }
        } catch (Exception e) {
            CraftEngine.instance().logger().warn("Failed to handle ClientboundSectionBlocksUpdatePacket", e);
        }
    };

    public static final BiConsumer<NetWorkUser, ByteBufPacketEvent> BLOCK_UPDATE = (user, event) -> {
        try {
            FriendlyByteBuf buf = event.getBuffer();
            BlockPos pos = buf.readBlockPos();
            int before = buf.readVarInt();
            if (user.clientModEnabled() && !BlockStateUtils.isVanillaBlock(before)) {
                return;
            }
            int state = remap(before);
            if (state == before) {
                return;
            }
            event.setChanged(true);
            buf.clear();
            buf.writeVarInt(event.packetID());
            buf.writeBlockPos(pos);
            buf.writeVarInt(state);
        } catch (Exception e) {
            CraftEngine.instance().logger().warn("Failed to handle ClientboundBlockUpdatePacket", e);
        }
    };

    public static final BiConsumer<NetWorkUser, ByteBufPacketEvent> LEVEL_EVENT = (user, event) -> {
        try {
            FriendlyByteBuf buf = event.getBuffer();
            int eventId = buf.readInt();
            if (eventId != WorldEvents.BLOCK_BREAK_EFFECT) return;
            BlockPos blockPos = buf.readBlockPos();
            int state = buf.readInt();
            boolean global = buf.readBoolean();
            int newState = remap(state);
            if (newState == state) {
                return;
            }
            event.setChanged(true);
            buf.clear();
            buf.writeVarInt(event.packetID());
            buf.writeInt(eventId);
            buf.writeBlockPos(blockPos);
            buf.writeInt(newState);
            buf.writeBoolean(global);
        } catch (Exception e) {
            CraftEngine.instance().logger().warn("Failed to handle ClientboundLevelEventPacket", e);
        }
    };

    public static final BiConsumer<NetWorkUser, ByteBufPacketEvent> TEAM_1_20_3 = (user, event) -> {
        if (!Config.interceptTeam()) return;
        try {
            FriendlyByteBuf buf = event.getBuffer();
            String name = buf.readUtf();
            byte method = buf.readByte();
            if (method != 2 && method != 0) return;
            Tag displayName = buf.readNbt(false);
            if (displayName == null) return;
            byte friendlyFlags = buf.readByte();
            Either<String, Integer> eitherVisibility = VersionHelper.isOrAbove1_21_5() ? Either.right(buf.readVarInt()) : Either.left(buf.readUtf(40));
            Either<String, Integer> eitherCollisionRule = VersionHelper.isOrAbove1_21_5() ? Either.right(buf.readVarInt()) : Either.left(buf.readUtf(40));
            int color = buf.readVarInt();
            Tag prefix = buf.readNbt(false);
            if (prefix == null) return;
            Tag suffix = buf.readNbt(false);
            if (suffix == null) return;

            Map<String, Component> tokens1 = CraftEngine.instance().fontManager().matchTags(displayName.getAsString());
            Map<String, Component> tokens2 = CraftEngine.instance().fontManager().matchTags(prefix.getAsString());
            Map<String, Component> tokens3 = CraftEngine.instance().fontManager().matchTags(suffix.getAsString());
            if (tokens1.isEmpty() && tokens2.isEmpty() && tokens3.isEmpty()) return;
            List<String> entities = method == 0 ? buf.readStringList() : null;
            event.setChanged(true);
            buf.clear();
            buf.writeVarInt(event.packetID());
            buf.writeUtf(name);
            buf.writeByte(method);
            buf.writeNbt(tokens1.isEmpty() ? displayName : AdventureHelper.componentToTag(AdventureHelper.replaceText(AdventureHelper.tagToComponent(displayName), tokens1)), false);
            buf.writeByte(friendlyFlags);
            eitherVisibility.ifLeft(buf::writeUtf).ifRight(buf::writeVarInt);
            eitherCollisionRule.ifLeft(buf::writeUtf).ifRight(buf::writeVarInt);
            buf.writeVarInt(color);
            buf.writeNbt(tokens2.isEmpty() ? prefix : AdventureHelper.componentToTag(AdventureHelper.replaceText(AdventureHelper.tagToComponent(prefix), tokens2)), false);
            buf.writeNbt(tokens3.isEmpty() ? suffix : AdventureHelper.componentToTag(AdventureHelper.replaceText(AdventureHelper.tagToComponent(suffix), tokens3)), false);
            if (entities != null) {
                buf.writeStringList(entities);
            }
        } catch (Exception e) {
            CraftEngine.instance().logger().warn("Failed to handle ClientboundSetPlayerTeamPacket", e);
        }
    };

    public static final TriConsumer<NetWorkUser, NMSPacketEvent, Object> PLAYER_INFO_UPDATE = (user, event, packet) -> {
        try {
            if (!user.isOnline()) return;
            if (!Config.interceptPlayerInfo()) return;
            List<Object> entries = FastNMS.INSTANCE.field$ClientboundPlayerInfoUpdatePacket$entries(packet);
            if (entries instanceof MarkedArrayList) {
                return;
            }
            EnumSet<? extends Enum<?>> enums = FastNMS.INSTANCE.field$ClientboundPlayerInfoUpdatePacket$actions(packet);
            outer: {
                for (Object entry : enums) {
                    if (entry == NetworkReflections.instance$ClientboundPlayerInfoUpdatePacket$Action$UPDATE_DISPLAY_NAME) {
                        break outer;
                    }
                }
                return;
            }
            boolean isChanged = false;
            List<Object> newEntries = new MarkedArrayList<>();
            for (Object entry : entries) {
                Object mcComponent = FastNMS.INSTANCE.field$ClientboundPlayerInfoUpdatePacket$Entry$displayName(entry);
                if (mcComponent == null) {
                    newEntries.add(entry);
                } else {
                    String json = ComponentUtils.minecraftToJson(mcComponent);
                    Map<String, Component> tokens = CraftEngine.instance().fontManager().matchTags(json);
                    if (tokens.isEmpty()) {
                        newEntries.add(entry);
                    } else {
                        Object newEntry = FastNMS.INSTANCE.constructor$ClientboundPlayerInfoUpdatePacket$Entry(entry, ComponentUtils.adventureToMinecraft(AdventureHelper.replaceText(AdventureHelper.jsonToComponent(json), tokens)));
                        newEntries.add(newEntry);
                        isChanged = true;
                    }
                }
            }
            if (isChanged) {
                event.replacePacket(FastNMS.INSTANCE.constructor$ClientboundPlayerInfoUpdatePacket(enums, newEntries));
            }
        } catch (Exception e) {
            CraftEngine.instance().logger().warn("Failed to handle ClientboundPlayerInfoUpdatePacket", e);
        }
    };

    public static final BiConsumer<NetWorkUser, ByteBufPacketEvent> TEAM_1_20 = (user, event) -> {
        if (!Config.interceptTeam()) return;
        try {
            FriendlyByteBuf buf = event.getBuffer();
            String name = buf.readUtf();
            byte method = buf.readByte();
            if (method != 2 && method != 0)
                return;
            String displayName = buf.readUtf();
            byte friendlyFlags = buf.readByte();
            String nameTagVisibility = buf.readUtf(40);
            String collisionRule = buf.readUtf(40);
            int color = buf.readVarInt();
            String prefix = buf.readUtf();
            String suffix = buf.readUtf();

            Map<String, Component> tokens1 = CraftEngine.instance().fontManager().matchTags(displayName);
            Map<String, Component> tokens2 = CraftEngine.instance().fontManager().matchTags(prefix);
            Map<String, Component> tokens3 = CraftEngine.instance().fontManager().matchTags(suffix);
            if (tokens1.isEmpty() && tokens2.isEmpty() && tokens3.isEmpty()) return;
            event.setChanged(true);

            List<String> entities = method == 0 ? buf.readStringList() : null;
            buf.clear();
            buf.writeVarInt(event.packetID());
            buf.writeUtf(name);
            buf.writeByte(method);
            buf.writeUtf(tokens1.isEmpty() ? displayName : AdventureHelper.componentToJson(AdventureHelper.replaceText(AdventureHelper.jsonToComponent(displayName), tokens1)));
            buf.writeByte(friendlyFlags);
            buf.writeUtf(nameTagVisibility);
            buf.writeUtf(collisionRule);
            buf.writeVarInt(color);
            buf.writeUtf(tokens2.isEmpty() ? prefix : AdventureHelper.componentToJson(AdventureHelper.replaceText(AdventureHelper.jsonToComponent(prefix), tokens2)));
            buf.writeUtf(tokens3.isEmpty() ? suffix : AdventureHelper.componentToJson(AdventureHelper.replaceText(AdventureHelper.jsonToComponent(suffix), tokens3)));
            if (entities != null) {
                buf.writeStringList(entities);
            }
        } catch (Exception e) {
            CraftEngine.instance().logger().warn("Failed to handle ClientboundSetPlayerTeamPacket", e);
        }
    };

    public static final BiConsumer<NetWorkUser, ByteBufPacketEvent> BOSS_EVENT_1_20 = (user, event) -> {
        if (!Config.interceptBossBar()) return;
        try {
            FriendlyByteBuf buf = event.getBuffer();
            UUID uuid = buf.readUUID();
            int actionType = buf.readVarInt();
            if (actionType == 0) {
                String json = buf.readUtf();
                Map<String, Component> tokens = CraftEngine.instance().fontManager().matchTags(json);
                if (tokens.isEmpty()) return;
                float health = buf.readFloat();
                int color = buf.readVarInt();
                int division = buf.readVarInt();
                byte flag = buf.readByte();
                event.setChanged(true);
                buf.clear();
                buf.writeVarInt(event.packetID());
                buf.writeUUID(uuid);
                buf.writeVarInt(actionType);
                buf.writeUtf(AdventureHelper.componentToJson(AdventureHelper.replaceText(AdventureHelper.jsonToComponent(json), tokens)));
                buf.writeFloat(health);
                buf.writeVarInt(color);
                buf.writeVarInt(division);
                buf.writeByte(flag);
            } else if (actionType == 3) {
                String json = buf.readUtf();
                Map<String, Component> tokens = CraftEngine.instance().fontManager().matchTags(json);
                if (tokens.isEmpty()) return;
                event.setChanged(true);
                buf.clear();
                buf.writeVarInt(event.packetID());
                buf.writeUUID(uuid);
                buf.writeVarInt(actionType);
                buf.writeUtf(AdventureHelper.componentToJson(AdventureHelper.replaceText(AdventureHelper.jsonToComponent(json), tokens)));
            }
        } catch (Exception e) {
            CraftEngine.instance().logger().warn("Failed to handle ClientboundBossEventPacket", e);
        }
    };

    public static final BiConsumer<NetWorkUser, ByteBufPacketEvent> BOSS_EVENT_1_20_3 = (user, event) -> {
        if (!Config.interceptBossBar()) return;
        try {
            FriendlyByteBuf buf = event.getBuffer();
            UUID uuid = buf.readUUID();
            int actionType = buf.readVarInt();
            if (actionType == 0) {
                Tag nbt = buf.readNbt(false);
                if (nbt == null) return;
                Map<String, Component> tokens = CraftEngine.instance().fontManager().matchTags(nbt.getAsString());
                if (tokens.isEmpty()) return;
                float health = buf.readFloat();
                int color = buf.readVarInt();
                int division = buf.readVarInt();
                byte flag = buf.readByte();
                event.setChanged(true);
                buf.clear();
                buf.writeVarInt(event.packetID());
                buf.writeUUID(uuid);
                buf.writeVarInt(actionType);
                buf.writeNbt(AdventureHelper.componentToTag(AdventureHelper.replaceText(AdventureHelper.tagToComponent(nbt), tokens)), false);
                buf.writeFloat(health);
                buf.writeVarInt(color);
                buf.writeVarInt(division);
                buf.writeByte(flag);
            } else if (actionType == 3) {
                Tag nbt = buf.readNbt(false);
                if (nbt == null) return;
                Map<String, Component> tokens = CraftEngine.instance().fontManager().matchTags(nbt.getAsString());
                if (tokens.isEmpty()) return;
                event.setChanged(true);
                buf.clear();
                buf.writeVarInt(event.packetID());
                buf.writeUUID(uuid);
                buf.writeVarInt(actionType);
                buf.writeNbt(AdventureHelper.componentToTag(AdventureHelper.replaceText(AdventureHelper.tagToComponent(nbt), tokens)), false);
            }
        } catch (Exception e) {
            CraftEngine.instance().logger().warn("Failed to handle ClientboundBossEventPacket", e);
        }
    };

    public static final BiConsumer<NetWorkUser, ByteBufPacketEvent> SET_OBJECTIVE_1_20 = (user, event) -> {
        if (!Config.interceptScoreboard()) return;
        try {
            FriendlyByteBuf buf = event.getBuffer();
            String objective = buf.readUtf();
            byte mode = buf.readByte();
            if (mode != 0 && mode != 2) return;
            String displayName = buf.readUtf();
            int renderType = buf.readVarInt();
            Map<String, Component> tokens = CraftEngine.instance().fontManager().matchTags(displayName);
            if (tokens.isEmpty()) return;
            event.setChanged(true);
            buf.clear();
            buf.writeVarInt(event.packetID());
            buf.writeUtf(objective);
            buf.writeByte(mode);
            buf.writeUtf(AdventureHelper.componentToJson(AdventureHelper.replaceText(AdventureHelper.jsonToComponent(displayName), tokens)));
            buf.writeVarInt(renderType);
        } catch (Exception e) {
            CraftEngine.instance().logger().warn("Failed to handle ClientboundSetObjectivePacket", e);
        }
    };

    public static final BiConsumer<NetWorkUser, ByteBufPacketEvent> SET_OBJECTIVE_1_20_3 = (user, event) -> {
        if (!Config.interceptScoreboard()) return;
        try {
            FriendlyByteBuf buf = event.getBuffer();
            String objective = buf.readUtf();
            byte mode = buf.readByte();
            if (mode != 0 && mode != 2) return;
            Tag displayName = buf.readNbt(false);
            if (displayName == null) return;
            int renderType = buf.readVarInt();
            boolean optionalNumberFormat = buf.readBoolean();
            if (optionalNumberFormat) {
                int format = buf.readVarInt();
                if (format == 0) {
                    Map<String, Component> tokens = CraftEngine.instance().fontManager().matchTags(displayName.getAsString());
                    if (tokens.isEmpty()) return;
                    event.setChanged(true);
                    buf.clear();
                    buf.writeVarInt(event.packetID());
                    buf.writeUtf(objective);
                    buf.writeByte(mode);
                    buf.writeNbt(AdventureHelper.componentToTag(AdventureHelper.replaceText(AdventureHelper.tagToComponent(displayName), tokens)), false);
                    buf.writeVarInt(renderType);
                    buf.writeBoolean(true);
                    buf.writeVarInt(0);
                } else if (format == 1) {
                    Map<String, Component> tokens = CraftEngine.instance().fontManager().matchTags(displayName.getAsString());
                    if (tokens.isEmpty()) return;
                    Tag style = buf.readNbt(false);
                    event.setChanged(true);
                    buf.clear();
                    buf.writeVarInt(event.packetID());
                    buf.writeUtf(objective);
                    buf.writeByte(mode);
                    buf.writeNbt(AdventureHelper.componentToTag(AdventureHelper.replaceText(AdventureHelper.tagToComponent(displayName), tokens)), false);
                    buf.writeVarInt(renderType);
                    buf.writeBoolean(true);
                    buf.writeVarInt(1);
                    buf.writeNbt(style, false);
                } else if (format == 2) {
                    Tag fixed = buf.readNbt(false);
                    if (fixed == null) return;
                    Map<String, Component> tokens1 = CraftEngine.instance().fontManager().matchTags(displayName.getAsString());
                    Map<String, Component> tokens2 = CraftEngine.instance().fontManager().matchTags(fixed.getAsString());
                    if (tokens1.isEmpty() && tokens2.isEmpty()) return;
                    event.setChanged(true);
                    buf.clear();
                    buf.writeVarInt(event.packetID());
                    buf.writeUtf(objective);
                    buf.writeByte(mode);
                    buf.writeNbt(tokens1.isEmpty() ? displayName : AdventureHelper.componentToTag(AdventureHelper.replaceText(AdventureHelper.tagToComponent(displayName), tokens1)), false);
                    buf.writeVarInt(renderType);
                    buf.writeBoolean(true);
                    buf.writeVarInt(2);
                    buf.writeNbt(tokens2.isEmpty() ? fixed : AdventureHelper.componentToTag(AdventureHelper.replaceText(AdventureHelper.tagToComponent(fixed), tokens2)), false);
                }
            } else {
                Map<String, Component> tokens = CraftEngine.instance().fontManager().matchTags(displayName.getAsString());
                if (tokens.isEmpty()) return;
                event.setChanged(true);
                buf.clear();
                buf.writeVarInt(event.packetID());
                buf.writeUtf(objective);
                buf.writeByte(mode);
                buf.writeNbt(AdventureHelper.componentToTag(AdventureHelper.replaceText(AdventureHelper.tagToComponent(displayName), tokens)), false);
                buf.writeVarInt(renderType);
                buf.writeBoolean(false);
            }
        } catch (Exception e) {
            CraftEngine.instance().logger().warn("Failed to handle ClientboundSetObjectivePacket", e);
        }
    };

    public static final BiConsumer<NetWorkUser, ByteBufPacketEvent> SYSTEM_CHAT_1_20 = (user, event) -> {
        if (!Config.interceptSystemChat()) return;
        try {
            FriendlyByteBuf buf = event.getBuffer();
            String jsonOrPlainString = buf.readUtf();
            Map<String, Component> tokens = CraftEngine.instance().fontManager().matchTags(jsonOrPlainString);
            if (tokens.isEmpty()) return;
            boolean overlay = buf.readBoolean();
            event.setChanged(true);
            buf.clear();
            buf.writeVarInt(event.packetID());
            buf.writeUtf(AdventureHelper.componentToJson(AdventureHelper.replaceText(AdventureHelper.jsonToComponent(jsonOrPlainString), tokens)));
            buf.writeBoolean(overlay);
        } catch (Exception e) {
            CraftEngine.instance().logger().warn("Failed to handle ClientboundSystemChatPacket", e);
        }
    };

    public static final BiConsumer<NetWorkUser, ByteBufPacketEvent> SYSTEM_CHAT_1_20_3 = (user, event) -> {
        if (!Config.interceptSystemChat()) return;
        try {
            FriendlyByteBuf buf = event.getBuffer();
            Tag nbt = buf.readNbt(false);
            if (nbt == null) return;
            Map<String, Component> tokens = CraftEngine.instance().fontManager().matchTags(nbt.getAsString());
            if (tokens.isEmpty()) return;
            boolean overlay = buf.readBoolean();
            event.setChanged(true);
            buf.clear();
            buf.writeVarInt(event.packetID());
            buf.writeNbt(AdventureHelper.componentToTag(AdventureHelper.replaceText(AdventureHelper.tagToComponent(nbt), tokens)), false);
            buf.writeBoolean(overlay);
        } catch (Exception e) {
            CraftEngine.instance().logger().warn("Failed to handle ClientboundSystemChatPacket", e);
        }
    };

    public static final BiConsumer<NetWorkUser, ByteBufPacketEvent> SET_SUBTITLE_TEXT_1_20 = (user, event) -> {
        if (!Config.interceptTitle()) return;
        try {
            FriendlyByteBuf buf = event.getBuffer();
            String json = buf.readUtf();
            Map<String, Component> tokens = CraftEngine.instance().fontManager().matchTags(json);
            if (tokens.isEmpty()) return;
            event.setChanged(true);
            buf.clear();
            buf.writeVarInt(event.packetID());
            buf.writeUtf(AdventureHelper.componentToJson(AdventureHelper.replaceText(AdventureHelper.jsonToComponent(json), tokens)));
        } catch (Exception e) {
            CraftEngine.instance().logger().warn("Failed to handle ClientboundSetSubtitleTextPacket", e);
        }
    };

    public static final BiConsumer<NetWorkUser, ByteBufPacketEvent> SET_SUBTITLE_TEXT_1_20_3 = (user, event) -> {
        if (!Config.interceptTitle()) return;
        try {
            FriendlyByteBuf buf = event.getBuffer();
            Tag nbt = buf.readNbt(false);
            if (nbt == null) return;
            Map<String, Component> tokens = CraftEngine.instance().fontManager().matchTags(nbt.getAsString());
            if (tokens.isEmpty()) return;
            event.setChanged(true);
            buf.clear();
            buf.writeVarInt(event.packetID());
            buf.writeNbt(AdventureHelper.componentToTag(AdventureHelper.replaceText(AdventureHelper.tagToComponent(nbt), tokens)), false);
        } catch (Exception e) {
            CraftEngine.instance().logger().warn("Failed to handle ClientboundSetSubtitleTextPacket", e);
        }
    };

    public static final BiConsumer<NetWorkUser, ByteBufPacketEvent> SET_TITLE_TEXT_1_20 = (user, event) -> {
        if (!Config.interceptTitle()) return;
        try {
            FriendlyByteBuf buf = event.getBuffer();
            String json = buf.readUtf();
            Map<String, Component> tokens = CraftEngine.instance().fontManager().matchTags(json);
            if (tokens.isEmpty()) return;
            event.setChanged(true);
            buf.clear();
            buf.writeVarInt(event.packetID());
            buf.writeUtf(AdventureHelper.componentToJson(AdventureHelper.replaceText(AdventureHelper.jsonToComponent(json), tokens)));
        } catch (Exception e) {
            CraftEngine.instance().logger().warn("Failed to handle ClientboundSetTitleTextPacket", e);
        }
    };

    public static final BiConsumer<NetWorkUser, ByteBufPacketEvent> SET_TITLE_TEXT_1_20_3 = (user, event) -> {
        if (!Config.interceptTitle()) return;
        try {
            FriendlyByteBuf buf = event.getBuffer();
            Tag nbt = buf.readNbt(false);
            if (nbt == null) return;
            Map<String, Component> tokens = CraftEngine.instance().fontManager().matchTags(nbt.getAsString());
            if (tokens.isEmpty()) return;
            event.setChanged(true);
            buf.clear();
            buf.writeVarInt(event.packetID());
            buf.writeNbt(AdventureHelper.componentToTag(AdventureHelper.replaceText(AdventureHelper.tagToComponent(nbt), tokens)), false);
        } catch (Exception e) {
            CraftEngine.instance().logger().warn("Failed to handle ClientboundSetTitleTextPacket", e);
        }
    };

    public static final BiConsumer<NetWorkUser, ByteBufPacketEvent> SET_ACTIONBAR_TEXT_1_20 = (user, event) -> {
        if (!Config.interceptActionBar()) return;
        try {
            FriendlyByteBuf buf = event.getBuffer();
            String json = buf.readUtf();
            Map<String, Component> tokens = CraftEngine.instance().fontManager().matchTags(json);
            if (tokens.isEmpty()) return;
            event.setChanged(true);
            buf.clear();
            buf.writeVarInt(event.packetID());
            buf.writeUtf(AdventureHelper.componentToJson(AdventureHelper.replaceText(AdventureHelper.jsonToComponent(json), tokens)));
        } catch (Exception e) {
            CraftEngine.instance().logger().warn("Failed to handle ClientboundSetActionBarTextPacket", e);
        }
    };

    public static final BiConsumer<NetWorkUser, ByteBufPacketEvent> SET_ACTIONBAR_TEXT_1_20_3 = (user, event) -> {
        if (!Config.interceptActionBar()) return;
        try {
            FriendlyByteBuf buf = event.getBuffer();
            Tag nbt = buf.readNbt(false);
            if (nbt == null) return;
            Map<String, Component> tokens = CraftEngine.instance().fontManager().matchTags(nbt.getAsString());
            if (tokens.isEmpty()) return;
            event.setChanged(true);
            buf.clear();
            buf.writeVarInt(event.packetID());
            buf.writeNbt(AdventureHelper.componentToTag(AdventureHelper.replaceText(AdventureHelper.tagToComponent(nbt), tokens)), false);
        } catch (Exception e) {
            CraftEngine.instance().logger().warn("Failed to handle ClientboundSetActionBarTextPacket", e);
        }
    };

    public static final BiConsumer<NetWorkUser, ByteBufPacketEvent> TAB_LIST_1_20 = (user, event) -> {
        if (!Config.interceptTabList()) return;
        try {
            FriendlyByteBuf buf = event.getBuffer();
            String json1 = buf.readUtf();
            String json2 = buf.readUtf();
            Map<String, Component> tokens1 = CraftEngine.instance().fontManager().matchTags(json1);
            Map<String, Component> tokens2 = CraftEngine.instance().fontManager().matchTags(json2);
            if (tokens1.isEmpty() && tokens2.isEmpty()) return;
            event.setChanged(true);
            buf.clear();
            buf.writeVarInt(event.packetID());
            buf.writeUtf(tokens1.isEmpty() ? json1 : AdventureHelper.componentToJson(AdventureHelper.replaceText(AdventureHelper.jsonToComponent(json1), tokens1)));
            buf.writeUtf(tokens2.isEmpty() ? json2 : AdventureHelper.componentToJson(AdventureHelper.replaceText(AdventureHelper.jsonToComponent(json2), tokens2)));
        } catch (Exception e) {
            CraftEngine.instance().logger().warn("Failed to handle ClientboundTabListPacket", e);
        }
    };

    public static final BiConsumer<NetWorkUser, ByteBufPacketEvent> TAB_LIST_1_20_3 = (user, event) -> {
        if (!Config.interceptTabList()) return;
        try {
            FriendlyByteBuf buf = event.getBuffer();
            Tag nbt1 = buf.readNbt(false);
            if (nbt1 == null) return;
            Tag nbt2 = buf.readNbt(false);
            if (nbt2 == null) return;
            Map<String, Component> tokens1 = CraftEngine.instance().fontManager().matchTags(nbt1.getAsString());
            Map<String, Component> tokens2 = CraftEngine.instance().fontManager().matchTags(nbt2.getAsString());
            if (tokens1.isEmpty() && tokens2.isEmpty()) return;
            event.setChanged(true);
            buf.clear();
            buf.writeVarInt(event.packetID());
            buf.writeNbt(tokens1.isEmpty() ? nbt1 : AdventureHelper.componentToTag(AdventureHelper.replaceText(AdventureHelper.tagToComponent(nbt1), tokens1)), false);
            buf.writeNbt(tokens2.isEmpty() ? nbt2 : AdventureHelper.componentToTag(AdventureHelper.replaceText(AdventureHelper.tagToComponent(nbt2), tokens2)), false);
        } catch (Exception e) {
            CraftEngine.instance().logger().warn("Failed to handle ClientboundTabListPacket", e);
        }
    };

    public static final BiConsumer<NetWorkUser, ByteBufPacketEvent> OPEN_SCREEN_1_20 = (user, event) -> {
        if (!Config.interceptContainer()) return;
        try {
            FriendlyByteBuf buf = event.getBuffer();
            int containerId = buf.readVarInt();
            int type = buf.readVarInt();
            String json = buf.readUtf();
            Map<String, Component> tokens = CraftEngine.instance().fontManager().matchTags(json);
            if (tokens.isEmpty()) return;
            event.setChanged(true);
            buf.clear();
            buf.writeVarInt(event.packetID());
            buf.writeVarInt(containerId);
            buf.writeVarInt(type);
            buf.writeUtf(AdventureHelper.componentToJson(AdventureHelper.replaceText(AdventureHelper.jsonToComponent(json), tokens)));
        } catch (Exception e) {
            CraftEngine.instance().logger().warn("Failed to handle ClientboundOpenScreenPacket", e);
        }
    };

    public static final BiConsumer<NetWorkUser, ByteBufPacketEvent> OPEN_SCREEN_1_20_3 = (user, event) -> {
        if (!Config.interceptContainer()) return;
        try {
            FriendlyByteBuf buf = event.getBuffer();
            int containerId = buf.readVarInt();
            int type = buf.readVarInt();
            Tag nbt = buf.readNbt(false);
            if (nbt == null) return;
            Map<String, Component> tokens = CraftEngine.instance().fontManager().matchTags(nbt.getAsString());
            if (tokens.isEmpty()) return;
            buf.clear();
            buf.writeVarInt(event.packetID());
            buf.writeVarInt(containerId);
            buf.writeVarInt(type);
            buf.writeNbt(AdventureHelper.componentToTag(AdventureHelper.replaceText(AdventureHelper.tagToComponent(nbt), tokens)), false);
        } catch (Exception e) {
            CraftEngine.instance().logger().warn("Failed to handle ClientboundOpenScreenPacket", e);
        }
    };

    public static final BiConsumer<NetWorkUser, ByteBufPacketEvent> LEVEL_PARTICLE_1_21_4 = (user, event) -> {
        try {
            FriendlyByteBuf buf = event.getBuffer();
            boolean overrideLimiter = buf.readBoolean();
            boolean alwaysShow = buf.readBoolean();
            double x = buf.readDouble();
            double y = buf.readDouble();
            double z = buf.readDouble();
            float xDist = buf.readFloat();
            float yDist = buf.readFloat();
            float zDist = buf.readFloat();
            float maxSpeed = buf.readFloat();
            int count = buf.readInt();
            Object option = FastNMS.INSTANCE.method$ParticleTypes$STREAM_CODEC$decode(buf);
            if (option == null) return;
            if (!CoreReflections.clazz$BlockParticleOption.isInstance(option)) return;
            Object blockState = FastNMS.INSTANCE.field$BlockParticleOption$blockState(option);
            int id = BlockStateUtils.blockStateToId(blockState);
            int remapped = remap(id);
            if (remapped == id) return;
            Object type = FastNMS.INSTANCE.method$BlockParticleOption$getType(option);
            Object remappedOption = FastNMS.INSTANCE.constructor$BlockParticleOption(type, BlockStateUtils.idToBlockState(remapped));
            event.setChanged(true);
            buf.clear();
            buf.writeVarInt(event.packetID());
            buf.writeBoolean(overrideLimiter);
            buf.writeBoolean(alwaysShow);
            buf.writeDouble(x);
            buf.writeDouble(y);
            buf.writeDouble(z);
            buf.writeFloat(xDist);
            buf.writeFloat(yDist);
            buf.writeFloat(zDist);
            buf.writeFloat(maxSpeed);
            buf.writeInt(count);
            FastNMS.INSTANCE.method$ParticleTypes$STREAM_CODEC$encode(buf, remappedOption);
        } catch (Exception e) {
            CraftEngine.instance().logger().warn("Failed to handle ClientboundLevelParticlesPacket", e);
        }
    };

    public static final BiConsumer<NetWorkUser, ByteBufPacketEvent> LEVEL_PARTICLE_1_20_5 = (user, event) -> {
        try {
            FriendlyByteBuf buf = event.getBuffer();
            boolean overrideLimiter = buf.readBoolean();
            double x = buf.readDouble();
            double y = buf.readDouble();
            double z = buf.readDouble();
            float xDist = buf.readFloat();
            float yDist = buf.readFloat();
            float zDist = buf.readFloat();
            float maxSpeed = buf.readFloat();
            int count = buf.readInt();
            Object option = FastNMS.INSTANCE.method$ParticleTypes$STREAM_CODEC$decode(buf);
            if (option == null) return;
            if (!CoreReflections.clazz$BlockParticleOption.isInstance(option)) return;
            Object blockState = FastNMS.INSTANCE.field$BlockParticleOption$blockState(option);
            int id = BlockStateUtils.blockStateToId(blockState);
            int remapped = remap(id);
            if (remapped == id) return;
            Object type = FastNMS.INSTANCE.method$BlockParticleOption$getType(option);
            Object remappedOption = FastNMS.INSTANCE.constructor$BlockParticleOption(type, BlockStateUtils.idToBlockState(remapped));
            event.setChanged(true);
            buf.clear();
            buf.writeVarInt(event.packetID());
            buf.writeBoolean(overrideLimiter);
            buf.writeDouble(x);
            buf.writeDouble(y);
            buf.writeDouble(z);
            buf.writeFloat(xDist);
            buf.writeFloat(yDist);
            buf.writeFloat(zDist);
            buf.writeFloat(maxSpeed);
            buf.writeInt(count);
            FastNMS.INSTANCE.method$ParticleTypes$STREAM_CODEC$encode(buf, remappedOption);
        } catch (Exception e) {
            CraftEngine.instance().logger().warn("Failed to handle ClientboundLevelParticlesPacket", e);
        }
    };

    public static final BiConsumer<NetWorkUser, ByteBufPacketEvent> LEVEL_PARTICLE_1_20 = (user, event) -> {
        try {
            FriendlyByteBuf buf = event.getBuffer();
            Object particleType = FastNMS.INSTANCE.method$FriendlyByteBuf$readById(buf, MBuiltInRegistries.PARTICLE_TYPE);
            boolean overrideLimiter = buf.readBoolean();
            double x = buf.readDouble();
            double y = buf.readDouble();
            double z = buf.readDouble();
            float xDist = buf.readFloat();
            float yDist = buf.readFloat();
            float zDist = buf.readFloat();
            float maxSpeed = buf.readFloat();
            int count = buf.readInt();
            Object option = FastNMS.INSTANCE.method$ClientboundLevelParticlesPacket$readParticle(buf, particleType);
            if (option == null) return;
            if (!CoreReflections.clazz$BlockParticleOption.isInstance(option)) return;
            Object blockState = FastNMS.INSTANCE.field$BlockParticleOption$blockState(option);
            int id = BlockStateUtils.blockStateToId(blockState);
            int remapped = remap(id);
            if (remapped == id) return;
            Object type = FastNMS.INSTANCE.method$BlockParticleOption$getType(option);
            Object remappedOption = FastNMS.INSTANCE.constructor$BlockParticleOption(type, BlockStateUtils.idToBlockState(remapped));
            event.setChanged(true);
            buf.clear();
            buf.writeVarInt(event.packetID());
            FastNMS.INSTANCE.method$FriendlyByteBuf$writeId(buf, remappedOption, MBuiltInRegistries.PARTICLE_TYPE);
            buf.writeBoolean(overrideLimiter);
            buf.writeDouble(x);
            buf.writeDouble(y);
            buf.writeDouble(z);
            buf.writeFloat(xDist);
            buf.writeFloat(yDist);
            buf.writeFloat(zDist);
            buf.writeFloat(maxSpeed);
            buf.writeInt(count);
            FastNMS.INSTANCE.method$ParticleOptions$writeToNetwork(remappedOption, buf);
        } catch (Exception e) {
            CraftEngine.instance().logger().warn("Failed to handle ClientboundLevelParticlesPacket", e);
        }
    };

    public static final TriConsumer<NetWorkUser, NMSPacketEvent, Object> PLAYER_ACTION = (user, event, packet) -> {
        try {
            if (!user.isOnline()) return;
            BukkitServerPlayer player = (BukkitServerPlayer) user;
            Player platformPlayer = player.platformPlayer();
            World world = platformPlayer.getWorld();
            Object blockPos = FastNMS.INSTANCE.field$ServerboundPlayerActionPacket$pos(packet);
            BlockPos pos = LocationUtils.fromBlockPos(blockPos);
            if (VersionHelper.isFolia()) {
                BukkitCraftEngine.instance().scheduler().sync().run(() -> {
                    try {
                        handlePlayerActionPacketOnMainThread(player, world, pos, packet);
                    } catch (Exception e) {
                        CraftEngine.instance().logger().warn("Failed to handle ServerboundPlayerActionPacket", e);
                    }
                }, world, pos.x() >> 4, pos.z() >> 4);
            } else {
                handlePlayerActionPacketOnMainThread(player, world, pos, packet);
            }
        } catch (Exception e) {
            CraftEngine.instance().logger().warn("Failed to handle ServerboundPlayerActionPacket", e);
        }
    };

    private static void handlePlayerActionPacketOnMainThread(BukkitServerPlayer player, World world, BlockPos pos, Object packet) {
        Object action = FastNMS.INSTANCE.field$ServerboundPlayerActionPacket$action(packet);
        if (action == NetworkReflections.instance$ServerboundPlayerActionPacket$Action$START_DESTROY_BLOCK) {
            Object serverLevel = FastNMS.INSTANCE.field$CraftWorld$ServerLevel(world);
            Object blockState = FastNMS.INSTANCE.method$BlockGetter$getBlockState(serverLevel, LocationUtils.toBlockPos(pos));
            int stateId = BlockStateUtils.blockStateToId(blockState);
            // not a custom block
            if (BlockStateUtils.isVanillaBlock(stateId)) {
                if (Config.enableSoundSystem()) {
                    Object blockOwner = FastNMS.INSTANCE.method$BlockState$getBlock(blockState);
                    if (BukkitBlockManager.instance().isBlockSoundRemoved(blockOwner)) {
                        player.startMiningBlock(pos, blockState, null);
                        return;
                    }
                }
                if (player.isMiningBlock()) {
                    player.stopMiningBlock();
                } else {
                    player.setClientSideCanBreakBlock(true);
                }
                return;
            }
            if (player.isAdventureMode()) {
                if (Config.simplifyAdventureBreakCheck()) {
                    ImmutableBlockState state = BukkitBlockManager.instance().getImmutableBlockStateUnsafe(stateId);
                    if (!player.canBreak(pos, state.vanillaBlockState().handle())) {
                        player.preventMiningBlock();
                        return;
                    }
                } else {
                    if (!player.canBreak(pos, null)) {
                        player.preventMiningBlock();
                        return;
                    }
                }
            }
            player.startMiningBlock(pos, blockState, BukkitBlockManager.instance().getImmutableBlockStateUnsafe(stateId));
        } else if (action == NetworkReflections.instance$ServerboundPlayerActionPacket$Action$ABORT_DESTROY_BLOCK) {
            if (player.isMiningBlock()) {
                player.abortMiningBlock();
            }
        } else if (action == NetworkReflections.instance$ServerboundPlayerActionPacket$Action$STOP_DESTROY_BLOCK) {
            if (player.isMiningBlock()) {
                player.stopMiningBlock();
            }
        }
    }

    public static final TriConsumer<NetWorkUser, NMSPacketEvent, Object> HELLO_C2S = (user, event, packet) -> {
        try {
            BukkitServerPlayer player = (BukkitServerPlayer) user;
            String name = (String) NetworkReflections.field$ServerboundHelloPacket$name.get(packet);
            player.setName(name);
            if (VersionHelper.isOrAbove1_20_2()) {
                UUID uuid = (UUID) NetworkReflections.field$ServerboundHelloPacket$uuid.get(packet);
                player.setUUID(uuid);
            } else {
                @SuppressWarnings("unchecked")
                Optional<UUID> uuid = (Optional<UUID>) NetworkReflections.field$ServerboundHelloPacket$uuid.get(packet);
                if (uuid.isPresent()) {
                    player.setUUID(uuid.get());
                } else {
                    player.setUUID(UUID.nameUUIDFromBytes(("OfflinePlayer:" + name).getBytes(StandardCharsets.UTF_8)));
                }
            }
        } catch (Exception e) {
            CraftEngine.instance().logger().warn("Failed to handle ServerboundHelloPacket", e);
        }
    };

    public static final TriConsumer<NetWorkUser, NMSPacketEvent, Object> SWING_HAND = (user, event, packet) -> {
        try {
            if (!user.isOnline()) return;
            BukkitServerPlayer player = (BukkitServerPlayer) user;
            if (!player.isMiningBlock()) return;
            Object hand = FastNMS.INSTANCE.field$ServerboundSwingPacket$hand(packet);
            if (hand == CoreReflections.instance$InteractionHand$MAIN_HAND) {
                player.onSwingHand();
            }
        } catch (Exception e) {
            CraftEngine.instance().logger().warn("Failed to handle ServerboundSwingPacket", e);
        }
    };

    public static final TriConsumer<NetWorkUser, NMSPacketEvent, Object> USE_ITEM_ON = (user, event, packet) -> {
        try {
            if (!user.isOnline()) return;
            BukkitServerPlayer player = (BukkitServerPlayer) user;
            if (player.isMiningBlock()) {
                player.stopMiningBlock();
            }
        } catch (Exception e) {
            CraftEngine.instance().logger().warn("Failed to handle ServerboundUseItemOnPacket", e);
        }
    };

    public static final TriConsumer<NetWorkUser, NMSPacketEvent, Object> RESPAWN = (user, event, packet) -> {
        try {
            BukkitServerPlayer player = (BukkitServerPlayer) user;
            player.clearView();
            Object dimensionKey;
            if (!VersionHelper.isOrAbove1_20_2()) {
                dimensionKey = NetworkReflections.field$ClientboundRespawnPacket$dimension.get(packet);
            } else {
                Object commonInfo = NetworkReflections.field$ClientboundRespawnPacket$commonPlayerSpawnInfo.get(packet);
                dimensionKey = NetworkReflections.field$CommonPlayerSpawnInfo$dimension.get(commonInfo);
            }
            Object location = FastNMS.INSTANCE.field$ResourceKey$location(dimensionKey);
            World world = Bukkit.getWorld(Objects.requireNonNull(NamespacedKey.fromString(location.toString())));
            if (world != null) {
                int sectionCount = (world.getMaxHeight() - world.getMinHeight()) / 16;
                player.setClientSideSectionCount(sectionCount);
                player.setClientSideDimension(Key.of(location.toString()));
            } else {
                CraftEngine.instance().logger().warn("Failed to handle ClientboundRespawnPacket: World " + location + " does not exist");
            }
        } catch (Exception e) {
            CraftEngine.instance().logger().warn("Failed to handle ClientboundRespawnPacket", e);
        }
    };

    public static final TriConsumer<NetWorkUser, NMSPacketEvent, Object> LOGIN = (user, event, packet) -> {
        try {
            BukkitServerPlayer player = (BukkitServerPlayer) user;
            player.setConnectionState(ConnectionState.PLAY);
            Object dimensionKey;
            if (!VersionHelper.isOrAbove1_20_2()) {
                if (BukkitNetworkManager.hasViaVersion()) {
                    user.setProtocolVersion(CraftEngine.instance().compatibilityManager().getPlayerProtocolVersion(player.uuid()));
                }
                dimensionKey = NetworkReflections.field$ClientboundLoginPacket$dimension.get(packet);
            } else {
                Object commonInfo = NetworkReflections.field$ClientboundLoginPacket$commonPlayerSpawnInfo.get(packet);
                dimensionKey = NetworkReflections.field$CommonPlayerSpawnInfo$dimension.get(commonInfo);
            }
            Object location = FastNMS.INSTANCE.field$ResourceKey$location(dimensionKey);
            World world = Bukkit.getWorld(Objects.requireNonNull(NamespacedKey.fromString(location.toString())));
            if (world != null) {
                int sectionCount = (world.getMaxHeight() - world.getMinHeight()) / 16;
                player.setClientSideSectionCount(sectionCount);
                player.setClientSideDimension(Key.of(location.toString()));
            } else {
                CraftEngine.instance().logger().warn("Failed to handle ClientboundLoginPacket: World " + location + " does not exist");
            }
        } catch (Exception e) {
            CraftEngine.instance().logger().warn("Failed to handle ClientboundLoginPacket", e);
        }
    };

    // 1.21.4-
    // We can't find the best solution, we can only keep the feel as good as possible
    // When the hotbar is full, the latest creative mode inventory can only be accessed when the player opens the inventory screen. Currently, it is not worth further handling this issue.
    public static final TriConsumer<NetWorkUser, NMSPacketEvent, Object> SET_CREATIVE_SLOT = (user, event, packet) -> {
        try {
            if (user.protocolVersion().isVersionNewerThan(ProtocolVersion.V1_21_4)) return;
            if (!user.isOnline()) return;
            BukkitServerPlayer player = (BukkitServerPlayer) user;
            if (VersionHelper.isFolia()) {
                player.platformPlayer().getScheduler().run(BukkitCraftEngine.instance().javaPlugin(), (t) -> {
                    try {
                        handleSetCreativeSlotPacketOnMainThread(player, packet);
                    } catch (Exception e) {
                        CraftEngine.instance().logger().warn("Failed to handle ServerboundSetCreativeModeSlotPacket", e);
                    }
                }, () -> {});
            } else {
                handleSetCreativeSlotPacketOnMainThread(player, packet);
            }
        } catch (Exception e) {
            CraftEngine.instance().logger().warn("Failed to handle ServerboundSetCreativeModeSlotPacket", e);
        }
    };

    private static void handleSetCreativeSlotPacketOnMainThread(BukkitServerPlayer player, Object packet) throws Exception {
        Player bukkitPlayer = player.platformPlayer();
        if (bukkitPlayer == null) return;
        if (bukkitPlayer.getGameMode() != GameMode.CREATIVE) return;
        int slot = VersionHelper.isOrAbove1_20_5() ? NetworkReflections.field$ServerboundSetCreativeModeSlotPacket$slotNum.getShort(packet) : NetworkReflections.field$ServerboundSetCreativeModeSlotPacket$slotNum.getInt(packet);
        if (slot < 36 || slot > 44) return;
        ItemStack item = FastNMS.INSTANCE.method$CraftItemStack$asCraftMirror(NetworkReflections.field$ServerboundSetCreativeModeSlotPacket$itemStack.get(packet));
        if (ItemUtils.isEmpty(item)) return;
        if (slot - 36 != bukkitPlayer.getInventory().getHeldItemSlot()) {
            return;
        }
        double interactionRange = player.getCachedInteractionRange();
        // do ray trace to get current block
        RayTraceResult result = bukkitPlayer.rayTraceBlocks(interactionRange, FluidCollisionMode.NEVER);
        if (result == null) return;
        Block hitBlock = result.getHitBlock();
        if (hitBlock == null) return;
        ImmutableBlockState state = BukkitBlockManager.instance().getImmutableBlockState(BlockStateUtils.blockDataToId(hitBlock.getBlockData()));
        // not a custom block
        if (state == null || state.isEmpty()) return;
        Key itemId = state.settings().itemId();
        // no item available
        if (itemId == null) return;
        BlockData data = BlockStateUtils.fromBlockData(state.vanillaBlockState().handle());
        // compare item
        if (data == null || !data.getMaterial().equals(item.getType())) return;
        ItemStack itemStack = BukkitCraftEngine.instance().itemManager().buildCustomItemStack(itemId, player);
        if (ItemUtils.isEmpty(itemStack)) {
            CraftEngine.instance().logger().warn("Item: " + itemId + " is not a valid item");
            return;
        }
        PlayerInventory inventory = bukkitPlayer.getInventory();
        int sameItemSlot = -1;
        int emptySlot = -1;
        for (int i = 0; i < 9 + 27; i++) {
            ItemStack invItem = inventory.getItem(i);
            if (ItemUtils.isEmpty(invItem)) {
                if (emptySlot == -1 && i < 9) emptySlot = i;
                continue;
            }
            if (invItem.getType().equals(itemStack.getType()) && invItem.getItemMeta().equals(itemStack.getItemMeta())) {
                if (sameItemSlot == -1) sameItemSlot = i;
            }
        }
        if (sameItemSlot != -1) {
            if (sameItemSlot < 9) {
                inventory.setHeldItemSlot(sameItemSlot);
                ItemStack previousItem = inventory.getItem(slot - 36);
                BukkitCraftEngine.instance().scheduler().sync().runDelayed(() -> inventory.setItem(slot - 36, previousItem));
            } else {
                ItemStack sameItem = inventory.getItem(sameItemSlot);
                int finalSameItemSlot = sameItemSlot;
                BukkitCraftEngine.instance().scheduler().sync().runDelayed(() -> {
                    inventory.setItem(finalSameItemSlot, new ItemStack(Material.AIR));
                    inventory.setItem(slot - 36, sameItem);
                });
            }
        } else {
            if (item.getAmount() == 1) {
                if (ItemUtils.isEmpty(inventory.getItem(slot - 36))) {
                    BukkitCraftEngine.instance().scheduler().sync().runDelayed(() -> inventory.setItem(slot - 36, itemStack));
                    return;
                }
                if (emptySlot != -1) {
                    inventory.setHeldItemSlot(emptySlot);
                    inventory.setItem(emptySlot, itemStack);
                } else {
                    BukkitCraftEngine.instance().scheduler().sync().runDelayed(() -> inventory.setItem(slot - 36, itemStack));
                }
            }
        }
    }

    // 1.21.4+
    public static final TriConsumer<NetWorkUser, NMSPacketEvent, Object> PICK_ITEM_FROM_BLOCK = (user, event, packet) -> {
        try {
            if (!user.isOnline()) return;
            Player player = (Player) user.platformPlayer();
            if (player == null) return;
            Object pos = NetworkReflections.field$ServerboundPickItemFromBlockPacket$pos.get(packet);
            if (VersionHelper.isFolia()) {
                int x = FastNMS.INSTANCE.field$Vec3i$x(pos);
                int z = FastNMS.INSTANCE.field$Vec3i$z(pos);
                BukkitCraftEngine.instance().scheduler().sync().run(() -> {
                    try {
                        handlePickItemFromBlockPacketOnMainThread(player, pos);
                    } catch (Exception e) {
                        CraftEngine.instance().logger().warn("Failed to handle ServerboundPickItemFromBlockPacket", e);
                    }
                }, player.getWorld(), x >> 4, z >> 4);
            } else {
                BukkitCraftEngine.instance().scheduler().sync().run(() -> {
                    try {
                        handlePickItemFromBlockPacketOnMainThread(player, pos);
                    } catch (Exception e) {
                        CraftEngine.instance().logger().warn("Failed to handle ServerboundPickItemFromBlockPacket", e);
                    }
                });
            }
        } catch (Exception e) {
            CraftEngine.instance().logger().warn("Failed to handle ServerboundPickItemFromBlockPacket", e);
        }
    };

    private static void handlePickItemFromBlockPacketOnMainThread(Player player, Object pos) throws Exception {
        Object serverLevel = FastNMS.INSTANCE.field$CraftWorld$ServerLevel(player.getWorld());
        Object blockState = FastNMS.INSTANCE.method$BlockGetter$getBlockState(serverLevel, pos);
        ImmutableBlockState state = BukkitBlockManager.instance().getImmutableBlockState(BlockStateUtils.blockStateToId(blockState));
        if (state == null) return;
        Key itemId = state.settings().itemId();
        if (itemId == null) return;
        pickItem(player, itemId, pos, null);
    }

    // 1.21.4+
    public static final TriConsumer<NetWorkUser, NMSPacketEvent, Object> PICK_ITEM_FROM_ENTITY = (user, event, packet) -> {
        try {
            int entityId = (int) NetworkReflections.field$ServerboundPickItemFromEntityPacket$id.get(packet);
            BukkitFurniture furniture = BukkitFurnitureManager.instance().loadedFurnitureByEntityId(entityId);
            if (furniture == null) return;
            Player player = (Player) user.platformPlayer();
            if (player == null) return;
            if (VersionHelper.isFolia()) {
                player.getScheduler().run(BukkitCraftEngine.instance().javaPlugin(), (t) -> {
                    try {
                        handlePickItemFromEntityOnMainThread(player, furniture);
                    } catch (Exception e) {
                        CraftEngine.instance().logger().warn("Failed to handle ServerboundPickItemFromEntityPacket", e);
                    }
                }, () -> {});
            } else {
                BukkitCraftEngine.instance().scheduler().sync().run(() -> {
                    try {
                        handlePickItemFromEntityOnMainThread(player, furniture);
                    } catch (Exception e) {
                        CraftEngine.instance().logger().warn("Failed to handle ServerboundPickItemFromEntityPacket", e);
                    }
                });
            }
        } catch (Exception e) {
            CraftEngine.instance().logger().warn("Failed to handle ServerboundPickItemFromEntityPacket", e);
        }
    };

    private static void handlePickItemFromEntityOnMainThread(Player player, BukkitFurniture furniture) throws Exception {
        Key itemId = furniture.config().settings().itemId();
        if (itemId == null) return;
        pickItem(player, itemId, null, FastNMS.INSTANCE.method$CraftEntity$getHandle(furniture.baseEntity()));
    }

    private static void pickItem(Player player, Key itemId, @Nullable Object blockPos, @Nullable Object entity) throws IllegalAccessException, InvocationTargetException {
        ItemStack itemStack = BukkitCraftEngine.instance().itemManager().buildCustomItemStack(itemId, BukkitCraftEngine.instance().adapt(player));
        if (itemStack == null) {
            CraftEngine.instance().logger().warn("Item: " + itemId + " is not a valid item");
            return;
        }
        assert CoreReflections.method$ServerGamePacketListenerImpl$tryPickItem != null;
        if (VersionHelper.isOrAbove1_21_5()) {
            CoreReflections.method$ServerGamePacketListenerImpl$tryPickItem.invoke(
                    CoreReflections.field$ServerPlayer$connection.get(FastNMS.INSTANCE.method$CraftPlayer$getHandle(player)),
                    FastNMS.INSTANCE.method$CraftItemStack$asNMSCopy(itemStack), blockPos, entity, true);
        } else {
            CoreReflections.method$ServerGamePacketListenerImpl$tryPickItem.invoke(
                    CoreReflections.field$ServerPlayer$connection.get(FastNMS.INSTANCE.method$CraftPlayer$getHandle(player)), FastNMS.INSTANCE.method$CraftItemStack$asNMSCopy(itemStack));
        }
    }

    public static final BiConsumer<NetWorkUser, ByteBufPacketEvent> ADD_ENTITY_BYTEBUFFER = (user, event) -> {
        try {
            FriendlyByteBuf buf = event.getBuffer();
            buf.readVarInt();
            buf.readUUID();
            int type = buf.readVarInt();
            ADD_ENTITY_HANDLERS[type].accept(user, event);
        } catch (Exception e) {
            CraftEngine.instance().logger().warn("Failed to handle ClientboundAddEntityPacket", e);
        }
    };

    // 1.21.2+
    public static final TriConsumer<NetWorkUser, NMSPacketEvent, Object> SYNC_ENTITY_POSITION = (user, event, packet) -> {
        try {
            int entityId = FastNMS.INSTANCE.method$ClientboundEntityPositionSyncPacket$id(packet);
            EntityPacketHandler handler = user.entityPacketHandlers().get(entityId);
            if (handler != null) {
                handler.handleSyncEntityPosition(user, event, packet);
            }
        } catch (Exception e) {
            CraftEngine.instance().logger().warn("Failed to handle ClientboundEntityPositionSyncPacket", e);
        }
    };

    public static final BiConsumer<NetWorkUser, ByteBufPacketEvent> REMOVE_ENTITY = (user, event) -> {
        try {
            FriendlyByteBuf buf = event.getBuffer();
            boolean isChange = false;
            IntList intList = buf.readIntIdList();
            for (int i = 0, size = intList.size(); i < size; i++) {
                int entityId = intList.getInt(i);
                EntityPacketHandler handler = user.entityPacketHandlers().remove(entityId);
                if (handler != null && handler.handleEntitiesRemove(user, intList)) {
                    isChange = true;
                }
            }
            if (isChange) {
                event.setChanged(true);
                buf.clear();
                buf.writeVarInt(event.packetID());
                buf.writeIntIdList(intList);
            }
        } catch (Exception e) {
            CraftEngine.instance().logger().warn("Failed to handle ClientboundRemoveEntitiesPacket", e);
        }
    };

    public static final TriConsumer<NetWorkUser, NMSPacketEvent, Object> INTERACT_ENTITY = (user, event, packet) -> {
        try {
            Player player = (Player) user.platformPlayer();
            if (player == null) return;
            int entityId;
            if (BukkitNetworkManager.hasModelEngine()) {
                int fakeId = FastNMS.INSTANCE.field$ServerboundInteractPacket$entityId(packet);
                entityId = CraftEngine.instance().compatibilityManager().interactionToBaseEntity(fakeId);
            } else {
                entityId = FastNMS.INSTANCE.field$ServerboundInteractPacket$entityId(packet);
            }
            BukkitFurniture furniture = BukkitFurnitureManager.instance().loadedFurnitureByEntityId(entityId);
            if (furniture == null) return;
            Object action = NetworkReflections.field$ServerboundInteractPacket$action.get(packet);
            Object actionType = NetworkReflections.method$ServerboundInteractPacket$Action$getType.invoke(action);
            if (actionType == null) return;
            Location location = furniture.baseEntity().getLocation();
            BukkitServerPlayer serverPlayer = (BukkitServerPlayer) user;
            if (serverPlayer.isSpectatorMode()) return;
            BukkitCraftEngine.instance().scheduler().sync().run(() -> {
                if (actionType == NetworkReflections.instance$ServerboundInteractPacket$ActionType$ATTACK) {
                    // todo 冒险模式破坏工具白名单
                    if (serverPlayer.isAdventureMode()) return;
                    if (furniture.isValid()) {
                        if (!BukkitCraftEngine.instance().antiGrief().canBreak(player, location)) {
                            return;
                        }
                        FurnitureBreakEvent breakEvent = new FurnitureBreakEvent(serverPlayer.platformPlayer(), furniture);
                        if (EventUtils.fireAndCheckCancel(breakEvent)) {
                            return;
                        }

                        // execute functions
                        PlayerOptionalContext context = PlayerOptionalContext.of(serverPlayer, ContextHolder.builder()
                                .withParameter(DirectContextParameters.FURNITURE, furniture)
                                .withParameter(DirectContextParameters.POSITION, furniture.position())
                        );
                        furniture.config().execute(context, EventTrigger.LEFT_CLICK);
                        furniture.config().execute(context, EventTrigger.BREAK);

                        CraftEngineFurniture.remove(furniture, serverPlayer, !serverPlayer.isCreativeMode(), true);
                    }
                } else if (actionType == NetworkReflections.instance$ServerboundInteractPacket$ActionType$INTERACT_AT) {
                    InteractionHand hand;
                    Location interactionPoint;
                    try {
                        Object interactionHand = NetworkReflections.field$ServerboundInteractPacket$InteractionAtLocationAction$hand.get(action);
                        hand = interactionHand == CoreReflections.instance$InteractionHand$MAIN_HAND ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
                        Object vec3 = NetworkReflections.field$ServerboundInteractPacket$InteractionAtLocationAction$location.get(action);

                        double x = FastNMS.INSTANCE.field$Vec3$x(vec3);
                        double y = FastNMS.INSTANCE.field$Vec3$y(vec3);
                        double z = FastNMS.INSTANCE.field$Vec3$z(vec3);
                        interactionPoint = new Location(location.getWorld(), x, y, z);
                    } catch (ReflectiveOperationException e) {
                        throw new RuntimeException("Failed to get interaction hand from interact packet", e);
                    }
                    FurnitureInteractEvent interactEvent = new FurnitureInteractEvent(serverPlayer.platformPlayer(), furniture, hand, interactionPoint);
                    if (EventUtils.fireAndCheckCancel(interactEvent)) {
                        return;
                    }

                    // execute functions
                    PlayerOptionalContext context = PlayerOptionalContext.of(serverPlayer, ContextHolder.builder()
                            .withParameter(DirectContextParameters.FURNITURE, furniture)
                            .withParameter(DirectContextParameters.POSITION, furniture.position())
                    );
                    furniture.config().execute(context, EventTrigger.RIGHT_CLICK);

                    if (player.isSneaking()) {
                        // try placing another furniture above it
                        AABB hitBox = furniture.aabbByEntityId(entityId);
                        if (hitBox == null) return;
                        Item<ItemStack> itemInHand = serverPlayer.getItemInHand(InteractionHand.MAIN_HAND);
                        if (itemInHand == null) return;
                        Optional<CustomItem<ItemStack>> optionalCustomitem = itemInHand.getCustomItem();
                        Location eyeLocation = player.getEyeLocation();
                        Vector direction = eyeLocation.getDirection();
                        Location endLocation = eyeLocation.clone();
                        endLocation.add(direction.multiply(serverPlayer.getCachedInteractionRange()));
                        Optional<EntityHitResult> result = hitBox.clip(LocationUtils.toVec3d(eyeLocation), LocationUtils.toVec3d(endLocation));
                        if (result.isEmpty()) {
                            return;
                        }
                        EntityHitResult hitResult = result.get();
                        if (optionalCustomitem.isPresent() && !optionalCustomitem.get().behaviors().isEmpty()) {
                            for (ItemBehavior behavior : optionalCustomitem.get().behaviors()) {
                                if (behavior instanceof FurnitureItemBehavior) {
                                    behavior.useOnBlock(new UseOnContext(serverPlayer, InteractionHand.MAIN_HAND, new BlockHitResult(hitResult.hitLocation(), hitResult.direction(), BlockPos.fromVec3d(hitResult.hitLocation()), false)));
                                    return;
                                }
                            }
                        }
                        // now simulate vanilla item behavior
                        serverPlayer.setResendSound();
                        FastNMS.INSTANCE.simulateInteraction(serverPlayer.serverPlayer(), DirectionUtils.toNMSDirection(hitResult.direction()), hitResult.hitLocation().x, hitResult.hitLocation().y, hitResult.hitLocation().z, LocationUtils.toBlockPos(hitResult.blockPos()));
                    } else {
                        furniture.findFirstAvailableSeat(entityId).ifPresent(seat -> {
                            if (furniture.tryOccupySeat(seat)) {
                                SeatEntity currentSeat = serverPlayer.seat();
                                if (currentSeat != null) currentSeat.dismount(serverPlayer);

								furniture.spawnSeatEntityForPlayer(serverPlayer, seat);
                            }
                        });
                    }
                }
            }, player.getWorld(), location.getBlockX() >> 4, location.getBlockZ() >> 4);
        } catch (Exception e) {
            CraftEngine.instance().logger().warn("Failed to handle ServerboundInteractPacket", e);
        }
    };

    public static final BiConsumer<NetWorkUser, ByteBufPacketEvent> SOUND = (user, event) -> {
        try {
            FriendlyByteBuf buf = event.getBuffer();
            int id = buf.readVarInt();
            if (id == 0) {
                Key soundId = buf.readKey();
                Float range = null;
                if (buf.readBoolean()) {
                    range = buf.readFloat();
                }
                int source = buf.readVarInt();
                int x = buf.readInt();
                int y = buf.readInt();
                int z = buf.readInt();
                float volume = buf.readFloat();
                float pitch = buf.readFloat();
                long seed = buf.readLong();
                Key mapped = BukkitBlockManager.instance().replaceSoundIfExist(soundId);
                if (mapped != null) {
                    event.setChanged(true);
                    buf.clear();
                    buf.writeVarInt(event.packetID());
                    buf.writeVarInt(0);
                    buf.writeKey(mapped);
                    if (range != null) {
                        buf.writeBoolean(true);
                        buf.writeFloat(range);
                    } else {
                        buf.writeBoolean(false);
                    }
                    buf.writeVarInt(source);
                    buf.writeInt(x);
                    buf.writeInt(y);
                    buf.writeInt(z);
                    buf.writeFloat(volume);
                    buf.writeFloat(pitch);
                    buf.writeLong(seed);
                }
            } else {
                Optional<Object> optionalSound = FastNMS.INSTANCE.method$IdMap$byId(MBuiltInRegistries.SOUND_EVENT, id - 1);
                if (optionalSound.isEmpty()) return;
                Object soundEvent = optionalSound.get();
                Key soundId = Key.of(FastNMS.INSTANCE.method$SoundEvent$location(soundEvent));
                int source = buf.readVarInt();
                int x = buf.readInt();
                int y = buf.readInt();
                int z = buf.readInt();
                float volume = buf.readFloat();
                float pitch = buf.readFloat();
                long seed = buf.readLong();
                Key mapped = BukkitBlockManager.instance().replaceSoundIfExist(soundId);
                if (mapped != null) {
                    event.setChanged(true);
                    buf.clear();
                    buf.writeVarInt(event.packetID());
                    buf.writeVarInt(0);
                    Object newId = KeyUtils.toResourceLocation(mapped);
                    Object newSoundEvent = FastNMS.INSTANCE.constructor$SoundEvent(newId, FastNMS.INSTANCE.method$SoundEvent$fixedRange(soundEvent));
                    FastNMS.INSTANCE.method$SoundEvent$directEncode(buf, newSoundEvent);
                    buf.writeVarInt(source);
                    buf.writeInt(x);
                    buf.writeInt(y);
                    buf.writeInt(z);
                    buf.writeFloat(volume);
                    buf.writeFloat(pitch);
                    buf.writeLong(seed);
                }
            }
        } catch (Exception e) {
            CraftEngine.instance().logger().warn("Failed to handle ClientboundSoundPacket", e);
        }
    };

    // we handle it on packet level to prevent it from being captured by plugins
    public static final TriConsumer<NetWorkUser, NMSPacketEvent, Object> RENAME_ITEM = (user, event, packet) -> {
        try {
            if (!Config.filterAnvil()) return;
            if (((BukkitServerPlayer) user).hasPermission(FontManager.BYPASS_ANVIL)) {
                return;
            }
            String message = (String) NetworkReflections.field$ServerboundRenameItemPacket$name.get(packet);
            if (message != null && !message.isEmpty()) {
                // check bypass
                FontManager manager = CraftEngine.instance().fontManager();
                IllegalCharacterProcessResult result = manager.processIllegalCharacters(message);
                if (result.has()) {
                    try {
                        NetworkReflections.field$ServerboundRenameItemPacket$name.set(packet, result.text());
                    } catch (ReflectiveOperationException e) {
                        CraftEngine.instance().logger().warn("Failed to replace chat", e);
                    }
                }
            }
        } catch (Exception e) {
            CraftEngine.instance().logger().warn("Failed to handle ServerboundRenameItemPacket", e);
        }
    };

    // we handle it on packet level to prevent it from being captured by plugins
    public static final TriConsumer<NetWorkUser, NMSPacketEvent, Object> SIGN_UPDATE = (user, event, packet) -> {
        try {
            if (!Config.filterSign()) return;
            // check bypass
            if (((BukkitServerPlayer) user).hasPermission(FontManager.BYPASS_SIGN)) {
                return;
            }
            String[] lines = (String[]) NetworkReflections.field$ServerboundSignUpdatePacket$lines.get(packet);
            FontManager manager = CraftEngine.instance().fontManager();
            if (!manager.isDefaultFontInUse()) return;
            for (int i = 0; i < lines.length; i++) {
                String line = lines[i];
                if (line != null && !line.isEmpty()) {
                    IllegalCharacterProcessResult result = manager.processIllegalCharacters(line);
                    if (result.has()) {
                        lines[i] = result.text();
                    }
                }
            }
        } catch (Exception e) {
            CraftEngine.instance().logger().warn("Failed to handle ServerboundSignUpdatePacket", e);
        }
    };

    // we handle it on packet level to prevent it from being captured by plugins
    @SuppressWarnings("unchecked")
    public static final TriConsumer<NetWorkUser, NMSPacketEvent, Object> EDIT_BOOK = (user, event, packet) -> {
        try {
            if (!Config.filterBook()) return;
            FontManager manager = CraftEngine.instance().fontManager();
            if (!manager.isDefaultFontInUse()) return;
            // check bypass
            if (((BukkitServerPlayer) user).hasPermission(FontManager.BYPASS_BOOK)) {
                return;
            }

            boolean changed = false;

            List<String> pages = (List<String>) NetworkReflections.field$ServerboundEditBookPacket$pages.get(packet);
            List<String> newPages = new ArrayList<>(pages.size());
            Optional<String> title = (Optional<String>) NetworkReflections.field$ServerboundEditBookPacket$title.get(packet);
            Optional<String> newTitle;

            if (title.isPresent()) {
                String titleStr = title.get();
                Pair<Boolean, String> result = processClientString(titleStr, manager);
                newTitle = Optional.of(result.right());
                if (result.left()) {
                    changed = true;
                }
            } else {
                newTitle = Optional.empty();
            }

            for (String page : pages) {
                Pair<Boolean, String> result = processClientString(page, manager);
                newPages.add(result.right());
                if (result.left()) {
                    changed = true;
                }
            }

            if (changed) {
                Object newPacket = NetworkReflections.constructor$ServerboundEditBookPacket.newInstance(
                        NetworkReflections.field$ServerboundEditBookPacket$slot.get(packet),
                        newPages,
                        newTitle
                );
                event.replacePacket(newPacket);
            }
        } catch (Exception e) {
            CraftEngine.instance().logger().warn("Failed to handle ServerboundEditBookPacket", e);
        }
    };

    private static Pair<Boolean, String> processClientString(String original, FontManager manager) {
        if (original.isEmpty()) {
            return Pair.of(false, original);
        }
        int[] codepoints = CharacterUtils.charsToCodePoints(original.toCharArray());
        int[] newCodepoints = new int[codepoints.length];
        boolean hasIllegal = false;
        for (int i = 0; i < codepoints.length; i++) {
            int codepoint = codepoints[i];
            if (manager.isIllegalCodepoint(codepoint)) {
                newCodepoints[i] = '*';
                hasIllegal = true;
            } else {
                newCodepoints[i] = codepoint;
            }
        }
        return hasIllegal ? Pair.of(true, new String(newCodepoints, 0, newCodepoints.length)) : Pair.of(false, original);
    }

    public static final TriConsumer<NetWorkUser, NMSPacketEvent, Object> CUSTOM_PAYLOAD = (user, event, packet) -> {
        try {
            if (!VersionHelper.isOrAbove1_20_5()) return;
            Object payload = NetworkReflections.field$ServerboundCustomPayloadPacket$payload.get(packet);
            if (NetworkReflections.clazz$DiscardedPayload.isInstance(payload)) {
                Payload discardedPayload = DiscardedPayload.from(payload);
                if (discardedPayload == null || !discardedPayload.channel().equals(NetworkManager.MOD_CHANNEL_KEY))
                    return;
                FriendlyByteBuf buf = discardedPayload.toBuffer();
                NetWorkDataTypes<?> dataType = NetWorkDataTypes.readType(buf);
                if (dataType == NetWorkDataTypes.CLIENT_CUSTOM_BLOCK) {
                    int clientBlockRegistrySize = dataType.as(Integer.class).decode(buf);
                    int serverBlockRegistrySize = RegistryUtils.currentBlockRegistrySize();
                    if (clientBlockRegistrySize != serverBlockRegistrySize) {
                        user.kick(Component.translatable(
                                "disconnect.craftengine.block_registry_mismatch",
                                TranslationArgument.numeric(clientBlockRegistrySize),
                                TranslationArgument.numeric(serverBlockRegistrySize)
                        ));
                        return;
                    }
                    user.setClientModState(true);
                } else if (dataType == NetWorkDataTypes.CANCEL_BLOCK_UPDATE) {
                    if (!VersionHelper.isOrAbove1_20_2()) return;
                    if (dataType.as(Boolean.class).decode(buf)) {
                        FriendlyByteBuf bufPayload = new FriendlyByteBuf(Unpooled.buffer());
                        dataType.writeType(bufPayload);
                        dataType.as(Boolean.class).encode(bufPayload, true);
                        user.sendCustomPayload(NetworkManager.MOD_CHANNEL_KEY, bufPayload.array());
                    }
                }
            }
        } catch (Exception e) {
            CraftEngine.instance().logger().warn("Failed to handle ServerboundCustomPayloadPacket", e);
        }
    };

    @SuppressWarnings("unchecked")
    public static final BiConsumer<NetWorkUser, ByteBufPacketEvent> SET_ENTITY_DATA = (user, event) -> {
        try {
            SeatEntity seat = ((BukkitServerPlayer)user).seat();
            if (seat != null) {
                seat.handleSetEntityData(user, event);
            }
            FriendlyByteBuf buf = event.getBuffer();
            int id = buf.readVarInt();
            EntityPacketHandler handler = user.entityPacketHandlers().get(id);
            if (handler != null) {
                handler.handleSetEntityData(user, event);
                return;
            }
            if (Config.interceptEntityName()) {
                boolean isChanged = false;
                List<Object> packedItems = FastNMS.INSTANCE.method$ClientboundSetEntityDataPacket$unpack(buf);
                for (int i = 0; i < packedItems.size(); i++) {
                    Object packedItem = packedItems.get(i);
                    int entityDataId = FastNMS.INSTANCE.field$SynchedEntityData$DataValue$id(packedItem);
                    if (entityDataId == EntityDataUtils.CUSTOM_NAME_DATA_ID) {
                        Optional<Object> optionalTextComponent = (Optional<Object>) FastNMS.INSTANCE.field$SynchedEntityData$DataValue$value(packedItem);
                        if (optionalTextComponent.isPresent()) {
                            Object textComponent = optionalTextComponent.get();
                            String json = ComponentUtils.minecraftToJson(textComponent);
                            Map<String, Component> tokens = CraftEngine.instance().fontManager().matchTags(json);
                            if (!tokens.isEmpty()) {
                                Component component = AdventureHelper.jsonToComponent(json);
                                for (Map.Entry<String, Component> token : tokens.entrySet()) {
                                    component = component.replaceText(b -> b.matchLiteral(token.getKey()).replacement(token.getValue()));
                                }
                                Object serializer = FastNMS.INSTANCE.field$SynchedEntityData$DataValue$serializer(packedItem);
                                packedItems.set(i, FastNMS.INSTANCE.constructor$SynchedEntityData$DataValue(entityDataId, serializer, Optional.of(ComponentUtils.adventureToMinecraft(component))));
                                isChanged = true;
                                break;
                            }
                        }
                    }
                }
                if (isChanged) {
                    event.setChanged(true);
                    buf.clear();
                    buf.writeVarInt(event.packetID());
                    buf.writeVarInt(id);
                    FastNMS.INSTANCE.method$ClientboundSetEntityDataPacket$pack(packedItems, buf);
                }
            }
        } catch (Exception e) {
            CraftEngine.instance().logger().warn("Failed to handle ClientboundSetEntityDataPacket", e);
        }
    };

    public static final BiConsumer<NetWorkUser, ByteBufPacketEvent> SET_SCORE_1_20_3 = (user, event) -> {
        try {
            if (!Config.interceptSetScore()) return;
            boolean isChanged = false;
            FriendlyByteBuf buf = event.getBuffer();
            String owner = buf.readUtf();
            String objectiveName = buf.readUtf();
            int score = buf.readVarInt();
            boolean hasDisplay = buf.readBoolean();
            Tag displayName = null;
            if (hasDisplay) {
                displayName = buf.readNbt(false);
            }
            outside:
            if (displayName != null) {
                Map<String, Component> tokens = CraftEngine.instance().fontManager().matchTags(displayName.getAsString());
                if (tokens.isEmpty()) break outside;
                Component component = AdventureHelper.tagToComponent(displayName);
                for (Map.Entry<String, Component> token : tokens.entrySet()) {
                    component = component.replaceText(b -> b.matchLiteral(token.getKey()).replacement(token.getValue()));
                }
                displayName = AdventureHelper.componentToTag(component);
                isChanged = true;
            }
            boolean hasNumberFormat = buf.readBoolean();
            int format = -1;
            Tag style = null;
            Tag fixed = null;
            if (hasNumberFormat) {
                format = buf.readVarInt();
                if (format == 0) {
                    if (displayName == null) return;
                } else if (format == 1) {
                    if (displayName == null) return;
                    style = buf.readNbt(false);
                } else if (format == 2) {
                    fixed = buf.readNbt(false);
                    if (fixed == null) return;
                    Map<String, Component> tokens = CraftEngine.instance().fontManager().matchTags(fixed.getAsString());
                    if (tokens.isEmpty() && !isChanged) return;
                    if (!tokens.isEmpty()) {
                        Component component = AdventureHelper.tagToComponent(fixed);
                        for (Map.Entry<String, Component> token : tokens.entrySet()) {
                            component = component.replaceText(b -> b.matchLiteral(token.getKey()).replacement(token.getValue()));
                        }
                        fixed = AdventureHelper.componentToTag(component);
                        isChanged = true;
                    }
                }
            }
            if (isChanged) {
                event.setChanged(true);
                buf.clear();
                buf.writeVarInt(event.packetID());
                buf.writeUtf(owner);
                buf.writeUtf(objectiveName);
                buf.writeVarInt(score);
                if (hasDisplay) {
                    buf.writeBoolean(true);
                    buf.writeNbt(displayName, false);
                } else {
                    buf.writeBoolean(false);
                }
                if (hasNumberFormat) {
                    buf.writeBoolean(true);
                    buf.writeVarInt(format);
                    if (format == 1) {
                        buf.writeNbt(style, false);
                    } else if (format == 2) {
                        buf.writeNbt(fixed, false);
                    }
                } else {
                    buf.writeBoolean(false);
                }
            }
        } catch (Exception e) {
            CraftEngine.instance().logger().warn("Failed to handle ClientboundSetScorePacket", e);
        }
    };

    public static final BiConsumer<NetWorkUser, ByteBufPacketEvent> CONTAINER_SET_CONTENT = (user, event) -> {
        try {
            if (!(user instanceof BukkitServerPlayer serverPlayer)) return;
            FriendlyByteBuf buf = event.getBuffer();
            int containerId = buf.readContainerId();
            int stateId = buf.readVarInt();
            int listSize = buf.readVarInt();
            List<ItemStack> items = new ArrayList<>(listSize);
            boolean changed = false;
            Object friendlyBuf = FastNMS.INSTANCE.constructor$FriendlyByteBuf(buf);
            for (int i = 0; i < listSize; i++) {
                ItemStack itemStack = FastNMS.INSTANCE.method$FriendlyByteBuf$readItem(friendlyBuf);
                Optional<ItemStack> optional = BukkitItemManager.instance().s2c(itemStack, serverPlayer);
                if (optional.isPresent()) {
                    items.add(optional.get());
                    changed = true;
                } else {
                    items.add(itemStack);
                }
            }
            ItemStack carriedItem = FastNMS.INSTANCE.method$FriendlyByteBuf$readItem(friendlyBuf);
            ItemStack newCarriedItem = carriedItem;
            Optional<ItemStack> optional = BukkitItemManager.instance().s2c(carriedItem, serverPlayer);
            if (optional.isPresent()) {
                changed = true;
                newCarriedItem = optional.get();
            }
            if (!changed) return;
            event.setChanged(true);
            buf.clear();
            buf.writeVarInt(event.packetID());
            buf.writeContainerId(containerId);
            buf.writeVarInt(stateId);
            buf.writeVarInt(listSize);
            Object newFriendlyBuf = FastNMS.INSTANCE.constructor$FriendlyByteBuf(buf);
            for (ItemStack itemStack : items) {
                FastNMS.INSTANCE.method$FriendlyByteBuf$writeItem(newFriendlyBuf, itemStack);
            }
            FastNMS.INSTANCE.method$FriendlyByteBuf$writeItem(newFriendlyBuf, newCarriedItem);
        } catch (Exception e) {
            CraftEngine.instance().logger().warn("Failed to handle ClientboundContainerSetContentPacket", e);
        }
    };

    public static final BiConsumer<NetWorkUser, ByteBufPacketEvent> CONTAINER_SET_SLOT = (user, event) -> {
        try {
            if (!(user instanceof BukkitServerPlayer serverPlayer)) return;
            FriendlyByteBuf buf = event.getBuffer();
            int containerId = buf.readContainerId();
            int stateId = buf.readVarInt();
            int slot = buf.readShort();
            Object friendlyBuf = FastNMS.INSTANCE.constructor$FriendlyByteBuf(buf);
            ItemStack itemStack = FastNMS.INSTANCE.method$FriendlyByteBuf$readItem(friendlyBuf);
            BukkitItemManager.instance().s2c(itemStack, serverPlayer).ifPresent((newItemStack) -> {
                event.setChanged(true);
                buf.clear();
                buf.writeVarInt(event.packetID());
                buf.writeContainerId(containerId);
                buf.writeVarInt(stateId);
                buf.writeShort(slot);
                Object newFriendlyBuf = FastNMS.INSTANCE.constructor$FriendlyByteBuf(buf);
                FastNMS.INSTANCE.method$FriendlyByteBuf$writeItem(newFriendlyBuf, newItemStack);
            });
        } catch (Exception e) {
            CraftEngine.instance().logger().warn("Failed to handle ClientboundContainerSetSlotPacket", e);
        }
    };

    public static final BiConsumer<NetWorkUser, ByteBufPacketEvent> SET_CURSOR_ITEM = (user, event) -> {
        try {
            if (!(user instanceof BukkitServerPlayer serverPlayer)) return;
            FriendlyByteBuf buf = event.getBuffer();
            Object friendlyBuf = FastNMS.INSTANCE.constructor$FriendlyByteBuf(buf);
            ItemStack itemStack = FastNMS.INSTANCE.method$FriendlyByteBuf$readItem(friendlyBuf);
            BukkitItemManager.instance().s2c(itemStack, serverPlayer).ifPresent((newItemStack) -> {
                event.setChanged(true);
                buf.clear();
                buf.writeVarInt(event.packetID());
                Object newFriendlyBuf = FastNMS.INSTANCE.constructor$FriendlyByteBuf(buf);
                FastNMS.INSTANCE.method$FriendlyByteBuf$writeItem(newFriendlyBuf, newItemStack);
            });
        } catch (Exception e) {
            CraftEngine.instance().logger().warn("Failed to handle ClientboundSetCursorItemPacket", e);
        }
    };

    public static final BiConsumer<NetWorkUser, ByteBufPacketEvent> SET_EQUIPMENT = (user, event) -> {
        try {
            if (!(user instanceof BukkitServerPlayer serverPlayer)) return;
            FriendlyByteBuf buf = event.getBuffer();
            boolean changed = false;
            Object friendlyBuf = FastNMS.INSTANCE.constructor$FriendlyByteBuf(buf);
            int entity = buf.readVarInt();
            List<com.mojang.datafixers.util.Pair<Object, ItemStack>> slots = Lists.newArrayList();
            int slotMask;
            do {
                slotMask = buf.readByte();
                Object equipmentSlot = CoreReflections.instance$EquipmentSlot$values[slotMask & 127];
                ItemStack itemStack = FastNMS.INSTANCE.method$FriendlyByteBuf$readItem(friendlyBuf);
                Optional<ItemStack> optional = BukkitItemManager.instance().s2c(itemStack, serverPlayer);
                if (optional.isPresent()) {
                    changed = true;
                    itemStack = optional.get();
                }
                slots.add(com.mojang.datafixers.util.Pair.of(equipmentSlot, itemStack));
            } while ((slotMask & -128) != 0);
            if (changed) {
                event.setChanged(true);
                buf.clear();
                buf.writeVarInt(event.packetID());
                buf.writeVarInt(entity);
                int i = slots.size();
                Object newFriendlyBuf = FastNMS.INSTANCE.constructor$FriendlyByteBuf(buf);
                for (int j = 0; j < i; ++j) {
                    com.mojang.datafixers.util.Pair<Object, ItemStack> pair = slots.get(j);
                    Enum<?> equipmentSlot = (Enum<?>) pair.getFirst();
                    boolean bl = j != i - 1;
                    int k = equipmentSlot.ordinal();
                    buf.writeByte(bl ? k | -128 : k);
                    FastNMS.INSTANCE.method$FriendlyByteBuf$writeItem(newFriendlyBuf, pair.getSecond());
                }
            }
        } catch (Exception e) {
            CraftEngine.instance().logger().warn("Failed to handle ClientboundSetEquipmentPacket", e);
        }
    };

    public static final BiConsumer<NetWorkUser, ByteBufPacketEvent> SET_PLAYER_INVENTORY_1_21_2 = (user, event) -> {
        try {
            if (!(user instanceof BukkitServerPlayer serverPlayer)) return;
            FriendlyByteBuf buf = event.getBuffer();
            int slot = buf.readVarInt();
            Object friendlyBuf = FastNMS.INSTANCE.constructor$FriendlyByteBuf(buf);
            ItemStack itemStack = FastNMS.INSTANCE.method$FriendlyByteBuf$readItem(friendlyBuf);
            BukkitItemManager.instance().s2c(itemStack, serverPlayer).ifPresent((newItemStack) -> {
                event.setChanged(true);
                buf.clear();
                buf.writeVarInt(event.packetID());
                buf.writeVarInt(slot);
                Object newFriendlyBuf = FastNMS.INSTANCE.constructor$FriendlyByteBuf(buf);
                FastNMS.INSTANCE.method$FriendlyByteBuf$writeItem(newFriendlyBuf, newItemStack);
            });
        } catch (Exception e) {
            CraftEngine.instance().logger().warn("Failed to handle ClientboundSetPlayerInventoryPacket", e);
        }
    };

    public static final BiConsumer<NetWorkUser, ByteBufPacketEvent> SET_CREATIVE_MODE_SLOT = (user, event) -> {
        try {
            FriendlyByteBuf buf = event.getBuffer();
            Object friendlyBuf = FastNMS.INSTANCE.constructor$FriendlyByteBuf(buf);
            short slotNum = buf.readShort();
            ItemStack itemStack = VersionHelper.isOrAbove1_20_5() ?
                    FastNMS.INSTANCE.method$FriendlyByteBuf$readUntrustedItem(friendlyBuf) : FastNMS.INSTANCE.method$FriendlyByteBuf$readItem(friendlyBuf);
            BukkitItemManager.instance().c2s(itemStack).ifPresent((newItemStack) -> {
                event.setChanged(true);
                buf.clear();
                buf.writeVarInt(event.packetID());
                buf.writeShort(slotNum);
                Object newFriendlyBuf = FastNMS.INSTANCE.constructor$FriendlyByteBuf(buf);
                if (VersionHelper.isOrAbove1_20_5()) {
                    FastNMS.INSTANCE.method$FriendlyByteBuf$writeUntrustedItem(newFriendlyBuf, newItemStack);
                } else {
                    FastNMS.INSTANCE.method$FriendlyByteBuf$writeItem(newFriendlyBuf, newItemStack);
                }
            });
        } catch (Exception e) {
            CraftEngine.instance().logger().warn("Failed to handle ServerboundSetCreativeModeSlotPacket", e);
        }
    };

    public static final BiConsumer<NetWorkUser, ByteBufPacketEvent> CONTAINER_CLICK_1_20 = (user, event) -> {
        try {
            if (VersionHelper.isOrAbove1_21_5()) return; // 1.21.5+需要其他办法解决同步问题
            FriendlyByteBuf buf = event.getBuffer();
            boolean changed = false;
            Object friendlyBuf = FastNMS.INSTANCE.constructor$FriendlyByteBuf(buf);
            int containerId = buf.readContainerId();
            int stateId = buf.readVarInt();
            short slotNum = buf.readShort();
            byte buttonNum = buf.readByte();
            int clickType = buf.readVarInt();
            int i = buf.readVarInt();
            Int2ObjectMap<ItemStack> changedSlots = new Int2ObjectOpenHashMap<>(i);
            for (int j = 0; j < i; ++j) {
                int k = buf.readShort();
                ItemStack itemStack = FastNMS.INSTANCE.method$FriendlyByteBuf$readItem(friendlyBuf);
                Optional<ItemStack> optional = BukkitItemManager.instance().c2s(itemStack);
                if (optional.isPresent()) {
                    changed = true;
                    itemStack = optional.get();
                }
                changedSlots.put(k, itemStack);
            }
            ItemStack carriedItem = FastNMS.INSTANCE.method$FriendlyByteBuf$readItem(friendlyBuf);
            Optional<ItemStack> optional = BukkitItemManager.instance().c2s(carriedItem);
            if (optional.isPresent()) {
                changed = true;
                carriedItem = optional.get();
            }
            if (changed) {
                event.setChanged(true);
                buf.clear();
                buf.writeVarInt(event.packetID());
                buf.writeContainerId(containerId);
                buf.writeVarInt(stateId);
                buf.writeShort(slotNum);
                buf.writeByte(buttonNum);
                buf.writeVarInt(clickType);
                buf.writeVarInt(changedSlots.size());
                Object newFriendlyBuf = FastNMS.INSTANCE.constructor$FriendlyByteBuf(buf);
                changedSlots.forEach((k, v) -> {
                    buf.writeShort(k);
                    FastNMS.INSTANCE.method$FriendlyByteBuf$writeItem(newFriendlyBuf, v);
                });
                FastNMS.INSTANCE.method$FriendlyByteBuf$writeItem(newFriendlyBuf, carriedItem);
            }
        } catch (Exception e) {
            CraftEngine.instance().logger().warn("Failed to handle ServerboundContainerClickPacket", e);
        }
    };

    public static final TriConsumer<NetWorkUser, NMSPacketEvent, Object> RESOURCE_PACK_PUSH = (user, event, packet) -> {
        try {
            if (!VersionHelper.isOrAbove1_20_2()) return;
            // we should only handle fake urls
            String url = FastNMS.INSTANCE.field$ClientboundResourcePackPushPacket$url(packet);
            if (!url.equals(BukkitPackManager.FAKE_URL)) {
                return;
            }

            event.setCancelled(true);
            UUID packUUID = FastNMS.INSTANCE.field$ClientboundResourcePackPushPacket$uuid(packet);
            ResourcePackHost host = CraftEngine.instance().packManager().resourcePackHost();
            host.requestResourcePackDownloadLink(user.uuid()).thenAccept(dataList -> {
                if (dataList.isEmpty()) {
                    user.simulatePacket(FastNMS.INSTANCE.constructor$ServerboundResourcePackPacket$SUCCESSFULLY_LOADED(packUUID));
                    return;
                }
                for (ResourcePackDownloadData data : dataList) {
                    Object newPacket = ResourcePackUtils.createPacket(data.uuid(), data.url(), data.sha1());
                    user.sendPacket(newPacket, true);
                    user.addResourcePackUUID(data.uuid());
                }
            }).exceptionally(throwable -> {
                CraftEngine.instance().logger().warn("Failed to handle ClientboundResourcePackPushPacket", throwable);
                user.simulatePacket(FastNMS.INSTANCE.constructor$ServerboundResourcePackPacket$SUCCESSFULLY_LOADED(packUUID));
                return null;
            });
        } catch (Exception e) {
            CraftEngine.instance().logger().warn("Failed to handle ClientboundResourcePackPushPacket", e);
        }
    };

    public static final TriConsumer<NetWorkUser, NMSPacketEvent, Object> HANDSHAKE_C2S = (user, event, packet) -> {
        try {
            if (BukkitNetworkManager.hasViaVersion()) return;
            int protocolVersion = NetworkReflections.field$ClientIntentionPacket$protocolVersion.getInt(packet);
            user.setProtocolVersion(protocolVersion);
        } catch (Exception e) {
            CraftEngine.instance().logger().warn("Failed to handle ClientIntentionPacket", e);
        }
    };

    public static final TriConsumer<NetWorkUser, NMSPacketEvent, Object> LOGIN_ACKNOWLEDGED = (user, event, packet) -> {
        try {
            if (BukkitNetworkManager.hasViaVersion()) {
                user.setProtocolVersion(CraftEngine.instance().compatibilityManager().getPlayerProtocolVersion(user.uuid()));
            }
        } catch (Exception e) {
            CraftEngine.instance().logger().warn("Failed to handle ServerboundLoginAcknowledgedPacket", e);
        }
    };

    public static final TriConsumer<NetWorkUser, NMSPacketEvent, Object> RESOURCE_PACK_RESPONSE = (user, event, packet) -> {
        try {
            if (user.sentResourcePack() || !Config.sendPackOnJoin() || !Config.kickOnDeclined()) return;
            Object action = NetworkReflections.field$ServerboundResourcePackPacket$action.get(packet);
            if (action == null) return;
            if (action == NetworkReflections.instance$ServerboundResourcePackPacket$Action$DECLINED
                    || action == NetworkReflections.instance$ServerboundResourcePackPacket$Action$FAILED_DOWNLOAD) {
                Object kickPacket = NetworkReflections.constructor$ClientboundDisconnectPacket.newInstance(
                        ComponentUtils.adventureToMinecraft(Component.translatable("multiplayer.requiredTexturePrompt.disconnect")));
                user.sendPacket(kickPacket, true);
                user.nettyChannel().disconnect();
                return;
            }
            if (action == NetworkReflections.instance$ServerboundResourcePackPacket$Action$SUCCESSFULLY_LOADED) {
                user.setSentResourcePack(true);
            }
        } catch (Exception e) {
            CraftEngine.instance().logger().warn("Failed to handle ServerboundResourcePackPacket", e);
        }
    };

    public static final TriConsumer<NetWorkUser, NMSPacketEvent, Object> ENTITY_EVENT = (user, event, packet) -> {
        try {
            Object player = user.serverPlayer();
            if (player == null) return;
            int entityId = NetworkReflections.field$ClientboundEntityEventPacket$entityId.getInt(packet);
            if (entityId != FastNMS.INSTANCE.method$Entity$getId(player)) return;
            byte eventId = NetworkReflections.field$ClientboundEntityEventPacket$eventId.getByte(packet);
            if (eventId >= 24 && eventId <= 28) {
                CraftEngine.instance().fontManager().refreshEmojiSuggestions(user.uuid());
            }
        } catch (Exception e) {
            CraftEngine.instance().logger().warn("Failed to handle ClientboundEntityEventPacket", e);
        }
    };

    public static final TriConsumer<NetWorkUser, NMSPacketEvent, Object> MOVE_POS_ENTITY = (user, event, packet) -> {
        try {
            int entityId = ProtectedFieldVisitor.get().field$ClientboundMoveEntityPacket$entityId(packet);
            if (BukkitFurnitureManager.instance().isFurnitureRealEntity(entityId)) {
                event.setCancelled(true);
            }
        } catch (Exception e) {
            CraftEngine.instance().logger().warn("Failed to handle ClientboundMoveEntityPacket", e);
        }
    };

    public static final TriConsumer<NetWorkUser, NMSPacketEvent, Object> MOVE_POS_AND_ROTATE_ENTITY = (user, event, packet) -> {
        try {
            int entityId = ProtectedFieldVisitor.get().field$ClientboundMoveEntityPacket$entityId(packet);
            EntityPacketHandler handler = user.entityPacketHandlers().get(entityId);
            if (handler != null) {
                handler.handleMoveAndRotate(user, event, packet);
            }
        } catch (Exception e) {
            CraftEngine.instance().logger().warn("Failed to handle ClientboundMoveEntityPacket$PosRot", e);
        }
    };

    public static final TriConsumer<NetWorkUser, NMSPacketEvent, Object> SET_EQUIPMENT_NMS = (user, event, packet) -> {
        try {
            int entityId = (int) NetworkReflections.method$ClientboundSetEquipmentPacket$getEntity.invoke(packet);
            EntityPacketHandler handler = user.entityPacketHandlers().get(entityId);
            if (handler != null) {
                handler.handleSetEquipment(user, event, packet);
            }
        } catch (Exception e) {
            CraftEngine.instance().logger().warn("Failed to handle ClientboundSetEquipmentPacket", e);
        }
    };

    public static final TriConsumer<NetWorkUser, NMSPacketEvent, Object> SET_CONTAINER_SLOT = (user, event, packet) -> {
        try {
            SeatEntity seat = ((BukkitServerPlayer) user).seat();
            if (seat != null) seat.handleContainerSetSlot(user, event, packet);
        } catch (Exception e) {
            CraftEngine.instance().logger().warn("Failed to handle ClientboundSetEquipmentPacket", e);
        }
    };
}
