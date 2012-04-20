package pl.edu.uj.portfel.login;

public class LoginEntryChooser {
	private String entryText;
	
	public LoginEntryChooser(String initialUsername) {
		entryText = initialUsername;
	}
	
	public String getEntryText() { return entryText; }
	public void setEntryText(String s) { entryText = s; }
}
