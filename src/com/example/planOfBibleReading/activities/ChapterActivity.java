package com.example.planOfBibleReading.activities;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;

import com.example.planOfBibleReading.App;
import com.example.planOfBibleReading.R;
import com.example.planOfBibleReading.model.Chapter;
import com.example.planOfBibleReading.model.Content;
import com.example.planOfBibleReading.model.Translate;
import com.example.planOfBibleReading.widgets.StyledButtonView;
import com.example.planOfBibleReading.widgets.StyledTextView;
import com.example.planOfBibleReading.widgets.StyledTitleView;

public class ChapterActivity extends Activity {

	private StyledTextView textChapter;
	private StyledTitleView titleChapter;
	private StyledButtonView btnNextChapter, btnCancelToMain, btnCancelToListBooks;
	private ArrayList<Integer> chapterForReadingIds;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_chapter);
			// Вытащим данные для определения нужных книг
			final Intent intent = getIntent();
			final int chapterId = intent.getIntExtra("id_chapter", -1);
			final String chapterName = intent.getStringExtra("name_chapter");
			final String bookName = intent.getStringExtra("name_book");
			chapterForReadingIds = intent
					.getIntegerArrayListExtra("chapter_array");
			final Boolean fromListPlans = intent.getBooleanExtra(
					"from_list_plans", false);
			final SharedPreferences sPref = getSharedPreferences(App.getSaveTranslateKey(),
					MODE_PRIVATE);
			final int idTranslate = sPref.getInt(App.getSaveTranslateKey(), -1);
			final Translate translate = App.getTranslateById(idTranslate);
			final Content itemContent = App.getContent(chapterId, idTranslate);
			final String title = translate.toString() + " " + bookName + ":"
					+ chapterName;
			setTitle(title);
			textChapter = (StyledTextView) findViewById(R.id.textChapter);

			titleChapter = (StyledTitleView) findViewById(R.id.titleChapter);

			if (itemContent != null) {
				titleChapter.setText(itemContent.toString());
				textChapter.setText(Html.fromHtml(itemContent.getContent()));
			} else
				titleChapter.setText("Глава не найдена");
			btnNextChapter = (StyledButtonView) findViewById(R.id.btnNextChapter);
			btnCancelToMain = (StyledButtonView) findViewById(R.id.btnCancelToMain);
			btnCancelToMain.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(final View v) {
					final Intent intent = new Intent(v.getContext(),
							BibleOnEveryDayActivity.class);
					startActivity(intent);
				}
			});
			
			if (!fromListPlans) {
				btnCancelToListBooks = (StyledButtonView) findViewById(R.id.btnCancelToListBooks);
//				App.getRightNowStyle().setButtonStyle(btnCancelToListBooks);
				btnCancelToListBooks.setVisibility(View.VISIBLE);
				btnCancelToListBooks.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(final View v) {
						final Intent intent = new Intent(v.getContext(),
								BooksListActivity.class);
						startActivity(intent);
					}
				});
				
			}
			if (fromListPlans && chapterForReadingIds.isEmpty())
				btnNextChapter.setVisibility(View.GONE);
			btnNextChapter.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(final View v) {
					final Intent intent = new Intent(v.getContext(),
							ChapterActivity.class);
					Chapter chapter;
					String bookName;
					final int chapterId;
					if (chapterForReadingIds != null
							&& !chapterForReadingIds.isEmpty()) {
						chapterId = chapterForReadingIds.get(0);
						chapter = App.getChapterById(chapterId);
						bookName = chapter.getBookName();
						chapterForReadingIds.remove((Integer) chapterId);
					} else {
						final Content nextContent = App
								.getNextContent(itemContent);
						chapter = App.getChapterById(nextContent.chapterId);
						chapterId = nextContent.chapterId;
						bookName = chapter.getBookName();
					}
					intent.putExtra("chapter_array", chapterForReadingIds);
					intent.putExtra("name_book", bookName);
					intent.putExtra("id_chapter", chapterId);
					intent.putExtra("name_chapter", chapter.toString());
					if (fromListPlans)
						intent.putExtra("from_list_plans", fromListPlans);
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
