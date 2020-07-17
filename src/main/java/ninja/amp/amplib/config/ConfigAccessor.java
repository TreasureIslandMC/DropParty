package ninja.amp.amplib.config;

import ninja.amp.amplib.AmpJavaPlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;

public class ConfigAccessor {
	private final AmpJavaPlugin plugin;
	private ConfigType configType;
	private File configFile;
	private FileConfiguration fileConfiguration;

	public ConfigAccessor(AmpJavaPlugin plugin, ConfigType configType, File parent) {
		this.plugin = plugin;
		this.configType = configType;
		this.configFile = new File(parent, configType.getFileName());
	}

	public ConfigAccessor reloadConfig() throws UnsupportedEncodingException {
		this.fileConfiguration = YamlConfiguration.loadConfiguration(this.configFile);
		Reader defConfigStream = new InputStreamReader(this.plugin.getResource(this.configType.getFileName()), "UTF8");
		if (defConfigStream != null) {
			YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
			this.fileConfiguration.setDefaults(defConfig);
		}

		return this;
	}

	public FileConfiguration getConfig() throws UnsupportedEncodingException {
		if (this.fileConfiguration == null) {
			this.reloadConfig();
		}

		return this.fileConfiguration;
	}

	public ConfigAccessor saveConfig() {
		if (this.fileConfiguration != null) {
			try {
				this.getConfig().save(this.configFile);
			} catch (IOException var2) {
				this.plugin.getLogger().log(Level.SEVERE, "Could not save config to " + this.configFile, var2);
			}
		}

		return this;
	}

	public ConfigAccessor saveDefaultConfig() {
		if (!this.configFile.exists()) {
			this.plugin.saveResource(this.configType.getFileName(), false);
		}

		return this;
	}

	public ConfigType getConfigType() {
		return this.configType;
	}

	public void destroy() {
		this.configType = null;
		this.configFile = null;
		this.fileConfiguration = null;
	}
}