package net.ircubic.eventmap;

import java.io.Serializable;
import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class ConflictResolution extends ListActivity  {
	private ArrayList<FriendConflict> conflicts;
	private FriendConflictAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		final Intent data = getIntent();
		final Serializable x = data.getSerializableExtra("conflicts");
		conflicts = new ArrayList<FriendConflict>();
		if (x != null) {
			@SuppressWarnings("unchecked")
			final ArrayList<Long> ids = (ArrayList<Long>)x;
			
			final String where = String.format("%s IN (%s)",
					FriendProvider.KEY_ID, TextUtils.join(",", ids));
			final Cursor c = managedQuery(FriendProvider.CONTENT_URI, null,
					where, null, null);

			
			while (c.moveToNext()) {
				final Long id = c.getLong(0);
				final String name = c.getString(1);
				conflicts.add(new FriendConflict(id, name));
			}
			adapter = new FriendConflictAdapter(this, conflicts);
			setListAdapter(adapter);
		}
		
		super.onCreate(savedInstanceState);
	}

	public void x(View v)
	{
		Button removeButton = (Button) v;
		View parent = (View)removeButton.getParent();
		FriendConflict f = (FriendConflict)parent.getTag(R.id.conflict_position);
		f.dismissed = !f.dismissed;
		adapter.notifyDataSetChanged();
	}

	
}
