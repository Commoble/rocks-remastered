package com.github.commoble.rocksremastered.registry;

import com.github.commoble.rocksremastered.RocksRemastered;

import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

public class ItemRegistrar
{
	public static void registerItems(IForgeRegistry<Item> registry)
	{
		RegistryHelper<Item> reg = new RegistryHelper<Item>(registry);
		
		BlockRegistrar.registry_keys.stream().forEach(key -> reg.register(
				key, 
				new BlockItem(
						ForgeRegistries.BLOCKS.getValue(new ResourceLocation(RocksRemastered.MODID, key)),
						new Item.Properties().group(CreativeTabs.tab)
					)
				)
			);
	}
}
