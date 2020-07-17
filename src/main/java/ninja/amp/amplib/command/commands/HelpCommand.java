package ninja.amp.amplib.command.commands;

import ninja.amp.amplib.AmpJavaPlugin;
import ninja.amp.amplib.command.Command;
import ninja.amp.amplib.messenger.PageList;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

public class HelpCommand extends Command {
	private final AmpJavaPlugin plugin;

	public HelpCommand(AmpJavaPlugin plugin) {
		super(plugin, "help");
		this.plugin = plugin;
		String pluginName = plugin.getName();
		this.setDescription("Lists all " + pluginName + " commands");
		this.setPermission(new Permission(pluginName.toLowerCase() + ".help", PermissionDefault.TRUE));
		this.setPlayerOnly(false);
	}

	public void execute(String command, CommandSender sender, String[] args) {
		int pageNumber = 1;
		if (args.length == 1) {
			pageNumber = PageList.getPageNumber(args[0]);
		}

		this.plugin.getCommandController().getPageList().sendPage(pageNumber, sender);
	}
}