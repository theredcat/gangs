package cat.red.gangs;

import java.util.Optional;

import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GamePostInitializationEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.service.economy.EconomyService;

import com.google.inject.Inject;

import cat.red.gangs.utils.Config;
import cat.red.gangs.utils.Database;

import cat.red.gangs.events.EntityDamageListener;
import cat.red.gangs.events.ExplosionEventListener;
import cat.red.gangs.events.PlayerBreakBlockListener;
import cat.red.gangs.events.PlayerInteractEntityListener;
import cat.red.gangs.events.PlayerInteractBlockListener;
import cat.red.gangs.events.PlayerPlaceBlockListener;


@Plugin(id = PluginInfo.ID, name = PluginInfo.NAME, version = PluginInfo.VERSION, description = PluginInfo.DESCRIPTION )
public class Gangs
{
	protected Gangs()
	{
		;
	}
	
	@Inject
	private Logger logger;
	private static Config config;
	private static Database data;
	private static EconomyService econ;

	public Logger getLogger()
	{
		return this.logger;
	}
	
	public static Config getConfig()
	{
		return config;
	}
	
	public static EconomyService getEconomy()
	{
		return econ;
	}

	public static Database getDatabase()
	{
		return data;
	}
	
	@Listener
	public void onGamePostInit(GamePostInitializationEvent event)
	{
		getLogger().info("Loading Gangs...");
		getLogger().debug("Searching for economy service...");
		Optional<EconomyService> econService = Sponge.getServiceManager().provide(EconomyService.class);

		if (econService.isPresent())
		{
			getLogger().debug("Economy service found, loading it");
			econ = econService.get();

			getLogger().debug("Loading config");
			config = new Config();
			
			getLogger().debug("Loading data storage service");
			data = new Database();
			
			getLogger().debug("Registering events");
			Sponge.getEventManager().registerListeners(this, new PlayerInteractBlockListener());
			Sponge.getEventManager().registerListeners(this, new PlayerBreakBlockListener());
			Sponge.getEventManager().registerListeners(this, new PlayerPlaceBlockListener());
			Sponge.getEventManager().registerListeners(this, new PlayerInteractEntityListener());
			Sponge.getEventManager().registerListeners(this, new ExplosionEventListener());
			Sponge.getEventManager().registerListeners(this, new EntityDamageListener());

			getLogger().info("Gangs has been loaded");
		}
		else
		{
			getLogger().error("No economy plugin was found! Gangs won't be loaded");
		}
	}
}
