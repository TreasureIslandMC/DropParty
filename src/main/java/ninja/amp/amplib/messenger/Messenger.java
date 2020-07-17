package ninja.amp.amplib.messenger;

import ninja.amp.amplib.AmpJavaPlugin;
import ninja.amp.amplib.config.DefaultConfigType;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import sun.rmi.runtime.Log;

import java.io.UnsupportedEncodingException;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Messenger {
	private final AmpJavaPlugin plugin;
	private boolean debug;
	private Logger log;
	private Map<Class<?>, RecipientHandler> recipientHandlers = new HashMap<>();
	public static ChatColor PRIMARY_COLOR;
	public static ChatColor SECONDARY_COLOR;
	public static ChatColor HIGHLIGHT_COLOR;

	public Messenger(AmpJavaPlugin plugin) throws UnsupportedEncodingException {
		this.plugin = plugin;
		this.debug = plugin.getConfig().getBoolean("debug", false);
		this.log = plugin.getLogger();
		this.registerMessages(EnumSet.allOf(DefaultMessage.class));
	}

	public Messenger registerMessages(EnumSet<? extends Message> messageEnum) throws UnsupportedEncodingException {
		FileConfiguration messageConfig = this.plugin.getConfigManager().getConfig(DefaultConfigType.MESSAGE);
		Iterator var3 = messageEnum.iterator();

		Message message;
		while(var3.hasNext()) {
			message = (Message)var3.next();
			messageConfig.addDefault(message.getPath(), message.getMessage());
		}

		messageConfig.options().copyDefaults(true);
		this.plugin.getConfigManager().getConfigAccessor(DefaultConfigType.MESSAGE).saveConfig();
		var3 = messageEnum.iterator();

		while(var3.hasNext()) {
			message = (Message)var3.next();
			message.setMessage(ChatColor.translateAlternateColorCodes('&', messageConfig.getString(message.getPath())));
		}

		return this;
	}

	public Messenger registerRecipient(Class recipientClass, RecipientHandler recipientHandler) {
		this.recipientHandlers.put(recipientClass, recipientHandler);
		return this;
	}

	public boolean sendMessage(Object recipient, Message message, Object... replace) {
		return this.sendRawMessage(recipient, DefaultMessage.PREFIX + (replace == null ? message.getMessage() : String.format(message.getMessage(), (Object[])replace)));
	}

	public boolean sendRawMessage(Object recipient, Object message) {
		if (recipient != null && message != null) {

			for (final Class<?> aClass : this.recipientHandlers.keySet()) {
				Class<?> recipientClass = aClass;
				if (recipientClass.isAssignableFrom(recipient.getClass())) {
					(this.recipientHandlers.get(recipientClass)).sendMessage(recipient, message.toString());
					return true;
				}
			}
		}

		return false;
	}

	public void log(Level level, Object... messages) {
		Object[] var3 = messages;
		int var4 = messages.length;

		for(int var5 = 0; var5 < var4; ++var5) {
			Object message = var3[var5];
			this.log.log(level, message.toString());
		}

	}

	public void debug(Exception e) {
		if (this.debug) {
			e.printStackTrace();
		}

	}

	public void debug(Object message) {
		if (this.debug) {
			this.log.log(Level.INFO, message.toString());
		}

	}

	public Logger getLogger() {
		return this.log;
	}

	public void destroy() {
		this.debug = false;
		this.log = null;
		this.recipientHandlers.clear();
		this.recipientHandlers = null;
	}

	static {
		PRIMARY_COLOR = ChatColor.WHITE;
		SECONDARY_COLOR = ChatColor.WHITE;
		HIGHLIGHT_COLOR = ChatColor.WHITE;
	}
}