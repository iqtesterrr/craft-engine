package net.momirealms.craftengine.bukkit.entity.furniture.seat;

import net.momirealms.craftengine.bukkit.world.BukkitWorld;
import net.momirealms.craftengine.core.entity.player.Player;
import net.momirealms.craftengine.core.entity.seat.SeatEntity;
import net.momirealms.craftengine.core.util.Direction;
import net.momirealms.craftengine.core.util.Key;
import net.momirealms.craftengine.core.world.World;
import org.bukkit.entity.Entity;

import java.lang.ref.WeakReference;

public abstract class BukkitSeatEntity extends SeatEntity {

	private final WeakReference<Entity> entity;

	public BukkitSeatEntity(Entity entity) {
		this.entity = new WeakReference<>(entity);
	}

	@Override
	public void dismount(Player from) {
		from.setSeat(null);
	}

	@Override
	public double x() {
		return literalObject().getLocation().getX();
	}

	@Override
	public double y() {
		return literalObject().getLocation().getY();
	}

	@Override
	public double z() {
		return literalObject().getLocation().getZ();
	}

	@Override
	public void tick() {
	}

	@Override
	public int entityID() {
		return literalObject().getEntityId();
	}

	@Override
	public float getXRot() {
		return literalObject().getLocation().getYaw();
	}

	@Override
	public float getYRot() {
		return literalObject().getLocation().getPitch();
	}

	@Override
	public World world() {
		return new BukkitWorld(literalObject().getWorld());
	}

	@Override
	public Direction getDirection() {
		return Direction.NORTH;
	}

	@Override
	public org.bukkit.entity.Entity literalObject() {
		return this.entity.get();
	}
}
