package tw.kayjean.ui.client.rpc;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

import tw.kayjean.ui.client.WUF;
import tw.kayjean.ui.client.model.Node;

public class CoordinateCallback implements AsyncCallback{
//delete	private GMarkerEventManager markerEventMan;
	Node avgnode;
	
	public void onFailure(final Throwable caught) {
        GWT.log("Error in CoordinateService!", caught);
        caught.printStackTrace();
//delete        doThisBeforeReturn(true);
    }
	
	public void onSuccess(final Object result) {
        
		//加入地圖上
/*		
        // Clear the map from indexes to GOverlayCollections, because we're going to fill them again!
        WUF.locPanel.clearOverlayMap();
        
		avgnode = (Node) result; 
		if (result == null) {
            GWT.log("CoordinateCallback got a null coordinate!", null);
            doThisBeforeReturn(false);
            return;
        }
		markerEventMan = new GMarkerEventManager();
		GLatLng point = new GLatLng (avgnode.x,avgnode.y);
		GMarkerOptions options = new GMarkerOptions();
		
		// Show a green icon
		options.setIcon(CustomIcon.GICON);
		options.setTitle(avgnode.name);
		
		GMarker marker = new GMarker (point, options);
		MouseClickListener clickListener = new MouseClickListener();
        markerEventMan.addOnClickListener(marker, clickListener);
        markerEventMan.addOnDblClickListener(marker, clickListener);
        
        if (WUF.mPanel.theMap.getZoom() != 16){
        	WUF.mPanel.theMap.setZoom(16);
        }
		WUF.mPanel.theMap.panTo(point);
		WUF.mPanel.theMap.addOverlay(marker);
		GWT.log("Just plotted average node", null);
		
		doThisBeforeReturn(true);
*/		
	}
	
	/**
	 * Always call this method before returning in onSuccess or onFailure
	 */
/*	
	private void doThisBeforeReturn(boolean locsExist) {
		WUF.isMakingServerCall = false;
        WUF.stopSpin();
        if (locsExist) {
        	WUF.locPanel.ensurePanelConsistency();
        	WUF.locPanel.toggleSOCheckBoxes(true);
        }
	}
*/
	
	/**
     * Click on a route marker to change the flash object being displayed
     */
/*	
    private class MouseClickListener implements GMarkerEventClickListener {

        public void onClick(GMarker marker) {
           WUF.setSWF(avgnode.name);
        }

        public void onDblClick(GMarker marker) {
            WUF.mPanel.theMap.setCenter(marker.getPoint());
            WUF.mPanel.theMap.zoomIn();
        }

    }
*/    
}
