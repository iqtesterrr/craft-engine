package net.momirealms.craftengine.core.plugin.gui;

import net.kyori.adventure.text.Component;
import net.momirealms.craftengine.core.entity.player.Player;
import net.momirealms.craftengine.core.plugin.Manageable;

public interface GuiManager extends Manageable {

    void openInventory(Player player, GuiType guiType);

    void updateInventoryTitle(Player player, Component component);

    Inventory createInventory(Gui gui, int size);
}
