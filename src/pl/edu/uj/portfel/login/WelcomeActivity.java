package pl.edu.uj.portfel.login;

import java.util.ArrayList;

import pl.edu.uj.portfel.ErrorReporter;
import pl.edu.uj.portfel.R;
import pl.edu.uj.portfel.db.AccountDao;
import pl.edu.uj.portfel.db.Database;
import pl.edu.uj.portfel.settings.SettingsActivity;
import pl.edu.uj.portfel.transaction.TransactionInputActivity;
import pl.edu.uj.portfel.transaction.TransactionListActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class WelcomeActivity extends Activity implements ErrorReporter, OnItemClickListener {
	private Database db;
	private ArrayList<LoginEntryChooser> xlist;
	private LoginUserInfoAdapter listAdapter;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        db = new Database(this, this);
        xlist = new ArrayList<LoginEntryChooser>();
        ListView list = (ListView) findViewById(R.id.listView1);

        listAdapter = new LoginUserInfoAdapter(this, R.layout.login_layout, xlist);
        
        fillAccountList(db, xlist);
        list.setAdapter(listAdapter);
        list.setOnItemClickListener(this);
    }
    
    public void onWindowFocusChanged(boolean hasFocus) {
    	super.onWindowFocusChanged(hasFocus);
    	
    	fillAccountList(db, xlist);
    	listAdapter.notifyDataSetChanged();
    }
    
    private void fillAccountList(Database db, ArrayList<LoginEntryChooser> xlist) {
    	int len = db.getAccountCount();
    	
    	xlist.clear();
    	for(int i = 1; i <= len; i++) {
    		AccountDao dao = db.getAccountByIndex(i);
    		xlist.add(new LoginEntryChooser(dao.getId(), dao.getName()));
    	}
	}

	@Override
    public boolean onCreateOptionsMenu(Menu m) {
    	MenuInflater i = getMenuInflater();
    	i.inflate(R.menu.login_menu, m);
    	return true;
    }
    
    @Override 
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch(item.getItemId()) {
    	case R.id.menu_login_about:
    		Toast.makeText(this, "wersja 0.00002", Toast.LENGTH_SHORT).show();
    		break;
    	case R.id.menu_login_settings:
    		startActivity(new Intent(this, SettingsActivity.class));
    		break;
    	}
    	
    	return true;
    }

	@Override
	public void reportError(String str) {
		Toast.makeText(this, str, Toast.LENGTH_SHORT);
	}

	@Override
	public void reportInfo(String str) {
		Toast.makeText(this, str, Toast.LENGTH_SHORT);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Intent map = new Intent(this, TransactionListActivity.class);
		
		LoginEntryChooser entry = xlist.get(position);
		map.putExtra("ACCOUNT_NAME", entry.getEntryText());
		map.putExtra("ACCOUNT_ID", entry.getAccountId());
		
		startActivity(map); 
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(data == null)
			return;
		
		long num = data.getLongExtra("OUTPUT", 0);
		
		Intent transactionIntent = new Intent(this, TransactionInputActivity.class);
		transactionIntent.putExtra("CASH", num);
		startActivity(transactionIntent);
	}
}