package cat.red.gangs.cmd;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import cat.red.gangs.PluginInfo;

public class VersionCmd implements CommandExecutor {
	public CommandResult execute(CommandSource src, CommandContext ctx) throws CommandException
	{
		src.sendMessage(Text.of(TextColors.RED, "Gangs version: ", TextColors.WHITE, PluginInfo.ID));
		return CommandResult.success();
	}
}
