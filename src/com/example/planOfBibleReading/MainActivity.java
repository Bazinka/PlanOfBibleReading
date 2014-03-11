package com.example.planOfBibleReading;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.example.planOfBibleReading.activities.BibleOnEveryDayActivity;
import com.example.planOfBibleReading.model.BibleStorage;
import com.example.planOfBibleReading.model.Translate;
import com.example.planOfBibleReading.tools.SaveBible;
import com.example.planOfBibleReading.widgets.StyledTitleView;

public class MainActivity extends Activity {

	private ProgressBar pbSaving;;

	private StyledTitleView tvTranslate;
	private String nameTranslate;
	private int idTranslate;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		pbSaving = (ProgressBar) findViewById(R.id.pbSaving);
		tvTranslate = (StyledTitleView) findViewById(R.id.tvTranslate);
		(new InitializationTask()).execute(new Object());
	}

	private final void startNextActivity() {
		final Intent intent = new Intent(getApplicationContext(),
				BibleOnEveryDayActivity.class);
		startActivity(intent);
	}

	private final class InitializationTask extends
			AsyncTask<Object, Object, Object> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			tvTranslate.setText("ѕодождите, идет инициализаци€ приложени€");
			App.getRightNowStyle().setBackgroundColor(
					findViewById(R.id.layoutMain));
			pbSaving.setVisibility(View.VISIBLE);
		}

		@Override
		protected Object doInBackground(final Object... params) {
			final BibleStorage bibleStorage = BibleStorage
					.getInstance(getApplicationContext());
			if (!bibleStorage.isDataBaseFill()) {
				final SharedPreferences sPref = getSharedPreferences(
						App.getSaveTranslateKey(), MODE_PRIVATE);
				final SaveBible bible = new SaveBible(getApplicationContext());
				bible.createBible(sPref);
			}
			App.setTranslates(bibleStorage.getAllTranslates());
			App.setBooks(bibleStorage.getAllBooks());
			App.setChapters(bibleStorage.getAllChapters());
			App.setContents(bibleStorage.getAllContent());
			App.setPlans(bibleStorage.getAllPlans());
			App.setPlanOnDays(bibleStorage.getAllPlanOnDay());
			App.setPlanOnPeriods(bibleStorage.getAllPlanOnPeriod());
			

			return null;
		}

		@Override
		protected void onPostExecute(final Object result) {
			super.onPostExecute(result);
			final SharedPreferences sPref = getSharedPreferences(
					App.getSaveTranslateKey(), MODE_PRIVATE);
			idTranslate = sPref.getInt(App.getSaveTranslateKey(), -1);
			final Translate translate = App.getTranslateById(idTranslate);
			startNextActivity();
			finish();
		}

	}
}