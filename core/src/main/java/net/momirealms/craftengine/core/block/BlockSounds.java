package net.momirealms.craftengine.core.block;

import net.momirealms.craftengine.core.sound.SoundData;
import net.momirealms.craftengine.core.util.Key;

import java.util.Map;

public class BlockSounds {
    /*
    Fall 0.5 0.75
    Place 1, 0.8
    Step 0.15, 1
    Hit 0.5 0.5
    Break 1 0.8
     */
    public static final SoundData EMPTY_SOUND = new SoundData(Key.of("minecraft:intentionally_empty"), 1, 1);
    public static final BlockSounds EMPTY = new BlockSounds(EMPTY_SOUND, EMPTY_SOUND, EMPTY_SOUND, EMPTY_SOUND, EMPTY_SOUND, EMPTY_SOUND);

    private final SoundData breakSound;
    private final SoundData stepSound;
    private final SoundData placeSound;
    private final SoundData hitSound;
    private final SoundData fallSound;
    private final SoundData landSound;

    public BlockSounds(SoundData breakSound, SoundData stepSound, SoundData placeSound, SoundData hitSound, SoundData fallSound, SoundData landSound) {
        this.breakSound = breakSound;
        this.stepSound = stepSound;
        this.placeSound = placeSound;
        this.hitSound = hitSound;
        this.fallSound = fallSound;
        this.landSound = landSound;
    }

    public static BlockSounds fromMap(Map<String, Object> map) {
        if (map == null) return EMPTY;
        return new BlockSounds(
                SoundData.create(map.getOrDefault("break", "minecraft:intentionally_empty"), 1f, 0.8f),
                SoundData.create(map.getOrDefault("step", "minecraft:intentionally_empty"), 0.15f, 1f),
                SoundData.create(map.getOrDefault("place", "minecraft:intentionally_empty"), 0f, 0.8f), // todo 0?
                SoundData.create(map.getOrDefault("hit", "minecraft:intentionally_empty"), 0.5f, 0.5f),
                SoundData.create(map.getOrDefault("fall", "minecraft:intentionally_empty"), 0.5f, 0.75f),
                SoundData.create(map.getOrDefault("land", "minecraft:intentionally_empty"), 0.3f, 1f)
        );
    }

    public SoundData breakSound() {
        return breakSound;
    }

    public SoundData stepSound() {
        return stepSound;
    }

    public SoundData placeSound() {
        return placeSound;
    }

    public SoundData hitSound() {
        return hitSound;
    }

    public SoundData landSound() {
        return landSound;
    }

    public SoundData fallSound() {
        return fallSound;
    }
}
