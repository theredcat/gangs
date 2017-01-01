package cat.red.gangs;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.Properties;

import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GamePostInitializationEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.service.economy.EconomyService;

import com.google.inject.Inject;

import cat.red.gangs.utils.Config;
import cat.red.gangs.utils.database.Database;
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
	
	@ConfigDir(sharedRoot = false)
	private Path configDir;
	
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

		getLogger().debug("Loading config");
		try {
			config = new Config(configDir, PluginInfo.ID);
			config.createProperty("database.host", "localhost");
			config.createProperty("database.port", 9200);
			config.createProperty("database.name", "gangs");
			
			getLogger().debug("Loading data storage service");
			data = new Database(config.getString("database.host"), config.getInt("database.port"), config.getString("database.name"));
			
			getLogger().debug("Registering events");
			Sponge.getEventManager().registerListeners(this, new PlayerInteractBlockListener());
			Sponge.getEventManager().registerListeners(this, new PlayerBreakBlockListener());
			Sponge.getEventManager().registerListeners(this, new PlayerPlaceBlockListener());
			Sponge.getEventManager().registerListeners(this, new PlayerInteractEntityListener());
			Sponge.getEventManager().registerListeners(this, new ExplosionEventListener());
			Sponge.getEventManager().registerListeners(this, new EntityDamageListener());

			getLogger().info("Gangs has been loaded");
		} catch (IOException e) {
			getLogger().error("Failed to load config");
			e.printStackTrace();
		} catch (Exception e) {
			getLogger().error("Configuration error");
			e.printStackTrace();
		}
	}
}
