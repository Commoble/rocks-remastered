package com.github.commoble.rocksremastered.world;

import java.util.Optional;
import java.util.Random;

import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ImprovedNoiseGenerator;
import net.minecraft.world.gen.OctavesNoiseGenerator;

public class NoiseGenerators
{
	private static Optional<NoiseGenerators> INSTANCE = Optional.empty();
	
	public static Optional<NoiseGenerators> getInstance(IWorld world)
	{
		if (world.isRemote())
		{
			return Optional.empty();
		}
		
		if (!INSTANCE.isPresent())
		{
			INSTANCE = Optional.of(new NoiseGenerators(world));
		}
		
		return INSTANCE;
	}

	public final OctavesNoiseGenerator geoNoiseA;
	public final OctavesNoiseGenerator geoNoiseB;
	public final OctavesNoiseGenerator geoNoiseC;
	public final OctavesNoiseGenerator geoNoiseBroad;
	
	public final OctavesNoiseGenerator volcanism;	// increases weighting of plutonic and volcanic rock
	public final OctavesNoiseGenerator erosion;	// reduces weighting of upper layers, especially sedimentary rock
	public final OctavesNoiseGenerator upheaval;	// shifts the layers used toward lower layers
	public final OctavesNoiseGenerator deposition;	// increases weighting of sedimentary rock
	

	private Random rand = new Random(); // use responsibly! ALWAYS reset the seed before using!

	public NoiseGenerators(IWorld world)
	{
		// this random needs to be the same whenever the world is loaded
		rand.setSeed(world.getSeed());
		geoNoiseA = new OctavesNoiseGenerator(rand, 1);
		geoNoiseB = new OctavesNoiseGenerator(rand, 1);
		geoNoiseC = new OctavesNoiseGenerator(rand, 1);
		volcanism = new OctavesNoiseGenerator(rand, 2);
		erosion = new OctavesNoiseGenerator(rand, 2);
		upheaval = new OctavesNoiseGenerator(rand, 2);
		deposition = new OctavesNoiseGenerator(rand, 2);
		geoNoiseBroad = new OctavesNoiseGenerator(rand, 4);
		// 4 octaves: +/- 15, usually not higher than 7.5
		// 2 octaves: +/- 3, usually not higher than 1.5
		// 1 octave: +/- 1, usually not higher than 0.5
	}
	
	public double getNonZeroNoiseValue(OctavesNoiseGenerator generator, double x, double y, double z, double size, double scale, double offset)
	{
		double value = (generator.func_205563_a(x * size, y*size, z * size)*scale + offset);
		if (value < 0D) value = 0D;
		return value;
	}
}
