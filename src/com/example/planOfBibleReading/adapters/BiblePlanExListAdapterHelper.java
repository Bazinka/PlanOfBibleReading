package com.example.planOfBibleReading.adapters;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import android.content.Context;

import com.example.planOfBibleReading.App;
import com.example.planOfBibleReading.model.PlanOnDay;

public class BiblePlanExListAdapterHelper {
	Context ctx;

	public BiblePlanExListAdapterHelper(final Context _ctx) {
		ctx = _ctx;
	}

	private BiblePlanExpandableListAdapter adapter = null;

	// коллекция для групп
	private final List<String> groupsName = new ArrayList<String>();

	public List<String> getGroupsName() {
		return groupsName;
	}

	// коллекция для групп вместе с элементами группы
	private final List<List<PlanOnDay>> groupsList = new ArrayList<List<PlanOnDay>>();

	// в итоге получится childData = ArrayList<childDataItem>
	public synchronized BiblePlanExpandableListAdapter getAdapter() {
		if (adapter == null) {

			// Вытащим все книги выбранного перевода (пока это будет просто
			// первый
			// попавшийся)
			try {
				// общая коллекция для коллекций элементов
				List<PlanOnDay> childData;
				final Calendar rightNow = Calendar.getInstance();

				// сейчас
				final long millisekBegin = rightNow.getTimeInMillis();
				final SimpleDateFormat format = new SimpleDateFormat(
						"EEEE, d MMMM yyyy");
				// узнаем последнюю дату, на которую есть план
				final List<Date> dates = new ArrayList<Date>();
				final List<PlanOnDay> allPlanOnDays = App.getAllPlanOnDays();
				final Calendar calend = Calendar.getInstance();

				for (final PlanOnDay pl : allPlanOnDays) {
					calend.set(pl.year, pl.month, pl.day);
					dates.add(new Date(calend.getTimeInMillis()));
				}
				Collections.sort(dates);

				int sizeDates = dates.size();

				final Date dateBegin = new Date();
				// добавляет последний фейковый день, чобы все выводилось
				final Calendar calEnd = Calendar.getInstance();
				calEnd.setTime(dates.get(sizeDates - 1));
				final long millisek = calEnd.getTimeInMillis()
						+ (24 * 3600 * 1000);
				dates.add(new Date(millisek));
				sizeDates = dates.size();
				final Date dateEnd = dates.get(sizeDates - 1);
				final Calendar gcal = Calendar.getInstance();
				gcal.setTime(dateBegin);
				String dayFormat = format.format(gcal.getTime()).toString();
				int day = gcal.get(Calendar.DAY_OF_MONTH);
				int month = gcal.get(Calendar.MONTH);
				int year = gcal.get(Calendar.YEAR);
				// заполняем подгруппу
				// Вытащим все планы на текущий день
				childData = App.getPlanOnDay(day, month, year);
				if (!childData.isEmpty()) {
					getGroupsName().add(dayFormat);
					getGroupsList().add(childData);
				}
				gcal.add(Calendar.DAY_OF_YEAR, 1);
				while (gcal.getTime().before(dateEnd)) {
					System.out.println(gcal.getTime().toString());
					dayFormat = format.format(gcal.getTime()).toString();
					day = gcal.get(Calendar.DAY_OF_MONTH);
					month = gcal.get(Calendar.MONTH);
					year = gcal.get(Calendar.YEAR);
					// заполняем подгруппу
					// Вытащим все планы на текущий день
					childData = App.getPlanOnDay(day, month, year);
					if (!childData.isEmpty()) {
						getGroupsName().add(dayFormat);
						getGroupsList().add(childData);
					}
					gcal.add(Calendar.DAY_OF_YEAR, 1);
				}
				adapter = new BiblePlanExpandableListAdapter(ctx,
						getGroupsList(), getGroupsName());
			} catch (final Exception e) {
				e.printStackTrace();
			}
		}
		return adapter;
	}

	public List<List<PlanOnDay>> getGroupsList() {
		return groupsList;
	}
}
