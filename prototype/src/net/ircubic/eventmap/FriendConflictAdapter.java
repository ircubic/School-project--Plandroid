package net.ircubic.eventmap;

import java.util.ArrayList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class FriendConflictAdapter extends BaseAdapter
{
	private final LayoutInflater mInflater;
	private final FriendConflict[] mData;

	public FriendConflictAdapter(final Activity context,
			final ArrayList<FriendConflict> data) {
		mInflater = LayoutInflater.from(context);
		mData = data.toArray(new FriendConflict[data.size()]);
	}

	public int getCount()
	{
		return mData.length;
	}

	public Object getItem(final int arg0)
	{
		try {
			return mData[arg0];
		}
		catch (final IndexOutOfBoundsException ex) {
			return null;
		}
	}

	public long getItemId(final int position)
	{
		final FriendConflict item = mData[position];
		return item.id;
	}

	public View getView(final int position, View convertView,
			final ViewGroup parent)
	{
		ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater
					.inflate(R.layout.friend_conflict_item, null);

			holder = new ViewHolder();
			holder.name = (TextView)convertView.findViewById(R.id.friendName);
			holder.message = (EditText)convertView
					.findViewById(R.id.conflictMessage);
			holder.removeButton = (Button)convertView
					.findViewById(R.id.removeConflict);

			convertView.setTag(R.id.conflict_holder, holder);
		} else {
			holder = (ViewHolder)convertView.getTag(R.id.conflict_holder);
		}

		final FriendConflict friend = mData[position];
		holder.name.setText(friend.name);
		holder.message.setText(friend.message);
		convertView.setTag(R.id.conflict_position, friend);

		if (friend.dismissed) {
			convertView.setBackgroundColor(0xFFFF0000);
		} else {
			convertView.setBackgroundColor(0x00000000);
		}

		return convertView;
	}

	static class ViewHolder
	{
		TextView name;
		EditText message;
		Button removeButton;
	}

}
