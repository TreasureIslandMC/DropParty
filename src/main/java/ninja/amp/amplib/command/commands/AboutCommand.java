package ninja.amp.amplib.command.commands;

import ninja.amp.amplib.AmpJavaPlugin;
import ninja.amp.amplib.command.Command;
import ninja.amp.amplib.messenger.Messenger;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import java.util.ArrayList;
import java.util.List;

public class AboutCommand extends Command {
	private final String header;
	private final List<String> info = new ArrayList<>();

	public AboutCommand(AmpJavaPlugin plugin) {
		super(plugin, "");
		String pluginName = plugin.getName();
		this.setDescription("Lists some information about " + pluginName);
		this.setPermission(new Permission(pluginName.toLowerCase() + ".about", PermissionDefault.TRUE));
		this.setPlayerOnly(false);
		this.header = Messenger.HIGHLIGHT_COLOR + "<-------<| " + Messenger.PRIMARY_COLOR + "About " + pluginName + " " + Messenger.HIGHLIGHT_COLOR + "|>------->";
		this.info.add(Messenger.SECONDARY_COLOR + "Author: " + StringUtils.join(plugin.getDescription().getAuthors(), ", "));
		this.info.add(Messenger.SECONDARY_COLOR + "Version: " + plugin.getDescription().getVersion());
	}

	public void execute(String command, CommandSender sender, String[] args) {
		sender.sendMessage(this.header);
		this.info.forEach(sender::sendMessage);
	}

	public void addInfo(String message) {
		this.info.add(message);
	}
}