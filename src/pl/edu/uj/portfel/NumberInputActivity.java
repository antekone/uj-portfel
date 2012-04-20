package pl.edu.uj.portfel;

import pl.edu.uj.portfel.utils.StringUtils;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class NumberInputActivity extends Activity {
	private String textBuffer;
	private String currency;
	private long number;
	private int fact;
	private boolean factMode;
	private int factPosition;
	private ViewHolder holder;
	
	private int highlightColor = 0xff343434;
	
	private class ViewHolder {
		TextView currencyLabel;
		TextView factorialLabel;
		TextView numberLabel;
	}
	
	public void onCreate(Bundle state) {
		super.onCreate(state);
		setContentView(R.layout.number_input);
		
		factMode = false;
		number = 0;
		textBuffer = "";
		currency = "PLN";
		
		holder = new ViewHolder();
		holder.currencyLabel = (TextView) findViewById(R.id.currencyLabel);
		holder.factorialLabel = (TextView) findViewById(R.id.numberFactLabel);
		holder.numberLabel = (TextView) findViewById(R.id.numberLabel);
		
		refreshNumber();
	}
	
	private void refreshNumber() {
		String fullNumber = StringUtils.commize(number, ' ', '.');
		String factNumber = String.format("%02d", fact);
		
		holder.numberLabel.setText(fullNumber);
		holder.factorialLabel.setText("." + factNumber);
		holder.currencyLabel.setText("PLN");
		
		if(factMode) {
			holder.numberLabel.setBackgroundColor(0);
			holder.factorialLabel.setBackgroundColor(highlightColor);
		} else {
			holder.numberLabel.setBackgroundColor(highlightColor);
			holder.factorialLabel.setBackgroundColor(0);
		}
	}
	
	public void buttonClick(View v) {
		Button b = (Button) v;
		String caption = b.getText().toString();
		int n = Integer.parseInt(caption);
	
		if(! factMode) {
			if(number < 1000000) {
				number *= 10;
				number += n;
			}
		} else {
			int multiplier = 0;
			
			switch(factPosition) {
			case 0: fact = 0; multiplier = 10; factPosition++; break;
			case 1: multiplier = 10;
				factMode = false;
				factPosition = 0;
				break;
			}
			
			fact *= multiplier;
			fact += n;
		}
		
		refreshNumber();
	}
	
	public void buttonDel(View v) {
		if(factMode) {
			fact = 0;
			factPosition = 0;
		} else {
			number /= 10;
		}

		refreshNumber();
	}
	
	public void buttonDotLabel(View v) {
		if(factMode == true) {
			factMode = false;
			factPosition = 0;
			
			refreshNumber();
		}
	}
	
	public void buttonDotLabel2(View v) {
		if(factMode == false) {
			factMode = true;
			factPosition = 0;
		
			refreshNumber();
		}
	}
	
	public void buttonDot(View v) {
		factMode = ! factMode;
		factPosition = 0;
		
		refreshNumber();
	}
	
	public void buttonApply(View v) {
		Intent map = new Intent();
		
		long num = number;
		num *= 100;
		num += fact;
		
		Log.d("X", "returning 3");
		map.putExtra("OUTPUT", num);
		Log.d("X", "returning 2");
		setResult(RESULT_OK, map);
		Log.d("X", "returning 1");
		finish();
		Log.d("X", "returning 0");
	}
}
