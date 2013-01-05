package bg.tarasoft.smartsales.views;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

public class ResizableImageView extends ImageView {

	public ResizableImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		Drawable d = getDrawable();
		int height, width;
		if (d != null) {
			// ceil not round - avoid thin vertical gaps along the left/right
			// edges
			if (d.getIntrinsicHeight() >= d.getIntrinsicWidth()) {
				height = MeasureSpec.getSize(heightMeasureSpec);
				width = (int) Math.ceil((float) height
						* (float) d.getIntrinsicWidth()
						/ (float) d.getIntrinsicHeight());
				System.out.println("VISOK IMAGE: " + height + " " + width);
				
			} else {

				height = MeasureSpec.getSize(heightMeasureSpec);
				width = MeasureSpec.getSize(widthMeasureSpec);
			/*	height = (int) Math.ceil((float) width
						* (float) d.getIntrinsicWidth()
						/ (float) d.getIntrinsicHeight());
				System.out.println("SHIROK IMAGE: " + height + " " + width);
				*/

			}

			// int width = MeasureSpec.getSize(widthMeasureSpec);
			// int height = (int) Math.ceil((float) width * (float)
			// d.getIntrinsicHeight() / (float) d.getIntrinsicWidth());

			setMeasuredDimension(width, height);
		} else {
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		}
	}

}