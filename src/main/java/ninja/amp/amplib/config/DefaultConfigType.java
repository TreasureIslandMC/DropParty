package ninja.amp.amplib.config;

public enum DefaultConfigType implements ConfigType {
	MESSAGE("lang.yml");

	private final String fileName;

	private DefaultConfigType(String fileName) {
		this.fileName = fileName;
	}

	public String getFileName() {
		return this.fileName;
	}
}
