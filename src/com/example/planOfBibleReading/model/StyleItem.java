package com.example.planOfBibleReading.model;

import android.content.Context;
import android.view.View;

public class StyleItem extends BiblePlanObject {
	private final Context context;
	private final String fontStandart;
	private final String fontBold;
	private final String fontButton;
	private final int colorBackground;
	private final int textColor;
	private final int colorBackgroundChecked;
	private final int colorBackgroundButton;
	private final int buttonSelector;
	private final String smallTextSize = "5sp";
	private final String normalTextSize = "5sp";
	private final String largeTextSize = "5sp";
	private final String xlargeTextSize = "5sp";
	private final String smallTitleSize = "5sp";
	private final String normalTitleSize = "5sp";
	private final String largeTitleSize = "5sp";
	private final String xlargeTitleSize = "5sp";
	private final String smallButtonSize = "5sp";
	private final String normalButtonSize = "5sp";
	private final String largeButtonSize = "5sp";
	private final String xlargeButtonSize = "5sp";

	public StyleItem(final Context context, final String fontStandart,
			final String fontBold, final String fontButton,
			final int colorBackground, final int textColor,
			final int colorBackgroundChecked, final int colorBackgroundButton,
			final String nameStyle, final int buttonSelector,
			final int numberStyle) {
		super(numberStyle, nameStyle);
		this.context = context;
		this.fontStandart = fontStandart;
		this.colorBackgroundButton = colorBackgroundButton;
		this.colorBackground = colorBackground;
		this.fontButton = fontButton;
		this.fontBold = fontBold;
		this.textColor = textColor;
		this.colorBackgroundChecked = colorBackgroundChecked;
		this.buttonSelector = buttonSelector;
		
	}

	public String getFontStandart() {
		return fontStandart;
	}

	public String getFontBold() {
		return fontBold;
	}

	public String getFontButton() {
		return fontButton;
	}

	public int getColorBackground() {
		return colorBackground;
	}

	public int getTextColor() {
		return textColor;
	}

	public int getColorBackgroundChecked() {
		return colorBackgroundChecked;
	}

	public final void setBackgroundColorCheckedItems(final View layout) {
		layout.setBackgroundColor(colorBackgroundChecked);
	}

	public final void setBackgroundColor(final View layout) {
		layout.setBackgroundColor(colorBackground);
	}

	public int getColorBackgroundButton() {
		return colorBackgroundButton;
	}

	public int getButtonSelector() {
		return buttonSelector;
	}
}
