package com.github.commoble.rocksremastered;

import java.util.HashMap;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;

public class DataTables
{
	// blocks that can be made into slabs, stairs, walls, buttons, or pressure plates
	public static final String[] IMPROVABLE_BLOCK_TYPES =
		{
				"stone",
				"smooth_stone",
				"cobblestone",
				"mossy_cobblestone",
				"stone_bricks",
				"mossy_stone_bricks",
				"cracked_stone_bricks"
		};
	
	// can't be improved into the above features due to texture not coming out right
	public static final String CHISELED_STONE_BRICK = "chiseled_stone_bricks";
	
	// ore blocks
	public static final String[] ORE_TYPES =
		{
				"coal",
				"iron",
				"redstone",
				"lapis",
				"gold",
				"diamond",
				"emerald"
		};
	
	public static void preInit()
	{
	}

	
	private static final Block.Properties STONE_PROPERTIES = Block.Properties.create(Material.ROCK, MaterialColor.STONE).hardnessAndResistance(1.5F, 6.0F);
	private static final Block.Properties SMOOTH_STONE_PROPERTIES = Block.Properties.create(Material.ROCK, MaterialColor.STONE).hardnessAndResistance(2.0F, 6.0F);
	
	// used by cobblestone and mossy cobblestone
	private static final Block.Properties COBBLESTONE_PROPERTIES = Block.Properties.create(Material.ROCK).hardnessAndResistance(2.0F, 6.0F);
	
	// used by stone bricks, mossy stone bricks, cracked stone bricks, chiseled stone bricks
	private static final Block.Properties STONE_BRICK_PROPERTIES = Block.Properties.create(Material.ROCK).hardnessAndResistance(1.5F, 6.0F);
	
	public static final Block.Properties BUTTON_PROPERTIES = Block.Properties.create(Material.MISCELLANEOUS).doesNotBlockMovement().hardnessAndResistance(0.5F);
	public static final Block.Properties PRESSURE_PLATE_PROPERTIES = Block.Properties.create(Material.ROCK).doesNotBlockMovement().hardnessAndResistance(0.5F);
	
	private static HashMap<String, Block.Properties> blockprops = new HashMap<>();
	
	public static Block.Properties getBlockProps(String blocktype)
	{
		return blockprops.get(blocktype);
	}
	
	static
	{
		blockprops.put("stone", STONE_PROPERTIES);
		blockprops.put("smooth_stone", SMOOTH_STONE_PROPERTIES);
		blockprops.put("cobblestone", COBBLESTONE_PROPERTIES);
		blockprops.put("mossy_cobblestone", COBBLESTONE_PROPERTIES);
		blockprops.put("stone_bricks", STONE_BRICK_PROPERTIES);
		blockprops.put("mossy_stone_bricks", STONE_BRICK_PROPERTIES);
		blockprops.put("cracked_stone_bricks", STONE_BRICK_PROPERTIES);
		blockprops.put("chiseled_stone_bricks", STONE_BRICK_PROPERTIES);
	}
}
