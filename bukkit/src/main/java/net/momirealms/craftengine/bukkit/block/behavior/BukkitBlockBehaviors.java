package net.momirealms.craftengine.bukkit.block.behavior;

import net.momirealms.craftengine.core.block.EmptyBlockBehavior;
import net.momirealms.craftengine.core.block.behavior.BlockBehaviors;
import net.momirealms.craftengine.core.util.Key;

public class BukkitBlockBehaviors extends BlockBehaviors {
    public static final Key BUSH_BLOCK = Key.from("craftengine:bush_block");
    public static final Key HANGING_BLOCK = Key.from("craftengine:hanging_block");
    public static final Key FALLING_BLOCK = Key.from("craftengine:falling_block");
    public static final Key LEAVES_BLOCK = Key.from("craftengine:leaves_block");
    public static final Key STRIPPABLE_BLOCK = Key.from("craftengine:strippable_block");
    public static final Key SAPLING_BLOCK = Key.from("craftengine:sapling_block");
    public static final Key ON_LIQUID_BLOCK = Key.from("craftengine:on_liquid_block");
    public static final Key NEAR_LIQUID_BLOCK = Key.from("craftengine:near_liquid_block");
    public static final Key WATERLOGGED_BLOCK = Key.from("craftengine:waterlogged_block");
    public static final Key CONCRETE_POWDER_BLOCK = Key.from("craftengine:concrete_powder_block");
    public static final Key VERTICAL_CROP_BLOCK = Key.from("craftengine:vertical_crop_block");
    public static final Key CROP_BLOCK = Key.from("craftengine:crop_block");
    public static final Key GRASS_BLOCK = Key.from("craftengine:grass_block");
    public static final Key LAMP_BLOCK = Key.from("craftengine:lamp_block");

    public static void init() {
        register(EMPTY, (block, args) -> EmptyBlockBehavior.INSTANCE);
        register(FALLING_BLOCK, FallingBlockBehavior.FACTORY);
        register(BUSH_BLOCK, BushBlockBehavior.FACTORY);
        register(HANGING_BLOCK, HangingBlockBehavior.FACTORY);
        register(LEAVES_BLOCK, LeavesBlockBehavior.FACTORY);
        register(STRIPPABLE_BLOCK, StrippableBlockBehavior.FACTORY);
        register(SAPLING_BLOCK, SaplingBlockBehavior.FACTORY);
        register(ON_LIQUID_BLOCK, OnLiquidBlockBehavior.FACTORY);
        register(NEAR_LIQUID_BLOCK, NearLiquidBlockBehavior.FACTORY);
        register(WATERLOGGED_BLOCK, WaterLoggedBlockBehavior.FACTORY);
        register(CONCRETE_POWDER_BLOCK, ConcretePowderBlockBehavior.FACTORY);
        register(VERTICAL_CROP_BLOCK, VerticalCropBlockBehavior.FACTORY);
        register(CROP_BLOCK, CropBlockBehavior.FACTORY);
        register(GRASS_BLOCK, GrassBlockBehavior.FACTORY);
        register(LAMP_BLOCK, LampBlockBehavior.FACTORY);
    }
}
