package net.momirealms.craftengine.core.entity.seat;

import net.momirealms.craftengine.core.entity.AbstractEntity;
import net.momirealms.craftengine.core.entity.Entity;
import net.momirealms.craftengine.core.entity.player.Player;

public abstract class SeatEntity extends AbstractEntity {

	public abstract void sync(Player to);

	public abstract void dismount(Player from);

	public abstract void remove();
}
