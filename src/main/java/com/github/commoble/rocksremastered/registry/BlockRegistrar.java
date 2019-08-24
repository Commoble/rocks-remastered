package com.github.commoble.rocksremastered.registry;

import java.util.ArrayList;
import java.util.List;

import com.github.commoble.rocksremastered.DataTables;
import com.github.commoble.rocksremastered.RocksRemastered;
import com.github.commoble.rocksremastered.blocks.RemasteredOreBlock;
import com.github.commoble.rocksremastered.blocks.RemasteredStairsBlock;
import com.github.commoble.rocksremastered.blocks.RemasteredStoneButtonBlock;
import com.github.commoble.rocksremastered.blocks.RemasteredStonePressurePlateBlock;
import com.github.commoble.rocksremastered.rocks.RockTables;

import net.minecraft.block.Block;
import net.minecraft.block.RedstoneOreBlock;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.WallBlock;
import net.minecraft.block.material.Material;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.registries.IForgeRegistry;

public class BlockRegistrar
{
	// remember registry keys used to make it simpler to register blockitems
	protected static List<String> registry_keys = new ArrayList<String>(800);
	
	
	
	public static void registerBlocks(IForgeRegistry<Block> registry)
	{
		RegistryHelper<Block> reg = new RegistryHelper<Block>(registry)
				{
			@Override
			public Block register(String registryKey, Block entry)
			{
				entry.setRegistryName(new ResourceLocation(RocksRemastered.MODID, registryKey));
				registry.register(entry);
				BlockRegistrar.registry_keys.add(registryKey);
				return entry;
			}
				};
				
		RockTables.beforeBlockRegistryInit(); // registry events happen before FML setup lifecycle events, this is where we need to do this
		
		RockTables.ROCK_TYPES.stream().map(rock -> rock.name).forEach(rockname ->
		{
			for (String blockname : DataTables.IMPROVABLE_BLOCK_TYPES)
			{
				String rockblock = rockname+"_"+blockname;
				Block.Properties props = DataTables.getBlockProps(blockname); 
				Block block = reg.register(rockblock, new Block(props)); // need blockstate for stairs
				reg.register(rockblock+"_slab", new SlabBlock(props));
				reg.register(rockblock+"_stairs", new RemasteredStairsBlock(block.getDefaultState(), props));
				reg.register(rockblock+"_wall", new WallBlock(props));
				reg.register(rockblock+"_button", new RemasteredStoneButtonBlock(DataTables.BUTTON_PROPERTIES));
				reg.register(rockblock+"_pressure_plate", new RemasteredStonePressurePlateBlock(DataTables.PRESSURE_PLATE_PROPERTIES));
			}
			reg.register(rockname+"_"+DataTables.CHISELED_STONE_BRICK, new Block(DataTables.getBlockProps("stone_bricks")));
			reg.register(rockname+"_coal_ore", new RemasteredOreBlock(0,2, getOreProps().harvestLevel(0)));
			reg.register(rockname+"_iron_ore", new RemasteredOreBlock(0,0, getOreProps().harvestLevel(1)));
			reg.register(rockname+"_lapis_ore", new RemasteredOreBlock(2,5, getOreProps().harvestLevel(1)));
			reg.register(rockname+"_gold_ore", new RemasteredOreBlock(0,0, getOreProps().harvestLevel(2)));
			reg.register(rockname+"_diamond_ore", new RemasteredOreBlock(3,7, getOreProps().harvestLevel(2)));
			reg.register(rockname+"_emerald_ore", new RemasteredOreBlock(3,7, getOreProps().harvestLevel(2)));
			
			reg.register(rockname+"_redstone_ore", new RedstoneOreBlock(Block.Properties.create(Material.ROCK).tickRandomly().lightValue(9).hardnessAndResistance(3.0F, 3.0F).harvestLevel(2).harvestTool(ToolType.PICKAXE)));
		});

		RockTables.afterBlockRegistryInit(); // after we have registered all of our blocks
	}
	
	private static Block.Properties getOreProps()
	{
		return Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 3.0F).harvestTool(ToolType.PICKAXE);
	}
}
