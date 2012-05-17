package pl.edu.uj.portfel.transaction.attributes.text;

import android.view.View;
import android.widget.TextView;
import pl.edu.uj.portfel.transaction.TransactionAttribute;
import pl.edu.uj.portfel.transaction.TransactionInputActivity.TransactionType;

public class TextTransactionAttribute extends TransactionAttribute {
	private String descr;
	private String caption;
	
	public TextTransactionAttribute(String caption, String descr) {
		super(TransactionAttribute.Type.TEXT);
		
		setCaption(caption);
		setDescription(descr);
	}
	
	public String getDescription() {
		return descr;
	}
	
	public String getCaption() {
		return caption;
	}
	
	public void setCaption(String s) {
		caption = s;
	}
	
	public void setDescription(String text) {
		descr = text;
	}

	@Override
	public void renderDescription(TextView v) {
		v.setText(descr);
	}
	
	@Override
	public void renderCaption(TextView v) {
		v.setText(caption);
	}

	@Override
	public void renderLabel(View v) {
		
	}
}
