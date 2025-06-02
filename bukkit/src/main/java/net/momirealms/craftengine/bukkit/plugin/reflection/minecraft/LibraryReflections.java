package net.momirealms.craftengine.bukkit.plugin.reflection.minecraft;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToByteEncoder;
import net.momirealms.craftengine.core.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.List;

import static java.util.Objects.requireNonNull;

public final class LibraryReflections {
    private LibraryReflections() {}

    public static final Method method$messageToByteEncoder$encode = requireNonNull(
            ReflectionUtils.getDeclaredMethod(
                    MessageToByteEncoder.class, void.class, ChannelHandlerContext.class, Object.class, ByteBuf.class
            )
    );

    public static final Method method$byteToMessageDecoder$decode = requireNonNull(
            ReflectionUtils.getDeclaredMethod(
                    ByteToMessageDecoder.class, void.class, ChannelHandlerContext.class, ByteBuf.class, List.class
            )
    );
}
