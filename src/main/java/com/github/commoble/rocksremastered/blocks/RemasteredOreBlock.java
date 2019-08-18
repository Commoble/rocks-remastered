package com.github.commoble.rocksremastered.blocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.OreBlock;
import net.minecraft.util.math.MathHelper;

public class RemasteredOreBlock extends OreBlock
{
	private int minXP;
	private int maxXP;
	public RemasteredOreBlock(int minXP, int maxXP, Block.Properties properties)
	{
		super(properties);
		this.minXP = minXP;
		this.maxXP = maxXP;
	}

	protected int func_220281_a(Random rand)
	{
		return MathHelper.nextInt(rand, this.minXP, this.maxXP);
	}
}
