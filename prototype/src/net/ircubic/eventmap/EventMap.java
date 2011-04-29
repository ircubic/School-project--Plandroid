package net.ircubic.eventmap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

public class EventMap extends Activity
{
	/** Called when the activity is first created. */
	@Override
	public void onCreate(final Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		final Button startButton = (Button)findViewById(R.id.scenarioStartButton);
		startButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(final View v)
			{
				startScenario();

			}
		});
	}

	protected void startScenario()
	{
		final Intent intent = new Intent(this, EventCreation.class);
		final RadioGroup scenarios = (RadioGroup)findViewById(R.id.radioGroup1);
		switch (scenarios.getCheckedRadioButtonId()) {
		case R.id.radio_baseline:
			intent.putExtra("percentage", 0d);
			break;
		case R.id.radio_private_small:
			intent.putExtra("percentage", 0.21d);
			break;
		case R.id.radio_private_big:
			intent.putExtra("percentage", 0.61d);
			break;
		}
		startActivity(intent);
	}
}
