package cat.red.gangs.events;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.DamageEntityEvent;

import cat.red.gangs.Gangs;
import cat.red.gangs.utils.Config;
import cat.red.gangs.types.Gang;
import cat.red.gangs.types.Entity;


public class EntityDamageListener
{
	@Listener
	public void onEntityDamaged(DamageEntityEvent event)
	{
		try {
			if (event.getCause().root() instanceof Player){
				Config config = Gangs.getConfig();
				
				Player source = (Player) event.getCause().root();
				Entity sourceEntity = new Entity(source.getUniqueId());
				Gang sourceGang = sourceEntity.getGang();
				
				org.spongepowered.api.entity.Entity target = (org.spongepowered.api.entity.Entity) event.getCause().root();
				Entity targetEntity = new Entity(target.getUniqueId());
				Gang targetGang = targetEntity.getGang();
	
				if(config.getBool("gang.friendlyFire") == true && sourceGang == targetGang) {
					event.setBaseDamage(0);
					event.setCancelled(true);
				}
			}else{
				System.out.println("Damage source is not a player : " + event.getCause().root().getClass().getName());
			}
		} catch (Exception e) {
			event.setBaseDamage(0);
			event.setCancelled(true);
			e.printStackTrace();
		}
	}
}