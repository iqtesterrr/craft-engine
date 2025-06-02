package net.momirealms.craftengine.core.block;

public class BlockRegistryMirror {
    private static BlockStateWrapper[] customBlockStates;
    private static BlockStateWrapper stoneState;

    public static void init(BlockStateWrapper[] states, BlockStateWrapper state) {
        customBlockStates = states;
        stoneState = state;
    }

    public static BlockStateWrapper stateByRegistryId(int vanillaId) {
        if (vanillaId < 0) return stoneState;
        return customBlockStates[vanillaId];
    }

    public static int size() {
        return customBlockStates.length;
    }
}
