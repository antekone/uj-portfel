package pl.edu.uj.portfel.transaction.attributes.audio;

import android.os.Environment;
import android.view.View;
import android.widget.TextView;
import pl.edu.uj.portfel.transaction.TransactionAttribute;

public class AudioTransactionAttribute extends TransactionAttribute {
	private String filename;
	
	public AudioTransactionAttribute(String filename) {
		super(TransactionAttribute.Type.SOUND);
		
		setFilename(filename);
	}
	
	public void setFilename(String s) {
		filename = s;
	}
	
	public String getFilename() {
		return filename;
	}
	
	@Override
	public void renderCaption(TextView v) {
		v.setText("Dzwiek");
	}

	@Override
	public void renderDescription(TextView v) {
		String dir = String.format("%s/portfel/", Environment.getExternalStorageDirectory().getAbsolutePath());
		String shortName = filename.replace(dir, "");
		v.setText(shortName);
	}

	@Override
	public void renderLabel(View v) {
		
	}
}
