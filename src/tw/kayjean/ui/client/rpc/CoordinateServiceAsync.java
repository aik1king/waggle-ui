package tw.kayjean.ui.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface CoordinateServiceAsync {
	public void getNode(String s, AsyncCallback callback);
	public void getIPLocation(AsyncCallback callback);
	public void getRTree (double w,double e,double s,double n, AsyncCallback callback);
}
