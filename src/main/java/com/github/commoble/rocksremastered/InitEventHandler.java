package com.github.commoble.rocksremastered;

import com.github.commoble.rocksremastered.rocks.RockTables;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@EventBusSubscriber(modid=RocksRemastered.MODID, bus=Bus.MOD)
public class InitEventHandler
{
	@SubscribeEvent
	// fired after registry events
	public static void onCommonSetupEvent(FMLCommonSetupEvent event)
	{
		RockTables.postRegistryInit();
	}
}
