package pl.edu.uj.portfel.server;

public class LoginUserReturnValues {
	private boolean loginOK;
	private String token;
	private boolean malformed;
	
	public LoginUserReturnValues(String _token, boolean _malformed) {
		loginOK = _token.length() > 0;
		token = _token;
		malformed = _malformed;
	}
	
	public boolean isLoginOK() {
		return loginOK;
	}
	
	public String getToken() {
		return token;
	}
	
	public boolean isAnswerMalformed() {
		return malformed;
	}
}
