package tw.kayjean.ui.client.rpc;

import java.util.HashMap;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface TastingServiceAsync {
    public void getTaste(List locations, AsyncCallback callback);
}
