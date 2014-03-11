package com.example.planOfBibleReading.activities;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.planOfBibleReading.App;
import com.example.planOfBibleReading.R;
import com.example.planOfBibleReading.adapters.ListObjectsAdapter;
import com.example.planOfBibleReading.model.PlanOnDay;
import com.example.planOfBibleReading.model.Translate;
import com.example.planOfBibleReading.widgets.StyledButtonView;
import com.example.planOfBibleReading.widgets.StyledTextView;

public class BibleOnEveryDayActivity extends Activity {
	private List<PlanOnDay> allPlans;
	private String[] createPlanItems;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bible_on_every_day);
		final SharedPreferences sPref = getSharedPreferences(
				App.getSaveTranslateKey(), MODE_PRIVATE);
		final int idTranslate = sPref.getInt(App.getSaveTranslateKey(), -1);
		final Translate translate = App.getTranslateById(idTranslate);

		btnSettingsInit();
		btnBibleInit();
		btnOpenPlanInit();

		allPlans = App.getAllPlanOnDays();
		if (allPlans.isEmpty()) {
			allPlansIsEmpty();
		} else {
			allPlansIsNotEmpty();
		}

		final LinearLayout layoutMain = (LinearLayout) findViewById(R.id.layoutMain);
		App.getRightNowStyle().setBackgroundColor(layoutMain);
	}

	private void btnSettingsInit() {
		final StyledButtonView btnSettings = (StyledButtonView) findViewById(R.id.btnSettings);
		btnSettings.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(final View v) {
				try {
					final Intent intent = new Intent(getApplicationContext(),
							SettingsActivity.class);
					startActivity(intent);
				} catch (final Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private void btnBibleInit() {
		final StyledButtonView btnBible = (StyledButtonView) findViewById(R.id.btnBible);
		btnBible.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(final View v) {
				try {
					final Intent intent = new Intent(v.getContext(),
							BooksListActivity.class);
					startActivity(intent);
				} catch (final Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private void btnOpenPlanInit() {
		final StyledButtonView btnOpenPlan = (StyledButtonView) findViewById(R.id.btnOpenPlan);
		btnOpenPlan.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(final View v) {
				try {
					final Intent intent = new Intent(v.getContext(),
							ListPlanOfReadingActivity.class);
					startActivity(intent);
				} catch (final Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private void allPlansIsEmpty() {
		final StyledTextView textNotFoundPlans = (StyledTextView) findViewById(R.id.textNotFoundPlans);

		final StyledButtonView btnCreatePlan = (StyledButtonView) findViewById(R.id.btnCreatePlan);
		final StyledButtonView btnOpenPlan = (StyledButtonView) findViewById(R.id.btnOpenPlan);
		btnOpenPlan.setVisibility(View.GONE);
		textNotFoundPlans.setText("На сегодня ничего нет");

		btnCreatePlan.setVisibility(View.VISIBLE);
		btnCreatePlan.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(final View v) {
				// TODO Auto-generated method stub
				final AlertDialog.Builder builder = new AlertDialog.Builder(v
						.getContext());
				builder.setTitle("Выбор действия");
				// получаем массив из файла ресурсов
				createPlanItems = getResources().getStringArray(
						R.array.CreatePlanItems);
				builder.setItems(createPlanItems,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(final DialogInterface dialog,
									final int item) {
								Intent intent;
								switch (item) {
								case 0:// Собственный план на 1 день
									intent = new Intent(v.getContext(),
											NewPlanOnDayActivity.class);
									startActivity(intent);
									break;
								case 1:// Собственный план на период
									intent = new Intent(v.getContext(),
											NewPlanOnPeriodActivity.class);
									startActivity(intent);
									break;
								case 2:// Воспользоваться существующими
										// планами
									intent = new Intent(v.getContext(),
											NewUniversPlanActivity.class);
									startActivity(intent);
									break;
								default:
									break;
								}
							}
						});
				final AlertDialog alert = builder.create();
				alert.show();

			}
		});
		textNotFoundPlans.setVisibility(View.VISIBLE);
	}

	private void allPlansIsNotEmpty() {
		final ListView lvPlanOnToday = (ListView) findViewById(R.id.lvPlanOnToday);
		final List<PlanOnDay> itemsPlans = App.getPlanOnToday();
		final StyledTextView textNotFoundPlans = (StyledTextView) findViewById(R.id.textNotFoundPlans);
		final StyledButtonView btnCreatePlan = (StyledButtonView) findViewById(R.id.btnCreatePlan);
		btnCreatePlan.setVisibility(View.GONE);
		if (itemsPlans.isEmpty())
			textNotFoundPlans.setText("На сегодня ничего нет");
		else
			textNotFoundPlans.setText("На сегодня по плану:");
		// Создаем адаптер, используя массив из файла ресурсов
		final ListObjectsAdapter<PlanOnDay> adapter = new ListObjectsAdapter<PlanOnDay>(
				getApplicationContext(), itemsPlans);
		lvPlanOnToday.setAdapter(adapter);
		lvPlanOnToday.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(final AdapterView<?> parent,
					final View view, final int position, final long id) {

				try {
					final PlanOnDay itemPlanOnDay = adapter.getItem(position);
					final Intent intent = new Intent(parent.getContext(),
							ChapterActivity.class);
					intent.putExtra("name_chapter", itemPlanOnDay.getChapter()
							.toString());
					final ArrayList<Integer> chapterForReadingIds = new ArrayList<Integer>();
					final Calendar date = Calendar.getInstance();
					date.set(itemPlanOnDay.year, itemPlanOnDay.month,
							itemPlanOnDay.day);
					final List<PlanOnDay> plansOnToday = App.getPlanOnToday();
					;
					final int numberItem = plansOnToday.indexOf(itemPlanOnDay);
					for (int i = numberItem; i < plansOnToday.size(); i++) {
						final PlanOnDay planOnToday = plansOnToday.get(i);
						chapterForReadingIds.add(planOnToday.getChapter().id);
					}
					chapterForReadingIds.remove((Integer) itemPlanOnDay
							.getChapter().id);
					intent.putExtra("chapter_array", chapterForReadingIds);
					intent.putExtra("name_book", itemPlanOnDay.getBookName());
					intent.putExtra("id_chapter", itemPlanOnDay.getChapter().id);
					intent.putExtra("from_list_plans", true);
					startActivity(intent);
				} catch (final Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
