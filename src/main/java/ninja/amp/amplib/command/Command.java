package ninja.amp.amplib.command;

import ninja.amp.amplib.AmpJavaPlugin;
import org.bukkit.command.CommandSender;

import java.io.UnsupportedEncodingException;

public abstract class Command extends CommandGroup {
	private String commandUsage;
	private String description;

	public Command(AmpJavaPlugin plugin, String name) {
		super(plugin, name);
	}

	public String getCommandUsage() {
		return this.commandUsage;
	}

	public void setCommandUsage(String commandUsage) {
		this.commandUsage = commandUsage;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public abstract void execute(String var1, CommandSender var2, String[] var3) throws UnsupportedEncodingException;
}