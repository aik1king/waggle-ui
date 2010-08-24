package tw.kayjean.ui.client.rpc;

import tw.kayjean.ui.client.model.Node;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.IsSerializable;

public interface CoordinateServiceAsync {
	public void getNode(String s, AsyncCallback callback);
	public void getIPLocation(AsyncCallback callback);
	public void getRTree ( String username , String name , AsyncCallback callback);
	public void sendNode ( String username , int type , String name , double x , double y , String geocell , AsyncCallback callback );
	public void getLocations(String prefix, int limit,AsyncCallback callback);
}
