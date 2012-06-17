package pl.edu.uj.portfel.microphone;

import java.io.IOException;

import pl.edu.uj.portfel.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import android.widget.ToggleButton;

public class AudioRecorderActivity extends Activity {
	public AudioRecorderActivity() {
		super();
	}
	
	ToggleButton mainButton;
	AudioRecorder recorder;
	
	public void onCreate(Bundle state) {
		super.onCreate(state);
		setContentView(R.layout.audio_recorder);
		
		mainButton = (ToggleButton) findViewById(R.id.audio_toggle_button);
		
		String fn = String.format("%d", System.currentTimeMillis());
		recorder = new AudioRecorder(fn);
	}
	
	public void toggleButton(View v) {
		boolean checked = mainButton.isChecked();
		
		if(checked) {
			try {
				recorder.start();
				Toast.makeText(this, "Nagrywanie...", Toast.LENGTH_SHORT).show();
			} catch(IOException e) {
				Toast.makeText(this, "Problem z nagrywaniem...", Toast.LENGTH_SHORT).show();
			}
		} else {
			try {
				recorder.stop();
				
				Intent i = new Intent();
				i.putExtra("FILENAME", recorder.getFilename());
				setResult(RESULT_OK, i);
				finish();
			} catch(IOException e) {
				Toast.makeText(this, "Problem z nagrywaniem...", Toast.LENGTH_SHORT).show();
			}
		}
	}
}
