package net.momirealms.craftengine.bukkit.plugin.command.feature;

import net.momirealms.craftengine.bukkit.plugin.command.BukkitCommandFeature;
import net.momirealms.craftengine.bukkit.plugin.reflection.minecraft.NetworkReflections;
import net.momirealms.craftengine.core.plugin.CraftEngine;
import net.momirealms.craftengine.core.plugin.command.CraftEngineCommandManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.incendo.cloud.Command;
import org.incendo.cloud.parser.standard.BooleanParser;
import org.incendo.cloud.parser.standard.FloatParser;

import java.lang.reflect.InvocationTargetException;

public class TestCommand extends BukkitCommandFeature<CommandSender> {

    public TestCommand(CraftEngineCommandManager<CommandSender> commandManager, CraftEngine plugin) {
        super(commandManager, plugin);
    }

    @Override
    public Command.Builder<? extends CommandSender> assembleCommand(org.incendo.cloud.CommandManager<CommandSender> manager, Command.Builder<CommandSender> builder) {
        return builder
                .senderType(Player.class)
                .required("tickRate", FloatParser.floatParser())
                .required("isFrozen", BooleanParser.booleanParser())
                .handler(context -> {
                    Player player = context.sender();
                    float tickRate = context.get("tickRate");
                    boolean isFrozen = context.get("isFrozen");
                    try {
                        plugin().adapt(player).sendPacket(NetworkReflections.constructor$ClientboundTickingStatePacket.newInstance(tickRate, isFrozen), true);
                    } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
                        player.sendMessage("发送失败");
                    }
                });
    }

    @Override
    public String getFeatureID() {
        return "debug_test";
    }
}
