package com.example.planOfBibleReading;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;

import com.example.planOfBibleReading.model.Book;
import com.example.planOfBibleReading.model.Chapter;
import com.example.planOfBibleReading.model.Content;
import com.example.planOfBibleReading.model.Plan;
import com.example.planOfBibleReading.model.PlanOnDay;
import com.example.planOfBibleReading.model.PlanOnPeriod;
import com.example.planOfBibleReading.model.StyleItem;
import com.example.planOfBibleReading.model.Translate;

public final class App extends Application {

	private static Context context;

	private static Map<Integer, Translate> translates = new HashMap<Integer, Translate>();
	private static Map<Integer, Book> books = new HashMap<Integer, Book>();
	private static Map<Integer, Chapter> chapters = new HashMap<Integer, Chapter>();
	private static Map<Integer, Content> contents = new HashMap<Integer, Content>();
	private static Map<Integer, Plan> plans = new HashMap<Integer, Plan>();
	private static Map<Integer, PlanOnPeriod> planOnPeriods = new HashMap<Integer, PlanOnPeriod>();
	private static Map<Integer, PlanOnDay> planOnDays = new HashMap<Integer, PlanOnDay>();
	private static List<StyleItem> styleItems = new ArrayList<StyleItem>();
	private static StyleItem rightNowStyle;
	private static final String saveStyleKey = "StylePref";
	private static final String saveTranslateKey = "translate";

	@Override
	public void onCreate() {
		super.onCreate();
		App.setContext(getApplicationContext());
		App.setStyleItems();
		final SharedPreferences sPref = getSharedPreferences(
				App.getSaveStyleKey(), MODE_PRIVATE);
		StyleItem item = App.getStyleItems().get(0);
		App.setRightNowStyle(item);
		if (sPref != null) {
			final int numberStyle = sPref.getInt(App.getSaveStyleKey(), -1);
			if (numberStyle != -1) {
				item = App.getStyleItems().get(numberStyle);
				App.setRightNowStyle(item);
			}

		}
	}

	public static Map<Integer, Translate> getTranslates() {
		return translates;
	}

	public static List<Translate> getAllTranslates() {
		return new ArrayList<Translate>(translates.values());
	}

	public static Translate getTranslateById(final Integer id) {
		return translates.get(id);
	}

	public static void setTranslates(final List<Translate> translates) {
		App.translates.clear();
		for (final Translate translate : translates)
			App.translates.put(translate.id, translate);
	}

	public static Map<Integer, Book> getBooks() {
		return books;
	}

	public static List<Book> getAllBooks() {
		return new ArrayList<Book>(books.values());
	}

	public static Book getBookById(final Integer id) {
		return books.get(id);
	}

	public static void setBooks(final List<Book> books) {
		App.books.clear();
		for (final Book book : books)
			App.books.put(book.id, book);

	}

	public static Map<Integer, Chapter> getChapters() {
		return chapters;
	}

	public static List<Chapter> getAllChapters() {
		final List<Chapter> chapter = new ArrayList<Chapter>(chapters.values());
		Collections.sort(chapter);
		return chapter;
	}

	public static Chapter getNextChapter(final Chapter item) {
		final Chapter now = App.getChapterById(item.id);
		final int nextNumber = now.number + 1;
		final Integer bookNumber = now.book.number;
		// если просто след глава
		Chapter next = App.getChapterByNumber(bookNumber, nextNumber);
		if (next != null) {
			return next;
		} else {
			// если была послед глава книги
			next = App.getChapterByNumber(bookNumber + 1, 1);
			if (next != null) {
				return next;
			}
		}
		return null;
	}

	public static Chapter getChapterById(final Integer id) {
		return chapters.get(id);
	}

	public static List<Chapter> getChaptersByBookId(final Integer bookId) {
		final List<Chapter> chapter = new ArrayList<Chapter>();
		for (final Chapter ch : chapters.values()) {
			if (bookId.equals(ch.book.id))
				chapter.add(ch);
		}
		Collections.sort(chapter);
		return chapter;
	}

	public static Chapter getChapterByNumber(final Integer thatBookNumber,
			final Integer number) {
		try {
			for (final Chapter chapter : chapters.values()) {
				if (chapter.book.number.equals(thatBookNumber))
					if (chapter.number.equals(number))
						return chapter;
			}
		} catch (final Exception e) {
			return null;
		}
		return null;
	}

	public static Integer getCountBetweenChapters(final Chapter first,
			final Chapter second) {
		int count = 0;
		String nameChapter;
		for (final Chapter chapter : chapters.values()) {
			nameChapter = first.book.name.toString() + ":"
					+ first.number.toString();
			nameChapter = nameChapter + " == " + chapter.book.name.toString()
					+ ":" + chapter.number.toString();
			if (first.compareTo(chapter) == 0) {
				count += 1;
				nameChapter = first.book.name.toString() + ":"
						+ first.number.toString();
				nameChapter = nameChapter + " == "
						+ chapter.book.name.toString() + ":"
						+ chapter.number.toString();
				Log.d("COMPARE", "ACCEPTED " + nameChapter);
			}
			if (first.compareTo(chapter) == -1
					&& second.compareTo(chapter) == 1) {
				count += 1;
				nameChapter = first.book.name.toString() + ":"
						+ first.number.toString();
				nameChapter = nameChapter + " < "
						+ chapter.book.name.toString() + ":"
						+ chapter.number.toString();
				nameChapter = nameChapter + " AND "
						+ chapter.book.name.toString() + ":"
						+ chapter.number.toString();
				nameChapter = nameChapter + " < " + second.book.name.toString()
						+ ":" + second.number.toString();
				Log.d("COMPARE", "ACCEPTED " + nameChapter);
			}
			if (second.compareTo(chapter) == 0) {
				count += 1;
				nameChapter = second.book.name.toString() + ":"
						+ second.number.toString();
				nameChapter = nameChapter + " == "
						+ chapter.book.name.toString() + ":"
						+ chapter.number.toString();
				Log.d("COMPARE", "ACCEPTED " + nameChapter);
			}
		}
		return count;
	}

	public static void setChapters(final List<Chapter> chapters) {
		App.chapters.clear();
		for (final Chapter chapter : chapters)
			App.chapters.put(chapter.id, chapter);
	}

	public static Map<Integer, Content> getContents() {
		return contents;
	}

	public static Content getContent(final Integer chapterId,
			final Integer translateId) {
		for (final Content content : contents.values()) {
			if (chapterId.equals(content.chapterId)
					&& translateId.equals(content.translateId))
				return content;
		}
		return null;
	}

	public static Content getNextContent(final Content item) {
		final Chapter now = App.getChapterById(item.chapterId);
		final int nextNumber = now.number + 1;
		final Integer bookNumber = now.book.number;
		// если просто след глава
		Chapter next = App.getChapterByNumber(bookNumber, nextNumber);
		if (next != null) {
			for (final Content content : contents.values()) {
				if (content.chapterId == next.id)
					return content;
			}
		} else {
			// если была послед глава книги
			next = App.getChapterByNumber(bookNumber + 1, 1);
			if (next != null) {
				for (final Content content : contents.values()) {
					if (content.chapterId == next.id)
						return content;
				}
			}
		}
		return null;
	}

	public static void setContents(final List<Content> contents) {
		App.contents.clear();
		for (final Content content : contents)
			App.contents.put(content.id, content);
	}

	public static Map<Integer, Plan> getPlans() {
		return plans;
	}

	public static List<Plan> getAllPlans() {
		final List<Plan> plan = new ArrayList<Plan>(plans.values());
		return plan;
	}

	public static List<Plan> getUniversPlans() {
		final List<Plan> rezPlans = new ArrayList<Plan>();
		try {
			for (final Plan plan : plans.values()) {
				if (plan.flagAutoCreate == 1)
					rezPlans.add(plan);
			}
		} catch (final Exception e) {
			e.printStackTrace();
		}
		return rezPlans;
	}

	public static List<Plan> getSelectedPlans() {
		final List<Plan> rezPlans = new ArrayList<Plan>();
		try {
			final Set<Plan> setPlans = new HashSet<Plan>();
			for (final PlanOnDay planOnDay : planOnDays.values()) {
				final Plan plan = getPlanById(planOnDay.idPlan);
				setPlans.add(plan);
			}

			for (final Plan plan : setPlans)
				rezPlans.add(plan);
		} catch (final Exception e) {
			e.printStackTrace();
		}
		return rezPlans;
	}

	public static Plan getPlanById(final Integer id) {
		return plans.get(id);
	}

	public static void setPlans(final List<Plan> plans) {
		App.plans.clear();
		for (final Plan plan : plans)
			App.plans.put(plan.id, plan);
	}

	public static Map<Integer, PlanOnPeriod> getPlanOnPeriods() {
		return planOnPeriods;
	}

	public static List<PlanOnPeriod> getPlanOnPeriodByPlanId(
			final Integer planId) {
		final List<PlanOnPeriod> plans = new ArrayList<PlanOnPeriod>();
		for (final PlanOnPeriod planOnPeriod : planOnPeriods.values()) {
			if (planId.equals(planOnPeriod.idPlan))
				plans.add(planOnPeriod);
		}
		return plans;
	}

	public static void setPlanOnPeriods(final List<PlanOnPeriod> planOnPeriods) {
		App.planOnPeriods.clear();
		for (final PlanOnPeriod planOnPeriod : planOnPeriods)
			App.planOnPeriods.put(planOnPeriod.id, planOnPeriod);
	}

	public static Map<Integer, PlanOnDay> getPlanOnDays() {
		return planOnDays;
	}

	public static List<PlanOnDay> getAllPlanOnDays() {
		final List<PlanOnDay> planOnDay = new ArrayList<PlanOnDay>(
				planOnDays.values());
		return planOnDay;
	}

	public static List<PlanOnDay> getPlanOnDayByPlanId(final Integer planId) {
		final List<PlanOnDay> rezPlanOnDay = new ArrayList<PlanOnDay>();
		for (final PlanOnDay planOnDay : planOnDays.values()) {
			if (planId.equals(planOnDay.idPlan))
				rezPlanOnDay.add(planOnDay);
		}
		return rezPlanOnDay;
	}

	public static List<PlanOnDay> getPlanOnDay(final int day, final int month,
			final int year) {

		final List<PlanOnDay> rezPlanOnDay = new ArrayList<PlanOnDay>();

		for (final PlanOnDay planOnDay : planOnDays.values()) {
			if (planOnDay.day == day && planOnDay.month == month
					&& planOnDay.year == year)
				rezPlanOnDay.add(planOnDay);
		}
		return rezPlanOnDay;
	}

	public static List<PlanOnDay> getPlanOnToday() {
		final Calendar rightNow = Calendar.getInstance();
		final int month = rightNow.get(Calendar.MONTH);
		final int day = rightNow.get(Calendar.DAY_OF_MONTH);
		final int year = rightNow.get(Calendar.YEAR);
		return getPlanOnDay(day, month, year);
	}

	public static void setPlanOnDays(final List<PlanOnDay> planOnDays) {
		App.planOnDays.clear();
		for (final PlanOnDay planOnDay : planOnDays)
			App.planOnDays.put(planOnDay.id, planOnDay);
	}

	public static Context getContext() {
		return context;
	}

	private static void setContext(final Context context) {
		App.context = context;
	}

	public static List<StyleItem> getStyleItems() {
		return styleItems;
	}

	public static StyleItem getStyleItem(final int key) {
		return styleItems.get(key);
	}

	public static void setStyleItem(final StyleItem styleItem) {
		App.styleItems.add(styleItem);
	}

	private static void setStyleItems() {
		final int backgroundColor = Color.rgb(245, 222, 179);
		final int textColor = Color.rgb(139, 69, 19);
		final int colorBackgroundChecked = Color.rgb(210, 105, 30);
		final int colorBackgroundButton = Color.rgb(218, 165, 32);
		final StyleItem item = new StyleItem(context,
				"fonts/MaiandaD-Regular.ttf", "fonts/MaiandaD-Bold.ttf",
				"fonts/MaiandaD-Regular.ttf", backgroundColor, textColor,
				colorBackgroundChecked, colorBackgroundButton,
				"—тандартный стиль", R.drawable.button_standart_selector, 0);
		App.setStyleItem(item);

		App.setStyleItem(new StyleItem(context, "fonts/Ampir_Regular.ttf",
				"fonts/Ampir_Regular.ttf", "fonts/Ampir_Regular.ttf", Color
						.rgb(230, 230, 250), Color.rgb(72, 61, 139), Color.rgb(
						123, 104, 238), Color.rgb(230, 230, 250),
				"Ћавандовый стиль", R.drawable.button_lavanda_selector, 1));

		App.setStyleItem(new StyleItem(context, "fonts/TriUcs8.TTF",
				"fonts/TriUcs8.TTF", "fonts/BookAntiqua-Regular.ttf", Color
						.rgb(238, 232, 170), Color.rgb(128, 128, 0), Color.rgb(
						189, 183, 107), Color.rgb(238, 232, 170),
				"—тарослав€нский стиль", R.drawable.button_old_russia_selector,
				2));

		App.setStyleItem(new StyleItem(context, "fonts/Heinrich_Regular.ttf",
				"fonts/Heinrich_Regular.ttf", "fonts/Heinrich_Regular.ttf",
				Color.rgb(144, 238, 144), Color.rgb(0, 100, 0), Color.rgb(34,
						139, 34), Color.rgb(173, 255, 47), "Ќемецкий стиль",
				R.drawable.button_german_selector, 3));
	}

	public static StyleItem getRightNowStyle() {
		return App.rightNowStyle;
	}

	public static void setRightNowStyle(final StyleItem item) {
		App.rightNowStyle = item;
	}

	public static String getSaveTranslateKey() {
		return saveTranslateKey;
	}

	public static String getSaveStyleKey() {
		return saveStyleKey;
	}

}
