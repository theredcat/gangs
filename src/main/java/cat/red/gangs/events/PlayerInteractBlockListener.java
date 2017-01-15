package cat.red.gangs.events;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.InteractBlockEvent;
import org.spongepowered.api.event.filter.cause.Root;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import cat.red.gangs.types.Entity;
import cat.red.gangs.types.Gang;
import cat.red.gangs.types.Territory;

import java.util.Optional;

public class PlayerInteractBlockListener
{
	@Listener
	public void onPlayerInteractBlock(InteractBlockEvent event, @Root Player player)
	{
		try
		{
			Optional<Location<World>> possibleLocation = event.getTargetBlock().getLocation();
	
			if (possibleLocation.isPresent())
			{
				Territory territory = new Territory(possibleLocation.get().getChunkPosition());
	
				if (territory.isClaimed())
				{
					Entity entity = new Entity(player.getUniqueId());
					Gang entityGang = entity.getGang();

					if (territory.gangCanInterract(entityGang))
					{
						event.setCancelled(true);
						return;
					}
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			event.setCancelled(true);
		}
	}
}
