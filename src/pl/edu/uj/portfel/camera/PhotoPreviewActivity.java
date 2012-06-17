package pl.edu.uj.portfel.camera;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import pl.edu.uj.portfel.R;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

public class PhotoPreviewActivity extends Activity {
	class ViewHolder {
		ImageView imageView;
	}
	
	ViewHolder holder;
	Bitmap loadedBitmap;
	
	public PhotoPreviewActivity() {
		super();
		holder = new ViewHolder();
	}
	
	public void onCreate(Bundle state) {
		super.onCreate(state);
		setContentView(R.layout.image_preview);
		
		Bundle extras = getIntent().getExtras();
		String requestedFilename = extras.getCharSequence("FILENAME").toString();
		
		holder.imageView = (ImageView) findViewById(R.id.image_preview);
		
		try {
			InputStream stream = new FileInputStream(requestedFilename);
			loadedBitmap = BitmapFactory.decodeStream(stream);
			holder.imageView.setImageBitmap(loadedBitmap);
		} catch(IOException e) {
			Toast.makeText(this, "Ladowanie obrazu nie powiodlo sie! - " + requestedFilename, Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
	}
}
