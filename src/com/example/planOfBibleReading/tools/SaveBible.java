package com.example.planOfBibleReading.tools;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.AssetManager;
import android.util.Log;

import com.example.planOfBibleReading.model.BibleStorage;

public class SaveBible {

	// map для дальнейшего заполнения базы. <название папки "book_0", id книги,
	// которая лежит в папке>
	private final Map<String, Integer> bookMap;
	private final Map<Integer, Map<String, Integer>> chaptersMap;
	private final Context context;
	private final AssetManager assetsFiles;
	private Boolean fillBooks = false;
	private final Boolean fillChapters = false;
	private int translateId;
	private final int NO = 0, YES = 1;
	private final BibleStorage bibleStorage;

	public SaveBible(final Context c) {
		context = c;
		bookMap = new LinkedHashMap<String, Integer>();
		chaptersMap = new LinkedHashMap<Integer, Map<String, Integer>>();
		bibleStorage = BibleStorage.getInstance(context);
		assetsFiles = context.getAssets();
	}

	private boolean isMatches(final String str, final String regEx) {
		final Pattern p = Pattern.compile(regEx);
		final Matcher m = p.matcher(str);
		final boolean b = m.lookingAt();

		return b;
	}

	private synchronized void setTranslateAndBooks(final String folder) {
		try {

			// сначала обрабатываем файл .inf
			final InputStreamReader istream = new InputStreamReader(
					assetsFiles.open(folder + "/books.inf"), "Windows-1251");
			final BufferedReader inf_MRT = new BufferedReader(istream);

			String raw = inf_MRT.readLine();

			int bookId = -1;
			// обрабатываем конфиг
			while (raw != null) {
				// смотрим перевод
				boolean bool = isMatches(raw, "[info]");
				if (bool) {
					final String[] resStr = raw.split("=");
					translateId = bibleStorage.putTranslate(resStr[1]);
				}

				bool = isMatches(raw, "book_");
				// смотрим книгу
				if (bool) {
					if (fillBooks) {
						return;
					}
					final Pattern pat = Pattern.compile("(\\d+)");
					final Matcher matcher = pat.matcher(raw);
					final boolean find = matcher.find();

					final String[] strWithoutTab = raw.split("\t");
					final String[] nameBook = strWithoutTab[1].split(":");
					if (translateId >= 0) {
						int number = 0;
						if (find) {
							final String group = matcher.group();
							number = Integer.parseInt(group) + 1;
						}
						bookId = bibleStorage.putBook(nameBook[0],
								strWithoutTab[0], number);
						bookMap.put(strWithoutTab[0], bookId);
						// выводим
						Log.d(folder, "<" + strWithoutTab[0] + ", " + bookId
								+ ">, " + nameBook[0]);
					}

				}
				raw = inf_MRT.readLine();
			}
			if (!fillBooks) {
				fillBooks = true;
			}
			inf_MRT.close();
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	private synchronized void saveChapters(final String folder,
			final Integer bookId) {
		int chapterId = 0;
		try {
			final Map<String, Integer> chapterMapChild = new LinkedHashMap<String, Integer>();
			final String[] assetsArray = assetsFiles.list(folder);
			for (final String file : assetsArray) {
				if (fillChapters) {
					final Map<String, Integer> c = chaptersMap.get(bookId);
					Log.d(folder,
							"file=" + file + ",  bookId=" + bookId.toString()
									+ ",   c.size=" + c.size());
					chapterId = c.get(file);
				}
				final InputStreamReader istream = new InputStreamReader(
						assetsFiles.open(folder + "/" + file), "Windows-1251");
				final BufferedReader fileOfChapter = new BufferedReader(istream);

				String raw = fileOfChapter.readLine();
				String name = "";
				String content = "";
				int number = 0;
				final Pattern pat = Pattern.compile("\\s(\\d+)");
				while (raw != null) {
					// ищем заголовок
					boolean bool = isMatches(raw, "<h4>");

					if (bool) {
						final Matcher matcher = pat.matcher(raw);
						final boolean find = matcher.find();
						if (find) {
							String group = matcher.group();
							group = group.replace(" ", "");
							name = group;
							number = Integer.parseInt(group);
						} else
							name = raw;

					}
					// собираем содержание
					bool = isMatches(raw, "<span>");
					if (bool) {
						content = content + raw;
					}
					raw = fileOfChapter.readLine();
				}
				if (!fillChapters) {
					chapterId = bibleStorage.putChapter(name, bookId, number);
					chapterMapChild.put(file, chapterId);
				}
				final int contentId = bibleStorage.putContent(content,
						chapterId, translateId);
			}
			if (!fillChapters) {
				chaptersMap.put(bookId, chapterMapChild);
				final Map<String, Integer> example = chaptersMap.get(bookId);
				final Integer example_int = example.get("ch1.txt");
			}
		} catch (final Exception e) {
			e.printStackTrace();
		}

	}

	public void CreatePlansOnPeriod() {
		// От Бытия до Откровения за год
		int dayCout = 365;
		final int idChapterBegin = chaptersMap.get(bookMap.get("book_0")).get(
				"ch1.txt"), idChapterEnd = chaptersMap.get(
				bookMap.get("book_65")).get("ch22.txt");
		String namePlan = "От Бытия до Откровения за 1 год";
		// final int NO = 0, YES = 1;
		int idPlan = bibleStorage.putPlan(namePlan, YES);
		bibleStorage.putPlanOnPeriod(dayCout, 1, 1, 22, 66, idPlan, NO);// последовательно

		// Чтение параллельно Нового и Старого завета
		dayCout = 365;
		namePlan = "Чтение параллельно Нового и Старого завета";
		idPlan = bibleStorage.putPlan(namePlan, YES);
		// Сначала Старый завет
		bibleStorage.putPlanOnPeriod(dayCout, 1, 1, 4, 39, idPlan, NO);
		// А теперь - Новый
		bibleStorage.putPlanOnPeriod(dayCout, 1, 40, 22, 66, idPlan, YES);// параллельно

		// Чтение по три отрывка в день (Н.З, В.З., Псалмы / Притчи)
		dayCout = 236;
		namePlan = "Чтение по три отрывка в день (Н.З, В.З., Псалмы / Притчи)";
		idPlan = bibleStorage.putPlan(namePlan, YES);// параллельно
		// Сначала В.З без Псалтыря
		bibleStorage.putPlanOnPeriod(dayCout, 1, 1, 42, 18, idPlan, NO);
		bibleStorage.putPlanOnPeriod(dayCout, 1, 21, 4, 39, idPlan, NO);
		// Н.З
		dayCout = 365;
		bibleStorage.putPlanOnPeriod(dayCout, 1, 40, 22, 66, idPlan, YES);
		// Псалмы / Притчи
		dayCout = 365;
		bibleStorage.putPlanOnPeriod(dayCout, 1, 19, 31, 20, idPlan, YES);

		// Чтение Псалмы и Притчи за 3 месяца
		dayCout = 92;
		namePlan = "Чтение Псалмы и Притчи за 3 месяца";
		idPlan = bibleStorage.putPlan(namePlan, YES);// параллельно
		// Псалмы / Притчи
		bibleStorage.putPlanOnPeriod(dayCout, 1, 19, 31, 20, idPlan, NO);
	}

	public synchronized void createBible(final SharedPreferences sPref) {
		try {

			// setTranslateAndBooks("MRT");
			//
			// for (final String key : bookMap.keySet()) {
			// final int value = bookMap.get(key);
			// Log.d("MRT/" + key, "key=" + key);
			// saveChapters("MRT/" + key, value);
			// }
			// if (!fillChapters) {
			// fillChapters = true;
			// }

			setTranslateAndBooks("SRT");
			// Синодальный перевод, записываем как перевод по-умолчанию
			final String saveTranslateKey = "translate";
			final Editor ed = sPref.edit();
			ed.putInt(saveTranslateKey, translateId);
			ed.commit();
			Integer count_chapters = 0;
			for (final String key : bookMap.keySet()) {
				Log.d("SRT/" + key, "key=" + key);
				final Integer value = bookMap.get(key);
				saveChapters("SRT/" + key, value);
				final int size = chaptersMap.get(value).size();
				count_chapters += size;
			}
			Log.d("COUNTCHAPTERS",
					"count_chapters = " + count_chapters.toString());
			// bookMap.clear();
			// Записываем существующие планы
			CreatePlansOnPeriod();

		} catch (final Exception e) {
			e.printStackTrace();
		}
	}
}
