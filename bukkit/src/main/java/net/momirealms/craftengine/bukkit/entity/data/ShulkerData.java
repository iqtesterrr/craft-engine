package net.momirealms.craftengine.bukkit.entity.data;

import net.momirealms.craftengine.bukkit.plugin.reflection.minecraft.CoreReflections;

public class ShulkerData<T> extends MobData<T> {
    public static final ShulkerData<Object> AttachFace = new ShulkerData<>(16, EntityDataValue.Serializers$DIRECTION, CoreReflections.instance$Direction$DOWN);
    public static final ShulkerData<Byte> Peek = new ShulkerData<>(17, EntityDataValue.Serializers$BYTE, (byte) 0);
    public static final ShulkerData<Byte> Color = new ShulkerData<>(18, EntityDataValue.Serializers$BYTE, (byte) 16);

    public ShulkerData(int id, Object serializer, T defaultValue) {
        super(id, serializer, defaultValue);
    }
}