package net.ircubic.eventmap;

import java.io.Serializable;
import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;

public class ConflictResolution extends ListActivity {
	private ArrayList<FriendConflict> conflicts;
	
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
			
			setListAdapter(new FriendConflictAdapter(this, conflicts));
		}
		
		super.onCreate(savedInstanceState);
	}

}
