package net.momirealms.craftengine.bukkit.plugin.network.impl;

import net.momirealms.craftengine.bukkit.plugin.network.PacketIds;

public class PacketIds1_20 implements PacketIds {

    @Override
    public int clientboundBlockUpdatePacket() {
        return 10;
    }

    @Override
    public int clientboundSectionBlocksUpdatePacket() {
        return 67;
    }

    @Override
    public int clientboundLevelParticlesPacket() {
        return 38;
    }

    @Override
    public int clientboundLevelEventPacket() {
        return 37;
    }

    @Override
    public int clientboundAddEntityPacket() {
        return 1;
    }

    @Override
    public int clientboundOpenScreenPacket() {
        return 48;
    }

    @Override
    public int clientboundSoundPacket() {
        return 98;
    }

    @Override
    public int clientboundRemoveEntitiesPacket() {
        return 62;
    }

    @Override
    public int clientboundSetEntityDataPacket() {
        return 82;
    }
}
