package pl.edu.uj.portfel.transaction;

import android.view.View;
import android.widget.TextView;

public abstract class TransactionAttribute {
	private Type type;
	public enum Type {
		TEXT, PICTURE, SOUND, VIDEO
	}
	
	protected TransactionAttribute(Type t) {
		setType(t);
	}
	
	protected void setType(Type t) { type = t; }
	public Type getType() { return type; }
	
	public abstract void renderCaption(TextView v);
	public abstract void renderDescription(TextView v);
	public abstract void renderLabel(View v);
}
