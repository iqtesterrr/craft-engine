package net.momirealms.craftengine.core.plugin.compatibility;

import net.momirealms.craftengine.core.entity.player.Player;

public interface LevelerProvider {

    void addExp(Player player, String target, double amount);

    int getLevel(Player player, String target);
}
