package net.momirealms.craftengine.bukkit.plugin.network.payload;

import io.netty.buffer.ByteBuf;

public interface NetWorkEncoder<T> {
    void encode(ByteBuf out, T value);
}
