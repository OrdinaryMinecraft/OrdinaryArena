package ru.flametaichou.ordinaryarena;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import ru.flametaichou.ordinaryarena.model.Arena;
import ru.flametaichou.ordinaryarena.model.ArenasXml;
import ru.flametaichou.ordinaryarena.service.ArenaService;
import ru.flametaichou.ordinaryarena.utils.NetworkHandler;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.*;

@Mod (modid = "ordinaryarena", name = "Ordinary Arena", version = "0.1", acceptableRemoteVersions = "*")

public class OrdinaryArenaBase {

	@SidedProxy(serverSide = "ru.flametaichou.ordinaryarena.CommonProxy", clientSide = "ru.flametaichou.ordinaryarena.ClientProxy")
	public static CommonProxy proxy;

	public static ArenaService arenaService;
	public static CommandsQueueHandler commandsQueue = new CommandsQueueHandler();
	private static long lastSaveTime;

	@EventHandler
	public void initialize(FMLInitializationEvent event)
	{
		MinecraftForge.EVENT_BUS.register(new PlayerInteractHandler());
		FMLCommonHandler.instance().bus().register(commandsQueue);
		arenaService = new ArenaService();
		lastSaveTime = 0;
		readArenas();
	}

	@EventHandler
	public void serverLoad(FMLServerStartingEvent event)
	{
		event.registerServerCommand(new OrdinaryArenaCommands());
		event.registerServerCommand(new OrdinaryArenaAdminCommands());
	}

	@EventHandler
	public void load(FMLPreInitializationEvent event) {
		NetworkHandler.init();
		proxy.preInit(event);
	}

	public static WorldServer getWorld(int dimId) {
		return DimensionManager.getWorld(dimId);
	}

	public static void readArenas() {
		try {
			JAXBContext jc = JAXBContext.newInstance(ArenasXml.class);
			Unmarshaller unmarshaller = jc.createUnmarshaller();
			File file = new File("config/arenas.xml");
			if (file.exists()) {
				arenaService.setLootListsXML((ArenasXml) unmarshaller.unmarshal(new FileReader("config/arenas.xml")));
				for (Arena arena : arenaService.getLootListsXML().getArenas()) {
					Logger.log("Arena loaded: " + arena.getName());
				}
			} else {
				Logger.error("lists.xml file not found!");
			}
		} catch (Exception e) {
			Logger.error("can't load arenas.xml! Reason: " + e.getCause());
			e.printStackTrace();
		}
	}

	// TODO: сохранение списков на создании, а не по таймеру!
	public static void writeArenas(long time) {
		if (lastSaveTime != time) {
			try {
				JAXBContext jc = JAXBContext.newInstance(ArenasXml.class);
				Marshaller marshaller = jc.createMarshaller();
				marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
				marshaller.marshal(arenaService.getLootListsXML(), new FileWriter("config/arenas.xml"));
			} catch (Exception e) {
				Logger.error("can't write to arenas.xml! Reason: " + e.getCause());
				e.printStackTrace();
			}
			lastSaveTime = time;
		}
	}
}
