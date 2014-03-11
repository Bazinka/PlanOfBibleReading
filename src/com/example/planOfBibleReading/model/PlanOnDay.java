package com.example.planOfBibleReading.model;

import android.content.Context;

import com.example.planOfBibleReading.App;

public class PlanOnDay extends BiblePlanObject {
	public final int day, month, year;
	public final int idChapter, idPlan;

	public PlanOnDay(final Context context, final int inputDay,
			final int inputMonth, final int inputYear,
			final int inputIdChapter, final int idObj,
			final int idPlan) {
		super(idObj, "");
		day = inputDay;
		month = inputMonth;
		year = inputYear;
		idChapter = inputIdChapter;
		// Еще надо определить содержание главы, название перевода и книги
		this.idPlan = idPlan;
		
	}

	public Chapter getChapter() {
		return App.getChapterById(idChapter);
	}

	public String getBookName() {
		return getChapter().book.toString();
	}

	public String getPlanName() {
		final Plan plan = App.getPlanById(idPlan);
		return plan.toString();
	}

	@Override
	public String toString() {
		return getBookName() + " : " + getChapter().toString();// "на " + day +
																// "/" +
		// month
	}
}
