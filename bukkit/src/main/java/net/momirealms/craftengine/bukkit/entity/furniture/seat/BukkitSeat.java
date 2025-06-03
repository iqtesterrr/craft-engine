package net.momirealms.craftengine.bukkit.entity.furniture.seat;

import net.momirealms.craftengine.bukkit.entity.furniture.BukkitFurniture;
import net.momirealms.craftengine.bukkit.util.EntityUtils;
import net.momirealms.craftengine.core.entity.furniture.AbstractSeat;
import net.momirealms.craftengine.core.entity.furniture.Furniture;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.joml.Vector3f;

/**
 * Base implementation for Bukkit seat types providing common spawn utilities.
 */
public abstract class BukkitSeat extends AbstractSeat {

    protected final boolean limitPlayerRotation;

    protected BukkitSeat(Vector3f offset, float yaw, boolean limitPlayerRotation) {
        super(offset, yaw);
        this.limitPlayerRotation = limitPlayerRotation;
    }

    /**
     * Spawn the seat entity at the calculated seat location and add the player as passenger.
     */
    protected Entity spawnSeatEntity(Player player, Furniture furniture) {
        Location loc = ((BukkitFurniture) furniture).calculateSeatLocation(this);
        return spawnSeatEntity(player, furniture, loc);
    }

    /**
     * Spawn the seat entity at the given location and add the player as passenger.
     */
    protected Entity spawnSeatEntity(Player player, Furniture furniture, Location loc) {
        Entity seatEntity = EntityUtils.spawnSeatEntity(furniture, this, player.getWorld(), loc, limitPlayerRotation, null);
        seatEntity.addPassenger(player);
        return seatEntity;
    }
}
