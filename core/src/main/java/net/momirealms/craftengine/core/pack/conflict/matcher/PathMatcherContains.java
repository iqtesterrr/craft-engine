package net.momirealms.craftengine.core.pack.conflict.matcher;

import net.momirealms.craftengine.core.pack.conflict.PathContext;
import net.momirealms.craftengine.core.plugin.context.Condition;
import net.momirealms.craftengine.core.plugin.context.condition.ConditionFactory;
import net.momirealms.craftengine.core.plugin.locale.LocalizedException;
import net.momirealms.craftengine.core.util.CharacterUtils;
import net.momirealms.craftengine.core.util.Key;
import net.momirealms.craftengine.core.util.ResourceConfigUtils;

import java.util.Map;

public class PathMatcherContains implements Condition<PathContext> {
    private final String path;

    public PathMatcherContains(String path) {
        this.path = path;
    }

    @Override
    public boolean test(PathContext path) {
        String pathStr = CharacterUtils.replaceBackslashWithSlash(path.path().toString());
        return pathStr.contains(this.path);
    }

    @Override
    public Key type() {
        return PathMatchers.CONTAINS;
    }

    public static class FactoryImpl implements ConditionFactory<PathContext> {

        @Override
        public Condition<PathContext> create(Map<String, Object> arguments) {
            String path = ResourceConfigUtils.requireNonEmptyStringOrThrow(arguments.get("path"), () -> new LocalizedException("warning.config.conflict_matcher.contains.missing_path"));
            return new PathMatcherContains(path);
        }
    }
}
