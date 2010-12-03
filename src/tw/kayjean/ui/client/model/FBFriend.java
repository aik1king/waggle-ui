package tw.kayjean.ui.client.model;

import com.google.gwt.user.client.rpc.IsSerializable;

/*
{"name":"Erhwei Piwen", "id":"100000124964790"},
{"name":"Jing-Kuang Tang", "id":"100000990330592"}
*/

public class FBFriend implements IsSerializable {
	public String id;
	public String name;

	public int item_me2friends;
	public int item_friends2me;
	
	public int me2friends;
	public int friends2me;
}
