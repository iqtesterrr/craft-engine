package net.momirealms.craftengine.bukkit.plugin;

import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import io.papermc.paper.plugin.bootstrap.PluginProviderContext;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import net.momirealms.craftengine.bukkit.plugin.agent.RuntimePatcher;
import net.momirealms.craftengine.bukkit.plugin.classpath.PaperClassPathAppender;
import net.momirealms.craftengine.core.plugin.logger.PluginLogger;
import net.momirealms.craftengine.core.plugin.logger.Slf4jPluginLogger;
import net.momirealms.craftengine.core.util.ReflectionUtils;
import net.momirealms.craftengine.core.util.VersionHelper;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.util.Objects;

@SuppressWarnings("UnstableApiUsage")
public class PaperCraftEngineBootstrap implements PluginBootstrap {
    private static final Class<?> clazz$PluginProviderContext = PluginProviderContext.class;
    private static final Class<?> clazz$ComponentLogger = Objects.requireNonNull(
            ReflectionUtils.getClazz(
                    "net{}kyori{}adventure{}text{}logger{}slf4j{}ComponentLogger".replace("{}", ".")
            )
    );
    private static final Method method$PluginProviderContext$getLogger = Objects.requireNonNull(
            ReflectionUtils.getMethod(
                    clazz$PluginProviderContext, clazz$ComponentLogger, new String[] { "getLogger" }
            )
    );
    protected BukkitCraftEngine plugin;

    @Override
    public void bootstrap(@NotNull BootstrapContext context) {
        PluginLogger logger;
        try {
            logger = new Slf4jPluginLogger((org.slf4j.Logger) method$PluginProviderContext$getLogger.invoke(context));
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Failed to getLogger", e);
        }
        this.plugin = new BukkitCraftEngine(
                logger,
                context.getDataDirectory(),
                new PaperClassPathAppender(this.getClass().getClassLoader())
        );
        this.plugin.applyDependencies();
        this.plugin.setUpConfig();
        if (VersionHelper.isOrAbove1_21_4()) {
            new ModernEventHandler(context, this.plugin).register();
        } else {
            try {
                RuntimePatcher.patch(this.plugin);
            } catch (Exception e) {
                throw new RuntimeException("Failed to patch server", e);
            }
        }
    }

    @Override
    public @NotNull JavaPlugin createPlugin(@NotNull PluginProviderContext context) {
        return new PaperCraftEnginePlugin(this);
    }

    public static class ModernEventHandler {
        private final BootstrapContext context;
        private final BukkitCraftEngine plugin;

        public ModernEventHandler(BootstrapContext context, BukkitCraftEngine plugin) {
            this.context = context;
            this.plugin = plugin;
        }

        public void register() {
            this.context.getLifecycleManager().registerEventHandler(LifecycleEvents.DATAPACK_DISCOVERY, (e) -> {
                try {
                    this.plugin.injectRegistries();
                } catch (Throwable ex) {
                    this.plugin.logger().warn("Failed to inject registries", ex);
                }
            });
        }
    }
}
