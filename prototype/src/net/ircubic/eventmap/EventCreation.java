package net.ircubic.eventmap;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TextView;

public class EventCreation extends Activity {

	private int start_year;
	private int start_month;
	private int start_day;
	
	private java.text.DateFormat df;
	
	private TextView start_time;
	private TextView end_time;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.event_creation);
		
		df = DateFormat.getDateFormat(getApplicationContext());
		start_time = (TextView)findViewById(R.id.editStartTime);
		end_time = (TextView)findViewById(R.id.editEndTime);
		
		final Calendar c = GregorianCalendar.getInstance();
		
		start_year = c.get(Calendar.YEAR);
		start_month = c.get(Calendar.MONTH);
		start_day = c.get(Calendar.DAY_OF_MONTH);
		
		updateDates();
	}

	private void updateDates() {
		Calendar d = GregorianCalendar.getInstance();
		String text = df.format(d.getTime());
		start_time.setText(text);
		end_time.setText(text);
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

	
}
