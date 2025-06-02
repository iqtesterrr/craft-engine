package net.momirealms.craftengine.core.plugin.compatibility;

import net.momirealms.craftengine.core.entity.furniture.ExternalModel;

public interface ModelProvider {

    ExternalModel createModel(String id);
}
