package net.momirealms.craftengine.bukkit.block.behavior;

import net.momirealms.craftengine.core.block.BlockBehavior;
import net.momirealms.craftengine.core.block.CustomBlock;
import net.momirealms.craftengine.core.block.behavior.BlockBehaviorFactory;
import net.momirealms.craftengine.core.block.properties.Property;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class WaterLoggedBlockBehavior extends BukkitBlockBehavior {
    public static final Factory FACTORY = new Factory();
    @Nullable
    private final Property<Boolean> waterloggedProperty;

    public WaterLoggedBlockBehavior(CustomBlock block, @Nullable Property<Boolean> waterloggedProperty) {
        super(block);
        this.waterloggedProperty = waterloggedProperty;
    }

    // TODO create real waterlogged blocks, needs to have real waterlogged property
//    @Override
//    public Object pickupBlock(Object thisBlock, Object[] args, Callable<Object> superMethod) throws Exception {
//        if (this.waterloggedProperty == null) return Reflections.instance$ItemStack$EMPTY;
//        Object blockState;
//        Object world;
//        Object pos;
//        if (VersionHelper.isVersionNewerThan1_20_2()) {
//            world = args[1];
//            pos = args[2];
//            blockState = args[3];
//        } else {
//            world = args[0];
//            pos = args[1];
//            blockState = args[2];
//        }
//        ImmutableBlockState immutableBlockState = BukkitBlockManager.instance().getImmutableBlockState(BlockStateUtils.blockStateToId(blockState));
//        if (immutableBlockState != null) {
//            if (immutableBlockState.get(this.waterloggedProperty)) {
//                Reflections.method$LevelWriter$setBlock.invoke(world, pos, immutableBlockState.with(this.waterloggedProperty, false).customBlockState().handle(), 3);
//                // TODO check can survive
//                Object itemStack = Reflections.constructor$ItemStack.newInstance(Reflections.instance$Items$WATER_BUCKET);
//                return itemStack;
//            }
//        }
//        return Reflections.instance$ItemStack$EMPTY;
//    }
//
//    @Override
//    public boolean placeLiquid(Object thisBlock, Object[] args, Callable<Object> superMethod) throws Exception {
//        if (this.waterloggedProperty == null) return false;
//        Object blockState = args[2];
//        ImmutableBlockState immutableBlockState = BukkitBlockManager.instance().getImmutableBlockState(BlockStateUtils.blockStateToId(blockState));
//        if (immutableBlockState != null) {
//            Object fluidType = Reflections.method$FluidState$getType.invoke(args[3]);
//            if (!immutableBlockState.get(this.waterloggedProperty) && fluidType == Reflections.instance$Fluids$WATER) {
//                Reflections.method$LevelWriter$setBlock.invoke(args[0], args[1], immutableBlockState.with(this.waterloggedProperty, true).customBlockState().handle(), 3);
//                Reflections.method$LevelAccessor$scheduleTick.invoke(args[0], fluidType, Reflections.method$Fluid$getTickDelay.invoke(fluidType, args[0]));
//                return true;
//            }
//        }
//        return false;
//    }
//
//    // use water
//    @Override
//    public boolean canPlaceLiquid(Object thisBlock, Object[] args, Callable<Object> superMethod) throws Exception {
//        return super.canPlaceLiquid(thisBlock, args, superMethod);
//    }
//
//    @Override
//    public Object getFluidState(Object thisBlock, Object[] args, Callable<Object> superMethod) throws Exception {
//        Object blockState = args[0];
//        ImmutableBlockState state = BukkitBlockManager.instance().getImmutableBlockState(BlockStateUtils.blockStateToId(blockState));
//        if (state == null || state.isEmpty() || this.waterloggedProperty == null) return super.getFluidState(thisBlock, args, superMethod);
//        boolean waterlogged = state.get(this.waterloggedProperty);
//        return waterlogged ? Reflections.method$FlowingFluid$getSource.invoke(Reflections.instance$Fluids$WATER, false) : super.getFluidState(thisBlock, args, superMethod);
//    }

    @SuppressWarnings("unchecked")
    public static class Factory implements BlockBehaviorFactory {
        @Override
        public BlockBehavior create(CustomBlock block, Map<String, Object> arguments) {
            Property<Boolean> waterlogged = (Property<Boolean>) block.getProperty("waterlogged");
            return new WaterLoggedBlockBehavior(block, waterlogged);
        }
    }
}
