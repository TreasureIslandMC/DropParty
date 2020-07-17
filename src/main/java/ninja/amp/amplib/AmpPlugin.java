package ninja.amp.amplib;

import ninja.amp.amplib.command.CommandController;
import ninja.amp.amplib.config.ConfigManager;
import ninja.amp.amplib.messenger.Messenger;

import java.io.UnsupportedEncodingException;

public interface AmpPlugin {
	void enableAmpLib() throws UnsupportedEncodingException;

	void disableAmpLib();

	ConfigManager getConfigManager();

	void setConfigManager(ConfigManager var1);

	Messenger getMessenger();

	void setMessenger(Messenger var1);

	CommandController getCommandController();

	void setCommandController(CommandController var1);
}
