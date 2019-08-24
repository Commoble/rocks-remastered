package com.github.commoble.rocksremastered.registry;

import com.github.commoble.rocksremastered.world.RockFeature;
import com.github.commoble.rocksremastered.world.SingleChunkPlacement;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.placement.IPlacementConfig;
import net.minecraft.world.gen.placement.NoPlacementConfig;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

public class FeatureRegistrar
{
	public static void registerFeatures(IForgeRegistry<Feature<?>> registry)
	{	// running the feature too early seems to cause lighting crashes, so we'll just use the last one
		// use JUST ONE feature, it's important that this is a singleton because it generates perlin noise which is expensive
		ConfiguredFeature<?> feature = Biome.createDecoratedFeature(new RockFeature(NoFeatureConfig::deserialize), IFeatureConfig.NO_FEATURE_CONFIG, new SingleChunkPlacement(NoPlacementConfig::deserialize), IPlacementConfig.NO_PLACEMENT_CONFIG);
		ForgeRegistries.BIOMES.forEach(biome ->biome.addFeature(GenerationStage.Decoration.UNDERGROUND_DECORATION, feature));
	}
}
