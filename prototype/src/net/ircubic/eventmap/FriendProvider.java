package net.ircubic.eventmap;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class FriendProvider extends ContentProvider
{

	public static final String KEY_NAME = "name";
	public static final String KEY_ID = "_id";

	private static final String AUTHORITY = "net.ircubic.eventmap.friendprovider";
	private static final String DATABASE_NAME = "data";
	private static final String DATABASE_TABLE = "friends";
	private static final String FRIENDS_TYPE = "vnd.android.cursor.dir/vnd.ircubic.friends";
	private static final String FRIEND_ID_TYPE = "vnd.android.cursor.item/vnd.ircubic.friend";
	private static final int FRIENDS = 1;
	private static final int FRIEND_ID = 2;

	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
			+ "/friends");

	private static final UriMatcher sUriMatcher;

	static {
		sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

		sUriMatcher.addURI(AUTHORITY, "friends", FRIENDS);

		sUriMatcher.addURI(AUTHORITY, "friends/#", FRIEND_ID);

	}

	private static class DBHelper extends SQLiteOpenHelper
	{

		public DBHelper(final Context context) {
			super(context, DATABASE_NAME, null, 1);
		}

		@Override
		public void onCreate(final SQLiteDatabase db)
		{
			db.execSQL(String
					.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT);",
							DATABASE_TABLE, KEY_ID, KEY_NAME));

			final String mock_vals[] = {"Case", "Armitage", "Molly Millions",
					"Maelcum", "Peter Riviera", "Wintermute", "Lady 3Jane",
					"Dixie Flatline", "Neuromancer"};

			final ContentValues values = new ContentValues();
			try {
				db.beginTransaction();
				for (final String val : mock_vals) {
					values.put(KEY_NAME, val);
					db.insert(DATABASE_TABLE, null, values);
					values.clear();
				}
				db.setTransactionSuccessful();
			}
			catch (final SQLException e) {}
			finally {
				db.endTransaction();
			}
		}

		@Override
		public void onUpgrade(final SQLiteDatabase db, final int oldVersion,
				final int newVersion)
		{
			db.execSQL(String.format("DROP TABLE IF EXISTS %s", DATABASE_TABLE));
			onCreate(db);
		}

	}

	private DBHelper mDBHelper;

	@Override
	public int delete(final Uri uri, final String selection,
			final String[] selectionArgs)
	{
		// STUB Not implemented, because unnecessary in prototype
		return 0;
	}

	@Override
	public String getType(final Uri uri)
	{
		switch (sUriMatcher.match(uri)) {
		case FRIENDS:
			return FRIENDS_TYPE;
		case FRIEND_ID:
			return FRIEND_ID_TYPE;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
	}

	@Override
	public Uri insert(final Uri uri, final ContentValues values)
	{
		if (sUriMatcher.match(uri) != FRIENDS) { throw new IllegalArgumentException(
				"Unknown URI " + uri); }

		final SQLiteDatabase db = mDBHelper.getWritableDatabase();
		final long row = db.insert(DATABASE_TABLE, KEY_NAME, values);
		if (row > 0) {
			final Uri newUri = ContentUris.withAppendedId(CONTENT_URI, row);
			getContext().getContentResolver().notifyChange(newUri, null);

			return newUri;
		} else {
			throw new SQLException("Failed to insert row into " + uri);
		}
	}

	@Override
	public boolean onCreate()
	{
		mDBHelper = new DBHelper(getContext());

		return true;
	}

	@Override
	public Cursor query(final Uri uri, final String[] projection,
			final String selection, final String[] selectionArgs,
			final String sortOrder)
	{
		final SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
		builder.setTables(DATABASE_TABLE);

		switch (sUriMatcher.match(uri)) {
		case FRIENDS:
			break;
		case FRIEND_ID:
			builder.appendWhere(KEY_ID + " = " + uri.getLastPathSegment());
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		final SQLiteDatabase db = mDBHelper.getWritableDatabase();
		final Cursor c = builder.query(db, projection, selection,
				selectionArgs, null, null, sortOrder);
		c.setNotificationUri(getContext().getContentResolver(), uri);
		return c;
	}

	@Override
	public int update(final Uri uri, final ContentValues values,
			final String selection, final String[] selectionArgs)
	{
		// STUB Not implemented, because unnecessary in prototype
		return 0;
	}

}
