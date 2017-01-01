package cat.red.gangs.events;

import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.data.Transaction;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.event.filter.cause.Root;
import org.spongepowered.api.world.explosion.Explosion;

import cat.red.gangs.Gangs;
import cat.red.gangs.types.Gang;
import cat.red.gangs.types.Territory;
import cat.red.gangs.utils.database.Database;

public class PlayerBreakBlockListener
{
	@Listener
	public void onPlayerBreakBlock(ChangeBlockEvent.Break event, @Root Player player)
	{
		Database data = Gangs.getDatabase();
		
		for (Transaction<BlockSnapshot> transaction : event.getTransactions())
		{
			Territory territory = data.getTerritory(transaction.getFinal().getLocation().get());

			if (territory.isClaimed())
			{
				Gang playerGang = data.getGang(player.getUniqueId());

				if (!event.getCause().first(Explosion.class).isPresent())
				{
					if (territory.gangCanBuild(playerGang))
					{
						event.setCancelled(true);
						return;
					}
				}
			}
		}
	}
}
