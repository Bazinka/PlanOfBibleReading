package com.example.planOfBibleReading.model;

public class Plan extends BiblePlanObject {

	public int flagAutoCreate;

	public Plan(final String name, final int id, final int flagAutoCreate) {
		super(id, name);
		this.flagAutoCreate = flagAutoCreate;
	}
}
