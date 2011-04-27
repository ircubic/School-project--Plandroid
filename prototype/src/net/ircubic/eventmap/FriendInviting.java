package net.ircubic.eventmap;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class FriendInviting extends ListActivity
{

	public static final int INVITE = 1;

	@Override
	protected void onCreate(final Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		final Cursor c = managedQuery(FriendProvider.CONTENT_URI, null, null,
				null, null);
		final String from[] = {FriendProvider.KEY_NAME};
		final int to[] = {R.id.friendName};
		final SimpleCursorAdapter ca = new SimpleCursorAdapter(this,
				R.layout.friend_row, c, from, to);

		final Button saveButton = new Button(this);
		saveButton.setText("Invite");
		saveButton.setOnClickListener(new OnClickListener() {

			public void onClick(final View v)
			{
				setInvitees();
				finish();
			}
		});
		getListView().addFooterView(saveButton);

		setListAdapter(ca);
		getListView().setItemsCanFocus(false);
		getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
	}

	protected void setInvitees()
	{
		final ListView v = getListView();
		final SparseBooleanArray checkstates = v.getCheckedItemPositions();
		final ArrayList<Long> invited_friends = new ArrayList<Long>();
		for (int i = 0; i < checkstates.size(); i++) {
			if (checkstates.valueAt(i) == true) {
				final long id = v.getAdapter().getItemId(checkstates.keyAt(i));
				invited_friends.add(id);
			}
		}
		final Intent i = new Intent();
		i.putExtra("invited", invited_friends);
		setResult(RESULT_OK, i);
	}

	@Override
	protected void onPause()
	{
		super.onPause();
	}

	@Override
	protected void onResume()
	{
		// TODO Auto-generated method stub
		super.onResume();
	}

}
