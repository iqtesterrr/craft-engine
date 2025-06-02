package net.momirealms.craftengine.bukkit.entity.data;

import net.momirealms.craftengine.bukkit.plugin.reflection.minecraft.MItems;

public class ItemDisplayEntityData<T> extends DisplayEntityData<T> {
    // Item display only
    public static final DisplayEntityData<Object> DisplayedItem = of(23, EntityDataValue.Serializers$ITEM_STACK, MItems.Air$ItemStack);
    /**
     * Display type:
     * 0 = NONE
     * 1 = THIRD_PERSON_LEFT_HAND
     * 2 = THIRD_PERSON_RIGHT_HAND
     * 3 = FIRST_PERSON_LEFT_HAND
     * 4 = FIRST_PERSON_RIGHT_HAND
     * 5 = HEAD
     * 6 = GUI
     * 7 = GROUND
     * 8 = FIXED
     */
    public static final DisplayEntityData<Byte> DisplayType = of(24, EntityDataValue.Serializers$BYTE, (byte) 0);

    public ItemDisplayEntityData(int id, Object serializer, T defaultValue) {
        super(id, serializer, defaultValue);
    }
}
