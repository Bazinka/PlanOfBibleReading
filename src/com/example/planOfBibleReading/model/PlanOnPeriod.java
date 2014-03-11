package com.example.planOfBibleReading.model;

import android.content.Context;

public class PlanOnPeriod extends BiblePlanObject {
	public final Integer dayCount;
	public final int numberBookBegin, numberBookEnd;
	public final int numberChapterBegin, numberChapterEnd;
	public final int idPlan;
	public final int flag_parallel;
	public final Context ctx;

	public PlanOnPeriod(final Context context, final Integer dayCount,
			final int id, final int numberChapterBegin,
			final int numberBookBegin, final int numberChapterEnd,
			final int numberBookEnd, final int idPlan, final int flag_parallel) {
		super(id, "");
		this.ctx = context;
		this.dayCount = dayCount;
		this.numberChapterBegin = numberChapterBegin;
		this.numberBookBegin = numberBookBegin;
		this.numberChapterEnd = numberChapterEnd;
		this.numberBookEnd = numberBookEnd;
		this.idPlan = idPlan;
		this.flag_parallel = flag_parallel;
	}

	public Integer getCountChapters() {
		
		final Integer rez = 0;
		return rez;
	}

	@Override
	public String toString() {
		return "План на " + dayCount.toString() + " дней";
	}
}
