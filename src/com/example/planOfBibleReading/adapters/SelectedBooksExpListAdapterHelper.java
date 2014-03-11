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

	// ��������� ��� �����
	private ArrayList<Book> groupsBook;

	// ��������� ��� ����� ������ � ���������� ������
	private final ArrayList<ArrayList<Chapter>> groupsList = new ArrayList<ArrayList<Chapter>>();

	// ����� ��������� ��� ��������� ���������
	private ArrayList<Chapter> childData;
	// � ����� ��������� childData = ArrayList<childDataItem>


	public SelectedBooksExpandableListAdapter getAdapter() {
		// ������� ��� ����� ���������� �������� (���� ��� ����� ������ ������
		// ����������)
		try {
			groupsBook = (ArrayList) App.getAllBooks();
			for (final Book group : groupsBook) {
				childData = new ArrayList<Chapter>();
				// ��������� ���������
				// ������� ��� ����� ������ �����
				childData = (ArrayList) App.getChaptersByBookId(group.id);
				// ��������� � ��������� ���������
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
