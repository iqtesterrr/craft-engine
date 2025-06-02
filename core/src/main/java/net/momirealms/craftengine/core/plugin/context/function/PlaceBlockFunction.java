package net.momirealms.craftengine.core.plugin.context.function;

import net.momirealms.craftengine.core.block.DelayedInitBlockState;
import net.momirealms.craftengine.core.block.UpdateOption;
import net.momirealms.craftengine.core.plugin.context.Condition;
import net.momirealms.craftengine.core.plugin.context.Context;
import net.momirealms.craftengine.core.plugin.context.number.NumberProvider;
import net.momirealms.craftengine.core.plugin.context.number.NumberProviders;
import net.momirealms.craftengine.core.plugin.context.parameter.DirectContextParameters;
import net.momirealms.craftengine.core.util.Key;
import net.momirealms.craftengine.core.util.MCUtils;
import net.momirealms.craftengine.core.util.ResourceConfigUtils;
import net.momirealms.craftengine.core.world.World;
import net.momirealms.craftengine.core.world.WorldPosition;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class PlaceBlockFunction<CTX extends Context> extends AbstractConditionalFunction<CTX> {
    private final DelayedInitBlockState delayedInitBlockState;
    private final NumberProvider x;
    private final NumberProvider y;
    private final NumberProvider z;
    private final NumberProvider updateFlags;

    public PlaceBlockFunction(DelayedInitBlockState delayedInitBlockState, NumberProvider x, NumberProvider y, NumberProvider z, NumberProvider updateFlags, List<Condition<CTX>> predicates) {
        super(predicates);
        this.delayedInitBlockState = delayedInitBlockState;
        this.x = x;
        this.y = y;
        this.z = z;
        this.updateFlags = updateFlags;
    }

    @Override
    public void runInternal(CTX ctx) {
        Optional<WorldPosition> optionalWorldPosition = ctx.getOptionalParameter(DirectContextParameters.POSITION);
        if (optionalWorldPosition.isPresent()) {
            World world = optionalWorldPosition.get().world();
            world.setBlockAt(MCUtils.fastFloor(this.x.getDouble(ctx)), MCUtils.fastFloor(this.y.getDouble(ctx)), MCUtils.fastFloor(this.z.getDouble(ctx)), this.delayedInitBlockState.getState(), this.updateFlags.getInt(ctx));
        }
    }

    @Override
    public Key type() {
        return CommonFunctions.PLACE_BLOCK;
    }

    public static class FactoryImpl<CTX extends Context> extends AbstractFactory<CTX> {

        public FactoryImpl(java.util.function.Function<Map<String, Object>, Condition<CTX>> factory) {
            super(factory);
        }

        @Override
        public Function<CTX> create(Map<String, Object> arguments) {
            String state = ResourceConfigUtils.requireNonEmptyStringOrThrow(arguments.get("block-state"), "warning.config.function.place_block.missing_block_state");
            DelayedInitBlockState delayedInitBlockState = new DelayedInitBlockState(state);
            NumberProvider x = NumberProviders.fromObject(arguments.getOrDefault("x", "<arg:position.x>"));
            NumberProvider y = NumberProviders.fromObject(arguments.getOrDefault("y", "<arg:position.y>"));
            NumberProvider z = NumberProviders.fromObject(arguments.getOrDefault("z", "<arg:position.z>"));
            NumberProvider flags = Optional.ofNullable(arguments.get("update-flags")).map(NumberProviders::fromObject).orElse(NumberProviders.direct(UpdateOption.UPDATE_ALL.flags()));
            return new PlaceBlockFunction<>(delayedInitBlockState, x, y, z, flags, getPredicates(arguments));
        }
    }
}
