package com.example.planOfBibleReading.adapters;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.planOfBibleReading.App;
import com.example.planOfBibleReading.R;
import com.example.planOfBibleReading.widgets.StyledTextView;

public class ListObjectsAdapter<BiblePlanObject> extends
		ArrayAdapter<BiblePlanObject> {

	private final LayoutInflater inflater;
	private final List<BiblePlanObject> data;
	private final List<BiblePlanObject> chekedData;

	public ListObjectsAdapter(final Context context,
			final List<BiblePlanObject> data) {
		super(context, R.layout.listview_item_row, data);
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.data = data;
		this.chekedData = new ArrayList<BiblePlanObject>();
	}

	@Override
	public View getView(final int position, final View convertView,
			final ViewGroup parent) {
		View row = convertView;
		try {

			if (row == null) {
				row = inflater.inflate(R.layout.listview_item_row, parent,
						false);
			}

			final StyledTextView txtTitle = (StyledTextView) row
					.findViewById(R.id.txtTitle);
			final BiblePlanObject obj = data.get(position);
			
			txtTitle.setText(obj.toString());
			txtTitle.setTag(obj);
			final View layoutMain = row.findViewById(R.id.layoutMain);
			App.getRightNowStyle().setBackgroundColor(layoutMain);
			final int index = getChekedData().indexOf(obj);
			if (index < 0) {
				App.getRightNowStyle().setBackgroundColor(txtTitle);
			} else {
				App.getRightNowStyle().setBackgroundColorCheckedItems(txtTitle);
			}
		} catch (final Exception e) {
			e.printStackTrace();
		}
		return row;
	}

	public void setChekedData(final int position) {
		final BiblePlanObject item = data.get(position);
		final int index = chekedData.indexOf(item);
		if (index < 0) {
			chekedData.add(item);
		} else {
			chekedData.remove(item);
		}

	}

	public List<BiblePlanObject> getChekedData() {
		return chekedData;
	}

}
