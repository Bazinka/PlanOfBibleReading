package com.example.planOfBibleReading.activities;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.example.planOfBibleReading.App;
import com.example.planOfBibleReading.R;
import com.example.planOfBibleReading.adapters.ListObjectsAdapter;
import com.example.planOfBibleReading.model.Chapter;
import com.example.planOfBibleReading.model.Translate;
import com.example.planOfBibleReading.widgets.StyledButtonView;
import com.example.planOfBibleReading.widgets.StyledTitleView;

public class ChaptersListActivity extends Activity {
	private ListView chapterListMain;
	private StyledTitleView titleChapter;
	private StyledButtonView btnCancelToMain, btnCancelToListBooks;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_chapter_list);
			chapterListMain = (ListView) findViewById(R.id.lvMain);
			// Вытащим данные для определения нужных книг
			final Intent intent = getIntent();

			final int bookId = intent.getIntExtra("id_book", -1);
			final String bookName = intent.getStringExtra("name_book");
			final SharedPreferences sPref = getSharedPreferences(App.getSaveTranslateKey(),
					MODE_PRIVATE);
			final int idTranslate = sPref.getInt(App.getSaveTranslateKey(), -1);
			final Translate translate = App.getTranslateById(idTranslate);
			titleChapter = (StyledTitleView) findViewById(R.id.titleListChapter);
			titleChapter.setText(translate.toString() + ", " + bookName);
			final List<Chapter> itemsChapters = App.getChaptersByBookId(bookId);
			// устанавливаем режим выбора пунктов списка
			// chapterListMain.setChoiceMode(ListView.CHOICE_MODE_NONE);
			// Создаем адаптер, используя массив из файла ресурсов
			final ListObjectsAdapter<Chapter> adapter = new ListObjectsAdapter<Chapter>(
					getApplicationContext(), itemsChapters);
			chapterListMain.setAdapter(adapter);
			chapterListMain.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(final AdapterView<?> parent,
						final View view, final int position, final long id) {

					try {
						final Chapter itemChapter = (Chapter) chapterListMain
								.getItemAtPosition(position);
						final Intent intent = new Intent(view.getContext(),
								ChapterActivity.class);
						intent.putExtra("name_chapter",
								itemChapter.name.toString());
						intent.putExtra("name_chapter",
								itemChapter.name.toString());
						intent.putExtra("name_book", bookName);
						intent.putExtra("id_chapter", itemChapter.id);
						startActivity(intent);
					} catch (final Exception e) {
						e.printStackTrace();
					}
				}
			});
			btnCancelToMain = (StyledButtonView) findViewById(R.id.btnCancelToMain);
			btnCancelToMain.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(final View v) {
					final Intent intent = new Intent(v.getContext(),
							BibleOnEveryDayActivity.class);
					startActivity(intent);
				}
			});
			btnCancelToListBooks = (StyledButtonView) findViewById(R.id.btnCancelToListBooks);
			btnCancelToListBooks.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(final View v) {
					final Intent intent = new Intent(v.getContext(),
							BooksListActivity.class);
					startActivity(intent);
				}
			});
			App.getRightNowStyle().setBackgroundColor(
					findViewById(R.id.layoutMain));
		} catch (final Exception e) {
			e.printStackTrace();
		}

	}
}
