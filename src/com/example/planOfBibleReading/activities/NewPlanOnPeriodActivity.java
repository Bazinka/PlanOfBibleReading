package com.example.planOfBibleReading.activities;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.planOfBibleReading.App;
import com.example.planOfBibleReading.MainActivity;
import com.example.planOfBibleReading.R;
import com.example.planOfBibleReading.adapters.BiblePlanExpandableListAdapter;
import com.example.planOfBibleReading.adapters.ListObjectsAdapter;
import com.example.planOfBibleReading.model.BibleStorage;
import com.example.planOfBibleReading.model.Book;
import com.example.planOfBibleReading.model.Chapter;
import com.example.planOfBibleReading.widgets.StyledButtonView;
import com.example.planOfBibleReading.widgets.StyledTextView;

public class NewPlanOnPeriodActivity extends Activity {

	private StyledTextView etDateEndReading, etDateBeginReading;
//	private StyledTitleView tvTitleWindow, tvTitleDateEnd, tvTitleDateBegin;
	private BiblePlanExpandableListAdapter adapter;
	private final int NO = 0, YES = 1;
	private StyledButtonView btnSavePlan, btnChangeDateBegin, btnChangeDateEnd,
			btnSelectBooks;
	private EditText etPlanName;
	private Calendar dayBegin, dayEnd;
	private  ListObjectsAdapter<Book> adapterBook;
//	private final List<Book> checkedBooks = new ArrayList<Book>();

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_plan_on_period);
		dayBegin = Calendar.getInstance();
		dayEnd = Calendar.getInstance();
		etDateEndReading = (StyledTextView) findViewById(R.id.etDateEndReading);
//		tvTitleDateBegin = (StyledTitleView) findViewById(R.id.tvTitleDateBegin);
//		tvTitleDateEnd = (StyledTitleView) findViewById(R.id.tvTitleDateEnd);
		final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		etDateBeginReading = (StyledTextView) findViewById(R.id.etDateBeginReading);
//		tvTitleWindow = (StyledTitleView) findViewById(R.id.tvTitleWindow);
		etPlanName = (EditText) findViewById(R.id.etPlanName);
		btnChangeDateBegin = (StyledButtonView) findViewById(R.id.btnChangeDateBegin);
		btnChangeDateBegin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(final View v) {
				final DatePickerDialog tpd = new DatePickerDialog(v
						.getContext(), new OnDateSetListener() {

					@Override
					public void onDateSet(final DatePicker view,
							final int year, final int monthOfYear,
							final int dayOfMonth) {
						dayBegin.set(year, monthOfYear, dayOfMonth);
						etDateBeginReading.setText(dateFormat.format(dayBegin
								.getTime()));
					}
				}, dayBegin.get(Calendar.YEAR), dayBegin.get(Calendar.MONTH),
						dayBegin.get(Calendar.DAY_OF_MONTH));
				tpd.setTitle("Настройка даты начала чтения");
				tpd.show();
			}
		});

		btnChangeDateEnd = (StyledButtonView) findViewById(R.id.btnChangeDateEnd);
		btnChangeDateEnd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(final View v) {
				final DatePickerDialog tpd = new DatePickerDialog(v
						.getContext(), new OnDateSetListener() {

					@Override
					public void onDateSet(final DatePicker view,
							final int year, final int monthOfYear,
							final int dayOfMonth) {
						dayEnd.set(year, monthOfYear, dayOfMonth);
						etDateEndReading.setText(dateFormat.format(dayEnd
								.getTime()));
					}
				}, dayEnd.get(Calendar.YEAR), dayEnd.get(Calendar.MONTH),
						dayEnd.get(Calendar.DAY_OF_MONTH));
				tpd.setTitle("Настройка даты окончания чтения");
				tpd.show();
			}
		});

		etDateEndReading.setText(dateFormat.format(dayBegin.getTime()));
		etDateBeginReading.setText(dateFormat.format(dayEnd.getTime()));

		btnSelectBooks = (StyledButtonView) findViewById(R.id.btnSelectBooks);
		btnSelectBooks.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(final View v) {
				show_alert();
			}
		});
		btnSavePlan = (StyledButtonView) findViewById(R.id.btnSavePlan);
		btnSavePlan.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(final View v) {
				final ProgressDialog pd = new ProgressDialog(v.getContext());
				pd.setTitle("Title");
				pd.setMessage("Подождите, идет создание плана чтения Библии");
				pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				pd.setCancelable(false);
				pd.show();

				final Thread t = new Thread(new Runnable() {
					@Override
					public void run() {

						final Calendar calBegin = Calendar.getInstance();
						calBegin.set(dayBegin.get(Calendar.YEAR),
								dayBegin.get(Calendar.MONTH),
								dayBegin.get(Calendar.DAY_OF_MONTH));
						final Calendar calEnd = Calendar.getInstance();
						calEnd.set(dayEnd.get(Calendar.YEAR),
								dayEnd.get(Calendar.MONTH),
								dayEnd.get(Calendar.DAY_OF_MONTH));
						final long millisekBegin = calBegin.getTimeInMillis();
						final long millisekEnd = calEnd.getTimeInMillis();
						// считаем количество дней в плане
						final long numberMillisOfPlan_long = millisekEnd
								- millisekBegin;
						final int numberMillisOfPlan = (int) (millisekEnd)
								- (int) (millisekBegin);

						final long numberDayOfPlan = (millisekEnd - millisekBegin) / 1000 / 3600 / 24;

						final BibleStorage bibleStorage = BibleStorage
								.getInstance(getApplicationContext());
						final List<Chapter> chaptersChecked = new ArrayList<Chapter>();
						final List<Book> checkedBooks = adapterBook.getChekedData();
						for (final Book book : checkedBooks) {
							List<Chapter> chaptersCheckedLocal = new ArrayList<Chapter>();
							chaptersCheckedLocal = App
									.getChaptersByBookId(book.id);
							chaptersChecked.addAll(chaptersCheckedLocal);
						}
						Collections.sort(chaptersChecked);

						// считаем меньшее количество глав

						int min_numberChaptersOnDay = chaptersChecked.size()
								/ (int) numberDayOfPlan;
						// считаем, в сколько днях нужно бОльшее количество глав
						int countDaysMin = chaptersChecked.size()
								- min_numberChaptersOnDay
								* (int) numberDayOfPlan;
						int numberChaptersOnDay;
						// если дней больше, чем глав в завете
						if (min_numberChaptersOnDay == 0) {
							min_numberChaptersOnDay = 1;
							countDaysMin = 0;
						}
						final Date dateBegin = new Date(millisekBegin);
						final Date dateEnd = new Date(millisekEnd);
						final Calendar gcal = Calendar.getInstance();
						gcal.setTime(dateBegin);
						String planName = "Собственный план чтения от "
								+ gcal.get(Calendar.DAY_OF_MONTH) + "."
								+ gcal.get(Calendar.MONDAY);

						if (etPlanName.getText().toString().length() != 0) {
							planName = etPlanName.getText().toString();
						}
						final Integer idPlan = bibleStorage.putPlan(planName,
								NO);
						int j = 0;
						Integer numLastChapter = 0, numChapter = 0;
						Chapter chapter;
						while (gcal.getTime().before(dateEnd)) {
							try {
								final int day = gcal.get(Calendar.DAY_OF_MONTH);
								final int month = gcal.get(Calendar.MONTH);
								final int year = gcal.get(Calendar.YEAR);
								if (j <= countDaysMin)
									numberChaptersOnDay = min_numberChaptersOnDay + 1;
								else
									numberChaptersOnDay = min_numberChaptersOnDay;
								for (int i = 0; i <= numberChaptersOnDay; i++) {
									if (numChapter < chaptersChecked.size()) {
										numChapter = numLastChapter + i;
										chapter = chaptersChecked
												.get(numChapter);

										final Integer idPlanOnDay = bibleStorage
												.putPlanOnDay(day, month, year,
														chapter.id, idPlan);
										Log.d("ChapterName", "ChapterName = "
												+ chapter.getFullChapterName()
												+ ", numChapter = "
												+ numChapter.toString());
									} else {
										numChapter = 0;
									}
								}
								numLastChapter = numChapter + 1;
								j++;
								System.out.println(gcal.getTime().toString());
								gcal.add(Calendar.DAY_OF_YEAR, 1);
							} catch (final Exception e) {
								// e.printStackTrace();
								break;
							}
						}
						final Intent intent = new Intent(v.getContext(),
								MainActivity.class);
						startActivity(intent);
						pd.dismiss();
					}
				});
				t.start();
			}
		});
		final LinearLayout layoutMain = (LinearLayout) findViewById(R.id.layoutMain);
		App.getRightNowStyle().setBackgroundColor(layoutMain);
	}

	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_plan_on_period, menu);
		return true;
	}

	private void show_alert() {

		final Dialog dia = new Dialog(this);
		dia.setContentView(R.layout.list_row_books);
		dia.setTitle("Выбор книги");
		dia.setCancelable(true);
		final List<Book> data = App.getAllBooks();
		final ListView list_alert = (ListView) dia
				.findViewById(R.id.alert_list);
		adapterBook = new ListObjectsAdapter<Book>(
				getApplicationContext(), data);

		list_alert.setAdapter(adapterBook);
		list_alert.setItemsCanFocus(false);
		list_alert.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		list_alert.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(final AdapterView<?> parent,
					final View itemClicked, final int position, final long id) {
				final Book book = data.get(position);
				adapterBook.setChekedData(position);
				adapterBook.notifyDataSetChanged();
//				final int index = checkedBooks.indexOf(book);
//				if (index < 0) {
//					checkedBooks.add(book);
//					App.getRightNowStyle().setBackgroundColorCheckedItems(
//							itemClicked);
//				} else {
//					checkedBooks.remove(book);
//					App.getRightNowStyle().setBackgroundColor(itemClicked);
//				}
//
//				Log.d("programm", "book=" + book.toString());
			}
		});

		final StyledButtonView btn = (StyledButtonView) dia.findViewById(R.id.buttonList);
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(final View v) {
				dia.dismiss();
			}
		});
		dia.show();
	}

}
