package pl.edu.uj.portfel.microphone;

import java.io.File;
import java.io.IOException;

import android.media.MediaRecorder;
import android.os.Environment;

public class AudioRecorder {
	private MediaRecorder recorder = new MediaRecorder();
	private String path;

	public AudioRecorder(String path) {
		this.path = sanitizePath("/portfel/" + path);
	}

	private String sanitizePath(String path) {
		if (!path.startsWith("/")) {
			path = "/" + path;
		}
		
		if (!path.contains(".")) {
			path += ".3gp";
		}
		
		return Environment.getExternalStorageDirectory().getAbsolutePath() + path;
	}
	
	public String getFilename() {
		return path;
	}
	
	public void start() throws IOException {
		String state = Environment.getExternalStorageState();
		if(! state.equals(Environment.MEDIA_MOUNTED)) {
			throw new IOException("Karta SD nie jest zamontowana");
		}
		
		File directory = new File(path).getParentFile();
		directory.mkdirs();
		
		if(! directory.exists()) {
			throw new IOException("Bledna nazwa pliku");
		}
		
		recorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
		recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
		recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
		recorder.setOutputFile(path);
		recorder.prepare();
		recorder.start();
	}
	
	public void stop() throws IOException {
		recorder.stop();
		recorder.reset();
		recorder.release();
	}
}
