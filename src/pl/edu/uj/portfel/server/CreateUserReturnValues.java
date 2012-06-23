package pl.edu.uj.portfel.server;

public class CreateUserReturnValues {
	private boolean malformed;
	
	private String timestamp, updatedTimestamp;
	private String email;
	private int id;
	private String token;
	
	public CreateUserReturnValues(String timestamp, String email, int id, String phone, String updatedTimestamp, String token, boolean malformed) {
		this.timestamp = timestamp;
		this.updatedTimestamp = updatedTimestamp;
		this.email = email;
		this.id = id;
		this.token = token;
		this.malformed = malformed;
	}
	
	public boolean isMalformed() { return malformed; }
	
	public boolean isCreationOK() {
		return (! isMalformed()) && (
				timestamp.length() > 0 && updatedTimestamp.length() > 0 && email.length() > 0 && token.length() > 0
		);
	}
	
	public String getToken() { return token; }
	public int getId() { return id; }
}
