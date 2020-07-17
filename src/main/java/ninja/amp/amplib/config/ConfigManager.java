package ninja.amp.amplib.config;

import ninja.amp.amplib.AmpJavaPlugin;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;


public class ConfigManager {
	private final AmpJavaPlugin plugin;
	private Map<ConfigType, ConfigAccessor> configs = new HashMap<>();

	public ConfigManager(AmpJavaPlugin plugin) {
		this.plugin = plugin;
		plugin.getConfig().options().copyDefaults(true);
		plugin.saveConfig();
		this.registerConfigTypes(EnumSet.allOf(DefaultConfigType.class));
	}

	public void registerConfigTypes(EnumSet<? extends ConfigType> configTypes) {
		File dataFolder = this.plugin.getDataFolder();

		for (final ConfigType configType : configTypes) {
			this.addConfigAccessor((new ConfigAccessor(this.plugin, configType, dataFolder)).saveDefaultConfig());
		}

	}

	public void addConfigAccessor(ConfigAccessor configAccessor) {
		this.configs.put(configAccessor.getConfigType(), configAccessor);
	}

	public ConfigAccessor getConfigAccessor(ConfigType configType) {
		return this.configs.get(configType);
	}

	public FileConfiguration getConfig(ConfigType configType) throws UnsupportedEncodingException {
		return this.configs.get(configType).getConfig();
	}

	public void destroy() {
		this.configs.values().forEach(ConfigAccessor::destroy);
		this.configs.clear();
		this.configs = null;
	}
}