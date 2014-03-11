package com.example.planOfBibleReading.model;

import com.example.planOfBibleReading.App;

public class Verse extends BiblePlanObject {
	public final Chapter chapter;
	public final Integer number;

	public Verse(final Integer chapterId, final int id, final int number) {
		super(id, "");
		this.chapter = App.getChapterById(chapterId);
		this.number = number;
	}

	@Override
	public String toString() {
		return chapter.toString() + "." + number.toString();
	}
}
