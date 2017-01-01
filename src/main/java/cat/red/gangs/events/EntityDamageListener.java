package cat.red.gangs.events;

import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.DamageEntityEvent;

import cat.red.gangs.Gangs;
import cat.red.gangs.utils.Config;
import cat.red.gangs.utils.database.Database;
import cat.red.gangs.types.Gang;

public class EntityDamageListener
{
	@Listener
	public void onEntityDamaged(DamageEntityEvent event)
	{
		Database data = Gangs.getDatabase();
		Config config = Gangs.getConfig();
		
		Entity source = (Player) event.getCause().root();
		Gang sourceGang = data.getGang(source.getUniqueId());
		
		Entity target = event.getTargetEntity();
		Gang targetGang = data.getGang(target.getUniqueId());

		try {
			if(config.getBool("gang.friendlyFire") == true && sourceGang == targetGang) {
				event.setBaseDamage(0);
				event.setCancelled(true);
			}
		} catch (Exception e) {
			event.setBaseDamage(0);
			event.setCancelled(true);
			e.printStackTrace();
		}
	}
}