package cat.red.gangs.cmd;

import java.util.HashMap;
import java.util.List;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.service.pagination.PaginationService;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import com.google.common.collect.Lists;

public class HelpCmd implements CommandExecutor {
	
	HashMap<List<String>, CommandSpec> allCommands;
	
	public HelpCmd(HashMap<List<String>, CommandSpec> commands){
		super();
		allCommands = commands;
	}
	
	public CommandResult execute(CommandSource src, CommandContext ctx) throws CommandException
	{
		List<Text> helpList = Lists.newArrayList();

		for (List<String> aliases : allCommands.keySet()) {
			CommandSpec commandSpec = allCommands.get(aliases);
			
			if(commandSpec.testPermission(src)) {
				Text commandHelp = Text.builder()
					.append(Text.builder()
						.append(Text.of(TextColors.WHITE, "Command: "))
						.append(Text.of(aliases.toString(), "\n"))
						.build())
					.append(Text.builder()
						.append(Text.of(TextColors.WHITE, "Command Description: "), commandSpec.getShortDescription(src).get(), Text.of("\n"))
						.build())
					.append(Text.builder()
						.append(Text.of(TextColors.WHITE, "Command Arguments: "), commandSpec.getUsage(src), Text.of("\n"))
						.build())
					.append(Text.builder()
						.append(Text.of(TextColors.WHITE, "Permission Node: "), 
							Text.of(commandSpec.toString().substring(commandSpec.toString().lastIndexOf("permission") + 11, 
									commandSpec.toString().indexOf("argumentParser") - 2)), 
							Text.of("\n"))
						.build())
					.build();
				helpList.add(commandHelp);
			}
		}

		PaginationService paginationService = Sponge.getServiceManager().provide(PaginationService.class).get();
		PaginationList.Builder paginationBuilder = paginationService.builder().title(Text.of(TextColors.RED, "Help")).padding(Text.of("-")).contents(helpList);
		paginationBuilder.sendTo(src);
		return CommandResult.success();
	}
}
