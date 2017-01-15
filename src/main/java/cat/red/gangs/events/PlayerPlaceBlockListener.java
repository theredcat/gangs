package cat.red.gangs.events;

import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.data.Transaction;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.event.filter.cause.Root;

import cat.red.gangs.types.Entity;
import cat.red.gangs.types.Gang;
import cat.red.gangs.types.Territory;

public class PlayerPlaceBlockListener
{
	@Listener
	public void onPlayerPlaceBlock(ChangeBlockEvent.Place event, @Root Player player)
	{
		try {			
			for (Transaction<BlockSnapshot> transaction : event.getTransactions())
			{
				Territory territory = new Territory(transaction.getFinal().getLocation().get().getChunkPosition());
				Entity entity = new Entity(player.getUniqueId());
				Gang playerGang = entity.getGang();
				
				if (territory.isClaimed() && !territory.gangCanBuild(playerGang))
				{
					event.setCancelled(true);
					return;
				}else if(transaction.getFinal().getState().getType() == BlockTypes.STRUCTURE_BLOCK){
					territory.claim(playerGang);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			event.setCancelled(true);
		}
	}
}
