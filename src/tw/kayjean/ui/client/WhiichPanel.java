package tw.kayjean.ui.client;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.maps.client.overlay.Overlay;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

import tw.kayjean.ui.client.model.Node;
import tw.kayjean.ui.client.rpc.CoordinateSendCallback;

public class WhiichPanel extends Composite {

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

    public WhiichPanel() {
        title.setStyleName("tabTitle");
        routeTab.add(title);
        routeTab.add(routeFlow);

        initWidget(routeTab);
        this.setStyleName("whiichPanel");

        routeFlow.setVisible(false);
    }

    /**
     * Void all effects done by users in hiding/showing routes and whatnot.
     */
    public void ensurePanelConsistency() {
        routeFlow.ensureLocUIConsistency();
    }

    // Overloaded: default is that the location is input using the textbox
    public void addWhiich(final String loc) {
        addWhiich(loc, true);
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
            if( !WUF.mPanel.pointinbox(tmpEntry.n.y, tmpEntry.n.x) ){
            	routeFlow.remove(tmpEntry);
            	WUF.mPanel.RemoveOverlay( tmpEntry.n.name );
            }
        }
    	return;
    }
    
    public void addWhiich(final Node n ) {
    	
    	//先進行確認,是否存在,不存在才要繼續
    	if( routeFlow.checkexist( n.name ) == false ){
            // We assume that we're really going to add a location now
    		// 0 means location , 1 means favorite , 2 means error , 3 means whiich
            LocationEntry newloc = new LocationEntry( n.name , n.fullname , n.x , n.y , 1 , n.geocell );
            routeFlow.add(newloc); // Add to display
            //加入地圖的點
            if( WUF.currentselect == 3 )
            	WUF.mPanel.AddOverlay(n.y, n.x, n.name , 3 );
            openRouteBar();
    	}
    }
    
    public void addWhiich(final String loc, boolean byTextBox) {
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
    public void closeRouteBar() {
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
     * Clears the map, then draws each LocationEntrys route again.
     */
    
    private void showAllAgain() {
//        WUF.clearMap();
//        for (int i = 0; i < routeFlow.size(); i++) {
//            WUF.mPanel.AddOverlay(   ((GOverlayCollection)overlaysMap).get(new Integer(i))  );
//        }
        routeFlow.ensureLocUIConsistency();
    }
    
}