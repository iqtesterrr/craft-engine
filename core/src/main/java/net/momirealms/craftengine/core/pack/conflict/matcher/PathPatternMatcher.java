package net.momirealms.craftengine.core.pack.conflict.matcher;

import net.momirealms.craftengine.core.pack.conflict.PathContext;
import net.momirealms.craftengine.core.plugin.context.Condition;
import net.momirealms.craftengine.core.plugin.context.condition.ConditionFactory;
import net.momirealms.craftengine.core.plugin.locale.LocalizedException;
import net.momirealms.craftengine.core.util.CharacterUtils;
import net.momirealms.craftengine.core.util.Key;
import net.momirealms.craftengine.core.util.ResourceConfigUtils;

import java.util.Map;
import java.util.regex.Pattern;

public class PathPatternMatcher implements Condition<PathContext> {
    private final Pattern pattern;

    public PathPatternMatcher(String pattern) {
        this.pattern = Pattern.compile(pattern);
    }

    public PathPatternMatcher(Pattern pattern) {
        this.pattern = pattern;
    }

    @Override
    public boolean test(PathContext path) {
        String pathStr = CharacterUtils.replaceBackslashWithSlash(path.path().toString());
        return this.pattern.matcher(pathStr).matches();
    }

    @Override
    public Key type() {
        return PathMatchers.PATTERN;
    }

    public Pattern pattern() {
        return pattern;
    }

    public static class FactoryImpl implements ConditionFactory<PathContext> {

        @Override
        public Condition<PathContext> create(Map<String, Object> arguments) {
            String pattern = ResourceConfigUtils.requireNonEmptyStringOrThrow(arguments.get("pattern"), () -> new LocalizedException("warning.config.conflict_matcher.pattern.missing_pattern"));
            return new PathPatternMatcher(pattern);
        }
    }
}
