package com.example.planOfBibleReading.tools;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.util.Log;

import com.example.planOfBibleReading.App;
import com.example.planOfBibleReading.model.BibleStorage;
import com.example.planOfBibleReading.model.Chapter;
import com.example.planOfBibleReading.model.PlanOnPeriod;

public class CreateItemsPlanReading {
	private final static int NO = 0, YES = 1;

	public static synchronized boolean createItemsByUniversalPlan(
			final Context context, final int idPlan, final Calendar selectDay) {
		boolean rezult = true;
		try {
			final BibleStorage bibleStorage = BibleStorage.getInstance(context);
			final List<PlanOnPeriod> planOnPeriod = App
					.getPlanOnPeriodByPlanId(idPlan);

			final Calendar gcal = Calendar.getInstance();
			int day, month, year;
			gcal.setTime(selectDay.getTime());
			for (final PlanOnPeriod plan : planOnPeriod) {
				int j = 0;
				if (plan.flag_parallel == YES) {
					gcal.setTime(selectDay.getTime());
				}
				final int dayCount = plan.dayCount;
				final long millisek = dayCount * 24L * 3600L * 1000L;
				final Date dateEnd = new Date(gcal.getTimeInMillis() + millisek);

				final Chapter firstChapter = App.getChapterByNumber(
						plan.numberBookBegin, plan.numberChapterBegin);

				final Chapter lastChapter = App.getChapterByNumber(
						plan.numberBookEnd, plan.numberChapterEnd);

				int countChapters = App.getCountBetweenChapters(firstChapter,
						lastChapter);
				// считаем меньшее количество глав
				int min_numberChaptersOnDay = (countChapters / dayCount);
				// если дней больше, чем глав в завете
				int k = 1;
				while (min_numberChaptersOnDay == 0) {
					countChapters = countChapters * k;
					// считаем меньшее количество глав
					min_numberChaptersOnDay = (countChapters / dayCount);
					k++;
				}
				// считаем, в сколько днях нужно бОльшее количество глав
				final int countDaysMin = countChapters
						- min_numberChaptersOnDay * dayCount;
				int numberChaptersOnDay;
				// int numLastChapter = firstChapter.id;
				Chapter chapter = firstChapter;
				while (gcal.getTime().before(dateEnd)) {
					if (j < countDaysMin)
						numberChaptersOnDay = min_numberChaptersOnDay + 1;
					else
						numberChaptersOnDay = min_numberChaptersOnDay;
					day = gcal.get(Calendar.DAY_OF_MONTH);
					month = gcal.get(Calendar.MONTH);
					year = gcal.get(Calendar.YEAR);
					for (int i = 0; i < numberChaptersOnDay; i++) {
						try {
							if (chapter != null
									&& chapter.compareTo(lastChapter) == -1) {

								final Integer idPlanOnDay = bibleStorage
										.putPlanOnDay(day, month, year,
												chapter.id, idPlan);
								Log.d("ChapterName",
										"ChapterName = "
												+ chapter.getFullChapterName()
												+ ", numChapter = "
												+ chapter.number.toString());
								chapter = App.getNextChapter(chapter);
							} else {
								if (chapter != null) {
									final Integer idPlanOnDay = bibleStorage
											.putPlanOnDay(day, month, year,
													chapter.id, idPlan);
									Log.d("ChapterName", "ChapterName = "
											+ chapter.getFullChapterName()
											+ ", numChapter = "
											+ chapter.number.toString());
								}
								// если это последний день, то не делаем нового
								// цикла
								if (j + 1 != dayCount)
									chapter = firstChapter;
								else
									chapter = null;
							}
						} catch (final Exception e) {
							break;
						}
					}
					j++;
					System.out.println(gcal.getTime().toString());
					gcal.add(Calendar.DAY_OF_YEAR, 1);
				}
			}
		} catch (final Exception e) {
			e.printStackTrace();
			rezult = false;
		}
		return rezult;
	}
};
