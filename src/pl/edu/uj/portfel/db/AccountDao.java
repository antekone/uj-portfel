package pl.edu.uj.portfel.db;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.database.Cursor;

public class AccountDao {
	public String name;
	public int type;
	public long id;
	public long rid;
	public double initialBalance;
	
	public static AccountDao newFromCursor(Cursor c) {
		AccountDao dao = new AccountDao();
		
		dao.id = c.getLong(c.getColumnIndexOrThrow("_id"));
		dao.rid = c.getLong(c.getColumnIndexOrThrow("rid"));
		dao.name = c.getString(c.getColumnIndexOrThrow("name"));
		dao.type = c.getInt(c.getColumnIndexOrThrow("type"));
		dao.initialBalance = c.getDouble(c.getColumnIndexOrThrow("ibalance"));
		c.close();
		return dao;
	}
	
	public static AccountDao newFromJSON(JSONObject json) throws JSONException {
		AccountDao dao = new AccountDao();
		
		dao.id = 0;
		dao.rid = json.getInt("id");
		dao.name = json.getString("name");
		dao.type = 0;
		dao.initialBalance = (double) json.getLong("balance_in_cents");
		
		return dao;
	}
	
	public static String[] getColumnList() {
		return new String[] { "_id", "rid", "name", "type", "ibalance" };
	}
	
	public ContentValues toContentValues() {
		ContentValues values = new ContentValues();
		
		values.put("name", name);
		values.put("type", type);
		values.put("ibalance", initialBalance);
		values.put("rid", rid);
		
		return values;
	}
	
	public String getName() { return name; }
	public long getId() { return id; }
	public long getRemoteId() { return rid; }
}
