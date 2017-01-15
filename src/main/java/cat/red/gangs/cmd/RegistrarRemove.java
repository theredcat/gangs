package cat.red.gangs.cmd;

import java.util.Collection;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.Villager;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import cat.red.gangs.Gangs;
import cat.red.gangs.utils.Config;


public class RegistrarRemove implements CommandExecutor {
	public CommandResult execute(CommandSource src, CommandContext ctx) throws CommandException
	{
		if(src instanceof Player){
			try {
				boolean matched = false;
				
				Config config = Gangs.getConfig();
				
				Player srcPlayer = (Player) src;
				Collection<Entity> entities = srcPlayer.getNearbyEntities(20.0f);
				 
				for(Entity entity : entities){
					if (entity instanceof Villager) {
						Villager villager = (Villager) entity;
						if(villager.get(Keys.DISPLAY_NAME).isPresent()){
							if(villager.get(Keys.DISPLAY_NAME).get().equals(config.getText("registrar.name"))){
								villager.remove();
								matched = true;
							}
						}
					}
				}
				if(matched)
					src.sendMessage(Text.of(TextColors.GREEN, "Registrar villager has been removed"));
				else
					src.sendMessage(Text.of(TextColors.RED, "No registrar can be found nearby"));
				
				return CommandResult.success();			
			} catch (Exception e){
				src.sendMessage(Text.of(TextColors.RED, "Something went wrong executing the command"));
				e.printStackTrace();
				return CommandResult.success();	
			}
		}else{
			src.sendMessage(Text.of(TextColors.RED, "Only logged-in players can remove registrars"));
			return CommandResult.success();					
		}

	}
}
