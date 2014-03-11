package com.example.planOfBibleReading.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

import com.example.planOfBibleReading.App;
import com.example.planOfBibleReading.R;
import com.example.planOfBibleReading.model.Book;
import com.example.planOfBibleReading.model.Chapter;
import com.example.planOfBibleReading.widgets.StyledTextView;
import com.example.planOfBibleReading.widgets.StyledTitleView;

public class SelectedBooksExpandableListAdapter extends BaseExpandableListAdapter {
	private final int TEXT_COLOR = Color.BLACK;
	private final int ERROR_BACKGROUND_COLOR = Color.BLACK;
	private final int NOT_CHEKED_BACKGROUND_COLOR = Color.WHITE;
	private final int CHEKED_BACKGROUND_COLOR = Color.MAGENTA;
	private final Context mContext;
	private final ArrayList<Book> groupsBook;
	private final ArrayList<ArrayList<Chapter>> groupsList;
	// коллекция для ВЫДЕЛЕННЫХ ЭЛЕМЕНТОВ
	private ArrayList<Chapter> checkedObj;

	public SelectedBooksExpandableListAdapter(final Context context,
			final ArrayList<ArrayList<Chapter>> groupList,
			final ArrayList<Book> groupBook) {
		mContext = context;
		groupsList = groupList;
		groupsBook = groupBook;
		// коллекция для ВЫДЕЛЕННЫХ ЭЛЕМЕНТОВ
		setCheckedObj(new ArrayList<Chapter>());
	}

	@Override
	public int getGroupCount() {
		return groupsList.size();
	}

	@Override
	public int getChildrenCount(final int groupPosition) {
		return groupsList.get(groupPosition).size();
	}

	@Override
	public Object getGroup(final int groupPosition) {
		return groupsList.get(groupPosition);
	}

	public String getGroupName(final int groupPosition) {
		return groupsBook.get(groupPosition).toString();
	}

	@Override
	public Object getChild(final int groupPosition, final int childPosition) {
		return groupsList.get(groupPosition).get(childPosition);
	}

	public String getChildName(final int groupPosition, final int childPosition) {
		return groupsList.get(groupPosition).get(childPosition).toString();
	}

	@Override
	public long getGroupId(final int groupPosition) {
		return groupPosition;
	}

	@Override
	public long getChildId(final int groupPosition, final int childPosition) {
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public View getGroupView(final int groupPosition, final boolean isExpanded,
			View convertView, final ViewGroup parent) {

		if (convertView == null) {
			final LayoutInflater inflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.group_view_plan_reading,
					null);
		}

		if (isExpanded) {
			// Изменяем что-нибудь, если текущая Group раскрыта
		} else {
			// Изменяем что-нибудь, если текущая Group скрыта
		}

		final StyledTitleView textGroup = (StyledTitleView) convertView
				.findViewById(R.id.textGroup);
		textGroup.setText(getGroupName(groupPosition));
		return convertView;

	}

	@Override
	public View getChildView(final int groupPosition, final int childPosition,
			final boolean isLastChild, View convertView, final ViewGroup parent) {
		if (convertView == null) {
			final LayoutInflater inflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.child_view_plan_reading,
					null);
		}

		final StyledTextView textChild = (StyledTextView) convertView
				.findViewById(R.id.textChild);
		textChild.setText(getChildName(groupPosition, childPosition));
		// смотрим, check'нутый ли уже объект
		final Chapter item = groupsList.get(groupPosition).get(childPosition);
		final int index = getCheckedObj().lastIndexOf(item);
		if (index >= 0) {
			App.getRightNowStyle().setBackgroundColorCheckedItems(textChild);
		} else {
			App.getRightNowStyle().setBackgroundColor(textChild);
		}

		return convertView;
	}

	@Override
	public boolean isChildSelectable(final int groupPosition,
			final int childPosition) {
		return true;
	}

	public boolean isCheckedItem(final int groupPosition,
			final int childPosition) {
		boolean rez;
		try {
			final Chapter item = (Chapter) getChild(groupPosition,
					childPosition);
			// смотрим, check'нутый ли уже объект
			final int index = getCheckedObj().lastIndexOf(item);
			if (index > 0) {
				getCheckedObj().remove(item);
				rez = false;
			} else {
				getCheckedObj().add(item);
				rez = true;
			}
		} catch (final Exception e) {
			e.printStackTrace();
			rez = false;
		}
		return rez;
	}

	public ArrayList<Chapter> getCheckedObj() {
		return checkedObj;
	}

	public void setCheckedObj(final ArrayList<Chapter> checkedObj) {
		this.checkedObj = checkedObj;
	}
}
