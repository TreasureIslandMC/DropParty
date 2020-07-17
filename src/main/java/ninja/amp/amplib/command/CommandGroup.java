package ninja.amp.amplib.command;

import ninja.amp.amplib.AmpJavaPlugin;
import ninja.amp.amplib.messenger.DefaultMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CommandGroup {
	private final AmpJavaPlugin plugin;
	private final String name;
	private Permission permission;
	private int minArgsLength = 0;
	private int maxArgsLength = -1;
	private boolean playerOnly = true;
	private final Map<String, CommandGroup> children = new LinkedHashMap<>();

	public CommandGroup(AmpJavaPlugin plugin, String name) {
		this.plugin = plugin;
		this.name = name.toLowerCase();
	}

	public String getName() {
		return this.name;
	}

	public Permission getPermission() {
		return this.permission;
	}

	public void setPermission(Permission permission) {
		this.permission = permission;
		if (this.plugin.getServer().getPluginManager().getPermission(permission.getName()) == null) {
			this.plugin.getServer().getPluginManager().addPermission(permission);
		}

	}

	public int getMinArgsLength() {
		return this.minArgsLength;
	}

	public int getMaxArgsLength() {
		return this.maxArgsLength;
	}

	public void setArgumentRange(int minArgsLength, int maxArgsLength) {
		this.minArgsLength = minArgsLength;
		this.maxArgsLength = maxArgsLength;
	}

	public boolean isPlayerOnly() {
		return this.playerOnly;
	}

	public void setPlayerOnly(boolean playerOnly) {
		this.playerOnly = playerOnly;
	}

	public boolean hasChildCommand(String name) {
		return this.children.containsKey(name.toLowerCase());
	}

	public CommandGroup getChildCommand(String name) {
		return this.children.get(name.toLowerCase());
	}

	public CommandGroup addChildCommand(CommandGroup command) {
		this.children.put(command.getName().toLowerCase(), command);
		if (this.permission != null && command.getPermission() != null) {
			command.getPermission().addParent(this.permission, true);
		}

		return this;
	}

	public List<CommandGroup> getChildren(boolean deep) {
		if (deep) {
			List<CommandGroup> deepChildren = new ArrayList<>();

			CommandGroup child;
			for(Iterator var3 = this.children.values().iterator(); var3.hasNext(); deepChildren.addAll(child.getChildren(true))) {
				child = (CommandGroup)var3.next();
				if (child instanceof Command) {
					deepChildren.add(child);
				}
			}

			return deepChildren;
		} else {
			return new ArrayList<>(this.children.values());
		}
	}

	public List<String> getTabCompleteList(String[] args) {
		return new ArrayList<>(this.children.keySet());
	}

	public void execute(String command, CommandSender sender, String[] args) throws UnsupportedEncodingException {
		CommandGroup entry = this.children.get(command.toLowerCase());
		if (entry instanceof Command) {
			if (entry.getMinArgsLength() > args.length && entry.getMinArgsLength() != -1 || entry.getMaxArgsLength() < args.length && entry.getMaxArgsLength() != -1) {
				this.plugin.getMessenger().sendMessage(sender, DefaultMessage.COMMAND_USAGE, ((Command)entry).getCommandUsage());
			} else if (entry.getPermission() != null && !sender.hasPermission(entry.getPermission())) {
				this.plugin.getMessenger().sendMessage(sender, DefaultMessage.COMMAND_NOPERMISSION, command);
			} else if (!(sender instanceof Player) && entry.isPlayerOnly()) {
				this.plugin.getMessenger().sendMessage(sender, DefaultMessage.COMMAND_NOTAPLAYER);
			} else {
				entry.execute(command, sender, args);
			}
		} else {
			String subCommand = args.length == 0 ? "" : args[0];
			if (entry.hasChildCommand(subCommand)) {
				String[] newArgs;
				if (args.length == 0) {
					newArgs = args;
				} else {
					newArgs = new String[args.length - 1];
					System.arraycopy(args, 1, newArgs, 0, args.length - 1);
				}

				entry.execute(subCommand, sender, newArgs);
			} else {
				this.plugin.getMessenger().sendMessage(sender, DefaultMessage.COMMAND_INVALID, "\"" + subCommand + "\"", "\"" + command + "\"");
			}
		}

	}
}