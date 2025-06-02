package net.momirealms.craftengine.core.plugin.context.function;

import net.momirealms.craftengine.core.entity.player.Player;
import net.momirealms.craftengine.core.plugin.context.Condition;
import net.momirealms.craftengine.core.plugin.context.Context;
import net.momirealms.craftengine.core.plugin.context.parameter.DirectContextParameters;
import net.momirealms.craftengine.core.util.Key;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class UpdateInteractionFunction<CTX extends Context> extends AbstractConditionalFunction<CTX> {

    public UpdateInteractionFunction(List<Condition<CTX>> predicates) {
        super(predicates);
    }

    @Override
    public void runInternal(CTX ctx) {
        Optional<Player> cancellable = ctx.getOptionalParameter(DirectContextParameters.PLAYER);
        cancellable.ifPresent(value -> value.updateLastSuccessfulInteractionTick(value.gameTicks()));
    }

    @Override
    public Key type() {
        return CommonFunctions.UPDATE_INTERACTION_TICK;
    }

    public static class FactoryImpl<CTX extends Context> extends AbstractFactory<CTX> {

        public FactoryImpl(java.util.function.Function<Map<String, Object>, Condition<CTX>> factory) {
            super(factory);
        }

        @Override
        public Function<CTX> create(Map<String, Object> arguments) {
            return new UpdateInteractionFunction<>(getPredicates(arguments));
        }
    }
}
