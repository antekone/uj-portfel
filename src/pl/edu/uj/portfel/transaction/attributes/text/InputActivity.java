package pl.edu.uj.portfel.transaction.attributes.text;

import pl.edu.uj.portfel.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class InputActivity extends Activity {
	public void onCreate(Bundle b) {
		super.onCreate(b);
		setContentView(R.layout.textattr_entry);
	}
	
	public void cancel(View v) {
		
		Intent result = new Intent();
		setResult(RESULT_CANCELED, result);

		finish();
	}
	
	public void accept(View v) {
		TextView caption = (TextView) findViewById(R.id.textAttributeEditCaption);
		TextView description = (TextView) findViewById(R.id.textAttributeEditDescription);
		
		Intent map = new Intent();
		map.putExtra("CAPTION", caption.getText());
		map.putExtra("DESCRIPTION", description.getText());
		setResult(RESULT_OK, map);
		
		finish();
	}
}
