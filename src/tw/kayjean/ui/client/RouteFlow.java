package tw.kayjean.ui.client;

import java.util.List;
import java.util.Vector;

//import tw.kayjean.ui.client.LocationPanel.ClearAllListener;
//import tw.kayjean.ui.client.LocationPanel.RouteFlow.LocationDragHandler;
//import tw.kayjean.ui.client.LocationPanel.RouteFlow.TrashBinDropController;

import com.allen_sauer.gwt.dnd.client.DragContext;
import com.allen_sauer.gwt.dnd.client.DragEndEvent;
import com.allen_sauer.gwt.dnd.client.DragHandler;
import com.allen_sauer.gwt.dnd.client.DragStartEvent;
import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.VetoDragException;
import com.allen_sauer.gwt.dnd.client.drop.IndexedDropController;
import com.allen_sauer.gwt.dnd.client.drop.SimpleDropController;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Our routelist, where elements are draggable!
 * Notice that the use of the RouteFlow class negates the need
 * for a separate location string List to be maintained.
 * Anything you wanted out of a location string list, call a method
 * inside this class!
 */
public class RouteFlow extends Composite {

    VerticalPanel dragElements = new VerticalPanel();
    PickupDragController widgetDragController;
    private int myType;

    /**
     * For the Clear All button
     */
    private class ClearAllListener implements ClickListener {
        public void onClick(Widget sender) {
//            closeRouteBar();
            WUF.clearMap();
            WUF.mPanel.recenter();
        }
    }
    
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

    public LocationEntry getEntry(int index) {
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

//        currHighlight = -1;

        // Go through each LocationEntry in routeFlow, making sure they look okay
        for (int i = 0; i < this.size(); i++) {

            // Sets index instance var, and changes Label on screen
            this.getEntry(i).setIndex(i);

            // Resets style
            this.getEntry(i).setStyleName("locationEntry");
//kayjean                this.getEntry(i).exts.setStyleName("extras");

            // Ensure stopovers trigger consistency
//            this.getEntry(i).showExtras();
//            if (i == this.size()-1) {
//                this.getEntry(i).hideExtras();
//            }

        }
//delete    Effects.Effect("Highlight",dragElements);
    }

    /**
     * Inner class of RouteFlow that controls what happens on drag movements.
     */
    private class LocationDragHandler implements DragHandler {
        public void onDragEnd(DragEndEvent event) {
//            signalMaptoDrawRoute();
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
            WUF.removeLocation( myType , (LocationEntry)(context.draggable));
        }

        public void onEnter(DragContext context) {
            super.onEnter(context);
        }

        public void onLeave(DragContext context) {
            super.onLeave(context);
        }
    }
}
