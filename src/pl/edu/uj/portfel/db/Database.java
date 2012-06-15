package pl.edu.uj.portfel.db;

import pl.edu.uj.portfel.ErrorReporter;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

// /data/data/pl.edu.uj.portfel/databases/portfel

/**
 * 
 * sqlite> .schema wpl_accounts
 * CREATE TABLE wpl_accounts ( _id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, type INTEGER, ibalance DOUBLE );
 * 
 * sqlite> .schema wpl_transactions
 * CREATE TABLE wpl_transactions ( _id INTEGER PRIMARY KEY AUTOINCREMENT, accId INTEGER, amount INTEGER, type INTEGER , timestamp timestamp);
 *
 */

public class Database extends SQLiteOpenHelper {

	public static final String DATABASE_NAME = "portfel";
	public static final String ACCOUNTS_TABLE_NAME = "wpl_accounts";
	public static final String TRANSACTIONS_TABLE_NAME = "wpl_transactions";
	public static final String ATTRIBUTES_TABLE_NAME = "wpl_attributes";
	
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

		Log.v("db", String.format("creating table %s", TRANSACTIONS_TABLE_NAME));
		db.execSQL(String.format("CREATE TABLE %s ( _id INTEGER PRIMARY KEY AUTOINCREMENT, accId INTEGER, amount INTEGER, type INTEGER, timestamp TIMESTAMP )", TRANSACTIONS_TABLE_NAME));
		
		Log.v("db", String.format("creating table %s", ATTRIBUTES_TABLE_NAME));
		db.execSQL(String.format("CREATE TABLE %s ( _id INTEGER PRIMARY KEY AUTOINCREMENT, tId INTEGER, type INTEGER, title TEXT, aux1 TEXT, aux2 TEXT, aux3 TEXT)", ATTRIBUTES_TABLE_NAME));
	}
	
	public AccountDao findAccountByName(String name) {
		Cursor c = getReadableDatabase().query(ACCOUNTS_TABLE_NAME, AccountDao.getColumnList(), "name=?", new String[] { name }, null, null, null, "1");
		if(c.getCount() != 1) {
			c.close();			
			return null;
		}
		
		c.moveToFirst();
		return AccountDao.newFromCursor(c);
	}
	
	public int getAccountCount() {
		Cursor c = getReadableDatabase().query(ACCOUNTS_TABLE_NAME, new String[] { "count(_id) as count" }, null, null, null, null, null, "1");
		if(c.getCount() != 1) {
			reporter.reportError(":( -- getAccountCount()");
			c.close();			
			return 0;
		}
		
		c.moveToFirst();
		int ret = c.getInt(c.getColumnIndexOrThrow("count"));
		c.close();
		return ret;
	}
	
	public int getTransactionCount(long accId) {
		Cursor c = getReadableDatabase().query(TRANSACTIONS_TABLE_NAME, new String[] { "count(_id) as count" }, "accId=?", new String[] { Long.toString(accId) }, null, null, null, "1");
		if(c.getCount() != 1) {
			reporter.reportError(":( -- getTransactionCount()");
			c.close();			
			return 0;
		}
		
		c.moveToFirst();
		int ret = c.getInt(c.getColumnIndexOrThrow("count"));
		c.close();
		return ret;
	}
	
	public int getAttributeCount(long tid) {
		Cursor c = getReadableDatabase().query(ATTRIBUTES_TABLE_NAME, new String[] { "count(_id) as count" }, "tid=?", new String[] { Long.toString(tid) }, null, null, null, "1");
		if(c.getCount() != 1) {
			reporter.reportError(":( -- getAttributeCount()");
			c.close();
			return 0;
		}
		
		c.moveToFirst();
		int ret = c.getInt(c.getColumnIndexOrThrow("count"));
		c.close();
		return ret;
	}
	
	public long[] getTransactionIds(long accId) {
		int count = getTransactionCount(accId);
		if(count == 0)
			return new long[] { };
		
		long[] table = new long[count];
		Cursor c = getReadableDatabase().query(TRANSACTIONS_TABLE_NAME, new String[] { "_id" }, "accId=?", new String[] { Long.toString(accId) }, null, null, "timestamp desc", null);
		if(c.getCount() != count) {
			reporter.reportError(String.format("idcount: %d, Cursor count: %d", count, c.getCount()));
			c.close();
			return new long[] { };
		}
		
		int pos = 0;
		
		while(! c.isLast()) {
			c.moveToNext();
			table[pos++] = c.getLong(0);
		}
		
		c.close();
		return table;
	}
	
	public long[] getAttributeIds(long tid) {
		int count = getAttributeCount(tid);
		if(count == 0)
			return new long[] { };
		
		long[] table = new long[count];
		Cursor c = getReadableDatabase().query(ATTRIBUTES_TABLE_NAME, new String[] { "_id" }, "tid=?", new String[] { Long.toString(tid) }, null, null, null, null);
		if(c.getCount() != count) {
			reporter.reportError(String.format("idcount: %d, Cursor count: %d", count, c.getCount()));
			c.close();
			return new long[] { };
		}
		
		int pos = 0;
		
		while(! c.isLast()) {
			c.moveToNext();
			table[pos++] = c.getLong(0);
		}
		
		c.close();
		return table;
	}
	
	public AccountDao getAccountByIndex(int idx) {
		Cursor c = getReadableDatabase().query(ACCOUNTS_TABLE_NAME, AccountDao.getColumnList(), "_id=?", new String[] { Integer.toString(idx) }, null, null, null, "1");
		if(c.getCount() != 1) {
			c.close();
			return null;
		}
		
		c.moveToFirst();
		return AccountDao.newFromCursor(c);
	}
	
	public TransactionDao getTransactionById(long id) {
		Cursor c = getReadableDatabase().query(TRANSACTIONS_TABLE_NAME, TransactionDao.getColumnList(), "_id=?", new String[] { Long.toString(id) }, null, null, null, "1");
		if(c.getCount() != 1) {
			c.close();
			return null;
		}
		
		c.moveToFirst();
		return TransactionDao.newFromCursor(c);
	}
	
	public AttributeDao getAttributeById(long id) {
		Cursor c = getReadableDatabase().query(ATTRIBUTES_TABLE_NAME, AttributeDao.getColumnList(), "_id=?", new String[] { Long.toString(id) }, null, null, null, "1");
		if(c.getCount() != 1) {
			c.close();
			return null;
		}
		
		c.moveToFirst();
		return AttributeDao.newFromCursor(c);
	}
	
	public String getTitleCandidateForTransactionId(long id) {
		Cursor c = getReadableDatabase().query(ATTRIBUTES_TABLE_NAME, new String[] { "title" }, "tId=? and type=1 and aux2='title'", new String[] { Long.toString(id) }, null, null, null, "1");
		if(c.getCount() < 1) {
			c.close();
			return null;
		}
		
		c.moveToFirst();
		String str = c.getString(c.getColumnIndexOrThrow("title"));
		c.close();
		return str;
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
	
	public boolean insertTransaction(TransactionDao dao) {
		SQLiteDatabase db = getWritableDatabase();
		
		try {
			ContentValues values = dao.toContentValues();
			long id = db.insert(TRANSACTIONS_TABLE_NAME, null, values);
			dao.setId(id);
			return true;
		} catch(SQLException e) {
			reportError(":( -- sql#1");
			return false;
		}
	}
	
	public boolean insertAttribute(AttributeDao dao) {
		SQLiteDatabase db = getWritableDatabase();
		
		try {
			ContentValues values = dao.toContentValues();
			long id = db.insert(ATTRIBUTES_TABLE_NAME, null, values);
			dao.setId(id);
			return true;
		} catch(SQLException e) {
			reportError(":( -- sql#1");
			return false;
		}
	}
	
	public boolean updateTransaction(TransactionDao dao) {
		if(dao.getId() == 0) {
			reportError("can't update Transaction row, becase it doesn't have an ID yet");
			return false;
		}
		
		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = dao.toContentValues();
		
		try {
			db.update(TRANSACTIONS_TABLE_NAME, values, "_id=?", new String[] { String.valueOf(dao.getId()) });
			return true;
		} catch(SQLException e) {
			reportError(":( -- sql#U");
			return false;
		}
	}
	
	public boolean updateAttribute(AttributeDao dao) {
		if(dao.getId() == 0) {
			reportError("can't update Attribute row, because it doesn't have an ID yet");
			return false;
		}
		
		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = dao.toContentValues();
		
		try {
			db.update(ATTRIBUTES_TABLE_NAME, values, "_id=?", new String[] { String.valueOf(dao.getId()) });
			return true;
		} catch(SQLException e) {
			reportError(":( -- sql#U");
			return false;
		}
	}
	
	public boolean removeAttributesForTid(long tid) {
		SQLiteDatabase dbw = getWritableDatabase();
		
		try {
			dbw.delete(ATTRIBUTES_TABLE_NAME, "tid=?", new String[] { Long.toString(tid) } );
			return true;
		} catch(SQLException e) {
			reportError(":( -- sql#R");
			return false;
		}
	}
	
	public boolean clearTextMarksForTid(long tid) {
		SQLiteDatabase dbw = getWritableDatabase();
		
		try {
			ContentValues cv = new ContentValues();
			cv.put("aux2", "");
			dbw.update(ATTRIBUTES_TABLE_NAME, cv, "tid=?", new String[] { Long.toString(tid) } );
			return true;
		} catch(SQLException e) {
			reportError(":( -- sql#CT");
			return false;
		}
	}
	
	public boolean removeTransaction(long tid) {
		removeAttributesForTid(tid);
		SQLiteDatabase dbw = getWritableDatabase();
		
		try {
			dbw.delete(TRANSACTIONS_TABLE_NAME, "_id=?", new String[] { Long.toString(tid) } );
			return true;
		} catch(SQLException e) {
			reportError(":( -- sql#RT");
			return false;
		}
	}
	
	public boolean writeTransaction(TransactionDao dao) {
		if(dao.getId() == 0) {
			return insertTransaction(dao);
		} else {
			return updateTransaction(dao);
		}
	}
	
	public boolean writeAttribute(AttributeDao dao) {
		if(dao.getId() == 0)
			return insertAttribute(dao);
		else
			return updateAttribute(dao);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int v1, int v2) {
		reporter.reportInfo("Aktualizowanie tabel SQL...");
		
		// Oh yes!
		db.execSQL(String.format("drop table %s", ACCOUNTS_TABLE_NAME));
		db.execSQL(String.format("drop table %s", TRANSACTIONS_TABLE_NAME));
		db.execSQL(String.format("drop table %s", ATTRIBUTES_TABLE_NAME));
	}
	
	void reportError(String err) {
		reporter.reportError(err);
	}
}
