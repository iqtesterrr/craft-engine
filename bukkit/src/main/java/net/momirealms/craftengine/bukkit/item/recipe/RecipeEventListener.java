package net.momirealms.craftengine.bukkit.item.recipe;

import net.momirealms.craftengine.bukkit.plugin.BukkitCraftEngine;
import net.momirealms.craftengine.bukkit.plugin.user.BukkitServerPlayer;
import net.momirealms.craftengine.bukkit.util.ItemUtils;
import net.momirealms.craftengine.bukkit.util.Reflections;
import net.momirealms.craftengine.core.item.Item;
import net.momirealms.craftengine.core.item.ItemManager;
import net.momirealms.craftengine.core.item.recipe.OptimizedIDItem;
import net.momirealms.craftengine.core.item.recipe.Recipe;
import net.momirealms.craftengine.core.item.recipe.RecipeTypes;
import net.momirealms.craftengine.core.item.recipe.input.CraftingInput;
import net.momirealms.craftengine.core.registry.BuiltInRegistries;
import net.momirealms.craftengine.core.registry.Holder;
import net.momirealms.craftengine.core.util.Key;
import net.momirealms.craftengine.core.util.VersionHelper;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RecipeEventListener implements Listener {
    private static final OptimizedIDItem<ItemStack> EMPTY = new OptimizedIDItem<>(null, null);
    private final ItemManager<ItemStack> itemManager;
    private final BukkitRecipeManager recipeManager;
    private final BukkitCraftEngine plugin;

    public RecipeEventListener(BukkitCraftEngine plugin, BukkitRecipeManager recipeManager, ItemManager<ItemStack> itemManager) {
        this.itemManager = itemManager;
        this.recipeManager = recipeManager;
        this.plugin = plugin;
    }

    @EventHandler
    public void onClickCartographyTable(InventoryClickEvent event) {
        if (VersionHelper.isPaper()) return;
        if (!(event.getClickedInventory() instanceof CartographyInventory cartographyInventory)) {
            return;
        }
        plugin.scheduler().sync().runDelayed(() -> {
            if (ItemUtils.hasCustomItem(cartographyInventory.getContents())) {
                cartographyInventory.setResult(new ItemStack(Material.AIR));
            }
        });
    }

    @EventHandler
    public void onSpecialRecipe(PrepareItemCraftEvent event) {
        org.bukkit.inventory.Recipe recipe = event.getRecipe();
        if (recipe == null)
            return;
        if (!(recipe instanceof ComplexRecipe))
            return;
        CraftingInventory inventory = event.getInventory();
        if (ItemUtils.hasCustomItem(inventory.getMatrix())) {
            inventory.setResult(null);
        }
    }

    @EventHandler
    public void onCrafting(PrepareItemCraftEvent event) {
        org.bukkit.inventory.Recipe recipe = event.getRecipe();
        if (recipe == null)
            return;

        // we only handle shaped and shapeless recipes
        boolean shapeless = event.getRecipe() instanceof ShapelessRecipe;
        boolean shaped = event.getRecipe() instanceof ShapedRecipe;
        if (!shaped && !shapeless) return;

        CraftingRecipe craftingRecipe = (CraftingRecipe) recipe;
        Key recipeId = Key.of(craftingRecipe.getKey().namespace(), craftingRecipe.getKey().value());

        CraftingInventory inventory = event.getInventory();
        ItemStack[] ingredients = inventory.getMatrix();

        boolean isCustom = this.recipeManager.isCustomRecipe(recipeId);

        // if the recipe is a vanilla one but not injected, custom items should never be ingredients
        if (this.recipeManager.isDataPackRecipe(recipeId) && !isCustom) {
            if (ItemUtils.hasCustomItem(ingredients)) {
                inventory.setResult(null);
            }
            return;
        }

        // Maybe it's recipe from other plugins, then we ignore it
        if (!isCustom) {
            return;
        }

        List<OptimizedIDItem<ItemStack>> optimizedIDItems = new ArrayList<>();
        for (ItemStack itemStack : ingredients) {
            if (ItemUtils.isEmpty(itemStack)) {
                optimizedIDItems.add(EMPTY);
            } else {
                Item<ItemStack> wrappedItem = this.itemManager.wrap(itemStack);
                Optional<Holder.Reference<Key>> idHolder = BuiltInRegistries.OPTIMIZED_ITEM_ID.get(wrappedItem.id());
                if (idHolder.isEmpty()) {
                    // an invalid item is used in recipe, we disallow it
                    inventory.setResult(null);
                    return;
                } else {
                    optimizedIDItems.add(new OptimizedIDItem<>(idHolder.get(), itemStack));
                }
            }
        }

        CraftingInput<ItemStack> input;
        if (ingredients.length == 9) {
            input = CraftingInput.of(3, 3, optimizedIDItems);
        } else if (ingredients.length == 4) {
            input = CraftingInput.of(2, 2, optimizedIDItems);
        } else {
            return;
        }

        Player player;
        try {
            player = (Player) Reflections.method$InventoryView$getPlayer.invoke(event.getView());
        } catch (ReflectiveOperationException e) {
            plugin.logger().warn("Failed to get inventory viewer", e);
            return;
        }

        BukkitServerPlayer serverPlayer = this.plugin.adapt(player);
        Recipe<ItemStack> lastRecipe = serverPlayer.lastUsedRecipe();
        if (lastRecipe != null && (lastRecipe.type() == RecipeTypes.SHAPELESS || lastRecipe.type() == RecipeTypes.SHAPED )) {
            if (lastRecipe.matches(input)) {
                inventory.setResult(lastRecipe.getResult(serverPlayer));
                return;
            }
        }

        Recipe<ItemStack> ceRecipe = this.recipeManager.getRecipe(RecipeTypes.SHAPELESS, input);
        if (ceRecipe != null) {
            inventory.setResult(ceRecipe.getResult(serverPlayer));
            serverPlayer.setLastUsedRecipe(ceRecipe);
            correctRecipeUsed(inventory, ceRecipe);
            return;
        }
        ceRecipe = this.recipeManager.getRecipe(RecipeTypes.SHAPED, input);
        if (ceRecipe != null) {
            inventory.setResult(ceRecipe.getResult(serverPlayer));
            serverPlayer.setLastUsedRecipe(ceRecipe);
            correctRecipeUsed(inventory, ceRecipe);
            return;
        }
        // clear result if not met
        inventory.setResult(null);
    }

    private void correctRecipeUsed(CraftingInventory inventory, Recipe<ItemStack> recipe) {
        Object holderOrRecipe = recipeManager.getRecipeHolderByRecipe(recipe);
        if (holderOrRecipe == null) return;
        try {
            Object resultInventory = Reflections.field$CraftInventoryCrafting$resultInventory.get(inventory);
            Reflections.field$ResultContainer$recipeUsed.set(resultInventory, holderOrRecipe);
        } catch (ReflectiveOperationException e) {
            plugin.logger().warn("Failed to correct used recipe", e);
        }
    }
}
