package net.momirealms.craftengine.core.item.modifier;

import net.momirealms.craftengine.core.item.Item;
import net.momirealms.craftengine.core.item.ItemBuildContext;
import net.momirealms.craftengine.core.item.JukeboxPlayable;

public class JukeboxSongModifier<I> implements ItemDataModifier<I> {
    private final JukeboxPlayable song;

    public JukeboxSongModifier(JukeboxPlayable song) {
        this.song = song;
    }

    @Override
    public String name() {
        return "jukebox-playable";
    }

    @Override
    public void apply(Item<I> item, ItemBuildContext context) {
        item.jukeboxSong(this.song);
    }
}
