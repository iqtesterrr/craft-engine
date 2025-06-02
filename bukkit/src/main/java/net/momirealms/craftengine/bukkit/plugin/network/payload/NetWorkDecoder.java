package net.momirealms.craftengine.bukkit.plugin.network.payload;

import io.netty.buffer.ByteBuf;

public interface NetWorkDecoder<T> {
    T decode(ByteBuf in);
}
