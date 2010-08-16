package tw.kayjean.ui.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.user.client.rpc.AsyncCallback;

import tw.kayjean.ui.client.rpc.CoordinateService;
import tw.kayjean.ui.client.rpc.CoordinateServiceAsync;

public class DataSwitch implements CoordinateServiceAsync {

	private final Map<String, Object> cache = new HashMap<String, Object>();

	private static DataSwitch dataSwitch = null;
	
	  public static DataSwitch get() {
		    if (dataSwitch == null) {
		      dataSwitch = new DataSwitch();
		    }
		    return dataSwitch;
		  }

	  private DataSwitch() {
		    // Pings the server every 5 seconds and clears the cache if an update
		    // occurred:

		  }
	
	public void getNode(String s, final AsyncCallback cb) {
		
		Waggle_ui.coordService.getNode(s, new AsyncCallback<Integer>() {
              public void onFailure(Throwable caught) {
                cb.onFailure(caught);
              }

              public void onSuccess(Integer result) {
                cb.onSuccess(result);
              }
        }
		);
	  }
	
	public void getIPLocation( final AsyncCallback cb ){
		
		Waggle_ui.coordService.getIPLocation( new AsyncCallback<String>() {
            public void onFailure(Throwable caught) {
              cb.onFailure(caught);
            }

            public void onSuccess(String result) {
              cb.onSuccess(result);
            }
      }
		);
		
	}
	
	public void getRTree ( String name , final AsyncCallback cb){

		final String QueryKey = name;
        List cachResult = (List) cache.get(QueryKey);
        if (cachResult != null) {
            cb.onSuccess(cachResult);
            return;
        }
		Waggle_ui.coordService.getRTree(name , new AsyncCallback<List>() {
            public void onFailure(Throwable caught) {
              cb.onFailure(caught);
            }

            public void onSuccess(List result) {
            	cache.put(QueryKey, result);
              cb.onSuccess(result);
            }
      }
		);
		
	}
	
}
