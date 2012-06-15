package pl.edu.uj.portfel.transaction;

import java.util.List;

import pl.edu.uj.portfel.R;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class TransactionListViewAdapter extends ArrayAdapter<TransactionListItem> {
	private List<TransactionListItem> items;
	private Context ctx;
	
	private class ViewHolder {
		TextView transactionItemAmount;
		TextView transactionItemTitle;
		TextView transactionItemSymbol;
	}
	
	public TransactionListViewAdapter(Context ctx, int id, List<TransactionListItem> source) {
		super(ctx, id, source);
		items = source;
		this.ctx = ctx;
	}
	
	@Override
	public View getView(int pos, View v, ViewGroup parent) {
		ViewHolder holder;
		
		if(v == null) {
			LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.transaction_list_item, null);
			
			holder = new ViewHolder();
			holder.transactionItemAmount = (TextView) v.findViewById(R.id.transactionItemAmount);
			holder.transactionItemTitle = (TextView) v.findViewById(R.id.transactionItemTitle);
			holder.transactionItemSymbol = (TextView) v.findViewById(R.id.transactionItemSymbol);

			v.setTag(holder);
		} else {
			holder = (ViewHolder) v.getTag();
		}
		
		TransactionListItem item = items.get(pos);
		
		String amountStr = String.format("%d.%02d %s", item.getAmount() / 100, item.getAmount() % 100, ctx.getString(R.string.currency_small));
		
		holder.transactionItemAmount.setText(amountStr);
		holder.transactionItemTitle.setText("\n" + item.getName() + "\n");
		holder.transactionItemSymbol.setText(item.getDate());
		
		return v;
	}
}
