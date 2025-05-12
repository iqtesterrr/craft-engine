package net.momirealms.craftengine.bukkit.entity.furniture.seat;

import net.momirealms.craftengine.bukkit.api.BukkitAdaptors;
import net.momirealms.craftengine.bukkit.entity.furniture.BukkitFurnitureManager;
import net.momirealms.craftengine.bukkit.entity.furniture.LoadedFurniture;
import net.momirealms.craftengine.bukkit.util.EntityUtils;
import net.momirealms.craftengine.bukkit.util.LegacyAttributeUtils;
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
import org.bukkit.persistence.PersistentDataType;
import org.joml.Vector3f;

import java.util.List;
import java.util.Objects;

public class SitSeat extends AbstractSeat {
	public static final SeatFactory FACTORY = new Factory();
	private final boolean limitPlayerRotation;

	public SitSeat(Vector3f offset, float yaw, boolean limitPlayerRotation) {
		super(offset, yaw);
		this.limitPlayerRotation = limitPlayerRotation;
	}

	@Override
	public Entity spawn(Player player, Furniture furniture) {
		return spawn((org.bukkit.entity.Player) player.platformPlayer(), furniture);
	}

	public Entity spawn(org.bukkit.entity.Player player, Furniture furniture) {
		Location location = ((LoadedFurniture)furniture).calculateSeatLocation(this);
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
		return new SitEntity(seatEntity);
	}

	private static class SitEntity extends BukkitSeatEntity {

		public SitEntity(org.bukkit.entity.Entity entity) {
			super(entity);
		}

		@Override
		public void sync(Player to) {}

		@Override
		public void remove() {
			org.bukkit.entity.Entity entity = this.literalObject();
			if (entity == null) return;
			for (org.bukkit.entity.Entity passenger : entity.getPassengers()) {
				entity.removePassenger(passenger);
				if (passenger instanceof org.bukkit.entity.Player p) {
					BukkitAdaptors.adapt(p).setSeat(null);
				}
			}
			entity.remove();
		}

		@Override
		public Key type() {
			return Key.of("craftengine", "sit") ;
		}
	}

	public static class Factory implements SeatFactory {

		@Override
		public Seat create(List<String> args) {
			if (args.size() == 1) return new SitSeat(MiscUtils.getAsVector3f(args.get(0), "seats"), 0, false);
			return new SitSeat(MiscUtils.getAsVector3f(args.get(0), "seats"), Float.parseFloat(args.get(1)), true);
		}
	}
}