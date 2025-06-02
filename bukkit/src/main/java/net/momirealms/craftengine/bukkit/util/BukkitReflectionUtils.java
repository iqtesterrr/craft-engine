package net.momirealms.craftengine.bukkit.util;

import net.momirealms.craftengine.core.util.ReflectionUtils;
import net.momirealms.craftengine.core.util.VersionHelper;

import java.util.List;
import java.util.function.Function;

public final class BukkitReflectionUtils {
    private static final String PREFIX_MC = "net.minecraft.";
    private static final String PREFIX_CRAFTBUKKIT = "org.bukkit.craftbukkit";
    private static final String CRAFT_SERVER = "CraftServer";
    private static final String CB_PKG_VERSION;

    private BukkitReflectionUtils() {}

    static {
        if (VersionHelper.isMojmap()) {
            CB_PKG_VERSION = ".";
        } else {
            String name;
            label: {
                for (int i = 0; i <= VersionHelper.minorVersion(); i++) {
                    try {
                        name = ".v1_" + VersionHelper.majorVersion() + "_R" + i + ".";
                        Class.forName(PREFIX_CRAFTBUKKIT + name + CRAFT_SERVER);
                        break label;
                    } catch (ClassNotFoundException ignored) {
                    }
                }
                throw new RuntimeException("Could not find CraftServer version");
            }
            CB_PKG_VERSION = name;
        }
    }

    public static String assembleCBClass(String className) {
        return PREFIX_CRAFTBUKKIT + CB_PKG_VERSION + className;
    }

    public static String assembleMCClass(String className) {
        return PREFIX_MC + className;
    }

    public static Class<?> findReobfOrMojmapClass(String reobf, String mojmap) {
        return findReobfOrMojmapClass(reobf, mojmap, BukkitReflectionUtils::assembleMCClass);
    }

    public static Class<?> findReobfOrMojmapClass(String reobf, String mojmap, Function<String, String> classDecorator) {
        if (VersionHelper.isMojmap()) return ReflectionUtils.getClazz(classDecorator.apply(mojmap));
        else return ReflectionUtils.getClazz(classDecorator.apply(reobf));
    }

    public static Class<?> findReobfOrMojmapClass(List<String> reobf, String mojmap) {
        return findReobfOrMojmapClass(reobf, mojmap, BukkitReflectionUtils::assembleMCClass);
    }

    public static Class<?> findReobfOrMojmapClass(List<String> reobf, String mojmap, Function<String, String> classDecorator) {
        if (VersionHelper.isMojmap()) return ReflectionUtils.getClazz(classDecorator.apply(mojmap));
        else return ReflectionUtils.getClazz(reobf.stream().map(classDecorator).toList().toArray(new String[0]));
    }

    public static Class<?> findReobfOrMojmapClass(String reobf, List<String> mojmap) {
        return findReobfOrMojmapClass(reobf, mojmap, BukkitReflectionUtils::assembleMCClass);
    }

    public static Class<?> findReobfOrMojmapClass(String reobf, List<String> mojmap, Function<String, String> classDecorator) {
        if (VersionHelper.isMojmap()) return ReflectionUtils.getClazz(mojmap.stream().map(classDecorator).toList().toArray(new String[0]));
        else return ReflectionUtils.getClazz(classDecorator.apply(reobf));
    }

    public static Class<?> findReobfOrMojmapClass(List<String> reobf, List<String> mojmap) {
        return findReobfOrMojmapClass(reobf, mojmap, BukkitReflectionUtils::assembleMCClass);
    }

    public static Class<?> findReobfOrMojmapClass(List<String> reobf, List<String> mojmap, Function<String, String> classDecorator) {
        String[] classes = VersionHelper.isMojmap()
                ? mojmap.stream().map(classDecorator).toList().toArray(new String[0])
                : reobf.stream().map(classDecorator).toList().toArray(new String[0]);
        return ReflectionUtils.getClazz(classes);
    }
}
