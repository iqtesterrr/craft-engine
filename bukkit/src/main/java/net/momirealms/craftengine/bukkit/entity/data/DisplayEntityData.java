package net.momirealms.craftengine.bukkit.entity.data;

import net.momirealms.craftengine.core.util.VersionHelper;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class DisplayEntityData<T> extends BaseEntityData<T> {
    // Display only
    public static final DisplayEntityData<Integer> InterpolationDelay = of(8, EntityDataValue.Serializers$INT, 0);

    // 1.19.4-1.20.1
    public static final DisplayEntityData<Integer> InterpolationDuration = of(9, EntityDataValue.Serializers$INT, 0);

    // 1.20.2+
    public static final DisplayEntityData<Integer> TransformationInterpolationDuration = of(9, EntityDataValue.Serializers$INT, 0);
    public static final DisplayEntityData<Integer> PositionRotationInterpolationDuration = of(10, EntityDataValue.Serializers$INT, 0);

    public static final DisplayEntityData<Object> Translation = of(11, EntityDataValue.Serializers$VECTOR3, new Vector3f(0f));
    public static final DisplayEntityData<Object> Scale = of(12, EntityDataValue.Serializers$VECTOR3, new Vector3f(1f));
    public static final DisplayEntityData<Object> RotationLeft = of(13, EntityDataValue.Serializers$QUATERNION, new Quaternionf(0f, 0f, 0f, 1f));
    public static final DisplayEntityData<Object> RotationRight = of(14, EntityDataValue.Serializers$QUATERNION, new Quaternionf(0f, 0f, 0f, 1f));
    /**
     * 	Billboard Constraints (0 = FIXED, 1 = VERTICAL, 2 = HORIZONTAL, 3 = CENTER)
     */
    public static final DisplayEntityData<Byte> BillboardConstraints = of(15, EntityDataValue.Serializers$BYTE, (byte) 0);
    /**
     * Brightness override (blockLight << 4 | skyLight << 20)
     */
    public static final DisplayEntityData<Integer> BrightnessOverride = of(16, EntityDataValue.Serializers$INT, -1);
    public static final DisplayEntityData<Float> ViewRange = of(17, EntityDataValue.Serializers$FLOAT, 1f);
    public static final DisplayEntityData<Float> ShadowRadius = of(18, EntityDataValue.Serializers$FLOAT, 0f);
    public static final DisplayEntityData<Float> ShadowStrength = of(19, EntityDataValue.Serializers$FLOAT, 0f);
    public static final DisplayEntityData<Float> Width = of(20, EntityDataValue.Serializers$FLOAT, 0f);
    public static final DisplayEntityData<Float> Height = of(21, EntityDataValue.Serializers$FLOAT, 0f);
    public static final DisplayEntityData<Integer> GlowColorOverride = of(22, EntityDataValue.Serializers$INT, -1);

    public static <T> DisplayEntityData<T> of(final int id, final Object serializer, T defaultValue) {
        return new DisplayEntityData<>(id, serializer, defaultValue);
    }

    public DisplayEntityData(int id, Object serializer, T defaultValue) {
        super(!VersionHelper.isOrAbove1_20_2() && id >= 11 ? id - 1 : id, serializer, defaultValue);
    }
}
