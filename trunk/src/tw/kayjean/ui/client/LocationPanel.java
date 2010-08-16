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
            
//�H�e�O�i��ROUTE�p��,�ҥH�|���o�ӻݭn
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
    
    public void addLocation(final String loc, double x , double y ) {
    	
    	//先進行確認,是否存在,不存在才要繼續
    	if( routeFlow.checkexist( loc ) == false ){
            // We assume that we're really going to add a location now
            LocationEntry newloc = new LocationEntry(loc , x , y );
            routeFlow.add(newloc); // Add to display
            //加入地圖的點
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

    /**
     * For the Clear All button
     */
    private class ClearAllListener implements ClickListener {
        public void onClick(Widget sender) {
            closeRouteBar();
            WUF.clearMap();
            WUF.mPanel.recenter();
        }
    }


    
    /**
     * Inner class representing one element of the routeFlow.
     * Contains both a label (name) of the Location, and a delete button for it, plus extras.
     * @author Simon
     */
    public class LocationEntry extends Composite implements ClickListener,HasDragHandle {

        private PushButton bDelete = new PushButton(new Image("images/button_remove.png"),this);
        private PushButton bDetails = new PushButton(new Image("images/button_details.png"),this);
        private ToggleButton bShowOnly = new ToggleButton(new Image("images/button_eye.gif"),this);
        private Label loctext;
        protected String name;
        protected double x;
        protected double y;

        // Stopover options for *after* this Location
    //delete    private StopoversSet stopovers = new StopoversSet();
        private Panel stopoversPanel;
        private HTML soClick;
        private VerticalPanel exts;

        // In buildStopOverOptions()
        private CheckBox passCafe = new CheckBox("place to eat");
        private CheckBox passLibrary = new CheckBox("library");
        private CheckBox passLab = new CheckBox("Computer Lab (students only)"); 

        private int myIndex;

        public LocationEntry(final String inName , double inx , double iny ) {
            name = inName;
            x = inx;
            y = iny;
            VerticalPanel contents = new VerticalPanel();

            contents.add(buildLabel());
            contents.add(buildExtras());
            stopoversPanel.setVisible(false);
            //hideExtras();

            initWidget(contents);
            this.setStyleName("locationEntry");
        }
        
        public LocationEntry(final String inName) {
            name = inName;
            VerticalPanel contents = new VerticalPanel();

            contents.add(buildLabel());
            contents.add(buildExtras());
            stopoversPanel.setVisible(false);
            //hideExtras();

            initWidget(contents);
            this.setStyleName("locationEntry");
        }

        /**
         * Builds the basic part of a LocationEntry.
         * Namely, the label of its name and a delete button.
         * In the old days, this was all there was to it.
         * @return ColumnPanel of old
         */
        private HorizontalPanel buildLabel() {
            HorizontalPanel p = new HorizontalPanel();
            loctext = new Label(name);
            
            HorizontalPanel buttons = new HorizontalPanel();
            buttons.add(bDetails);
            buttons.add(bDelete);

            p.add(loctext);
            p.add(buttons);

            bDelete.setTitle("Click to delete \"" + name + "\"");
            bDetails.setTitle("Click to show details about \"" + name + "\"");

            //p.setCellVerticalAlignment(loctext, HasVerticalAlignment.ALIGN_MIDDLE);
            //p.setCellHorizontalAlignment(bDelete, HasHorizontalAlignment.ALIGN_RIGHT);
            p.setStyleName("label");
            loctext.setStyleName("text");
            buttons.addStyleName("buttons");
            return p;
        }

        private VerticalPanel buildExtras() {
            exts = new VerticalPanel();
            HorizontalPanel extsBaseRow = new HorizontalPanel();

            soClick = new HTML("<div class=\"togglelink\">set stopovers</div>");
            soClick.setTitle("Click to add some stopovers in between this location and the next.");
            soClick.addClickListener(this);

            bShowOnly.setTitle("Click to hide all route segments except this one! Click again to restore.");

            stopoversPanel = buildStopOvers();
            
            extsBaseRow.add(bShowOnly);
            extsBaseRow.add(soClick);

            //extsBaseRow.setCellHorizontalAlignment(soClick, HasHorizontalAlignment.ALIGN_RIGHT);

            exts.add(extsBaseRow);
            exts.add(stopoversPanel);

            exts.setStyleName("extras");
            extsBaseRow.setStyleName("base_row");
            bShowOnly.setStyleName("eye");
            stopoversPanel.setStyleName("stopovers");
            return exts;
        }

        protected void hideExtras() {
            exts.setVisible(false);
        }

        protected void showExtras() {
            exts.setVisible(true);
        }

        /**
         * 
         * @return stopovers to requirements so that there is no spacing for these elements
         */
        private VerticalPanel buildStopOvers() {
            VerticalPanel contents = new VerticalPanel();
            Label stopoverLabel = new Label("After " + name + ", stop by a:");

            VerticalPanel stopBoxes = new VerticalPanel();
            stopBoxes.add(passCafe);        
            stopBoxes.add(passLibrary);
            stopBoxes.add(passLab); 
            //stopBoxes.setStyleName("indented-block");

            passCafe.addClickListener(this);
            passLibrary.addClickListener(this);
            passLab.addClickListener(this);

            contents.add(stopoverLabel);
            contents.add(stopBoxes);

            return contents;
        }

        /**
         * Expands the extras panel
         */
        private void toggleExtras() {
        	//顯示完整HTML
        	//重覆點選,可以造成打開,關閉效果
            stopoversPanel.setVisible(!stopoversPanel.isVisible());
        }

        /**
         * Resets the stopovers required after this Location
         */
        protected void resetOptions() {
            passCafe.setChecked(false);
            passLab.setChecked(false);
            passLibrary.setChecked(false);
        }

        /**
         * Use this method to assign a number to be visible on the location entry bar
         * @param index The number you want assigned.
         */
        protected void setIndex(int index) {
            myIndex = index;
            loctext.setText(myIndex+1 + ") " + this.name);
        }

        /**
         * Generates and returns a StopoversSet object representing
         * this LocationEntry's stopover selection
         * @return StopoverSet
         */
    /*delete    
        protected StopoversSet getStopoversSet() {
            stopovers.passCafe = passCafe.isChecked();
            stopovers.passLab = passLab.isChecked();
            stopovers.passLib = passLibrary.isChecked();
            return stopovers;
        }
    */
        
        /**
         * Catches clicks on the delete button and more
         */
        public void onClick(final Widget sender) {
            if (sender == bDelete) {
                // When the button is hit, remove ourselves from the list
            	removeLocation(this);
            }
            else if (sender == bDetails) {
                WUF.setSWF(this.name);
            }
            else if (sender == soClick) {
                toggleExtras();
            }
            else if (sender == bShowOnly) {
                if (currHighlight == myIndex) {
                    showAllAgain();
                } else {
                    currHighlight = myIndex; // Set the currently highlighted
//delete                    onlyShowRoute(myIndex+1);
                }
            }
            else if (sender == passLab ||
                    sender == passCafe ||
                    sender == passLibrary)
                signalMaptoDrawRoute();
        }

        /**
         * What region can we drag on?
         */
        public Widget getDragHandle() {
            return loctext;
        }

    }
 
    /**
     * Our routelist, where elements are draggable!
     * Notice that the use of the RouteFlow class negates the need
     * for a separate location string List to be maintained.
     * Anything you wanted out of a location string list, call a method
     * inside this class!
     */
    protected class RouteFlow extends Composite {

        VerticalPanel dragElements = new VerticalPanel();
        PickupDragController widgetDragController;

        public RouteFlow() {
            VerticalPanel routeAndTrash = new VerticalPanel();

            // Clear all button
            HTML clearAll = new HTML("<div id=\"clear_all\"></div>");
            clearAll.setTitle("Click to clear all locations from this list, or you can drag locations here to delete them as an alternative to clicking the red buttons!");
            clearAll.addClickListener(new ClearAllListener());

            // Define a boundary for the dragging of elements
            AbsolutePanel boundaryPanel = new AbsolutePanel();
            boundaryPanel.add(dragElements);
            
            // Combine Trash and Route
            routeAndTrash.add(clearAll);
            routeAndTrash.add(boundaryPanel);

            // Create new controllers and tie them, plus the dragElements, together
            widgetDragController = new PickupDragController(boundaryPanel, true);
            widgetDragController.addDragHandler(new LocationDragHandler());
            IndexedDropController widgetDropController = new IndexedDropController(dragElements);
            TrashBinDropController trashDropController = new TrashBinDropController(clearAll);
            widgetDragController.registerDropController(widgetDropController);
            widgetDragController.registerDropController(trashDropController);

            initWidget(routeAndTrash);
            
            // Set Styles
            this.setStyleName("routeflow");
            routeAndTrash.setStyleName("routeandtrash");
            boundaryPanel.setStyleName("dragarea");
            //dragElements.setStyleName("routelist");
        }

        /**
         * Add LocationEntries via this method.
         * @param le New LocationEntry object
         */
        protected void add (LocationEntry le) {
            widgetDragController.makeDraggable(le);
            dragElements.add(le);
            //準備使用這個,加在正確位置 dragElements.insert(w, beforeIndex)
        }

        /**
         * Remove LocationEntries via this method.
         * @param le LocationEntry object to delete
         */
        protected void remove (LocationEntry le) {
            dragElements.remove(le);
        }

        /**
         * Clear the entire routeFlow.
         */        
        protected void clear() {
            dragElements.clear();
        }

        /**
         * Get the size of the verticalpanel.
         * In other words, how many LocationEntries do we have?
         * @return int number of LocationEntry objects we have
         */
        protected int size() {
            return dragElements.getWidgetCount();
        }

        /**
         * Given an index into the VerticalPanel, return the string
         * of the Location that the LocationEntry represents.
         * @param index
         * @return String of Location at index
         */        
        protected String getLocString(int index) {
            return ((LocationEntry) dragElements.getWidget(index)).name;
        }

    /*delete    
        private StopoversSet getStopoversSet(int index) {
            return ((LocationEntry) dragElements.getWidget(index)).getStopoversSet();
        }
    */

        private LocationEntry getEntry(int index) {
            return ((LocationEntry) dragElements.getWidget(index));
        }
        
        protected boolean checkexist( String cname ) {
            for (int i = 0; i < this.size(); i++) {
                String name = this.getLocString(i);
                //rank之下的就不需要重新計算了
            	//因為每一個一定是依照重要性排序的
            	//所以只要比對到目前排序最後一個就可以了
                int rank = 3;
                if( cname.equalsIgnoreCase(name) )
                	return true;
            }
            return false;
        }
        
        /**
         * Generates an entire List object of Location Strings.
         * For use when calling the RoutingService.
         * @return List
         */
        protected List generateLocStringList() {
            List locs = new Vector();
            for (int i = 0; i < this.size(); i++) {
                locs.add(this.getLocString(i));
            }
            return locs;
        }

    /*delete    
        protected List generateStopoversChangeList() {
            List sSets = new Vector();
            // Pull a stopovers set from each LocationEntry
            for (int i = 0; i < this.size(); i++) {
                sSets.add(this.getStopoversSet(i));
            }
            return sSets;
        }
    */
        
        /**
         * Goes through the LocationEntrys of this RouteFlow.
         * Ensures that the trigger for stopovers is shown or not, depending on index
         * Also goes and labels each of the LocationEntrys so you know which number they are
         * Also undos hiding of LocationEntrys (resetting style)
         * Resets currHighlight to none of the LocationEntrys
         */
        protected void ensureLocUIConsistency() {

            currHighlight = -1;

            // Go through each LocationEntry in routeFlow, making sure they look okay
            for (int i = 0; i < this.size(); i++) {

                // Sets index instance var, and changes Label on screen
                this.getEntry(i).setIndex(i);

                // Resets style
                this.getEntry(i).setStyleName("locationEntry");
                this.getEntry(i).exts.setStyleName("extras");

                // Ensure stopovers trigger consistency
//                this.getEntry(i).showExtras();
//                if (i == this.size()-1) {
//                    this.getEntry(i).hideExtras();
//                }

            }
//delete    Effects.Effect("Highlight",dragElements);
        }

        /**
         * Inner class of RouteFlow that controls what happens on drag movements.
         */
        private class LocationDragHandler implements DragHandler {
            public void onDragEnd(DragEndEvent event) {
                signalMaptoDrawRoute();
            }
            public void onDragStart(DragStartEvent event) {}
            public void onPreviewDragEnd(DragEndEvent event) throws VetoDragException {}
            public void onPreviewDragStart(DragStartEvent event) throws VetoDragException {}
        }

        /**
         * This class controls the drop behavior the recycle bin.
         * It simply calls removeLocation when a LocationEntry is dropped onto it.
         * @author Simon
         */
        private class TrashBinDropController extends SimpleDropController {

            public TrashBinDropController(Widget dropTarget) {
                super(dropTarget);
            }

/*            
            public DragEndEvent onDrop(Widget reference, Widget draggable, DragController dragController) {
                DragEndEvent event = super.onDrop(reference, draggable, dragController);
                removeLocation((LocationEntry) draggable);
                return event;
            }

            public void onEnter(Widget reference, Widget draggable, DragController dragController) {
                super.onEnter(reference, draggable, dragController);
            }

            public void onLeave(Widget draggable, DragController dragController) {
                super.onLeave(draggable, dragController);
            }

            public void onPreviewDrop(Widget reference, Widget draggable, DragController dragController) throws VetoDropException {
                super.onPreviewDrop(reference, draggable, dragController);
            }
*/            
            
            public void onDrop(DragContext context) {
                super.onDrop(context);
                removeLocation((LocationEntry)(context.draggable));
            }

            public void onEnter(DragContext context) {
                super.onEnter(context);
            }

            public void onLeave(DragContext context) {
                super.onLeave(context);
            }
        }
    }
    
}