package pl.edu.uj.portfel.login;

import java.util.List;

import pl.edu.uj.portfel.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class LoginUserInfoAdapter extends ArrayAdapter<LoginEntryChooser> {
	private List<LoginEntryChooser> items;
	
	private class ViewHolder {
		public TextView text;
	}
	
	public LoginUserInfoAdapter(Context ctx, int id, List<LoginEntryChooser> _items) {
		super(ctx, id, _items);
		items = _items;
	}
	
	@Override
	public View getView(int pos, View v, ViewGroup parent) {
		ViewHolder holder;
		if(v == null) {
			LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.login_layout, null);
			
			holder = new ViewHolder();
			holder.text = (TextView) v.findViewById(R.id.loginUserName);
			v.setTag(holder);
		} else {
			holder = (ViewHolder) v.getTag();
		}
		
		LoginEntryChooser user = items.get(pos);
		holder.text.setText(user.getEntryText());
		return v;
	}
}
