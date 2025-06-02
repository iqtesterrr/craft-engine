package net.momirealms.craftengine.bukkit.entity.data;

import net.momirealms.craftengine.bukkit.plugin.reflection.minecraft.CoreReflections;

import java.util.Optional;

public class BaseEntityData<T> extends SimpleEntityData<T> {
    public static final BaseEntityData<Byte> SharedFlags = new BaseEntityData<>(0, EntityDataValue.Serializers$BYTE, (byte) 0);
    public static final BaseEntityData<Integer> AirSupply = new BaseEntityData<>(1, EntityDataValue.Serializers$INT, 300);
    public static final BaseEntityData<Optional<Object>> CustomName = new BaseEntityData<>(2, EntityDataValue.Serializers$OPTIONAL_COMPONENT, Optional.empty());
    public static final BaseEntityData<Boolean> CustomNameVisible = new BaseEntityData<>(3, EntityDataValue.Serializers$BOOLEAN, false);
    public static final BaseEntityData<Boolean> Silent = new BaseEntityData<>(4, EntityDataValue.Serializers$BOOLEAN, false);
    public static final BaseEntityData<Boolean> NoGravity = new BaseEntityData<>(5, EntityDataValue.Serializers$BOOLEAN, false);
    public static final BaseEntityData<Object> Pose = new BaseEntityData<>(6, EntityDataValue.Serializers$POSE, CoreReflections.instance$Pose$STANDING);
    public static final BaseEntityData<Integer> TicksFrozen = new BaseEntityData<>(7, EntityDataValue.Serializers$INT, 0);

    public BaseEntityData(int id, Object serializer, T defaultValue) {
        super(id, serializer, defaultValue);
    }
}
