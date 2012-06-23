package pl.edu.uj.portfel.settings;

import pl.edu.uj.portfel.ErrorReporter;
import pl.edu.uj.portfel.R;
import pl.edu.uj.portfel.WPAsyncTask;
import pl.edu.uj.portfel.server.CreateUserReturnValues;
import pl.edu.uj.portfel.server.Server;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class NewUserActivity extends Activity implements ErrorReporter {
	public final static String PREFS_NAME = "wplogin";
	private Server server;

	private class ViewHolder {
		public EditText username;
		public EditText password;
	}

	ViewHolder holder;

	public void onCreate(Bundle savedState) {
		super.onCreate(savedState);
		setContentView(R.layout.new_user);

		server = new Server();
		
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

		createOnServer();
	}
	
    private void showErrorBox(String caption, String msg) {
        AlertDialog progress = new AlertDialog.Builder(this).create();
        progress.setTitle(caption);
        progress.setCancelable(true);
        progress.setMessage(msg);
        progress.show();
    }
    
    private void createOnServer() {
        final ProgressDialog progress = new ProgressDialog(this);
        progress.setTitle("Czekaj");
        progress.setCancelable(false);
        progress.setMessage("Tworzenie uzytkownika...");
        progress.show();
        
		TelephonyManager tMgr =(TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
		final String myNumber = tMgr.getLine1Number();
       
        WPAsyncTask createTask = new WPAsyncTask(this, server) {
			@Override
			public void onFinished() {
				progress.dismiss();
				finish();
			}

			@Override
			public void run() {
				SharedPreferences sp = getSharedPreferences(NewUserActivity.PREFS_NAME, 0);
				if (sp != null) {
					try {
						String username = sp.getString("username", "");
						String password = sp.getString("password", "");
						
						CreateUserReturnValues rv = server.createUser(username, myNumber, password);
						
						if(rv.isMalformed()) {
							runOnUiThread(new Runnable() {
								public void run() { showErrorBox("Blad", "Bledna odpowiedz od serwera!"); }
							});
						}
						
						if(rv.isCreationOK()) {
//							serverToken = rv.getToken();
							
							class LocalRunnable implements Runnable {
								public String t;
								public int i;
								
								public void run() { showErrorBox("Blad", String.format("token: %s, id: %d", t, i)); }
							}
							
							LocalRunnable obj = new LocalRunnable();
							
							obj.t = rv.getToken();
							obj.i = rv.getId();
							
							runOnUiThread(obj);
						} else {
							runOnUiThread(new Runnable() {
								public void run() { showErrorBox("Blad", "Tworzenie uzytkownika nieudane!"); }
							});
						}
					} catch (Exception e) {
						
					}
					
					onFinished();
				}
			}
        };
        
        new Thread(createTask).start();
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
