package pl.edu.uj.portfel.settings;

import pl.edu.uj.portfel.ErrorReporter;
import pl.edu.uj.portfel.R;
import pl.edu.uj.portfel.db.Database;
import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class NewAccountActivity extends Activity implements ErrorReporter {
	private Database db;
	
	@Override
	public void onCreate(Bundle savedState) {
		super.onCreate(savedState);
		setContentView(R.layout.new_account);
		
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
			finish();
		}
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
