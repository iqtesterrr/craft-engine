package net.momirealms.craftengine.core.pack.host;

import net.momirealms.craftengine.core.pack.host.impl.*;
import net.momirealms.craftengine.core.registry.BuiltInRegistries;
import net.momirealms.craftengine.core.registry.Holder;
import net.momirealms.craftengine.core.registry.Registries;
import net.momirealms.craftengine.core.registry.WritableRegistry;
import net.momirealms.craftengine.core.util.Key;
import net.momirealms.craftengine.core.util.ResourceKey;

import java.util.Map;

public class ResourcePackHosts {
    public static final Key NONE = Key.of("craftengine:none");
    public static final Key SELF = Key.of("craftengine:self");
    public static final Key EXTERNAL = Key.of("craftengine:external");
    public static final Key LOBFILE = Key.of("craftengine:lobfile");
    public static final Key S3 = Key.of("craftengine:s3");
    public static final Key ALIST = Key.of("craftengine:alist");
    public static final Key DROPBOX = Key.of("craftengine:dropbox");
    public static final Key ONEDRIVE = Key.of("craftengine:onedrive");

    static {
        register(NONE, NoneHost.FACTORY);
        register(SELF, SelfHost.FACTORY);
        register(EXTERNAL, ExternalHost.FACTORY);
        register(LOBFILE, LobFileHost.FACTORY);
        register(S3, S3Host.FACTORY);
        register(ALIST, AlistHost.FACTORY);
        register(DROPBOX, DropboxHost.FACTORY);
        register(ONEDRIVE, OneDriveHost.FACTORY);
    }

    public static void register(Key key, ResourcePackHostFactory factory) {
        Holder.Reference<ResourcePackHostFactory> holder = ((WritableRegistry<ResourcePackHostFactory>) BuiltInRegistries.RESOURCE_PACK_HOST_FACTORY)
                .registerForHolder(new ResourceKey<>(Registries.RESOURCE_PACK_HOST_FACTORY.location(), key));
        holder.bindValue(factory);
    }

    public static ResourcePackHost fromMap(Map<String, Object> map) {
        String type = (String) map.get("type");
        if (type == null) {
            throw new NullPointerException("host type cannot be null");
        }
        Key key = Key.withDefaultNamespace(type, "craftengine");
        ResourcePackHostFactory factory = BuiltInRegistries.RESOURCE_PACK_HOST_FACTORY.getValue(key);
        if (factory == null) {
            throw new IllegalArgumentException("Unknown resource pack host type: " + type);
        }
        return factory.create(map);
    }
}
