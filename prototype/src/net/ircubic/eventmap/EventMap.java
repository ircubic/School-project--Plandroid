package net.ircubic.eventmap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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
		startActivity(intent);

	}
}
