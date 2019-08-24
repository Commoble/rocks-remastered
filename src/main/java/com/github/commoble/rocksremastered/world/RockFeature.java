package com.github.commoble.rocksremastered.world;

import java.util.Random;
import java.util.function.Function;
import java.util.stream.IntStream;

import com.github.commoble.rocksremastered.rocks.EnumRockClassification;
import com.github.commoble.rocksremastered.rocks.RockLayer;
import com.github.commoble.rocksremastered.rocks.RockTables;
import com.github.commoble.rocksremastered.rocks.RockType;
import com.mojang.datafixers.Dynamic;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.MutableBlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

public class RockFeature extends Feature<NoFeatureConfig>
{
	private static final double ONE_64TH = 0.015625;
	private static final double ONE_32TH = 0.03125D;
	private static final double ONE_16TH = 0.0625D;
	private static final double ONE_8TH = 0.125D;

	public static BlockState NEW_STATE = Blocks.STONE_BRICKS.getDefaultState();
	public static Block OLD_BLOCK = Blocks.STONE;
	public static final MutableBlockPos mutapos = new MutableBlockPos(0,0,0);

	public RockFeature(Function<Dynamic<?>, ? extends NoFeatureConfig> configFactoryIn)
	{
		super(configFactoryIn);
	}

	@Override
	public boolean place(IWorld worldIn, ChunkGenerator<? extends GenerationSettings> generator, Random rand,
			BlockPos pos, NoFeatureConfig config)
	{
		// pos is chunkX*16, 0, chunkZ*16
		//IChunk chunk = worldIn.getChunk(pos.getX() >> 4, pos.getZ() >> 4);
		IChunk chunk = worldIn.getChunk(pos);
		
		if (chunk != null)
		{
			NoiseGenerators.getInstance(worldIn).ifPresent(noise -> this.generateChunk(noise, chunk, pos, worldIn));
		}
		else
		{
			System.out.println("chunk is null at " + pos.toString());
		}
		
		return true;
	}
	
	public void generateChunk(NoiseGenerators noise, IChunk chunk, BlockPos pos, IWorld world)
	{
		//int size=16;
		
//		double[] geoTableA;
//		double[] geoTableB;
//		double[] geoTableC;
//		double[] geoTableBroad;
		// we use chunk.setBlockState here instead of world.getBlockState because the chunk method is faster
		// blockpos passed into the chunk method is local intrachunk coordinates, i.e. x and z in range [0,15]
		IntStream.range(0,16).forEach(x ->
			IntStream.range(0, 16).forEach(z ->
				this.setBlockColumn(noise, chunk, x, z, world)
			)
		);
	}
	
	protected void setBlockColumn(NoiseGenerators noise, IChunk chunk, int localX, int localZ, IWorld world)
	{
		RockLayer[] layers = RockTables.getLayerTable(world);
		int chunkX = chunk.getPos().x;
		int chunkZ = chunk.getPos().z;
		int globalX = chunkX*16 + localX;
		int globalZ = chunkZ*16 + localZ;
		int maxHeight = world.getHeight(Heightmap.Type.WORLD_SURFACE, globalX, globalZ);
		
		// get some noise for this position
		//double noiseA = noise.getNonZeroNoiseValue(noise.geoNoiseA, globalX, 109, globalZ, ONE_32TH, 1.5D);
		//double noiseB = noise.getNonZeroNoiseValue(noise.geoNoiseB, globalX, 109, globalZ, ONE_32TH, 1.5D);
		//double noiseC = noise.getNonZeroNoiseValue(noise.geoNoiseC, globalX, 109, globalZ, ONE_32TH, 1.5D);
		double volcanism = noise.getNonZeroNoiseValue(noise.volcanism, globalX, 109, globalZ, ONE_64TH, 1D, 4D);
		double upheaval = noise.getNonZeroNoiseValue(noise.upheaval, globalX, 109, globalZ, ONE_64TH, 1D, 4D);
		double erosion = noise.getNonZeroNoiseValue(noise.erosion, globalX, 109, globalZ, ONE_64TH, 1D, 4D);
		double deposition = noise.getNonZeroNoiseValue(noise.deposition, globalX, 109, globalZ, ONE_64TH, 1D, 4D);
		double broadNoise = (noise.geoNoiseBroad.func_205563_a(globalX * 0.001D, 109D, globalZ * 0.002D))*10;

		//double baseLayerID = noiseA + noiseB + noiseC + broadNoise - upheaval; // we'll mod this later
		double baseLayerID = broadNoise - upheaval;
		double upperLayerValue = baseLayerID + (deposition) - erosion;
		double lowerLayerValue = baseLayerID - (volcanism);
		if (upperLayerValue < lowerLayerValue) upperLayerValue = lowerLayerValue;
		// fractional size multiplier of the top and bottom layers
		double topLayerMultiplier = upperLayerValue - Math.floor(upperLayerValue);
		double bottomLayerMultiplier = Math.ceil(lowerLayerValue) - lowerLayerValue;
		int topID = (int)(upperLayerValue)+1;
		int bottomID = (int)(lowerLayerValue);
		int layercount = topID - bottomID + 1; // the number of rock layers in the column at this x,z coordinate

		RockType[] localLayers = new RockType[layercount];	// layers of rock in this column
		double[] weights = new double[layercount];	// relative sizes of the layers in this column (seperate from their default weights)
		int[] layerHeights = new int[layercount];	// y-levels to generate each layer up to
		
		double totalweight = 0;
		
		// calculate layerweights
		for (int i=0; i < layercount; i++)
		{
			int id = (i+bottomID) % 100;
			if (id < 0)
				id += 100;	// -1 % 100 = -1 in java
			RockLayer layer = layers[id];
			EnumRockClassification rockClass = layer.rockType.rockClassification;
			weights[i] = layer.layerWeight;
			
			// handle volcanism
			if (rockClass == EnumRockClassification.PLUTONIC || rockClass == EnumRockClassification.VOLCANIC)
			{
				weights[i] *= (1D + volcanism);
			}
			
			// handle upheaval
			if (rockClass == EnumRockClassification.PLUTONIC || rockClass == EnumRockClassification.METAMORPHIC)
			{
				weights[i] *= (1D + upheaval);
			}
			
			// handle erosion -- less of rocks near the surface, especially sedimentary
			double erosionFactor = ((double)i / (double)layercount);
			if (rockClass == EnumRockClassification.SEDIMENTARY)
			{
				erosionFactor += 1D/(double)layercount;
				weights[i] *= (1D + deposition);	// deposition = more sedimentary rocks
			}
			weights[i] /= (1D + erosionFactor * erosion);
			weights[i] *= noise.getNonZeroNoiseValue(noise.geoNoiseA, globalX, layer.localVariationCode, globalZ, ONE_32TH, 1D, 4D);
			
			localLayers[i] = layer.rockType;
			if (i==0) weights[i] *= bottomLayerMultiplier;
			if (i==layercount-1) weights[i] *= topLayerMultiplier;
			totalweight += weights[i];
		}
		
		double totalWeightMultiplier = 1/totalweight; // faster to multiply by this than to divide every time
		
		// do the first layer first
		layerHeights[0] = (int)(weights[0] * totalWeightMultiplier * maxHeight); // height of this layer in this column
		for (int layer=1; layer<layercount; layer++)
		{
			layerHeights[layer] = (int)(weights[layer] * totalWeightMultiplier * maxHeight) + layerHeights[layer-1];
		}
		
		int currentLayer = 0;
		int topLayer = layercount-1;
		for (int y=0; y<maxHeight; y++)
		{
			mutapos.setPos(localX, y, localZ); // position is local to chunk
			if (y > layerHeights[currentLayer] && currentLayer != topLayer)
			{
				currentLayer++;	// go to the next layer when we are above the previous layer
			}
			this.setBlockState(chunk, mutapos, localLayers[currentLayer]);
		}
	}
	
	// pos is local to chunk, e.g. x and z in range [0, 15]
	protected void setBlockState(IChunk chunk, BlockPos pos, RockType type)
	{
		if (chunk.getPos().x == 0)
		{
			chunk.setBlockState(pos, Blocks.AIR.getDefaultState(), false);
		}
		else
		{
			BlockState oldState = chunk.getBlockState(mutapos);
			if (type.substitutions.containsKey(oldState))
			{
					chunk.setBlockState(mutapos, type.substitutions.get(oldState), false);
			}
		}
	}

}
