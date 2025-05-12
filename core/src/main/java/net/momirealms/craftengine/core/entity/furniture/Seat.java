package net.momirealms.craftengine.core.entity.furniture;

import net.momirealms.craftengine.core.entity.Entity;
import net.momirealms.craftengine.core.entity.player.Player;
import org.joml.Vector3f;

public interface Seat {

    Entity spawn(Player player, Furniture furniture);

    Vector3f offset();

    float yaw();
}
