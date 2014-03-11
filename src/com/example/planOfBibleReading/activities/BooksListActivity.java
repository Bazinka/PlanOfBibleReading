package com.example.planOfBibleReading.activities;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.planOfBibleReading.App;
import com.example.planOfBibleReading.R;
import com.example.planOfBibleReading.adapters.ListObjectsAdapter;
import com.example.planOfBibleReading.model.Book;
import com.example.planOfBibleReading.model.Translate;
import com.example.planOfBibleReading.widgets.StyledButtonView;
import com.example.planOfBibleReading.widgets.StyledTitleView;

public class BooksListActivity extends Activity {

	private ListView booksListMain;
	private StyledButtonView btnCancelToMain;
	private StyledTitleView txtTitle;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_books_list);
		booksListMain = (ListView) findViewById(R.id.lvMain);
		btnCancelToMain = (StyledButtonView) findViewById(R.id.btnCancelToMain);
		txtTitle = (StyledTitleView) findViewById(R.id.tvTitle);
		// Вытащим данные для определения нужных книг
		final SharedPreferences sPref = getSharedPreferences(
				App.getSaveTranslateKey(), MODE_PRIVATE);
		final int idTranslate = sPref.getInt(App.getSaveTranslateKey(), -1);
		final Translate translate = App.getTranslateById(idTranslate);
		txtTitle.setText(translate.toString());
		btnCancelToMain.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(final View v) {
				final Intent intent = new Intent(v.getContext(),
						BibleOnEveryDayActivity.class);
				startActivity(intent);
			}
		});
		final List<Book> itemsBook = App.getAllBooks();
		// Создаем адаптер, используя массив из файла ресурсов
		final ListObjectsAdapter<Book> adapter = new ListObjectsAdapter<Book>(
				getApplicationContext(), itemsBook);
		booksListMain.setAdapter(adapter);
		booksListMain.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(final AdapterView<?> parent,
					final View view, final int position, final long id) {

				try {
					final Book itemBook = (Book) booksListMain
							.getItemAtPosition(position);
					Log.d("info", "itemClick: position = " + position
							+ ", id = " + id);
					final Intent intent = new Intent(view.getContext(),
							ChaptersListActivity.class);
					intent.putExtra("id_book", itemBook.id);
					intent.putExtra("name_book", itemBook.name);
					startActivity(intent);
				} catch (final Exception e) {
					e.printStackTrace();
				}
			}
		});
		final LinearLayout layoutMain = (LinearLayout) findViewById(R.id.layoutMain);
		App.getRightNowStyle().setBackgroundColor(layoutMain);
	}
}
