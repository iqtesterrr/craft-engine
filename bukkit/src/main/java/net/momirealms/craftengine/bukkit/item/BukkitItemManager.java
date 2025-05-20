package net.momirealms.craftengine.bukkit.item;

import com.saicone.rtag.item.ItemTagStream;
import net.momirealms.craftengine.bukkit.item.behavior.AxeItemBehavior;
import net.momirealms.craftengine.bukkit.item.behavior.BoneMealItemBehavior;
import net.momirealms.craftengine.bukkit.item.behavior.BucketItemBehavior;
import net.momirealms.craftengine.bukkit.item.behavior.WaterBucketItemBehavior;
import net.momirealms.craftengine.bukkit.item.factory.BukkitItemFactory;
import net.momirealms.craftengine.bukkit.item.listener.ArmorEventListener;
import net.momirealms.craftengine.bukkit.item.listener.DebugStickListener;
import net.momirealms.craftengine.bukkit.item.listener.ItemEventListener;
import net.momirealms.craftengine.bukkit.nms.FastNMS;
import net.momirealms.craftengine.bukkit.plugin.BukkitCraftEngine;
import net.momirealms.craftengine.bukkit.util.ItemUtils;
import net.momirealms.craftengine.bukkit.util.KeyUtils;
import net.momirealms.craftengine.bukkit.util.Reflections;
import net.momirealms.craftengine.core.entity.player.Player;
import net.momirealms.craftengine.core.item.*;
import net.momirealms.craftengine.core.item.modifier.IdModifier;
import net.momirealms.craftengine.core.plugin.context.ContextHolder;
import net.momirealms.craftengine.core.plugin.locale.LocalizedResourceConfigException;
import net.momirealms.craftengine.core.registry.BuiltInRegistries;
import net.momirealms.craftengine.core.registry.Holder;
import net.momirealms.craftengine.core.registry.WritableRegistry;
import net.momirealms.craftengine.core.util.Key;
import net.momirealms.craftengine.core.util.ReflectionUtils;
import net.momirealms.craftengine.core.util.ResourceConfigUtils;
import net.momirealms.craftengine.core.util.ResourceKey;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

public class BukkitItemManager extends AbstractItemManager<ItemStack> {
    static {
        registerVanillaItemExtraBehavior(AxeItemBehavior.INSTANCE, ItemKeys.AXES);
        registerVanillaItemExtraBehavior(WaterBucketItemBehavior.INSTANCE, ItemKeys.WATER_BUCKETS);
        registerVanillaItemExtraBehavior(BucketItemBehavior.INSTANCE, ItemKeys.BUCKET);
        registerVanillaItemExtraBehavior(BoneMealItemBehavior.INSTANCE, ItemKeys.BONE_MEAL);
    }

    private static BukkitItemManager instance;
    private final BukkitItemFactory<? extends ItemWrapper<ItemStack>> factory;
    private final BukkitCraftEngine plugin;
    private final ItemEventListener itemEventListener;
    private final DebugStickListener debugStickListener;
    private final ArmorEventListener armorEventListener;

    public BukkitItemManager(BukkitCraftEngine plugin) {
        super(plugin);
        instance = this;
        this.plugin = plugin;
        this.factory = BukkitItemFactory.create(plugin);
        this.itemEventListener = new ItemEventListener(plugin);
        this.debugStickListener = new DebugStickListener(plugin);
        this.armorEventListener = new ArmorEventListener();
        this.registerAllVanillaItems();
        if (plugin.hasMod()) {
            Class<?> clazz$CustomStreamCodec = ReflectionUtils.getClazz("net.momirealms.craftengine.mod.item.CustomStreamCodec");
            if (clazz$CustomStreamCodec != null) {
                Field s2cProcessor = ReflectionUtils.getDeclaredField(clazz$CustomStreamCodec, Function.class, 0);
                Field c2sProcessor = ReflectionUtils.getDeclaredField(clazz$CustomStreamCodec, Function.class, 1);
                Function<Object, Object> s2c = (raw) -> {
                    ItemStack itemStack = FastNMS.INSTANCE.method$CraftItemStack$asCraftMirror(raw);
                    Item<ItemStack> wrapped = this.wrap(itemStack.clone());
                    Optional<CustomItem<ItemStack>> customItem = wrapped.getCustomItem();
                    if (customItem.isEmpty()) {
                        return raw;
                    }
                    CustomItem<ItemStack> custom = customItem.get();
                    if (!custom.hasClientBoundDataModifier()) {
                        return raw;
                    }
                    for (NetworkItemDataProcessor<ItemStack> processor : custom.networkItemDataProcessors()) {
                        processor.toClient(wrapped, ItemBuildContext.EMPTY);
                    }
                    wrapped.load();
                    return wrapped.getLiteralObject();
                };

                Function<Object, Object> c2s = (raw) -> {
                    ItemStack itemStack = FastNMS.INSTANCE.method$CraftItemStack$asCraftMirror(raw);
                    Item<ItemStack> wrapped = this.wrap(itemStack);
                    Optional<CustomItem<ItemStack>> customItem = wrapped.getCustomItem();
                    if (customItem.isEmpty()) {
                        return raw;
                    }
                    CustomItem<ItemStack> custom = customItem.get();
                    if (!custom.hasClientBoundDataModifier()) {
                        return raw;
                    }
                    for (NetworkItemDataProcessor<ItemStack> processor : custom.networkItemDataProcessors()) {
                        processor.toServer(wrapped, ItemBuildContext.EMPTY);
                    }
                    wrapped.load();
                    return wrapped.getLiteralObject();
                };
                try {
                    assert s2cProcessor != null;
                    s2cProcessor.set(null, s2c);
                    assert c2sProcessor != null;
                    c2sProcessor.set(null, c2s);
                } catch (ReflectiveOperationException e) {
                    plugin.logger().warn("Failed to load custom stream codec", e);
                }
            }
        }
    }

    @Override
    public void delayedInit() {
        Bukkit.getPluginManager().registerEvents(this.itemEventListener, this.plugin.bootstrap());
        Bukkit.getPluginManager().registerEvents(this.debugStickListener, this.plugin.bootstrap());
        Bukkit.getPluginManager().registerEvents(this.armorEventListener, this.plugin.bootstrap());
    }

    public static BukkitItemManager instance() {
        return instance;
    }

    @Override
    public Optional<BuildableItem<ItemStack>> getVanillaItem(Key key) {
        Material material = Registry.MATERIAL.get(KeyUtils.toNamespacedKey(key));
        if (material == null) {
            return Optional.empty();
        }
        return Optional.of(new CloneableConstantItem(key, new ItemStack(material)));
    }

    @Override
    public int fuelTime(ItemStack itemStack) {
        if (ItemUtils.isEmpty(itemStack)) return 0;
        Optional<CustomItem<ItemStack>> customItem = wrap(itemStack).getCustomItem();
        return customItem.map(it -> it.settings().fuelTime()).orElse(0);
    }

    @Override
    public int fuelTime(Key id) {
        return getCustomItem(id).map(it -> it.settings().fuelTime()).orElse(0);
    }

    @Override
    public void disable() {
        this.unload();
        HandlerList.unregisterAll(this.itemEventListener);
        HandlerList.unregisterAll(this.debugStickListener);
        HandlerList.unregisterAll(this.armorEventListener);
    }

    @Override
    public Item<ItemStack> fromByteArray(byte[] bytes) {
        return this.factory.wrap(ItemTagStream.INSTANCE.fromBytes(bytes));
    }

    @Override
    public ItemStack buildCustomItemStack(Key id, Player player) {
        return Optional.ofNullable(customItems.get(id)).map(it -> it.buildItemStack(new ItemBuildContext(player, ContextHolder.EMPTY), 1)).orElse(null);
    }

    @Override
    public ItemStack buildItemStack(Key id, @Nullable Player player) {
        return Optional.ofNullable(buildCustomItemStack(id, player)).orElseGet(() -> createVanillaItemStack(id));
    }

    @Override
    public Item<ItemStack> createCustomWrappedItem(Key id, Player player) {
        return Optional.ofNullable(customItems.get(id)).map(it -> it.buildItem(player)).orElse(null);
    }

    private ItemStack createVanillaItemStack(Key id) {
        NamespacedKey key = NamespacedKey.fromString(id.toString());
        if (key == null) {
            this.plugin.logger().warn(id + " is not a valid namespaced key");
            return new ItemStack(Material.AIR);
        }
        Material material = Registry.MATERIAL.get(key);
        if (material == null) {
            this.plugin.logger().warn(id + " is not a valid material");
            return new ItemStack(Material.AIR);
        }
        return new ItemStack(material);
    }

    @Override
    public Item<ItemStack> createWrappedItem(Key id, @Nullable Player player) {
        return Optional.ofNullable(this.customItems.get(id)).map(it -> it.buildItem(player)).orElseGet(() -> {
            ItemStack itemStack = createVanillaItemStack(id);
            return wrap(itemStack);
        });
    }

    @Override
    public Item<ItemStack> wrap(ItemStack itemStack) {
        if (ItemUtils.isEmpty(itemStack)) return null;
        return this.factory.wrap(itemStack);
    }

    @Override
    public Key itemId(ItemStack itemStack) {
        Item<ItemStack> wrapped = wrap(itemStack);
        return wrapped.id();
    }

    @Override
    public Key customItemId(ItemStack itemStack) {
        Item<ItemStack> wrapped = wrap(itemStack);
        if (!wrapped.hasTag(IdModifier.CRAFT_ENGINE_ID)) return null;
        return wrapped.id();
    }

    @Override
    protected CustomItem.Builder<ItemStack> createPlatformItemBuilder(Holder<Key> id, Key materialId) {
        Material material = ResourceConfigUtils.requireNonNullOrThrow(Registry.MATERIAL.get(KeyUtils.toNamespacedKey(materialId)), () -> new LocalizedResourceConfigException("warning.config.item.invalid_material", materialId.toString()));
        return BukkitCustomItem.builder(material).material(materialId).id(id);
    }

    @SuppressWarnings("unchecked")
    private void registerAllVanillaItems() {
        try {
            for (NamespacedKey item : FastNMS.INSTANCE.getAllVanillaItems()) {
                if (item.getNamespace().equals("minecraft")) {
                    Key id = KeyUtils.namespacedKey2Key(item);
                    VANILLA_ITEMS.add(id);
                    Holder.Reference<Key> holder =  BuiltInRegistries.OPTIMIZED_ITEM_ID.get(id)
                            .orElseGet(() -> ((WritableRegistry<Key>) BuiltInRegistries.OPTIMIZED_ITEM_ID)
                                    .register(new ResourceKey<>(BuiltInRegistries.OPTIMIZED_ITEM_ID.key().location(), id), id));
                    Object resourceLocation = KeyUtils.toResourceLocation(id.namespace(), id.value());
                    Object mcHolder = ((Optional<Object>) Reflections.method$Registry$getHolder1.invoke(Reflections.instance$BuiltInRegistries$ITEM, Reflections.method$ResourceKey$create.invoke(null, Reflections.instance$Registries$ITEM, resourceLocation))).get();
                    Set<Object> tags = (Set<Object>) Reflections.field$Holder$Reference$tags.get(mcHolder);
                    for (Object tag : tags) {
                        Key tagId = Key.of(Reflections.field$TagKey$location.get(tag).toString());
                        VANILLA_ITEM_TAGS.computeIfAbsent(tagId, (key) -> new ArrayList<>()).add(holder);
                    }
                }
            }
        } catch (ReflectiveOperationException e) {
            plugin.logger().warn("Failed to init vanilla items", e);
        }
    }
}
