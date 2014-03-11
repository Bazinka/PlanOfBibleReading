package com.example.planOfBibleReading.activities;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.planOfBibleReading.App;
import com.example.planOfBibleReading.MainActivity;
import com.example.planOfBibleReading.R;
import com.example.planOfBibleReading.adapters.ListObjectsAdapter;
import com.example.planOfBibleReading.model.StyleItem;
import com.example.planOfBibleReading.model.Translate;
import com.example.planOfBibleReading.widgets.StyledButtonView;
import com.example.planOfBibleReading.widgets.StyledTextView;

public class SettingsActivity extends Activity {
	private String translateText;
	private ListView lvItems;
	private ListObjectsAdapter<StyleItem> adapter;
	private List<StyleItem> listStyleItems;
	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		lvItems = (ListView) findViewById(R.id.lvItems);
		listStyleItems = App.getStyleItems();
		adapter = new ListObjectsAdapter<StyleItem>(
				getApplicationContext(), listStyleItems);
		TranslateInit();
		btnSaveInit();
		lvItemsInit();
		final LinearLayout layoutMain = (LinearLayout) findViewById(R.id.layoutMain);
		App.getRightNowStyle().setBackgroundColor(layoutMain);
	}

	public void lvItemsInit() {
		
		lvItems.setAdapter(adapter);
		lvItems.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		final SharedPreferences sPref = getSharedPreferences(
				App.getSaveStyleKey(), MODE_PRIVATE);
		final int numberStyle = sPref.getInt(App.getSaveStyleKey(), -1);
		if (numberStyle != -1)
			adapter.setChekedData(numberStyle);
		else
			adapter.setChekedData(0);

		lvItems.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(final AdapterView<?> parent,
					final View itemClicked, final int position, final long id) {
				final StyleItem style = listStyleItems.get(position);
				adapter.getChekedData().clear();
				adapter.setChekedData(style.id);
				adapter.notifyDataSetChanged();
//				if (checkedStyle != null && checkedStyle.equals(style)) {
//					final int index = listStyleItems.indexOf(checkedStyle);
//					final View oldView = adapter.getView(index, null, parent);
//					App.getRightNowStyle().setBackgroundColor(oldView);
//					adapter.notifyDataSetChanged();
//					checkedStyle = null;
//				} else {
//					checkedStyle = style;
//					App.getRightNowStyle().setBackgroundColorCheckedItems(
//							itemClicked);
//				}
			}
		});

	}

	public void btnSaveInit() {
		final StyledButtonView btnSave = (StyledButtonView) findViewById(R.id.btnSave);
		btnSave.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(final View v) {
				final List<StyleItem> checkedStyles = adapter.getChekedData();
				final StyleItem checkedStyle = checkedStyles.get(0);
				if (checkedStyle != null) {
					App.setRightNowStyle(checkedStyle);
					final SharedPreferences sPref = getSharedPreferences(
							App.getSaveStyleKey(), MODE_PRIVATE);
					final Editor ed = sPref.edit();
					ed.putInt(App.getSaveStyleKey(), checkedStyle.id);
					ed.commit();
				}
				final Intent intent = new Intent(v.getContext(),
						MainActivity.class);
				startActivity(intent);
			}
		});
	}

	public void TranslateInit() {
		final StyledButtonView btnTranslate = (StyledButtonView) findViewById(R.id.btnTranslate);
		final StyledTextView tvTranslate = (StyledTextView) findViewById(R.id.tvTranslate);
		// получаем сохраненный перевод
		final SharedPreferences sPref = getSharedPreferences(
				App.getSaveTranslateKey(), MODE_PRIVATE);
		final int idTranslate = sPref.getInt(App.getSaveTranslateKey(), -1);
		final Translate translate = App.getTranslateById(idTranslate);
		if (translate != null) {
			translateText = "Вами выбран: " + translate.toString();
			tvTranslate.setText(translateText);
			btnTranslate.setText("Изменить перевод");
		} else {
			translateText = "";
			tvTranslate.setText(translateText);
			btnTranslate.setText("Выбрать перевод");
		}
		btnTranslate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(final View v) {
				final List<Translate> data = App.getAllTranslates();

				final ListObjectsAdapter adapter = new ListObjectsAdapter<Translate>(
						getApplicationContext(), data) {

					TextView title;

					@Override
					public View getView(final int position, View convertView,
							final ViewGroup parent) {
						final LayoutInflater inflater = (LayoutInflater) getApplicationContext()
								.getSystemService(
										Context.LAYOUT_INFLATER_SERVICE);

						if (convertView == null) {
							convertView = inflater.inflate(
									R.layout.dialog_list_row, null);

							title = (TextView) convertView
									.findViewById(R.id.title);
						} else {
							title = (TextView) convertView
									.findViewById(R.id.title);
						}
						title.setText(data.get(position).toString());
						title.setTextColor(Color.BLACK);

						return convertView;
					}
				};
				final AlertDialog.Builder builder = new AlertDialog.Builder(v
						.getContext());
				builder.setTitle("Выбор перевода");
				builder.setAdapter(adapter,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(final DialogInterface dialog,
									final int item) {
								final Translate item_translate = (Translate) adapter
										.getItem(item);
								final String stringPref = MainActivity.class
										.toString();
								final SharedPreferences sPref = getSharedPreferences(
										stringPref, MODE_PRIVATE);
								final Editor ed = sPref.edit();
								ed.putInt(App.getSaveTranslateKey(),
										item_translate.id);
								ed.commit();
								translateText = "Вами выбран: "
										+ item_translate.toString();
								tvTranslate.setText(translateText);
							}
						});
				final AlertDialog alert = builder.create();
				alert.show();
				builder.create();
			}
		});
	}

}
