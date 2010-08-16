package tw.kayjean.ui.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.IsSerializable;

public interface CoordinateServiceAsync {
	public void getNode(String s, AsyncCallback callback);
	public void getIPLocation(AsyncCallback callback);
	public void getRTree ( String name , AsyncCallback callback);
}
