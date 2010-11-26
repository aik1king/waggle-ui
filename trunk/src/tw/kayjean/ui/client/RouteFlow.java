package tw.kayjean.ui.client;

import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

import com.allen_sauer.gwt.dnd.client.DragContext;
import com.allen_sauer.gwt.dnd.client.DragController;
import com.allen_sauer.gwt.dnd.client.DragEndEvent;
import com.allen_sauer.gwt.dnd.client.DragHandler;
import com.allen_sauer.gwt.dnd.client.DragStartEvent;
import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.VetoDragException;
import com.allen_sauer.gwt.dnd.client.drop.AbsolutePositionDropController;
import com.allen_sauer.gwt.dnd.client.drop.VerticalPanelDropController;
import com.allen_sauer.gwt.dnd.client.drop.HorizontalPanelDropController;
import com.allen_sauer.gwt.dnd.client.drop.IndexedDropController;
import com.allen_sauer.gwt.dnd.client.drop.SimpleDropController;
import com.allen_sauer.gwt.dnd.client.drop.DropController;
import com.allen_sauer.gwt.dnd.client.PickupDragController;

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
    PickupDragController dragController;

    //friends
    private static final int COLUMNS = 2;
    private static final int ROWS = 2;
    private static final int IMAGE_HEIGHT = 58;
    private static final int IMAGE_WIDTH = 65;
    
    public RouteFlow() {
    	AbsolutePanel containingPanel = new AbsolutePanel();
    	dragController = new PickupDragController(containingPanel, false);
    	containingPanel.setPixelSize(600, 400);
    	setWidget(containingPanel);

    	Bin trashBin2 = new TrashBin(IMAGE_WIDTH, IMAGE_HEIGHT);
    	containingPanel.add(trashBin2);
    	BinDropController trashDropController = new BinDropController(trashBin2);
        dragController.registerDropController(trashDropController);

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

            Bin trashBin = new TrashBin(IMAGE_WIDTH, IMAGE_HEIGHT);
            simplePanel.setWidget( trashBin );
            BinDropController openTrashBinDropController = new BinDropController(trashBin);
            dragController.registerDropController(openTrashBinDropController);
          }
        }
        containingPanel.add(flexTable);

	    VerticalPanelDropController widgetDropController = new VerticalPanelDropController(dragElements);
	    dragController.registerDropController(widgetDropController);
	    containingPanel.add(dragElements, 200, 20);
    }

    /**
     * Add LocationEntries via this method.
     * @param le New LocationEntry object
     */
    protected void add (LocationEntry le) {
    	dragController.makeDraggable(le);
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
}
