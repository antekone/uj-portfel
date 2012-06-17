package pl.edu.uj.portfel.camera;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import pl.edu.uj.portfel.R;
import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;

public class CameraPhotoActivity extends Activity {
	CameraPreview preview;
	Button buttonClick;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.camerademo);

		preview = new CameraPreview(this);
		((FrameLayout) findViewById(R.id.preview)).addView(preview);

		buttonClick = (Button) findViewById(R.id.buttonClick);
		buttonClick.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				try {
					preview.camera.takePicture(shutterCallback, rawCallback, jpegCallback);
				} catch(RuntimeException e) { 
					e.printStackTrace();
				}
			}
		});
	}
	
	@Override
	public void onBackPressed() {
		finish();
	}
	
	@Override
	public void onPause() {
		super.onPause();
	}
	
	@Override
	public void onResume() {
		super.onResume();
	}

	ShutterCallback shutterCallback = new ShutterCallback() {
		public void onShutter() {
			// shutter
		}
	};

	PictureCallback rawCallback = new PictureCallback() {
		public void onPictureTaken(byte[] data, Camera camera) {
			// picture taken
		}
	};
	
	PictureCallback jpegCallback = new PictureCallback() {
		public void onPictureTaken(byte[] data, Camera camera) {
			FileOutputStream outStream = null;
			try {
				String dir = String.format("%s/portfel", Environment.getExternalStorageDirectory().getAbsolutePath());
				String outputFilename = String.format("%s/%d.jpg", dir, System.currentTimeMillis());
				
				File fdir = new File(dir);
				File ffile = new File(outputFilename);
				
				fdir.mkdirs();
				ffile.createNewFile();
				
				outStream = new FileOutputStream(ffile);
				outStream.write(data);
				outStream.close();
				
				Intent result = new Intent();
				result.putExtra("FILENAME", outputFilename);
				setResult(RESULT_OK, result);
				
				finish();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
			}
		}
	};
}