package net.ircubic.eventmap;

import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;

public class EventCreation extends Activity {

	private Calendar calendar_start;
	private Calendar calendar_end;
	private Calendar editing_calendar;

	private java.text.DateFormat df;
	private java.text.DateFormat tf;

	private Button start_time;
	private Button start_date;
	private Button end_date;
	private Button end_time;

	static final int START_DATE_DIALOG = 0;
	static final int START_TIME_DIALOG = 1;
	static final int END_DATE_DIALOG = 2;
	static final int END_TIME_DIALOG = 3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.event_creation);

		df = DateFormat.getDateFormat(getApplicationContext());
		tf = DateFormat.getTimeFormat(getApplicationContext());

		start_date = set_up_button(R.id.editStartDate, START_DATE_DIALOG);
		start_time = set_up_button(R.id.editStartTime, START_TIME_DIALOG);
		end_date = set_up_button(R.id.editEndDate, END_DATE_DIALOG);
		end_time = set_up_button(R.id.editEndTime, END_TIME_DIALOG);
		
		calendar_start = GregorianCalendar.getInstance();
		calendar_end = GregorianCalendar.getInstance();
		calendar_end.add(Calendar.HOUR, 1);

		updateDates();
	}
	
	private Button set_up_button(int id, final int dialog_id){
		Button button = (Button) findViewById(id);
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showDialog(dialog_id);
			}
		});
		return button;
	}

	private void updateDates() {
		start_date.setText(df.format(calendar_start.getTime()));
		start_time.setText(tf.format(calendar_start.getTime()));
		end_date.setText(df.format(calendar_end.getTime()));
		end_time.setText(tf.format(calendar_end.getTime()));
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	private TimePickerDialog.OnTimeSetListener mStartTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			editing_calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
			editing_calendar.set(Calendar.MINUTE, minute);
			updateDates();
		}
	};

	private OnDateSetListener mStartDateSetListener = new OnDateSetListener() {

		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			editing_calendar.set(Calendar.YEAR, year);
			editing_calendar.set(Calendar.MONTH, monthOfYear);
			editing_calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
			updateDates();
		}
	};

	@Override
	protected Dialog onCreateDialog(int id) {
		if(id == START_TIME_DIALOG || id == START_DATE_DIALOG)
			editing_calendar = calendar_start;
		else
			editing_calendar = calendar_end;
		
		if(id == START_TIME_DIALOG || id == END_TIME_DIALOG) {
			return new TimePickerDialog(this, mStartTimeSetListener,
					editing_calendar.get(Calendar.HOUR_OF_DAY),
					editing_calendar.get(Calendar.MINUTE), false);
		}
		
		else if(id == START_DATE_DIALOG || id == END_DATE_DIALOG) {
			return new DatePickerDialog(this, mStartDateSetListener,
					editing_calendar.get(Calendar.YEAR),
					editing_calendar.get(Calendar.MONTH),
					editing_calendar.get(Calendar.DAY_OF_MONTH));
		}
		else
			return null;
	}

}
