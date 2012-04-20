package pl.edu.uj.portfel.settings.entries;

import java.util.List;

import pl.edu.uj.portfel.R;
import pl.edu.uj.portfel.login.LoginEntryChooser;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class SettingsAdapter extends ArrayAdapter<Setting> {
	private List<Setting> items;
	
	private class ViewHolder {
		TextView mediumText;
		TextView smallText;
	}
	
	public SettingsAdapter(Context context, int textViewResourceId, List<Setting> _items) {
		super(context, textViewResourceId, _items);
		items = _items;
	}

	@Override
	public View getView(int pos, View v, ViewGroup parent) {
		Setting setting = items.get(pos);
		if(setting instanceof DefaultSetting)
			return getDefaultSettingView((DefaultSetting) setting, v, parent);
		else if(setting instanceof SeparatorSetting)
			return getSeparatorSettingView((SeparatorSetting) setting, v, parent);
		else
			return null;
	}
	
	private View getDefaultSettingView(DefaultSetting setting, View v, ViewGroup parent) {
		ViewHolder holder;

		if(v == null) {
			LayoutInflater inf = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inf.inflate(R.layout.settings_layout, null);
			
			holder = new ViewHolder();
			holder.mediumText = (TextView) v.findViewById(R.id.settingMediumText);
			holder.smallText = (TextView) v.findViewById(R.id.settingSmallText);
			v.setTag(holder);
		} else {
			holder = (ViewHolder) v.getTag();
		}
		
		
		holder.mediumText.setText(setting.cid);
		holder.smallText.setText(setting.hid);
		return v;
	}
	
	private View getSeparatorSettingView(SeparatorSetting setting, View v, ViewGroup parent) {
		if(v == null) {
			return new SeparatorCustomView(getContext());
		} else
			return v;
	}
	
	public List<Setting> getItems() { return items; }
}
