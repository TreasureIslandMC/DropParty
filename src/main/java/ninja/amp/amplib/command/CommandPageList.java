package ninja.amp.amplib.command;

import ninja.amp.amplib.AmpJavaPlugin;
import ninja.amp.amplib.messenger.Messenger;
import ninja.amp.amplib.messenger.PageList;

import java.util.ArrayList;
import java.util.List;

public class CommandPageList extends PageList {
	public CommandPageList(AmpJavaPlugin plugin) {
		super(plugin, "Commands", 8);
		List<String> strings = new ArrayList<>();

		for (final CommandGroup command : plugin.getCommandController().getCommands()) {
			for (final CommandGroup child : command.getChildren(true)) {
				strings.add(Messenger.PRIMARY_COLOR + ((Command) child).getCommandUsage());
				strings.add(Messenger.SECONDARY_COLOR + "-" + ((Command) child).getDescription());
			}
		}

		this.setStrings(strings);
	}
}