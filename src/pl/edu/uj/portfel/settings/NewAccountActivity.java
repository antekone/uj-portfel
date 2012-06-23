package pl.edu.uj.portfel.settings;

import pl.edu.uj.portfel.ErrorReporter;
import pl.edu.uj.portfel.R;
import pl.edu.uj.portfel.WPAsyncTask;
import pl.edu.uj.portfel.db.AccountDao;
import pl.edu.uj.portfel.db.Database;
import pl.edu.uj.portfel.server.Server;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class NewAccountActivity extends Activity implements ErrorReporter {
	private Database db;
	private Server server;
	private String serverToken;
	
	@Override
	public void onCreate(Bundle savedState) {
		super.onCreate(savedState);
		setContentView(R.layout.new_account);
		
		serverToken = getIntent().getExtras().getString("SERVER_TOKEN");
		
		server = new Server();
		db = new Database(this, this);
	}
	
	public void cancelCreation(View v) {
		finish();
	}
	
	public void applyCreation(View v) {
		EditText aname = (EditText) findViewById(R.id.account_name);
		EditText abalance = (EditText) findViewById(R.id.account_initial_balance);
		Spinner aspinner = (Spinner) findViewById(R.id.account_type);
		
		String accountName = aname.getText().toString();
		double initialBalance = .0;
		
		try {
			initialBalance = Double.parseDouble(abalance.getText().toString());
		} catch(NumberFormatException e) {
			initialBalance = .0;
		}
		
		int type = aspinner.getSelectedItemPosition();
		if(db.findAccountByName(accountName) != null) {
			reportError(R.string.account_already_exists);
		} else {
			db.writeAccount(accountName, type, initialBalance);
			Toast.makeText(this, R.string.account_created, Toast.LENGTH_SHORT).show();
			
			sync(accountName, type, initialBalance);			
		}
	}
	
	public void sync(String name, int type, double initialBalance) {
        final ProgressDialog progress = new ProgressDialog(this);
        progress.setTitle("Czekaj");
        progress.setCancelable(false);
        progress.setMessage("Zapisywanie...");
        progress.show();
		
        final String n = name;
        final int t = type;
        final double ib = initialBalance;
        
		WPAsyncTask syncTask = new WPAsyncTask(this, server) {

			@Override
			public void onFinished() {
				progress.dismiss();
				
				runOnUiThread(new Runnable() {
					public void run() {
						finish();
					}
				});
			}

			@Override
			public void run() {
				long rid = server.createAccount(serverToken, n, t, ib);
				
				AccountDao dao = db.findAccountByName(n);
				dao.rid = rid;
				db.updateAccount(dao);
				
				onFinished();
			}
			
		};
		
		new Thread(syncTask).start();
	}
	
	void reportError(int strid) {
		Toast.makeText(this, strid, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void reportError(String str) {
		Toast.makeText(this, str, Toast.LENGTH_SHORT);
	}

	@Override
	public void reportInfo(String str) {
		Toast.makeText(this, str, Toast.LENGTH_SHORT);
	}
}
