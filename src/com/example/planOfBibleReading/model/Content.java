package com.example.planOfBibleReading.model;

import com.example.planOfBibleReading.App;


public class Content extends BiblePlanObject {
	public final int translateId;
	public final int chapterId;
	private final String content;
	
	public Content(final String content, final int id, final int chapterId, final int translateId) {
		super(id, "");
		this.translateId = translateId;
		this.chapterId = chapterId;
		this.content = content;
	}
	
	

	public Chapter getChapter() {
		return App.getChapterById(chapterId);
	}
	
	public String getContent() {
		return content;
	}
	
	@Override
	public String toString() {
		final Chapter chapter = getChapter();
		return chapter.book.toString() + " : " + chapter.number.toString();
	}
}
