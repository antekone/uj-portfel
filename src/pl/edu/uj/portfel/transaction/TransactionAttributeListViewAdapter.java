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

public class TransactionAttributeListViewAdapter extends ArrayAdapter<TransactionAttribute> {
	private List<TransactionAttribute> items;
	
	private class ViewHolder {
		TextView transactionImageLabel;
		TextView transactionCaption;
		TextView transactionDescription;
	}
	
	public TransactionAttributeListViewAdapter(Context ctx, int id, List<TransactionAttribute> source) {
		super(ctx, id, source);
		items = source;
		
	}
	
	@Override
	public View getView(int pos, View v, ViewGroup parent) {
		ViewHolder holder;
		
		if(v == null) {
			LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.transaction_item, null);
			
			holder = new ViewHolder();
			holder.transactionImageLabel = (TextView) v.findViewById(R.id.transactionImageLabel);
			holder.transactionCaption = (TextView) v.findViewById(R.id.transactionCaption);
			holder.transactionDescription = (TextView) v.findViewById(R.id.transactionDescription);
			v.setTag(holder);
			
			holder.transactionCaption.setTextColor(Color.WHITE);
		} else {
			holder = (ViewHolder) v.getTag();
		}
		
		TransactionAttribute attr = items.get(pos);
		
		attr.renderCaption(holder.transactionCaption);
		attr.renderDescription(holder.transactionDescription);
		attr.renderLabel(holder.transactionImageLabel);
		
		return v;
	}
}
