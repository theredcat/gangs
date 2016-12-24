package cat.red.gangs.events;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.world.ExplosionEvent;
import org.spongepowered.api.world.explosion.Explosion;

import cat.red.gangs.Gangs;
import cat.red.gangs.types.Territory;
import cat.red.gangs.utils.Config;
import cat.red.gangs.utils.Database;

public class ExplosionEventListener
{
	@Listener
	public void onExplosion(ExplosionEvent.Pre event)
	{
		Database data = Gangs.getDatabase();
		Config config = Gangs.getConfig();
		Territory territory = data.getTerritory(event.getExplosion().getLocation());
		
		if (territory.isClaimed())
		{
			Boolean shouldExplode = config.getBool("claimed.protection.explosion.explode");
			Boolean shouldCauseFire = config.getBool("claimed.protection.explosion.fire");
			
			Explosion explosion = Sponge.getRegistry().createBuilder(Explosion.Builder.class)
					.from(event.getExplosion())
					.shouldBreakBlocks(shouldExplode)
					.canCauseFire(shouldCauseFire)
					.build();
				
			event.setExplosion(explosion);
		}
	}
}
