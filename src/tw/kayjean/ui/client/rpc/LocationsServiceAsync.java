package tw.kayjean.ui.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface LocationsServiceAsync {
    public void getLocations(AsyncCallback callback);
    public void getLocations(String prefix, int limit,AsyncCallback callback);
}
