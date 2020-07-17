package ninja.amp.amplib.command;

import ninja.amp.amplib.AmpJavaPlugin;
import ninja.amp.amplib.messenger.DefaultMessage;
import ninja.amp.amplib.messenger.PageList;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executor;

public class CommandController implements TabExecutor {
	private final AmpJavaPlugin plugin;
	private Set<CommandGroup> commands = new HashSet<>();
	private PageList pageList = null;

	public CommandController(AmpJavaPlugin plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(final CommandSender sender, final org.bukkit.command.Command cmd, final String label, final String[] args) {
		Iterator var5 = this.commands.iterator();

		CommandGroup command;
		do {
			if (!var5.hasNext()) {
				return false;
			}

			command = (CommandGroup) var5.next();
		} while (!command.getName().equalsIgnoreCase(cmd.getName()));

		String subCommand = args.length > 0 ? args[0] : "";
		if (command.hasChildCommand(subCommand)) {
			if (subCommand.equals("")) {
				try {
					command.execute(subCommand, sender, args);
				} catch (UnsupportedEncodingException var11) {
					var11.printStackTrace();
				}
			} else {
				String[] newArgs;
				if (args.length == 1) {
					newArgs = new String[0];
				} else {
					newArgs = new String[args.length - 1];
					System.arraycopy(args, 1, newArgs, 0, args.length - 1);
				}

				try {
					command.execute(subCommand, sender, newArgs);
				} catch (UnsupportedEncodingException var10) {
					var10.printStackTrace();
				}
			}
		} else {
			this.plugin.getMessenger().sendMessage(sender, DefaultMessage.COMMAND_INVALID, new Object[]{"\"" + subCommand + "\"", "\"" + command.getName() + "\""});
		}

		return true;
	}

	@Override
	public List<String> onTabComplete(final CommandSender sender, final org.bukkit.command.Command cmd, final String alias, final String[] args) {
		Iterator var5 = this.commands.iterator();

		CommandGroup command;
		do {
			if (!var5.hasNext()) {
				return new ArrayList<>();
			}

			command = (CommandGroup) var5.next();
		} while (!command.getName().equalsIgnoreCase(cmd.getName()));

		if (args.length > 0) {
			int commandAmount = 1;
			String[] newArgs = args;
			int var9 = args.length;

			for (int var10 = 0; var10 < var9; ++var10) {
				String arg = newArgs[var10];
				if (command.hasChildCommand(arg)) {
					command = command.getChildCommand(arg);
					++commandAmount;
				}
			}

			if (args.length == 1) {
				newArgs = new String[0];
			} else {
				newArgs = new String[args.length - commandAmount];
				System.arraycopy(args, commandAmount, newArgs, 0, args.length - commandAmount);
			}

			return command.getTabCompleteList(newArgs);
		} else {
			return command.getTabCompleteList(args);
		}
	}

	public Set<CommandGroup> getCommands() {
		return this.commands;
	}

	public void addCommand(CommandGroup command) {
		this.commands.add(command);
		this.plugin.getCommand(command.getName()).setExecutor(this);
		this.pageList = new CommandPageList(this.plugin);
	}

	public PageList getPageList() {
		return this.pageList;
	}

	public void destroy() {
		for (final CommandGroup command : this.commands) {
			this.plugin.getCommand(command.getName()).setExecutor(null);
		}

		this.commands.clear();
		this.pageList = null;
	}


}