package pl.edu.uj.portfel.settings;

import pl.edu.uj.portfel.ErrorReporter;
import pl.edu.uj.portfel.R;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class NewUserActivity extends Activity implements ErrorReporter {
	public final String PREFS_NAME = "wplogin";

	private class ViewHolder {
		public EditText username;
		public EditText password;
	}

	ViewHolder holder;

	public void onCreate(Bundle savedState) {
		super.onCreate(savedState);
		setContentView(R.layout.new_user);

		holder = new ViewHolder();
		holder.username = (EditText) findViewById(R.id.newuser_email);
		holder.password = (EditText) findViewById(R.id.newuser_password);

		SharedPreferences sp = getSharedPreferences(PREFS_NAME, 0);
		if (sp != null) {
			try {
				String username = sp.getString("username", "");
				String password = sp.getString("password", "");

				holder.username.setText(username);
				holder.password.setText(password);
			} catch (Exception e) {
				Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
			}
		}
	}

	public void apply(View v) {
		SharedPreferences sp = getSharedPreferences(PREFS_NAME, 0);
		SharedPreferences.Editor e = sp.edit();

		e.putString("username", holder.username.getText().toString());
		e.putString("password", holder.password.getText().toString());
		e.commit();
		
		finish();
	}

	public void cancel(View v) {
		finish();
	}

	@Override
	public void reportError(String str) {
		Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void reportInfo(String str) {
		Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
	}
}
