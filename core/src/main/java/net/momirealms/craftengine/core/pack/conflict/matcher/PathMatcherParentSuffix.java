package net.momirealms.craftengine.core.pack.conflict.matcher;

import net.momirealms.craftengine.core.pack.conflict.PathContext;
import net.momirealms.craftengine.core.plugin.context.Condition;
import net.momirealms.craftengine.core.plugin.context.condition.ConditionFactory;
import net.momirealms.craftengine.core.plugin.locale.LocalizedException;
import net.momirealms.craftengine.core.util.CharacterUtils;
import net.momirealms.craftengine.core.util.Key;
import net.momirealms.craftengine.core.util.ResourceConfigUtils;

import java.nio.file.Path;
import java.util.Map;

public class PathMatcherParentSuffix implements Condition<PathContext> {
    private final String suffix;

    public PathMatcherParentSuffix(String suffix) {
        this.suffix = suffix;
    }

    @Override
    public boolean test(PathContext path) {
        Path parent = path.path().getParent();
        if (parent == null) return false;
        String pathStr = CharacterUtils.replaceBackslashWithSlash(parent.toString());
        return pathStr.endsWith(suffix);
    }

    @Override
    public Key type() {
        return PathMatchers.PARENT_PATH_SUFFIX;
    }

    public static class FactoryImpl implements ConditionFactory<PathContext> {

        @Override
        public Condition<PathContext> create(Map<String, Object> arguments) {
            String suffix = ResourceConfigUtils.requireNonEmptyStringOrThrow(arguments.get("suffix"), () -> new LocalizedException("warning.config.conflict_matcher.parent_suffix.missing_suffix"));
            return new PathMatcherParentSuffix(suffix);
        }
    }
}
