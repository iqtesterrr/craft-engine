package net.momirealms.craftengine.bukkit.sound;

import net.momirealms.craftengine.bukkit.nms.FastNMS;
import net.momirealms.craftengine.bukkit.plugin.reflection.minecraft.CoreReflections;
import net.momirealms.craftengine.bukkit.plugin.reflection.minecraft.MRegistries;
import net.momirealms.craftengine.bukkit.util.ComponentUtils;
import net.momirealms.craftengine.bukkit.util.KeyUtils;
import net.momirealms.craftengine.core.plugin.CraftEngine;
import net.momirealms.craftengine.core.sound.AbstractSoundManager;
import net.momirealms.craftengine.core.sound.JukeboxSong;
import net.momirealms.craftengine.core.util.Key;
import net.momirealms.craftengine.core.util.VersionHelper;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class BukkitSoundManager extends AbstractSoundManager {

    public BukkitSoundManager(CraftEngine plugin) {
        super(plugin);
        VANILLA_SOUND_EVENTS.addAll(FastNMS.INSTANCE.getAllVanillaSounds().stream().map(it -> Key.of(it.getNamespace(), it.getKey())).toList());
    }

    @Override
    protected void registerSongs(Map<Key, JukeboxSong> songs) {
        if (songs.isEmpty()) return;
        try {
            Object registry = CoreReflections.method$RegistryAccess$registryOrThrow.invoke(FastNMS.INSTANCE.registryAccess(), MRegistries.instance$Registries$JUKEBOX_SONG);;
            unfreezeRegistry(registry);
            for (Map.Entry<Key, JukeboxSong> entry : songs.entrySet()) {
                Key id = entry.getKey();
                JukeboxSong jukeboxSong = entry.getValue();
                Object resourceLocation = KeyUtils.toResourceLocation(id);
                Object soundId = KeyUtils.toResourceLocation(jukeboxSong.sound());
                Object song = CoreReflections.method$Registry$get.invoke(registry, resourceLocation);

                Object soundEvent = VersionHelper.isOrAbove1_21_2() ?
                        CoreReflections.constructor$SoundEvent.newInstance(soundId, Optional.of(jukeboxSong.range())) :
                        CoreReflections.constructor$SoundEvent.newInstance(soundId, jukeboxSong.range(), false);
                Object soundHolder = CoreReflections.method$Holder$direct.invoke(null, soundEvent);

                if (song == null) {
                    song = CoreReflections.constructor$JukeboxSong.newInstance(soundHolder, ComponentUtils.adventureToMinecraft(jukeboxSong.description()), jukeboxSong.lengthInSeconds(), jukeboxSong.comparatorOutput());
                    Object holder = CoreReflections.method$Registry$registerForHolder.invoke(null, registry, resourceLocation, song);
                    CoreReflections.method$Holder$Reference$bindValue.invoke(holder, song);
                    CoreReflections.field$Holder$Reference$tags.set(holder, Set.of());
                }
            }
            freezeRegistry(registry);
        } catch (Exception e) {
            plugin.logger().warn("Failed to register jukebox songs.", e);
        }
    }

    private void unfreezeRegistry(Object registry) throws IllegalAccessException {
        CoreReflections.field$MappedRegistry$frozen.set(registry, false);
    }

    private void freezeRegistry(Object registry) throws IllegalAccessException {
        CoreReflections.field$MappedRegistry$frozen.set(registry, true);
    }
}
