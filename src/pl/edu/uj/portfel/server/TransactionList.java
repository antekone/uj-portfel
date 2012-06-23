package pl.edu.uj.portfel.server;

import java.util.ArrayList;
import java.util.List;

import pl.edu.uj.portfel.db.TransactionDao;

public class TransactionList {
	private List<TransactionDao> transactionList;
	boolean malformed;
	
	public TransactionList(boolean _malformed) {
		transactionList = new ArrayList<TransactionDao>();
		malformed = _malformed;
	}
	
	public boolean isMalformed() { return malformed; }
	public void setMalformed(boolean b) { malformed = b; }
	public List<TransactionDao> getTransactionList() { return transactionList; }
}
