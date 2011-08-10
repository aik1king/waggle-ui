package tw.kayjean.ui.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.IsSerializable;

import tw.kayjean.ui.client.model.Node;
import tw.kayjean.ui.client.model.Poi;
import tw.kayjean.ui.client.model.FBDetail;
import tw.kayjean.ui.client.model.FBFriends;

public interface CoordinateServiceAsync {
	public void getNode(String s, AsyncCallback callback);
	public void getIPLocation(AsyncCallback callback);
	public void getRTree ( String username , String name , int type , AsyncCallback callback);
	public void sendNode ( Node nd , AsyncCallback callback );
	public void sendDetail ( FBDetail fd , FBFriends ffs , AsyncCallback callback );
//	public void getLocations(String prefix, int limit,AsyncCallback callback);
}
