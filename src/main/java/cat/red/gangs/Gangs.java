package cat.red.gangs;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GamePostInitializationEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.text.Text;

import com.google.inject.Inject;

import cat.red.gangs.cmd.HelpCmd;
import cat.red.gangs.cmd.RegistrarHelp;
import cat.red.gangs.cmd.RegistrarRemove;
import cat.red.gangs.cmd.RegistrarSpawn;
import cat.red.gangs.cmd.VersionCmd;
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
	
	@Inject
	@ConfigDir(sharedRoot = false)
	private Path configDir;
	
	@Inject
	private Logger logger;
	private static Config config;
	private HashMap<List<String>, CommandSpec> commands = new HashMap<List<String>, CommandSpec>();

	public Logger getLogger()
	{
		return this.logger;
	}
	
	public static Config getConfig()
	{
		return config;
	}
	
	private void registerCommand(String command, String description, String permissionNode, CommandExecutor executor){
		this.commands.put(Arrays.asList(command), CommandSpec.builder()
			.description(Text.of(description))
			.permission(permissionNode)
			.executor(executor)
			.build());
	}

	@Listener
	public void onGamePostInit(GamePostInitializationEvent event)
	{
		getLogger().info("Loading Gangs...");
		try {
			getLogger().info("Searching for config in "+configDir);
			config = new Config(configDir, PluginInfo.ID);
			
			getLogger().info("Generating default options list");
			config.createProperty("database.host", "localhost");
			config.createProperty("database.port", 9200);
			config.createProperty("database.name", "gangs");
			config.createProperty("territory.explosion.fire", false);
			config.createProperty("territory.explosion.explode", false);
			config.createProperty("gang.friendlyFire", false);
			config.createProperty("registrar.book.name", Text.of("Gang book"));
			config.createProperty("registrar.book.lore", Text.of("Your gang management registry"));
			config.createProperty("registrar.name", Text.of("Gang registrar"));
			config.createProperty("features.territory", true);
			config.createProperty("features.gang", true);
			config.createProperty("features.registrar", true);
			
			getLogger().info("Loading configuration");
			config.load();
			
			getLogger().info("Loading data storage service");
			new Database(config.getString("database.host"), config.getInt("database.port"), config.getString("database.name"));
			
			getLogger().info("Registering events");
			Sponge.getEventManager().registerListeners(this, new PlayerInteractBlockListener());
			Sponge.getEventManager().registerListeners(this, new PlayerBreakBlockListener());
			Sponge.getEventManager().registerListeners(this, new PlayerPlaceBlockListener());
			Sponge.getEventManager().registerListeners(this, new PlayerInteractEntityListener());
			Sponge.getEventManager().registerListeners(this, new ExplosionEventListener());
			Sponge.getEventManager().registerListeners(this, new EntityDamageListener());
			
			getLogger().info("Building sub commands");

			// Registrar
			this.registerCommand("registrar", "Add/remove registrar", "gangs.registrar.help", new RegistrarHelp());
			this.registerCommand("registrar_spawn", "Add registrar", "gangs.registrar.spawn", new RegistrarSpawn());
			this.registerCommand("registrar_remove", "Remove registrar", "gangs.registrar.remove", new RegistrarRemove());
			
			// Version
			this.registerCommand("version", "Show version information", "gangs.version", new VersionCmd());

			
			getLogger().info("Registering main command");
			
			CommandSpec gangCommands = CommandSpec.builder()
				.description(Text.of("Help Command"))
				.permission("gangs.help")
				.executor(new HelpCmd(this.commands))
				.children(this.commands)
				.build();

			
			Sponge.getCommandManager().register   (this, gangCommands, PluginInfo.ID, "p");

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
