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

//顯示別人認為我最適合項目
//不需要dnd
public class FavoritePanel extends Composite {

    private Label title = new Label("別人認定我項目");
	
    private VerticalPanel routeTab = new VerticalPanel(); // Holds frame of routeFlow
    protected RouteFlow2 routeFlow2 = new RouteFlow2(); // Holds LocationEntrys

    public FavoritePanel() {
        title.setStyleName("tabTitle");
        routeTab.add(title);
        routeTab.add(routeFlow2);
        initWidget(routeTab);
        this.setStyleName("favoritePanel");
        routeFlow2.setVisible(false);
    }

    /**
     * Will add a new location to the current structures, possibly triggering an
     * auto-routing.
     * @param loc String of the location to be entered. Will be checked!
     */
    public void checkpointinbox(){
    	//table中每一個點都送入map中,檢查是否符合範圍
        LocationEntry tmpEntry;
        for (int i = routeFlow2.size() - 1 ; i >= 0 ; i--) {
            tmpEntry = routeFlow2.getEntry(i);
            if( !WUF.mPanel.pointinbox(tmpEntry.n.y, tmpEntry.n.x) ){
            	routeFlow2.remove(tmpEntry);
            	WUF.mPanel.RemoveOverlay( tmpEntry.n.name );
            }
        }
    	return;
    }
    
    public void addFavorite(final Node n ) {
    	
    	//先進行確認,是否存在,不存在才要繼續
    	if( routeFlow2.checkexist( n.name ) == false ){
            // We assume that we're really going to add a location now
    		// 0 means location , 1 means favorite , 2 means error
            LocationEntry newloc = new LocationEntry( n.name , n.fullname , n.x , n.y , 1 , n.geocell );
            routeFlow2.add(newloc); // Add to display
            //加入地圖的點
            if( WUF.currentselect == 1 )
            	WUF.mPanel.AddOverlay(n.y, n.x, n.name , 1 );
            open();
    	}
    }
    

    /**
     * Hides the route bar, which implicitly means that all locations are gone.
     * Thus, just to be safe, clears the data structures again.
     */
    public void close() {
    	routeFlow2.clear();
    	routeFlow2.setVisible(false);
        title.setText("沒有內容啦");
        //UserInfoViewController.message("There are currently no locations on the map. Please start typing one.");
    }

    /**
     * Shows the route bar, and tries pitifully to try to make it scrollable. ensures that we're on this tab, too.
     */
    private void open() {
    	routeFlow2.setVisible(true);
        title.setText("別人認定項目");
    }
}