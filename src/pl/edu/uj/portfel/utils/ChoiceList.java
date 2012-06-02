package pl.edu.uj.portfel.utils;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

class ChoiceAtom {
	private String text;
	private ChoiceActivatedClosure closure;
	
	public ChoiceAtom(String _text, ChoiceActivatedClosure _closure) {
		text = _text;
		closure = _closure;
	}
	
	public String getText() { return text; }
	public ChoiceActivatedClosure getClosure() { return closure; }
}

class ChoiceListClickHandler implements OnClickListener {
	private List<ChoiceAtom> items;
	
	public ChoiceListClickHandler(List<ChoiceAtom> srcItems) {
		items = srcItems;
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		items.get(which).getClosure().actionPerformed(dialog, which);
	}
}

public class ChoiceList {
	private List<ChoiceAtom> items;
	private Context ctx;
	private String title;
	
	public ChoiceList(Context _ctx) {
		ctx = _ctx;
		items = new ArrayList<ChoiceAtom>();
	}
	
	public ChoiceList(Context _ctx, String _title) {
		ctx = _ctx;
		title = _title;
		items = new ArrayList<ChoiceAtom>();
	}
	
	public void add(String text, ChoiceActivatedClosure chosen) {
		items.add(new ChoiceAtom(text, chosen));
	}
	
	public void run() {
		ChoiceListClickHandler clickHandler = new ChoiceListClickHandler(items);

		AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
		if(title != null && title.length() > 0)
			builder.setTitle(title);
		
		CharSequence[] itemsArray = new CharSequence[items.size()];
		for(int i = 0, len = items.size(); i < len; i++)
			itemsArray[i] = items.get(i).getText();
		
		builder.setItems(itemsArray, clickHandler);
		
		AlertDialog alert = builder.create();
		alert.show();
	}
}

