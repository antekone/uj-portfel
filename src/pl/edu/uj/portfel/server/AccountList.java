package pl.edu.uj.portfel.server;

import java.util.ArrayList;
import java.util.List;

import pl.edu.uj.portfel.db.AccountDao;

public class AccountList {
	private List<AccountDao> accounts;
	boolean malformed;
	
	public AccountList() {
		malformed = true;
		accounts = new ArrayList<AccountDao>();
	}
	
	public boolean isMalformed() { return malformed; }
	public void setMalformed(boolean f) { malformed = f; }
	public List<AccountDao> getAccounts() { return accounts; }
}
