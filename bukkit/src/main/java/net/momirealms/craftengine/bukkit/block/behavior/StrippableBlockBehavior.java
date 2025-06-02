package net.momirealms.craftengine.bukkit.block.behavior;

import net.momirealms.craftengine.bukkit.api.CraftEngineBlocks;
import net.momirealms.craftengine.bukkit.block.BukkitBlockManager;
import net.momirealms.craftengine.bukkit.plugin.reflection.minecraft.CoreReflections;
import net.momirealms.craftengine.bukkit.util.*;
import net.momirealms.craftengine.bukkit.world.BukkitBlockInWorld;
import net.momirealms.craftengine.core.block.BlockBehavior;
import net.momirealms.craftengine.core.block.CustomBlock;
import net.momirealms.craftengine.core.block.ImmutableBlockState;
import net.momirealms.craftengine.core.block.UpdateOption;
import net.momirealms.craftengine.core.block.behavior.BlockBehaviorFactory;
import net.momirealms.craftengine.core.entity.player.InteractionHand;
import net.momirealms.craftengine.core.entity.player.InteractionResult;
import net.momirealms.craftengine.core.entity.player.Player;
import net.momirealms.craftengine.core.item.CustomItem;
import net.momirealms.craftengine.core.item.Item;
import net.momirealms.craftengine.core.item.ItemKeys;
import net.momirealms.craftengine.core.item.context.UseOnContext;
import net.momirealms.craftengine.core.plugin.CraftEngine;
import net.momirealms.craftengine.core.util.Key;
import net.momirealms.craftengine.core.util.ResourceConfigUtils;
import net.momirealms.craftengine.core.util.VersionHelper;
import net.momirealms.craftengine.core.world.BlockPos;
import net.momirealms.craftengine.core.world.Vec3d;
import net.momirealms.sparrow.nbt.CompoundTag;
import org.bukkit.GameEvent;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.block.Block;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.Map;
import java.util.Optional;

public class StrippableBlockBehavior extends BukkitBlockBehavior {
    public static final Factory FACTORY = new Factory();
    private static final Key AXE_STRIP_SOUND = Key.of("minecraft:item.axe.strip");
    private final Key stripped;

    public StrippableBlockBehavior(CustomBlock block, Key stripped) {
        super(block);
        this.stripped = stripped;
    }

    public Key stripped() {
        return this.stripped;
    }

    @SuppressWarnings("unchecked")
    @Override
    public InteractionResult useOnBlock(UseOnContext context, ImmutableBlockState state) {
        Item<ItemStack> item = (Item<ItemStack>) context.getItem();
        if (item == null) {
            return InteractionResult.PASS;
        }
        Optional<CustomItem<ItemStack>> optionalCustomItem = item.getCustomItem();
        if (optionalCustomItem.isEmpty()) {
            if (!item.is(ItemTags.AXES))
                return InteractionResult.PASS;
        } else {
            CustomItem<ItemStack> customItem = optionalCustomItem.get();
            if (!customItem.settings().tags().contains(ItemTags.AXES) && !item.is(ItemTags.AXES))
                return InteractionResult.PASS;
        }

        Player player = context.getPlayer();
        // no adventure mode
        if (player.isAdventureMode()) {
            return InteractionResult.PASS;
        }
        Item<?> offHandItem = player.getItemInHand(InteractionHand.OFF_HAND);
        // is using a shield
        if (context.getHand() == InteractionHand.MAIN_HAND && offHandItem != null && offHandItem.vanillaId().equals(ItemKeys.SHIELD) && !player.isSecondaryUseActive()) {
            return InteractionResult.PASS;
        }

        Optional<CustomBlock> optionalNewCustomBlock = BukkitBlockManager.instance().blockById(stripped());
        if (optionalNewCustomBlock.isEmpty()) {
            CraftEngine.instance().logger().warn("stripped block " + stripped() + " does not exist");
            return InteractionResult.FAIL;
        }
        CustomBlock newCustomBlock = optionalNewCustomBlock.get();
        CompoundTag compoundTag = state.propertiesNbt();
        ImmutableBlockState newState = newCustomBlock.getBlockState(compoundTag);

        org.bukkit.entity.Player bukkitPlayer = ((org.bukkit.entity.Player) player.platformPlayer());

        BukkitBlockInWorld clicked = (BukkitBlockInWorld) context.getLevel().getBlockAt(context.getClickedPos());
        Block block = clicked.block();
        // Call bukkit event
        EntityChangeBlockEvent event = new EntityChangeBlockEvent(bukkitPlayer, block, BlockStateUtils.fromBlockData(newState.customBlockState().handle()));
        if (EventUtils.fireAndCheckCancel(event)) {
            return InteractionResult.FAIL;
        }

        BlockPos pos = context.getClickedPos();
        context.getLevel().playBlockSound(Vec3d.atCenterOf(pos), AXE_STRIP_SOUND, 1, 1);
        CraftEngineBlocks.place(block.getLocation(), newState, UpdateOption.UPDATE_ALL_IMMEDIATE, false);
        block.getWorld().sendGameEvent(bukkitPlayer, GameEvent.BLOCK_CHANGE, new Vector(pos.x(), pos.y(), pos.z()));
        Material material = MaterialUtils.getMaterial(item.vanillaId());
        bukkitPlayer.setStatistic(Statistic.USE_ITEM, material, bukkitPlayer.getStatistic(Statistic.USE_ITEM, material) + 1);

        // resend swing if it's not interactable on client side
        if (!InteractUtils.isInteractable(
                bukkitPlayer, BlockStateUtils.fromBlockData(state.vanillaBlockState().handle()),
                context.getHitResult(), item
        ) || player.isSecondaryUseActive()) {
            player.swingHand(context.getHand());
        }
        // shrink item amount
        if (VersionHelper.isOrAbove1_20_5()) {
            Object itemStack = item.getLiteralObject();
            Object serverPlayer = player.serverPlayer();
            Object equipmentSlot = context.getHand() == InteractionHand.MAIN_HAND ? CoreReflections.instance$EquipmentSlot$MAINHAND : CoreReflections.instance$EquipmentSlot$OFFHAND;
            try {
                CoreReflections.method$ItemStack$hurtAndBreak.invoke(itemStack, 1, serverPlayer, equipmentSlot);
            } catch (ReflectiveOperationException e) {
                CraftEngine.instance().logger().warn("Failed to hurt itemStack", e);
            }
        } else {
            ItemStack itemStack = item.getItem();
            itemStack.damage(1, bukkitPlayer);
        }
        return InteractionResult.SUCCESS;
    }

    public static class Factory implements BlockBehaviorFactory {

        @Override
        public BlockBehavior create(CustomBlock block, Map<String, Object> arguments) {
            String stripped = ResourceConfigUtils.requireNonEmptyStringOrThrow(arguments.get("stripped"), "warning.config.block.behavior.strippable.missing_stripped");
            return new StrippableBlockBehavior(block, Key.of(stripped));
        }
    }
}
