package cat.red.gangs.events;


import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.InteractEntityEvent;
import org.spongepowered.api.event.filter.cause.Root;

import cat.red.gangs.types.Entity;
import cat.red.gangs.types.Gang;
import cat.red.gangs.types.Territory;

public class PlayerInteractEntityListener
{

	@Listener
	public void onPlayerLeftClick(InteractEntityEvent.Primary event, @Root Player player)
	{
		try {
			Territory territory = new Territory(event.getTargetEntity().getLocation().getChunkPosition());
	
			if (territory.isClaimed())
			{
				Entity entity = new Entity(player.getUniqueId());
				Gang entityGang = entity.getGang();
				
				boolean isTargetAPlayer = event.getTargetEntity() instanceof Player;
						
				if (!isTargetAPlayer && territory.gangCanInterract(entityGang))
				{
					event.setCancelled(true);
					return;
				}
			}
		} catch (Exception e){
			e.printStackTrace();
			event.setCancelled(true);
		}
	}	

	@Listener
	public void onPlayerRightClick(InteractEntityEvent.Secondary event, @Root Player player)
	{
		try {
			Territory territory = new Territory(event.getTargetEntity().getLocation().getChunkPosition());
	
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
		} catch (Exception e){
			e.printStackTrace();
			event.setCancelled(true);
		}
	}
}
