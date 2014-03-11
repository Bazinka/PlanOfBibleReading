package com.example.planOfBibleReading.adapters;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

import com.example.planOfBibleReading.App;
import com.example.planOfBibleReading.R;
import com.example.planOfBibleReading.model.PlanOnDay;
import com.example.planOfBibleReading.widgets.StyledTextView;
import com.example.planOfBibleReading.widgets.StyledTitleView;

public class BiblePlanExpandableListAdapter extends BaseExpandableListAdapter {

	private final int TEXT_COLOR = Color.BLACK;
	private final int ERROR_BACKGROUND_COLOR = Color.BLACK;
	private final int BACKGROUND_COLOR = Color.WHITE;
	private final Context mContext;
	private final List<String> groupsName;
	private final List<List<PlanOnDay>> groupsList;

	public BiblePlanExpandableListAdapter(final Context context,
			final List<List<PlanOnDay>> groupList,
			final List<String> groupsName) {
		mContext = context;
		groupsList = groupList;
		this.groupsName = groupsName;
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
		return groupsName.get(groupPosition);
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

		try {
			if (convertView == null) {
				final LayoutInflater inflater = (LayoutInflater) mContext
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(
						R.layout.group_view_plan_reading, null);
			}

			if (isExpanded) {
				// Изменяем что-нибудь, если текущая Group раскрыта
			} else {
				// Изменяем что-нибудь, если текущая Group скрыта
			}

			final StyledTitleView textGroup = (StyledTitleView) convertView
					.findViewById(R.id.textGroup);
			textGroup.setText(getGroupName(groupPosition));
		} catch (final Exception e) {
			e.printStackTrace();
			convertView = null;
		}
		return convertView;

	}

	@Override
	public View getChildView(final int groupPosition, final int childPosition,
			final boolean isLastChild, View convertView, final ViewGroup parent) {
		try {
			if (convertView == null) {
				final LayoutInflater inflater = (LayoutInflater) mContext
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(
						R.layout.child_view_plan_reading, null);
			}

			final StyledTextView textChild = (StyledTextView) convertView
					.findViewById(R.id.textChild);
			final PlanOnDay item = groupsList.get(groupPosition).get(
					childPosition);
			final String namePlan = "  (" + item.getPlanName() + ")";
			textChild.setText(getChildName(groupPosition, childPosition)
					+ namePlan);
			App.getRightNowStyle().setBackgroundColor(convertView.findViewById(R.id.layoutMain));
		} catch (final Exception e) {
			e.printStackTrace();
			convertView = null;
		}
		return convertView;
	}

	@Override
	public boolean isChildSelectable(final int groupPosition,
			final int childPosition) {
		return true;
	}
}
