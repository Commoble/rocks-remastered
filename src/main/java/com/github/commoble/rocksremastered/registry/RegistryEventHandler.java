package com.github.commoble.rocksremastered.registry;

import com.github.commoble.rocksremastered.RocksRemastered;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid=RocksRemastered.MODID, bus=Bus.MOD)
public class RegistryEventHandler
{
	@SubscribeEvent
	public static void onBlockRegistryEvent(RegistryEvent.Register<Block> event)
	{
		BlockRegistrar.registerBlocks(event.getRegistry());
	}
	
	@SubscribeEvent
	public static void onItemRegistryEvent(RegistryEvent.Register<Item> event)
	{
		ItemRegistrar.registerItems(event.getRegistry());
	}
}
