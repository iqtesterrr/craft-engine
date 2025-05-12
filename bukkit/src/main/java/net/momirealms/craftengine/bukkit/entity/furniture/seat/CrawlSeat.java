package net.momirealms.craftengine.bukkit.entity.furniture.seat;

import net.momirealms.craftengine.bukkit.api.BukkitAdaptors;
import net.momirealms.craftengine.bukkit.entity.data.ShulkerData;
import net.momirealms.craftengine.bukkit.entity.furniture.BukkitFurnitureManager;
import net.momirealms.craftengine.bukkit.entity.furniture.LoadedFurniture;
import net.momirealms.craftengine.bukkit.nms.FastNMS;
import net.momirealms.craftengine.bukkit.plugin.BukkitCraftEngine;
import net.momirealms.craftengine.bukkit.plugin.user.BukkitServerPlayer;
import net.momirealms.craftengine.bukkit.util.EntityUtils;
import net.momirealms.craftengine.bukkit.util.LegacyAttributeUtils;
import net.momirealms.craftengine.bukkit.util.Reflections;
import net.momirealms.craftengine.core.entity.Entity;
import net.momirealms.craftengine.core.entity.furniture.AbstractSeat;
import net.momirealms.craftengine.core.entity.furniture.Furniture;
import net.momirealms.craftengine.core.entity.furniture.Seat;
import net.momirealms.craftengine.core.entity.furniture.SeatFactory;
import net.momirealms.craftengine.core.entity.player.Player;
import net.momirealms.craftengine.core.util.Key;
import net.momirealms.craftengine.core.util.MiscUtils;
import net.momirealms.craftengine.core.util.VersionHelper;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Pose;
import org.bukkit.persistence.PersistentDataType;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class CrawlSeat extends AbstractSeat {
	public static final SeatFactory FACTORY = new Factory();
	private static final List<Object> visualData = new ArrayList<>();
	private final boolean limitPlayerRotation;

	static {
		ShulkerData.NoGravity.addEntityDataIfNotDefaultValue(true, visualData);
		ShulkerData.Silent.addEntityDataIfNotDefaultValue(true, visualData);
		ShulkerData.MobFlags.addEntityDataIfNotDefaultValue((byte) 0x01, visualData);
		ShulkerData.SharedFlags.addEntityDataIfNotDefaultValue((byte) 0x20, visualData);
	}

	public CrawlSeat(Vector3f offset, float yaw, boolean limitPlayerRotation) {
		super(offset, yaw);
		this.limitPlayerRotation = limitPlayerRotation;
	}

	@Override
	public Entity spawn(Player player, Furniture furniture) {
		return spawn((org.bukkit.entity.Player) player.platformPlayer(), furniture);
	}

	public Entity spawn(org.bukkit.entity.Player player, Furniture furniture) {
		Location location = ((LoadedFurniture)furniture).calculateSeatLocation(this);

		int visualId = Reflections.instance$Entity$ENTITY_COUNTER.incrementAndGet();
		List<Object> packets = new ArrayList<>();
		packets.add(FastNMS.INSTANCE.constructor$ClientboundAddEntityPacket(visualId, UUID.randomUUID(), location.getX(), location.getY(), location.getZ(), location.getPitch(), location.getYaw(),
				Reflections.instance$EntityType$SHULKER, 0, Reflections.instance$Vec3$Zero, 0));
		packets.add(FastNMS.INSTANCE.constructor$ClientboundSetEntityDataPacket(visualId, List.copyOf(visualData)));
		Object bundle = FastNMS.INSTANCE.constructor$ClientboundBundlePacket(packets);
		BukkitAdaptors.adapt(player).sendPacket(bundle, true);

		org.bukkit.entity.Entity seatEntity = this.limitPlayerRotation ?
				EntityUtils.spawnEntity(player.getWorld(), VersionHelper.isOrAbove1_20_2() ? location.subtract(0,0.9875,0) : location.subtract(0,0.990625,0), EntityType.ARMOR_STAND, entity -> {
					ArmorStand armorStand = (ArmorStand) entity;
					if (VersionHelper.isOrAbove1_21_3()) {
						Objects.requireNonNull(armorStand.getAttribute(Attribute.MAX_HEALTH)).setBaseValue(0.01);
					} else {
						LegacyAttributeUtils.setMaxHealth(armorStand);
					}
					armorStand.setSmall(true);
					armorStand.setInvisible(true);
					armorStand.setSilent(true);
					armorStand.setInvulnerable(true);
					armorStand.setArms(false);
					armorStand.setCanTick(false);
					armorStand.setAI(false);
					armorStand.setGravity(false);
					armorStand.setPersistent(false);
					armorStand.getPersistentDataContainer().set(BukkitFurnitureManager.FURNITURE_SEAT_BASE_ENTITY_KEY, PersistentDataType.INTEGER, furniture.baseEntityId());
					armorStand.getPersistentDataContainer().set(BukkitFurnitureManager.FURNITURE_SEAT_VECTOR_3F_KEY, PersistentDataType.STRING, this.offset().x + ", " + this.offset().y + ", " + this.offset().z);
				}) :
				EntityUtils.spawnEntity(player.getWorld(), VersionHelper.isOrAbove1_20_2() ? location : location.subtract(0,0.25,0), EntityType.ITEM_DISPLAY, entity -> {
					ItemDisplay itemDisplay = (ItemDisplay) entity;
					itemDisplay.setPersistent(false);
					itemDisplay.getPersistentDataContainer().set(BukkitFurnitureManager.FURNITURE_SEAT_BASE_ENTITY_KEY, PersistentDataType.INTEGER, furniture.baseEntityId());
					itemDisplay.getPersistentDataContainer().set(BukkitFurnitureManager.FURNITURE_SEAT_VECTOR_3F_KEY, PersistentDataType.STRING, this.offset().x + ", " + this.offset().y + ", " + this.offset().z);
				});
		seatEntity.addPassenger(player);
		// Todo 调查一下，位置y的变化是为了什么，可能是为了让玩家做下去之后的位置是seatlocation的位置
		BukkitCraftEngine.instance().scheduler().sync().runLater(() -> player.setPose(Pose.SWIMMING, true),
				1, location.getWorld(), location.getBlockX() >> 4, location.getBlockZ() >> 4);

		return new CrawlEntity(seatEntity, visualId);
		// Todo 检测版本实现潜影贝缩小 + 找到合适的位置保持玩家的姿势 + 潜影贝骑乘展示实体
	}

	private static class CrawlEntity extends BukkitSeatEntity {
		private final int visual;

		public CrawlEntity(org.bukkit.entity.Entity entity, int visual) {
			super(entity);
			this.visual = visual;
		}

		@Override
		public void sync(Player to) {
			org.bukkit.entity.Entity entity = this.literalObject();
			for (org.bukkit.entity.Entity passenger : entity.getPassengers()) {
				if (passenger instanceof org.bukkit.entity.Player) {
					try {
						Object serverPlayer = FastNMS.INSTANCE.method$CraftEntity$getHandle(passenger);
						Reflections.method$Entity$refreshEntityData.invoke(serverPlayer, to.serverPlayer());
					} catch (Exception e) {
						BukkitCraftEngine.instance().logger().warn("Failed to sync player pose", e);
					}
				}
			}
		}

		@Override
		public void dismount(Player from) {
			super.dismount(from);
			((org.bukkit.entity.Player) from.platformPlayer()).setPose(Pose.STANDING, false);
			try {
				@SuppressWarnings("Confusing primitive array argument to var-arg method PrimitiveArrayArgumentToVariableArgMethod")
				Object packet = Reflections.constructor$ClientboundRemoveEntitiesPacket.newInstance(new int[]{visual});
				from.sendPacket(packet, true);
			} catch (Exception e) {
				BukkitCraftEngine.instance().logger().warn("Failed to send remove entity packet", e);
			}
		}

		@Override
		public void remove() {
			org.bukkit.entity.Entity entity = this.literalObject();
			if (entity == null) return;

			for (org.bukkit.entity.Entity passenger : entity.getPassengers()) {
				entity.removePassenger(passenger);
				if (passenger instanceof org.bukkit.entity.Player p) {
					dismount(BukkitAdaptors.adapt(p));
				}
			}
			entity.remove();
		}

		@Override
		public Key type() {
			return Key.of("craftengine", "crawl");
		}
	}

	public static class Factory implements SeatFactory {

		@Override
		public Seat create(List<String> args) {
			if (args.size() == 1) return new CrawlSeat(MiscUtils.getAsVector3f(args.get(0), "seats"), 0, false);
			return new CrawlSeat(MiscUtils.getAsVector3f(args.get(0), "seats"), Float.parseFloat(args.get(1)), true);
		}
	}
}