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

    @Override
    public int clientboundSetTitleTextPacket() {
        return 95;
    }

    @Override
    public int clientboundSetSubtitleTextPacket() {
        return 93;
    }

    @Override
    public int clientboundSetActionBarTextPacket() {
        return 70;
    }

    @Override
    public int clientboundBossEventPacket() {
        return 11;
    }

    @Override
    public int clientboundSystemChatPacket() {
        return 100;
    }

    @Override
    public int clientboundTabListPacket() {
        return 101;
    }

    @Override
    public int clientboundSetPlayerTeamPacket() {
        return 90;
    }

    @Override
    public int clientboundSetObjectivePacket() {
        return 88;
    }
}
