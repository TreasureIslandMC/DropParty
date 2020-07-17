package ninja.amp.amplib.command.commands;

import ninja.amp.amplib.AmpJavaPlugin;
import ninja.amp.amplib.command.Command;
import ninja.amp.amplib.messenger.DefaultMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

public class ReloadCommand extends Command {
	private final AmpJavaPlugin plugin;

	public ReloadCommand(AmpJavaPlugin plugin) {
		super(plugin, "reload");
		this.plugin = plugin;
		String pluginName = plugin.getName();
		this.setDescription("Reloads the " + pluginName + " plugin");
		this.setPermission(new Permission(pluginName.toLowerCase() + ".reload", PermissionDefault.OP));
		this.setPlayerOnly(false);
	}

	public void execute(String command, CommandSender sender, String[] args) {
		this.plugin.onDisable();
		this.plugin.onEnable();
		this.plugin.getMessenger().sendMessage(sender, DefaultMessage.RELOAD);
	}
}
