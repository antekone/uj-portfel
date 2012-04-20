package pl.edu.uj.portfel.db;

import pl.edu.uj.portfel.ErrorReporter;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class Database extends SQLiteOpenHelper {

	public static final String DATABASE_NAME = "portfel";
	public static final String ACCOUNTS_TABLE_NAME = "wpl_accounts";
	
	public static final int DATABASE_VERSION = 2;
	private ErrorReporter reporter;
	
	public Database(Context ctx, ErrorReporter reporter) {
		super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
		this.reporter = reporter;
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.v("db", String.format("creating table %s", ACCOUNTS_TABLE_NAME));
		
		db.execSQL(String.format("CREATE TABLE %s ( _id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, type INTEGER, ibalance DOUBLE )", ACCOUNTS_TABLE_NAME));
	}
	
	public AccountDao findAccountByName(String name) {
		Cursor c = getReadableDatabase().query(ACCOUNTS_TABLE_NAME, AccountDao.getColumnList(), "name=?", new String[] { name }, null, null, null, "1");
		if(c.getCount() != 1)
			return null;
		
		c.moveToFirst();
		return AccountDao.newFromCursor(c);
	}
	
	public int getAccountCount() {
		Cursor c = getReadableDatabase().query(ACCOUNTS_TABLE_NAME, new String[] { "count(_id) as count" }, null, null, null, null, null, "1");
		if(c.getCount() != 1) {
			reporter.reportError(":( -- getAccountCount()");
			return 0;
		}
		
		c.moveToFirst();
		return c.getInt(c.getColumnIndexOrThrow("count"));
	}
	
	public AccountDao getAccountByIndex(int idx) {
		Cursor c = getReadableDatabase().query(ACCOUNTS_TABLE_NAME, AccountDao.getColumnList(), "_id=?", new String[] { Integer.toString(idx) }, null, null, null, "1");
		if(c.getCount() != 1)
			return null;
		
		c.moveToFirst();
		return AccountDao.newFromCursor(c);
	}
	
	public boolean writeAccount(String name, int type, double initialBalance) {
		AccountDao dao = new AccountDao();
		dao.name = name.trim();
		dao.type = type;
		dao.initialBalance = initialBalance;
		return writeAccount(dao);
	}
	
	public boolean writeAccount(AccountDao dao) {
		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = dao.toContentValues();

		try {
			long id = db.insert(ACCOUNTS_TABLE_NAME, null, values);
			dao.id = id;
			return true;
		} catch(SQLException e) {
			reportError(":( -- sql#1");
			return false;
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int v1, int v2) {
		Log.v("db", String.format("dropping table %s", ACCOUNTS_TABLE_NAME));

		reporter.reportInfo("Aktualizowanie tabel SQL...");
		db.execSQL(String.format("drop table %s", ACCOUNTS_TABLE_NAME));
	}
	
	void reportError(String err) {
		reporter.reportError(err);
	}
}
