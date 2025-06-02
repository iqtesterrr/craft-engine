package net.momirealms.craftengine.bukkit.block.behavior;

import net.momirealms.craftengine.bukkit.block.BukkitBlockManager;
import net.momirealms.craftengine.bukkit.nms.FastNMS;
import net.momirealms.craftengine.bukkit.plugin.reflection.minecraft.CoreReflections;
import net.momirealms.craftengine.bukkit.plugin.reflection.minecraft.MBlocks;
import net.momirealms.craftengine.bukkit.util.BlockStateUtils;
import net.momirealms.craftengine.bukkit.util.LocationUtils;
import net.momirealms.craftengine.bukkit.world.BukkitWorld;
import net.momirealms.craftengine.core.block.CustomBlock;
import net.momirealms.craftengine.core.block.ImmutableBlockState;
import net.momirealms.craftengine.core.item.Item;
import net.momirealms.craftengine.core.plugin.context.ContextHolder;
import net.momirealms.craftengine.core.plugin.context.parameter.DirectContextParameters;
import net.momirealms.craftengine.core.util.VersionHelper;
import net.momirealms.craftengine.core.world.BlockPos;
import net.momirealms.craftengine.core.world.Vec3d;
import net.momirealms.craftengine.core.world.WorldEvents;
import net.momirealms.craftengine.core.world.WorldPosition;

import java.util.concurrent.Callable;

public abstract class AbstractCanSurviveBlockBehavior extends BukkitBlockBehavior {
    protected final int delay;

    protected AbstractCanSurviveBlockBehavior(CustomBlock customBlock, int delay) {
        super(customBlock);
        this.delay = delay;
    }

    @Override
    public void tick(Object thisBlock, Object[] args, Callable<Object> superMethod) throws Exception {
        if (this.delay == 0) return;
        Object blockState = args[0];
        Object level = args[1];
        Object blockPos = args[2];
        if (!canSurvive(thisBlock, args, () -> true)) {
            int stateId = BlockStateUtils.blockStateToId(blockState);
            ImmutableBlockState currentState = BukkitBlockManager.instance().getImmutableBlockState(stateId);
            if (currentState != null && !currentState.isEmpty() && currentState.owner().value() == this.customBlock) {
                // break the crop
                FastNMS.INSTANCE.method$Level$removeBlock(level, blockPos, false);
                net.momirealms.craftengine.core.world.World world = new BukkitWorld(FastNMS.INSTANCE.method$Level$getCraftWorld(level));
                WorldPosition position = new WorldPosition(world, Vec3d.atCenterOf(LocationUtils.fromBlockPos(blockPos)));
                ContextHolder.Builder builder = ContextHolder.builder()
                        .withParameter(DirectContextParameters.POSITION, position);
                for (Item<Object> item : currentState.getDrops(builder, world, null)) {
                    world.dropItemNaturally(position, item);
                }
                world.playBlockSound(position, currentState.sounds().breakSound());
                FastNMS.INSTANCE.method$Level$levelEvent(level, WorldEvents.BLOCK_BREAK_EFFECT, blockPos, stateId);
            }
        }
    }

    @Override
    public boolean canSurvive(Object thisBlock, Object[] args, Callable<Object> superMethod) throws Exception {
        Object state = args[0];
        Object world = args[1];
        Object pos = args[2];
        return canSurvive(thisBlock, state, world, pos);
    }

    @Override
    public void onPlace(Object thisBlock, Object[] args, Callable<Object> superMethod) throws Exception {
        Object world = args[1];
        Object blockPos = args[2];
        CoreReflections.method$LevelAccessor$scheduleTick.invoke(world, blockPos, thisBlock, 2);
    }

    @Override
    public Object updateShape(Object thisBlock, Object[] args, Callable<Object> superMethod) throws Exception {
        Object level;
        Object blockPos;
        Object state = args[0];
        if (VersionHelper.isOrAbove1_21_2()) {
            level = args[1];
            blockPos = args[3];
        } else {
            level = args[3];
            blockPos = args[4];
        }
        int stateId = BlockStateUtils.blockStateToId(state);
        ImmutableBlockState previousState = BukkitBlockManager.instance().getImmutableBlockState(stateId);
        if (previousState == null || previousState.isEmpty()) {
            return state;
        }
        if (this.delay != 0) {
            CoreReflections.method$LevelAccessor$scheduleTick.invoke(level, blockPos, thisBlock, this.delay);
            return state;
        }
        if (!canSurvive(thisBlock, new Object[] {state, level, blockPos}, () -> true)) {
            BlockPos pos = LocationUtils.fromBlockPos(blockPos);
            net.momirealms.craftengine.core.world.World world = new BukkitWorld(FastNMS.INSTANCE.method$Level$getCraftWorld(level));
            WorldPosition position = new WorldPosition(world, Vec3d.atCenterOf(pos));
            ContextHolder.Builder builder = ContextHolder.builder()
                    .withParameter(DirectContextParameters.POSITION, position);
            for (Item<Object> item : previousState.getDrops(builder, world, null)) {
                world.dropItemNaturally(position, item);
            }
            world.playBlockSound(position, previousState.sounds().breakSound());
            FastNMS.INSTANCE.method$Level$levelEvent(level, WorldEvents.BLOCK_BREAK_EFFECT, blockPos, stateId);
            return CoreReflections.method$Block$defaultBlockState.invoke(MBlocks.AIR);
        }
        return state;
    }

    protected abstract boolean canSurvive(Object thisBlock, Object state, Object world, Object blockPos) throws Exception;
}
