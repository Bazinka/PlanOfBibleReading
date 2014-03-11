package com.example.planOfBibleReading.model;

public class Book extends BiblePlanObject {
	public final Integer number;

	public Book(final String name, final int id, final int number) {
		super(id, name);
		this.number = number;
	}
}
