package pl.edu.uj.portfel;

import java.io.IOException;

import pl.edu.uj.portfel.recorder.AudioRecorder;
import pl.edu.uj.portfel.utils.Currency;
import pl.edu.uj.portfel.utils.StringUtils;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class TransactionInputActivity extends Activity {
	AudioRecorder recorder;

	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		Currency currency = new Currency();

		setContentView(R.layout.transaction);

		long cash = getIntent().getExtras().getLong("CASH");

		TextView cashValue = (TextView) findViewById(R.id.cashValue);
		cashValue.setText(StringUtils.getCanonicalCashValue(cash, currency));
	}

	public void recordClicked(View v) {
		try {
			if (recorder == null) {
				recorder = new AudioRecorder("/w-portfel/test.3gp");
				recorder.start();
				
				Toast.makeText(this, "recording start", Toast.LENGTH_SHORT).show();
			} else {
				recorder.stop();
				Toast.makeText(this, "recording stop", Toast.LENGTH_SHORT).show();
			}
		} catch (IOException e) {
			try {
				if(recorder != null)
					recorder.stop();
			} catch(IOException ex) {
				
			}
		}
	}
}
