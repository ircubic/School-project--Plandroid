package net.ircubic.eventmap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class EventCreation extends Activity
{

	private Calendar calendar_start;
	private Calendar calendar_end;
	private Calendar editing_calendar;
	private ArrayList<Long> invitees;
	private double conflict_percentage;

	private java.text.DateFormat df;
	private java.text.DateFormat tf;

	private Button start_time;
	private Button start_date;
	private Button end_date;
	private Button end_time;
	private ListView invitee_list;
	private TextView event_title;
	private boolean rescheduled;
	static final int START_DATE_DIALOG = 0;
	static final int START_TIME_DIALOG = 1;
	static final int END_DATE_DIALOG = 2;
	static final int END_TIME_DIALOG = 3;
	private static final int CONFLICT_DIALOG_MAJOR = 4;
	private static final int CONFLICT_DIALOG_MINOR = 5;

	@Override
	protected void onCreate(final Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		setContentView(R.layout.event_creation);
		setTitle(R.string.title_create_event);

		df = DateFormat.getDateFormat(getApplicationContext());
		tf = DateFormat.getTimeFormat(getApplicationContext());
		rescheduled = false;
		
		invitee_list = (ListView)findViewById(R.id.inviteeList);
		start_date = set_up_button(R.id.editStartDate, START_DATE_DIALOG);
		start_time = set_up_button(R.id.editStartTime, START_TIME_DIALOG);
		end_date = set_up_button(R.id.editEndDate, END_DATE_DIALOG);
		end_time = set_up_button(R.id.editEndTime, END_TIME_DIALOG);
		event_title = (TextView)findViewById(R.id.textEventTitle);

		final Button inviteButton = (Button)findViewById(R.id.inviteButton);
		inviteButton.setOnClickListener(new OnClickListener() {

			public void onClick(final View v)
			{
				startInviting();
			}
		});

		final Button createEventButton = (Button)findViewById(R.id.eventCreateButton);
		createEventButton.setOnClickListener(new OnClickListener() {

			public void onClick(final View v)
			{
				checkConflictAndCreate();
			}
		});

		calendar_start = Calendar.getInstance();
		calendar_end = Calendar.getInstance();
		calendar_end.add(Calendar.HOUR, 1);

		final Intent i = getIntent();
		conflict_percentage = i.getDoubleExtra("percentage", 0.33d);
		updateDates();
	}

	protected void checkConflictAndCreate()
	{
		if (!rescheduled && conflict_percentage > 0.0 && invitee_list.getCount() > 0) {
			if (conflict_percentage >= 0.60) {
				showDialog(CONFLICT_DIALOG_MAJOR);
			} else if (conflict_percentage >= 0.20) {
				showDialog(CONFLICT_DIALOG_MINOR);
			}
		} else {
			createAndFinish();
		}
	}

	private void handleConflict()
	{
		
		final Intent intent = new Intent(this, ConflictResolution.class);
		final ArrayList<Long> ids = new ArrayList<Long>();
		final SimpleCursorAdapter ca = (SimpleCursorAdapter)invitee_list
				.getAdapter();
		final Cursor c = ca.getCursor();
		final int nums = c.getCount();
		final long amount = Math.max(1, Math.round(nums * conflict_percentage));
		final Random rand = new Random();
		for (int i = 0; i < amount; i++) {
			final int position = rand.nextInt(nums);
			c.moveToPosition(position);
			ids.add(c.getLong(0));
		}
		intent.putExtra("conflicts", ids);
		startActivityForResult(intent, ConflictResolution.RESOLVE);
	}

	private void createAndFinish()
	{
		final Toast toast = Toast.makeText(getApplicationContext(), "Event "
				+ event_title.getText() + " created", Toast.LENGTH_SHORT);
		toast.show();
		finish();
	}

	private void startInviting()
	{
		final Intent intent = new Intent(this, FriendInviting.class);
		startActivityForResult(intent, FriendInviting.INVITE);
	}

	private Button set_up_button(final int id, final int dialog_id)
	{
		final Button button = (Button)findViewById(id);
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(final View v)
			{
				showDialog(dialog_id);
			}
		});
		return button;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onActivityResult(final int requestCode,
			final int resultCode, final Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode != Activity.RESULT_OK)
			return;

		if (requestCode == FriendInviting.INVITE) {
			final Serializable x = data.getSerializableExtra("invited");

			if (x != null) {
				invitees = (ArrayList<Long>)x;
				final String where = String.format("%s IN (%s)",
						FriendProvider.KEY_ID, TextUtils.join(",", invitees));
				final Cursor c = managedQuery(FriendProvider.CONTENT_URI, null,
						where, null, FriendProvider.KEY_NAME + " ASC");

				final String from[] = {FriendProvider.KEY_NAME};
				final int to[] = {R.id.friendName};
				final SimpleCursorAdapter adapter = new SimpleCursorAdapter(
						this, R.layout.friend_row2, c, from, to);
				invitee_list.setAdapter(adapter);

				((View)invitee_list.getParent()).setVisibility(View.VISIBLE);
			}
		} else if (requestCode == ConflictResolution.RESOLVE) {
			createAndFinish();
		}
	}

	private void updateDates()
	{
		start_date.setText(df.format(calendar_start.getTime()));
		start_time.setText(tf.format(calendar_start.getTime()));
		end_date.setText(df.format(calendar_end.getTime()));
		end_time.setText(tf.format(calendar_end.getTime()));
	}

	private final OnTimeSetListener mStartTimeSetListener = new OnTimeSetListener() {
		public void onTimeSet(final TimePicker view, final int hourOfDay,
				final int minute)
		{
			editing_calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
			editing_calendar.set(Calendar.MINUTE, minute);
			updateDates();
		}
	};

	private final OnDateSetListener mStartDateSetListener = new OnDateSetListener() {

		public void onDateSet(final DatePicker view, final int year,
				final int monthOfYear, final int dayOfMonth)
		{
			editing_calendar.set(Calendar.YEAR, year);
			editing_calendar.set(Calendar.MONTH, monthOfYear);
			editing_calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
			updateDates();
		}
	};

	@Override
	protected Dialog onCreateDialog(final int id)
	{
		if (id == START_TIME_DIALOG || id == START_DATE_DIALOG)
			editing_calendar = calendar_start;
		else
			editing_calendar = calendar_end;

		if (id == START_TIME_DIALOG || id == END_TIME_DIALOG) {
			return new TimePickerDialog(this, mStartTimeSetListener,
					editing_calendar.get(Calendar.HOUR_OF_DAY),
					editing_calendar.get(Calendar.MINUTE), false);
		}

		else if (id == START_DATE_DIALOG || id == END_DATE_DIALOG) {
			return new DatePickerDialog(this, mStartDateSetListener,
					editing_calendar.get(Calendar.YEAR),
					editing_calendar.get(Calendar.MONTH),
					editing_calendar.get(Calendar.DAY_OF_MONTH));
		} else if (id == CONFLICT_DIALOG_MAJOR) {
			final AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("Most guests are busy then, want to reschedule?")
					.setTitle("Schedule conflict!")
					.setCancelable(false)
					.setPositiveButton("Reschedule",
							new DialogInterface.OnClickListener() {
								public void onClick(
										final DialogInterface dialog,
										final int id)
								{
									rescheduled = true;
									findViewById(R.id.dateBox)
											.setBackgroundColor(0xFF660000);
									dialog.cancel();
								}
							})
					.setNegativeButton("View conflicts",
							new DialogInterface.OnClickListener() {
								public void onClick(
										final DialogInterface dialog,
										final int which)
								{
									handleConflict();
								}
							});
			return builder.create();
		} else if (id == CONFLICT_DIALOG_MINOR) {
			final AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(
					"Some guests are busy then, want to review the conflicts?")
					.setTitle("Schedule conflict!")
					.setCancelable(false)
					.setPositiveButton("Ignore",
							new DialogInterface.OnClickListener() {
								public void onClick(
										final DialogInterface dialog,
										final int id)
								{
									createAndFinish();
								}
							})
					.setNegativeButton("View conflicts",
							new DialogInterface.OnClickListener() {
								public void onClick(
										final DialogInterface dialog,
										final int which)
								{
									handleConflict();
								}
							});
			return builder.create();
		} else
			return null;
	}

}
