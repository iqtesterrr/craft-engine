package net.momirealms.craftengine.bukkit.entity.furniture;

import net.momirealms.craftengine.bukkit.entity.DisplayEntityData;
import net.momirealms.craftengine.bukkit.item.BukkitItemManager;
import net.momirealms.craftengine.bukkit.util.Reflections;
import net.momirealms.craftengine.core.entity.furniture.AbstractFurnitureElement;
import net.momirealms.craftengine.core.entity.furniture.Billboard;
import net.momirealms.craftengine.core.entity.furniture.ItemDisplayContext;
import net.momirealms.craftengine.core.item.Item;
import net.momirealms.craftengine.core.plugin.CraftEngine;
import net.momirealms.craftengine.core.util.Key;
import net.momirealms.craftengine.core.util.QuaternionUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class BukkitFurnitureElement extends AbstractFurnitureElement {
    private List<Object> cachedValues;

    public BukkitFurnitureElement(Key item,
                                  Billboard billboard,
                                  ItemDisplayContext transform,
                                  Vector3f scale,
                                  Vector3f translation,
                                  Vector3f offset,
                                  Quaternionf rotation) {
        super(item, billboard, transform, scale, translation, offset, rotation);
    }

    @Override
    public void addSpawnPackets(int entityId, double x, double y, double z, float yaw, Consumer<Object> packets) {
        try {
            Vector3f offset = QuaternionUtils.toQuaternionf(0, Math.toRadians(180 - yaw), 0).conjugate().transform(new Vector3f(position()));
            packets.accept(Reflections.constructor$ClientboundAddEntityPacket.newInstance(
                    entityId, UUID.randomUUID(), x + offset.x, y + offset.y, z - offset.z, 0, yaw,
                    Reflections.instance$EntityType$ITEM_DISPLAY, 0, Reflections.instance$Vec3$Zero, 0
            ));
            packets.accept(Reflections.constructor$ClientboundSetEntityDataPacket.newInstance(entityId, getCachedValues()));
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Failed to construct element spawn packet", e);
        }
    }

    private synchronized List<Object> getCachedValues() {
        if (this.cachedValues == null) {
            this.cachedValues = new ArrayList<>();
            Item<ItemStack> item = BukkitItemManager.instance().createWrappedItem(item(), null);
            if (item == null) {
                CraftEngine.instance().logger().warn("Failed to create furniture element for " + item() + " because item " + item() + " not found");
                item = BukkitItemManager.instance().wrap(new ItemStack(Material.STONE));
            }
            DisplayEntityData.DisplayedItem.addEntityDataIfNotDefaultValue(item.getLiteralObject(), this.cachedValues);
            DisplayEntityData.Scale.addEntityDataIfNotDefaultValue(scale(), this.cachedValues);
            DisplayEntityData.RotationLeft.addEntityDataIfNotDefaultValue(rotation(), this.cachedValues);
            DisplayEntityData.BillboardConstraints.addEntityDataIfNotDefaultValue(billboard().id(), this.cachedValues);
            DisplayEntityData.Translation.addEntityDataIfNotDefaultValue(translation(), this.cachedValues);
            DisplayEntityData.DisplayType.addEntityDataIfNotDefaultValue(transform().id(), this.cachedValues);
        }
        return this.cachedValues;
    }
}
