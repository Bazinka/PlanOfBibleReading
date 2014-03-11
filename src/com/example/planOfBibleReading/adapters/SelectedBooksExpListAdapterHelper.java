package com.example.planOfBibleReading.adapters;

import java.util.ArrayList;

import android.content.Context;

import com.example.planOfBibleReading.App;
import com.example.planOfBibleReading.model.Book;
import com.example.planOfBibleReading.model.Chapter;

public class SelectedBooksExpListAdapterHelper {
	Context ctx;

	public SelectedBooksExpListAdapterHelper(final Context _ctx) {
		ctx = _ctx;
	}

	private SelectedBooksExpandableListAdapter adapter;

	// коллекция для групп
	private ArrayList<Book> groupsBook;

	// коллекция для групп вместе с элементами группы
	private final ArrayList<ArrayList<Chapter>> groupsList = new ArrayList<ArrayList<Chapter>>();

	// общая коллекция для коллекций элементов
	private ArrayList<Chapter> childData;
	// в итоге получится childData = ArrayList<childDataItem>


	public SelectedBooksExpandableListAdapter getAdapter() {
		// Вытащим все книги выбранного перевода (пока это будет просто первый
		// попавшийся)
		try {
			groupsBook = (ArrayList) App.getAllBooks();
			for (final Book group : groupsBook) {
				childData = new ArrayList<Chapter>();
				// заполняем подгруппу
				// Вытащим все главы данной книги
				childData = (ArrayList) App.getChaptersByBookId(group.id);
				// добавляем в коллекцию коллекций
				groupsList.add(childData);
			}

			adapter = new SelectedBooksExpandableListAdapter(ctx, groupsList,
					groupsBook);
		} catch (final Exception e) {
			e.printStackTrace();
		}
		return adapter;
	}
}
