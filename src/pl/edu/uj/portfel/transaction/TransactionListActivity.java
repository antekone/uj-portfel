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
	
	public void onCreate(Bundle b) {
		super.onCreate(b);
		setContentView(R.layout.transaction_list);

		db = new Database(this, this);
		
		holder = new ViewHolder();
		holder.transactionListCaption = (TextView) findViewById(R.id.transactionListCaption);
		
		initActivity();
		
		ListView list = (ListView) findViewById(R.id.transactionList);
		listAdapter = new TransactionListViewAdapter(this, R.layout.transaction_list_item, items);
		list.setAdapter(listAdapter);
		list.setOnItemClickListener(this);
	}
	
	void loadTransactionList(long id) {
		long[] ids = db.getTransactionIds(id);
		items = new ArrayList<TransactionListItem>();
		
		for(long tid: ids) {
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
		long accId = b.getLong("ACCOUNT_ID");
		
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
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		
	}
	
	public void addTransaction(View v) {
		Intent transactionIntent = new Intent(this, NumberInputActivity.class);
		startActivityForResult(transactionIntent, 0);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.i("info", "result");
		
		if(data == null)
			return;
		
		Log.i("info", "result2");
		
		if(requestCode == 0) {
			Log.i("info", "result3");
			long num = data.getLongExtra("OUTPUT", 0);
			
			Intent transactionIntent = new Intent(this, TransactionInputActivity.class);
			transactionIntent.putExtra("CASH", num);
			startActivity(transactionIntent);
		}
	}
}
