package net.momirealms.craftengine.bukkit.block;

import net.momirealms.craftengine.bukkit.nms.FastNMS;
import net.momirealms.craftengine.core.block.BlockShape;
import org.jetbrains.annotations.Nullable;

public class BukkitBlockShape implements BlockShape {
    private final Object rawBlockState;
    private final Object supportBlockState;

    public BukkitBlockShape(Object rawBlockState, @Nullable Object supportBlockState) {
        this.rawBlockState = rawBlockState;
        this.supportBlockState = supportBlockState == null ? rawBlockState : supportBlockState;
    }

    @Override
    public Object getShape(Object thisObj, Object[] args) {
        return FastNMS.INSTANCE.method$BlockState$getShape(this.rawBlockState, args[1], args[2], args[3]);
    }

    @Override
    public Object getCollisionShape(Object thisObj, Object[] args) {
        return FastNMS.INSTANCE.method$BlockState$getCollisionShape(this.rawBlockState, args[1], args[2], args[3]);
    }

    @Override
    public Object getSupportShape(Object thisObj, Object[] args) {
        return FastNMS.INSTANCE.method$BlockState$getBlockSupportShape(this.supportBlockState, args[1], args[2]);
    }
}
