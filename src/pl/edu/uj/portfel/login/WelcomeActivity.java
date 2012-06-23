package pl.edu.uj.portfel.login;

import java.util.ArrayList;

import pl.edu.uj.portfel.ErrorReporter;
import pl.edu.uj.portfel.R;
import pl.edu.uj.portfel.WPAsyncTask;
import pl.edu.uj.portfel.db.AccountDao;
import pl.edu.uj.portfel.db.Database;
import pl.edu.uj.portfel.server.AccountList;
import pl.edu.uj.portfel.server.LoginUserReturnValues;
import pl.edu.uj.portfel.server.Server;
import pl.edu.uj.portfel.settings.NewUserActivity;
import pl.edu.uj.portfel.settings.SettingsActivity;
import pl.edu.uj.portfel.transaction.TransactionInputActivity;
import pl.edu.uj.portfel.transaction.TransactionListActivity;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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
	private Server server;
	private String serverToken;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        db = new Database(this, this);
        server = new Server();
        
        login();
        
        xlist = new ArrayList<LoginEntryChooser>();
        ListView list = (ListView) findViewById(R.id.listView1);

        listAdapter = new LoginUserInfoAdapter(this, R.layout.login_layout, xlist);

        fillAccountList(db, xlist);
        list.setAdapter(listAdapter);
        list.setOnItemClickListener(this);
    }
    
    private void showErrorBox(String caption, String msg) {
        AlertDialog progress = new AlertDialog.Builder(this).create();
        progress.setTitle(caption);
        progress.setCancelable(true);
        progress.setMessage(msg);
        progress.show();
    }
    
    private void sync() {
    	final ProgressDialog progress = new ProgressDialog(this);
        progress.setTitle("Czekaj");
        progress.setCancelable(false);
        progress.setMessage("Synchronizacja...");
        progress.show();
        
        WPAsyncTask syncTask = new WPAsyncTask(this, server) {
        	@Override
        	public void onFinished() {
        		runOnUiThread(new Runnable() {
        			public void run() {
                		progress.dismiss();
        				onWindowFocusChanged(true);
        			}
        		});
        	}
        	
        	@Override
        	public void run() {
        		AccountList acList = server.getAccountList(serverToken);
        		
            	for(AccountDao remoteDao: acList.getAccounts()) {
            		boolean exists = false;
            		long[] ids = db.getAccountIds();
            		
            		for(long id: ids) {
	            		AccountDao dao = db.getAccountById(id);
	            		
	            		if(dao.getRemoteId() == remoteDao.getRemoteId()) {
	            			if(! dao.getName().equals(remoteDao.getName())) {
	            				dao.name = remoteDao.getName();
	            				db.updateAccount(dao);
	            			}
	            			
	            			exists = true;
	            			break;
	            		}
	            	}
            		
            		if(! exists) {
            			db.writeAccount(remoteDao);
            		}
            	}
            	
            	onFinished();
        	}
        };
        
        new Thread(syncTask).start();
    }
    
    private void login() {
        final ProgressDialog progress = new ProgressDialog(this);
        progress.setTitle("Czekaj");
        progress.setCancelable(false);
        progress.setMessage("Logowanie...");
        progress.show();
        
        WPAsyncTask loginTask = new WPAsyncTask(this, server) {
			@Override
			public void onFinished() {
				runOnUiThread(new Runnable() {
					public void run() {
						progress.dismiss();
						sync();
					}
				});
			}

			@Override
			public void run() {
				SharedPreferences sp = getSharedPreferences(NewUserActivity.PREFS_NAME, 0);
				if (sp != null) {
					try {
						String username = sp.getString("username", "");
						String password = sp.getString("password", "");
						
						LoginUserReturnValues rv = server.loginUser(username, password);
						
						if(rv.isAnswerMalformed()) {
							runOnUiThread(new Runnable() {
								public void run() { showErrorBox("Blad", "Bledna odpowiedz od serwera!"); }
							});
						}
						
						if(rv.isLoginOK()) {
							serverToken = rv.getToken();
						} else {
							runOnUiThread(new Runnable() {
								public void run() { showErrorBox("Blad", "Logowanie nieudane!"); }
							});
						}
					} catch (Exception e) {
						
					}
					
					onFinished();
				}
			}
        };
        
        new Thread(loginTask).start();
    }
    
    public void onWindowFocusChanged(boolean hasFocus) {
    	super.onWindowFocusChanged(hasFocus);
    	
    	fillAccountList(db, xlist);
    	listAdapter.notifyDataSetChanged();
    }
    
    private void fillAccountList(Database db, ArrayList<LoginEntryChooser> xlist) {
//    	int len = db.getAccountCount();
//    	
//    	xlist.clear();
//    	for(int i = 1; i <= len; i++) {
//    		AccountDao dao = db.getAccountByIndex(i);
//    		xlist.add(new LoginEntryChooser(dao.getId(), dao.getName()));
//    	}
    	
    	xlist.clear();
    	long[] ids = db.getAccountIds();
    	for(long id: ids) {
    		AccountDao dao = db.getAccountById(id);
    		xlist.add(new LoginEntryChooser(dao.getId(), dao.getRemoteId(), dao.getName()));
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
    	Intent args;
    	
    	switch(item.getItemId()) {
    	case R.id.menu_login_about:
    		Toast.makeText(this, "wersja 0.00002", Toast.LENGTH_SHORT).show();
    		break;
    	case R.id.menu_login_settings:
    		args = new Intent(this, SettingsActivity.class);
    		args.putExtra("SERVER_TOKEN", serverToken);
    		startActivity(args);
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
		map.putExtra("SERVER_TOKEN", serverToken);
		map.putExtra("REMOTE_ACCID", entry.getRemoteAccountId());
		
		startActivity(map); 
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(data == null)
			return;
		
		long num = data.getLongExtra("OUTPUT", 0);
		
		Intent transactionIntent = new Intent(this, TransactionInputActivity.class);
		transactionIntent.putExtra("CASH", num);
		transactionIntent.putExtra("SERVER_TOKEN", serverToken);
		
		startActivity(transactionIntent);
	}
}
