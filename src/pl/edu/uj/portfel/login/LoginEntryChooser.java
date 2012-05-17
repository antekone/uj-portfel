package pl.edu.uj.portfel.login;

public class LoginEntryChooser {
	private String entryText;
	private long accId;
	
	public LoginEntryChooser(long accId, String initialUsername) {
		entryText = initialUsername;
		this.accId = accId;
	}
	
	public String getEntryText() { return entryText; }
	public void setEntryText(String s) { entryText = s; }
	public long getAccountId() { return accId; }
}
