package pl.edu.uj.portfel.transaction;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import pl.edu.uj.portfel.ErrorReporter;
import pl.edu.uj.portfel.NumberInputActivity;
import pl.edu.uj.portfel.R;
import pl.edu.uj.portfel.WPAsyncTask;
import pl.edu.uj.portfel.db.AttributeDao;
import pl.edu.uj.portfel.db.Database;
import pl.edu.uj.portfel.db.TransactionDao;
import pl.edu.uj.portfel.server.AttributeList;
import pl.edu.uj.portfel.server.Server;
import pl.edu.uj.portfel.server.TransactionList;
import pl.edu.uj.portfel.utils.ChoiceActivatedClosure;
import pl.edu.uj.portfel.utils.ChoiceList;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class TransactionListActivity extends Activity implements ErrorReporter, OnItemClickListener, OnItemLongClickListener {
	class ViewHolder {
		TextView transactionListCaption;
	}
	
	private ViewHolder holder;
	private List<TransactionListItem> items;
	private Database db;
	private TransactionListViewAdapter listAdapter;
	private long accId;
	private long remoteAccId;
	
	private Server server;
	private String serverToken;
	
	public void onCreate(Bundle b) {
		super.onCreate(b);
		setContentView(R.layout.transaction_list);
		
		server = new Server();
		serverToken = getIntent().getExtras().getString("SERVER_TOKEN");
		remoteAccId = getIntent().getExtras().getLong("REMOTE_ACCID");

		holder = new ViewHolder();
		holder.transactionListCaption = (TextView) findViewById(R.id.transactionListCaption);

		items = new ArrayList<TransactionListItem>();
		db = new Database(this, this);
		initActivity();
		
		ListView list = (ListView) findViewById(R.id.transactionList);
		listAdapter = new TransactionListViewAdapter(this, R.layout.transaction_list_item, items);
		list.setAdapter(listAdapter);
		list.setOnItemClickListener(this);
		list.setOnItemLongClickListener(this);
		
		sync();
	}
	
	public void syncTransaction(TransactionDao local, TransactionDao remote) {
		if(local.getAmount() != remote.getAmount())
			local.setAmount(remote.getAmount());
	}
	
	public void sync() {
        final ProgressDialog progress = new ProgressDialog(this);
        progress.setTitle("Czekaj");
        progress.setCancelable(false);
        progress.setMessage("Synchronizacja...");
        progress.show();
		
		WPAsyncTask syncTask = new WPAsyncTask(this, server) {

			@Override
			public void onFinished() {
				runOnUiThread(new Runnable() { public void run() { progress.dismiss(); refreshTransactionList(); } });
			}

			@Override
			public void run() {
				TransactionList tnList = server.getTransactionList(serverToken, accId, remoteAccId);
				
				for(TransactionDao dao: tnList.getTransactionList()) {
					long[] ids = db.getTransactionIds(accId);
					boolean exists = false;
					
					for(long id: ids) {
						TransactionDao localDao = db.getTransactionById(id);
						
						if(localDao.getRid() == dao.getRid()) {
							syncTransaction(localDao, dao);
							exists = true;
							break;
						}
					}
					
					if(! exists) {
						db.writeTransaction(dao);
						
						AttributeList attrList = server.getAttributeList(serverToken, dao.getId(), dao.getRid());
						for(AttributeDao adao: attrList.getAttributeList()) {
							db.writeAttribute(adao);
						}
					}
				}
				
				onFinished();
			}
		};
		
		new Thread(syncTask).start();
	}
	
	public String getTransactionTitle(TransactionDao dao) {
		String candidateTitle = db.getTitleCandidateForTransactionId(dao.getId());
		if(candidateTitle == null || candidateTitle.length() == 0) {
			return getString(R.string.transaction_noname);
		} else
			return candidateTitle;
	}
	
	void loadTransactionList(long accountId) {
		long[] ids = db.getTransactionIds(accountId);
		items.clear();
		
		for(long tid: ids) {
			TransactionDao dao = db.getTransactionById(tid);
			
			TransactionListItem listItem = new TransactionListItem();
			listItem.setId(dao.getId());
			listItem.setAmount(dao.getAmount());
			listItem.setDate(getDateFromTransactionDao(dao));
			listItem.setName(getTransactionTitle(dao));
			items.add(listItem);
		}
	}
	
	private String getDateFromTransactionDao(TransactionDao dao) {
		long ts = dao.getTimestamp();
		
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(new Date(ts));

		return String.format("%02d %02d %d", gc.get(Calendar.DAY_OF_MONTH), 1 + gc.get(Calendar.MONTH), gc.get(Calendar.YEAR));
	}
	
	private void initActivity() {
		Bundle b = getIntent().getExtras();
	
		String accName = b.getString("ACCOUNT_NAME");
		accId = b.getLong("ACCOUNT_ID");
		
		loadTransactionList(accId);
		
		holder.transactionListCaption.setText(String.format("Konto: %s, Id: %d", accName, accId));
	}

	@Override
	public void reportError(String str) {
		Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void reportInfo(String str) {
		Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int idx, long arg3) {
		TransactionListItem transaction = items.get(idx);
		
		long tid = transaction.getId();
		long cash = transaction.getAmount();
		
		Intent transactionIntent = new Intent(this, TransactionInputActivity.class);
		transactionIntent.putExtra("LOAD_TRANSACTION_ID", tid);
		transactionIntent.putExtra("CASH", cash);
		transactionIntent.putExtra("ACCOUNT_ID", accId);
		transactionIntent.putExtra("SERVER_TOKEN", serverToken);
		startActivityForResult(transactionIntent, 2);
	}
	
	public void addTransaction(View v) {
		Intent transactionIntent = new Intent(this, NumberInputActivity.class);
		startActivityForResult(transactionIntent, 0);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		items.clear();
		loadTransactionList(accId);
		listAdapter = new TransactionListViewAdapter(this, R.layout.transaction_list_item, items);
		ListView list = (ListView) findViewById(R.id.transactionList);
		list.setAdapter(listAdapter);

		if(data == null)
			return;
		
		if(requestCode == 0) {
			long num = data.getLongExtra("OUTPUT", 0);
			
			Intent transactionIntent = new Intent(this, TransactionInputActivity.class);
			transactionIntent.putExtra("CASH", num);
			transactionIntent.putExtra("ACCOUNT_ID", accId);
			transactionIntent.putExtra("SERVER_TOKEN", serverToken);
			startActivityForResult(transactionIntent, 1);
		}
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int idx, long arg3) {
		ChoiceList list = new ChoiceList(this, getString(R.string.select_action));
		
		final AdapterView<?> a = arg0;
		final View b = arg1;
		final int c = idx;
		final long d = arg3;
		
		list.add(getString(R.string.transaction_edit), new ChoiceActivatedClosure() {
			@Override
			public void actionPerformed(DialogInterface dialog, int which) {
				onItemClick(a, b, c, d);
			}
		});
		
		list.add(getString(R.string.transaction_remove), new ChoiceActivatedClosure() {
			@Override
			public void actionPerformed(DialogInterface dialog, int which) {
				removeTransactionGuarded(c);
			}
		});
		
		list.run();
		return true;
	}
	
	private void removeTransactionGuarded(int idx) {
		AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
		final int fidx = idx;
		dlgAlert.setMessage(R.string.transaction_remove_text);
		dlgAlert.setTitle(R.string.warning);
		dlgAlert.setPositiveButton(R.string.ok, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				removeTransaction(fidx);
			}
		});
		
		dlgAlert.setNegativeButton(R.string.cancel, null);
		dlgAlert.setCancelable(true);
		dlgAlert.create().show();
	}
	
	private void removeTransaction(int idx) {
		TransactionListItem item = items.get(idx);
		db.removeTransaction(item.getId());
		refreshTransactionList();
	}
	
	private void refreshTransactionList() {
		loadTransactionList(accId);
		
		listAdapter.notifyDataSetChanged();
	}
}
