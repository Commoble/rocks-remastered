package com.github.commoble.rocksremastered.rocks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.world.IWorld;

public class RockTables
{
	public static Map<BlockState, String> oreStringMap = new HashMap<>();

	public static List<RockType> ROCK_TYPES;
	public static Map<EnumRockClassification, List<RockType>> rocksByCategory = new HashMap<>();
	public static Map<Integer, RockLayer[]> layersByWorld = new HashMap<>();
	
	public static String STONE = "stone";
	public static String COAL = "coal_ore";
	public static String IRON = "iron_ore";
	public static String GOLD = "gold_ore";
	public static String LAPIS = "lapis_ore";
	public static String REDSTONE = "redstone_ore";
	public static String DIAMOND = "diamond_ore";
	public static String EMERALD = "emerald_ore";
	
	// start of Rocks Remastered block registry
	public static void beforeBlockRegistryInit()
	{
		ROCK_TYPES = RockJsonReader.getRocksFromJson();
	}
	
	// end of Rocks Remastered block registry
	public static void afterBlockRegistryInit()
	{		
		registerDefaultSubstitutions(ROCK_TYPES);
	}
	
	public static RockLayer[] getLayerTable(IWorld world)
	{
		int worldID = world.getDimension().getType().getId();
		if (!layersByWorld.containsKey(worldID))
		{
			layersByWorld.put(worldID, generateRockLayers(world.getSeed()));
		}
		return layersByWorld.get(worldID);
	}
	
	// after registry from all mods is concluded
	public static void postRegistryInit()
	{
		// organize the rocks by rock type
		for(EnumRockClassification rockClass : EnumRockClassification.values())
		{
			rocksByCategory.put(rockClass, new ArrayList<RockType>());
		}
		ROCK_TYPES.stream().forEach(rock -> 
		{
			rock.initSubstitutions(oreStringMap);
			rocksByCategory.get(rock.rockClassification).add(rock);
		});
	}

	private static void registerDefaultSubstitutions(List<RockType> rocks)
	{
		addOreString(Blocks.STONE.getDefaultState(), STONE);
		addOreString(Blocks.ANDESITE.getDefaultState(), STONE);
		addOreString(Blocks.GRANITE.getDefaultState(), STONE);
		addOreString(Blocks.DIORITE.getDefaultState(), STONE);
		
		addOreString(Blocks.COAL_ORE.getDefaultState(), COAL);
		addOreString(Blocks.IRON_ORE.getDefaultState(), IRON);
		addOreString(Blocks.GOLD_ORE.getDefaultState(), GOLD);
		addOreString(Blocks.LAPIS_ORE.getDefaultState(), LAPIS);
		addOreString(Blocks.REDSTONE_ORE.getDefaultState(), REDSTONE);
		addOreString(Blocks.DIAMOND_ORE.getDefaultState(), DIAMOND);
		addOreString(Blocks.EMERALD_ORE.getDefaultState(), EMERALD);
	}
	
	public static void addOreString(BlockState state, String string)
	{
		oreStringMap.put(state, string);
	}
	
	private static RockLayer[] generateRockLayers(long seed)
	{
		Random rand = new Random(seed + 123451);
		RockLayer[] layers = new RockLayer[100];
		
		for (int i=0; i<100; i++)
		{
			// decide on a rock classification
			// for simplicity's sake, in this generation system, all of minecraft above bedrock is considered "near the surface"
			// on stupidworld, about 75% of surface rock is sedimentary rock
			EnumRockClassification cat;
			double layerweight;
			int catpicker = rand.nextInt(12);
			if (catpicker < 9) // 75%
			{
				cat = EnumRockClassification.SEDIMENTARY;
				layerweight = 5 + rand.nextDouble()*10D; // 5 to 15
			}
			else if (catpicker < 10) // 25%
			{
				cat = EnumRockClassification.METAMORPHIC;
				layerweight = 5 + rand.nextDouble()*5D; // 10 to 15
			}
			else if (catpicker < 11) // 25%
			{
				cat = EnumRockClassification.PLUTONIC;
				layerweight = 5 + rand.nextDouble()*5D;
			}
			else // 25%
			{
				cat = EnumRockClassification.VOLCANIC;
				layerweight = 5 + rand.nextDouble() * 10D;	// 5 to 15
			}
			
			List<RockType> rocks = rocksByCategory.get(cat);
			RockType rock = rocks.get(rand.nextInt(rocks.size()));
			layers[i] = new RockLayer(rock, layerweight, rand.nextDouble() * 10000D);
		}
		
		return layers;
	}
}
