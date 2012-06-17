package pl.edu.uj.portfel.microphone;

import java.io.IOException;

import pl.edu.uj.portfel.R;
import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.ToggleButton;

public class AudioPlayerActivity extends Activity implements OnCompletionListener {
	private String filename;
	private MediaPlayer mp;
	
	private ToggleButton playButton;
	private ProgressBar progress;
	private Handler handler = new Handler();
	
	private void updateProgress() {
		if(mp != null && mp.isPlaying()) {
			Runnable notification = new Runnable() {
				public void run() {
					updateProgress();
				}
			};
			
			progress.setProgress(mp.getCurrentPosition());
			handler.postDelayed(notification, 250);
		}
	}
	
	public void onCreate(Bundle state) {
		super.onCreate(state);
		setContentView(R.layout.audio_player);
		
		Bundle extras = getIntent().getExtras();
		filename = extras.getString("FILENAME");
		
		playButton = (ToggleButton) findViewById(R.id.audio_player_play_button);
		progress = (ProgressBar) findViewById(R.id.audio_player_progress);
	}
	
	public void playButtonClicked(View w) {
		if(playButton.isChecked()) {
			try {
				mp = new MediaPlayer();
				mp.setOnCompletionListener(this);
				
				mp.setDataSource(filename);
				mp.prepare();
			} catch(IOException e) {
				Toast.makeText(this, "Problem z odtwarzaniem dzwieku", Toast.LENGTH_SHORT).show();
				setResult(RESULT_CANCELED);
				finish();
			}
			
			progress.setMax(mp.getDuration());
			progress.setProgress(0);
			
			mp.start();
			updateProgress();
		} else {
			mp.stop();
		}
	}
	
	@Override
	public void onCompletion(MediaPlayer mp) {
		playButton.setChecked(false);
		progress.setProgress(0);
	}
}
