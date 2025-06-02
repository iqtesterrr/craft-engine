package net.momirealms.craftengine.core.block.behavior;

import net.momirealms.craftengine.core.block.BlockBehavior;
import net.momirealms.craftengine.core.block.CustomBlock;

public abstract class AbstractBlockBehavior extends BlockBehavior {
    protected CustomBlock customBlock;

    public AbstractBlockBehavior(CustomBlock customBlock) {
        this.customBlock = customBlock;
    }
}
