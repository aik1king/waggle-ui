package tw.kayjean.ui.client;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

import tw.kayjean.ui.client.model.Node;

public class LocationPanel extends Composite {

    private Label title = new Label("選擇項目提供給朋友");
    private VerticalPanel routeTab = new VerticalPanel(); // 只是容器 Holds frame of routeFlow

    protected RouteFlow routeFlow = new RouteFlow(); // Holds LocationEntrys
    
    public LocationPanel() {
        title.setStyleName("tabTitle");
        routeTab.add(title);
        routeTab.add(routeFlow);
        initWidget(routeTab);
        this.setStyleName("locationPanel");
        routeFlow.setVisible(true);
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
    
    public void addLocation(final Node n ) {
    	
    	//先進行確認,是否存在,不存在才要繼續
    	if( routeFlow.checkexist( n.name ) == false ){
            // We assume that we're really going to add a location now
    		// 0 means location , 1 means favorite , 2 means error
            LocationEntry newloc = new LocationEntry( n.name , n.fullname , n.x , n.y , 0 , n.geocell );
            routeFlow.add(newloc); // Add to display
            //加入地圖的點
            if( WUF.currentselect == 0 )
            	WUF.mPanel.AddOverlay(n.y, n.x, n.name , 1 );
            open();
    	}
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
    }

    /**
     * Hides the route bar, which implicitly means that all locations are gone.
     * Thus, just to be safe, clears the data structures again.
     */
    
    public void close() {
        routeFlow.clear();
        routeFlow.setVisible(false);
        title.setText("沒有內容啦");
        //UserInfoViewController.message("There are currently no locations on the map. Please start typing one.");
    }

    /**
     * Shows the route bar, and tries pitifully to try to make it scrollable. ensures that we're on this tab, too.
     */
    private void open() {
        routeFlow.setVisible(true);
        title.setText("選擇項目提供給朋友");
    }
}