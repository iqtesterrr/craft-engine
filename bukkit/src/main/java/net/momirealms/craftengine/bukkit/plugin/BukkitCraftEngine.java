package net.momirealms.craftengine.bukkit.plugin;

import net.momirealms.antigrieflib.AntiGriefLib;
import net.momirealms.craftengine.bukkit.api.event.CraftEngineReloadEvent;
import net.momirealms.craftengine.bukkit.block.BukkitBlockManager;
import net.momirealms.craftengine.bukkit.block.behavior.BukkitBlockBehaviors;
import net.momirealms.craftengine.bukkit.compatibility.papi.PlaceholderAPIUtils;
import net.momirealms.craftengine.bukkit.entity.furniture.BukkitFurnitureManager;
import net.momirealms.craftengine.bukkit.entity.furniture.hitbox.BukkitHitBoxTypes;
import net.momirealms.craftengine.bukkit.font.BukkitFontManager;
import net.momirealms.craftengine.bukkit.item.BukkitItemManager;
import net.momirealms.craftengine.bukkit.item.behavior.BukkitItemBehaviors;
import net.momirealms.craftengine.bukkit.item.recipe.BukkitRecipeManager;
import net.momirealms.craftengine.bukkit.loot.BukkitVanillaLootManager;
import net.momirealms.craftengine.bukkit.pack.BukkitPackManager;
import net.momirealms.craftengine.bukkit.plugin.command.BukkitCommandManager;
import net.momirealms.craftengine.bukkit.plugin.command.BukkitSenderFactory;
import net.momirealms.craftengine.bukkit.plugin.gui.BukkitGuiManager;
import net.momirealms.craftengine.bukkit.plugin.injector.BukkitInjector;
import net.momirealms.craftengine.bukkit.plugin.network.BukkitNetworkManager;
import net.momirealms.craftengine.bukkit.plugin.scheduler.BukkitSchedulerAdapter;
import net.momirealms.craftengine.bukkit.plugin.user.BukkitServerPlayer;
import net.momirealms.craftengine.bukkit.sound.BukkitSoundManager;
import net.momirealms.craftengine.bukkit.util.EventUtils;
import net.momirealms.craftengine.bukkit.util.Reflections;
import net.momirealms.craftengine.bukkit.world.BukkitWorldManager;
import net.momirealms.craftengine.core.entity.player.Player;
import net.momirealms.craftengine.core.item.ItemManager;
import net.momirealms.craftengine.core.plugin.CraftEngine;
import net.momirealms.craftengine.core.plugin.classpath.ReflectionClassPathAppender;
import net.momirealms.craftengine.core.plugin.command.sender.SenderFactory;
import net.momirealms.craftengine.core.plugin.config.Config;
import net.momirealms.craftengine.core.plugin.dependency.Dependencies;
import net.momirealms.craftengine.core.plugin.dependency.Dependency;
import net.momirealms.craftengine.core.plugin.gui.category.ItemBrowserManagerImpl;
import net.momirealms.craftengine.core.plugin.logger.JavaPluginLogger;
import net.momirealms.craftengine.core.plugin.scheduler.SchedulerAdapter;
import net.momirealms.craftengine.core.plugin.scheduler.SchedulerTask;
import net.momirealms.craftengine.core.util.ReflectionUtils;
import net.momirealms.craftengine.core.util.VersionHelper;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Pose;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.lang.reflect.Field;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("unchecked")
public class BukkitCraftEngine extends CraftEngine {
    private static BukkitCraftEngine instance;
    private final JavaPlugin bootstrap;
    private SchedulerTask tickTask;
    private boolean successfullyLoaded = false;
    private boolean successfullyEnabled = false;
    private boolean requiresRestart = false;
    private boolean hasMod = false;
    private AntiGriefLib antiGrief;
    private boolean hasPlaceholderAPI;

    public BukkitCraftEngine(JavaPlugin bootstrap) {
        super((p) -> {
            CraftEngineReloadEvent event = new CraftEngineReloadEvent((BukkitCraftEngine) p);
            EventUtils.fireAndForget(event);
        });
        instance = this;
        this.bootstrap = bootstrap;
        super.classPathAppender = new ReflectionClassPathAppender(this);
        super.scheduler = new BukkitSchedulerAdapter(this);
        super.logger = new JavaPluginLogger(bootstrap.getLogger());
        // find mod class if present
        Class<?> modClass = ReflectionUtils.getClazz(MOD_CLASS);
        if (modClass != null) {
            Field isSuccessfullyRegistered = ReflectionUtils.getDeclaredField(modClass, "isSuccessfullyRegistered");
            try {
                requiresRestart = !(boolean) isSuccessfullyRegistered.get(null);
                hasMod = true;
            } catch (Exception ignore) {
            }
        }
    }

    @Override
    public void onPluginLoad() {
        super.onPluginLoad();
        Reflections.init();
        BukkitInjector.init();
        super.networkManager = new BukkitNetworkManager(this);
        super.blockManager = new BukkitBlockManager(this);
        super.furnitureManager = new BukkitFurnitureManager(this);
        this.successfullyLoaded = true;
    }

    @Override
    protected List<Dependency> platformDependencies() {
        return List.of(
                Dependencies.BSTATS_BUKKIT,
                Dependencies.CLOUD_BUKKIT, Dependencies.CLOUD_PAPER, Dependencies.CLOUD_BRIGADIER, Dependencies.CLOUD_MINECRAFT_EXTRAS
        );
    }

    @Override
    public void onPluginEnable() {
        if (this.successfullyEnabled) {
            logger().severe(" ");
            logger().severe(" ");
            logger().severe(" ");
            logger().severe("Please do not restart plugins at runtime.");
            logger().severe(" ");
            logger().severe(" ");
            logger().severe(" ");
            Bukkit.getPluginManager().disablePlugin(this.bootstrap);
            return;
        }
        this.successfullyEnabled = true;
        if (this.hasMod && this.requiresRestart) {
            logger().warn(" ");
            logger().warn(" ");
            logger().warn(" ");
            logger().warn("This is the first time you have installed CraftEngine. A restart is required to apply the changes.");
            logger().warn(" ");
            logger().warn(" ");
            logger().warn(" ");
            Bukkit.getServer().shutdown();
            return;
        }
        if (!this.successfullyLoaded) {
            logger().severe(" ");
            logger().severe(" ");
            logger().severe(" ");
            logger().severe("Failed to enable CraftEngine. Please check the log on loading stage.");
            logger().severe("To reduce the loss caused by plugin not loaded, now shutting down the server");
            logger().severe(" ");
            logger().severe(" ");
            logger().severe(" ");
            Bukkit.getServer().shutdown();
            return;
        }
        BukkitBlockBehaviors.init();
        BukkitItemBehaviors.init();
        BukkitHitBoxTypes.init();
        super.packManager = new BukkitPackManager(this);
        super.senderFactory = new BukkitSenderFactory(this);
        super.itemManager = new BukkitItemManager(this);
        super.recipeManager = new BukkitRecipeManager(this);
        super.commandManager = new BukkitCommandManager(this);
        super.itemBrowserManager = new ItemBrowserManagerImpl(this);
        super.guiManager = new BukkitGuiManager(this);
        super.worldManager = new BukkitWorldManager(this);
        super.soundManager = new BukkitSoundManager(this);
        super.vanillaLootManager = new BukkitVanillaLootManager(this);
        super.fontManager = new BukkitFontManager(this);
        super.onPluginEnable();
        // compatibility
        // register expansion
        if (this.isPluginEnabled("PlaceholderAPI")) {
            PlaceholderAPIUtils.registerExpansions(this);
            this.hasPlaceholderAPI = true;
        }
    }

    @Override
    public void onPluginDisable() {
        super.onPluginDisable();
        if (this.tickTask != null) this.tickTask.cancel();
        if (!Bukkit.getServer().isStopping()) {
            logger().severe(" ");
            logger().severe(" ");
            logger().severe(" ");
            logger().severe("Please do not disable plugins at runtime.");
            logger().severe(" ");
            logger().severe(" ");
            logger().severe(" ");
            Bukkit.getServer().shutdown();
        }
    }

    @Override
    public void platformDelayedEnable() {
        if (Config.metrics()) {
            new Metrics(this.bootstrap(), 24333);
        }
        // tick task
        if (VersionHelper.isFolia()) {
            this.tickTask = this.scheduler().sync().runRepeating(() -> {
                for (BukkitServerPlayer serverPlayer : networkManager().onlineUsers()) {
                    org.bukkit.entity.Player player = serverPlayer.platformPlayer();
                    Location location = player.getLocation();
                    scheduler().sync().run(serverPlayer::tick, player.getWorld(), location.getBlockX() >> 4, location.getBlockZ() >> 4);
                }
            }, 1, 1);
        } else {
            this.tickTask = this.scheduler().sync().runRepeating(() -> {
                for (BukkitServerPlayer serverPlayer : networkManager().onlineUsers()) {
                    serverPlayer.tick();
                }
            }, 1, 1);
        }
    }

    @Override
    public InputStream resourceStream(String filePath) {
        return bootstrap.getResource(filePath.replace("\\", "/"));
    }

    @Override
    public File dataFolderFile() {
        return bootstrap().getDataFolder();
    }

    @Override
    public Path dataFolderPath() {
        return bootstrap().getDataFolder().toPath().toAbsolutePath();
    }

    @SuppressWarnings("deprecation")
    @Override
    public String pluginVersion() {
        return bootstrap().getDescription().getVersion();
    }

    @Override
    public String serverVersion() {
        return Bukkit.getServer().getBukkitVersion().split("-")[0];
    }

    @Override
    public SchedulerAdapter<World> scheduler() {
        return (SchedulerAdapter<World>) scheduler;
    }

    @Override
    public ItemManager<ItemStack> itemManager() {
        return (ItemManager<ItemStack>) itemManager;
    }

    @Override
    public BukkitBlockManager blockManager() {
        return (BukkitBlockManager) blockManager;
    }

    @Override
    public BukkitFurnitureManager furnitureManager() {
        return (BukkitFurnitureManager) furnitureManager;
    }

    @Override
    public SenderFactory<CraftEngine, CommandSender> senderFactory() {
        return (SenderFactory<CraftEngine, CommandSender>) senderFactory;
    }

    public JavaPlugin bootstrap() {
        return bootstrap;
    }

    public static BukkitCraftEngine instance() {
        return instance;
    }

    @Override
    public boolean hasPlaceholderAPI() {
        return this.hasPlaceholderAPI;
    }

    @Override
    public boolean isPluginEnabled(String plugin) {
        return Bukkit.getPluginManager().isPluginEnabled(plugin);
    }

    @Override
    public String parse(Player player, String text) {
        return PlaceholderAPIUtils.parse((org.bukkit.entity.Player) player.platformPlayer(), text);
    }

    @Override
    public BukkitNetworkManager networkManager() {
        return (BukkitNetworkManager) networkManager;
    }

    @Override
    public BukkitPackManager packManager() {
        return (BukkitPackManager) packManager;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public void saveResource(String resourcePath) {
        if (resourcePath.isEmpty()) {
            throw new IllegalArgumentException("ResourcePath cannot be null or empty");
        }

        File outFile = new File(dataFolderFile(), resourcePath);
        if (outFile.exists())
            return;

        resourcePath = resourcePath.replace('\\', '/');
        InputStream in = resourceStream(resourcePath);
        if (in == null)
            return;

        int lastIndex = resourcePath.lastIndexOf('/');
        File outDir = new File(dataFolderFile(), resourcePath.substring(0, Math.max(lastIndex, 0)));

        if (!outDir.exists()) {
            outDir.mkdirs();
        }

        try {
            OutputStream out = new FileOutputStream(outFile);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.close();
            in.close();

        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public BukkitServerPlayer adapt(org.bukkit.entity.Player player) {
        if (player == null) return null;
        return Optional.ofNullable((BukkitServerPlayer) networkManager().getOnlineUser(player)).orElseGet(
                () -> (BukkitServerPlayer) networkManager().getUser(player)
        );
    }

    public boolean hasMod() {
        return hasMod;
    }

    public boolean requiresRestart() {
        return requiresRestart;
    }

    public AntiGriefLib antiGrief() {
        if (this.antiGrief == null) {
            this.antiGrief = AntiGriefLib.builder(this.bootstrap)
                    .ignoreOP(true)
                    .silentLogs(true)
                    .build();
        }
        return this.antiGrief;
    }
}
