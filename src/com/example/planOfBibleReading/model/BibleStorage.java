package com.example.planOfBibleReading.model;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BibleStorage {

	// Название таблиц
	public final static String tbChapter = "chapter";
	public final static String tbBook = "book";
	public final static String tbTranslate = "translate";
	public final static String tbPlan = "plan";
	public final static String tbPlanOnDay = "planOnDay";
	public final static String tbPlanOnPeriod = "planOnPeriod";
	public final static String tbContent = "content";

	private static BibleStorage instance = null;

	public final static BibleStorage getInstance(final Context context) {
		if (instance == null) {
			instance = new BibleStorage(context);
		}

		return instance;
	}

	private final DBHelper dbHelper;

	private BibleStorage(final Context context) {
		this.dbHelper = new DBHelper(context);
	}

	// класс по созданию и управлению БД
	private final class DBHelper extends SQLiteOpenHelper {

		// Название БД
		private final static String dbName = "testDB";

		// название Столбцов
		private final static String colName = "name";
		private final static String colContent = "content";
		private final static String colIdBook = "idBook";
		private final static String colIdTranslate = "idTranslate";
		private final static String colIdChapter = "idChapter";

		private final static String colDay = "day";
		private final static String colMonth = "month";
		private final static String colYear = "year";
		private final static String colTag = "tag";
		private final static String colNumber = "number";

		private final static String colidChapter = "idChapter";
		private final static String colidPlan = "idPlan";

		private final static String colDayCount = "dayCount";

		private final static String colNumberChapterBegin = "numberChapterBegin";
		private final static String colNumberChapterEnd = "numberChapterEnd";
		private final static String colNumberBookBegin = "idChapterBegin";
		private final static String colNumberBookEnd = "idChapterEnd";
		private final static String colStatus = "status";
		// надо ли параллельное чтение с предыдущей записью или продолжать
		private final static String colParallels = "flag_parallel";
		// флаг для планов: создан ли автоматически или пользователем
		private final static String colCreateAuto = "auto_create";

		private final static String colId = "id";

		final Context ctx;

		public DBHelper(final Context context) {
			super(context, dbName, null, 1);
			ctx = context;
		}

		public boolean isDataBaseFill(final SQLiteDatabase db,
				final String tbName) {
			final Cursor cursor = db.rawQuery("select * from  " + tbName, null);
			final int resultCount = cursor.getCount();
			return resultCount > 0 ? true : false;
		}

		private void createDB(final SQLiteDatabase db) {
			try {
				db.execSQL("create table " + tbTranslate + " ( "
						+ "id integer primary key autoincrement, " + colName
						+ " text" + ");");
				db.execSQL("create table " + tbBook + " ( "
						+ "id integer primary key autoincrement, " + colName
						+ " text, " + colTag + " text, " + colNumber
						+ " integer); ");
				db.execSQL("create table " + tbChapter + " ( "
						+ "id integer primary key autoincrement, " + colName
						+ " text, " + colNumber + " integer, " + colIdBook
						+ " integer, foreign key(" + colIdBook
						+ ") references " + tbBook + "(id));");
				db.execSQL("create table " + tbContent + " ( "
						+ "id integer primary key autoincrement, " + colContent
						+ " text, " + colIdChapter + " integer,"
						+ colIdTranslate + " integer, foreign key("
						+ colIdChapter + ") references " + tbChapter
						+ "(id), foreign key(" + colIdTranslate
						+ ") references " + tbTranslate + "(id));");
				db.execSQL("create table " + tbPlan + " ( "
						+ "id integer primary key autoincrement, " + colName
						+ " text, " + colCreateAuto + " integer );");
				db.execSQL("create table " + tbPlanOnDay + " ( "
						+ "id integer primary key autoincrement, " + colStatus
						+ " integer, " + colDay + " integer, " + colMonth
						+ " integer, " + colYear + " integer, " + colidPlan
						+ " integer, " + colidChapter
						+ " integer, foreign key(" + colidChapter
						+ ") references " + tbChapter + "(id), foreign key("
						+ colidPlan + ") references " + tbPlan + "(id));");
				db.execSQL("create table " + tbPlanOnPeriod + " ( "
						+ "id integer primary key autoincrement, "
						+ colParallels + " integer, " + colDayCount
						+ " integer, " + colNumberChapterBegin + " integer, "
						+ colNumberChapterEnd + " integer, "
						+ colNumberBookBegin + " integer, " + colNumberBookEnd
						+ " integer, " + colidPlan + " integer, foreign key("
						+ colidPlan + ") references " + tbPlan + "(id));");

			} catch (final Exception e) {
				e.printStackTrace();
			}
		}

		private void dropDB(final SQLiteDatabase db) {
			db.execSQL("drop table if exists " + tbChapter);
			db.execSQL("drop table if exists " + tbBook);
			db.execSQL("drop table if exists " + tbTranslate);
			db.execSQL("drop table if exists " + tbPlan);
			db.execSQL("drop table if exists " + tbPlanOnDay);
			db.execSQL("drop table if exists " + tbContent);
			db.execSQL("drop table if exists " + tbPlanOnPeriod);
		}

		@Override
		public void onCreate(final SQLiteDatabase db) {
			createDB(db);
		}

		@Override
		public void onUpgrade(final SQLiteDatabase db, final int oldVersion,
				final int newVersion) {
			dropDB(db);
			createDB(db);
		}

		public int putContent(final SQLiteDatabase db, final String content,
				final int idChapter, final int translateId) {
			int success = -1;
			try {
				final ContentValues cv = new ContentValues();
				cv.put(colContent, content);
				cv.put(colidChapter, idChapter);
				cv.put(colIdTranslate, translateId);
				success = (int) db.insertOrThrow(tbContent, null, cv);
			} catch (final Exception e) {
				e.printStackTrace();
				success = -1;
			}
			return success;
		}

		public int putPlanOnPeriod(final SQLiteDatabase db, final int dayCount,
				final int numberChapterBegin, final int numberBookBegin,
				final int numberChapterEnd, final int numberBookEnd,
				final int idPlan, final int flagParal) {
			int success = -1;
			try {
				final ContentValues cv = new ContentValues();
				cv.put(colDayCount, dayCount);
				cv.put(colNumberChapterBegin, numberChapterBegin);
				cv.put(colNumberChapterEnd, numberChapterEnd);
				cv.put(colNumberBookBegin, numberBookBegin);
				cv.put(colNumberBookEnd, numberBookEnd);
				cv.put(colidPlan, idPlan);
				cv.put(colParallels, flagParal);
				success = (int) db.insertOrThrow(tbPlanOnPeriod, null, cv);
			} catch (final Exception e) {
				e.printStackTrace();
				success = -1;
			}
			return success;
		}

		public int putChapter(final SQLiteDatabase db, final String name,
				final int bookId, final int number) {
			int success = -1;
			try {
				final ContentValues cv = new ContentValues();
				cv.put(colName, name);
				cv.put(colIdBook, bookId);
				cv.put(colNumber, number);
				success = (int) db.insertOrThrow(tbChapter, null, cv);
			} catch (final Exception e) {
				e.printStackTrace();
				success = -1;
			}
			return success;
		}

		public int putBook(final SQLiteDatabase db, final String name,
				final String tag, final int number) {
			int success = -1;
			try {
				final ContentValues cv = new ContentValues();
				cv.put(colName, name);
				cv.put(colTag, tag);
				cv.put(colNumber, number);

				success = (int) db.insertOrThrow(tbBook, null, cv);
			} catch (final Exception e) {
				e.printStackTrace();
				success = -1;
			}
			return success;
		}

		public int putPlan(final SQLiteDatabase db, final String name,
				final int flagCreateAuto) {
			int success = -1;
			try {
				final ContentValues cv = new ContentValues();
				cv.put(colName, name);
				cv.put(colCreateAuto, flagCreateAuto);
				success = (int) db.insertOrThrow(tbPlan, null, cv);
			} catch (final Exception e) {
				e.printStackTrace();
				success = -1;
			}
			return success;
		}

		public int putTranslate(final SQLiteDatabase db, final String name) {
			int success = -1;
			try {
				final ContentValues cv = new ContentValues();
				cv.put(colName, name);
				success = (int) db.insertOrThrow(tbTranslate, null, cv);
			} catch (final Exception e) {
				e.printStackTrace();
				success = -1;
			}
			return success;
		}

		public int putPlanOnDay(final SQLiteDatabase db, final int day,
				final int month, final int year, final int chapterId,
				final int planId) {
			int success = -1;
			try {
				final ContentValues cv = new ContentValues();
				cv.put(colDay, day);
				cv.put(colMonth, month);
				cv.put(colYear, year);
				cv.put(colidChapter, chapterId);
				if (planId > 0) {
					cv.put(colidPlan, planId);
				}
				success = (int) db.insertOrThrow(tbPlanOnDay, null, cv);
			} catch (final Exception e) {
				e.printStackTrace();
				success = -1;
			}
			return success;
		}

		public List<Plan> getAllPlans(final SQLiteDatabase db) {
			final List<Plan> result = new ArrayList<Plan>();
			try {
				final Cursor getCursor = db.query(tbPlan, null, null, null,
						null, null, colId);
				if (getCursor.moveToFirst()) {

					final int nameColIndex = getCursor.getColumnIndex(colName);
					final int IdColIndex = getCursor.getColumnIndex(colId);
					final int flagCreateAutoColIndex = getCursor
							.getColumnIndex(colCreateAuto);

					do {
						final String rezName = getCursor
								.getString(nameColIndex);
						final int rezId = getCursor.getInt(IdColIndex);
						final int rezFlagAuto = getCursor
								.getInt(flagCreateAutoColIndex);

						result.add(new Plan(rezName, rezId, rezFlagAuto));
					} while (getCursor.moveToNext());
				}
				getCursor.close();

			} catch (final Exception e) {
				e.printStackTrace();
			}
			return result;
		}

		public List<Translate> getAllTranslates(final SQLiteDatabase db) {
			final List<Translate> result = new ArrayList<Translate>();
			try {
				final Cursor getCursor = db.query(tbTranslate, null, null,
						null, null, null, colId);
				if (getCursor.moveToFirst()) {

					final int nameColIndex = getCursor.getColumnIndex(colName);
					final int IdColIndex = getCursor.getColumnIndex(colId);

					do {
						final String rezName = getCursor
								.getString(nameColIndex);
						final int rezId = getCursor.getInt(IdColIndex);

						result.add(new Translate(rezName, rezId));
					} while (getCursor.moveToNext());
				}
				getCursor.close();

			} catch (final Exception e) {
				e.printStackTrace();
			}
			return result;
		}

		public List<Book> getAllBooks(final SQLiteDatabase db) {
			final List<Book> result = new ArrayList<Book>();
			try {

				final Cursor getCursor = db.query(tbBook, null, null, null,
						null, null, colId);
				if (getCursor.moveToFirst()) {

					final int nameColIndex = getCursor.getColumnIndex(colName);
					final int idColIndex = getCursor.getColumnIndex(colId);
					final int numberColIndex = getCursor
							.getColumnIndex(colNumber);

					do {
						final String rezName = getCursor
								.getString(nameColIndex);
						final int rezId = getCursor.getInt(idColIndex);
						final int rezNumber = getCursor.getInt(numberColIndex);
						result.add(new Book(rezName, rezId, rezNumber));
					} while (getCursor.moveToNext());
				}
				getCursor.close();

			} catch (final Exception e) {
				e.printStackTrace();
			}
			return result;
		}

		public List<Content> getAllContent(final SQLiteDatabase db) {
			final List<Content> result = new ArrayList<Content>();
			try {
				final Cursor getCursor = db.query(tbContent, null, null, null,
						null, null, colId);
				if (getCursor.moveToFirst()) {

					final int contentColIndex = getCursor
							.getColumnIndex(colContent);
					final int idChapterColIndex = getCursor
							.getColumnIndex(colIdChapter);
					final int idTranslateColIndex = getCursor
							.getColumnIndex(colIdTranslate);
					final int idColIndex = getCursor.getColumnIndex(colId);

					do {
						final String rezContent = getCursor
								.getString(contentColIndex);
						final int rezIdChapter = getCursor
								.getInt(idChapterColIndex);
						final int rezIdTranslate = getCursor
								.getInt(idTranslateColIndex);
						final int rezId = getCursor.getInt(idColIndex);

						result.add(new Content(rezContent, rezId, rezIdChapter,
								rezIdTranslate));
					} while (getCursor.moveToNext());
				}
				getCursor.close();

			} catch (final Exception e) {
				e.printStackTrace();
			}
			return result;
		}

		public List<Chapter> getAllChapters(final SQLiteDatabase db) {
			final List<Chapter> result = new ArrayList<Chapter>();
			try {
				final Cursor getCursor = db.query(tbChapter, null, null, null,
						null, null, colNumber);
				// final Cursor getCursor = db.query(tbChapter, null, null,
				// null,
				// null, null, null);
				if (getCursor.moveToFirst()) {

					final int nameColIndex = getCursor.getColumnIndex(colName);
					final int IdColIndex = getCursor.getColumnIndex(colId);
					final int bookIdColIndex = getCursor
							.getColumnIndex(colIdBook);
					final int numberColIndex = getCursor
							.getColumnIndex(colNumber);

					do {
						final String rezName = getCursor
								.getString(nameColIndex);
						final int rezId = getCursor.getInt(IdColIndex);
						final int rezBookId = getCursor.getInt(bookIdColIndex);
						final int rezNumber = getCursor.getInt(numberColIndex);

						result.add(new Chapter(ctx, rezName, rezId, rezBookId,
								rezNumber));
					} while (getCursor.moveToNext());
				}
				getCursor.close();

			} catch (final Exception e) {
				e.printStackTrace();
			}
			return result;
		}

		public List<PlanOnPeriod> getAllPlanOnPeriod(final SQLiteDatabase db) {
			final List<PlanOnPeriod> result = new ArrayList<PlanOnPeriod>();
			try {
				final Cursor getCursor = db.query(tbPlanOnPeriod, null, null,
						null, null, null, colId);
				if (getCursor.moveToFirst()) {
					final int dayCountColIndex = getCursor
							.getColumnIndex(colDayCount);
					final int idColIndex = getCursor.getColumnIndex(colId);

					final int numberChapterBeginColIndex = getCursor
							.getColumnIndex(colNumberChapterBegin);

					final int numberChapterEndColIndex = getCursor
							.getColumnIndex(colNumberChapterEnd);

					final int numberBookBeginColIndex = getCursor
							.getColumnIndex(colNumberBookBegin);

					final int numberBookEndColIndex = getCursor
							.getColumnIndex(colNumberBookEnd);

					final int idPlanColIndex = getCursor
							.getColumnIndex(colidPlan);
					final int flagParallelColIndex = getCursor
							.getColumnIndex(colParallels);

					do {
						final int rezDayCount = getCursor
								.getInt(dayCountColIndex);
						final int rezId = getCursor.getInt(idColIndex);

						final int rezNumberChapterBegin = getCursor
								.getInt(numberChapterBeginColIndex);

						final int rezNumberChapterEnd = getCursor
								.getInt(numberChapterEndColIndex);

						final int rezNumberBookBegin = getCursor
								.getInt(numberBookBeginColIndex);

						final int rezNumberBookEnd = getCursor
								.getInt(numberBookEndColIndex);

						final int rezIdPlanCol = getCursor
								.getInt(idPlanColIndex);
						final int rezflagParallel = getCursor
								.getInt(flagParallelColIndex);

						result.add(new PlanOnPeriod(ctx, rezDayCount, rezId,
								rezNumberChapterBegin, rezNumberBookBegin,
								rezNumberChapterEnd, rezNumberBookEnd,
								rezIdPlanCol, rezflagParallel));
					} while (getCursor.moveToNext());
				}
				getCursor.close();

			} catch (final Exception e) {
				e.printStackTrace();
			}
			return result;
		}

		public List<PlanOnDay> getAllPlanOnDay(final SQLiteDatabase db) {
			final List<PlanOnDay> result = new ArrayList<PlanOnDay>();
			int rezDay = 0;
			int rezMonth = 0, rezYear = 0;
			int rezChapter = 0, rezId = 0;
			int rezPlanId = 0;
			try {
				final Cursor getCursor = db.query(tbPlanOnDay, null, null,
						null, null, null, colId);
				if (getCursor.moveToFirst()) {
					final int idColIndex = getCursor.getColumnIndex(colId);
					final int dayColIndex = getCursor.getColumnIndex(colDay);
					final int monthColIndex = getCursor
							.getColumnIndex(colMonth);
					final int yearColIndex = getCursor.getColumnIndex(colYear);
					final int chapterColIndex = getCursor
							.getColumnIndex(colidChapter);
					final int planColIndex = getCursor
							.getColumnIndex(colidPlan);

					do {

						rezId = getCursor.getInt(idColIndex);
						rezDay = getCursor.getInt(dayColIndex);
						rezMonth = getCursor.getInt(monthColIndex);
						rezYear = getCursor.getInt(yearColIndex);
						rezChapter = getCursor.getInt(chapterColIndex);
						rezPlanId = getCursor.getInt(planColIndex);

						result.add(new PlanOnDay(ctx, rezDay, rezMonth,
								rezYear, rezChapter, rezId, rezPlanId));
					} while (getCursor.moveToNext());
				}
				getCursor.close();

			} catch (final Exception e) {
				e.printStackTrace();
			}
			return result;
		}

		public boolean removeItem(final SQLiteDatabase db, final String tbName,
				final int id) {
			boolean success = true;
			try {
				final int count = db.delete(tbName, "id = ?", new String[] { ""
						+ id });
				if (count == 0)
					success = false;
			} catch (final Exception e) {
				e.printStackTrace();
				success = false;
			}
			return success;
		}
	}

	public final synchronized int putContent(final String content,
			final int idChapter, final int translateId) {
		final SQLiteDatabase db = dbHelper.getWritableDatabase();
		final int result = dbHelper.putContent(db, content, idChapter,
				translateId);
		db.close();
		return result;
	}

	public final synchronized int putPlanOnPeriod(final int dayCount,
			final int numberChapterBegin, final int numberBookBegin,
			final int numberChapterEnd, final int numberBookEnd,
			final Integer idPlan, final Integer flagParal) {
		final SQLiteDatabase db = dbHelper.getWritableDatabase();
		final int result = dbHelper.putPlanOnPeriod(db, dayCount,
				numberChapterBegin, numberBookBegin, numberChapterEnd,
				numberBookEnd, idPlan, flagParal);
		db.close();
		return result;
	}

	public final synchronized int putChapter(final String name,
			final int bookId, final int number) {
		final SQLiteDatabase db = dbHelper.getWritableDatabase();
		final int result = dbHelper.putChapter(db, name, bookId, number);
		db.close();
		return result;
	}

	public final synchronized int putPlanOnDay(final int day,
			final Integer month, final Integer year, final int chapterId,
			final int planId) {
		final SQLiteDatabase db = dbHelper.getWritableDatabase();
		final int result = dbHelper.putPlanOnDay(db, day, month, year,
				chapterId, planId);
		db.close();
		return result;
	}

	public final synchronized int putBook(final String name, final String tag,
			final int number) {
		final SQLiteDatabase db = dbHelper.getWritableDatabase();
		final int result = dbHelper.putBook(db, name, tag, number);
		db.close();
		return result;
	}

	public final synchronized int putPlan(final String name,
			final int colCreateAuto) {
		final SQLiteDatabase db = dbHelper.getWritableDatabase();
		final int result = dbHelper.putPlan(db, name, colCreateAuto);
		db.close();
		return result;
	}

	public final synchronized int putTranslate(final String name) {
		int result = -1;
		try {
			final SQLiteDatabase db = dbHelper.getWritableDatabase();
			result = dbHelper.putTranslate(db, name);
			db.close();
		} catch (final Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public final synchronized List<Plan> getAllPlans() {
		final SQLiteDatabase db = dbHelper.getReadableDatabase();
		final List<Plan> result = dbHelper.getAllPlans(db);
		db.close();
		return result;
	}

	public final synchronized List<Translate> getAllTranslates() {
		final SQLiteDatabase db = dbHelper.getReadableDatabase();
		final List<Translate> result = dbHelper.getAllTranslates(db);
		db.close();
		return result;
	}

	public final synchronized List<Book> getAllBooks() {
		final SQLiteDatabase db = dbHelper.getReadableDatabase();
		List<Book> result;
		result = dbHelper.getAllBooks(db);
		db.close();
		return result;
	}

	public final synchronized List<Chapter> getAllChapters() {
		final SQLiteDatabase db = dbHelper.getReadableDatabase();
		List<Chapter> result;
		result = dbHelper.getAllChapters(db);
		db.close();
		return result;
	}

	public final synchronized List<Content> getAllContent() {
		final SQLiteDatabase db = dbHelper.getReadableDatabase();
		List<Content> result;
		result = dbHelper.getAllContent(db);
		db.close();
		return result;
	}

	public final synchronized List<PlanOnDay> getAllPlanOnDay() {
		final SQLiteDatabase db = dbHelper.getReadableDatabase();
		List<PlanOnDay> result;
		result = dbHelper.getAllPlanOnDay(db);
		db.close();
		return result;
	}

	public final synchronized List<PlanOnPeriod> getAllPlanOnPeriod() {
		final SQLiteDatabase db = dbHelper.getReadableDatabase();
		List<PlanOnPeriod> result;
		result = dbHelper.getAllPlanOnPeriod(db);
		db.close();
		return result;
	}

	public final synchronized boolean isDataBaseFill() {
		boolean result = false;
		final SQLiteDatabase db = dbHelper.getReadableDatabase();
		result = dbHelper.isDataBaseFill(db, tbTranslate);
		db.close();
		return result;

	}

	public final synchronized boolean removeItem(final String tbName,
			final int id) {
		boolean result = false;
		final SQLiteDatabase db = dbHelper.getReadableDatabase();
		result = dbHelper.removeItem(db, tbName, id);
		db.close();
		return result;

	}
}