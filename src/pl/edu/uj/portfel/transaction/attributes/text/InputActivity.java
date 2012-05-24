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
		
		Bundle extras = getIntent().getExtras();
		if(extras != null) {
			String caption = extras.getString("INITIAL_CAPTION");
			String description = extras.getString("INITIAL_DESCRIPTION");
			
			if(caption != null && caption.length() > 0) {
				TextView captionObj = (TextView) findViewById(R.id.textAttributeEditCaption);
				captionObj.setText(caption);
			}
			
			if(description != null && description.length() > 0) {
				TextView descriptionObj = (TextView) findViewById(R.id.textAttributeEditDescription);
				descriptionObj.setText(description);
			}
		}
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
