package tw.kayjean.ui.client.rpc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

import tw.kayjean.ui.client.WUF;

public class LocationsCallback implements AsyncCallback {

    public void onFailure(Throwable caught) {
        GWT.log("Error in Locations Service! ", caught);
        caught.printStackTrace();
    }

    public void onSuccess(Object result) {
        if (result == null) {
            GWT.log("No locations string Set returned in LocationsService!", null);
        }

        Map myLocations = (Map) result;
        
        // Sets up avgNodes collection to allow for inputting locations by clicking
        //delete WUF.mPanel.avgNodes = (Collection) myLocations.values();
        
        List locNames = new ArrayList(myLocations.keySet());
        Collections.sort(locNames);
        //CMF.inPanel.textBox.setCompletionItems(locNames);
        
        // GO! Try to populate textBox2
        WUF.inPanel.populateSuggestions(myLocations.keySet());
        
        // Pre-populate a route, if necessary.
        // Note that this is done here because it DEPENDS on the locations being populated already.
        // Yes, this makes this service a one-time thing.
        
        //加入先前CGI內容
        //不過現在沒有CGI,絕對不要啟動這裡
        //WUF.locPanel.loadGETLocations();
    }

}