package net.ircubic.eventmap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class ConflictResolution extends ListActivity
{

	public static final int RESOLVE = 0;

	private FriendConflictAdapter adapter;
	private final ArrayList<FriendConflict> conflicts = new ArrayList<FriendConflict>();

	@Override
	protected void onCreate(final Bundle savedInstanceState)
	{
		final Intent data = getIntent();
		final Serializable x = data.getSerializableExtra("conflicts");

		if (x != null) {
			@SuppressWarnings("unchecked")
			final ArrayList<Long> ids = (ArrayList<Long>)x;
			if (ids.size() > 0) {
				final String where = String.format("%s IN (%s)",
						FriendProvider.KEY_ID, TextUtils.join(",", ids));
				final Cursor c = managedQuery(FriendProvider.CONTENT_URI, null,
						where, null, FriendProvider.KEY_NAME + " ASC");

				final String[] events = {"Wedding", "Birthday party",
						"Movie evening", "Dinner", "Lunch", "Get-together",
						"Going to nightclub", "Family night", "Game-night",
						"Date"};
				final Random rand = new Random();

				while (c.moveToNext()) {
					final Long id = c.getLong(0);
					final String name = c.getString(1);
					final String message = events[rand.nextInt(events.length)];
					conflicts.add(new FriendConflict(id, name, message));
				}
			}
		}

		if (conflicts.size() == 0) {
			final Toast toast = Toast.makeText(getApplicationContext(),
					"Tried to handle a zero-conflict", Toast.LENGTH_LONG);
			toast.show();
			finish();
		}

		final TextView desctext = new TextView(this);
		desctext.setText(R.string.conflict_description);
		getListView().addHeaderView(desctext);

		final Button finish = new Button(this);
		finish.setText(R.string.conflict_finish_button);
		finish.setOnClickListener(new OnClickListener() {

			public void onClick(final View v)
			{
				finishResolving();
			}
		});
		getListView().addFooterView(finish);

		adapter = new FriendConflictAdapter(this, conflicts);
		setListAdapter(adapter);
		setTitle(R.string.title_resolve_conflict);

		super.onCreate(savedInstanceState);
	}

	protected void finishResolving()
	{
		// TODO Finish resolving
		setResult(RESULT_OK);
		finish();

	}

	public void removeClicked(final View v)
	{
		final ImageButton removeButton = (ImageButton)v;
		final View parent = (View)removeButton.getParent();
		final FriendConflict f = (FriendConflict)parent
				.getTag(R.id.conflict_position);
		conflicts.remove(f);
		adapter.notifyDataSetChanged();
	}

}
