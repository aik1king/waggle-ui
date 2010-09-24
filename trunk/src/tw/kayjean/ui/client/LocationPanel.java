package tw.kayjean.ui.client;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import com.google.gwt.maps.client.overlay.Overlay;

import com.allen_sauer.gwt.dnd.client.DragController;
import com.allen_sauer.gwt.dnd.client.DragContext;
import com.allen_sauer.gwt.dnd.client.DragEndEvent;
import com.allen_sauer.gwt.dnd.client.DragHandler;
import com.allen_sauer.gwt.dnd.client.DragStartEvent;
import com.allen_sauer.gwt.dnd.client.HasDragHandle;
import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.VetoDragException;
import com.allen_sauer.gwt.dnd.client.drop.IndexedDropController;
import com.allen_sauer.gwt.dnd.client.drop.SimpleDropController;


import tw.kayjean.ui.client.rpc.CoordinateCallback;
import tw.kayjean.ui.client.rpc.TastingCallback;

public class LocationPanel extends Composite {

    protected RouteFlow routeFlow = new RouteFlow(); // Holds LocationEntrys
    private VerticalPanel routeTab = new VerticalPanel(); // Holds frame of routeFlow

    private static final int MAXLOCS = 10;
    private int currHighlight = -1;

    private Label title = new Label("Your Route (Is Empty)");

    /**
     * {GMarker => Node.name}
     * Right now, this is used for accessing node names, which are necessary
     * for accessing flash objects at the time of a mouse click on a marker.
     */
    public Map markNameMap = new HashMap();

    /**
     * Mapping integers (starting Location index) => GOverlayCollections
     */
    private Map overlaysMap = new HashMap();

    public LocationPanel() {
        title.setStyleName("tabTitle");
        routeTab.add(title);
        routeTab.add(routeFlow);

        initWidget(routeTab);
        this.setStyleName("locationPanel");

        routeFlow.setVisible(false);
    }

    /**
     * Void all effects done by users in hiding/showing routes and whatnot.
     */
    public void ensurePanelConsistency() {
        routeFlow.ensureLocUIConsistency();
    }

    /**
     * Runs JSNI call to pull the GET header, and populates a route.
     */
/*    
    public void loadGETLocations() {
        String getlocs = parseGET("loc");
        if (getlocs == null || getlocs.equals("")) { return; }

        String[] locArray = getlocs.split(",");

        // Go through the array, verifying each location and then adding to our structures
        for (int i = 0; i < locArray.length; i++) {

            // Bring it down to our style, and look for matches
            locArray[i] = locArray[i].replaceAll("%20", " ").replaceAll("_"," ").toLowerCase();
            //locMatches = WUF.inPanel.textBox.getCompletionItems(locArray[i]);

            //for (int j = 0; j < locMatches.length; j++) {
                // For each match, bring it to lower case and see if it matches
            
//delete            String result = WUF.inPanel.lowercaseMatch(locArray[i]);
//delete                if (result != null) {
//delete                    LocationEntry newloc = new LocationEntry(result);
                    LocationEntry newloc = new LocationEntry(locArray[i]);
                    routeFlow.add(newloc); // Add to display
//delete                }
         //   }
        }

        // Do SOMETHING with the map
        if (routeFlow.size() > 0) {
            openRouteBar();
            WUF.selectRouteTab();
            
            if (routeFlow.dragElements.getWidgetCount() == 1) {
                signalMaptoDrawCoordinate(routeFlow.getLocString(0));
            }
            else if (routeFlow.size() > 1) {
                signalMaptoDrawRoute();
            }
            
        }
    }
*/
    
    // Overloaded: default is that the location is input using the textbox
    public void addLocation(final String loc) {
        addLocation(loc, true);
    }

    /**
     * Will add a new location to the current structures, possibly triggering an
     * auto-routing.
     * @param loc String of the location to be entered. Will be checked!
     */
    public void checkpointinbox(){
    	//table中每一個點都送入map中,檢查是否符合範圍
        LocationEntry tmpEntry;
        for (int i = routeFlow.size() - 1 ; i >= 0 ; i--) {
            tmpEntry = routeFlow.getEntry(i);
            if( !WUF.mPanel.pointinbox(tmpEntry.y, tmpEntry.x) ){
            	routeFlow.remove(tmpEntry);
            	WUF.mPanel.RemoveOverlay( tmpEntry.name );
            }
        }
    	return;
    }
    
    public void addLocation(final String loc, double x , double y , String geocell ) {
    	
    	//先進行確認,是否存在,不存在才要繼續
    	if( routeFlow.checkexist( loc ) == false ){
            // We assume that we're really going to add a location now
    		// 0 means location , 1 means favorite
            LocationEntry newloc = new LocationEntry(loc , x , y , 0 , geocell );
            routeFlow.add(newloc); // Add to display
            //如果目前正在看自己TAB,就在地圖加入點
            if( WUF.currentselect == 0 )
            	WUF.mPanel.AddOverlay(y, x, loc , 0 );
            openRouteBar();
    	}
    }
    
    public void addLocation(final String loc, boolean byTextBox) {
        if (byTextBox) {
            if (loc.equals("") || !WUF.inPanel.isValidLocation(loc)) {
            	WUF.message("You did not type a valid location. Try pressing Enter or Tab before finishing, to autocomplete!");
//delete                WUF.inPanel.shakeIt();
                return;
            }
        }

        // Size limit enforced here
        if (routeFlow.size() >= MAXLOCS) {
        	WUF.message("Sorry, we limit the route to " + MAXLOCS + " locations for efficiency.");
            return;
        }

        // We assume that we're really going to add a location now
        LocationEntry newloc = new LocationEntry(loc);

        routeFlow.add(newloc); // Add to display

        // Scriptaculous!
//delete        Effects.Effect("Pulsate",newloc);

        
        if (routeFlow.size() == 1) {
            // First location? Put a marker and pan to it.
            signalMaptoDrawCoordinate(loc);
            WUF.message("Add more locations and a route will be generated auto-magically.");
            openRouteBar();
        }
        else { // location.size() should be greater than one by now 
            signalMaptoDrawRoute();
        }

        
        WUF.selectRouteTab();
    }

    /**
     * Takes a location out of our structures, possibly triggering an
     * auto-rerouting.
     * @param loc String of location to remove.
     */
    public void removeLocation(final LocationEntry loc) {

        /** Actually remove it, Scriptaculously! This actually doesn't work due to the fact that we're still using tables.
        Effects.Effect("DropOut", loc, "{ sync:true, duration: 3.0 }").addEffectListener(
                new Effects.EffectListenerAdapter() { 
                    public void onAfterFinish(Effect sender) { 
                        routeFlow.remove(loc); 
                    } });           **/
        routeFlow.remove(loc); // Just in case the above didn't finish.

        // Possible auto-reroute
        if (routeFlow.size() > 1) {
            signalMaptoDrawRoute();
        }
        else {
            // We must have 1 or none left
            WUF.clearMap();
            if (routeFlow.size() == 1) {
                signalMaptoDrawCoordinate(routeFlow.getLocString(0));
                WUF.message("Add more locations, and routes will be generated automatically.");
            }
            // Possible hiding of the display
            else if (routeFlow.size() < 1) {
                closeRouteBar();
                WUF.mPanel.recenter();
            }
        }
    }

    /**
     * Hides the route bar, which implicitly means that all locations are gone.
     * Thus, just to be safe, clears the data structures again.
     */
    private void closeRouteBar() {
        routeFlow.clear();
        routeFlow.setVisible(false);
        title.setText("Your Route (Is Empty)");
        WUF.message("There are currently no locations on the map. Please start typing one.");
    }

    /**
     * Shows the route bar, and tries pitifully to try to make it scrollable. ensures that we're on this tab, too.
     */
    private void openRouteBar() {
        routeFlow.setVisible(true);
        title.setText("Your Route");
    }

    /**
     * Make the RPC call to the server for a coordinate marker!
     * 
     * @param locName String name of the location to be marked
     */
    public void signalMaptoDrawCoordinate(String locName) {
        WUF.startSpin();
        WUF.isMakingServerCall = true;
        WUF.clearMap();
//delete        toggleSOCheckBoxes(false);	// disable checkboxes until the callback completes

//        Waggle_ui.coordService.getNode(locName, new CoordinateCallback());
    }

    /**
     * Make the RPC call to the server for a route!
     */
    public void signalMaptoDrawRoute() {
        GWT.log("There are " + routeFlow.size() + " locations to plot",null);

        if (routeFlow.size() < 2) {
            GWT.log("Map is being signalled to draw route, with less than 2 locations.", new Throwable());
            WUF.message("Please start typing a location in the box above.");
            return;
        }

        WUF.startSpin();
        WUF.isMakingServerCall = true;
//delete        toggleSOCheckBoxes(false);	// disable checkboxes until the callback completes

        // This will clear all overlays, uncheck the POI boxes, and exit out of Map Mod Mode (if we were in it)
        WUF.clearMap();

        // (Re-)Initialize the markNodeMap
        markNameMap.clear();

        // Send all our locations over to the RoutingService!
//        Waggle_ui.tastingService.getTaste(
//                routeFlow.generateLocStringList(),
//                new TastingCallback());
    }

    public void addToOverlayMap(Integer index, Overlay olay) {
        if (!overlaysMap.containsKey(index)) {
            overlaysMap.put(index, new GOverlayCollection());
        }
        ((GOverlayCollection) overlaysMap.get(index)).add(olay);
    }

    public void clearOverlayMap() {
        overlaysMap.clear();
    }

    /**
     * Clears overlays on the GMap, and then puts a specific set of overlays on.
     * These overlays are the markers and polylines that compose the route from 
     * Location index "index" to Location index "index+1"
     * @param index The integer representing the index of routeFlow we want to start
     * this route from.
     */
/*    
 * �u��ܦU�Ӹ��|���Y�@�Ӭq��
    private void onlyShowRoute(int index) {
        // UI on the Map
        WUF.clearMap();
        // Index matches with RoutingCallback.locCount
        WUF.mPanel.theMap.addOverlay((GOverlayCollection) overlaysMap.get(new Integer(index)));

        // UI in the routelist
        for (int i = 0; i < routeFlow.size(); i++) {
            if (i != index && i!= index-1) {
                routeFlow.getEntry(i).addStyleName("faded"); // opacitize it
            } else {
                routeFlow.getEntry(i).removeStyleName("faded"); // unopacitize it
            }
        }
    }
*/
    
    /**
     * Clears the map, then draws each LocationEntrys route again.
     */
    
    private void showAllAgain() {
//        WUF.clearMap();
//        for (int i = 0; i < routeFlow.size(); i++) {
//            WUF.mPanel.AddOverlay(   ((GOverlayCollection)overlaysMap).get(new Integer(i))  );
//        }
        routeFlow.ensureLocUIConsistency();
    }
    
    /**
     * Enables all stopover checkboxes in the entire routeFlow if the boolean argument is true, 
     * and disables them if the argument is false. 
     * 
     * @param enable boolean
     */
/*    
    public void toggleSOCheckBoxes(boolean enable) {
        LocationEntry tmpEntry;
        for (int i = 0; i < routeFlow.size(); i++) {
            tmpEntry = routeFlow.getEntry(i);
            tmpEntry.passLab.setEnabled(enable);
            tmpEntry.passCafe.setEnabled(enable);
            tmpEntry.passLibrary.setEnabled(enable);
        }
    }
*/
    /**
     * JSNI method to parse the GET string and return it for mangling in Java.
     * @args name String that is the name of the GET var you want (can return many, comma delimited)
     */
    private native String parseGET(String name) /*-{
    get_string = $doc.location.search;         
    return_value = '';

    do { //This loop is made to catch all instances of any get variable.
        name_index = get_string.indexOf(name + '=');

        if(name_index != -1)
        {
            get_string = get_string.substr(name_index + name.length + 1, get_string.length - name_index);

            end_of_value = get_string.indexOf('&');
            if(end_of_value != -1)                
                value = get_string.substr(0, end_of_value);                
            else                
                value = get_string;                

            if(return_value == '' || value == '')
                return_value += value;
            else
                return_value += ',' + value;
        }
    } while(name_index != -1)

    //Restores all the blank spaces.
    space = return_value.indexOf('+');
    while(space != -1)
    { 
        return_value = return_value.substr(0, space) + ' ' + 
            return_value.substr(space + 1, return_value.length);

        space = return_value.indexOf('+');
    }

    return(return_value); 
  }-*/;
    
}