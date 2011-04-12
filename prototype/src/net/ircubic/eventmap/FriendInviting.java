package net.ircubic.eventmap;

import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class FriendInviting extends ListActivity {

	public static final int INVITE = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Cursor c = managedQuery(FriendProvider.CONTENT_URI, null, null, null, null);
		String from[] = {FriendProvider.KEY_NAME};
		int to[] = {R.id.friendName};
		SimpleCursorAdapter ca = new SimpleCursorAdapter(this, R.layout.friend_row, c, from, to);
		/*Button saveButton = new Button(this);
		saveButton.setText("Invite");
		getListView().addFooterView(saveButton);*/
		setListAdapter(ca);
		getListView().setItemsCanFocus(false);
		getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
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
