package cat.red.gangs.events;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.InteractBlockEvent;
import org.spongepowered.api.event.filter.cause.Root;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import cat.red.gangs.Gangs;
import cat.red.gangs.types.Gang;
import cat.red.gangs.types.Territory;
import cat.red.gangs.utils.database.Database;

import java.util.Optional;

public class PlayerInteractBlockListener
{
	@Listener
	public void onPlayerInteractBlock(InteractBlockEvent event, @Root Player player)
	{
		Optional<Location<World>> possibleLocation = event.getTargetBlock().getLocation();

		if (possibleLocation.isPresent())
		{
			Database data = Gangs.getDatabase(); 
			Territory territory = data.getTerritory(possibleLocation.get());
			
			try
			{
				if (territory.isClaimed())
				{
					Gang playerGang = data.getGang(player.getUniqueId());

					if (territory.gangCanInterract(playerGang))
					{
						event.setCancelled(true);
						return;
					}
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}

	}
}
