package net.momirealms.craftengine.bukkit.entity.furniture.seat;

import com.google.common.collect.ForwardingMultimap;
import com.google.common.collect.Multimap;
import net.momirealms.craftengine.bukkit.api.BukkitAdaptors;
import net.momirealms.craftengine.bukkit.entity.BukkitEntity;
import net.momirealms.craftengine.bukkit.entity.data.ShulkerData;
import net.momirealms.craftengine.bukkit.entity.furniture.BukkitFurnitureManager;
import net.momirealms.craftengine.bukkit.entity.furniture.LoadedFurniture;
import net.momirealms.craftengine.bukkit.nms.FastNMS;
import net.momirealms.craftengine.bukkit.plugin.BukkitCraftEngine;
import net.momirealms.craftengine.bukkit.util.EntityUtils;
import net.momirealms.craftengine.bukkit.util.LegacyAttributeUtils;
import net.momirealms.craftengine.bukkit.util.Reflections;
import net.momirealms.craftengine.core.entity.Entity;
import net.momirealms.craftengine.core.entity.furniture.AbstractSeat;
import net.momirealms.craftengine.core.entity.furniture.Furniture;
import net.momirealms.craftengine.core.entity.furniture.Seat;
import net.momirealms.craftengine.core.entity.furniture.SeatFactory;
import net.momirealms.craftengine.core.entity.player.Player;
import net.momirealms.craftengine.core.plugin.CraftEngine;
import net.momirealms.craftengine.core.util.Key;
import net.momirealms.craftengine.core.util.MiscUtils;
import net.momirealms.craftengine.core.util.VersionHelper;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Pose;
import org.bukkit.persistence.PersistentDataType;
import org.joml.Vector3f;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class LaySeat extends AbstractSeat {
	public static final SeatFactory FACTORY = new Factory();

	public LaySeat(Vector3f offset, float yaw) {
		super(offset, yaw);
	}

	@Override
	@SuppressWarnings("unchecked")
	public Entity spawn(Player serverPlayer, Furniture furniture) {
		Location location = ((LoadedFurniture)furniture).calculateSeatLocation(this);
		org.bukkit.entity.Player player = (org.bukkit.entity.Player) serverPlayer.platformPlayer();

		Object npc;
		try {
			Object server = Reflections.method$MinecraftServer$getServer.invoke(null);
			Object level = FastNMS.INSTANCE.field$CraftWorld$ServerLevel(player.getWorld());
			Object npcProfile = Reflections.constructor$GameProfile.newInstance(UUID.randomUUID(), player.getName());
			Object pProfile = Reflections.method$ServerPlayer$getGameProfile.invoke(serverPlayer.serverPlayer());
			Multimap<String, Object> properties = (Multimap<String, Object>) Reflections.method$GameProfile$getProperties.invoke(npcProfile);
			properties.putAll((Multimap<String, Object>) Reflections.method$GameProfile$getProperties.invoke(pProfile));
			Object information = Reflections.method$ServerPlayer$clientInformation.invoke(serverPlayer.serverPlayer());
			npc = Reflections.constructor$ServerPlayer.newInstance(server, level, npcProfile, information);
		} catch (Exception e) {
			CraftEngine.instance().logger().warn("Failed to create NPC", e);
		}

		return null;
	}

	private static class LayEntity extends BukkitSeatEntity {
		private final WeakReference<Object> npc;

		public LayEntity(org.bukkit.entity.Entity entity, org.bukkit.entity.Entity npc) {
			super(entity);
			this.npc = new WeakReference<>(npc);
		}

		@Override
		public void sync(Player to) {

		}

		@Override
		public void remove() {

		}

		@Override
		public Key type() {
			return Key.of("craftengine", "lay");
		}
	}

	public static class Factory implements SeatFactory {

		@Override
		public Seat create(List<String> arguments) {
			return null;
		}
	}
}
