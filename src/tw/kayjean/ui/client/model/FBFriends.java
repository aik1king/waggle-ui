package tw.kayjean.ui.client.model;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

public class FBFriends implements IsSerializable {
	public List<FBFriend> items;

	public FBFriend myself;
	public FBFriend trash;
	public FBFriend wish;
	
	public int me2friends;
	public int friends2me;
}
