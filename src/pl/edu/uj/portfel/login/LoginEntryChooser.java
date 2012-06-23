package pl.edu.uj.portfel.login;

public class LoginEntryChooser {
	private String entryText;
	private long accId, raccId;
	
	public LoginEntryChooser(long accId, long raccId, String initialUsername) {
		entryText = initialUsername;
		this.accId = accId;
		this.raccId = raccId;
	}
	
	public String getEntryText() { return entryText; }
	public void setEntryText(String s) { entryText = s; }
	public long getAccountId() { return accId; }
	public long getRemoteAccountId() { return raccId; }
}
