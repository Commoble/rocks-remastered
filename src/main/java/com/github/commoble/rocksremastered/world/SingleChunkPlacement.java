package com.github.commoble.rocksremastered.world;

import java.util.Random;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import com.mojang.datafixers.Dynamic;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.placement.NoPlacementConfig;
import net.minecraft.world.gen.placement.Placement;

/** This Placement just returns the chunkX*16, 0, chunkZ*16 coordinate for the given chunk **/
public class SingleChunkPlacement extends Placement<NoPlacementConfig>
{

	public SingleChunkPlacement(Function<Dynamic<?>, ? extends NoPlacementConfig> configFactoryIn)
	{
		super(configFactoryIn);
	}

	@Override
	public Stream<BlockPos> getPositions(IWorld worldIn, ChunkGenerator<? extends GenerationSettings> generatorIn,
			Random random, NoPlacementConfig configIn, BlockPos pos)
	{
		return Stream.of(pos);
	}

}
