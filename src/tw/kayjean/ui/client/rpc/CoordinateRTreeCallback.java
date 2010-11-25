package tw.kayjean.ui.client.rpc;

import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.maps.client.InfoWindow;
import com.google.gwt.maps.client.InfoWindowContent;
import com.google.gwt.maps.client.geom.LatLng;


import tw.kayjean.ui.client.WUF;
import tw.kayjean.ui.client.model.Node;

public class CoordinateRTreeCallback implements AsyncCallback{

	public void onFailure(final Throwable caught) {
		GWT.log("Error in CoordinateService!", caught);
		caught.printStackTrace();
		//        doThisBeforeReturn(true);
	}

	public void onSuccess(final Object result ) {

//		WUF.mPanel.Clear();

		List avgNodes = (List) result;
		if (result == null) {
			GWT.log("CoordinateRTreeCallback got a null coordinate!", null);
			//MainFrame.message( "找不到符合地點" );
			//doThisBeforeReturn(false);
			return;
		}
		//清除TABLE內容
//		WUF.locPanel.ClearPanelNear();
		int grade = 0;
		String POINAME = "附近景點";
		VerticalPanel list = new VerticalPanel();

		for (Iterator locIter = avgNodes.iterator(); locIter.hasNext();) {
			Node avgNode = (Node) locIter.next();
			
			if( WUF.mPanel.pointinbox(avgNode.y, avgNode.x) == true ){
				//如果節點屬於目前視窗範圍,才要加入,不然不需要加入
				
				//加入時候,應該依照數值進行排序,分數高的放上面
				if( avgNode.type == 0 )
					WUF.locPanel.addLocation(avgNode);
				else if( avgNode.type == 1 )
					WUF.favPanel.addFavorite(avgNode);
/*				
				else if( avgNode.type == 2 )
					WUF.errPanel.addError(avgNode);
				else if( avgNode.type == 3 )
					WUF.whiichPanel.addWhiich(avgNode);
*/					
			}
			
/*			
			//圖層上畫出每一個節點
			WUF.mPanel.AddOverlay(avgNode.y, avgNode.x, avgNode.name , 0 );
			final LatLng point = LatLng.newInstance(avgNode.y , avgNode.x );
			final String w = avgNode.name;
			ClickListener handler = new ClickListener() {
				public void onClick(Widget sender) {
					//應該是要打開TABLE中某個項目
					//目前先用打開視窗代替
					Label directions = new Label( w );
					
				    //directions.addClickListener(new ClickListener() {
				     // public void onClick(Widget arg0) {
				     //   System.out.println("getting data...");
				     //   WUF.inPanel.setLocString(w);
				     //   WUF.inPanel.signalToAddLoc();
				     // }
				    //});
					FlowPanel panel = new FlowPanel();
					panel.add(directions);
					InfoWindow info = WUF.mPanel.GetInfoWnd();
					info.open(point, new InfoWindowContent(panel));
				}
			};
			//文字框框中加入每一個節點
            LocationEntry newloc = new LocationEntry(w);
            routeFlow.add(newloc); // Add to display
			
			Label l = new Label(avgNode.name);
			l.addClickListener(handler);
			list.add( l );
			grade++;
*/
		}
		//各個類別分別存入自己歸屬內容
//		MainFrame.locPanel.AddPanel(list , POINAME );
//		MainFrame.message( "Grade is " + grade );
		//MainFrame.mPanel.setVisible(false);
		//MainFrame.locPanel.setVisible(true);

	}
}
