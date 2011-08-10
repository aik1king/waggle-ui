package tw.kayjean.ui.client;

import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.SimplePanel;

import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.drop.VerticalPanelDropController;

public class RouteFlow extends Composite {

	//整個資料區塊位置,用這個表現
    VerticalPanel dragElements = new VerticalPanel();
    PickupDragController dragController;

    //friends
    private static final int COLUMNS = 3;
    private static final int ROWS = 10;
    private static final int IMAGE_WIDTH = 70;
    private static final int IMAGE_HEIGHT = 85;

    
    AbsolutePanel containingPanel = new AbsolutePanel();
    private static boolean friends = false;
    
    public RouteFlow() {
    	dragController = new PickupDragController(containingPanel, false);
    	containingPanel.setPixelSize(600, 600);
    	setWidget(containingPanel);

	    VerticalPanelDropController widgetDropController = new VerticalPanelDropController(dragElements);
	    dragController.registerDropController(widgetDropController);
	    //沒什麼用處 dragElements.setSize("100px", "300px");
	    containingPanel.add(dragElements, 20, 20);
    	
	    //http://examples.roughian.com/index.htm#Panels~VerticalSplitPanel
	    
    }

    public void initfriend(){
        //friends
    	if( friends == false ){

    	//myself
    	Bin trashBin2 = new Bin(IMAGE_WIDTH, IMAGE_HEIGHT , Waggle_ui.friends.myself );
    	containingPanel.add(trashBin2 , 360 , 20 );
    	BinDropController trashDropController2 = new BinDropController(trashBin2);
        dragController.registerDropController(trashDropController2);

        //trash
	    Bin trashBin3 = new Bin(IMAGE_WIDTH, IMAGE_HEIGHT , Waggle_ui.friends.trash );
    	containingPanel.add(trashBin3 , 420 , 20 );
    	BinDropController trashDropController3 = new BinDropController(trashBin3);
        dragController.registerDropController(trashDropController3);

        //wish
	    Bin trashBin4 = new Bin(IMAGE_WIDTH, IMAGE_HEIGHT , Waggle_ui.friends.wish );
    	containingPanel.add(trashBin4 , 480 , 20 );
    	BinDropController trashDropController4 = new BinDropController(trashBin4);
        dragController.registerDropController(trashDropController4);
        
        FlexTable flexTable = new FlexTable();
        // create our grid
        int totalcount = 0;
        if( Waggle_ui.friends != null && Waggle_ui.friends.items != null )
        	totalcount = Waggle_ui.friends.items.size();
        int count = 0;
        for (int i = 0; i < COLUMNS && count < totalcount ; i++) {
          for (int j = 0; j < ROWS && count < totalcount ; j++) {
        	  
            // create a simple panel drop target for the current cell
            SimplePanel simplePanel = new SimplePanel();
            simplePanel.setPixelSize(IMAGE_WIDTH, IMAGE_HEIGHT);
            flexTable.setWidget(i, j, simplePanel);
            //flexTable.getCellFormatter().setStyleName(i, j, CSS_DEMO_PUZZLE_CELL);

            Bin friendBin = new Bin(IMAGE_WIDTH, IMAGE_HEIGHT , Waggle_ui.friends.items.get(count) );
            simplePanel.setWidget( friendBin );
            BinDropController openTrashBinDropController = new BinDropController(friendBin);
            dragController.registerDropController(openTrashBinDropController);
            
            count++;
          }
        }
        containingPanel.add(flexTable , 380 , 60 );
        friends = true;
    	}
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
