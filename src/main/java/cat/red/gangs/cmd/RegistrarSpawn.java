package cat.red.gangs.cmd;

import java.util.ArrayList;
import java.util.List;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.manipulator.mutable.entity.AgentData;
import org.spongepowered.api.data.manipulator.mutable.entity.TradeOfferData;
import org.spongepowered.api.data.type.Careers;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.living.Villager;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.entity.spawn.EntitySpawnCause;
import org.spongepowered.api.event.cause.entity.spawn.SpawnTypes;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.merchant.TradeOffer;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.extent.Extent;

import cat.red.gangs.Gangs;
import cat.red.gangs.utils.Config;


public class RegistrarSpawn implements CommandExecutor {
	public CommandResult execute(CommandSource src, CommandContext ctx) throws CommandException
	{
		if(src instanceof Player){
			try {
				Config config = Gangs.getConfig();
				
				Player srcPlayer = (Player) src;
				Location<World> spawnLocation =  srcPlayer.getLocation();
				Extent extent = spawnLocation.getExtent();
				Villager villager = (Villager) extent.createEntity(EntityTypes.VILLAGER, spawnLocation.getPosition());
				
				ItemStack adminBook = ItemStack.builder().itemType(ItemTypes.BOOK).build();
				List<Text> adminBookLore = new ArrayList<Text>();
				adminBookLore.add(config.getText("registrar.book.lore"));
				
				adminBook.offer(Keys.DISPLAY_NAME, config.getText("registrar.book.name"));
				adminBook.offer(Keys.ITEM_LORE, adminBookLore);
				adminBook.offer(Keys.BOOK_AUTHOR, config.getText("registrar.name"));
				
				TradeOfferData tradeOfferData = Sponge.getDataManager().getManipulatorBuilder(TradeOfferData.class).get().create();
				
				tradeOfferData.set(tradeOfferData.tradeOffers()
					.add(TradeOffer.builder()
						.firstBuyingItem(ItemStack.of(ItemTypes.BOOK, 1))
						.secondBuyingItem(ItemStack.of(ItemTypes.DIAMOND, 3))
						.sellingItem(adminBook)
						.uses(0)
						.maxUses(Integer.MAX_VALUE)
						.canGrantExperience(false)
						.build()
					)
				);
				
				villager.setRotation(srcPlayer.getRotation());
				villager.setHeadRotation(srcPlayer.getHeadRotation());
				villager.offer(Keys.CAREER, Careers.CLERIC);
				villager.offer(Keys.DISPLAY_NAME, config.getText("registrar.name"));
                villager.offer(Keys.AI_ENABLED, false);
                villager.offer(Keys.CUSTOM_NAME_VISIBLE, true);
				villager.offer(tradeOfferData);
				
				extent.spawnEntity(villager,
					Cause.source(EntitySpawnCause.builder()
					.entity(villager).type(SpawnTypes.PLUGIN).build()).build());
				
				src.sendMessage(Text.of(TextColors.GREEN, "Registrar villager has been spawn on your location"));
				return CommandResult.success();			
			} catch (Exception e){
				src.sendMessage(Text.of(TextColors.RED, "Something went wrong executing the command"));
				e.printStackTrace();
				return CommandResult.success();	
			}
		}else{
			src.sendMessage(Text.of(TextColors.RED, "Only logged-in players can spawn registrars"));
			return CommandResult.success();					
		}

	}
}
