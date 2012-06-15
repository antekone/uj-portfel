package pl.edu.uj.portfel.transaction.image;

import pl.edu.uj.portfel.transaction.TransactionAttribute;
import pl.edu.uj.portfel.transaction.TransactionInputActivity.TransactionType;
import android.view.View;
import android.widget.TextView;

public class ImageTransactionAttribute extends TransactionAttribute {
	private String filename;
	
	public ImageTransactionAttribute(String filename) {
		super(TransactionAttribute.Type.PICTURE);
		
		setFilename(filename);
	}
	
	@Override
	public void renderCaption(TextView v) {
		v.setText("Zdjecie");
	}

	@Override
	public void renderDescription(TextView v) {
		v.setText(filename);
	}

	@Override
	public void renderLabel(View v) {
		
	}
	
	public String getFilename() { return filename; }
	public void setFilename(String s) { filename = s; }
}
