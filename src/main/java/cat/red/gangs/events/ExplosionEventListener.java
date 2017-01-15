package cat.red.gangs.events;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.world.ExplosionEvent;
import org.spongepowered.api.world.explosion.Explosion;

import cat.red.gangs.Gangs;
import cat.red.gangs.types.Territory;
import cat.red.gangs.utils.Config;

public class ExplosionEventListener
{
	@Listener
	public void onExplosion(ExplosionEvent.Pre event)
	{
		try {
			Config config = Gangs.getConfig();
			Territory territory = new Territory(event.getExplosion().getLocation().getChunkPosition());
			
			if (territory.isClaimed())
			{
					Boolean shouldExplode = config.getBool("territory.explosion.explode");
					Boolean shouldCauseFire = config.getBool("territory.explosion.fire");
					
					Explosion explosion = Sponge.getRegistry().createBuilder(Explosion.Builder.class)
							.from(event.getExplosion())
							.shouldBreakBlocks(shouldExplode)
							.canCauseFire(shouldCauseFire)
							.build();
						
					event.setExplosion(explosion);
			}
		} catch (Exception e) {
			Explosion explosion = Sponge.getRegistry().createBuilder(Explosion.Builder.class)
					.from(event.getExplosion())
					.shouldBreakBlocks(false)
					.canCauseFire(false)
					.build();
				
			event.setExplosion(explosion);
			e.printStackTrace();
		}
	}
}
