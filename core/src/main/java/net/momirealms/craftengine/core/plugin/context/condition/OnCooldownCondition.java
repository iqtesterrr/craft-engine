package net.momirealms.craftengine.core.plugin.context.condition;

import net.momirealms.craftengine.core.entity.player.Player;
import net.momirealms.craftengine.core.plugin.context.Condition;
import net.momirealms.craftengine.core.plugin.context.Context;
import net.momirealms.craftengine.core.plugin.context.parameter.DirectContextParameters;
import net.momirealms.craftengine.core.util.Key;
import net.momirealms.craftengine.core.util.ResourceConfigUtils;

import java.util.Map;
import java.util.Optional;

public class OnCooldownCondition<CTX extends Context> implements Condition<CTX> {
    private final String key;

    public OnCooldownCondition(String key) {
        this.key = key;
    }

    @Override
    public Key type() {
        return CommonConditions.ON_COOLDOWN;
    }

    @Override
    public boolean test(CTX ctx) {
        Optional<Player> player = ctx.getOptionalParameter(DirectContextParameters.PLAYER);
        if (player.isPresent()) {
            Player p = player.get();
            return p.cooldown().isOnCooldown(this.key);
        }
        return false;
    }

    public static class FactoryImpl<CTX extends Context> implements ConditionFactory<CTX> {

        @Override
        public Condition<CTX> create(Map<String, Object> arguments) {
            String id = ResourceConfigUtils.requireNonEmptyStringOrThrow(arguments.get("id"), "warning.config.condition.on_cooldown.missing_id");
            return new OnCooldownCondition<>(id);
        }
    }
}
