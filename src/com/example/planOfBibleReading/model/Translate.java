package com.example.planOfBibleReading.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Translate extends BiblePlanObject implements Parcelable {

	public Translate(final String name, final int id) {
		super(id, name);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(final Parcel dest, final int flags) {
		dest.writeString(name);
		dest.writeInt(id);
	}

	public static final Creator<Translate> CREATOR = new Creator<Translate>() {
		@Override
		public Translate createFromParcel(final Parcel in) {
			return new Translate(in.readString(), in.readInt());
		}

		@Override
		public Translate[] newArray(final int size) {
			return new Translate[size];
		}
	};
}
