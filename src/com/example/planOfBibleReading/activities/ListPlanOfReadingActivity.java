package com.example.planOfBibleReading.activities;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ListView;

import com.example.planOfBibleReading.App;
import com.example.planOfBibleReading.MainActivity;
import com.example.planOfBibleReading.R;
import com.example.planOfBibleReading.adapters.BiblePlanExListAdapterHelper;
import com.example.planOfBibleReading.adapters.BiblePlanExpandableListAdapter;
import com.example.planOfBibleReading.adapters.ListObjectsAdapter;
import com.example.planOfBibleReading.model.BibleStorage;
import com.example.planOfBibleReading.model.Plan;
import com.example.planOfBibleReading.model.PlanOnDay;
import com.example.planOfBibleReading.widgets.StyledButtonView;
import com.example.planOfBibleReading.widgets.StyledTextView;

public class ListPlanOfReadingActivity extends Activity {
	private StyledTextView tvPlan;
	private StyledButtonView btnDeletePlan, btnCreatePlan, btnCancelToMain;
	private ExpandableListView lvPlan;
	private BiblePlanExpandableListAdapter adapter;
	List<Plan> listPlans;
	private final int PROCESS_STATUS_START = 0;
	private final int PROCESS_STATUS_END = 1;
	private final int NO = 0;
	private String[] createPlanItems;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_list_plan_of_reading);

			btnCreatePlan = (StyledButtonView) findViewById(R.id.btnCreatePlan);
			btnCreatePlan.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(final View v) {
					// TODO Auto-generated method stub
					final AlertDialog.Builder builder = new AlertDialog.Builder(
							v.getContext());
					builder.setTitle("Выбор действия");
					// получаем массив из файла ресурсов
					createPlanItems = getResources().getStringArray(
							R.array.CreatePlanItems);
					builder.setItems(createPlanItems,
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(
										final DialogInterface dialog,
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
			tvPlan = (StyledTextView) findViewById(R.id.tvPlan);
			btnCancelToMain = (StyledButtonView) findViewById(R.id.btnCancelToMain);
			btnCancelToMain.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(final View v) {
					final Intent intent = new Intent(v.getContext(),
							BibleOnEveryDayActivity.class);
					startActivity(intent);
				}
			});
			final BiblePlanExListAdapterHelper adapterHelper = new BiblePlanExListAdapterHelper(
					getApplicationContext());
			adapter = adapterHelper.getAdapter();
			lvPlan = (ExpandableListView) findViewById(R.id.lvPlan);
			lvPlan.setAdapter(adapter);
			if (adapterHelper.getGroupsName().isEmpty()) {
				tvPlan.setText(R.string.planDoesNotExist);
			} else {
				btnDeletePlan = (StyledButtonView) findViewById(R.id.btnDeletePlan);
				btnDeletePlan.setVisibility(View.VISIBLE);
				btnDeletePlan.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(final View v) {

						final List<Plan> listPlans = App.getSelectedPlans();
						final Dialog dia = new Dialog(v.getContext());
						dia.setContentView(R.layout.list_row_books);
						dia.setTitle("Выбор плана для удаления");
						dia.setCancelable(true);
						final BibleStorage bibleStorage = BibleStorage
								.getInstance(getApplicationContext());
						final ListView list_alert = (ListView) dia
								.findViewById(R.id.alert_list);
						final ListObjectsAdapter<Plan> adapterPlans = new ListObjectsAdapter<Plan>(
								getApplicationContext(), listPlans);

						list_alert.setAdapter(adapterPlans);
						list_alert.setItemsCanFocus(false);
						list_alert.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
//						final List<Plan> checkedPlans = new ArrayList<Plan>();
						list_alert
								.setOnItemClickListener(new OnItemClickListener() {
									@Override
									public void onItemClick(
											final AdapterView<?> parent,
											final View itemClicked,
											final int position, final long id) {
										final Plan plan = listPlans
												.get(position);
//										adapterPlans.getChekedData().clear();
										adapterPlans.setChekedData(position);
										adapterPlans.notifyDataSetChanged();
//										final int index = checkedPlans
//												.indexOf(plan);
//										if (index < 0) {
//											checkedPlans.add(plan);
//											App.getRightNowStyle()
//													.setBackgroundColorCheckedItems(
//															itemClicked);
//										} else {
//											checkedPlans.remove(plan);
//											App.getRightNowStyle()
//													.setBackgroundColor(
//															itemClicked);
//										}
									}
								});

						final StyledButtonView btn = (StyledButtonView) dia
								.findViewById(R.id.buttonList);
						btn.setText("Выбрать удаляемый план");
						btn.setOnClickListener(new View.OnClickListener() {

							@Override
							public void onClick(final View v) {

								final ProgressDialog pd = new ProgressDialog(v
										.getContext());
								pd.setTitle("Title");
								pd.setMessage("Подождите, идет удаление");
								pd.setCancelable(false);
								// меняем стиль на индикатор
								pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
								final Handler h = new Handler() {
									@Override
									public void handleMessage(final Message msg) {
										switch (msg.what) {
										case PROCESS_STATUS_START:
											pd.show();
											break;
										case PROCESS_STATUS_END:
											pd.dismiss();
											final Intent intent = new Intent(v
													.getContext(),
													MainActivity.class);
											startActivity(intent);
											break;
										}
									}
								};
								final Thread t = new Thread(new Runnable() {
									@Override
									public void run() {
										h.sendEmptyMessage(PROCESS_STATUS_START);
										// TODO: Удалить элементы
										boolean success = false;
										final List<Plan> checkedPlans = adapterPlans.getChekedData();
										for (final Plan plan : checkedPlans) {
											final List<PlanOnDay> plansOnDay = App
													.getPlanOnDayByPlanId(plan.id);
											for (final PlanOnDay planOnDay : plansOnDay) {
												success = bibleStorage
														.removeItem(
																bibleStorage.tbPlanOnDay,
																planOnDay.id);
											}
											if (success
													&& plan.flagAutoCreate == NO)
												bibleStorage.removeItem(
														bibleStorage.tbPlan,
														plan.id);

										}
										h.sendEmptyMessage(PROCESS_STATUS_END);
									}
								});
								t.start();

								dia.dismiss();
							}
						});
						dia.show();
					}
				});

				// нажатие на элемент
				lvPlan.setOnChildClickListener(new OnChildClickListener() {
					@Override
					public boolean onChildClick(
							final ExpandableListView parent, final View v,
							final int groupPosition, final int childPosition,
							final long id) {
						final PlanOnDay itemPlanOnDay = (PlanOnDay) adapter
								.getChild(groupPosition, childPosition);
						final Intent intent = new Intent(parent.getContext(),
								ChapterActivity.class);
						intent.putExtra("name_chapter", itemPlanOnDay
								.getChapter().toString());
						final ArrayList<Integer> chapterForReadingIds = new ArrayList<Integer>();
						final List<String> groupsName = adapterHelper
								.getGroupsName();
						final Calendar date = Calendar.getInstance();
						date.set(itemPlanOnDay.year, itemPlanOnDay.month,
								itemPlanOnDay.day);
						final SimpleDateFormat format = new SimpleDateFormat(
								"EEEE, d MMMM yyyy");
						final String dayFormat = format.format(date.getTime())
								.toString();
						final int numberDay = groupsName.indexOf(dayFormat);
						// вытаскиваем
						final List<PlanOnDay> plansOnToday = adapterHelper
								.getGroupsList().get(numberDay);
						final int numberItem = plansOnToday
								.indexOf(itemPlanOnDay);
						for (int i = numberItem; i < plansOnToday.size(); i++) {
							final PlanOnDay planOnToday = plansOnToday.get(i);
							chapterForReadingIds.add(planOnToday.getChapter().id);
						}
						chapterForReadingIds.remove((Integer) itemPlanOnDay
								.getChapter().id);
						intent.putExtra("chapter_array", chapterForReadingIds);
						intent.putExtra("name_book",
								itemPlanOnDay.getBookName());
						intent.putExtra("id_chapter",
								itemPlanOnDay.getChapter().id);
						intent.putExtra("from_list_plans", true);
						startActivity(intent);
						return false;
					}
				});
				lvPlan.setLongClickable(true);
				lvPlan.setOnItemLongClickListener(new OnItemLongClickListener() {
					@Override
					public boolean onItemLongClick(final AdapterView<?> parent,
							final View view, final int position, final long id) {
						try {
							if (ExpandableListView.getPackedPositionType(id) == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
								final int groupPosition = ExpandableListView
										.getPackedPositionGroup(id);
								final int childPosition = ExpandableListView
										.getPackedPositionChild(id);
								final PlanOnDay item = (PlanOnDay) adapter
										.getChild(groupPosition, childPosition);
								final AlertDialog.Builder builder = new AlertDialog.Builder(
										ListPlanOfReadingActivity.this);
								// заголовок
								builder.setTitle(R.string.deleteTitle);
								// сообщение
								builder.setMessage(R.string.deleteQues);
								// иконка
								builder.setIcon(android.R.drawable.ic_input_delete);

								final Dialog.OnClickListener myClickListener = new Dialog.OnClickListener() {
									@Override
									public void onClick(
											final DialogInterface dialog,
											final int which) {
										switch (which) {
										// Удаляем
										case Dialog.BUTTON_POSITIVE:
											adapterHelper.getGroupsList()
													.get(groupPosition)
													.remove(item);
											adapter.notifyDataSetChanged();
											// finish();

											break;
										// не удаляем
										case Dialog.BUTTON_NEGATIVE:
											// finish();
											break;
										}
										dialog.dismiss();
									}
								};

								// кнопка положительного ответа
								builder.setPositiveButton(android.R.string.yes,
										myClickListener);

								// кнопка отрицательного ответа
								builder.setNegativeButton(android.R.string.no,
										myClickListener);
								// создаем диалог
								final AlertDialog alert = builder.create();
								alert.show();
								// builder.create();

							}
						} catch (final Exception e) {
							e.printStackTrace();
							return false;
						}
						return true;

					}
				});
			}
			App.getRightNowStyle().setBackgroundColor(
					findViewById(R.id.layoutMain));
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.list_plan_of_reading, menu);
		return true;
	}

}
