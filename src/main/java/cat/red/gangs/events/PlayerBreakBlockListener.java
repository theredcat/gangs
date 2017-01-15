package cat.red.gangs.events;

import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.data.Transaction;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.event.filter.cause.Root;
import org.spongepowered.api.world.explosion.Explosion;

import cat.red.gangs.types.Entity;
import cat.red.gangs.types.Gang;
import cat.red.gangs.types.Territory;

public class PlayerBreakBlockListener
{
	@Listener
	public void onPlayerBreakBlock(ChangeBlockEvent.Break event, @Root Player player)
	{
		try {
			for (Transaction<BlockSnapshot> transaction : event.getTransactions())
			{
				Territory territory = new Territory(transaction.getFinal().getLocation().get().getChunkPosition());
	
				if (territory.isClaimed())
				{
					if (!event.getCause().first(Explosion.class).isPresent())
					{
						Entity entity =  new Entity(player.getUniqueId());
						Gang playerGang = entity.getGang();
						
						if (territory.gangCanBuild(playerGang))
						{
							event.setCancelled(true);
							return;
						}
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
