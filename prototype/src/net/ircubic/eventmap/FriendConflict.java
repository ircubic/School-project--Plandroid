package net.ircubic.eventmap;

public class FriendConflict
{
	public final String name;
	public final long id;
	public String message;
	public boolean dismissed;

	public FriendConflict(final long id, final String name) {
		this.id = id;
		this.name = name;
	}
}
