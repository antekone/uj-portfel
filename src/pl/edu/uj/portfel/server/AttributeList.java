package pl.edu.uj.portfel.server;

import java.util.ArrayList;
import java.util.List;

import pl.edu.uj.portfel.db.AttributeDao;

public class AttributeList {

	private List<AttributeDao> attributeList;
	boolean malformed;
	
	public AttributeList(boolean _malformed) {
		attributeList = new ArrayList<AttributeDao>();
		malformed = _malformed;
	}
	
	public boolean isMalformed() { return malformed; }
	public void setMalformed(boolean b) { malformed = b; }
	public List<AttributeDao> getAttributeList() { return attributeList; }
}
