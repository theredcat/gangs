package cat.red.gangs.events;

import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.data.Transaction;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.event.filter.cause.Root;

import cat.red.gangs.Gangs;
import cat.red.gangs.types.Gang;
import cat.red.gangs.types.Territory;
import cat.red.gangs.utils.Database;

public class PlayerPlaceBlockListener
{
	@Listener
	public void onPlayerPlaceBlock(ChangeBlockEvent.Place event, @Root Player player)
	{
		for (Transaction<BlockSnapshot> transaction : event.getTransactions())
		{
			Database data = Gangs.getDatabase(); 
			Territory territory = data.getTerritory(transaction.getFinal().getLocation().get());


			if (territory.isClaimed())
			{
				Gang playerGang = data.getGang(player.getUniqueId());

				if (territory.gangCanBuild(playerGang))
				{
					event.setCancelled(true);
					return;
				}
			}
		}
	}
}
