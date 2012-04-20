package pl.edu.uj.portfel.settings.entries;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class SeparatorCustomView extends View {

	public SeparatorCustomView(Context context) {
		super(context);
	}

	protected void onDraw(Canvas ctx) {
		Paint p = new Paint();
		p.setColor(Color.WHITE);
		ctx.drawRect(5, 5, 10, 10, p);
	}
	
	protected void onMeasure(int width, int height) {
		setMeasuredDimension(width, 20);
	}
}
