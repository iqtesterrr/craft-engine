package net.momirealms.craftengine.bukkit.compatibility.leveler;

import dev.aurelium.auraskills.api.AuraSkillsApi;
import dev.aurelium.auraskills.api.registry.NamespacedId;
import net.momirealms.craftengine.core.entity.player.Player;
import net.momirealms.craftengine.core.plugin.compatibility.LevelerProvider;

public class AuraSkillsLevelerProvider implements LevelerProvider {

    @Override
    public void addExp(Player player, String target, double amount) {
        AuraSkillsApi.get().getUser(player.uuid()).addSkillXp(AuraSkillsApi.get().getGlobalRegistry().getSkill(NamespacedId.fromDefault(target)), amount);
    }

    @Override
    public int getLevel(Player player, String target) {
        return AuraSkillsApi.get().getUser(player.uuid()).getSkillLevel(AuraSkillsApi.get().getGlobalRegistry().getSkill(NamespacedId.fromDefault(target)));
    }
}
