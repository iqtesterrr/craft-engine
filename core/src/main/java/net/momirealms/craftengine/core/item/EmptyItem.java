//package net.momirealms.craftengine.core.item;
//
//import com.google.gson.JsonElement;
//import net.momirealms.craftengine.core.item.behavior.ItemBehavior;
//import net.momirealms.craftengine.core.util.Key;
//
//import java.util.List;
//import java.util.Optional;
//
//public class EmptyItem<I> implements Item<I> {
//    private final I item;
//
//    public EmptyItem(I item) {
//        this.item = item;
//    }
//
//    @Override
//    public Item<I> addEnchantment(Enchantment enchantment) {
//        return this;
//    }
//
//    @Override
//    public Optional<CustomItem<I>> getCustomItem() {
//        return Optional.empty();
//    }
//
//    @Override
//    public Optional<List<ItemBehavior>> getItemBehavior() {
//        return Optional.empty();
//    }
//
//    @Override
//    public boolean isCustomItem() {
//        return false;
//    }
//
//    @Override
//    public boolean isBlockItem() {
//        return false;
//    }
//
//    @Override
//    public Key id() {
//        return ItemKeys.AIR;
//    }
//
//    @Override
//    public Key vanillaId() {
//        return ItemKeys.AIR;
//    }
//
//    @Override
//    public Optional<Key> customId() {
//        return Optional.empty();
//    }
//
//    @Override
//    public Item<I> customId(Key id) {
//        return this;
//    }
//
//    @Override
//    public int count() {
//        return 0;
//    }
//
//    @Override
//    public Item<I> count(int amount) {
//        return this;
//    }
//
//    @Override
//    public Item<I> trim(Trim trim) {
//        return this;
//    }
//
//    @Override
//    public Optional<Trim> trim() {
//        return Optional.empty();
//    }
//
//    @Override
//    public Item<I> customModelData(Integer data) {
//        return this;
//    }
//
//    @Override
//    public Optional<Integer> customModelData() {
//        return Optional.empty();
//    }
//
//    @Override
//    public Item<I> damage(Integer data) {
//        return this;
//    }
//
//    @Override
//    public Optional<Integer> damage() {
//        return Optional.empty();
//    }
//
//    @Override
//    public Item<I> repairCost(Integer data) {
//        return this;
//    }
//
//    @Override
//    public Optional<Integer> repairCost() {
//        return Optional.empty();
//    }
//
//    @Override
//    public Item<I> maxDamage(Integer data) {
//        return this;
//    }
//
//    @Override
//    public Optional<Integer> maxDamage() {
//        return Optional.empty();
//    }
//
//    @Override
//    public Item<I> dyedColor(Integer data) {
//        return this;
//    }
//
//    @Override
//    public Optional<Integer> dyedColor() {
//        return Optional.empty();
//    }
//
//    @Override
//    public Item<I> customName(String displayName) {
//        return this;
//    }
//
//    @Override
//    public Optional<String> customName() {
//        return Optional.empty();
//    }
//
//    @Override
//    public Item<I> itemName(String itemName) {
//        return this;
//    }
//
//    @Override
//    public Optional<String> itemName() {
//        return Optional.empty();
//    }
//
//    @Override
//    public Item<I> itemModel(String itemModel) {
//        return this;
//    }
//
//    @Override
//    public Optional<String> itemModel() {
//        return Optional.empty();
//    }
//
//    @Override
//    public Item<I> tooltipStyle(String tooltipStyle) {
//        return this;
//    }
//
//    @Override
//    public Optional<String> tooltipStyle() {
//        return Optional.empty();
//    }
//
//    @Override
//    public Item<I> lore(List<String> lore) {
//        return this;
//    }
//
//    @Override
//    public Optional<JukeboxPlayable> jukeboxSong() {
//        return Optional.empty();
//    }
//
//    @Override
//    public Item<I> jukeboxSong(JukeboxPlayable song) {
//        return this;
//    }
//
//    @Override
//    public Optional<EquipmentData> equippable() {
//        return Optional.empty();
//    }
//
//    @Override
//    public Item<I> equippable(EquipmentData equipmentData) {
//        return this;
//    }
//
//    @Override
//    public Optional<List<String>> lore() {
//        return Optional.empty();
//    }
//
//    @Override
//    public Item<I> unbreakable(boolean unbreakable) {
//        return this;
//    }
//
//    @Override
//    public boolean unbreakable() {
//        return false;
//    }
//
//    @Override
//    public Item<I> skull(String data) {
//        return this;
//    }
//
//    @Override
//    public Optional<Enchantment> getEnchantment(Key enchantmentId) {
//        return Optional.empty();
//    }
//
//    @Override
//    public Item<I> setEnchantments(List<Enchantment> enchantments) {
//        return this;
//    }
//
//    @Override
//    public Item<I> setStoredEnchantments(List<Enchantment> enchantments) {
//        return this;
//    }
//
//    @Override
//    public Item<I> addStoredEnchantment(Enchantment enchantment) {
//        return this;
//    }
//
//    @Override
//    public Item<I> itemFlags(List<String> flags) {
//        return this;
//    }
//
//    @Override
//    public Object getTag(Object... path) {
//        return null;
//    }
//
//    @Override
//    public Item<I> setTag(Object value, Object... path) {
//        return this;
//    }
//
//    @Override
//    public boolean hasTag(Object... path) {
//        return false;
//    }
//
//    @Override
//    public boolean removeTag(Object... path) {
//        return false;
//    }
//
//    @Override
//    public boolean hasComponent(Object type) {
//        return false;
//    }
//
//    @Override
//    public void removeComponent(Object type) {
//    }
//
//    @Override
//    public Object getComponent(Object type) {
//        return null;
//    }
//
//    @Override
//    public Object getJavaTypeComponent(Object type) {
//        return null;
//    }
//
//    @Override
//    public JsonElement getJsonTypeComponent(Object type) {
//        return null;
//    }
//
//    @Override
//    public void setComponent(Object type, Object value) {
//    }
//
//    @Override
//    public void resetComponent(Object type) {
//    }
//
//    @Override
//    public I getItem() {
//        return this.item;
//    }
//
//    @Override
//    public I load() {
//        return this.item;
//    }
//
//    @Override
//    public int maxStackSize() {
//        return 0;
//    }
//
//    @Override
//    public Item<I> maxStackSize(int amount) {
//        return this;
//    }
//
//    @Override
//    public Item<I> copyWithCount(int count) {
//        return this;
//    }
//
//    @Override
//    public boolean is(Key itemTag) {
//        return false;
//    }
//
//    @Override
//    public Object getLiteralObject() {
//        return null;
//    }
//
//    @Override
//    public Item<I> mergeCopy(Item<?> another) {
//        return this;
//    }
//
//    @Override
//    public void merge(Item<I> another) {
//    }
//
//    @Override
//    public byte[] toByteArray() {
//        return new byte[0];
//    }
//}
