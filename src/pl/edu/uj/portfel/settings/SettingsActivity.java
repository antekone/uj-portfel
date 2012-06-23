package pl.edu.uj.portfel.settings;

import java.util.ArrayList;
import java.util.List;

import pl.edu.uj.portfel.R;
import pl.edu.uj.portfel.settings.entries.DefaultSetting;
import pl.edu.uj.portfel.settings.entries.Setting;
import pl.edu.uj.portfel.settings.entries.SettingsAdapter;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

public class SettingsActivity extends ListActivity {
	private SettingsAdapter adapter;
	private String serverToken;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		serverToken = getIntent().getExtras().getString("SERVER_TOKEN");
		
		if(adapter == null) {
			ArrayList<Setting> items = new ArrayList<Setting>();
			items.add(new DefaultSetting(R.string.settings_account, 		R.string.settings_account_hint,			NewUserActivity.class));
			items.add(new DefaultSetting(R.string.account_editor,			R.string.account_editor_hint,			NewAccountActivity.class));
			items.add(new DefaultSetting(R.string.settings_synchronization, R.string.settings_synchronization_hint,	null));
			adapter = new SettingsAdapter(this, R.layout.settings, items);
		}
		
		setListAdapter(adapter);
	}
	
	protected void onListItemClick(ListView list, View v, int position, long id) {
		List<Setting> items = adapter.getItems();
		Class destClass = items.get(position).getDestActivity();
		
		if(destClass != null) {
			Intent args = new Intent(this, destClass);
			args.putExtra("SERVER_TOKEN", serverToken);
			startActivity(args);
		} else {
			Toast.makeText(this, R.string.not_implemented, Toast.LENGTH_SHORT).show();
		}
	}
}
