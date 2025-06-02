package net.momirealms.craftengine.bukkit.entity.data;

import net.momirealms.craftengine.bukkit.plugin.reflection.minecraft.MBlocks;

public class BlockDisplayEntityData<T> extends DisplayEntityData<T> {
    // Block display only
    public static final DisplayEntityData<Object> DisplayedBlock = of(23, EntityDataValue.Serializers$BLOCK_STATE, MBlocks.AIR$defaultState);

    public BlockDisplayEntityData(int id, Object serializer, T defaultValue) {
        super(id, serializer, defaultValue);
    }
}
