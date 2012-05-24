package pl.edu.uj.portfel.transaction;

import java.util.ArrayList;
import java.util.List;

import pl.edu.uj.portfel.ErrorReporter;
import pl.edu.uj.portfel.NumberInputActivity;
import pl.edu.uj.portfel.R;
import pl.edu.uj.portfel.db.Database;
import pl.edu.uj.portfel.db.TransactionDao;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class TransactionListActivity extends Activity implements ErrorReporter, OnItemClickListener {
	class ViewHolder {
		TextView transactionListCaption;
	}
	
	private ViewHolder holder;
	private List<TransactionListItem> items;
	private Database db;
	private TransactionListViewAdapter listAdapter;
	private long accId;
	
	public void onCreate(Bundle b) {
		super.onCreate(b);
		setContentView(R.layout.transaction_list);

		holder = new ViewHolder();
		holder.transactionListCaption = (TextView) findViewById(R.id.transactionListCaption);
		
		initActivity();
		
		ListView list = (ListView) findViewById(R.id.transactionList);
		listAdapter = new TransactionListViewAdapter(this, R.layout.transaction_list_item, items);
		list.setAdapter(listAdapter);
		list.setOnItemClickListener(this);
	}
	
	void loadTransactionList(long id) {
		db = new Database(this, this);
		long[] ids = db.getTransactionIds(id);
		items = new ArrayList<TransactionListItem>();
		
		Log.i("accId " + id, "ids size " + ids.length);
		
		for(long tid: ids) {
			Log.i("accId " + id, "loading id " + tid);
			
			TransactionDao dao = db.getTransactionById(tid);
			
			TransactionListItem listItem = new TransactionListItem();
			listItem.setId(dao.getId());
			listItem.setAmount(dao.getAmount());
			listItem.setName("no name");
			items.add(listItem);
		}
	}
	
	void initActivity() {
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
			startActivityForResult(transactionIntent, 1);
		}
	}
}
