package com.example.planOfBibleReading.widgets;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.example.planOfBibleReading.App;

public class StyledTextView extends TextView {

	public StyledTextView(final Context context, final AttributeSet attrs,
			final int defStyle) {
		super(context, attrs, defStyle);

		final Typeface myTypeface = Typeface.createFromAsset(
				context.getAssets(), App.getRightNowStyle().getFontStandart());
		setTypeface(myTypeface);
		setTextColor(App.getRightNowStyle().getTextColor());
		setBackgroundResource(App.getRightNowStyle().getButtonSelector());
//		setBackgroundColor(App.getRightNowStyle().getColorBackground());
	}

	public StyledTextView(final Context context, final AttributeSet attrs) {
		super(context, attrs, 0);

		final Typeface myTypeface = Typeface.createFromAsset(
				context.getAssets(), App.getRightNowStyle().getFontStandart());
		setTypeface(myTypeface);
		setTextColor(App.getRightNowStyle().getTextColor());
		setBackgroundResource(App.getRightNowStyle().getButtonSelector());
//		setBackgroundColor(App.getRightNowStyle().getColorBackground());
	}

}
