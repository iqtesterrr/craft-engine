package net.momirealms.craftengine.core.world.particle;

import net.momirealms.craftengine.core.block.BlockStateWrapper;
import net.momirealms.craftengine.core.block.DelayedInitBlockState;

public class BlockStateData implements ParticleData {
    private final DelayedInitBlockState blockState;

    public BlockStateData(DelayedInitBlockState blockState) {
        this.blockState = blockState;
    }

    public BlockStateWrapper blockState() {
        return blockState.getState();
    }
}
