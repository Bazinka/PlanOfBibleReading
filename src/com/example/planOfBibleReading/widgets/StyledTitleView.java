package com.example.planOfBibleReading.widgets;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.example.planOfBibleReading.App;

public class StyledTitleView extends TextView {

	public StyledTitleView(final Context context, final AttributeSet attrs,
			final int defStyle) {
		super(context, attrs, defStyle);

		final Typeface myTypeface = Typeface.createFromAsset(
				context.getAssets(), App.getRightNowStyle().getFontBold());
		setTypeface(myTypeface);
		setTextColor(App.getRightNowStyle().getTextColor());
		setBackgroundColor(App.getRightNowStyle().getColorBackground());
		setBackgroundResource(App.getRightNowStyle().getButtonSelector());
	}

	public StyledTitleView(final Context context, final AttributeSet attrs) {
		super(context, attrs, 0);
		final String FontBold = App.getRightNowStyle().getFontBold();
		final Typeface myTypeface = Typeface.createFromAsset(
				context.getAssets(), App.getRightNowStyle().getFontBold());
		setTypeface(myTypeface);
 		setTextColor(App.getRightNowStyle().getTextColor());
		setBackgroundColor(App.getRightNowStyle().getColorBackground());
		setBackgroundResource(App.getRightNowStyle().getButtonSelector());
	}

}
