package pl.edu.uj.portfel.db;

import pl.edu.uj.portfel.transaction.TransactionAttribute;
import pl.edu.uj.portfel.transaction.attributes.text.TextTransactionAttribute;
import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

public class AttributeDao {
	private long id;
	private long tid;
	private int type;
	private String title;
	private String aux1;
	private String aux2;
	private String aux3;
	
	enum AttributeType {
		TEXT, IMAGE, AUDIO, VIDEO
	}
	
	public static AttributeDao fromAttributeObj(long tid, TransactionAttribute attr) {
		switch(attr.getType()) {
		case TEXT: return fromTextAttributeObj(tid, (TextTransactionAttribute) attr);
		}
		
		throw new RuntimeException();
	}
	
	public static AttributeDao fromTextAttributeObj(long tid, TextTransactionAttribute attr) {
		AttributeDao dao = new AttributeDao();
		
		dao.setTid(tid);
		dao.setType(AttributeType.TEXT);
		dao.setTitle(attr.getCaption());
		dao.setAux1(attr.getDescription());
		
		return dao;
	}
	
	public TransactionAttribute createTransactionAttribute() {
		switch(getTypeObj()) {
		case TEXT:
			return createTextTransactionAttribute();
		}
		
		throw new RuntimeException();
	}
	
	public TextTransactionAttribute createTextTransactionAttribute() {
		TextTransactionAttribute attr = new TextTransactionAttribute(title, aux1);
		return attr;
	}
	
	public long getId() { return id; }
	public long getTid() { return tid; }
	public int getType() { return type; }
	
	public AttributeType getTypeObj() {
		if(type == 1)
			return AttributeType.TEXT;
		else if(type == 2)
			return AttributeType.IMAGE;
		else if(type == 3)
			return AttributeType.AUDIO;
		else if(type == 4)
			return AttributeType.VIDEO;
		else
			throw new RuntimeException();
	}
	
	public void setType(AttributeType type) {
		if(type == AttributeType.TEXT)
			setType(1);
		else if(type == AttributeType.IMAGE)
			setType(2);
		else if(type == AttributeType.AUDIO)
			setType(3);
		else if(type == AttributeType.VIDEO)
			setType(4);
		else
			throw new RuntimeException();
	}
	
	public String getTitle() { return title; }
	public String getAux1() { return aux1; }
	public String getAux2() { return aux2; }
	public String getAux3() { return aux3; }
	
	public void setId(long i) { id = i; }
	public void setTid(long i) { tid = i; }
	
	public void setType(int i) { type = i; }
	public void setTitle(String t) { title = t; }
	public void setAux1(String a) { aux1 = a; }
	public void setAux2(String a) { aux2 = a; }
	public void setAux3(String a) { aux3 = a; }
	
	public ContentValues toContentValues() {
		ContentValues values = new ContentValues();
		
		values.put("tid", tid);
		values.put("type", type);
		values.put("title", title);
		values.put("aux1", aux1);
		values.put("aux2", aux2);
		values.put("aux3", aux3);
		
		return values;
	}
	
	public static AttributeDao newFromCursor(Cursor c) {
		AttributeDao dao = new AttributeDao();
		
		dao.id = c.getLong(c.getColumnIndexOrThrow("_id"));
		dao.type = c.getInt(c.getColumnIndexOrThrow("type"));
		dao.tid = c.getLong(c.getColumnIndexOrThrow("tId"));
		dao.title = c.getString(c.getColumnIndexOrThrow("title"));
		dao.aux1 = c.getString(c.getColumnIndexOrThrow("aux1"));
		dao.aux2 = c.getString(c.getColumnIndexOrThrow("aux2"));
		dao.aux3 = c.getString(c.getColumnIndexOrThrow("aux3"));
		c.close();
		
		return dao;
	}
	
	public static String[] getColumnList() {
		return new String[] { "_id", "tId", "type", "title", "aux1", "aux2", "aux3" };
	}
}
