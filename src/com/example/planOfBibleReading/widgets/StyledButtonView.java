package com.example.planOfBibleReading.widgets;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

import com.example.planOfBibleReading.App;

public class StyledButtonView extends Button {
	public StyledButtonView(final Context context, final AttributeSet attrs,
			final int defStyle) {
		super(context, attrs, defStyle);

		final Typeface myTypeface = Typeface.createFromAsset(
				context.getAssets(), App.getRightNowStyle().getFontButton());
		setTypeface(myTypeface);
		setTextColor(App.getRightNowStyle().getTextColor());
		setBackgroundResource(App.getRightNowStyle().getButtonSelector());
		// setBackgroundColor(App.getRightNowStyle().getColorBackgroundButton());
	}

	public StyledButtonView(final Context context, final AttributeSet attrs) {
		super(context, attrs);

		final Typeface myTypeface = Typeface.createFromAsset(
				context.getAssets(), App.getRightNowStyle().getFontButton());
		setTypeface(myTypeface);
		setTextColor(App.getRightNowStyle().getTextColor());
		setBackgroundResource(App.getRightNowStyle().getButtonSelector());
		// setBackgroundColor(App.getRightNowStyle().getColorBackgroundButton());
	}
	
	public StyledButtonView(final Context context){
		this(context, null);
	}
}
