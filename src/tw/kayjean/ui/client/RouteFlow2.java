package tw.kayjean.ui.client;

import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;


//別人推薦去過什麼地方
public class RouteFlow2 extends Composite {

	//整個資料區塊位置,用這個表現
    VerticalPanel dragElements = new VerticalPanel();

    AbsolutePanel containingPanel = new AbsolutePanel();
    
    public RouteFlow2() {
    	containingPanel.setPixelSize(600, 400);
    	setWidget(containingPanel);
	    containingPanel.add(dragElements, 200, 20);
    }
    
    /**
     * Add LocationEntries via this method.
     * @param le New LocationEntry object
     */
    protected void add (LocationEntry le) {
		dragElements.add(le);
    }

    /**
     * Remove LocationEntries via this method.
     * @param le LocationEntry object to delete
     */
    protected void remove (LocationEntry le) {
        dragElements.remove(le);
    }

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
