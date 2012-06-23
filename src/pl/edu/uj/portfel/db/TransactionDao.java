package pl.edu.uj.portfel.db;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import pl.edu.uj.portfel.transaction.TransactionAttribute;
import pl.edu.uj.portfel.transaction.TransactionInputActivity.TransactionType;
import android.content.ContentValues;
import android.database.Cursor;

public class TransactionDao {
	private long id;
	private long rid;
	private long accId;
	private int type;
	private long amount;
	private long timestamp;
//	private List<TransactionAttribute> attributes;
	
	public long getId() { return id; }
	public void setId(long i) { id = i; }
	
	public long getAccId() { return accId; }
	public void setAccId(long _accId) { accId = _accId; }
	
	public void setAmount(long x)  { amount = x; }
	public long getAmount() { return amount; }
	
	public long getTimestamp() { return timestamp; }
	public void setTimestamp(long t) { timestamp = t; }
	
	public int getType() { return type; }
	
	public long getRid() { return rid; }
	public void setRid(long i) { rid = i; }
	
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
		values.put("rid", rid);
		values.put("type", type);
		values.put("amount", amount);
		values.put("timestamp", timestamp);
		
		return values;
	}
	
	public static TransactionDao newFromCursor(Cursor c) {
		TransactionDao dao = new TransactionDao();
		
		dao.id = c.getLong(c.getColumnIndexOrThrow("_id"));
		dao.rid = c.getLong(c.getColumnIndexOrThrow("rid"));
		dao.accId = c.getLong(c.getColumnIndexOrThrow("accId"));
		dao.amount = c.getLong(c.getColumnIndexOrThrow("amount"));
		dao.type = c.getInt(c.getColumnIndexOrThrow("type"));
		dao.timestamp = c.getLong(c.getColumnIndexOrThrow("timestamp"));
		
		c.close();
		return dao;
	}
	
	public static TransactionDao newFromJSON(JSONObject json, long accId) throws JSONException {
		TransactionDao dao = new TransactionDao();
		
		dao.id = 0;
		dao.rid = json.getInt("id");
		dao.accId = accId;
		dao.amount = json.getLong("value_in_cents");
		dao.type = dao.amount >= 0 ? 1 : 0;
		dao.timestamp = 0;
		
		return dao;
	}
	
	public static String[] getColumnList() {
		return new String[] { "_id", "rid", "accId", "amount", "type", "timestamp" };
	}
}
