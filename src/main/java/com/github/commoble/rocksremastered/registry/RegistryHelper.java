package com.github.commoble.rocksremastered.registry;

import com.github.commoble.rocksremastered.RocksRemastered;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class RegistryHelper<T extends IForgeRegistryEntry<T>>
{
	public IForgeRegistry<T> registry;
	
	public RegistryHelper(IForgeRegistry<T> registry)
	{
		this.registry = registry;
	}
	
	public T register(String registryKey, T entry)
	{
		entry.setRegistryName(new ResourceLocation(RocksRemastered.MODID, registryKey));
		registry.register(entry);
		return entry;
	}
}
