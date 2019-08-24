package com.github.commoble.rocksremastered.rocks;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.BlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

public class RockType
{
	public String name;
	public EnumRockClassification rockClassification;
	public HashMap<BlockState, BlockState> substitutions = new HashMap<>();
	
	public RockType(String name, EnumRockClassification rockClassification)
	{
		this.name = name;
		this.rockClassification = rockClassification;
	}
	
	public void initSubstitutions(Map<BlockState, String> substitutionStrings)
	{
		for (BlockState state : substitutionStrings.keySet())
		{
			ResourceLocation loc = new ResourceLocation("rocksremastered", this.name + "_" + substitutionStrings.get(state));
			this.substitutions.put(state, ForgeRegistries.BLOCKS.getValue(loc).getDefaultState());
		}
	}
	
	@Override
	public String toString()
	{
		return this.name;
	}
}
