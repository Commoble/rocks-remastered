package com.github.commoble.rocksremastered.registry;

import com.github.commoble.rocksremastered.RocksRemastered;

import net.minecraft.block.Blocks;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class CreativeTabs
{
	public static final ItemGroup tab = new ItemGroup(RocksRemastered.MODID) {
		@Override
		public ItemStack createIcon()
		{
			return new ItemStack(Blocks.STONE);
		}
	};
}
