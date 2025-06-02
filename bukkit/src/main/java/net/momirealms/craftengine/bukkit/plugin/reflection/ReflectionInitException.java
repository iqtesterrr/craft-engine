package net.momirealms.craftengine.bukkit.plugin.reflection;

public class ReflectionInitException extends RuntimeException {

    public ReflectionInitException(String message) {
        super(message);
    }

    public ReflectionInitException(String message, Throwable cause) {
        super(message, cause);
    }
}
