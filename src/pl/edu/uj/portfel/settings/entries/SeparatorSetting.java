package pl.edu.uj.portfel.settings.entries;

public class SeparatorSetting implements Setting {
	public int type;
	
	public SeparatorSetting(int type) {
		this.type = type;
	}

	@Override
	public Class getDestActivity() {
		return null;
	}
}
