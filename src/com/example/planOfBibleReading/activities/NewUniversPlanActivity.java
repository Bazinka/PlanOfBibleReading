package com.example.planOfBibleReading.activities;

import java.text.SimpleDateFormat;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.planOfBibleReading.App;
import com.example.planOfBibleReading.MainActivity;
import com.example.planOfBibleReading.R;
import com.example.planOfBibleReading.adapters.ListObjectsAdapter;
import com.example.planOfBibleReading.model.Plan;
import com.example.planOfBibleReading.tools.CreateItemsPlanReading;
import com.example.planOfBibleReading.widgets.StyledButtonView;
import com.example.planOfBibleReading.widgets.StyledTextView;

public class NewUniversPlanActivity extends Activity {
	private ListView lvItems;
	// public Plan checkedPlans;
	private StyledTextView etDateReading;
	// private StyledTitleView tvTitleDate, tvTitleWindow, tvListTitle;
	private StyledButtonView btnSavePlan, btnChangeDateReading;
	private Calendar selectDay;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_univers_plan);
		lvItems = (ListView) findViewById(R.id.lvItems);
		final List<Plan> data = App.getUniversPlans();
		final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		etDateReading = (StyledTextView) findViewById(R.id.etDateReading);
		selectDay = Calendar.getInstance();
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
						selectDay.set(year, monthOfYear, dayOfMonth);
						etDateReading.setText(dateFormat.format(selectDay
								.getTime()));
					}
				}, selectDay.get(Calendar.YEAR), selectDay.get(Calendar.MONTH),
						selectDay.get(Calendar.DAY_OF_MONTH));
				tpd.show();
			}
		});
		etDateReading.setText(dateFormat.format(selectDay.getTime()));
		final ListObjectsAdapter<Plan> adapter = new ListObjectsAdapter<Plan>(
				getApplicationContext(), data);
		lvItems.setAdapter(adapter);
		lvItems.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		lvItems.setSelection(0);
		lvItems.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(final AdapterView<?> parent,
					final View view, final int position, final long id) {
				try {
					final Plan itemPlan = adapter.getItem(position);
					adapter.getChekedData().clear();
					adapter.setChekedData(position);
					adapter.notifyDataSetChanged();
					//
					// if (checkedPlans != null &&
					// checkedPlans.equals(itemPlan)) {
					// final int index = data.indexOf(checkedPlans);
					// final View oldView = adapter.getView(index, null,
					// parent);
					// App.getRightNowStyle().setBackgroundColor(oldView);
					// adapter.notifyDataSetChanged();
					// checkedPlans = null;
					// } else {
					// checkedPlans = itemPlan;
					// App.getRightNowStyle().setBackgroundColorCheckedItems(
					// view);
					// }
				} catch (final Exception e) {
					e.printStackTrace();
				}
			}
		});
		btnSavePlan = (StyledButtonView) findViewById(R.id.btnSavePlan);
		btnSavePlan.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(final View v) {
				try {
					final ProgressDialog pd = new ProgressDialog(v.getContext());
					pd.setTitle("Title");
					
					final List<Plan> checkedPlans = adapter.getChekedData();
					if (!checkedPlans.isEmpty()) {
						final Plan checkedPlan = adapter.getChekedData().get(0);
						pd.setMessage("Подождите, идет создание плана: "
								+ checkedPlan.toString());
						pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
						pd.setCancelable(false);
						pd.show();
						final Thread t = new Thread(new Runnable() {
							@Override
							public void run() {
								final boolean success = CreateItemsPlanReading
										.createItemsByUniversalPlan(
												getApplicationContext(),
												checkedPlan.id, selectDay);
								final Intent intent = new Intent(
										v.getContext(), MainActivity.class);
								startActivity(intent);
								pd.dismiss();
							}
						});
						t.start();
					}
				} catch (final Exception e) {
					e.printStackTrace();
				}
			}
		});
		final LinearLayout layoutMain = (LinearLayout) findViewById(R.id.layoutMain);
		App.getRightNowStyle().setBackgroundColor(layoutMain);
	}

	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_univers_plan, menu);
		return true;
	}

}
