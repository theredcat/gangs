package cat.red.gangs.events;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.InteractEntityEvent;
import org.spongepowered.api.event.filter.cause.Root;

import cat.red.gangs.Gangs;
import cat.red.gangs.types.Gang;
import cat.red.gangs.types.Territory;
import cat.red.gangs.utils.Database;

public class PlayerInteractEntityListener
{

	@Listener
	public void onPlayerLeftClick(InteractEntityEvent.Primary event, @Root Player player)
	{
		Database data = Gangs.getDatabase(); 
		Territory territory = data.getTerritory(event.getTargetEntity().getLocation());

		if (territory.isClaimed())
		{
			Gang playerGang = data.getGang(player.getUniqueId());
			boolean isTargetAPlayer = event.getTargetEntity() instanceof Player;
					
			if (!isTargetAPlayer && territory.gangCanInterract(playerGang))
			{
				event.setCancelled(true);
				return;
			}
		}
	}	

	@Listener
	public void onPlayerRightClick(InteractEntityEvent.Secondary event, @Root Player player)
	{
		Database data = Gangs.getDatabase(); 
		Territory territory = data.getTerritory(event.getTargetEntity().getLocation());

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
}
