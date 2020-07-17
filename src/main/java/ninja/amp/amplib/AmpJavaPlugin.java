package ninja.amp.amplib;

import ninja.amp.amplib.command.CommandController;
import ninja.amp.amplib.config.ConfigManager;
import ninja.amp.amplib.messenger.Messenger;
import ninja.amp.amplib.messenger.RecipientHandler;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;


import java.io.UnsupportedEncodingException;

public class AmpJavaPlugin extends JavaPlugin implements AmpPlugin {
	private ConfigManager configManager;
	private Messenger messenger;
	private CommandController commandController;

	public void enableAmpLib() throws UnsupportedEncodingException {
		this.configManager = new ConfigManager(this);
		this.messenger = (new Messenger(this)).registerRecipient(CommandSender.class, new RecipientHandler() {
			public void sendMessage(Object recipient, String message) {
				((CommandSender)recipient).sendMessage(message);
			}
		}).registerRecipient(Server.class, new RecipientHandler() {
			public void sendMessage(Object recipient, String message) {
				((Server)recipient).broadcastMessage(message);
			}
		});
		this.commandController = new CommandController(this);
	}

	public void disableAmpLib() {
		this.commandController.destroy();
		this.commandController = null;
		this.messenger.destroy();
		this.messenger = null;
		this.configManager.destroy();
		this.configManager = null;
	}

	public ConfigManager getConfigManager() {
		return this.configManager;
	}

	public void setConfigManager(ConfigManager configManager) {
		this.configManager = configManager;
	}

	public Messenger getMessenger() {
		return this.messenger;
	}

	public void setMessenger(Messenger messenger) {
		this.messenger = messenger;
	}

	public CommandController getCommandController() {
		return this.commandController;
	}

	public void setCommandController(CommandController commandController) {
		this.commandController = commandController;
	}
}