package pl.edu.uj.portfel.db;

import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;

import pl.edu.uj.portfel.transaction.TransactionAttribute;
import pl.edu.uj.portfel.transaction.TransactionInputActivity.TransactionType;

public class TransactionDao {
	private long id;
	private int type;
	private long amount;
	private List<TransactionAttribute> attributes;
	
	public long getId() { return id; }
	public void setId(long i) { id = i; }
	
	public void setAmount(long x)  { amount = x; }
	public long getAmount() { return amount; }
	
	public int getType() { return type; }
	
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
		
		values.put("type", type);
		values.put("amount", amount);
		
		return values;
	}
	
	public static TransactionDao newFromCursor(Cursor c) {
		TransactionDao dao = new TransactionDao();
		
		dao.id = c.getLong(c.getColumnIndexOrThrow("_id"));
		dao.amount = c.getLong(c.getColumnIndexOrThrow("amount"));
		dao.type = c.getInt(c.getColumnIndexOrThrow("type"));
		
		return dao;
	}
	
	public static String[] getColumnList() {
		return new String[] { "_id", "amount", "type" };
	}
}
