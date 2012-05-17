package pl.edu.uj.portfel.transaction;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import pl.edu.uj.portfel.ErrorReporter;
import pl.edu.uj.portfel.R;
import pl.edu.uj.portfel.db.Database;
import pl.edu.uj.portfel.recorder.AudioRecorder;
import pl.edu.uj.portfel.transaction.attributes.text.InputActivity;
import pl.edu.uj.portfel.transaction.attributes.text.TextTransactionAttribute;
import pl.edu.uj.portfel.utils.Currency;
import pl.edu.uj.portfel.utils.StringUtils;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class TransactionInputActivity extends Activity implements OnItemClickListener, ErrorReporter {
	private AudioRecorder recorder;
	private ViewHolder holder;
	private Database db;
	
	private TransactionType type;
	private List<TransactionAttribute> attributes;
	private TransactionAttributeListViewAdapter listAdapter;
	
	public enum TransactionType {
		EXPENSE, EARNING
	}
	
	class ViewHolder {
		public TextView plusBtn;
		public TextView minusBtn;
		public TextView cashValue;
	}
	
	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.transaction);
		
		db = new Database(this, this);
		
		attributes = new ArrayList<TransactionAttribute>();
		Currency currency = new Currency();
		
		loadTransactions(db);
		
		holder = new ViewHolder();
		holder.cashValue = (TextView) findViewById(R.id.cashValue);
		holder.minusBtn = (TextView) findViewById(R.id.transactionMinusBtn);
		holder.plusBtn = (TextView) findViewById(R.id.transactionPlusBtn);

		long cash = getIntent().getExtras().getLong("CASH");
		holder.cashValue.setText(StringUtils.getCanonicalCashValue(cash, currency));
		
		ListView list = (ListView) findViewById(R.id.transactionAttributeList);
		listAdapter = new TransactionAttributeListViewAdapter(this, R.layout.transaction_item, attributes);
		list.setAdapter(listAdapter);
		list.setOnItemClickListener(this);
	}
	
	public void loadTransactions(Database db) {
		// TODO
	}

	public void recordClicked(View v) {
		try {
			if (recorder == null) {
				recorder = new AudioRecorder("/w-portfel/test.3gp");
				recorder.start();
				
				Toast.makeText(this, "recording start", Toast.LENGTH_SHORT).show();
			} else {
				recorder.stop();
				Toast.makeText(this, "recording stop", Toast.LENGTH_SHORT).show();
			}
		} catch (IOException e) {
			try {
				if(recorder != null)
					recorder.stop();
			} catch(IOException ex) {
				
			}
		}
	}
	
	public void addTextAttribute(View v) {
		Intent transactionIntent = new Intent(this, InputActivity.class);
		startActivityForResult(transactionIntent, 1);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(data == null)
			return;
		
		if(requestCode == 1) {
			String caption = data.getCharSequenceExtra("CAPTION").toString();
			String description = data.getCharSequenceExtra("DESCRIPTION").toString();
			
			TextTransactionAttribute attr = new TextTransactionAttribute(caption, description);
			
			attributes.add(attr);
			listAdapter.notifyDataSetChanged();
		}
	}

	
	public void plusBtnClick(View v) {
		holder.plusBtn.setBackgroundColor(Color.GREEN);
		holder.minusBtn.setBackgroundColor(Color.BLACK);
		type = TransactionType.EARNING;
	}
	
	public void minusBtnClick(View v) {
		holder.minusBtn.setBackgroundColor(Color.RED);
		holder.plusBtn.setBackgroundColor(Color.BLACK);
		type = TransactionType.EXPENSE;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		
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
