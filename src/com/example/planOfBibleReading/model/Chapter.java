package com.example.planOfBibleReading.model;

import android.content.Context;
import android.text.Html;
import android.text.Spanned;

import com.example.planOfBibleReading.App;

public class Chapter extends BiblePlanObject implements Comparable<Chapter> {
	public final Integer number;
	public final Book book;
	public final Spanned name;
	public String beauty_name;

	public Chapter(final Context context, final String name, final int id,
			final int bookId, final int number) {
		super(id, name);
		this.name = Html.fromHtml(name);
		this.beauty_name = name;
		this.book = App.getBookById(bookId);
		// this.bookId = bookId;
		this.number = number;
	}

	public String getBookName() {
		return book.toString();
	}

	public String getFullChapterName() {
		return getBookName() + ":" + name.toString();
	}

	@Override
	public String toString() {
		return "Глава " + number.toString();
	}

	@Override
	public int compareTo(final Chapter that) {
		final Integer thatNumber = that.number;
		final Integer thisNumber = this.number;
		final Integer thisBookNumber = this.book.number;
		final Integer thatBookNumber = that.book.number;
		if (thisBookNumber == thatBookNumber) {
			if (thisNumber == thatNumber)
				return 0;
			else if (thisNumber > thatNumber)
				return 1;
			else
				return -1;

		} else if (thisBookNumber > thatBookNumber)
			return 1;
		else
			return -1;
	}
}
