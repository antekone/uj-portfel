package pl.edu.uj.portfel.settings.entries;

public class DefaultSetting implements Setting {
	public int cid;
	public int hid;
	public boolean idValues;
	public Class activityClass;

	public DefaultSetting(int cid, int hid, Class aclass) {
		this.cid = cid;
		this.hid = hid;
		activityClass = aclass;
	}

	@Override
	public Class getDestActivity() {
		return activityClass;
	}
}
