package com.example.planOfBibleReading.activities;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.LinearLayout;

import com.example.planOfBibleReading.App;
import com.example.planOfBibleReading.MainActivity;
import com.example.planOfBibleReading.R;
import com.example.planOfBibleReading.adapters.SelectedBooksExpListAdapterHelper;
import com.example.planOfBibleReading.adapters.SelectedBooksExpandableListAdapter;
import com.example.planOfBibleReading.model.BibleStorage;
import com.example.planOfBibleReading.model.Chapter;
import com.example.planOfBibleReading.widgets.StyledButtonView;
import com.example.planOfBibleReading.widgets.StyledTextView;

public class NewPlanOnDayActivity extends Activity {

	private StyledTextView tvDate;
	private SelectedBooksExpandableListAdapter adapter;
	private Calendar selectedDay;
	private ExpandableListView elvMain;
	private StyledButtonView btnChangeDateReading;
	private StyledButtonView btnSavePlan;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_new_plan_on_day);
			tvDate = (StyledTextView) findViewById(R.id.etDateReading);
			selectedDay = Calendar.getInstance();
			tvDate.setText(selectedDay.get(Calendar.DAY_OF_MONTH) + "."
					+ selectedDay.get(Calendar.MONTH) + "."
					+ selectedDay.get(Calendar.YEAR));
			final SelectedBooksExpListAdapterHelper adapterHelper = new SelectedBooksExpListAdapterHelper(
					getApplicationContext());
			adapter = adapterHelper.getAdapter();
			elvMain = (ExpandableListView) findViewById(R.id.elvMain);
			elvMain.setAdapter(adapter);

			// нажатие на элемент
			elvMain.setOnChildClickListener(new OnChildClickListener() {
				@Override
				public boolean onChildClick(final ExpandableListView parent,
						final View v, final int groupPosition,
						final int childPosition, final long id) {
					final boolean isChecked = adapter.isCheckedItem(groupPosition,
							childPosition);
					if(isChecked)
						App.getRightNowStyle().setBackgroundColorCheckedItems(v);
					else
						App.getRightNowStyle().setBackgroundColor(v);
					return false;
				}
			});

			btnChangeDateReading = (StyledButtonView) findViewById(R.id.btnChangeDateReading);
			btnChangeDateReading.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(final View v) {
					final DatePickerDialog tpd = new DatePickerDialog(v
							.getContext(), new OnDateSetListener() {

						@Override
						public void onDateSet(final DatePicker view,
								final int year, final int monthOfYear,
								final int dayOfMonth) {
							selectedDay.set(year, monthOfYear, dayOfMonth);
							tvDate.setText(dayOfMonth + "." + monthOfYear + "."
									+ year);
						}
					}, selectedDay.get(Calendar.YEAR), selectedDay
							.get(Calendar.MONTH), selectedDay
							.get(Calendar.DAY_OF_MONTH));
					tpd.show();
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

							final Integer day = selectedDay
									.get(Calendar.DAY_OF_MONTH);
							final Integer month = selectedDay
									.get(Calendar.MONTH);
							final Integer year = selectedDay.get(Calendar.YEAR);
							final BibleStorage bibleStorage = BibleStorage
									.getInstance(getApplicationContext());
							Integer idPlanOnDay = 0;
							final String planName = "План чтения Библии на "
									+ day.toString() + "." + month.toString()
									+ "." + year.toString();
							final int planId = bibleStorage
									.putPlan(planName, 0);
							final List<Integer> rez = new ArrayList<Integer>();
							for (final Chapter chapter : adapter
									.getCheckedObj()) {
								idPlanOnDay = bibleStorage.putPlanOnDay(day,
										month, year, chapter.id, planId);
								rez.add(idPlanOnDay);
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
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_plan_on_day, menu);
		return true;
	}

}
