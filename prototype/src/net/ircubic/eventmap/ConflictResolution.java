package net.ircubic.eventmap;

import java.io.Serializable;
import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

public class ConflictResolution extends ListActivity
{
	private FriendConflictAdapter adapter;

	@Override
	protected void onCreate(final Bundle savedInstanceState)
	{
		final Intent data = getIntent();
		final Serializable x = data.getSerializableExtra("conflicts");
		final ArrayList<FriendConflict> conflicts = new ArrayList<FriendConflict>();

		if (x != null) {
			@SuppressWarnings("unchecked")
			final ArrayList<Long> ids = (ArrayList<Long>)x;
			if (ids.size() > 0) {
				final String where = String.format("%s IN (%s)",
						FriendProvider.KEY_ID, TextUtils.join(",", ids));
				final Cursor c = managedQuery(FriendProvider.CONTENT_URI, null,
						where, null, null);

				while (c.moveToNext()) {
					final Long id = c.getLong(0);
					final String name = c.getString(1);
					conflicts.add(new FriendConflict(id, name));
				}
			}
		}

		if (conflicts.size() == 0) {
			final Toast toast = Toast.makeText(getApplicationContext(),
					"Tried to handle a zero-conflict", Toast.LENGTH_LONG);
			toast.show();
			finish();
		}

		adapter = new FriendConflictAdapter(this, conflicts);
		setListAdapter(adapter);
		setTitle(R.string.title_resolve_conflict);

		super.onCreate(savedInstanceState);
	}

	public void removeClicked(final View v)
	{
		final ImageButton removeButton = (ImageButton)v;
		final View parent = (View)removeButton.getParent();
		final FriendConflict f = (FriendConflict)parent
				.getTag(R.id.conflict_position);
		f.dismissed = !f.dismissed;
		adapter.notifyDataSetChanged();
	}

}
