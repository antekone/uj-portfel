package pl.edu.uj.portfel.db;

import java.util.List;

import pl.edu.uj.portfel.transaction.TransactionAttribute;
import pl.edu.uj.portfel.transaction.TransactionInputActivity;
import pl.edu.uj.portfel.transaction.TransactionInputActivity.TransactionType;
import android.content.ContentValues;
import android.database.Cursor;

public class TransactionDao {
	private long id;
	private long accId;
	private int type;
	private long amount;
	private long timestamp;
	private List<TransactionAttribute> attributes;
	
	public long getId() { return id; }
	public void setId(long i) { id = i; }
	
	public long getAccId() { return accId; }
	public void setAccId(long _accId) { accId = _accId; }
	
	public void setAmount(long x)  { amount = x; }
	public long getAmount() { return amount; }
	
	public long getTimestamp() { return timestamp; }
	public void setTimestamp(long t) { timestamp = t; }
	
	public int getType() { return type; }
	
	public TransactionType getTypeObj() {
		if(type == 0)
			return TransactionType.EARNING;
		else if(type == 1)
			return TransactionType.EXPENSE;
		else
			throw new RuntimeException();
	}
	
	public void setType(TransactionType type) {
		if(type == TransactionType.EARNING)
			this.type = 0;
		else if(type == TransactionType.EXPENSE)
			this.type = 1;
		else {
			throw new RuntimeException();
		}
	}
	
	public ContentValues toContentValues() {
		ContentValues values = new ContentValues();
		
		values.put("accId", accId);
		values.put("type", type);
		values.put("amount", amount);
		values.put("timestamp", timestamp);
		
		return values;
	}
	
	public static TransactionDao newFromCursor(Cursor c) {
		TransactionDao dao = new TransactionDao();
		
		dao.id = c.getLong(c.getColumnIndexOrThrow("_id"));
		dao.accId = c.getLong(c.getColumnIndexOrThrow("accId"));
		dao.amount = c.getLong(c.getColumnIndexOrThrow("amount"));
		dao.type = c.getInt(c.getColumnIndexOrThrow("type"));
		dao.timestamp = c.getLong(c.getColumnIndexOrThrow("timestamp"));
		
		c.close();
		return dao;
	}
	
	public static String[] getColumnList() {
		return new String[] { "_id", "accId", "amount", "type", "timestamp" };
	}
}
