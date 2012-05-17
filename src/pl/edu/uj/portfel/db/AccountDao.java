package pl.edu.uj.portfel.db;

import android.content.ContentValues;
import android.database.Cursor;

public class AccountDao {
	public String name;
	public int type;
	public long id;
	public double initialBalance;
	
	public static AccountDao newFromCursor(Cursor c) {
		AccountDao dao = new AccountDao();
		
		dao.id = c.getLong(c.getColumnIndexOrThrow("_id"));
		dao.name = c.getString(c.getColumnIndexOrThrow("name"));
		dao.type = c.getInt(c.getColumnIndexOrThrow("type"));
		dao.initialBalance = c.getDouble(c.getColumnIndexOrThrow("ibalance"));
		
		return dao;
	}
	
	public static String[] getColumnList() {
		return new String[] { "_id", "name", "type", "ibalance" };
	}
	
	public ContentValues toContentValues() {
		ContentValues values = new ContentValues();
		
		values.put("name", name);
		values.put("type", type);
		values.put("ibalance", initialBalance);
		
		return values;
	}
	
	public String getName() { return name; }
	public long getId() { return id; }
}
