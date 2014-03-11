package com.example.planOfBibleReading.model;

public class BiblePlanObject {
	public final int id;
	public final String name;

	public BiblePlanObject(final int id, final String name) {
		this.id = id;
		this.name = name;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
