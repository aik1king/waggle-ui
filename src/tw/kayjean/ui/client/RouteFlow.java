package tw.kayjean.ui.client;

import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

import com.allen_sauer.gwt.dnd.client.DragContext;
import com.allen_sauer.gwt.dnd.client.DragEndEvent;
import com.allen_sauer.gwt.dnd.client.DragHandler;
import com.allen_sauer.gwt.dnd.client.DragStartEvent;
import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.VetoDragException;
import com.allen_sauer.gwt.dnd.client.drop.IndexedDropController;
import com.allen_sauer.gwt.dnd.client.drop.SimpleDropController;
import com.allen_sauer.gwt.dnd.client.drop.DropController;
import com.allen_sauer.gwt.dnd.client.PickupDragController;
//import com.allen_sauer.gwt.dnd.demo.client.DemoDragHandler;
//import com.allen_sauer.gwt.dnd.demo.client.example.DraggableFactory;
//import com.allen_sauer.gwt.dnd.demo.client.example.Example;


/**
 * Our routelist, where elements are draggable!
 * Notice that the use of the RouteFlow class negates the need
 * for a separate location string List to be maintained.
 * Anything you wanted out of a location string list, call a method
 * inside this class!
 */
public class RouteFlow extends Composite {

	//整個資料區塊位置,用這個表現
    VerticalPanel dragElements = new VerticalPanel();
    
    PickupDragController widgetDragController;

    //friends
    private static final int COLUMNS = 2;
    private static final int ROWS = 2;
    private static final int IMAGE_HEIGHT = 58;
    private static final int IMAGE_WIDTH = 65;
    
    public RouteFlow() {
        VerticalPanel routeAndTrash = new VerticalPanel();

        // Clear all button
        HTML clearAll = new HTML("<div id=\"clear_all\">TRASH</div>");
        clearAll.setTitle("Click to clear all locations from this list, or you can drag locations here to delete them as an alternative to clicking the red buttons!");
//        clearAll.addClickListener(new ClearAllListener());
        routeAndTrash.add(clearAll);
        //專門為了TRASH設計的動作
        TrashBinDropController trashDropController = new TrashBinDropController(clearAll);

        
        // Define a boundary for the dragging of elements
        AbsolutePanel boundaryPanel = new AbsolutePanel();
        boundaryPanel.add(dragElements);
        // Create new controllers and tie them, plus the dragElements, together
        //將一個實際區域和DRAG功能結合
        widgetDragController = new PickupDragController(boundaryPanel, true);
        widgetDragController.addDragHandler(new LocationDragHandler());
        routeAndTrash.add(boundaryPanel);
        
        //有排列順序型態
        IndexedDropController widgetDropController = new IndexedDropController(dragElements);
        

        //friends
        FlexTable flexTable = new FlexTable();
        // create our grid
        for (int i = 0; i < COLUMNS; i++) {
          for (int j = 0; j < ROWS; j++) {
            // create a simple panel drop target for the current cell
            SimplePanel simplePanel = new SimplePanel();
            simplePanel.setPixelSize(IMAGE_WIDTH, IMAGE_HEIGHT);
            flexTable.setWidget(i, j, simplePanel);
            //flexTable.getCellFormatter().setStyleName(i, j, CSS_DEMO_PUZZLE_CELL);

            // place a pumpkin in each panel in the cells in the first column
            //if (j == 0) {
            //  simplePanel.setWidget(createDraggable());
            //}
            Bin trashBin = new TrashBin(IMAGE_WIDTH, IMAGE_HEIGHT);
            simplePanel.setWidget( trashBin );
            BinDropController openTrashBinDropController = new BinDropController(trashBin);
            widgetDragController.registerDropController(openTrashBinDropController);
            
            // instantiate a drop controller of the panel in the current cell
            //SetWidgetDropController dropController = new SetWidgetDropController(simplePanel);
            //dragController.registerDropController(dropController);
          }
        }
        routeAndTrash.add(flexTable);
        

        widgetDragController.registerDropController(widgetDropController);
        widgetDragController.registerDropController(trashDropController);

        //生成
        initWidget(routeAndTrash);

        // Set Styles
        this.setStyleName("routeflow");
        routeAndTrash.setStyleName("routeandtrash");
        boundaryPanel.setStyleName("dragarea");
    }

    /**
     * For the Clear All button
     */
/*    
    private class ClearAllListener implements ClickListener {
        public void onClick(Widget sender) {
            WUF.clearMap();
            WUF.mPanel.recenter();
        }
    }
*/
    /**
     * Inner class of RouteFlow that controls what happens on drag movements.
     */
    //只是拉來拉去,沒有實際用處
    private class LocationDragHandler implements DragHandler {
        public void onDragEnd(DragEndEvent event) {
//            signalMaptoDrawRoute();
        }
        public void onDragStart(DragStartEvent event) {}
        public void onPreviewDragEnd(DragEndEvent event) throws VetoDragException {}
        public void onPreviewDragStart(DragStartEvent event) throws VetoDragException {}
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
        return ((LocationEntry) dragElements.getWidget(index)).n.name;
    }

    public LocationEntry getEntry(int index) {
        return ((LocationEntry) dragElements.getWidget(index));
    }

    protected boolean checkexist( String cname ) {
        for (int i = 0; i < this.size(); i++) {
            String name = this.getLocString(i);
            //rank之下的就不需要重新計算了
        	//因為每一個一定是依照重要性排序的
        	//所以只要比對到目前排序最後一個就可以了
            //int rank = 3;
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
/*    
    protected List generateLocStringList() {
        List locs = new Vector();
        for (int i = 0; i < this.size(); i++) {
            locs.add(this.getLocString(i));
        }
        return locs;
    }
*/
    
    /**
     * Goes through the LocationEntrys of this RouteFlow.
     * Ensures that the trigger for stopovers is shown or not, depending on index
     * Also goes and labels each of the LocationEntrys so you know which number they are
     * Also undos hiding of LocationEntrys (resetting style)
     * Resets currHighlight to none of the LocationEntrys
     */
/*    
    protected void ensureLocUIConsistency1() {

//        currHighlight = -1;

        // Go through each LocationEntry in routeFlow, making sure they look okay
        for (int i = 0; i < this.size(); i++) {

            // Sets index instance var, and changes Label on screen
            this.getEntry(i).setIndex(i);

            // Resets style
            this.getEntry(i).setStyleName("locationEntry");
        }
    }
*/

    /**
     * This class controls the drop behavior the recycle bin.
     * It simply calls removeLocation when a LocationEntry is dropped onto it.
     * @author Simon
     */
    private class TrashBinDropController extends SimpleDropController {
        public TrashBinDropController(Widget dropTarget) {
            super(dropTarget);
        }
        
        public void onDrop(DragContext context) {
            super.onDrop(context);
            //就是放在垃圾桶就是了
            WUF.favoriteLocation( (LocationEntry)(context.draggable));
        }

        public void onEnter(DragContext context) {
            super.onEnter(context);
        }

        public void onLeave(DragContext context) {
            super.onLeave(context);
        }

        @Override
        public void onPreviewDrop(DragContext context) throws VetoDragException {
        	super.onPreviewDrop(context);
//        	if (!bin.isWidgetEater()) 
//        	{
//        		throw new VetoDragException();
//        	}
        }
/*原本寫法    	
    	public TrashBinDropController(Widget dropTarget) {
            super(dropTarget);
        }

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
    }
    
    private class BinDropController extends SimpleDropController {

    	  private static final String CSS_DEMO_BIN_DRAGGABLE_ENGAGE = "demo-bin-draggable-engage";

    	  private Bin bin;

    	  public BinDropController(Bin bin) {
    	    super(bin);
    	    this.bin = bin;
    	  }

    	  @Override
    	  public void onDrop(DragContext context) {
    	    for (Widget widget : context.selectedWidgets) {
    	      bin.eatWidget(widget);
    	    }
    	    super.onDrop(context);
    	  }

    	  @Override
    	  public void onEnter(DragContext context) {
    	    super.onEnter(context);
    	    for (Widget widget : context.selectedWidgets) {
    	      widget.addStyleName(CSS_DEMO_BIN_DRAGGABLE_ENGAGE);
    	    }
    	    bin.setEngaged(true);
    	  }

    	  @Override
    	  public void onLeave(DragContext context) {
    	    for (Widget widget : context.selectedWidgets) {
    	      widget.removeStyleName(CSS_DEMO_BIN_DRAGGABLE_ENGAGE);
    	    }
    	    bin.setEngaged(false);
    	    super.onLeave(context);
    	  }

    	  @Override
    	  public void onPreviewDrop(DragContext context) throws VetoDragException {
    	    super.onPreviewDrop(context);
    	    if (!bin.isWidgetEater()) {
    	      throw new VetoDragException();
    	    }
    	  }
    }

}
