package net.ircubic.eventmap;

import java.util.ArrayList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
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
			ArrayList<FriendConflict> data) {
		mInflater = LayoutInflater.from(context);
		mData = (FriendConflict[])data.toArray(new FriendConflict[data.size()]);
	}

	public int getCount()
	{
		return mData.length;
	}

	public Object getItem(int arg0)
	{
		try {
			return mData[arg0];
		}
		catch (IndexOutOfBoundsException ex) {
			return null;
		}
	}

	public long getItemId(int position)
	{
		final FriendConflict item = mData[position];
		return item.id;
	}

	public View getView(int position, View convertView, ViewGroup parent)
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

			holder.removeButton.setOnClickListener(mRemoveListener);

			convertView.setTag(R.id.conflict_holder, holder);
		} else {
			holder = (ViewHolder)convertView.getTag(R.id.conflict_holder);
		}

		convertView.setTag(R.id.conflict_position, position);
		final FriendConflict friend = mData[position];
		holder.name.setText(friend.name);
		holder.message.setText(friend.message);

		if (friend.dismissed) {
			// TODO: Strike-out text
		}

		return convertView;
	}

	private OnClickListener mRemoveListener = new OnClickListener() {

		public void onClick(View v)
		{

			Integer position = (Integer)((View)v.getParent())
					.getTag(R.id.conflict_position);
			mData[position].dismissed = !mData[position].dismissed;
		}
	};

	static class ViewHolder
	{
		TextView name;
		EditText message;
		Button removeButton;
	}

}
